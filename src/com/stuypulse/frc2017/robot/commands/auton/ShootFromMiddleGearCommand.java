package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootFromMiddleGearCommand extends CommandGroup {
    public static final double MIDDLE_GEAR_TO_BOILER_ANGLE = 90;
    public static final double MIDDLE_GEAR_TO_BOILER_DISTANCE = 109.7;

    public ShootFromMiddleGearCommand() {
        addSequential(new RotateDegreesGyroCommand(MIDDLE_GEAR_TO_BOILER_ANGLE, true));
        //(new OptionalCVPositionForBoilerCommand(MIDDLE_GEAR_TO_BOILER_DISTANCE));
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
