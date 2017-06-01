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
    private CANTalon shooterAssistMotor; 

    public Shooter() {
        shooterMotorA = new CANTalon(RobotMap.SHOOTER_MOTOR_A_PORT);
        shooterMotorB = new CANTalon(RobotMap.SHOOTER_MOTOR_B_PORT);
        shooterAssistMotor = new CANTalon(RobotMap.SHOOTER_ASSIST_MOTOR_PORT);
        //Initializing the variable with the port(9)
        shooterMotorA.enableBrakeMode(false);
        shooterMotorB.enableBrakeMode(false);
        shooterMotorA.setInverted(true);
        shooterMotorB.setInverted(true);
        shooterMotorA.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
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
        shooterMotorB.set(speed);
        shooterAssistMotor.set(speed);
        //This sets the speed of the motor to the same speed that the shooter is operating at.
        //If the shooter is running backwards, so will this thing.
        //If the shooter is running at the minimum speed (0.3), this motor will as well.
    }

    // cut current to motor so it stops eventually, but doesn't apply brakes
    public void stop() {
        shooterMotorA.set(0.0);
        shooterMotorB.set(0.0);
        shooterAssistMotor.set(0.0);
        //Stops the motor when this method is run in the various commands.
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
    public void setPIDF(double p, double i, double d, double f) {
        shooterMotorA.setPID(p, i, d);
        shooterMotorA.setF(f);
        shooterMotorB.setPID(p, i, d);
        shooterMotorB.setF(f);
    }

}
