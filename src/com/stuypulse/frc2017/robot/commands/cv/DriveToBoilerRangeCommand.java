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
        super();
    }

    @Override
    protected double getInchesToMove() {
        cvReading = Robot.boilerVision.processImage();
        if (cvReading != null) {
            return BoilerVision.getDistanceToBoiler(cvReading[1]);
        }
        return 0.0;
    }
}
