package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.util.Vector;

import stuyvision.VisionModule;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.gui.DoubleSliderVariable;
import stuyvision.gui.IntegerSliderVariable;

public class LiftVision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 60, 0, 255);
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
        liftCamera = Cameras.initializeCamera(RobotMap.LIFT_CAMERA_PORT);
    }

    /**
     * @return {@code double[]} containing the distance and angle to the tip of
     *         the peg
     *         or {@code null} if we failed to see the targets
     */
    public double[] processImage() {
        return processImage(false);
    }

    public double[] processImage(boolean save) {
        Vector[] targets = processImageVectors(save);
        if (targets == null) {
            return null;
        }
        double[] reading = LiftMath.findDistanceAndAngle(targets[0], targets[1]);
        System.out.println(
                "Dist to peg tip: " + reading[0] + "\nAngle: " + reading[1] + "\n------------------------------------");
        return reading;
    }

    /**
     * Take a new image and determine how we should move to
     * get to the peg, if we see it.
     * Part of the "tip" method of approaching the peg (see LiftMath).
     *
     * @return The {@code Vector} describing how we should move to the peg,
     *         or {@code null} if we could failed to see the targets.
     */
    public Vector mTip_processImage() {
        return mTip_processImage(false);
    }

    public Vector mTip_processImage(boolean save) {
        Vector[] targets = processImageVectors(save);
        if (targets == null) {
            return null;
        }
        return LiftMath.mTip_getPath(targets[0], targets[1]);
    }

    /**
     * Process an image from the camera and determine the distance and angle to
     * the lift targets, if they exist
     *
     * @return {@code Vector[]} containing {@code Vector}s to the left and right
     *         targets
     *         or {@code null} if we failed to se the targets
     */
    public Vector[] processImageVectors() {
        return processImageVectors(false);
    }

    public Vector[] processImageVectors(boolean save) {
        if (liftCamera == null) {
            initializeCamera();
        }
        Mat frame = Cameras.getImage(liftCamera);
        String date = (new Date()).toString();
        if (save) {
            Imgcodecs.imwrite("/tmp/" + date + ".png", frame);
        }
        Mat filtered = filterLift(frame);
        if (save) {
            Imgcodecs.imwrite("/tmp/" + date + "-FILTERED.png", frame);
        }
        Vector[] targets = getTargets(frame, filtered);

        frame.release();
        filtered.release();
        return targets;
    }

    private boolean firstFrame = true;

    @Override
    public void run(Mat frame) {
        Mat filtered = filterLift(frame);
        Vector[] targets = getTargets(frame, filtered);
        if (targets != null && firstFrame) {
            System.out.println("======================================================");
            System.out.println("Target 0: " + targets[0] + "\n");
            System.out.println("Target 1: " + targets[1] + "\n");
            System.out.println("Path to tip: " + LiftMath.mTip_getPath(targets[0], targets[1]) + "\n");
            firstFrame = false;
        }
        filtered.release();
    }

    /**
     * @param frame
     *            The frame containing the unfiltered image to be processed
     * @return {@code Mat} with everything but the lift targets filtered out
     */
    public Mat filterLift(Mat frame) {
        if (hasGuiApp()) {
            postImage(frame, "Original");
        }

        Mat filtered = new Mat();
        Imgproc.cvtColor(frame, filtered, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(filtered, channels);

        Imgproc.medianBlur(channels.get(0), channels.get(0), 5);

        // Filter channel by hue
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        if (hasGuiApp()) {
            postImage(channels.get(0), "Hue-Filtered Frame");
        }

        // Dilate then erode
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);

        // Filter channel by saturation
        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()),
                channels.get(1));
        if (hasGuiApp()) {
            postImage(channels.get(1), "Saturation-Filtered Frame");
        }

        // Filter channel by value
        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));
        if (hasGuiApp()) {
            postImage(channels.get(2), "Value-Filtered Frame");
        }

        // Combine the channels together using bitwise and
        Core.bitwise_and(channels.get(0), channels.get(1), filtered);
        Core.bitwise_and(filtered, channels.get(2), filtered);

        if (hasGuiApp()) {
            postImage(filtered, "Final HSV filtering");
        }

        // Release all Mats created
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
     *            The frame after filtering out the lift targets
     * @return {@code Vector[]} where the first element is the {@code Vector} to
     *         the left target, and the second is
     *         the {@code Vector} to the right target
     *         or {@code null} if we failed to see the targets
     */
    public Vector[] getTargets(Mat original, Mat filtered) {
        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(filtered, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Rect> rects = new ArrayList<Rect>();

        // Sort the contours in ascending order (by area)
        contours.sort(new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint m1, MatOfPoint m2) {
                Rect rect1 = Imgproc.boundingRect(m1);
                Rect rect2 = Imgproc.boundingRect(m2);

                if (rect1.area() < rect2.area()) {
                    return -1;
                } else if (rect2.area() > rect1.area()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        for (int i = 0; i < contours.size();) {
            // Use boundingRects to find targets
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            Rect rect = Imgproc.boundingRect(points);

            points.release();
            contour2f.release();

            double area = rect.area();
            if ((area < minGoalArea.value() || area > maxGoalArea.value())
                    || !aspectRatioThreshold(rect.width, rect.height)) {
                contours.remove(i);
            } else {
                // Sort the targets in ascending order (by area)
                int j = 0;
                while (j < rects.size() && rects.get(j).area() < area) {
                    j++;
                }
                rects.add(j, rect);
                i++;
            }
        }

        // One vision target was broken up by the peg
        if (rects.size() == 3) {
            Rect r1 = rects.get(0);
            Rect r2 = rects.get(1);

            // Combine the points from the smallest rectangles together to create a new one
            MatOfPoint p = new MatOfPoint(
                    new Point(r1.x, r1.y),
                    new Point(r1.x, r1.y + r1.height),
                    new Point(r1.x + r1.width, r1.y),
                    new Point(r1.x + r1.width, r1.y + r1.height),

                    new Point(r2.x, r2.y),
                    new Point(r2.x, r2.y + r2.height),
                    new Point(r2.x + r2.width, r2.y),
                    new Point(r2.x + r2.width, r2.y + r2.height));

            // Replace the two smallest contours with the combined one
            contours.remove(0);
            contours.remove(0);
            contours.add(p);

            // Replace the two smallest rectangles with the combined one
            Rect combined = Imgproc.boundingRect(p);
            rects.remove(0);
            rects.remove(0);
            rects.add(combined);
        }

        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            Imgproc.rectangle(drawn, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 1);
        }

        // Get Vectors to reflexite targets if we can
        Vector[] targets;
        if (contours.size() == 2) {
            sortContours(contours);
            targets = getTargetVectors(contours);
            Point center1 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(0)),
                    CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 + getCenterY(contours.get(0)));
            Point center2 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(1)),
                    CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 + getCenterY(contours.get(1)));
            if (hasGuiApp()) {
                Imgproc.circle(drawn, center1, 1, new Scalar(0, 0, 255), 2);
                Imgproc.circle(drawn, center2, 1, new Scalar(0, 0, 255), 2);

                // Draw the X and Y axes
                Imgproc.line(drawn, new Point(0, 134.5), new Point(360, 134.5), new Scalar(0, 0, 255), 1);
                Imgproc.line(drawn, new Point(179.5, 0), new Point(179.5, 270), new Scalar(0, 0, 255), 1);
                postImage(drawn, "Detected");
            }
        } else {
            targets = null;
        }

        // Release contours and Mats we have created
        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }

        hierarchy.release();
        approxCurve.release();
        drawn.release();

        return targets;
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
        double ratio = height / width;
        return minGoalRatio.value() < ratio && ratio < maxGoalRatio.value();
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The X coordinate of the left-most point in {@code points}
     */
    public double getLeftMostX(MatOfPoint points) {
        List<Point> coords = points.toList();
        double leftMostX = coords.get(0).x;
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).x < leftMostX) {
                leftMostX = coords.get(i).x;
            }
        }
        return leftMostX;
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The height of the contour in pixels
     */
    public double getHeight(MatOfPoint points) {
        List<Point> coords = points.toList();
        Point topY = coords.get(0);
        Point bottomY = coords.get(0);
        for (int i = 0; i < coords.size(); i++) {
            if (Math.abs(getLeftMostX(points) - coords.get(i).x) <= CVConstants.LIFT_HEIGHT_X_THRESHOLD) {
                if (coords.get(i).y > bottomY.y) {
                    bottomY = coords.get(i);
                } else if (coords.get(i).y < topY.y) {
                    topY = coords.get(i);
                }
            }
        }
        return LiftMath.distance(bottomY, topY);
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The width of the contour in pixels
     */
    public double getWidth(MatOfPoint points) {
        List<Point> coords = points.toList();
        double leftX = getLeftMostX(points);
        double rightX = coords.get(0).x;
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).x > rightX) {
                rightX = coords.get(i).x;
            }
        }
        return rightX - leftX;
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The X coordinate at the center of {@code points}
     */
    public double getCenterX(MatOfPoint points) {
        List<Point> coords = points.toList();
        double rightMostX = coords.get(0).x;
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).x > rightMostX) {
                rightMostX = coords.get(i).x;
            }
        }
        return (getLeftMostX(points) + rightMostX) / 2 - CVConstants.CAMERA_FRAME_PX_WIDTH / 2;
    }

    /**
     * @param points
     *            List of points representing a contour
     * @return The Y coordinate at the center of {@code points}
     */
    public double getCenterY(MatOfPoint points) {
        List<Point> coords = points.toList();
        double bottomMostY = coords.get(0).y;
        double topMostY = coords.get(0).y;
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).y > bottomMostY) {
                bottomMostY = coords.get(i).y;
            } else if (coords.get(i).y < topMostY) {
                topMostY = coords.get(i).y;
            }
        }
        return (bottomMostY + topMostY) / 2 - CVConstants.CAMERA_FRAME_PX_HEIGHT / 2;
    }

    /**
     * Sorts contours from left to right (assumes a length of 2)
     *
     * @param contours
     *            List of contours to sort
     */
    public void sortContours(ArrayList<MatOfPoint> contours) {
        if (getLeftMostX(contours.get(0)) > getLeftMostX(contours.get(1))) {
            contours.add(contours.remove(0));
        }
    }

    /**
     * @param contours
     *            List of contours
     * @return {@code Vector[]} containing the {@code Vector}s to the left and
     *         right
     */
    public Vector[] getTargetVectors(ArrayList<MatOfPoint> contours) {
        Vector rightTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(1)),
                getCenterY(contours.get(1)), getHeight(contours.get(1)));
        Vector leftTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(0)),
                getCenterY(contours.get(0)), getHeight(contours.get(0)));
        return new Vector[] { leftTarget, rightTarget };
    }

    public DeviceCaptureSource getLiftCamera() {
        return liftCamera;
    }

}
