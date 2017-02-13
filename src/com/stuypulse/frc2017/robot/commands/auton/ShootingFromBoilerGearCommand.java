package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForBoilerCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootingFromBoilerGearCommand extends CommandGroup {
    public static final double BOILER_GEAR_REVERSE_SHOOTING_DISTANCE = 80.8;
    public static final double BOILER_GEAR_TURN_TO_BOILER_ANGLE = 180;
    public static final double BOILER_GEAR_TO_BOILER_DISTANCE = -80.8;

    public ShootingFromBoilerGearCommand() {
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
       
        addSequential(new DriveForwardEncodersCommand(BOILER_GEAR_REVERSE_SHOOTING_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_GEAR_TURN_TO_BOILER_ANGLE));
        if (Robot.cvChooser.getSelected()) {
			addSequential(new SetupForBoilerCommand());
		} else {
			addSequential(new DriveForwardEncodersCommand(BOILER_GEAR_TO_BOILER_DISTANCE));
		}
        addSequential(new BlenderRunWithUnjammingCommand());
        
    }
}
}
