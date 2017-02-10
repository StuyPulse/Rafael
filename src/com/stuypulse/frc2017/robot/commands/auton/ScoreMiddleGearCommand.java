package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearCommand extends CommandGroup {

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

    	addSequential(new DriveForwardEncodersCommand(RobotMap.START_TO_MIDDLE_GEAR_DISTANCE));
    	addSequential(new GearTrapReleaseGearCommand());
    	addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_REVERSE_DISTANCE));
    	addSequential(new GearPusherRetractGearCommand());
    	addSequential(new GearTrapTrapGearCommand());
    	
    }
}
