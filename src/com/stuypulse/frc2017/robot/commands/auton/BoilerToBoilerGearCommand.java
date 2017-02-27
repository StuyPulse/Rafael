package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BoilerToBoilerGearCommand extends CommandGroup {
    public static final double BOILER_BACK_UP_DISTANCE = -80.8;
    public static final double BOILER_TURN_AROUND_ANGLE = 180;
    public static final double BOILER_TO_BOILER_GEAR_DISTANCE = 80.8;

    public BoilerToBoilerGearCommand() {
        // Add Commands here:
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveInchesEncodersCommand(BOILER_BACK_UP_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_TURN_AROUND_ANGLE));
        addSequential(new DriveInchesEncodersCommand(BOILER_TO_BOILER_GEAR_DISTANCE));
    }
}
