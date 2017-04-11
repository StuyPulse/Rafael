package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */

public class ShooterAcceleratePIDOldCommand extends PIDCommand {

    private double speed;
    private boolean auto;

    public ShooterAcceleratePIDOldCommand(double speed) {
        super(0.0,0.0,0.0);
        this.speed = speed;
        requires(Robot.shooter);
    }

    public ShooterAcceleratePIDOldCommand() {
        this(0.0);
        auto = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (auto) {
            speed = SmartDashboard.getNumber("pid-shooter-speed",0.0);
        }

        PIDController controller = getPIDController();
        controller.setPID(
                SmartDashboard.getNumber("P ShooterAccelerate", 0.0),
                SmartDashboard.getNumber("I ShooterAccelerate", 0.0),
                SmartDashboard.getNumber("D ShooterAccelerate", 0.0)
                );
        controller.reset();
        if (!controller.isEnabled())
            controller.enable();

        Robot.shooter.stop();
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // This should be converted to a graph (LinePlot) when testing
        SmartDashboard.putNumber("PID ShooterAccelerate OUTPUT", returnPIDInput());
    }

    // Only stop when interrupted!
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.shooter.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    @Override
    protected double returnPIDInput() {
        // TODO Auto-generated method stub
        return (Robot.shooter.getSpeed() - speed);
    }

    @Override
    protected void usePIDOutput(double output) {
        // TODO Auto-generated method stub
        Robot.shooter.setSpeed(output);
    }
}
