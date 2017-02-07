package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ApproachHPFromBoilerGearCommand extends CommandGroup {



	public ApproachHPFromBoilerGearCommand(boolean isRedAlliance) {
        if(isRedAlliance){
        	
        	// Add Commands here:
        	//addSequential(new ScoreBoilerGearCommand(Robotmap.));
        	addSequential(new RotateDegreesGyroCommand((RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE)));//Rotates after TH
			addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
			addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE));
			addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
    
        }else{
        	addSequential(new RotateDegreesGyroCommand((RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE*-1)));//Rotates after TH
    		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE));
    		addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE*-1));
    		addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE));
		}
    }
}
