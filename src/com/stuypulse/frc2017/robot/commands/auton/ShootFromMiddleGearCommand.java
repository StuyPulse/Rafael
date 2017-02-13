package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForBoilerCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootFromMiddleGearCommand extends CommandGroup {
    public static final double MIDDLE_GEAR_TO_BOILER_ANGLE = 90;
    public static final double MIDDLE_GEAR_TO_BOILER_DISTANCE = 109.7;

    public ShootFromMiddleGearCommand() {
    	if(DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red){
    		addSequential(new RotateDegreesGyroCommand(MIDDLE_GEAR_TO_BOILER_ANGLE));
    	}else{
    		addSequential(new RotateDegreesGyroCommand(-1.0 * MIDDLE_GEAR_TO_BOILER_ANGLE));
    	}
    	if (Robot.cvChooser.getSelected()) {
			addSequential(new SetupForBoilerCommand());
		} else {
			addSequential(new DriveForwardEncodersCommand(MIDDLE_GEAR_TO_BOILER_DISTANCE));
		}
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
