package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.ShooterStopCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Shooter extends Subsystem {

	private CANTalon shooterMotor;
	private CANTalon shooterMotorTwo;

	public Shooter() {
		shooterMotor = new CANTalon(RobotMap.SHOOTER_MOTOR_PORT);
		shooterMotorTwo = new CANTalon(RobotMap.SHOOTER_MOTOR_PORT_TWO);
		shooterMotor.enableBrakeMode(false);
		shooterMotorTwo.enableBrakeMode(false);
	}

	public void setSpeed(double speed) {
		shooterMotor.set(speed);
		shooterMotorTwo.set(speed);
	}

	// cut current to motor so it stops eventually, but doesn't apply brakes
	public void stop() {
		shooterMotor.set(0);
		shooterMotorTwo.set(0);
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
