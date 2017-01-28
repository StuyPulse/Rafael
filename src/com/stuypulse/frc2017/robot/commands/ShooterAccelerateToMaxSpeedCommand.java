package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterAccelerateToMaxSpeedCommand extends Command {

    public ShooterAccelerateToMaxSpeedCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	requires (Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setSpeed(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//none
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
