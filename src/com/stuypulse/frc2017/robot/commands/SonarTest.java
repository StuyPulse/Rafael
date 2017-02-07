package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SonarTest extends Command {

    public SonarTest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.sonar);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        System.out.println("One: " +  Robot.sonar.getRangeInchesOne()); // Print value of sonar one
        System.out.println("Two: " + Robot.sonar.getRangeInchesTwo()); // Print value of sonar two
        System.out.println("================================");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
