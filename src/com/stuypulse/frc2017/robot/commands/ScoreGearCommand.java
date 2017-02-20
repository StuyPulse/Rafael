package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.commands.auton.DoubleSequentialCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreGearCommand extends CommandGroup {

    public ScoreGearCommand(double delayinseconds) {
    	addSequential(new GearPusherPushGearCommand());
    	addSequential(new GearTrapReleaseGearCommand());
    	addSequential(new DelaySecondsCommand(delayinseconds));
    	addSequential(new GearPusherRetractGearCommand());
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
    }
}
