package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveUntilTimeoutCommand extends Command {

    private double motorSpeed;

    public DriveUntilTimeoutCommand(double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        motorSpeed = speed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.drivetrain.tankDrive(motorSpeed, motorSpeed);
        Robot.drivetrain.resetEncoders();
        setTimeout(SmartDashboard.getNumber("drive fwd time", 3.0));
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double speed = SmartDashboard.getNumber("drive fwd speed", 0.5);
        Robot.drivetrain.tankDrive(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return Robot.oi.driverIsOverriding() || isTimedOut();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
