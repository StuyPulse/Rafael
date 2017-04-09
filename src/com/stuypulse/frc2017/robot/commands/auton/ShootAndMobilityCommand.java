package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.BlenderSpinCommand;
import com.stuypulse.frc2017.robot.commands.BlenderStopCommand;
import com.stuypulse.frc2017.robot.commands.DelaySecondsCommand;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateIdealSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterStopCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootAndMobilityCommand extends CommandGroup {

    public ShootAndMobilityCommand() {
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

        addSequential(new DriveTrainHighGearCommand(), 1.0); // Timeout to align to boiler
        addSequential(new ShooterAccelerateIdealSpeedCommand()); 
        addSequential(new BlenderSpinCommand());
        addSequential(new DelaySecondsCommand(5.0)); // Wait for balls to be emptied
        addSequential(new BlenderStopCommand());
        addSequential(new ShooterStopCommand());
        addSequential(new DriveInchesEncodersCommand(-12)); // Back away from boiler
        addSequential(new RotateDegreesGyroCommand(180)); // Turn away from boiler
        addSequential(new DriveInchesEncodersCommand(100)); // Drive for mobility

    }
}
