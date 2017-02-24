package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
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
    public static final double START_TO_HP_GEAR_TURN_DISTANCE = 80;//114.3;
    public static final double HP_GEAR_TURN_TO_HP_GEAR_ANGLE = 60;
    public static final double AFTER_TURN_TO_HP_GEAR_DISTANCE = 19;
    public static final double HP_GEAR_REVERSE_DISTANCE = -6;

    public ScoreHPGearCommand(boolean useCV) {

        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveForwardEncodersCommand(START_TO_HP_GEAR_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * HP_GEAR_TURN_TO_HP_GEAR_ANGLE));

        if (useCV) {
            addSequential(new SetupForGearCommand());
        } else {
            addSequential(new DriveForwardEncodersCommand(AFTER_TURN_TO_HP_GEAR_DISTANCE));
        }

        addSequential(new ScoreGearCommand());
        addSequential(new DriveForwardEncodersCommand(HP_GEAR_REVERSE_DISTANCE));
        addSequential(new GearTrapTrapGearCommand());
    }
}
