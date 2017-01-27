package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private Solenoid gearPusherPiston;
	//We are assuming there are only two states to this Solenoid.
	//The speed of the piston might play a role.
	
	private boolean opened;
	
	public GearPusher() {
		gearPusherPiston = new Solenoid(RobotMap.PCM_3,RobotMap.GEAR_PUSHER_SOLENOID_PORT);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void toggle() {
    		open(!opened);
    }
    
    public void open(boolean open) { 
    	gearPusherPiston.set(open);
    	opened = open;
    }
    
}

