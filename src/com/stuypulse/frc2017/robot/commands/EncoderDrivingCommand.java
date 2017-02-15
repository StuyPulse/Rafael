package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.auton.AutoMovementCommand;
import com.stuypulse.frc2017.util.BoolBox;

/**
 *
 */
public abstract class EncoderDrivingCommand extends AutoMovementCommand {

    protected double initialInchesToMove; // positive is forward
    protected boolean cancelCommand; // set by subclass

    private boolean abort;

    abstract protected void setInchesToMove();

    public EncoderDrivingCommand() {
        super();
        requires(Robot.drivetrain);
    }

    public EncoderDrivingCommand(BoolBox forceStopController) {
        super(forceStopController);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            if (externallyStopped()) {
                return;
            }
            super.initialize();
            Robot.drivetrain.resetEncoders();
            initialInchesToMove = 0.0;
            cancelCommand = false;
            abort = false;
            setInchesToMove();
        } catch (Exception e) {
            System.out.println("Error in initialize in EncoderDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // max speed is 0.8 motor value
    private final static double distForMaxSpeed = 5 * 12.0;

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        try {
            super.execute();
            if (!getForceStopped()) {
                double inchesToGo = inchesToMove();
                double speed = 0.7 + 0.3 * Math.min(1.0, Math.pow(inchesToGo / distForMaxSpeed, 2));
                // The above speed calculation is based on the one that has worked for GyroRotationalCommand
                System.out.println("Inches to go: " + inchesToGo);
                speed *= Math.signum(initialInchesToMove);
                Robot.drivetrain.tankDrive(speed, speed);
            }
        } catch (Exception e) {
            System.out.println("Error in execute in EncoderDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (abort || cancelCommand || getForceStopped()) {
            return true;
        }
        return Math.abs(inchesToMove()) <= 3.0;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void interrupted() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    private double inchesToMove() {
        // Encoders only return nonnegative values
        return Math.abs(initialInchesToMove) - Robot.drivetrain.encoderDistance();
    }
}
