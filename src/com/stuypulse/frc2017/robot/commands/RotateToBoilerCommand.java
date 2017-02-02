package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

/**
 *
 */
public class RotateToBoilerCommand extends GyroRotationalCommand {

    private double[] cvReading;

    @Override
    protected void setDesiredAngle() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            desiredAngle = cvReading[2];
        } else {
            desiredAngle = 0; // TODO: abort
        }
    }

    @Override
    protected void onEnd() {
    }
}