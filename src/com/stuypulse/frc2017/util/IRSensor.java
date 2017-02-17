package com.stuypulse.frc2017.util;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class IRSensor {
	//Equation numbers
	//Resulted from forming an equation with graph using many coordinates and calculator
	//Power Regression used

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
		return RobotMap.CONVERSION_FACTOR_CM_TO_INCHES
				* (RobotMap.EQUATION_FACTOR * Math.pow(getVoltage(), RobotMap.EQUATION_EXPONENT));
		// Link to Graph of Equation:
		// http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf (pg. 4)
		// This method is an approximation of that graph.
	}

	public double getVoltage() {
		return distanceSensor.getVoltage();
	}

	public boolean isGearDetected() {
		return getDistance() < SmartDashboard.getNumber("IR Sensor Distance", RobotMap.IR_SENSOR_THRESHOLD);
	}

	public void handleAutoGearPush() {
		if (!Robot.isAutoOverridden() && isGearDetected() && Robot.geartrap.isTrapped()) {
		    // If the timer is stopped, start it
		    if (!isTimerRunning) {
		        // Based on a mirror of old source code, the start() method resets the
		        // timer as well (current source code, and docs, don't help; if someone can
		        // test it, do so and replace this comment).
		        timeSinceEntry.start();
		        isTimerRunning = true;
		    }
		    // If the "time is up", push the gear and reset the timer
			if (timeSinceEntry.get() > SmartDashboard.getNumber("IR Sensor Time", RobotMap.IR_TIME_IN_MECHANISM_THRESHOLD)) {
				Robot.gearpusher.extend();
				timeSinceEntry.stop();
				isTimerRunning = false;
				timeSinceEntry.reset();
			}
		}
	}
	public void gearLEDSignalControl() {
		if(isGearDetected()) {
			Robot.ledGearSensingSignal.stayOn();
		} else {
			Robot.ledGearSensingSignal.stayOff();
		}
	}
}
