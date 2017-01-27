package com.stuypulse.frc2017.robot.subsystems;

import static edu.stuy.robot.RobotMap;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	private CANTalon shooterMotor;
	private double currentSpeed;

	public Shooter() {
		shooterMotor = new CANTalon(RobotMap.SHOOTER_MOTOR_PORT);
		currentSpeed = 0.0;
	}

	public double setSpeed(double speed) {
		shooterMotor.set(speed);

	}
	
	public double getCurrentMotorSpeedInRPM() {
        return shooterMotor.getSpeed();
    }
	
	
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
