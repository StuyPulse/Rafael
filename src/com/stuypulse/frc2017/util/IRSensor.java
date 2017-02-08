package com.stuypulse.frc2017.util;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;


/**
 *
 */
public class IRSensor {
    
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
        return (0.393701 * (12.23368994 * (Math.pow(getVoltage(), -.9779601588))));
    }
    public static double getVoltage() {
        return distanceSensor.getVoltage();
    }

    public static boolean gearInMechanism() {
    	return getDistance() < RobotMap.IR_SENSOR_THRESHOLD;       
    }
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


