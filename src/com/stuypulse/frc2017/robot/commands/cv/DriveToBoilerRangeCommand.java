package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.EncoderDrivingCommand;
import com.stuypulse.frc2017.robot.cv.BoilerVision;

/**
 *
 */
public class DriveToBoilerRangeCommand extends EncoderDrivingCommand {

    private double cvReading[];

    public DriveToBoilerRangeCommand() {
        super(Robot.stopAutoMovement);
    }

    @Override
    protected void setInchesToMove() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            initialInchesToMove = BoilerVision.getDistanceToBoiler(cvReading[1]);
        } else {
            initialInchesToMove = 0;
            cancelCommand = true;
        }
    }
}
