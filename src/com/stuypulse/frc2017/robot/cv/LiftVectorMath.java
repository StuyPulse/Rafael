package com.stuypulse.frc2017.robot.cv;

import com.stuypulse.frc2017.util.Vector;

public class LiftVectorMath {
    /**
     * @param lift_left Position of the left edge of the lift
     * relative to the lift camera
     * @param lift_right Position of the right edge of the lift
     * relative to the lift camera
     * @param intermediate_dist Distance from the peg base (along the
     * lift's normal) which the bot should go to before rotating and
     * approaching the peg head-on.
     * @param final_dist Distace from the peg to stop at
     * @return Path along which the robot should move, expressed as an
     * array of vectors describing discrete linear movements.
     */
    public static Vector[] getPath(
            Vector lift_left,
            Vector lift_right,
            double intermediate_dist,
            double final_dist
            ) {
        // Get average distance between pegs (approximate center of peg)
        Vector peg = lift_left.plus(lift_right).scaleBy(0.5);
        // Get visual width of lift target
        Vector lift_ltr = lift_right.minus(lift_left);
        // Get parallel displacement vector from lift target
        Vector from_peg = lift_ltr.rotateBy(-90).withMagnitude(intermediate_dist);
        // Get vector where the bot will rotate towards the target.
        Vector m1 = peg.minus(from_peg);
        // Get vector where the bot will stop, right in front of the peg.
        Vector m2 = lift_ltr.rotateBy(90).withMagnitude(intermediate_dist - final_dist);
        return new Vector[] {m1, m2};
    }

}
