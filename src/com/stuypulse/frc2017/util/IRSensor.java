package com.stuypulse.frc2017.util;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class IRSensor {
	//Equation numbers
	//Resulted from forming an equation with graph using many coordinates and calculator
	//Power Regression used
	private static final double EQUATION_FACTOR = 12.23368994;

	private static final double EQUATION_EXPONENT = -0.9779601588;

	private static final double CONVERSION_FACTOR_CM_TO_INCHES = 0.393701;

	private AnalogInput distanceSensor;

	// Create instance of a timer that we can use to keep track of how long the
	// gear is kept in the position for.
	private Timer timeSinceEntry;
	// We manually record whether the timer is running (Timer keeps that information private...)
	private boolean isTimerRunning;

	public IRSensor() {
		distanceSensor = new AnalogInput(RobotMap.IR_SENSOR_PORT);
		timeSinceEntry = new Timer();
		isTimerRunning = false;
	}

	public double getDistance() {
		return CONVERSION_FACTOR_CM_TO_INCHES
				* (EQUATION_FACTOR * Math.pow(getVoltage(), EQUATION_EXPONENT));
		// Link to Graph of Equation:
		// http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf (pg. 4)
		// This method is an approximation of that graph.
	}

	public double getVoltage() {
		return distanceSensor.getVoltage();
	}

	public boolean isGearDetected() {
		return getDistance() < RobotMap.IR_SENSOR_THRESHOLD;
	}
	
	public boolean isGearOut() {
		return getDistance() > RobotMap.IR_SENSOR_THRESHOLD;
	}

	public void gearLEDSignalControl() {
		if(isGearDetected()) {
			Robot.ledGearSensingSignal.stayOn();
		} else {
			Robot.ledGearSensingSignal.stayOff();
		}
	}
}
