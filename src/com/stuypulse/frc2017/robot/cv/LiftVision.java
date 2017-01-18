package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.stuypulse.frc2017.robot.RobotMap;

import stuyvision.VisionModule;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.gui.DoubleSliderVariable;
import stuyvision.gui.IntegerSliderVariable;

public class LiftVision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 64,  0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 95, 0, 255);

    public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 50, 0, 255);
    public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 255, 0, 255);

    public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 60, 0, 255);
    public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 255, 0, 255);

    public DoubleSliderVariable minGoalRatio = new DoubleSliderVariable("Min Ratio", 0.5, 0.5, 10.0);
    public DoubleSliderVariable maxGoalRatio = new DoubleSliderVariable("Max Ratio", 3.0, 0.5, 10.0);

    public DoubleSliderVariable minGoalArea = new DoubleSliderVariable("Min Area", 25.0, 0.0, 10000);
    public DoubleSliderVariable maxGoalArea = new DoubleSliderVariable("Max Area", 10000.0, 0.0, 10000);

    private DeviceCaptureSource liftCamera;

    public void initializeCamera() {
    	liftCamera = Camera.initializeCamera(RobotMap.LIFT_CAMERA_PORT);
    }

    public void processImage() {
    	if (liftCamera == null) {
    		initializeCamera();
    	}
    	// TODO: Create non-GUI processing method and invoke here
    }

    public void run(Mat frame) {
        if (hasGuiApp()) {
            postImage(frame, "Original");
        }

        Mat filtered = new Mat();
        Imgproc.cvtColor(frame, filtered, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(filtered, channels);

        Imgproc.medianBlur(channels.get(0), channels.get(0), 5);

        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        if (hasGuiApp()) {
            postImage(channels.get(0), "Hue-Filtered Frame");
        }

        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);

        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), channels.get(1));
        if (hasGuiApp()) {
            postImage(channels.get(1), "Saturation-Filtered Frame");
        }

        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));
        if (hasGuiApp()) {
            postImage(channels.get(2), "Value-Filtered Frame");
        }

        Core.bitwise_and(channels.get(0), channels.get(1), filtered);
        Core.bitwise_and(filtered, channels.get(2), filtered);

        if (hasGuiApp()) {
            postImage(filtered, "Final HSV filtering");
        }

        filterLift(frame, filtered);

        // Free all mats
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }
        dilateKernel.release();
        erodeKernel.release();
        filtered.release();
    }

    public void filterLift(Mat original, Mat filtered) {

        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(filtered, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Rect> rects = new ArrayList<Rect>();

        for (Iterator<MatOfPoint> it = contours.iterator(); it.hasNext();) {
            // boundingRect strategy
            MatOfPoint2f contour2f = new MatOfPoint2f(it.next().toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            Rect rect = Imgproc.boundingRect(points);

            points.release();
            contour2f.release();

            double area = rect.area();
            if ((area < minGoalArea.value() || area > maxGoalArea.value()) || !aspectRatioThreshold(rect.width, rect.height)) {
                it.remove();
                continue;
            }
            int i = 0;
            while (i < rects.size() && rects.get(i).area() < area) {
                i++;
            }
            rects.add(i, rect);
        }

        // One vision target was broken up by the peg
        if (rects.size() == 3) {
            Rect r1 = rects.get(0);
            Rect r2 = rects.get(1);
            MatOfPoint p = new MatOfPoint(
                    new Point(r1.x, r1.y),
                    new Point(r1.x+r1.width, r1.y+r1.height),
                    new Point(r2.x, r2.y),
                    new Point(r2.x+r2.width, r2.y+r2.height));
            Rect combined = Imgproc.boundingRect(p);
            rects.remove(0);
            rects.remove(0);
            rects.add(combined);
        }

        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            Imgproc.rectangle(drawn, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0, 255, 0), 1);

            // RotatedRect strategy
            // MatOfPoint2f tmp = new MatOfPoint2f();
            // contours.get(i).convertTo(tmp, CvType.CV_32FC1);
            // RotatedRect r = Imgproc.minAreaRect(tmp);
            // double area = r.size.area();
            // if (area < minGoalArea.value() || area > maxGoalArea.value()) {
            //     continue;
            // }
            // if (!aspectRatioThreshold(r.size.width, r.size.height)) {
            //     continue;
            // }
            // Point[] points = new Point[4];
            // r.points(points);
            // for (int j = 0; j < points.length; j++) {
            //     Imgproc.line(drawn, points[j], points[(j + 1) % 4], new Scalar(0, 255, 0));
            // }
        }
        if (hasGuiApp()) {
            postImage(drawn, "Detected");
        }

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }

        hierarchy.release();
        approxCurve.release();
        drawn.release();
    }

    public boolean aspectRatioThreshold(double width, double height) {
        // Lift targets are always taller than they are wide
        // if (width > height) {
        //     return false;
        // }
        double ratio = height / width;
        return minGoalRatio.value() < ratio && ratio < maxGoalRatio.value();
    }

    public double rectAreaRatio(ArrayList<Rect> rects) {
        // Assumes that rects contains the two rectangles
        // representing the two lift vision targets
        // Returns the ratio of the areas of the two
        // rectangles from L : R
        Rect leftRect;
        Rect rightRect;
        if(rects.get(0).x < rects.get(1).x) {
            leftRect = rects.get(0);
            rightRect = rects.get(1);
        } else {
            leftRect = rects.get(1);
            rightRect = rects.get(0);
        }
        return leftRect.area() / rightRect.area();
    }
    
    public DeviceCaptureSource getLiftCamera(){
    	return liftCamera;
    }
}
