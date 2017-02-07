package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainTankDriveCommand extends Command {

    public DrivetrainTankDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double leftJoystick = Robot.oi.driverPad.getLeftY();
    	double rightJoystick = Robot.oi.driverPad.getRightY();
    	Robot.drivetrain.tankDrive(leftJoystick, rightJoystick);
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
