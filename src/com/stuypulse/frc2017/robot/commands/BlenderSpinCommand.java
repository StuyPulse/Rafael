package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BlenderSpinCommand extends Command {

    public BlenderSpinCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.blender);
        requires(Robot.ballgate);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.ballgate.open();
        Robot.blender.run();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // the motor ROTATES 

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
