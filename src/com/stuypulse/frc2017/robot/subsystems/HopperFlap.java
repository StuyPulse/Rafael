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

    private static final double OPEN_ANGLE = 170;
    private static final double CLOSED_ANGLE = 0;

    private Servo servo;

    private boolean open;

    public HopperFlap() {
        servo = new Servo(RobotMap.HOPPER_FLAP_SERVO_PORT);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void open() {
        open = true;
        servo.setAngle(OPEN_ANGLE);
    }

    public void close() {
        open = false;
        servo.setAngle(CLOSED_ANGLE);
    }

    public void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    public boolean isOpened() {
        return open;
    }

}
