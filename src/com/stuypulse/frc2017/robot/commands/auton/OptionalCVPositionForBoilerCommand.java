package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForBoilerCommand;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Used in autonomous to either drive a set number of inches to the lift, if the
 * auton CV chooser on SmartDashboard was selected to not use CV in auton, or to
 * drive to the lift with CV alignment otherwise.
 */
public class OptionalCVPositionForBoilerCommand extends Command {

    private SetupForBoilerCommand cvCommand;
    private DriveInchesEncodersCommand driveCommand;

    public OptionalCVPositionForBoilerCommand(double approxDistance) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
        // We instantiate the commands in the constructor (instead of
        // initialize()), in case WPILib expects commands to be instantiated
        // early on under the hood.
        cvCommand = new SetupForBoilerCommand();
        driveCommand = new DriveInchesEncodersCommand(approxDistance);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if (Robot.cvChooser.getSelected()) {
            cvCommand.initialize();

        } else {
            driveCommand.initialize();

        }
    }

    // Called repeatedly when this Command is scheduled to rune
    @Override
    protected void execute() {
        if (Robot.cvChooser.getSelected()) {
            cvCommand.execute();
        } else {
            driveCommand.execute();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (Robot.cvChooser.getSelected()) {
            return cvCommand.isFinished();
        } else {
            return driveCommand.isFinished();
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        if (Robot.cvChooser.getSelected()) {
            cvCommand.end();
        } else {
            driveCommand.end();
        }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        if (Robot.cvChooser.getSelected()) {
            cvCommand.interrupted();
        } else {
            driveCommand.interrupted();
        }
    }
}
