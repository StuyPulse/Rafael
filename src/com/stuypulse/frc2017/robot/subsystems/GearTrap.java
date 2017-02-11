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
	private Solenoid pistons;
	private boolean trapped;
	
	public GearTrap() {
		pistons = new Solenoid(RobotMap.GEAR_TRAP_SOLENOID_PORT);
		trapped = true;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void toggle() {
    	open(!trapped);
    }

    public void open(boolean trap) {
    	pistons.set(trap);
    	trapped = trap;
    }
    public void trapped() {
    	pistons.set(true);
    }
    public void released() {
    	pistons.set(false);
    }
}
