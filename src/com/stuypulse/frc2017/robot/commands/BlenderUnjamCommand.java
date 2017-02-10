package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BlenderUnjamCommand extends Command {

    public BlenderUnjamCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.blender);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //This makes the blender unjam (see the title)
    	Robot.blender.setUnjamSpeed();	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !Robot.blender.isMotorJammed();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
