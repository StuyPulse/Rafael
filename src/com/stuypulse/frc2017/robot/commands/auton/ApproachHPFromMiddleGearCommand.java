package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromMiddleGearCommand extends CommandGroup {

	public ApproachHPFromMiddleGearCommand() {
		int direction;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		
		addSequential(new DriveTrainHighGearCommand());
		addSequential(new RotateDegreesGyroCommand(direction * RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE));
	}
}