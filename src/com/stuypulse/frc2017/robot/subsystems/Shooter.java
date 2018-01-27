package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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
    private WPI_TalonSRX shooterMotorA;
    private WPI_TalonSRX shooterMotorB;

    public Shooter() {
        shooterMotorA = new WPI_TalonSRX(RobotMap.SHOOTER_MOTOR_A_PORT);
        shooterMotorB = new WPI_TalonSRX(RobotMap.SHOOTER_MOTOR_B_PORT);
        shooterMotorA.setNeutralMode(NeutralMode.Coast);
        shooterMotorB.setNeutralMode(NeutralMode.Coast);
        shooterMotorA.setInverted(true);
        shooterMotorB.setInverted(true);
        //Replaced setFeedBackDevice. Parameters Include pidIdx and timeoutMS
        shooterMotorA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        //shooterMotorA.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        //shooterMotorA.changeControlMode(TalonControlMode.Speed);
        //shooterMotorB.changeControlMode(TalonControlMode.Speed);
        }

    @Override
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

    public void resetEncoder() {
        shooterMotorA.reset();
        shooterMotorA.enable();
        shooterMotorA.set(0.0);
    }

    public double getDistance() {
        return shooterMotorA.getSelectedSensorPosition(0);

    }

    public double getSpeed() {
        return shooterMotorA.getSelectedSensorVelocity(0);
        //return shooterMotorA.getSpeed();.
    }

    public double getCurrentShooterMotorA() {
        return shooterMotorA.getOutputCurrent();
    }

    public double getCurrentShooterMotorB() {
        return shooterMotorB.getOutputCurrent();
    }

    // set motor PID values
    public void setPIDF(double p, double i, double d, double f) {
        /* Sets PID values for shooterMotorA */
        shooterMotorA.config_kP(0, p, 0); 
        shooterMotorA.config_kI(0, i, 0); 
        shooterMotorA.config_kD(0, d, 0); 
        shooterMotorA.config_kF(0, f, 0);
        
        /* Sets PID values for shooterMotorB */
        shooterMotorB.config_kP(0, p, 0); 
        shooterMotorB.config_kI(0, i, 0); 
        shooterMotorB.config_kD(0, d, 0); 
        shooterMotorB.config_kF(0, f, 0);
    }

}
