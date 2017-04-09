package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveInchesPIDCommand extends PIDCommand {

    private double speed;
    private double distance;
    private boolean auto;

    private PIDController controller;

    public DriveInchesPIDCommand() {
        super(0.0, 0.0, 0.0);
        this.auto = true;
        requires(Robot.drivetrain);
    }

    public DriveInchesPIDCommand(double speed, double distance) {
        super(0.0, 0.0, 0.0);
        this.speed = speed;
        this.distance = distance;
        this.auto = false;
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (auto) {
            distance = SmartDashboard.getNumber("pid-drive-distance", 0.0);
            speed = SmartDashboard.getNumber("pid-drive-speed", 0.0);
        }

        controller = getPIDController();
        controller.setPID(
                SmartDashboard.getNumber("P DriveInches", 0.0),
                SmartDashboard.getNumber("I DriveInches", 0.0),
                SmartDashboard.getNumber("D DriveInches", 0.0));
        controller.reset();

        System.out.println("[DriveInchesPIDCommand#initialize()]: encoder value BEFORE reset: " + Robot.drivetrain.encoderDistance());
        Robot.drivetrain.resetEncoders();
        Robot.drivetrain.resetGyro();
        System.out.println("[DriveInchesPIDCommand#initialize()]: encoder value AFTER reset: " + Robot.drivetrain.encoderDistance());

        controller.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (Robot.oi.driverIsOverriding()) {
            Robot.stopAutoMovement.set(true);
            return;
        }
        // This should be converted to a graph (LinePlot) when testing
        SmartDashboard.putNumber("PID DriveInches OUTPUT", returnPIDInput());
        System.out.println("[DriveInchesPIDCommand#" + "execute()" + "] "
                + "\n encoderDistance: " + Robot.drivetrain.encoderDistance() 
                + "\n distance final: " + distance 
                + "\n isAutoOverriden: " + Robot.stopAutoMovement.get());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.stopAutoMovement.get() || Robot.drivetrain.encoderDistance() >= distance;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("[DriveInchesPIDCommand#end()]: END");
        Robot.drivetrain.stop();
        controller.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    @Override
    protected double returnPIDInput() {
        return Robot.drivetrain.gyroAngle();
    }

    @Override
    protected void usePIDOutput(double output) {
        Robot.drivetrain.tankDrive(speed + output, speed - output);
    }
}
