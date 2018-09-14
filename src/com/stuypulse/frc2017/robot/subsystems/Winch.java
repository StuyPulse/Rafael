package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
//import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private WPI_TalonSRX winchMotorA;
    private WPI_TalonSRX winchMotorB;


    public Winch() {
        winchMotorA = new WPI_TalonSRX(RobotMap.WINCH_MOTOR_PORT_A);
        winchMotorB = new WPI_TalonSRX(RobotMap.WINCH_MOTOR_PORT_B);
        
        winchMotorA.setNeutralMode(NeutralMode.Brake);
        winchMotorB.setNeutralMode(NeutralMode.Brake);
    }

    public void startWinchFast() {
        winchMotorA.set(RobotMap.WINCH_MOTOR_SPEED_FAST);
        winchMotorB.set(RobotMap.WINCH_MOTOR_SPEED_FAST);
    }

    public void startWinchSlow() {
        winchMotorA.set(RobotMap.WINCH_MOTOR_SPEED_SLOW);
        winchMotorB.set(RobotMap.WINCH_MOTOR_SPEED_SLOW);
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
