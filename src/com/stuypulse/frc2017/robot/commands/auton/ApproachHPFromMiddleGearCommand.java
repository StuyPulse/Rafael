package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromMiddleGearCommand extends CommandGroup {

	public ApproachHPFromMiddleGearCommand(boolean isRedAlliance) {
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.
		if (isRedAlliance) {
			addSequential(new RotateDegreesGyroCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE));
			addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE));
			addSequential(new RotateDegreesGyroCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE));
			addSequential(new DriveForwardEncodersCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE));
		} else {
			addSequential(new RotateDegreesGyroCommand(-1 * RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE));
			addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE));
			addSequential(new RotateDegreesGyroCommand(-1 * RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE));
			addSequential(new DriveForwardEncodersCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE));
		}
	}
}