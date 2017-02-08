package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;
import java.util.Iterator;
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
        double[] reading = null;
        // TODO: implement finding angle and distance
        //double[] reading = {findAngleToLift(), findDistanceToLift()};
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

        String s = "";
        if (contours.size() == 2) {
            sortContours(contours);
	        targets = getTargetVectors(contours);
	        Point center1 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(0)), CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 +  getCenterY(contours.get(0)));
	        Point center2 = new Point(CVConstants.CAMERA_FRAME_PX_WIDTH / 2 + getCenterX(contours.get(1)), CVConstants.CAMERA_FRAME_PX_HEIGHT / 2 + getCenterY(contours.get(1)));
	        double height1 = getHeight(contours.get(0));
	        double height2 = getHeight(contours.get(1));
	        //System.out.println(LiftMath.stripXToAngle(getCenterX(contours.get(0))) + "\n" + LiftMath.stripXToAngle(getCenterX(contours.get(1))));
	        System.out.println("center1: " + center1);
	        System.out.println("center2: " + center2);
	        System.out.println("----------------------------------");
	        Imgproc.circle(drawn, center1, 1, new Scalar(0,0,255), 2);
	        Imgproc.circle(drawn, center2, 1, new Scalar(0,0,255), 2);
	        Imgproc.line(drawn, new Point(0, 134.5), new Point(360, 134.5), new Scalar(0,0,255), 1);
	        Imgproc.line(drawn, new Point(179.5, 0), new Point(179.5, 270), new Scalar(0,0,255), 1);
	        //for(Vector v: vectors) {
	        //    s += v + "\n";
	        //}
	        s += LiftMath.heightToDistance(height1, true) + "\n";
	        s += LiftMath.heightToDistance(height2, false);
	        if (hasGuiApp()) {
	            postImage(drawn, s);
	        }
        }

        // Post vector diagram of calculated path
        /*
        if (hasGuiApp()) {
            double distance_lift_left = liftMath.get
            Vector[] vectors = LiftVectorMath.getPath(
                    lift_left,
                    lift_right,
                    intermediate_dist,
                    final_dist);
            // Using image width and height for consistency. You can actually use any dimension.
            VectorDrawer drawer = new VectorDrawer(drawn.width(), drawn.height());
            drawer.addVectors(vectors);
            Mat vectorDiagram = drawer.getImage();
            postImage(vectorDiagram, "Vector Diagram");
        }
        */

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
        //return bottomY.y - topY.y;
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
        //return topMostY - CVConstants.CAMERA_FRAME_PX_HEIGHT / 2;
    }

    public void sortContours(ArrayList<MatOfPoint> contours) {
        if(getLeftMostX(contours.get(0)) > getLeftMostX(contours.get(1))) {
            contours.add(contours.remove(0));
        }
    }

    public Vector[] getTargetVectors(ArrayList<MatOfPoint> contours) {
        Vector leftTarget;
        Vector rightTarget;
        rightTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(1)), getHeight(contours.get(0)), false);
        leftTarget = LiftMath.stripFramePosToPhysicalPos(getCenterX(contours.get(0)), getHeight(contours.get(0)), true);
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
     * @param angle: angle opposite of the side you trying to find (in degrees)
     * @return The length of the side you trying to find
     */
    public static double lawOfCosine(double length_1, double length_2, double angle){
    	angle = Math.toRadians(angle);
    	return Math.sqrt(length_1 * length_1 + length_2 * length_2 - 2 * length_1 * length_2 * Math.cos(angle));
    }
    
    /**
     * 
     * @param angle: angle between the reflexites (in degrees)
     * @param a: Length between the reflexites (in inches)
     * @param b: Length to the closest reflexite (in inches)
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
     * @param a: Angle between the viewing head of the camera to the farthest reflexite
     * @param b: Angle between the wall and the farthest reflexite
     * @param c: Angle between the viewing head of the camera to the base of the peg
     * @param length
     * @return The distance towards the tip of the peg
     */
    public static double findDistanceToLift(double a, double b, double c){
    	double angle = 90 - (a + b - c);
    	return lawOfCosine(CVConstants.PEG_LENGTH, cevian, angle);
    }
    
    /**
     * 
     * @param a: Distance to the tip of the peg (in inches)
     * @param angle: Angle between the viewing head of the camera to the base of the peg
     * @return
     */
    public static double findAngleToLift(double a, double angle){
    	return angle + Math.toDegrees(Math.acos((a * a + cevian * cevian - CVConstants.PEG_LENGTH * CVConstants.PEG_LENGTH) / (2 * a * cevian)));
    }

    public static void main(String[] args){
    	System.out.println(findAngle(45, 1, 1));
    	System.out.println(findAngle(30, 1, Math.sqrt(3)));
    }
}
