package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OptionalCVPositionForGearCommand extends Command {
	// create both commands in constructor in case command framwwork requires
	private SetupForGearCommand cvCommand;
	private DriveForwardEncodersCommand driveCommand;

	public OptionalCVPositionForGearCommand() {
		requires(Robot.drivetrain);
		cvCommand = new SetupForGearCommand();
		driveCommand = new DriveForwardEncodersCommand(ScoreHPGearCommand.AFTER_TURN_TO_HP_GEAR_DISTANCE);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (Robot.cvChooser.getSelected()) {
			cvCommand.initialize();

		} else {
			driveCommand.initialize();

		}
	}

	}

	// Called repeatedly when this Command is scheduled to rune
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
