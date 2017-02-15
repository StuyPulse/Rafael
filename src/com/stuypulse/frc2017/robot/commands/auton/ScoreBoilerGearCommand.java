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
public class ScoreBoilerGearCommand extends CommandGroup {
    public static final double START_TO_BOILER_GEAR_TURN_DISTANCE = 114.3;
    public static final double BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE = -60;
    public static final double AFTER_TURN_TO_BOILER_GEAR_DISTANCE = 51;
    public static final double BOILER_GEAR_REVERSE_DISTANCE = -51;
    
    public ScoreBoilerGearCommand() {
        int direction;

		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		} 
		addSequential(new DriveForwardEncodersCommand(START_TO_BOILER_GEAR_TURN_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE));
		// If use-CV was selected in SmartDashboard, align and drive forward
		// with CV; otherwise, just drive forward.
		addSequential(new OptionalCVPositionForGearCommand());
		addSequential(new GearTrapReleaseGearCommand());
		addSequential(new GearPusherRetractGearCommand());
		addSequential(new DriveForwardEncodersCommand(BOILER_GEAR_REVERSE_DISTANCE));
		addSequential(new GearTrapTrapGearCommand());
    }
}
