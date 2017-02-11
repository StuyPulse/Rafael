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
        super();
        // TODO: force-stopping (can be handled simply through AutoMovementCommand)
    }

    private double[] cvReading;

    protected void setDesiredAngle() {
        cvReading = Robot.liftVision.processImage();
        if (cvReading != null) {
            // TODO: FIX MATH
            desiredAngle = cvReading[0];
        } else {
            desiredAngle = 0.0;
        }
    }

    @Override
    protected void onEnd() {
    }

}
