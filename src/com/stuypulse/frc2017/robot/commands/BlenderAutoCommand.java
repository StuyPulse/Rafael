package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;



/**
 *
 */
public class BlenderAutoCommand extends Command {

    private double motorStartTime;
    private boolean motorStarting;
    
	public BlenderAutoCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.blender);
    	requires(Robot.ballgate);
    	motorStartTime = Timer.getFPGATimestamp();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.blender.run(true);
    	Robot.ballgate.open();
    	motorStarting = true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Timer.getFPGATimestamp() - motorStartTime >= 2.0 ) { //TODO: Find proper wait delay (seconds).
    		motorStarting = false;
    	}
    	if(!motorStarting) {
    		if(Robot.blender.isJammed) { //TODO: Decide whether to use the function isJammed(), or the variable isJammed.
    			Robot.blender.run(false);
    			motorStarting = true;
    		} else {
    			Robot.blender.run(true);
    			motorStarting = true;
    		}
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballgate.close();
    	Robot.blender.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
