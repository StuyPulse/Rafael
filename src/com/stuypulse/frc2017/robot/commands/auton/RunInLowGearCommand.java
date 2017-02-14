package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainLowGearCommand;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RunInLowGearCommand extends CommandGroup {

    public RunInLowGearCommand(Command command) {

    	addSequential(new DriveTrainLowGearCommand());
        command.start();
        addSequential(new DriveTrainHighGearCommand());

    }
}
