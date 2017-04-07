package com.stuypulse.frc2017.util;

import edu.wpi.first.wpilibj.AnalogInput;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

public class PressureSensor {

    private AnalogInput pressureSensor;

    public PressureSensor() {
        pressureSensor = new AnalogInput(RobotMap.PRESSURE_SENSOR_PORT);
    }
    
    int Vcc = 5;
    
    public double getVout() {
        return pressureSensor.getVoltage();
    }
    
    public double getPressure() {
        return ((250 * (getVout() / Vcc)) - 25);
    }
    
    public boolean isPressureLow() {
        return getPressure() < RobotMap.PRESSURE_SENSOR_THRESHOLD;
    }
    
    public void pressureLEDSignalControl() {
        if (isPressureLow()) {
            Robot.ledPressureSensingSignal.stayOn();
        } else {
            Robot.ledPressureSensingSignal.stayOff();
        }
    }
}
