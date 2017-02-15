package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearCommand extends CommandGroup {
    public static final double START_TO_MIDDLE_GEAR_DISTANCE = 114.3;
    public static final double MIDDLE_GEAR_REVERSE_DISTANCE = -36;

    public ScoreMiddleGearCommand() {
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

    	// Drive to gear, release trap, drive back, retract gear pusher, close trap.
    	
        addSequential(new OptionalCVPositionForGearCommand());
    	addSequential(new GearTrapReleaseGearCommand());
    	addSequential(new DriveForwardEncodersCommand(MIDDLE_GEAR_REVERSE_DISTANCE));
    	addSequential(new GearPusherRetractGearCommand());
    	addSequential(new GearTrapTrapGearCommand());
    	
    }
}
