package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class HopperFlap extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Servo hopperServo;

    public HopperFlap() {
        hopperServo = new Servo(RobotMap.HOPPER_FLAP_SERVO_PORT);
    }

    public void setServoAngle(double degrees) {
        hopperServo.setAngle(degrees);
    }

    public double getServoAngle(){
        return hopperServo.getAngle();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

