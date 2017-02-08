package com.stuypulse.frc2017.util;

import java.util.Timer;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;


/**
 *
 */
public class IRSensor {
    
    private static final double TRIAL_AND_ERROR_NUMBER = 12.23368994;	

	private static final double EQUATION_EXPONENT = -.9779601588;

	private static final double CONVERSION_FACTOR_CM_TO_INCHES = 0.393701;

	// Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static AnalogInput distanceSensor;
    
    // Keeps track of whether the gear was previously detected
    private static boolean gearWasDetected;

    // Create instance of a timer that we can use to keep track of how long the 
    // gear is kept in the position for. 
    private static Timer timeSinceEntry;

    public IRSensor() {
        distanceSensor = new AnalogInput(RobotMap.IR_SENSOR_PORT);
        gearWasDetected = false;
        timeSinceEntry = new Timer();
        
    }
    
    public static double getDistance() {
        return (CONVERSION_FACTOR_CM_TO_INCHES * (TRIAL_AND_ERROR_NUMBER * (Math.pow(getVoltage(), EQUATION_EXPONENT))));
        // Link to Graph of Equation 
        // http://image.dfrobot.com/image/data/SEN0143/GP2Y0A41SK%20datasheet.pdf (pg. 4)
    }
    public static double getVoltage() {
        return distanceSensor.getVoltage();
    }

    public static boolean gearInMechanism() {
    	return getDistance() < RobotMap.IR_SENSOR_THRESHOLD;       
    }
    
    // TODO: Look over logic
    public static void gearCheckTime() {
       if (gearInMechanism()){
    	   if (gearWasDetected) {
               if (timeSinceEntry.get() > RobotMap.IR_TIME_IN_MECHANISM_THRESHOLD) {
                   Robot.gearpusher.extend();
                   timeSinceEntry.stop();
                   timeSinceEntry.reset();
                   gearWasDetected = false;
               }
           }else {
        	   timeSinceEntry.start();
        	   gearWasDetected = true;
           }
       }
   }
 }


