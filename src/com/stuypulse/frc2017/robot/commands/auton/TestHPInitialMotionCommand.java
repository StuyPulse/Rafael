package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveInchesPIDCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TestHPInitialMotionCommand extends CommandGroup {

    public TestHPInitialMotionCommand() {
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
        double extra; // extra distance to go due to field irregularities, based on alliance color
        if (RobotMap.ALLIANCE == DriverStation.Alliance.Red) {
            extra = 0.0;
        } else {
            extra = 3.0;
        }
        addSequential(new DriveInchesPIDCommand(0.5, ScoreHPGearCommand.START_TO_HP_GEAR_TURN_DISTANCE + extra));
    }
}
