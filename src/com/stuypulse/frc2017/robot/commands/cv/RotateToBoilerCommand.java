package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;

/**
 *
 */
public class RotateToBoilerCommand extends GyroRotationalCommand {

    private double[] cvReading;

    public RotateToBoilerCommand() {
        super(false);
    }

    public RotateToBoilerCommand(boolean gentle) {
        super(gentle);
    }

    public RotateToBoilerCommand(boolean gentle, double tolerance) {
        super(gentle, tolerance);
    }

    @Override
    protected double getDesiredAngle() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            return cvReading[2];
        }
        return 0.0;
    }

    @Override
    protected void onEnd() {
    }
}