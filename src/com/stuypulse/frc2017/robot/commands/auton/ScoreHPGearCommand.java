package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreHPGearCommand extends CommandGroup {
    public static final double START_TO_HP_GEAR_TURN_DISTANCE = 114.3;
    public static final double HP_GEAR_TURN_TO_HP_GEAR_ANGLE = 60;
    public static final double AFTER_TURN_TO_HP_GEAR_DISTANCE = 51;
    public static final double HP_GEAR_REVERSE_DISTANCE = -51;
    
    public  ScoreHPGearCommand() {
    	
    	int direction;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		addSequential(new DriveForwardEncodersCommand(START_TO_HP_GEAR_TURN_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * HP_GEAR_TURN_TO_HP_GEAR_ANGLE));
		
		addSequential(new OptionalCVPositionForGearCommand());
		
		addSequential(new GearTrapReleaseGearCommand());
		addSequential(new GearPusherRetractGearCommand());
		addSequential(new DriveForwardEncodersCommand(HP_GEAR_REVERSE_DISTANCE));
		addSequential(new GearTrapTrapGearCommand());
    }
}
