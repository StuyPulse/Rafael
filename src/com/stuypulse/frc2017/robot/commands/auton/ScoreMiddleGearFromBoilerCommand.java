package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreMiddleGearFromBoilerCommand extends CommandGroup {
    private static final double BACK_UP_FROM_BOILER_DISTANCE = -131.9;
    private static final double TURN_AWAY_FROM_BOILER = 135;
    private static final double MOVE_TOWARDS_LIFT = 80.7;
    private static final double TURN_TOWARDS_LIFT = 90;
    private static final double MOVE_TO_LIFT = 21;

    public ScoreMiddleGearFromBoilerCommand() {
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }

        addSequential(new DriveForwardEncodersCommand(BACK_UP_FROM_BOILER_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(TURN_AWAY_FROM_BOILER * direction));
        addSequential(new DriveForwardEncodersCommand(MOVE_TOWARDS_LIFT));
        addSequential(new RotateDegreesGyroCommand(TURN_TOWARDS_LIFT * direction));

        addSequential(new OptionalCVPositionForGearCommand(MOVE_TO_LIFT));
        addSequential(new ScoreGearCommand());
        addSequential(new DriveForwardEncodersCommand(ScoreMiddleGearCommand.MIDDLE_GEAR_REVERSE_DISTANCE));
        addSequential(new GearTrapTrapGearCommand());

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
    }
}
