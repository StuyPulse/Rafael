package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.BOILER_CAMERA_TILT_ANGLE;
import static com.stuypulse.frc2017.robot.CVConstants.BOILER_CAMERA_Y;
import static com.stuypulse.frc2017.robot.CVConstants.BOILER_TARGET_HEIGHT;

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
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 64, 0, 255);
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

    /**
     * @return {@code double[]} containing the distance and angle to the boiler
     *         or {@code null} if we failed to see the targets
     */
    public double[] processImage() {
        if (boilerCamera == null) {
            initializeCamera();
        }

        Mat frame = Camera.getImage(boilerCamera);
        Mat filtered = filterBoiler(frame);
        double reading[] = getBoilerTargets(frame, filtered);

        frame.release();
        filtered.release();
        return reading;
    }

    public void run(Mat frame) {
        Mat filtered = filterBoiler(frame);
        getBoilerTargets(frame, filtered);
        filtered.release();
    }

    /**
     * @param frame
     *            The frame containing the unfiltered image to be processed
     * @return {@code Mat} with everything but the boiler targets filtered out
     */
    public Mat filterBoiler(Mat frame) {
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
        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()),
                channels.get(1));
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

        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }
        dilateKernel.release();
        erodeKernel.release();

        return filtered;
    }

    /**
     * @param original
     *            The original, unfiltered frame
     * @param filtered
     *            The frame after filtering out the boiler targets
     * @return {@code double[]} containing the x offset, y offset, and angle to
     *         targets (from the center of the image)
     *         or {@code null} if we failed to see the targets
     */
    public double[] getBoilerTargets(Mat original, Mat filtered) {

        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(filtered, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Point> pointsList = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {

            // boundingRect strategy
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
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

            points.release();
            contour2f.release();
        }

        // Create a bounding rectangle around the two detected rectangles
        MatOfPoint p = new MatOfPoint(pointsList.toArray(new Point[pointsList.size()]));
        Rect combined = Imgproc.boundingRect(p);
        Point center = new Point(combined.x + combined.width / 2, combined.y + combined.height / 2);

        if (hasGuiApp()) {
            double w = drawn.width();
            double h = drawn.height();
            Imgproc.rectangle(drawn, new Point(combined.x, combined.y),
                    new Point(combined.x + combined.width, combined.y + combined.height), new Scalar(0, 255, 0), 1);
            Imgproc.circle(drawn, center, 1, new Scalar(0, 0, 255), 2);
            Imgproc.line(drawn, new Point(w / 2, h / 2), center, new Scalar(0, 0, 255));
        }

        double[] vector = new double[3];
        vector[0] = center.x - original.width() / 2.0; // X offset
        vector[1] = original.height() / 2.0 - center.y; // Y offset
        vector[2] = Math.atan(vector[0] / vector[1]) * (180.0 / Math.PI); // Angle to targets
        String offsets = "x: " + vector[0] + "\ny: " + vector[1] + "\nangle: " + vector[2];
        String direction = (vector[0] > 0 ? "LEFT" : "RIGHT") + " and " + (vector[1] > 0 ? "BACKWARDS" : "FORWARDS");

        if (hasGuiApp()) {
            postImage(drawn, "Detected");
        }

        // Free all Mats created
        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }
        approxCurve.release();
        p.release();
        drawn.release();

        return vector;
    }

    /**
     * @param width
     *            The width of the object
     * @param height
     *            The height of the object
     * @return {@code true} if the aspect ratio is in range, {@code false}
     *         otherwise
     */
    public boolean aspectRatioThreshold(double width, double height) {
        // Boiler targets are always wider than they are tall
        if (height > width) {
            return false;
        }
        double ratio = width / height;
        return minGoalRatio.value() < ratio && ratio < maxGoalRatio.value();
    }

    /**
     * @param y
     *            The y coordinate of the center of the boiler target from the
     *            center of the frame
     * @return Distance from the boiler in inches
     */
    public static double getDistanceToBoiler(double y) {
        double angle = yInFrameToDegreesFromHorizon(y);
        return (BOILER_TARGET_HEIGHT - BOILER_CAMERA_Y) / Math.tan(Math.toRadians(angle));
    }

    /**
     * @param height
     *            Height of the boiler target frome the center of the frame
     * @return Vertical angle to the boiler
     */
    public static double yInFrameToDegreesFromHorizon(double height) {
        return BOILER_CAMERA_TILT_ANGLE - Camera.frameYPxToDegrees(height);
    }

    public DeviceCaptureSource getBoilerCamera() {
        return boilerCamera;
    }
}
