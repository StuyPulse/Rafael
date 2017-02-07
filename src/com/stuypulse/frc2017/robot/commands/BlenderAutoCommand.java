package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;



/**
 *
 */
public class BlenderAutoCommand extends Command {

    public BlenderAutoCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.blender);
    	requires(Robot.ballgate);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.blender.run(true);
    	Robot.ballgate.open();
    	Timer wait = new Timer();
    	//TODO: Find proper wait delay (seconds).
    	wait.delay(2);
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.blender.isJammed) {
    		Robot.blender.run(false);
    	} else {
    		Robot.blender.run(true);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballgate.close();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
