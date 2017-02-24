package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreGearCommand extends CommandGroup {

    public ScoreGearCommand() {
        addSequential(new DelaySecondsCommand(0.1));
        addSequential(new GearPusherRetractGearCommand());
        addSequential(new GearTrapReleaseGearCommand());
        addSequential(new DelaySecondsCommand(0.5));
        addSequential(new GearPusherPushGearCommand());
        addSequential(new DelaySecondsCommand(0.5));
        addSequential(new GearPusherRetractGearCommand());

        // AFTER THIS, the bot must back up and the gear trap must be closed

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
