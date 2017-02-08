package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromBoilerGearCommand extends CommandGroup {

	public ApproachHPFromBoilerGearCommand(boolean isRedAlliance) {
		double direction;
		if (isRedAlliance) {
			direction = 1.0;
		} else {
			direction = -1.0;
		}
		addSequential(new RotateDegreesGyroCommand(direction * RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction *  RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(-1.0* direction *  RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE));
	//we are negating the previous degree angle so that the robot is in a direction facing the HP, opposite of its previous, better for the driver during telly-op
	}
}
