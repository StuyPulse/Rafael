package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ScoreGearCommand extends CommandGroup {

    public static final double FIRST_DELAY = 3.0;
    public static final double SECOND_DELAY = 3.0;

    public ScoreGearCommand() {
        addSequential(new GearPusherRetractGearCommand());
        addSequential(new GearTrapReleaseGearCommand());
        addSequential(new DelaySecondsCommand(SmartDashboard.getNumber("delay-one", FIRST_DELAY), Robot.gearpusher));
        addSequential(new GearPusherPushGearCommand());
        addSequential(new DelaySecondsCommand(SmartDashboard.getNumber("delay-two", SECOND_DELAY), Robot.gearpusher));
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
