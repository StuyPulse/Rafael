package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.SonarTest;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sonar extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Ultrasonic sonarOne;
    private Ultrasonic sonarTwo;
    private Ultrasonic sonarThree; 
    private Ultrasonic sonarFour;
//    private double rangeInchesLeft; 
//    private double rangeInchesRight;
    
    public Sonar(){
        sonarOne = new Ultrasonic(RobotMap.SONAR_ONE_TRIGGER_PIN , RobotMap.SONAR_ONE_ECHO_PIN);
        sonarTwo = new Ultrasonic(RobotMap.SONAR_TWO_TRIGGER_PIN , RobotMap.SONAR_TWO_ECHO_PIN);
        sonarThree = new Ultrasonic(RobotMap.SONAR_THREE_TRIGGER_PIN , RobotMap.SONAR_THREE_ECHO_PIN);
        sonarFour = new Ultrasonic(RobotMap.SONAR_FOUR_TRIGGER_PIN , RobotMap.SONAR_FOUR_ECHO_PIN);
        sonarOne.setAutomaticMode(true);
        sonarTwo.setAutomaticMode(true);
        sonarThree.setAutomaticMode(true);
        sonarFour.setAutomaticMode(true);
    }
    public double getRangeInchesOne() {
        return sonarOne.getRangeInches();
    }
    
    public double getRangeInchesTwo() {
        return sonarTwo.getRangeInches();
    }
    public double getRangeInchesThree() {
        return sonarThree.getRangeInches();
    }
    
    public double getRangeInchesFour() {
        return sonarFour.getRangeInches();
    }
/*    public boolean activatedLeft(){
        return leftSonar.isEnabled();
    }
    public boolean activatedRight(){
        return rightSonar.isEnabled();
    }
*/
//    public double getAngle() {
//        rangeInchesLeft = leftSonar.getRangeInches();
//        rangeInchesRight = rightSonar.getRangeInches();
//        if (rangeInchesRight > rangeInchesLeft) {
//            return (-1 * (90 - (Math.atan(RobotMap.SONAR_GAP_DISTANCE / (rangeInchesRight - rangeInchesLeft)) * (180 / Math.PI))));
//        } else {
//            return 90 - (Math.atan(RobotMap.SONAR_GAP_DISTANCE / (rangeInchesLeft - rangeInchesRight)) * (180 / Math.PI)); 
//        }
//       
//    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new SonarTest());
    }
}

