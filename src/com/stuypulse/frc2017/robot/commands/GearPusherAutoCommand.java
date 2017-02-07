package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPusherAutoCommand extends Command {

    public GearPusherAutoCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.gearpusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		Robot.gearpusher.extend();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.gearpusher.gearPusherPiston.get();
    }
    

    // Called once after isFinished returns true
    protected void end() {
    		Robot.gearpusher.retract();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
