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

    protected void setInchesToMove() {
        cvReading = Robot.liftVision.mTip_processImage();
        if(cvReading != null) {
            initialInchesToMove = cvReading.getMagnitude();
        } else {
            initialInchesToMove = 0.0;
            cancelCommand = true;
        }
    }
}
