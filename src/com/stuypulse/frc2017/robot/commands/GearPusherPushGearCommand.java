package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GearPusherPushGearCommand extends Command {

	private Timer timeSinceEntry;
	private boolean isTimerRunning;

	public GearPusherPushGearCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.gearpusher);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		handleAutoGearPush();
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

	private void handleAutoGearPush() {
		if (Robot.gearpusher.isExtended() == true) {
			// If the "time is up", push the gear and reset the timer
			if (!Robot.gearpusher.irsensor.isGearDetected()) {
				if (isTimerRunning == false) {
					timeSinceEntry.start();
					isTimerRunning = true;
				}
				if (timeSinceEntry.hasPeriodPassed(RobotMap.IR_TIME_IN_MECHANISM_THRESHOLD)) {
					Robot.gearpusher.retract();
					timeSinceEntry.stop();
					timeSinceEntry.reset();
					isTimerRunning = false;
				}
			}
		} else if (Robot.gearpusher.irsensor.isGearDetected()) {
			if (isTimerRunning == false) {
				timeSinceEntry.start();
				isTimerRunning = true;
			}
			if (timeSinceEntry.hasPeriodPassed(RobotMap.IR_TIME_IN_MECHANISM_THRESHOLD)) {
				Robot.gearpusher.extend();
				timeSinceEntry.stop();
				timeSinceEntry.reset();
				isTimerRunning = false;
			}
		}
	}
}