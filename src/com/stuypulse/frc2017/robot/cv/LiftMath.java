package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_HEIGHT;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_Y;
import static com.stuypulse.frc2017.robot.CVConstants.LIFT_TARGET_Y;

import org.opencv.core.Point;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.util.Vector;

public class LiftMath {
    /**
     * @param lift_left Position of the left edge of the lift
     * relative to the lift camera
     * @param lift_right Position of the right edge of the lift
     * relative to the lift camera
     * @param intermediate_dist Distance from the peg base (along the
     * lift's normal) which the bot should go to before rotating and
     * approaching the peg head-on.
     * @param final_dist Distance from the peg to stop at
     * @return Path along which the robot should move, expressed as an
     * array of vectors describing discrete linear movements.
     */
    public static Vector[] getPath(
            Vector lift_left,
            Vector lift_right,
            double intermediate_dist,
            double final_dist
            ) {
        // Get average distance between lift targets (approximate position of peg)
        Vector peg = lift_left.plus(lift_right).scaleBy(0.5);
        // Get distance between left and right lift strips
        Vector lift_ltr = lift_right.minus(lift_left);
        // Get vector from peg to point on our path where we stop and turn
        Vector from_peg = lift_ltr.rotateBy(-90).withMagnitude(intermediate_dist);
        // Get vector from current location to point on our path where we stop and turn
        Vector m1 = peg.plus(from_peg);
        // Get vector from m1 to where the bot will stop, right in front of the peg.
        Vector m2 = from_peg.scaleBy(-1.0).withMagnitude(intermediate_dist - final_dist);
        return new Vector[] {m1, m2};
    }

    public static double radiansToDegrees(double rads) {
    	return rads / Math.PI * 180.0;
    }

    public static double degreesToRadians(double degs) {
        return degs * Math.PI / 180.0;
    }

    /**
     * @param stripY Center y-coordinate of reflexite strip.
     * @return Distance from camera to the reflexite strip.
     */
    public static double stripYToDistance(double stripY) {
        //System.out.println("Argument to stripYToDistance is " + stripY);
    	double angle = 2*Camera.frameYPxToDegrees(stripY);
        //System.out.println("Angle to stripY is " + angle + " degrees");
        return (LIFT_TARGET_Y - CAMERA_Y) / Math.tan(Math.toRadians(angle));
        //return ((LIFT_TARGET_Y - CAMERA_Y) * CVConstants.CAMERA_FOCAL_LENGTH_X)
        //        / (stripY - (CAMERA_FRAME_PX_HEIGHT / 2) - 0.5);

    }

    public static double distance(Point first, Point second) {
        return Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
    }

    public static double heightToDistance(double imgHeight, boolean left) {
        double a = 1.0;
        double b = 0.0;
        if(left) {
            a = 1.122;//With Test Images: 1.165;
            b = 1.44;//With Test Images: 5.864;
        } else {
            a = 1.124;//With Test Images: 1.174;
            b = 1.002;//With Test Images: 5.203;
        }
        System.out.println(left + ": " + imgHeight);
        return (CVConstants.REFLEXITE_LENGTH * CVConstants.CAMERA_FOCAL_LENGTH_Y / imgHeight - b) / a;
    }

    /**
     * @param stripX Center x-coordinate of reflexite
     * @return Angle between where the camera is pointing and the reflexite stnrip.
     */
    public static double stripXToAngle(double stripX) {
        return Camera.frameXPxToDegrees(stripX);
    }

    public static Vector stripFramePosToPhysicalPos(double stripX, double imgHeight, boolean left) {
    	//System.out.println("Angle: " + stripXToAngle(stripX));
    	//System.out.println("Z: " + stripYToDistance(stripY));
        //System.out.println("Magnitude: " + stripYToDistance(stripY) / Math.cos(stripXToAngle(stripX)));
        //return Vector.fromPolar(stripXToAngle(stripX), stripYToDistance(stripY));
        return Vector.fromPolar(stripXToAngle(stripX), heightToDistance(imgHeight, left));
    }
}
