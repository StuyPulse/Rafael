package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {

	public static CANTalon blenderMotor;
	public static double[] current;
	public boolean isJammed;

	public Blender() {
		blenderMotor = new CANTalon(RobotMap.BLENDER_MOTOR_PORT);
		current = new double[15];
		isJammed = false; //Set true when blender is jammed
	}
	public void updateCurrentValue() {
		for(int i = 0; i < current.length - 1; i++){
			current[i] = current[i + 1]; 
		}
		current[current.length - 1] = blenderMotor.getOutputCurrent();
		setIsJammed();
	}
	private boolean setIsJammed() {
		//Finds array sum for the Average.
		int arraySum = 0;
		for(int arrayCounter = 1; arrayCounter < 16; arrayCounter++) {
			arraySum += current[arrayCounter];
		}
		double currentArithmeticMean = arraySum/current.length;
		//Checks whether the average is over the threshold for not jammed.
		isJammed = currentArithmeticMean > RobotMap.CURRENT_THRESHOLD_FOR_JAM;
		//Used to stop the unjam command in the isFinished method.
		return isJammed;
	}

	public void run() {
		blenderMotor.set(RobotMap.BLENDER_MOTOR_SPEED);
	}
	public void setUnjamSpeed() {
		blenderMotor.set(RobotMap.BLENDER_MOTOR_UNJAM_SPEED);
	}

	public void stop() {
		blenderMotor.set(0.0);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	// TODO: Write stop command and set as default command
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}