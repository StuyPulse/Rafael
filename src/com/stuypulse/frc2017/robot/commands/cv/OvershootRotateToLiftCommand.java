package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.robot.commands.cv.RotateToLiftCommand;

/**
 *
 */
public class OvershootRotateToLiftCommand extends RotateToLiftCommand {

    public OvershootRotateToLiftCommand() {
        super(false);
    }

    public OvershootRotateToLiftCommand(boolean gentle) {
        super(gentle);
    }

    public OvershootRotateToLiftCommand(boolean gentle, double tolerance) {
        super(gentle, tolerance);
    }

    @Override
    protected double getDesiredAngle() {
        return CVConstants.ROTATE_OFFSET_ANGLE + super.getDesiredAngle();
    }
}
