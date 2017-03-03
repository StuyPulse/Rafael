package com.stuypulse.frc2017.robot.commands;


import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BlenderTestMotorWithGamepadCommand extends Command {

    public BlenderTestMotorWithGamepadCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.blender);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double joyStickInput = Robot.oi.operatorPad.getLeftY();
        System.out.println("Joystick Blender Test Input: " + joyStickInput);
        Robot.blender.set(joyStickInput);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.oi.operatorPad.getLeftButton().get();
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("MOTOR TEST FINISHED");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
