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

	private static AnalogInput distanceSensor;

	// Create instance of a timer that we can use to keep track of how long the
	// gear is kept in the position for.
	private static Timer timeSinceEntry;
	// We manually record whether the timer is running (Timer keeps that information private...)
	private static boolean isTimerRunning;

	public IRSensor() {
		distanceSensor = new AnalogInput(RobotMap.IR_SENSOR_PORT);
		timeSinceEntry = new Timer();
		isTimerRunning = false;
	}

	public static double getDistance() {
		return CONVERSION_FACTOR_CM_TO_INCHES
				* (EQUATION_FACTOR * Math.pow(getVoltage(), EQUATION_EXPONENT));
		// Link to Graph of Equation:
		// http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf (pg. 4)
		// This method is an approximation of that graph.
	}

	public static double getVoltage() {
		return distanceSensor.getVoltage();
	}

	public static boolean isGearDetected() {
		return getDistance() < RobotMap.IR_SENSOR_THRESHOLD;
	}

	public static void handleAutoGearPush() {
		if (isGearDetected()) {
		    // If the timer is stopped, start it
		    if (!isTimerRunning) {
		        // Based on a mirror of old source code, the start() method resets the
		        // timer as well (current source code, and docs, don't help; if someone can
		        // test it, do so and replace this comment).
		        timeSinceEntry.start();
		        isTimerRunning = true;
		    }
		    // If the "time is up", push the gear and reset the timer
			if (timeSinceEntry.get() > RobotMap.IR_TIME_IN_MECHANISM_THRESHOLD) {
				Robot.gearpusher.extend();
				timeSinceEntry.stop();
				isTimerRunning = false;
				timeSinceEntry.reset();
			}
		}
	}
}
