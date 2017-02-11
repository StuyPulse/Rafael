package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromMiddleGearCommand extends CommandGroup {
    public static final double MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE = -90;
    public static final double MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE = 46.6;//TODO: check value
    public static final double AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE = 90;
    public static final double AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE = 362; //374- 12 inches deviation
	public ApproachHPFromMiddleGearCommand() {
		int direction;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		
		addSequential(new DriveTrainHighGearCommand());
		addSequential(new RotateDegreesGyroCommand(direction * MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE));
	}
}