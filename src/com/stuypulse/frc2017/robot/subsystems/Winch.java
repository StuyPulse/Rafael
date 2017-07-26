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
    private CANTalon winchMotorA;
    private CANTalon winchMotorB;


    public Winch() {
        winchMotorA = new CANTalon(RobotMap.WINCH_MOTOR_PORT_A);
        winchMotorA.enableBrakeMode(true);
        winchMotorB = new CANTalon(RobotMap.WINCH_MOTOR_PORT_B);
        winchMotorB.enableBrakeMode(true);
    }

    public void startWinch() {
        winchMotorA.set(RobotMap.WINCH_MOTOR_SPEED);
        winchMotorB.set(RobotMap.WINCH_MOTOR_SPEED);
    }

    public void stopWinch() {
        winchMotorA.set(0.0);
        winchMotorB.set(0.0);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public double getMotorCurrent() {
        return winchMotorA.getOutputCurrent();
    }
}
