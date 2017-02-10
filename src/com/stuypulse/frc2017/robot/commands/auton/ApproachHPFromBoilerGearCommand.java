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
public class ApproachHPFromBoilerGearCommand extends CommandGroup {

	public ApproachHPFromBoilerGearCommand() {
		int direction;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		addSequential(new DriveTrainHighGearCommand());
        addSequential(new RotateDegreesGyroCommand((direction * RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE)));
    	addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
    	addSequential(new RotateDegreesGyroCommand(direction * RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE));
    	addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
	}
}