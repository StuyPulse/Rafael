package com.stuypulse.frc2017.robot.commands.cv;

import java.util.Arrays;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;
import com.stuypulse.frc2017.util.Vector;

/**
 *
 */
public class RotateToLiftCommand extends GyroRotationalCommand {

    private Vector cvReading;

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
        cvReading = Robot.liftVision.mTip_processImage();
        if (cvReading != null) {
            System.out.println(cvReading);
            desiredAngle = cvReading.getDegrees();
        } else {
            System.out.println("No reading");
            desiredAngle = 0.0;
            cancelCommand = true;
        }
    }

    @Override
    protected void onEnd() {
    }
}
