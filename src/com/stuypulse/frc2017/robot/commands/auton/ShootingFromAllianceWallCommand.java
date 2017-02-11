package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootingFromAllianceWallCommand extends CommandGroup {
    public static final double BACK_UP_TO_SHOOT_FROM_ALLIANCE_WALL_DISTANCE = -36.0;

	public ShootingFromAllianceWallCommand() {
		
		addSequential(new DriveForwardEncodersCommand(BACK_UP_TO_SHOOT_FROM_ALLIANCE_WALL_DISTANCE));
		//add CV
		addSequential(new BlenderRunWithUnjammingCommand());
	}
}
