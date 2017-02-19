package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FOCAL_LENGTH_Y;
import static com.stuypulse.frc2017.robot.CVConstants.REFLEXITE_LENGTH;

import org.opencv.core.Point;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * We have three alignment routines, ordered by complexity:
 * 1, "bisect": Rotate halfway between the two reflexite strips (by the angle
 *  bisector), then drive onto the peg.
 * 2, "tip": Rotate to the *tip of the peg*, then drive forward.
 * 3, "twostep": Drive first to the perpendicular bisector of the lift, turn
 *  toward the lift, then run alignment method (1).
 *
 * The "bisect" method code works as of 2017-02-18, but is too inaccurate.
 * The "tip" method is our best bet (as of 2017-02-18).
 * The "twostep" method, while fancy and hypothetically the most reliable, may
 * be too slow anyway.
 */
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
    public static Vector[] mTwoStep_getPath(
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
        Vector from_peg = lift_ltr.rotateBy(90).withMagnitude(intermediate_dist);
        // Get vector from current location to point on our path where we stop and turn
        Vector m1 = peg.plus(from_peg);
        // Get vector from m1 to where the bot will stop, right in front of the peg.
        Vector m2 = from_peg.scaleBy(-1.0).withMagnitude(intermediate_dist - final_dist);
        return new Vector[] {m1, m2};
    }

    /**
     * For tip method (see LiftMath). Get Vector describing the movement to do
     * to score the peg: rotate by the angle of the returned Vector, then drive
     * forward that distance.
     * @param lift_left Vector to left reflexite strip
     * @param lift_right Vector to right reflexite strip
     * @return Path to follow to score the peg
     */
    public static Vector mTip_getPath(
            Vector lift_left,
            Vector lift_right) {
        // to_A variables are vectors from camera to A
        // A_to_B variables are vectors from A to B
        Vector to_peg_base = Vector.avg(lift_left, lift_right);
        Vector lift_ltr = lift_right.minus(lift_left);
        Vector peg_base_to_tip = lift_ltr.rotateBy(90).withMagnitude(CVConstants.PEG_LENGTH);
        Vector to_peg_tip = to_peg_base.plus(peg_base_to_tip);
        // We want to move past the tip of the peg, so the peg actually impales us:
        Vector to_beyond_peg_tip = to_peg_tip.withMagnitude(to_peg_tip.getMagnitude() + CVConstants.PAST_PEG_DISTANCE);
        return to_beyond_peg_tip;
    }

    /**
     * @param stripY Center y-coordinate of reflexite strip.
     * @return Distance from camera to the reflexite strip.
     */
    public static double stripYToDistance(double stripY, double stripHeight) {
        return REFLEXITE_LENGTH * CAMERA_FOCAL_LENGTH_Y / stripHeight;
        // System.out.println("Argument to stripYToDistance is " + stripY);
    }

    public static double distance(Point first, Point second) {
        return Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
    }

    // TODO: what are these magic numbers.
    public static double findCevian(double a, double b){
        return Math.sqrt((4.125 * (a * a + b * b) - 68.0625) / 8.25);
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
    public static double lawOfCosAngle(double a, double b, double c) {
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
     * @param cevian Segment from vertex
     * @return The distance towards the tip of the peg
     */
    public static double findDistanceToLift(double a, double b, double c, double cevian){
        double angle = 90 - (a + b - c);
        return lawOfCosine(CVConstants.PEG_LENGTH, cevian, angle);
    }

    /**
     *
     * @param a Distance to the tip of the peg (in inches)
     * @param angle Angle between the viewing head of the camera to the base of the peg
     * @return
     */
    public static double findAngleToLift(double a, double angle, double cevian){
        return angle + Math.toDegrees(Math.acos((a * a + cevian * cevian - CVConstants.PEG_LENGTH * CVConstants.PEG_LENGTH) / (2 * a * cevian)));
    }

    // TODO: why not just take two Vectors?
    // TODO: move to LiftMath
    /**
     * Determine the distance and angle to the peg tip, from the distances
     * and angles to each reflexite strip.
     *
     * DOES NOT WORK.
     *
     * @param lDistance Distance to the left reflexite strip
     * @param rDistance Distance to the right reflexite strip
     * @param lAngle Angle to left reflexite strip from camera heading
     * @param rAngle Angle to right reflexite strip from camera heading
     * @return
     */
    public static double[] findDistanceAndAngle(double lDistance, double rDistance, double lAngle, double rAngle) {
        double lAngleRad = Math.toRadians(Math.abs(lAngle));
        double rAngleRad = Math.toRadians(Math.abs(rAngle));
        double angleBtw = Math.toRadians(lawOfCosAngle(lDistance, rDistance, CVConstants.DISTANCE_BETWEEN_REFLEXITE));
        // Angle between wall and the reflexite strip that the camera's heading is further away from
        double a = Math.asin((lAngleRad > rAngleRad? rDistance : lDistance) * Math.sin(angleBtw) / CVConstants.DISTANCE_BETWEEN_REFLEXITE);
        double distToPegBase = Math.sqrt((Math.pow(lDistance, 2) + Math.pow(rDistance, 2)) / 2 +
                Math.pow(CVConstants.DISTANCE_BETWEEN_REFLEXITE / 2, 2));
        // Angle between the segment to the base of the peg and the lift wall
        double b = Math.asin((lAngleRad > rAngleRad? lDistance : rDistance) * Math.sin(a) / distToPegBase);
        double distToPegTip = lawOfCosine(CVConstants.PEG_LENGTH, distToPegBase, 90 - Math.toDegrees(b)) + SmartDashboard.getNumber("distance onto peg", CVConstants.PAST_PEG_DISTANCE);
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
        return new double[] {distToPegTip, avg};
    }

    /**
     * @param stripX Center x-coordinate of reflexite
     * @return Angle between where the camera is pointing and the reflexite stnrip.
     */
    public static double stripXToAngle(double stripX) {
        return Camera.frameXPxToDegrees(stripX);
    }

    public static Vector stripFramePosToPhysicalPos(double stripX, double stripY, double stripHeight){        //double imgHeight, boolean left) {
        //System.out.println("Horizontal Angle: " + stripXToAngle(stripX));
        //System.out.println("Perpendicular length: " + stripYToDistance(stripY, stripHeight));
    	//System.out.println("------------------------------------");
        return Vector.fromPolar(stripXToAngle(stripX), stripYToDistance(stripY, stripHeight));
    }
}
