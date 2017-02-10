package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;



/**
 *
 */
public class BlenderRunWithUnjammingCommand extends Command {

    private double motorUnjamTime;
    private boolean motorIsUnjamming;
    
	public BlenderRunWithUnjammingCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.blender);
    	requires(Robot.ballgate);
    	motorUnjamTime = Timer.getFPGATimestamp();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.blender.run();
    	Robot.ballgate.open();
    	motorIsUnjamming = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Timer.getFPGATimestamp() - motorUnjamTime >= RobotMap.BLENDER_MOTOR_UNJAM_TIME) {
    		motorIsUnjamming = false;
    	}
        if (!motorIsUnjamming) {
            if (Robot.blender.isMotorJammed()) {
    			Robot.blender.run();
    			motorIsUnjamming = false;
    		} else {
    			Robot.blender.setUnjamSpeed();
    			motorIsUnjamming = true;
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
