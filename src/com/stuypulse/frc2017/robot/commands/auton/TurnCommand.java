package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnCommand extends Command {
    private double angle;
    private double speed;

    public TurnCommand(double angle, double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        this.angle = angle;
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.drivetrain.resetGyro();
        if (angle > 0) {
            Robot.drivetrain.tankDrive(speed, -speed);
        } else {
            Robot.drivetrain.tankDrive(-speed, speed);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (angle >= 0) {
            return Robot.drivetrain.gyroAngle() > angle;
        }
        return Robot.drivetrain.gyroAngle() < angle;
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
        Robot.drivetrain.stop();
    }
}
