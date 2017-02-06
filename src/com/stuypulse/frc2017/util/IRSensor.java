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
    private AnalogInput distanceSensor;
    
    public IRSensor() {
        distanceSensor = new AnalogInput(IR_SENSOR_PORT);
    }
    
    public double getDistance() {
        return (12.23368994 * (Math.pow(getVoltage(), -.9779601588)));
    }
    public double getVoltage() {
        return distanceSensor.getVoltage();
    }
    
    public boolean hasGear() {
        return getDistance() > IR_SENSOR_THRESHOLD;
    }
    
}

