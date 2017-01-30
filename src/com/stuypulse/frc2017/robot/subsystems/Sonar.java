package com.stuypulse.frc2017.robot.subsystems;

import org.usfirst.frc.team694.robot.RobotMap;
import org.usfirst.frc.team694.robot.commands.SonarTest;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sonar extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Ultrasonic leftSonar;
    private Ultrasonic rightSonar;
    private double rangeInchesLeft; 
    private double rangeInchesRight;
    
    public Sonar(){
        leftSonar = new Ultrasonic(RobotMap.LEFT_TRIGGER_PIN , RobotMap.LEFT_ECHO_PIN);
        rightSonar = new Ultrasonic(RobotMap.RIGHT_TRIGGER_PIN , RobotMap.RIGHT_ECHO_PIN);
        leftSonar.setAutomaticMode(true);
        rightSonar.setAutomaticMode(true);
    }
    public double getRangeInchesLeft() {
        return leftSonar.getRangeInches();
    }
    
    public double getRangeInchesRight() {
        return rightSonar.getRangeInches();
    }
    public boolean activatedLeft(){
        return leftSonar.isEnabled();
    }
    public boolean activatedRight(){
        return rightSonar.isEnabled();
    }
    
    public double getAngle() {
        rangeInchesLeft = leftSonar.getRangeInches();
        rangeInchesRight = rightSonar.getRangeInches();
        if (rangeInchesRight > rangeInchesLeft) {
            return (-1 * (90 - (Math.atan(RobotMap.SONAR_GAP_DISTANCE / (rangeInchesRight - rangeInchesLeft)) * (180 / Math.PI))));
        } else {
            return 90 - (Math.atan(RobotMap.SONAR_GAP_DISTANCE / (rangeInchesLeft - rangeInchesRight)) * (180 / Math.PI)); 
        }
       
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new SonarTest());
    }
}

