package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Use sensors to adjust left and right drivetrain speeds for straight driving
 */
public abstract class EncoderStraightDrivingCommand extends AutoMovementCommand {

    private static final double MIN_INCHES_TO_MOVE = 0.1;

    private static final double TOLERANCE = 3.0;

    private double initialInchesToMove; // positive is forward, negative is backward
    private boolean cancelCommand; // true when initialInchesToMove is zero or too small for us to meaningfully go

    private boolean abort;
    private boolean doneRamping;
    private double startTime;

    abstract protected double getInchesToMove();

    public EncoderStraightDrivingCommand() {
        super();
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        try {
            if (getForceStopped()) {
                System.out.println(
                        "[EncoderStraightDrivingCommand] Quitting in initialize(), because auto-movement is force-stopped.");
                return;
            }
            Robot.drivetrain.resetEncoders();

            // Set inches we want to drive
            initialInchesToMove = getInchesToMove();
            // If it is zero (or close), cancel execution
            cancelCommand = Math.abs(initialInchesToMove) < MIN_INCHES_TO_MOVE;
            abort = false;
            doneRamping = false;
            startTime = Timer.getFPGATimestamp();
            System.out.println("[EncoderStraightDrivingCommand] initialInchesToMove: " + initialInchesToMove);
        } catch (Exception e) {
            System.out.println("Error in initialize in EncoderStraightDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    private double distForMaxSpeed = 3 * 12.0;

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void inferiorExecute() {
        try {
            distForMaxSpeed = SmartDashboard.getNumber("auto-drive-dist-for-max-speed");
            double speed = 0.0;
            double inchesToGo = inchesToMove();
            if (doneRamping) {
                // Based on the one that has worked for GyroRotationalCommand
                speed = SmartDashboard.getNumber("auto-drive-base-speed", 0.45) + SmartDashboard.getNumber("auto-drive-range", 0.4) * Math.min(1.0, Math.pow(inchesToGo / distForMaxSpeed, 2));
            } else {
                double t = Timer.getFPGATimestamp() - startTime;
                final double RAMP_TIME = 1.0;
                if (t < RAMP_TIME * 0.5) {
                    speed = t * t;
                } else if (t < RAMP_TIME) {
                    speed = t * t + 4 * t - 1;
                }
                if (t > RAMP_TIME || inchesToGo < initialInchesToMove / 2) {
                    speed = 1;
                    doneRamping = true;
                }
                speed *= SmartDashboard.getNumber("auto-drive-ramp-max-speed", 0.7); // Max motor value while ramping
                speed = Math.min(1.0, speed);
            }
            speed *= Math.signum(initialInchesToMove);
            double vLeft = speed;
            double vRight = speed;
            if (Robot.straightDrivingChooser.getSelected()) {
                /*if (Robot.drivetrain.encoderDistance() > 10.0) {
                    System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    vLeft *= Robot.drivetrain.rightEncoderDistance() / Robot.drivetrain.leftEncoderDistance();
                }//*/
                if (Math.abs(Robot.drivetrain.rightEncoderDistance()
                        - Robot.drivetrain.leftEncoderDistance()) > SmartDashboard.getNumber("winne-threshold")) {
                    vLeft += SmartDashboard.getNumber("winne-scale") * Math
                            .signum(Robot.drivetrain.rightEncoderDistance() - Robot.drivetrain.leftEncoderDistance());
                }//*/
            }
            if (Robot.straightDrivingChooser.getSelected()) {
                System.out.println("speed: " + speed);
                System.out.println("  left: " + vLeft);
                System.out.println("  right: " + vRight);
            }
            Robot.drivetrain.tankDrive(vLeft, vRight);
        } catch (Exception e) {
            System.out.println("Error in execute in EncoderStraightDrivingCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (abort || cancelCommand || getForceStopped()) {
            printEndInfo("isFinished");
            return true;
        }
        return Math.abs(inchesToMove()) <= TOLERANCE;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        printEndInfo("end");
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        printEndInfo("interrupted");
        Robot.drivetrain.stop();
    }

    private double inchesToMove() {
        return initialInchesToMove - Robot.drivetrain.encoderDistance();
    }

    // Used in isFinished, end, interrupted
    private void printEndInfo(String where) {
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] tolerance: " + TOLERANCE);
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] desired inches to move: " + initialInchesToMove);
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] doneRamping: " + doneRamping);
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] cancelCommand: " + cancelCommand);
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] getForceStopped(): " + getForceStopped());
        System.out.println("[EncoderStraightDrivingCommand#" + where + "] abort: " + abort);
    }
}
