package com.stuypulse.frc2017.robot.commands.auton;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveInchesBasicCommand {

    private double speed;
    private double inches;
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
            return Robot.drivetrain.encoderDistance() > inches;
        }
        // Otherwise we are moving backwards
        return Robot.drivetrain.encoderDistance() < inches;
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
