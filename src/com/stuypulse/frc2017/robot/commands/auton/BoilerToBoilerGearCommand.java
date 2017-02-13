package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

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
        addSequential(new DriveForwardEncodersCommand(BOILER_BACK_UP_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_TURN_AROUND_ANGLE));
		if (Robot.cvChooser.getSelected()) {
			addSequential(new SetupForGearCommand());
		} else {
			addSequential(new DriveForwardEncodersCommand(BOILER_TO_BOILER_GEAR_DISTANCE));
		}
    }
}
