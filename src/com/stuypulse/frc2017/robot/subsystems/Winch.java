package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANTalon winchMotor;

    public Winch() {
        winchMotor = new CANTalon(RobotMap.WINCH_MOTOR_PORT);
        winchMotor.enableBrakeMode(true);
    }

    public void startWinch() {
        winchMotor.set(RobotMap.WINCH_MOTOR_SPEED);
    }

    public void stopWinch() {
        winchMotor.set(0.0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public double getMotorCurrent() {
        return winchMotor.getOutputCurrent();
    }
}
