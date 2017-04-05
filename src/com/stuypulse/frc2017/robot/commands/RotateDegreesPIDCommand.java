package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesPIDCommand extends PIDCommand {

    private double angle;
    private boolean auto;

    private PIDController controller;

    public RotateDegreesPIDCommand() {
        super(0.0, 0.0, 0.0);
        this.auto = true;
        requires(Robot.drivetrain);
    }

    public RotateDegreesPIDCommand(double angle) {
        super(0.0, 0.0, 0.0);
        this.angle = angle;
        this.auto = false;
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (auto) {
            angle = SmartDashboard.getNumber("pid-rotate-angle", 0.0);
        }

        controller = getPIDController();
        controller.setPID(
                SmartDashboard.getNumber("kP", 0.0),
                SmartDashboard.getNumber("kI", 0.0),
                SmartDashboard.getNumber("kD", 0.0)
        );
        controller.reset();
        if (!controller.isEnabled()) {
            controller.enable();
        }

        Robot.drivetrain.resetEncoders();
        Robot.drivetrain.resetGyro();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.drivetrain.gyroAngle() >= this.angle;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.stop();
        controller.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    @Override
    protected double returnPIDInput() {
        return angle - Robot.drivetrain.gyroAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.drivetrain.tankDrive(-output, output);
    }
}
