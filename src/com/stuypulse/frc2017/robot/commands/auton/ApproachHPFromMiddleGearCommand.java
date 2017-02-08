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
		int direction;
		if (isRedAlliance) {
			direction = 1;
		} else {
			direction = -1;
		}
		
		addSequential(new RotateDegreesGyroCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE * direction));
		addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE * direction));
		addSequential(new DriveForwardEncodersCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE));
	}
}