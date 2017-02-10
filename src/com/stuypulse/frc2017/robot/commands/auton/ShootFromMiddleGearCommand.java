package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.BlenderSpinCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootFromMiddleGearCommand extends CommandGroup {

    public ShootFromMiddleGearCommand(boolean isRedAlliance) {
    	if(isRedAlliance){
    		addSequential(new RotateDegreesGyroCommand(RobotMap.MIDDLE_GEAR_TO_BOILER_ANGLE));
    	}else{
    		addSequential(new RotateDegreesGyroCommand(RobotMap.MIDDLE_GEAR_TO_BOILER_ANGLE*-1));
    	}
    	addSequential(new DriveForwardEncodersCommand(RobotMap.MIDDLE_GEAR_TO_BOILER_DISTANCE));
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
