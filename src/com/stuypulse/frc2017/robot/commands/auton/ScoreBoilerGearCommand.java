package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;
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
    public static final double START_TO_BOILER_GEAR_TURN_DISTANCE = 74.0;
    public static final double BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE = -60.0;
    public static final double AFTER_TURN_TO_BOILER_GEAR_DISTANCE = 16.0;
    public static final double BOILER_GEAR_REVERSE_DISTANCE = -24.0;

    public ScoreBoilerGearCommand(boolean score) {
        int direction;
        if (RobotMap.ALLIANCE == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveInchesPIDCommand(0.5, START_TO_BOILER_GEAR_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE));

        // Approach the peg
        addSequential(new DriveInchesPIDCommand(0.5, AFTER_TURN_TO_BOILER_GEAR_DISTANCE));

        if (score) {
            addSequential(new ScoreGearCommand());
            addSequential(new DriveInchesEncodersCommand(BOILER_GEAR_REVERSE_DISTANCE));
            addSequential(new GearTrapTrapGearCommand());
        }
    }

}
