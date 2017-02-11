package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.EncoderDrivingCommand;

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