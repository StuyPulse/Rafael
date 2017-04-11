package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleGearMobilityMinimalCommand extends CommandGroup {
    private static final double FIRST_MOBILITY_TURN_DISTANCE = 53.3; //baseline-robot length
    private static final double FIRST_MOBILITY_TURN_ANGLE = -90;
    private static final double SECOND_MOBILITY_TURN_DISTANCE = 61.1;
    private static final double SECOND_MOBILITY_TURN_ANGLE = 90;
    private static final double FINAL_MOBILITY_TURN_DISTANCE = 52; //robot length + 12 inches deviation

    public MiddleGearMobilityMinimalCommand() {
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
        addSequential(new DriveInchesEncodersCommand(FIRST_MOBILITY_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(FIRST_MOBILITY_TURN_ANGLE, true));
        addSequential(new DriveInchesEncodersCommand(SECOND_MOBILITY_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(SECOND_MOBILITY_TURN_ANGLE, true));
        addSequential(new DriveInchesEncodersCommand(FINAL_MOBILITY_TURN_DISTANCE));
    }
}
