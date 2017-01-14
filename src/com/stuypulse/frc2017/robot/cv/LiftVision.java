package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;
import stuyvision.gui.DoubleSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class LiftVision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 64,  0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 95, 0, 255);

    public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 50, 0, 255);
    public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 255, 0, 255);

    public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 60, 0, 255);
    public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 255, 0, 255);

    public DoubleSliderVariable minGoalRatio = new DoubleSliderVariable("Min Ratio", 1.0, 1.0, 10.0);
    public DoubleSliderVariable maxGoalRatio = new DoubleSliderVariable("Max Ratio", 3.0, 1.0, 10.0);

    public DoubleSliderVariable minGoalArea = new DoubleSliderVariable("Min Area", 50.0, 0.0, 10000);
    public DoubleSliderVariable maxGoalArea = new DoubleSliderVariable("Max Area", 10000.0, 0.0, 10000);

    public void run(Mat frame) {
        postImage(frame, "Original");

        Mat filtered = new Mat();
        Imgproc.cvtColor(frame, filtered, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(filtered, channels);

        Imgproc.medianBlur(channels.get(0), channels.get(0), 5);

        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        postImage(channels.get(0), "Hue-Filtered Frame");

        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);

        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), channels.get(1));
        postImage(channels.get(1), "Saturation-Filtered Frame");

        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));
        postImage(channels.get(2), "Value-Filtered Frame");

        Core.bitwise_and(channels.get(0), channels.get(1), filtered);
        Core.bitwise_and(filtered, channels.get(2), filtered);

        postImage(filtered, "Final HSV filtering");

        filterGear(frame, filtered);
    }

    public void filterGear(Mat original, Mat filtered) {

        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();

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
        postImage(drawn, "Detected");
    }

    public boolean aspectRatioThreshold(double width, double height) {
        // Gear targets are always taller than they are wider
        if (width > height) {
            return false;
        }
        double ratio = width / height;
        return (minGoalRatio.value() < ratio && ratio < maxGoalRatio.value())
                || (1 / maxGoalRatio.value() < ratio && ratio < 1 / minGoalRatio.value());
    }
}
