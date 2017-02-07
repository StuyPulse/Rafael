package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreBoilerGearCommand extends CommandGroup {
    
    public  ScoreBoilerGearCommand() {
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
        // e.g. if Command1 requires 	chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	addSequential(new DriveForwardEncodersCommand(RobotMap.START_TO_BOILER_GEAR_TURN_DISTANCE));
    	addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE));
    	addSequential(new DriveForwardEncodersCommand(RobotMap.AFTER_TURN_TO_BOILER_GEAR_DISTANCE));
    	addSequential(new GearTrapReleaseGearCommand());
    	addSequential(new GearPusherRetractGearCommand());
    	addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_REVERSE_DISTANCE));
    	addSequential(new GearTrapTrapGearCommand());
    }
}
