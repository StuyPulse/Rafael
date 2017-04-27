package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveInchesPIDCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearCommand extends CommandGroup {
    public static final double START_TO_MIDDLE_GEAR_DISTANCE = 69;
    public static final double MIDDLE_GEAR_REVERSE_DISTANCE = -12;
    private boolean useCV;

    public ScoreMiddleGearCommand(boolean useCV) {
        this.useCV = useCV;
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
            // First, move forward to get close enough for the camera to see
            addSequential(new DriveInchesEncodersCommand(30));
            addSequential(new SetupForGearCommand());
        } else {
            // TODO: Replace speed and modify to work without magic numbers
            addSequential(new DriveInchesPIDCommand(0.5, START_TO_MIDDLE_GEAR_DISTANCE), 5.0);
            //new DriveInchesEncodersCommand(START_TO_MIDDLE_GEAR_DISTANCE));
        }
        addSequential(new ScoreGearCommand());
        addSequential(new DriveInchesEncodersCommand(MIDDLE_GEAR_REVERSE_DISTANCE), 3.0);
        addSequential(new GearTrapTrapGearCommand());
    }

    @Override
    public boolean isFinished() {
        // If we are in autonomous and CV did not find the goal, terminate this command
        // rather than dump out a gear.
        if (useCV && Robot.isAutonomous && !Robot.cvFoundGoal) {
            return true;
        }
        return super.isFinished();
    }
}
