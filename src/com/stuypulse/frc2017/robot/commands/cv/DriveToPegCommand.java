package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.EncoderDrivingCommand;
import com.stuypulse.frc2017.util.Vector;

/**
 *
 */
public class DriveToPegCommand extends EncoderDrivingCommand {

    private Vector cvReading;

    public DriveToPegCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super();
    }

    protected double getInchesToMove() {
        cvReading = Robot.liftVision.mTip_processImage();
        if(cvReading != null) {
            return cvReading.getMagnitude();
        }
        return 0.0;
    }
}
