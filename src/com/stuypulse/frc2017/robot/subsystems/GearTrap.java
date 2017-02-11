package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;

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
	private boolean trapped;
	
	public GearTrap() {
		leftPiston = new Solenoid(RobotMap.PCM_1,RobotMap.GEAR_TRAP_LEFT_SOLENOID_PORT);
		rightPiston = new Solenoid(RobotMap.PCM_2,RobotMap.GEAR_TRAP_RIGHT_SOLENOID_PORT);
		trapped = true;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new GearPusherRetractGearCommand());
    }

    public void toggle() {
    	open(!trapped);
    }

    public void open(boolean trap) {
    	leftPiston.set(trap);
    	rightPiston.set(trap);
    	trapped = trap;
    }
    public void trapped() {
    	leftPiston.set(true);
    	rightPiston.set(true);
    }
    public void released() {
    	leftPiston.set(false);
    	rightPiston.set(false);
    }
}
