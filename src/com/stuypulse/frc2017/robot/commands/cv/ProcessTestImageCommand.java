package com.stuypulse.frc2017.robot.commands.cv;

import java.util.Arrays;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ProcessTestImageCommand extends InstantCommand {

    public ProcessTestImageCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        double[] reading = Robot.liftVision.processImage(true);
        if (reading == null) {
            System.out.println("No reading");
        } else {
            System.out.println("Reading: " + Arrays.toString(reading));
        }
    }
}
