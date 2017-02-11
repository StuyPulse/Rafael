package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

/**
 *
 */
public class DriveToBoilerRangeCommand extends EncoderDrivingCommand {

    public DriveToBoilerRangeCommand() {
        super(Robot.stopAutoMovement);
    }

    private double cvReading[];
    @Override
    protected void setInchesToMove() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            initialInchesToMove = cvReading[1];
        } else {
            initialInchesToMove = 0;
            cancelCommand = true;
        }
    }
}