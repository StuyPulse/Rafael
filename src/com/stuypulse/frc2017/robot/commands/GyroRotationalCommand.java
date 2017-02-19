package com.stuypulse.frc2017.robot.commands;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_VIEWING_ANGLE_X;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.util.BoolBox;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

/**
 * Abstract command for rotating a certain number of degrees.
 * The angle to rotate is determined at runtime during initialize,
 * by the abstract method <code>getDesiredAngle</code>
 * @author Berkow
 *
 */
public abstract class GyroRotationalCommand extends AutoMovementCommand {

    private static final double MIN_DEGREES_TO_MOVE = 0.1;

    private double desiredAngle; // positive is clockwise, negative is counterclockwise
    private boolean cancelCommand; // true when desiredAngle is zero or too small for us to meaningfully go

    private boolean abort; // When there is an error in a method

    private boolean gentleRotate;
    private double tolerance;

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
                System.out.println("[GyroRotationalCommand] Quitting in initialize(), because auto-movement is force-stopped.");
                return;
            }
            Robot.drivetrain.resetGyro();

            // Set angle we want to rotate
            desiredAngle = getDesiredAngle();
            // If it is zero (or close), cancel execution
            cancelCommand = Math.abs(desiredAngle) < MIN_DEGREES_TO_MOVE;

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
        return Math.abs(degreesToMove() / (CAMERA_VIEWING_ANGLE_X / 2));
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
                    : baseSpeed + SmartDashboard.getNumber("autorotate-gentle-range") * Math.pow(howMuchWeHaveToGo(), 2);
            // left is negative when turning left
            boolean turnLeft = degreesToMove() < 0.0;
            printInfo(speed, baseSpeed, turnLeft);
            if (turnLeft) {
                Robot.drivetrain.tankDrive(-speed, speed);
            } else {
                Robot.drivetrain.tankDrive(speed, -speed);
            }
        } catch (Exception e) {
            System.out.println("\n\n\n\n\nError in execute in RotateToAimCommand:");
            e.printStackTrace();
            abort = true; // abort command
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        try {
            // TODO: don't assign to tolerance here
            tolerance = SmartDashboard.getNumber("cv tolerance");

            printEndInfo("isFinished");

            if (abort || cancelCommand || getForceStopped()) {
                return true;
            }

            // Judgment of success:
            double degsOff = degreesToMove();
            SmartDashboard.putNumber("CV degrees off", degsOff);

            return Math.abs(degsOff) < tolerance;
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
            return true; // abort
        }
    }

    // Called once after isFinished returns true
    protected void end() {
        printEndInfo("end");
        Robot.drivetrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
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
