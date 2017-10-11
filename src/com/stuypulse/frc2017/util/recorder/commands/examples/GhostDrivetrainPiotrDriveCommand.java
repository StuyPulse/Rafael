package com.stuypulse.frc2017.util.recorder.commands.examples;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *     This is an exact copy of DriveTrainPiotrDriveCommand
 *     except all mentions of "Robot.oi" are replaced with "Robot.ghostOI"
 */

public class GhostDrivetrainPiotrDriveCommand extends Command {
    public GhostDrivetrainPiotrDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double leftJoystick = Robot.ghostOI.driverPad.getLeftY();
        double rightJoystick = Robot.ghostOI.driverPad.getRightY();

        //if robot is moving forward
        if (leftJoystick + rightJoystick >= RobotMap.PIOTR_DRIVE_MARGIN_OF_ERROR) {
            if (Robot.ghostOI.driverPad.getRawLeftTrigger()) {
                rightJoystick += RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
                leftJoystick -= RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
            }

            if (Robot.ghostOI.driverPad.getRawRightTrigger()) {
                rightJoystick -= RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
                leftJoystick += RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
            }
        } else {
            //if robot is moving backwards
            if (Robot.ghostOI.driverPad.getRawLeftTrigger()) {
                rightJoystick -= RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
                leftJoystick += RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
            }

            if (Robot.ghostOI.driverPad.getRawRightTrigger()) {
                rightJoystick += RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
                leftJoystick -= RobotMap.PIOTR_DRIVE_TURN_ADJUSTMENT;
            }
        }

        //Caps the values of left and right joystick values; between -1 and 1.
        rightJoystick = Math.min(rightJoystick, 1);
        rightJoystick = Math.max(rightJoystick, -1);
        leftJoystick = Math.min(leftJoystick, 1);
        leftJoystick = Math.max(leftJoystick, -1);

        Robot.drivetrain.tankDrive(leftJoystick, rightJoystick);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
