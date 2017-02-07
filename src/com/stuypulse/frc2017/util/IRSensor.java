package com.stuypulse.frc2017.util;

import static com.stuypulse.frc2017.robot.RobotMap.IR_SENSOR_THRESHOLD;
import static com.stuypulse.frc2017.robot.RobotMap.IR_SENSOR_PORT;   
import edu.wpi.first.wpilibj.AnalogInput;


/**
 *
 */
public class IRSensor {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static AnalogInput distanceSensor;
    
    public IRSensor() {
        distanceSensor = new AnalogInput(IR_SENSOR_PORT);
    }
    
    public static double getDistance() {
        return (0.393701 * (12.23368994 * (Math.pow(getVoltage(), -.9779601588))));
    }
    public static double getVoltage() {
        return distanceSensor.getVoltage();
    }

    public static boolean gearInMechanism() {
        return getDistance() < IR_SENSOR_THRESHOLD;
    }
    public static boolean gearCheckTime() {
        if (gearInMechanism()){
            try {
                Thread.sleep(501);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
            
        }else{
            return false;
        }
   }
 }


