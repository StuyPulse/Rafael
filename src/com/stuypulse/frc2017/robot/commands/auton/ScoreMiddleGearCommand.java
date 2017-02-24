package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearCommand extends CommandGroup {
    public static final double START_TO_MIDDLE_GEAR_DISTANCE = 64;//114.3;
    public static final double MIDDLE_GEAR_REVERSE_DISTANCE = -12;

    public ScoreMiddleGearCommand(boolean useCV) {
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

        if (useCV) {
            addSequential(new DriveForwardEncodersCommand(10));
            addSequential(new SetupForGearCommand());
        } else {
            addSequential(new DriveForwardEncodersCommand(START_TO_MIDDLE_GEAR_DISTANCE));
        }
        addSequential(new ScoreGearCommand());
        addSequential(new DriveForwardEncodersCommand(MIDDLE_GEAR_REVERSE_DISTANCE));
        addSequential(new GearTrapTrapGearCommand());
    }

    @Override
    public boolean isFinished() {
        // If we are in autonomous and CV did not find the goal, terminate this command
        // rather than dump out a gear.
        if (Robot.isAutonomous && !Robot.cvFoundGoal) {
            return true;
        }
        return super.isFinished();
    }
}
