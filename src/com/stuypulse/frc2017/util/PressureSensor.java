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
    
    public double getVoltage() {
        return pressureSensor.getVoltage();
    }

    // TODO: what units does getPressure()'s return value use? Should be commented,
    // as well as the source of these `250` and `25` numbers.
    public double getPressure() {
        return ((250 * (getVoltage() / Vcc)) - 25);
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
