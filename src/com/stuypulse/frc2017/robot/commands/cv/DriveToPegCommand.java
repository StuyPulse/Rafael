package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.EncoderDrivingCommand;
import com.stuypulse.frc2017.util.Vector;

/**
 *
 */
public class DriveToPegCommand extends EncoderDrivingCommand {

    public DriveToPegCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    private double[] cvReading;

    protected void setInchesToMove() {
        cvReading = Robot.liftVision.processImage();
        if(cvReading != null) {
            initialInchesToMove = cvReading[1];
        } else {
            initialInchesToMove = 0.0; 
        }
    }
}
