package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.util.Vector;

import stuyvision.VisionModule;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.gui.DoubleSliderVariable;
import stuyvision.gui.IntegerSliderVariable;

public class LiftVision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 60,  0, 255);
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
    
    private static double cevian = 0.0;

    public void initializeCamera() {
        liftCamera = Camera.initializeCamera(RobotMap.LIFT_CAMERA_PORT);
    }

    public double[] processImage() {
        if (liftCamera == null) {
            initializeCamera();
        }
        Mat raw = new Mat();
        Mat frame = new Mat();
        liftCamera.readSized(raw, frame);
        Vector[] targets = hsvThresholding(frame);
        double[] reading = findDistanceAndAngle(targets[0].getMagnitude(), targets[1].getMagnitude(), targets[0].getDegrees(), targets[1].getDegrees());
        return reading;
    }

    public void run(Mat frame) {
        hsvThresholding(frame);
    }

    public Vector[] hsvThresholding(Mat frame) {
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

        Vector[] targets = filterLift(frame, filtered);

        // Free all mats
        for (int i = 0; i < channels.size(); i++) {
            channels.get(i).release();
        }

        dilateKernel.release();
        erodeKernel.release();
        filtered.release();

        return targets;
    }

    public Vector[] filterLift(Mat original, Mat filtered) {

        Vector[] targets = null;

        Mat drawn = original.clone();

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(filtered, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        ArrayList<Rect> rects = new ArrayList<Rect>();
        contours.sort(new Comparator<MatOfPoint>() {
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

        for (int i = 0; i < contours.size(); i++) {
            // boundingRect strategy
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            Rect rect = Imgproc.boundingRect(points);

            points.release();
            contour2f.release();

            double area = rect.area();
            if ((area < minGoalArea.value() || area > maxGoalArea.value()) || !aspectRatioThreshold(rect.width, rect.height)) {
                contours.remove(i);
                continue;
            }
            int j = 0;
            while (j < rects.size() && rects.get(j).area() < area) {
                j++;
            }
            rects.add(j, rect);
        }

        // One vision target was broken up by the peg
        if (rects.size() == 3) {
            Rect r1 = rects.get(0);
            Rect r2 = rects.get(1);
            MatOfPoint p = new MatOfPoint(
                    new Point(r1.x, r1.y),
                    new Point(r1.x, r1.y+r1.height),
                    new Point(r1.x+r1.width, r1.y),
                    new Point(r1.x+r1.width, r1.y+r1.height),

                    new Point(r2.x, r2.y),
                    new Point(r2.x, r2.y + r2.height),
                    new Point(r2.x+r2.width, r2.y),
                    new Point(r2.x+r2.width, r2.y+r2.height));
            contours.remove(0);
            contours.remove(0);
            contours.add(p);

            Rect combined = Imgproc.boundingRect(p);
            rects.remove(0);
            rects.remove(0);
            rects.add(combined);
        }

        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            Imgproc.rectangle(drawn, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(0, 255, 0), 1);
        }

        if (contours.size() == 2) {
            sortContours(contours);
	        targets = getTargetVectors(contours);
	        Point center1 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(0)), CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 +  getCenterY(contours.get(0)));
	        Point center2 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(1)), CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 + getCenterY(contours.get(1)));
	        double height1 = getHeight(contours.get(0));
	        double height2 = getHeight(contours.get(1));
	        //System.out.println(LiftMath.stripXToAngle(getCenterX(contours.get(0))) + "\n" + LiftMath.stripXToAngle(getCenterX(contours.get(1))));
	        //System.out.println("center1: " + center1);
	        //System.out.println("center2: " + center2);
	        //System.out.println("----------------------------------");
	        Imgproc.circle(drawn, center1, 1, new Scalar(0,0,255), 2);
	        Imgproc.circle(drawn, center2, 1, new Scalar(0,0,255), 2);
	        Imgproc.line(drawn, new Point(0, 134.5), new Point(360, 134.5), new Scalar(0,0,255), 1);
	        Imgproc.line(drawn, new Point(179.5, 0), new Point(179.5, 270), new Scalar(0,0,255), 1);
	        if (hasGuiApp()) {
	            postImage(drawn, "Detected");
	        }
        }

        double[] reading = findDistanceAndAngle(targets[0].getMagnitude(), targets[1].getMagnitude(), targets[0].getDegrees(), targets[1].getDegrees());

        System.out.println("Dist to peg tip: " + reading[0] + "\nAngle: " + reading[1] + "\n------------------------------------");

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
        }

        hierarchy.release();
        approxCurve.release();
        drawn.release();

        return targets;
    }

    public boolean aspectRatioThreshold(double width, double height) {
        // Lift targets are always taller than they are wide
        // if (width > height) {
        //     return false;
        // }
        double ratio = height / width;
        return minGoalRatio.value() < ratio && ratio < maxGoalRatio.value();
    }

    public double getLeftMostX(MatOfPoint points) {
        List<Point> coords = points.toList();
        double leftMostX = coords.get(0).x;
        for(int i = 0; i < coords.size(); i++) {
            if(coords.get(i).x < leftMostX) {
                leftMostX = coords.get(i).x;
            }
        }
        return leftMostX;
    }

    public double getHeight(MatOfPoint points) {
        List<Point> coords = points.toList();
        Point topY = coords.get(0);
        Point bottomY = coords.get(0);
        for(int i = 0; i < coords.size(); i++) {
            if(Math.abs(getLeftMostX(points) - coords.get(i).x) <= CVConstants.LIFT_HEIGHT_X_THRESHOLD) {
               if(coords.get(i).y > bottomY.y) {
                   bottomY = coords.get(i);
               } else if(coords.get(i).y < topY.y) {
                   topY = coords.get(i);
               }
            }
        }
        return LiftMath.distance(bottomY, topY);
    }

    public double getWidth(MatOfPoint points) {
        List<Point> coords = points.toList();
        double leftX = getLeftMostX(points);
        double rightX = coords.get(0).x;
        for(int i = 0; i < coords.size(); i++) {
            if(coords.get(i).x > rightX) {
                rightX = coords.get(i).x;
            }
        }
        return rightX - leftX;
    }

    public double getCenterX(MatOfPoint points) {
        List<Point> coords = points.toList();
        double rightMostX = coords.get(0).x;
        for(int i = 0; i < coords.size(); i++) {
            if(coords.get(i).x > rightMostX) {
                rightMostX = coords.get(i).x;
            }
        }
        return (getLeftMostX(points) + rightMostX) / 2 - CVConstants.CAMERA_FRAME_PX_WIDTH / 2;
    }

    public double getCenterY(MatOfPoint points) {
        List<Point> coords = points.toList();
        double bottomMostY = coords.get(0).y;
        double topMostY = coords.get(0).y;
        for(int i = 0; i < coords.size(); i++) {
            if(coords.get(i).y > bottomMostY) {
                bottomMostY = coords.get(i).y;
            }
            else if(coords.get(i).y < topMostY) {
                topMostY = coords.get(i).y;
            }
        }
        return (bottomMostY + topMostY) / 2 - CVConstants.CAMERA_FRAME_PX_HEIGHT / 2;
    }

    public void sortContours(ArrayList<MatOfPoint> contours) {
        if (getLeftMostX(contours.get(0)) > getLeftMostX(contours.get(1))) {
            contours.add(contours.remove(0));
        }
    }

    public Vector[] getTargetVectors(ArrayList<MatOfPoint> contours) {
        Vector leftTarget;
        Vector rightTarget;
        rightTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(1)), getCenterY(contours.get(1)), getHeight(contours.get(1)));
        leftTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(0)), getCenterY(contours.get(0)), getHeight(contours.get(0)));
        return new Vector[] {leftTarget, rightTarget};
    }
    
    public DeviceCaptureSource getLiftCamera(){
        return liftCamera;
    }
    
    public static void findCevian(double a, double b){
    	cevian = Math.sqrt((4.125 * (a * a + b * b) - 68.0625) / 8.25);
    }

    /**
     * 
     * @param length_1
     * @param length_2
     * @param angle angle opposite of the side you trying to find (in degrees)
     * @return The length of the side you trying to find
     */
    public static double lawOfCosine(double length_1, double length_2, double angle){
    	angle = Math.toRadians(angle);
    	return Math.sqrt(length_1 * length_1 + length_2 * length_2 - 2 * length_1 * length_2 * Math.cos(angle));
    }

    /**
     * 
     * @param a Side of triangle with the desired angle adjacent
     * @param b Side of triangle with the desired angle adjacent
     * @param c Side of triangle opposite of the desired angle
     * @return The included angle between sides a and b and opposite side c (in degrees)
     */
    public double lawOfCosAngle(double a, double b, double c) {
        return Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
    }

    /**
     *
     * @param angle angle between the reflexites (in degrees)
     * @param a Length between the reflexites (in inches)
     * @param b Length to the closest reflexite (in inches)
     * @return The angle between the wall and the farthest reflexite (in degrees)
     */
    public static double findAngle(double angle, double a, double b){
    	double r_angle = Math.toRadians(angle);
    	return Math.toDegrees(Math.asin(Math.sin(r_angle) * b / a));
    }
    
    public static double findAngleToBaseOfPeg(double lAngle, double rAngle){
    	return (lAngle + rAngle) /2;
    }
    
    /**
     * 
     * @param a Angle between the viewing head of the camera to the farthest reflexite
     * @param b Angle between the wall and the farthest reflexite
     * @param c Angle between the viewing head of the camera to the base of the peg
     * @param length
     * @return The distance towards the tip of the peg
     */
    public static double findDistanceToLift(double a, double b, double c){
    	double angle = 90 - (a + b - c);
    	return lawOfCosine(CVConstants.PEG_LENGTH, cevian, angle);
    }
    
    /**
     * 
     * @param a Distance to the tip of the peg (in inches)
     * @param angle Angle between the viewing head of the camera to the base of the peg
     * @return
     */
    public static double findAngleToLift(double a, double angle){
    	return angle + Math.toDegrees(Math.acos((a * a + cevian * cevian - CVConstants.PEG_LENGTH * CVConstants.PEG_LENGTH) / (2 * a * cevian)));
    }

    public double[] findDistanceAndAngle(double lDistance, double rDistance, double lAngle, double rAngle) {
        double lAngleRad = Math.toRadians(Math.abs(180 - lAngle));
        double rAngleRad = Math.toRadians(Math.abs(180 - rAngle));
        double angleBtw = Math.toRadians(lawOfCosAngle(lDistance, rDistance, CVConstants.DISTANCE_BETWEEN_REFLEXITE));
        // Angle between wall and the reflexite strip that the camera's heading is further away from
        double a = Math.asin((lAngleRad > rAngleRad? rDistance : lDistance) * Math.sin(angleBtw) / CVConstants.DISTANCE_BETWEEN_REFLEXITE);
        double distToPegBase = Math.sqrt((Math.pow(lDistance, 2) + Math.pow(rDistance, 2)) / 2 +
                Math.pow(CVConstants.DISTANCE_BETWEEN_REFLEXITE / 2, 2));
        // Angle between the segment to the base of the peg and the lift wall
        double b = Math.asin((lAngleRad > rAngleRad? lDistance : rDistance) * Math.sin(a) / distToPegBase);
        double distToPegTip = lawOfCosine(CVConstants.PEG_LENGTH, distToPegBase, 90 - Math.toDegrees(b));
        // Angle between the segment to the base and tip of the peg
        double c = Math.asin(CVConstants.PEG_LENGTH * Math.cos(b) / distToPegTip);
        double desiredAngle;
        if((lAngleRad > rAngleRad && lDistance > rDistance) || (lAngleRad < rAngleRad && lDistance < rDistance)) {
            // Angle between wall and the reflexite strip that the camera's heading is closer to
            double d = Math.asin((lAngleRad > rAngleRad? lDistance : rDistance) * Math.sin(angleBtw) / CVConstants.DISTANCE_BETWEEN_REFLEXITE);
            desiredAngle = Math.toDegrees(c + b - d - Math.min(lAngleRad, rAngleRad));
        } else {
            desiredAngle = Math.toDegrees(Math.max(lAngleRad, rAngleRad) - Math.PI + a + b - c);
        }
        double avg = (lAngleRad > rAngleRad? -1.0 : 1.0) * Math.toDegrees((lAngleRad + rAngleRad) / 2);
        return new double[] {distToPegTip, avg}; //desiredAngle};
    }

}
