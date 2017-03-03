package com.stuypulse.frc2017.robot.commands;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_VIEWING_ANGLE_X;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

/**
 * Abstract command for rotating a certain number of degrees.
 * The angle to rotate is determined at runtime during initialize,
 * by the abstract method <code>getDesiredAngle</code>
 */
public abstract class GyroRotationalCommand extends AutoMovementCommand {

    private static final double MIN_DEGREES_TO_MOVE = 0.1;

    private double desiredAngle; // positive is clockwise, negative is counterclockwise
    private boolean cancelCommand; // true when desiredAngle is zero or too small for us to meaningfully go

    private boolean abort; // When there is an error in a method

    private int onTargetCounter;
    private boolean gentleRotate;
    private double tolerance;

    private double lastMotorValue;

    private boolean useSignalLights;

    public GyroRotationalCommand() {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        tolerance = 1.0;
    }

    public GyroRotationalCommand(boolean gentle) {
        super();
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        tolerance = 1.0;
    }

    public GyroRotationalCommand(boolean gentle, double tolerance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        this.tolerance = tolerance;
    }

    public void setUseSignalLights(boolean use) {
        useSignalLights = use;
    }

    protected abstract double getDesiredAngle();

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        try {
            // If we received a forceStoppedBox controller and it is already
            // true, stop immediately.
            if (getForceStopped()) {
                System.out.println(
                        "[GyroRotationalCommand] Quitting in initialize(), because auto-movement is force-stopped.");
                return;
            }
            Robot.drivetrain.resetGyro();

            lastMotorValue = 0.0;

            // Set angle we want to rotate
            desiredAngle = getDesiredAngle();
            onTargetCounter = 0;
            // If it is zero (or close), cancel execution
            cancelCommand = Math.abs(desiredAngle) < SmartDashboard.getNumber("autorotate-min-degrees");

            abort = false;

            System.out.println("[GyroRotationalCommand] desiredAngle: " + desiredAngle);
        } catch (Exception e) {
            System.out.println("Error in intialize in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
        }
    }

    // INCREASE these if it is OVERshooting
    // DECREASE these if it is UNDERshooting
    private double TUNE_FACTOR = 1;//.1;
    private double TUNE_OFFSET = 0.0;

    private double angleMoved() {
        double gyro = Robot.drivetrain.gyroAngle();
        // TODO: the gyro > 180 condition accounted for
        // an issue with the old gyro. If the navX reliably records
        // the angle *delta*, this will only cause problems
        // when desiredAngle > 180.
        if (gyro > 180) {
            return gyro - 360;
        }
        return gyro * TUNE_FACTOR + TUNE_OFFSET;
    }

    private double degreesToMove() {
        return desiredAngle - angleMoved();
    }

    private double howMuchWeHaveToGo() {
        // Used for ramping
        return Math.abs(degreesToMove() / (SmartDashboard.getNumber("autorotate-woah-degrees", 8)));
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void inferiorExecute() {
        try {
            double baseSpeed = gentleRotate
                    ? SmartDashboard.getNumber("autorotate-gentle-speed")
                    : SmartDashboard.getNumber("autorotate-speed");
            double speed = gentleRotate
                    ? baseSpeed + SmartDashboard.getNumber("autorotate-range") * Math.pow(howMuchWeHaveToGo(), 2)
                    : baseSpeed
                            + SmartDashboard.getNumber("autorotate-gentle-range") * Math.pow(howMuchWeHaveToGo(), 2);
            boolean turnLeft = degreesToMove() < 0.0;

            if (Robot.drivetrain.avgAbsEncoderSpeed() < SmartDashboard.getNumber("autorotate-stall-speed-threshold")) {
                System.out.println("STALL (speed " + Robot.drivetrain.avgAbsEncoderSpeed() + ") motor value originally "+ speed);
                speed += 0.1;
            }

            speed = Math.min(1.0, speed);
            printInfo(speed, baseSpeed, turnLeft);
            // left is negative when turning left
            if (turnLeft) {
                Robot.drivetrain.tankDrive(-speed, speed);
            } else {
                Robot.drivetrain.tankDrive(speed, -speed);
            }
            lastMotorValue = speed;
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nError in execute in RotateToAimCommand:");
            e.printStackTrace();
            abort = true; // abort command
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        try {
            // TODO: don't assign to tolerance here
            tolerance = SmartDashboard.getNumber("cv tolerance") + SmartDashboard.getNumber("tolerance-vary-scalar") * lastMotorValue;

            if (abort || cancelCommand || getForceStopped()) {
                printEndInfo("isFinished");
                return true;
            }

            // Judgment of success:
            double degsOff = degreesToMove();
            SmartDashboard.putNumber("CV degrees off", degsOff);

            boolean onTarget = Math.abs(degsOff) < tolerance;
            if (onTarget) {
                onTargetCounter++;
            } else {
                onTargetCounter = 0;
            }
            return onTargetCounter >= SmartDashboard.getNumber("autorotate-counter-threshold", 3);
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
            return true; // abort
        }
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

    // Used in inferiorExecute
    private void printInfo(double speed, double baseSpeed, boolean left) {
        String s = "";
        s += "====== GyroRotationalCommand ======\n";
        s += "| rotating: " + desiredAngle + "degrees\n";
        s += "| already rotated: " + angleMoved() + "\n";
        s += "| still need to rotate: " + degreesToMove() + "\n";
        s += "| Turning " + (left ? "LEFT" : "RIGHT") + "\n";
        s += "| With absolute motor value: " + speed + "\n";
        // Potentially useful information:
        s += "| ---------------------------------\n";
        double h = howMuchWeHaveToGo();
        s += "| howMuchWeHaveToGo(): " + h + "\n";
        s += "| howMuchWeHaveToGo() squared: " + (h * h) + "\n";
        s += "| base motor value: " + String.format("%.2f", baseSpeed) + "\n";
        s += "| extra motor value: " + String.format("%.2f", speed - baseSpeed) + "\n";
        s += "===================================\n";
        System.out.print(s);
    }

    // Used in isFinished, end, interrupted
    private void printEndInfo(String where) {
        System.out.println("[GyroRotationalCommand#" + where + "] tolerance: " + tolerance);
        System.out.println("[GyroRotationalCommand#" + where + "] degrees off: " + degreesToMove());
        System.out.println("[GyroRotationalCommand#" + where + "] desired angle: " + desiredAngle);
        System.out.println("[GyroRotationalCommand#" + where + "] cancelCommand: " + cancelCommand);
        System.out.println("[GyroRotationalCommand#" + where + "] getForceStopped(): " + getForceStopped());
        System.out.println("[GyroRotationalCommand#" + where + "] abort: " + abort);
    }
}
