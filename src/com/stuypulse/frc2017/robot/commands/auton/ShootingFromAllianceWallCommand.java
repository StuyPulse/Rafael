package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForBoilerCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootingFromAllianceWallCommand extends CommandGroup {
    public static final double BACK_UP_TO_SHOOT_FROM_ALLIANCE_WALL_DISTANCE = -36.0;

	public ShootingFromAllianceWallCommand() {
		
		if (Robot.cvChooser.getSelected()) {
			addSequential(new SetupForBoilerCommand());
		} else {
			addSequential(new DriveForwardEncodersCommand(BACK_UP_TO_SHOOT_FROM_ALLIANCE_WALL_DISTANCE));
		}
		addSequential(new BlenderRunWithUnjammingCommand());
	}
}
