package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreBoilerGearFromBoilerCommand extends CommandGroup {
	private static final double BACK_UP_FROM_BOILER_DISTANCE = -1;
	private static final double TURN_FROM_BOILER_BACK_UP = -1;
	private static final double FOWARD_FROM_BOILER_TURN = -1;
	private static final double TURN_TO_BOILER_GEAR = -1;
	private static final double FOWARD_TO_BOILER_GEAR = -1;
	
    public ScoreBoilerGearFromBoilerCommand() {
    	int direction; 
    	if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
    	
    	addSequential (new DriveForwardEncodersCommand(BACK_UP_FROM_BOILER_DISTANCE));
    	addSequential (new RotateDegreesGyroCommand(TURN_FROM_BOILER_BACK_UP * direction));
    	addSequential (new DriveForwardEncodersCommand(FOWARD_FROM_BOILER_TURN));
    	addSequential (new RotateDegreesGyroCommand(TURN_TO_BOILER_GEAR * direction));
    	addSequential (new DriveForwardEncodersCommand(FOWARD_TO_BOILER_GEAR));
		addSequential(new GearTrapReleaseGearCommand());
		addSequential(new GearPusherRetractGearCommand());
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_REVERSE_DISTANCE));
		addSequential(new GearTrapTrapGearCommand());
    }
}
