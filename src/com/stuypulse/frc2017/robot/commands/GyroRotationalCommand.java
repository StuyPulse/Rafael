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

    protected double desiredAngle;
    protected boolean cancelCommand;

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

    public GyroRotationalCommand(BoolBox forceStopBox, boolean gentle) {
        super(forceStopBox);
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

    public GyroRotationalCommand(BoolBox forceStopBox, boolean gentle, double tolerance) {
        super(forceStopBox);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
        gentleRotate = gentle;
        this.tolerance = tolerance;
    }

    public void setUseSignalLights(boolean use) {
        useSignalLights = use;
    }

    protected abstract void setDesiredAngle();

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        try {
            // If we received a forceStoppedBox controller and it is already
            // true, stop immediately.
            if (externallyStopped()) {
                return;
            }
            super.initialize();
            abort = false;
            cancelCommand = false;
            Robot.drivetrain.resetGyro();

            // Set defaults for values accessible by setDesiredAngle
            desiredAngle = 0.0;
            setDesiredAngle();
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
    protected void execute() {
        try {
            super.execute();
            if (!getForceStopped()) {
                double speed = gentleRotate
                        ? SmartDashboard.getNumber("autorotate-speed", 0.35) + 0.3 * Math.pow(howMuchWeHaveToGo(), 2)
                        : SmartDashboard.getNumber("autorotate-speed", 0.35) + 0.3 * Math.pow(howMuchWeHaveToGo(), 2);
                System.out.println("\n\n\n\n\n\n\nSpeed to use:\t" + speed);
                System.out.println("getGyroAngle():\t" + Robot.drivetrain.gyroAngle());
                System.out.println("angleMoved():\t" + angleMoved());
                System.out.println("desiredAngle:\t" + desiredAngle);
                System.out.println("degreesToMove():\t" + degreesToMove());
                // right is negative when turning right
                if (degreesToMove() < 0) {
                    System.out.println("\nMoving left, as degreesToMove()=" + desiredAngle + " < 0");
                    System.out.println("So: tankDrive(" + -speed + ", " + speed + ")\n");
                    Robot.drivetrain.tankDrive(-speed, speed);
                } else {
                    System.out.println("\nMoving RIGHT, as degreesToMove()=" + desiredAngle + " > 0");
                    System.out.println("So: tankDrive(" + speed + ", " + -speed + ")\n");
                    Robot.drivetrain.tankDrive(speed, -speed);
                }
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
            if (cancelCommand || getForceStopped()) {
                return true;
            }

            // When no more can or should be done:
            if (abort || Math.abs(desiredAngle) < 0.001) {
                // The last condition above is *not* the judgment of whether aiming has
                // succeeded; it is a failsafe for cases in which desiredAngle is 0
                System.out.println("\n\n\n\n\n\n\ndesiredAngle: " + desiredAngle);
                return true;
            }

            // Judgment of success:
            double degsOff = degreesToMove();
            SmartDashboard.putNumber("CV degrees off", degsOff);

            boolean onTarget = Math.abs(degsOff) < tolerance;
            System.out.println("degsOff: " + degsOff + "\nonTarget: " + onTarget);

            return onTarget;
        } catch (Exception e) {
            System.out.println("Error in isFinished in RotateToAimCommand:");
            e.printStackTrace();
            abort = true;
            return true; // abort
        }
    }

    abstract protected void onEnd();

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.stop();
        System.out.println("ENDED");
        onEnd();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.tankDrive(0.0, 0.0);
    }
}