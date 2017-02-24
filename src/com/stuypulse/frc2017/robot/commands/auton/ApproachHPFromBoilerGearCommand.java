package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
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
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveTrainHighGearCommand());
        addSequential(new RotateDegreesGyroCommand((direction * BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE)));
        addSequential(new DriveForwardEncodersCommand(BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE));
        addSequential(new DriveForwardEncodersCommand(BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
    }
}
