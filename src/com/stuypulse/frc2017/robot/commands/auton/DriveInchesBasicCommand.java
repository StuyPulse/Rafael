package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesBasicCommand extends Command {

    private final double speed;
    private final double inches;

    // TODO: once encoder issues are resolved `encodersInitial` should not
    // be necessary (issue is encoders not properly reseting)
    private double encodersInitial;

    // NOTE: speed must be negative if inches is negative
    public DriveInchesBasicCommand(double speed, double inches) {
        this.speed = speed;
        this.inches = inches;
        encodersInitial = 0.0; // set in intialize()
        requires(Robot.drivetrain);
    }

    @Override
    public void initialize() {
        Robot.drivetrain.tankDrive(speed, speed);
        encodersInitial = Robot.drivetrain.encoderDistance();
    } 

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        if (inches >= 0) {
            return Robot.drivetrain.encoderDistance() - encodersInitial > inches;
        }
        // Otherwise we are moving backwards
        return Robot.drivetrain.encoderDistance() - encodersInitial < inches;
    }

    @Override
    public void end() {
        Robot.drivetrain.stop();
    }

    @Override
    public void interrupted() {
        Robot.drivetrain.stop();
    }
}
