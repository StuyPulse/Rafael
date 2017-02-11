package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleGearMobilityMinimalCommand extends CommandGroup {
    
    private double FIRST_MOBILITY_TURN_DISTANCE = -1;
    private double FIRST_MOBILITY_TURN_ANGLE = -1;
    private double SECOND_MOBILITY_TURN_DISTANCE = -1;
    private double SECOND_MOBILITY_TURN_ANGLE = -1;
    private double FINAL_MOBILITY_TURN_DISTANCE = -1;
	
    public  MiddleGearMobilityMinimalCommand() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	int direction = 1;
		if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
			direction = 1;
		} else {
			direction = -1;
		}
		addSequential(new DriveForwardEncodersCommand(FIRST_MOBILITY_TURN_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * FIRST_MOBILITY_TURN_ANGLE));
		addSequential(new DriveForwardEncodersCommand(SECOND_MOBILITY_TURN_DISTANCE));
		addSequential(new RotateDegreesGyroCommand(direction * SECOND_MOBILITY_TURN_ANGLE));
		addSequential(new DriveForwardEncodersCommand(FINAL_MOBILITY_TURN_DISTANCE));
    }
 }

