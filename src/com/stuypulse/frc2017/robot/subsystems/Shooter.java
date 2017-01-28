package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Shooter extends Subsystem {

	private CANTalon shooterMotor;

	public Shooter() {
		shooterMotor = new CANTalon(RobotMap.SHOOTER_MOTOR_PORT);
		shooterMotor.enableBrakeMode(false);
	}

	public void setSpeed(double speed) {
		shooterMotor.set(speed);
	}

	// cut current to motor so it stops eventually, but doesn't apply brakes
	public void cutMotorPower() {
		shooterMotor.set(0);
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
