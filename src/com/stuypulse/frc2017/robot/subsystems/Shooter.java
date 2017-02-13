package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Shooter extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // There are two motors on the shooter, hooked up to one gearbox, with the
    // sole purpose of doubling torque.
	private CANTalon shooterMotorA;
	private CANTalon shooterMotorB;

	public Shooter() {
		shooterMotorA = new CANTalon(RobotMap.SHOOTER_MOTOR_A_PORT);
		shooterMotorB = new CANTalon(RobotMap.SHOOTER_MOTOR_B_PORT);
		shooterMotorA.enableBrakeMode(false);
		shooterMotorB.enableBrakeMode(false);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

	public void setSpeed(double speed) {
		shooterMotorA.set(speed);
		shooterMotorB.set(speed);
	}

	// cut current to motor so it stops eventually, but doesn't apply brakes
	public void stop() {
		shooterMotorA.set(0.0);
		shooterMotorB.set(0.0);
	}

	public double getCurrentMotorSpeedInRPM() {
        return (shooterMotorA.getSpeed() + shooterMotorB.getSpeed()) / 2.0;
    }
	
	public double getCurrentVoltageShooterMotorA() {
	    return(shooterMotorA.getOutputVoltage());
	}
	
	public double getCurrentVoltageShooterMotorB() {
        return(shooterMotorB.getOutputVoltage());
    }
	
}
