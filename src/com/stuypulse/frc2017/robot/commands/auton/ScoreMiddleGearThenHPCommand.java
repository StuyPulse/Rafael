package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveInchesPIDCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearThenHPCommand extends CommandGroup {
    // START_TO_MIDDLE_GEAR_DISTANCE form ScoreMiddleGearCommand is used
    public static final double MIDDLE_GEAR_REVERSE_DISTANCE = -80;
    public static final double CROSS_ALLIANCE_STATION_DISTANCE = 90;
    public static final double APPROACH_HP_STATION_DISTANCE = 150;

    private static final double SPEED = 0.5;

    private boolean useCV;

    public ScoreMiddleGearThenHPCommand() {
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

        // Commands mostly duplicative of ScoreMiddleGearCommand:
        addSequential(new DriveInchesPIDCommand(SPEED, ScoreMiddleGearCommand.START_TO_MIDDLE_GEAR_DISTANCE), 5.0);
        addSequential(new ScoreGearCommand());
        addSequential(new DriveInchesPIDCommand(-SPEED, MIDDLE_GEAR_REVERSE_DISTANCE), 5.0);
        addSequential(new GearTrapTrapGearCommand());

        // Turn and move toward the retrieval zone on our side of the field
        addSequential(new RotateDegreesGyroCommand(-90.0, true));
        addSequential(new DriveInchesPIDCommand(SPEED, CROSS_ALLIANCE_STATION_DISTANCE));

        // Turn and move toward the retrieval zone
        addSequential(new RotateDegreesGyroCommand(90.0, true));
        addSequential(new DriveInchesPIDCommand(SPEED, APPROACH_HP_STATION_DISTANCE));
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
