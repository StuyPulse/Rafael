package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;

/**
 *
 */
public class RotateToBoilerCommand extends GyroRotationalCommand {

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
        double[] cvReading = Robot.boilerVision.processImage();
        boolean foundGoal = cvReading != null;
        Robot.cvFoundGoal = foundGoal;
        if (foundGoal) {
            return cvReading[2];
        }
        return 0.0;
    }
}
