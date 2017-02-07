package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromBoilerGearCommand extends CommandGroup {

    public ApproachHPFromBoilerGearCommand() {
        // Add Commands here:
        //addSequential(new ScoreBoilerGearCommand(Robotmap.));
        addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_TO_HP_EXIT_ANGLE));//Rotates after TH
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_HP_EXIT_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_T0_HP_NEUTRAL_ZONE_ROTATE_ANGLE));
		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_HP_ENTER_DISTANCE));
    }
}
