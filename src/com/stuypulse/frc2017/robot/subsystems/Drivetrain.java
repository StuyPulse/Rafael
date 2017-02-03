package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DrivetrainPiotrDriveCommand;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Drivetrain extends Subsystem {

	private CANTalon frontLeftWheel;
	private CANTalon frontRightWheel;
	private CANTalon backLeftWheel;
    private CANTalon backRightWheel;

    private RobotDrive robotDrive;
    
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    
    private AHRS gyro;

	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Drivetrain() {
    	frontLeftWheel = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_PORT);
    	frontRightWheel = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_PORT);
    	backLeftWheel = new CANTalon(RobotMap.BACK_LEFT_MOTOR_PORT);
    	backRightWheel = new CANTalon(RobotMap.BACK_RIGHT_MOTOR_PORT);

    	robotDrive = new RobotDrive(backLeftWheel, frontLeftWheel, backRightWheel, frontRightWheel);

    	leftEncoder = new Encoder(RobotMap.DRIVETRAIN_ENCODER_LEFT_CHANNEL_A, RobotMap.DRIVETRAIN_ENCODER_LEFT_CHANNEL_B);
    	rightEncoder = new Encoder(RobotMap.DRIVETRAIN_ENCODER_RIGHT_CHANNEL_A, RobotMap.DRIVETRAIN_ENCODER_RIGHT_CHANNEL_B);

    	leftEncoder.setDistancePerPulse(RobotMap.DRIVETRAIN_ENCODER_INCHES_PER_PULSE);
    	rightEncoder.setDistancePerPulse(RobotMap.DRIVETRAIN_ENCODER_INCHES_PER_PULSE);
    	
    	gyro = new AHRS(SPI.Port.kMXP);
    	resetGyro();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand( new DrivetrainPiotrDriveCommand() );
    }

    public void tankDrive(double left, double right) {
    	robotDrive.tankDrive(left,right);
    }

    public void stop() {
    	tankDrive(0,0);
    }

    public double gyroAngle() {
        return gyro.getAngle();
    }
    
    public void resetGyro() {
        gyro.reset();
    }
    
    public void resetEncoders() {
    	leftEncoder.reset();
    	rightEncoder.reset();
    }
    
    public double encoderDistance() {
    	return Math.max( leftEncoderDistance() , rightEncoderDistance() );
    }

    public double leftEncoderDistance() {
    	return Math.abs(leftEncoder.getDistance());
    }

    public double rightEncoderDistance() {
    	return Math.abs(rightEncoder.getDistance());
    }

}
