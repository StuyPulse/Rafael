package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Sonar extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Ultrasonic sonar;

    public Sonar() {
        sonar = new Ultrasonic(RobotMap.SONAR_TRIGGER_PIN , RobotMap.SONAR_ECHO_PIN);
    }
    
    public double getRange() {
        return sonar.getRangeInches();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

