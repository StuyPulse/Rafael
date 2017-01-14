package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;

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

public class BoilerVision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 64,  0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 95, 0, 255);

    public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 160, 0, 255);
    public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 255, 0, 255);

    public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 75, 0, 255);
    public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 255, 0, 255);

    public DoubleSliderVariable minGoalRatio = new DoubleSliderVariable("Min Goal Ratio", 1.1, 1.0, 10.0);
    public DoubleSliderVariable maxGoalRatio = new DoubleSliderVariable("Max Goal Ratio", 5.0, 1.0, 10.0);

    public DoubleSliderVariable minGoalArea = new DoubleSliderVariable("Min Goal Area", 50.0, 0.0, 10000);
    public DoubleSliderVariable maxGoalArea = new DoubleSliderVariable("Max Goal Area", 1500.0, 0.0, 10000);

    private DeviceCaptureSource boilerCamera;

    public void initializeCamera() {
        boilerCamera = Camera.initializeCamera(RobotMap.BOILER_CAMERA_PORT);
    }

    public void processImage() {
        if (boilerCamera == null) {
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

        // Filter the hue channel
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        if (hasGuiApp()) {
            postImage(channels.get(0), "Hue-Filtered Frame");
        }

        // Dilate then erode the hue channel
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));

        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);

        // Filter the saturation channel
        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), channels.get(1));
        if (hasGuiApp()) {
            postImage(channels.get(1), "Saturation-Filtered Frame");
        }

        // Filter the value channel
        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));
        if (hasGuiApp()) {
            postImage(channels.get(2), "Value-Filtered Frame");
        }

        // Combine all the channels
        Core.bitwise_and(channels.get(0), channels.get(1), filtered);
        Core.bitwise_and(channels.get(2), filtered, filtered);

        if (hasGuiApp()) {
            postImage(filtered, "Final HSV filtering");
        }

        filterBoiler(frame, filtered);
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }
        filtered.release();
    }

    public double[] filterBoiler(Mat original, Mat filtered) {

        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Point> pointsList = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {

            // boundingRect strategy
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            Rect rect = Imgproc.boundingRect(points);
            double area = rect.area();
            if (area < minGoalArea.value() || area > maxGoalArea.value()) {
                continue;
            }
            if (!aspectRatioThreshold(rect.width, rect.height)) {
                continue;
            }
            pointsList.add(new Point(rect.x, rect.y));
            pointsList.add(new Point(rect.x + rect.width, rect.y + rect.height));

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
            points.release();
            contour2f.release();
        }

        // Draw a bounding rectangle around the two detected rectangles
        MatOfPoint p = new MatOfPoint(pointsList.toArray(new Point[pointsList.size()]));
        Rect combined = Imgproc.boundingRect(p);
        Imgproc.rectangle(drawn, new Point(combined.x, combined.y), new Point(combined.x+combined.width, combined.y+combined.height), new Scalar(0, 255, 0), 1);

        double w = drawn.width();
        double h = drawn.height();
        Point center = new Point(combined.x+combined.width/2, combined.y+combined.height/2);
        Imgproc.circle(drawn, center, 1, new Scalar(0, 0, 255), 2);
        Imgproc.line(drawn, new Point(w/2, h/2), center, new Scalar(0, 0, 255));

        double[] vector = new double[3];
        vector[0] = center.x - original.width() / 2.0;
        vector[1] = original.height() / 2.0 - center.y;
        vector[2] = Math.atan(vector[0] / vector[1]) * (180.0 / Math.PI);
        String offsets = "x: " + vector[0] + "\ny: " + vector[1] + "\nangle: " + vector[2];
        String direction = (vector[0] > 0 ? "LEFT" : "RIGHT") + " and " + (vector[1] > 0 ? "BACKWARDS" : "FORWARDS");

        if (hasGuiApp()) {
            postImage(drawn, "Detected");
        }

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }
        approxCurve.release();
        p.release();
        drawn.release();

        return vector;
    }

    public boolean aspectRatioThreshold(double width, double height) {
        // Boiler targets are always wider than they are tall
        if (height > width) {
            return false;
        }
        double ratio = width / height;
        return minGoalRatio.value() < ratio && ratio < maxGoalRatio.value();
    }
    
    public DeviceCaptureSource getBoilerCamera(){
    	return boilerCamera;
    }
}
