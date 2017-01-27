package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class GearTrap extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private Solenoid leftPiston;
	private Solenoid rightPiston;
	private boolean opened;
	
	public GearTrap() {
		leftPiston = new Solenoid(RobotMap.PCM_1,RobotMap.GEAR_TRAP_LEFT_SOLENOID_PORT);
		rightPiston = new Solenoid(RobotMap.PCM_2,RobotMap.GEAR_TRAP_RIGHT_SOLENOID_PORT);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void toggle() {
    	if(opened) {
    		open(false);
    	} else {
    		open(true);
    	} 
    }
    
    public void open(boolean open) {
    	leftPiston.set(open);
    	rightPiston.set(open);
    	opened = open;
    }
}

