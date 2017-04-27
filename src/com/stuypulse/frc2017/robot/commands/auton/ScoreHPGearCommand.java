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
    public static final double START_TO_HP_GEAR_TURN_DISTANCE = 74.0;
    public static final double HP_GEAR_TURN_TO_HP_GEAR_ANGLE = 60.0;
    public static final double AFTER_TURN_TO_HP_GEAR_DISTANCE = 16.0;
    public static final double HP_GEAR_REVERSE_DISTANCE = -24.0;

    public ScoreHPGearCommand(boolean score, DriverStation.Alliance alliance) {
        int direction;
        double extra;
        if (alliance == DriverStation.Alliance.Red) {
            direction = 1;
            extra = 0.0;
        } else {
            direction = -1;
            extra = 3.0;
        }
        addSequential(new DriveInchesPIDCommand(0.5, START_TO_HP_GEAR_TURN_DISTANCE + extra));
        addSequential(new RotateDegreesGyroCommand(direction * HP_GEAR_TURN_TO_HP_GEAR_ANGLE));

        // Approach the peg
        addSequential(new DriveInchesPIDCommand(0.5, AFTER_TURN_TO_HP_GEAR_DISTANCE), 2.0);

        if (score) {
            addSequential(new ScoreGearCommand());
            addSequential(new DriveInchesEncodersCommand(HP_GEAR_REVERSE_DISTANCE), 1.5);
            addSequential(new GearTrapTrapGearCommand());

            addSequential(new RotateDegreesGyroCommand(direction * -HP_GEAR_TURN_TO_HP_GEAR_ANGLE), 1.5);
            addSequential(new DriveInchesPIDCommand(0.5, 80.0));
        }
    }

}
