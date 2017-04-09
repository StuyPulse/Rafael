package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromHPGearCommand extends CommandGroup {
    public static final double HP_GEAR_TO_NEUTRAL_ZONE_ANGLE = -60;
    public static final double HP_GEAR_TO_NEUTRAL_ZONE_DISTANCE = 362; //374- 12 inches deviation

    public ApproachHPFromHPGearCommand() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        // addSequential(new Command2());
        // these will run in order.

        addSequential(new DriveTrainHighGearCommand());
        addSequential(new RotateDegreesGyroCommand(HP_GEAR_TO_NEUTRAL_ZONE_ANGLE, true));
        addSequential(new DriveInchesEncodersCommand(HP_GEAR_TO_NEUTRAL_ZONE_DISTANCE));

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        // addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
