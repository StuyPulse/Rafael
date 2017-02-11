package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;

/**
 *
 */
public class RotateToBoilerCommand extends GyroRotationalCommand {

    private double[] cvReading;

    public RotateToBoilerCommand() {
        super(Robot.stopAutoMovement, false);
    }

    public RotateToBoilerCommand(boolean gentle) {
        super(Robot.stopAutoMovement, gentle);
    }

    public RotateToBoilerCommand(boolean gentle, double tolerance) {
        super(Robot.stopAutoMovement, gentle, tolerance);
    }

    @Override
    protected void setDesiredAngle() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            desiredAngle = cvReading[2];
        } else {
            desiredAngle = 0;
            cancelCommand = true;
        }
    }

    @Override
    protected void onEnd() {
    }
}