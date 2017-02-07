package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;
import com.stuypulse.frc2017.util.Vector;

/**
 *
 */
public class RotateToLiftCommand extends GyroRotationalCommand {

    public RotateToLiftCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    private Vector[] cvReading;

    protected void setDesiredAngle() {
        cvReading = Robot.liftVision.processImage();
        if (cvReading != null) {
            desiredAngle = (cvReading[0].getDegrees() + cvReading[1].getDegrees()) / 2;
        } else {
            desiredAngle = 0.0;
        }
    }

    @Override
    protected void onEnd() {
        // TODO Auto-generated method stub
    }

}
