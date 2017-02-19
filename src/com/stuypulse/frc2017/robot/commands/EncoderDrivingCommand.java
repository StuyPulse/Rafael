package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.util.BoolBox;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public abstract class EncoderDrivingCommand extends AutoMovementCommand {

    protected double initialInchesToMove; // positive is forward
    protected boolean cancelCommand; // set by subclass

    private boolean abort;
    private boolean doneRamping;
    private double startTime;

    abstract protected void setInchesToMove();

    public EncoderDrivingCommand() {
        super();
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        try {
            if (getForceStopped()) {
                System.out.println("[EncoderDrivingCommand] Quitting in initialize(), because auto-movement is force-stopped.");
                return;
            }
            Robot.drivetrain.resetEncoders();
            initialInchesToMove = 0.0;
            cancelCommand = false;
            abort = false;
            doneRamping = false;
            setInchesToMove();
            System.out.println("initialInchesToMove: " + initialInchesToMove);
        } catch (Exception e) {
            System.out.println("Error in initialize in EncoderDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // max speed is 0.8 motor value
    private final static double distForMaxSpeed = 5 * 12.0;

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void inferiorExecute() {
        try {
            double speed = 0.0;
            double inchesToGo = inchesToMove();
            if (doneRamping) {
                // Based on the one that has worked for GyroRotationalCommand
                speed = 0.45 + 0.4 * Math.min(1.0, Math.pow(inchesToGo / distForMaxSpeed, 2));
            } else {
                double t = Timer.getFPGATimestamp() - startTime;
                final double RAMP_TIME = 1.0;
                if (t < RAMP_TIME * 0.5) {
                    speed = t * t;
                } else if (t < RAMP_TIME) {
                    speed = t * t + 4 * t - 1;
                }
                if (t > RAMP_TIME || inchesToGo < 18.0) {
                    speed = 1;
                    doneRamping = true;
                }
                speed *= 0.7; // Max at 0.7 while ramping
                speed = Math.min(1.0, speed);
            }
            speed *= Math.signum(initialInchesToMove);
            Robot.drivetrain.tankDrive(speed, speed);
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
    protected void interrupted() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }

    private double inchesToMove() {
        // Encoders only return nonnegative values
        return Math.abs(initialInchesToMove) - Robot.drivetrain.encoderDistance();
    }
}
