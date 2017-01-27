package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;



/**
 *
 */
public class Blender extends Subsystem {
	
	private CANTalon blenderMotor; 
	
	private final double MOTOR_SPEED = 0.2;
	
	public Blender() {
		
		blenderMotor = new CANTalon(RobotMap.BLENDER_MOTOR_PORT);
	}

	public void run(boolean direction) {
		if (direction) {
			blenderMotor.set(MOTOR_SPEED);
		}else {
			blenderMotor.set(MOTOR_SPEED * -1);	
		}		
	}
	
	public void stop() {
		blenderMotor.set(0);
	}
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

