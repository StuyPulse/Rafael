package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.EncoderDrivingCommand;
import com.stuypulse.frc2017.robot.cv.BoilerVision;

/**
 *
 */
public class DriveToBoilerRangeCommand extends EncoderDrivingCommand {

    public DriveToBoilerRangeCommand() {
        super();
    }

    @Override
    protected double getInchesToMove() {
        double[] cvReading = Robot.boilerVision.processImage();
        boolean foundGoal = cvReading != null;
        Robot.cvFoundGoal = foundGoal;
        if (foundGoal) {
            return BoilerVision.getDistanceToBoiler(cvReading[1]);
        }
        return 0.0;
    }
}
