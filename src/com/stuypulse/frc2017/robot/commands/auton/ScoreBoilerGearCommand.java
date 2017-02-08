package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreBoilerGearCommand extends CommandGroup {
    
    public  ScoreBoilerGearCommand(boolean isRedAlliance) {
    	int direction;
		if (isRedAlliance) {
			direction = 1;
		} else {
			direction = -1;
		} 
		addSequential(new DriveForwardEncodersCommand(RobotMap.START_TO_BOILER_GEAR_TURN_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE * direction));
		addSequential(new DriveForwardEncodersCommand(RobotMap.AFTER_TURN_TO_BOILER_GEAR_DISTANCE));
		addSequential(new GearTrapReleaseGearCommand());
		addSequential(new GearPusherRetractGearCommand());
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_REVERSE_DISTANCE));
		addSequential(new GearTrapTrapGearCommand());
    }
}
