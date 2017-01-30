package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForwardCommand extends Command {

	private double distance;
	private double time;
	private double speed;
	
	private double startTime;

    public DriveForwardCommand(double distance, double time, double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        this.distance = distance;
        this.time = time;
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    	Robot.drivetrain.tankDrive(speed, speed);
    	Robot.drivetrain.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.drivetrain.encoderDistance() >= distance
        		|| Timer.getFPGATimestamp() - startTime >= time);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
