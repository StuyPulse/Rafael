package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ScoreGearCommand extends CommandGroup {

    public ScoreGearCommand() {
        addSequential(new GearPusherRetractGearCommand());
    	addSequential(new GearTrapReleaseGearCommand());
        addSequential(new DelaySecondsCommand(SmartDashboard.getNumber("delay-one", 1.0), Robot.gearpusher));
        addSequential(new GearPusherPushGearCommand());
    	addSequential(new DelaySecondsCommand(SmartDashboard.getNumber("delay-two", 1.0), Robot.gearpusher));
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
