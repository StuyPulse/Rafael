package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MobilityMinimalCommand extends CommandGroup {
    
    public  MobilityMinimalCommand() {
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
    		
    	/*if (middleGear && isRedAlliance){
    		addSequential(new DriveForwardEncodersCommand(RobotMap.START_TO_MOBILITY_TURN_DISTANCE));
    		addSequential(new RotateDegreesGyroCommand(RobotMap.START_TO_MOBILITY_TURN_ANGLE));
    		addSequential(new DriveForwardEncodersCommand(RobotMap.MOBILITY_TURN_DISTANCE));
    		addSequential(new RotateDegreesGyroCommand(RobotMap.MOBILITY_TURN_ANGLE));
    		addSequential(new DriveForwardEncodersCommand(RobotMap.END_MOBILITY_TURN_DISTANCE));
    	}else if(middleGear && !isRedAlliance){
    		addSequential(new DriveForwardEncodersCommand(RobotMap.START_TO_MOBILITY_TURN_DISTANCE));
    		addSequential(new RotateDegreesGyroCommand(-1 * RobotMap.START_TO_MOBILITY_TURN_ANGLE));
    		addSequential(new DriveForwardEncodersCommand(RobotMap.MOBILITY_TURN_DISTANCE));
    		addSequential(new RotateDegreesGyroCommand(-1 * RobotMap.MOBILITY_TURN_ANGLE));
    		addSequential(new DriveForwardEncodersCommand(RobotMap.END_MOBILITY_TURN_DISTANCE));
    	*/
    	addSequential(new DriveForwardEncodersCommand(RobotMap.MOBILITY_TO_BASELINE_DISTANCE));
    	
    }
    	
    }

