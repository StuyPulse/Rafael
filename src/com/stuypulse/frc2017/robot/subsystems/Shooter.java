package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
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
        shooterMotorA.setInverted(true);
        shooterMotorB.setInverted(true);

        // reverse closed loop (ex. PID) output
        //shooterMotorA.reverseOutput(true);
        //shooterMotorB.reverseOutput(true);

        shooterMotorA.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        shooterMotorB.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);

        //shooterMotorA.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        //shooterMotorA.changeControlMode(TalonControlMode.Speed);
        //shooterMotorB.changeControlMode(TalonControlMode.Speed);

        shooterMotorA.configEncoderCodesPerRev(RobotMap.SHOOTER_ENCODER_PULSES_PER_REVOLUTION);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void setSpeed(double speed) {
        shooterMotorA.set(speed);
        // If shooterMotorB is a follower, by default it will mimic A's shooter value
        if (shooterMotorB.getControlMode() != TalonControlMode.Follower) {
            shooterMotorB.set(speed);
        }
    }

    // cut current to motor so it stops eventually, but doesn't apply brakes
    public void stop() {
        setSpeed(0);
    }

    public void resetEncoder() {
        shooterMotorA.reset();
        shooterMotorA.enable();
        shooterMotorA.set(0.0);
    }

    public double getDistance() {
        return shooterMotorA.getPosition();

    }

    public double getSpeed() {
        return shooterMotorA.getEncVelocity();
        //return shooterMotorA.getSpeed();
    }

    public double getCurrentShooterMotorA() {
        return shooterMotorA.getOutputCurrent();
    }

    public double getCurrentShooterMotorB() {
        return shooterMotorB.getOutputCurrent();
    }

    // set motor PID values
    // run setFollowerMode(true) if you want this to work!
    public void setPIDF(double p, double i, double d, double f) {
        shooterMotorA.setPID(p, i, d);
        shooterMotorA.setF(f);
        shooterMotorB.setPID(p, i, d);
        shooterMotorB.setF(f);
    }

    // Changes control mode (PercentVBus or Speed, ect...)
    public void changeControlMode(TalonControlMode mode){
        shooterMotorA.changeControlMode(mode);
        shooterMotorB.changeControlMode(mode);
    }

    // For PID. Whether Motor B mimics motor A
    public void setFollowerMode(boolean follower) {
        if (follower) {
            shooterMotorB.changeControlMode(TalonControlMode.Follower);
            shooterMotorB.set(RobotMap.SHOOTER_MOTOR_A_PORT);
        } else {
            shooterMotorB.changeControlMode(shooterMotorA.getControlMode());
            shooterMotorB.set(0); // TODO: Is this necessary?
        }
    }

}
