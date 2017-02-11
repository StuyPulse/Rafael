package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;

/**
 *
 */
public class RotateToLiftCommand extends GyroRotationalCommand {

    private double[] cvReading;

    public RotateToLiftCommand() {
        super(Robot.stopAutoMovement, false);
    }

    public RotateToLiftCommand(boolean gentle) {
        super(Robot.stopAutoMovement, gentle);
    }

    public RotateToLiftCommand(boolean gentle, double tolerance) {
        super(Robot.stopAutoMovement, gentle, tolerance);
    }

    protected void setDesiredAngle() {
        cvReading = Robot.liftVision.processImage();
        if (cvReading != null) {
            // TODO: FIX MATH
            desiredAngle = cvReading[0];
        } else {
            desiredAngle = 0.0;
            cancelCommand = true;
        }
    }

    @Override
    protected void onEnd() {
    }
}
