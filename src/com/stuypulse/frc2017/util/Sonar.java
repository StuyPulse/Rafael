package com.stuypulse.frc2017.util;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Ultrasonic;

/**
 *
 */
// TODO: This wrapper class over Ultrasonic is unnecessary
public class Sonar {
    // Distance in inches from the edge of the frame perimeter to the sonar
    // senors. This is so that we can accurately measure the *bot*'s distance
    // to the wall instead of the *sonar*'s distance.
    public static final double SONAR_INSET_INCHES = 2.0; // TODO: this is a guess

    private Ultrasonic sonar;

    public Sonar() {
        sonar = new Ultrasonic(RobotMap.SONAR_TRIGGER_PIN , RobotMap.SONAR_ECHO_PIN);
    }

    public double getRange() {
        return sonar.getRangeInches();
    }
}

