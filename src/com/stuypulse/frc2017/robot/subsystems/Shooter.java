package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.ShooterStopCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Shooter extends Subsystem {

	private CANTalon firstShooterMotor;
	private CANTalon secondShooterMotor;

	public Shooter() {
		firstShooterMotor = new CANTalon(RobotMap.SHOOTER_MOTOR_PORT);
		firstShooterMotor.enableBrakeMode(false);
		secondShooterMotor = new CANTalon(RobotMap)
	}

	public void setSpeed(double speed) {
		firstShooterMotor.set(speed);
	}

	// cut current to motor so it stops eventually, but doesn't apply brakes
	public void stop() {
		firstShooterMotor.set(0);
	}

	public double getCurrentMotorSpeedInRPM() {
        return firstShooterMotor.getSpeed();
    }	

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
