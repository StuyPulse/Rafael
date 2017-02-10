package com.stuypulse.frc2017.robot.subsystems;

import java.lang.reflect.Array;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {

	public static CANTalon blenderMotor;
	public static double[] current;
	public boolean isJammed;
	public Encoder blenderEncoder;

	public Blender() {
		blenderMotor = new CANTalon(RobotMap.BLENDER_MOTOR_PORT);
		blenderEncoder = new Encoder(RobotMap.BLENDER_ENCODER_CHANNEL_A, RobotMap.BLENDER_ENCODER_CHANNEL_B);
		blenderEncoder.setDistancePerPulse(RobotMap.BLENDER_ENCODER_DEGREES_PER_PULSE);
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
		for(int arrayCounter = 0; arrayCounter < current.length; arrayCounter++) {
			arraySum += current[arrayCounter];
		}
		double currentArithmeticMean = arraySum/current.length;
		double blenderDegreesPerPulse = blenderEncoder.getRate();
		//Checks whether the average is over the threshold for not jammed.
		boolean isCurrentHigh = currentArithmeticMean > RobotMap.BLENDER_CURRENT_THRESHOLD_FOR_JAM;
		boolean isSpeedHigh = blenderDegreesPerPulse > RobotMap.BLENDER_DEGREES_PER_PULSE_THRESHOLD_FOR_JAM;
		if(isCurrentHigh && isSpeedHigh) {
			isJammed = false;
		}
		if(isCurrentHigh && !isSpeedHigh) {
			isJammed = true;
		}
		if(!isCurrentHigh && isSpeedHigh) {
			isJammed = false;
		}
		if(!isCurrentHigh && !isSpeedHigh) {
			isJammed = false;
		}
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