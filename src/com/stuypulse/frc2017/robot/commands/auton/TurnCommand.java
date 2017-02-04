package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnCommand extends Command {
    private double time;
    private double angle;
    private double speed;

    public TurnCommand(double angle, double time, double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        this.angle = angle;
        this.time = time;
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.drivetrain.resetGyro();
        setTimeout(time);
        if (angle > 0) {
            Robot.drivetrain.tankDrive(speed, -speed);
        } else {
            Robot.drivetrain.tankDrive(-speed, speed);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.gyroAngle()) >= Math.abs(angle) || isTimedOut();
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
