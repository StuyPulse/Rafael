package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Drivetrain extends Subsystem {

	private CANTalon frontLeftWheel;
	private CANTalon frontRightWheel;
	private CANTalon backLeftWheel;
    private CANTalon backRightWheel;

	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Drivetrain() {
    	frontLeftWheel = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_PORT);
    	frontRightWheel = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_PORT);
    	backLeftWheel = new CANTalon(RobotMap.BACK_LEFT_MOTOR_PORT);
    	backRightWheel = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_PORT);
    }

    public void tankDrive(double left, double right) {
    	frontLeftWheel.set(left);
    	frontRightWheel.set(right);
    	backLeftWheel.set(left);
    	backRightWheel.set(right);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
