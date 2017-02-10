package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromHPGearCommand extends CommandGroup {

	public ApproachHPFromHPGearCommand() {
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		int direction;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		
		addSequential(new RotateDegreesGyroCommand(direction * RobotMap.HP_GEAR_TO_NEUTRAL_ZONE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.HP_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
		
		
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
	}
}
