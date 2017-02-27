package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RunInLowGearCommand extends CommandGroup {

    public RunInLowGearCommand(Command command) {

        addSequential(new DriveTrainLowGearCommand());
        addSequential(command);
        addSequential(new DriveTrainHighGearCommand());

    }
}
