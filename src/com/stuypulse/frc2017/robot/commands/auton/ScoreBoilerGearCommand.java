package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveInchesPIDCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */

public class ScoreBoilerGearCommand extends CommandGroup {
    public static final double START_TO_BOILER_GEAR_TURN_DISTANCE = 100.0;
    public static final double BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE = -60;
    public static final double AFTER_TURN_TO_BOILER_GEAR_DISTANCE = 24;
    public static final double BOILER_GEAR_REVERSE_DISTANCE = -12;

    public ScoreBoilerGearCommand(boolean score) {
        addSequential(new DriveInchesPIDCommand(0.5, START_TO_BOILER_GEAR_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE, true));

        // Approach the peg
        addSequential(new DriveInchesPIDCommand(0.5, AFTER_TURN_TO_BOILER_GEAR_DISTANCE));

        if (score) {
            addSequential(new ScoreGearCommand());
            addSequential(new DriveInchesEncodersCommand(BOILER_GEAR_REVERSE_DISTANCE));
            addSequential(new GearTrapTrapGearCommand());
        }
    }

}
