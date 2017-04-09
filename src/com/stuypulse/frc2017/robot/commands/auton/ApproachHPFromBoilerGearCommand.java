package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromBoilerGearCommand extends CommandGroup {
    public static final double BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE = 60;
    public static final double BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE = 71;
    public static final double BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE = -43;
    public static final double BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE = 361;

    public ApproachHPFromBoilerGearCommand() {
        addSequential(new DriveTrainHighGearCommand());
        addSequential(new RotateDegreesGyroCommand(BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE, true));
        addSequential(new DriveInchesEncodersCommand(BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE, true));
        addSequential(new DriveInchesEncodersCommand(BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
    }
}
