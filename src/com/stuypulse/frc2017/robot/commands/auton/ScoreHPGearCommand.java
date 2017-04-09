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
public class ScoreHPGearCommand extends CommandGroup {
    public static final double START_TO_HP_GEAR_TURN_DISTANCE = 100.0;
    public static final double HP_GEAR_TURN_TO_HP_GEAR_ANGLE = 60;
    public static final double AFTER_TURN_TO_HP_GEAR_DISTANCE = 24;
    public static final double HP_GEAR_REVERSE_DISTANCE = -12;

    public ScoreHPGearCommand(boolean score) {
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveInchesPIDCommand(0.5, START_TO_HP_GEAR_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * HP_GEAR_TURN_TO_HP_GEAR_ANGLE));
        if (score) {
            addSequential(new DriveInchesPIDCommand(0.5, AFTER_TURN_TO_HP_GEAR_DISTANCE));
            addSequential(new ScoreGearCommand());
            addSequential(new DriveInchesEncodersCommand(HP_GEAR_REVERSE_DISTANCE));
            addSequential(new GearTrapTrapGearCommand());
        }
    }

}
