package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.kauailabs.navx.frc.AHRS;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DrivetrainTankDriveCommand;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */

public class Drivetrain extends Subsystem {

    /**
     * Talon for left top motor. Has encoder on it.
     */
	private CANTalon leftTopMotor;
    /**
     * Talon for right top motor. Has encoder on it.
     */
	private CANTalon rightTopMotor;
	private CANTalon leftBottomMotor;
    private CANTalon rightBottomMotor;
    
    private Solenoid gearShift;
    
    private RobotDrive robotDrive;
    
    private boolean shifted;
    private AHRS gyro;


	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Drivetrain() {
    	leftTopMotor = new CANTalon(RobotMap.LEFT_TOP_MOTOR_PORT);
    	rightTopMotor = new CANTalon(RobotMap.RIGHT_TOP_MOTOR_PORT);
    	leftBottomMotor = new CANTalon(RobotMap.LEFT_BOTTOM_MOTOR_PORT);
    	rightBottomMotor = new CANTalon(RobotMap.RIGHT_BOTTOM_MOTOR_PORT);

        leftTopMotor.enableBrakeMode(true);
        rightTopMotor.enableBrakeMode(true);
        leftBottomMotor.enableBrakeMode(true);
        rightBottomMotor.enableBrakeMode(true);

        leftTopMotor.setInverted(true);
        rightTopMotor.setInverted(true);
        leftBottomMotor.setInverted(true);
        rightBottomMotor.setInverted(true);

    	gearShift = new Solenoid(RobotMap.GEAR_SHIFT_SOLENOID_PORT);

    	leftTopMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	rightTopMotor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	
    	shifted = false;
    	
    	robotDrive = new RobotDrive(leftBottomMotor, leftTopMotor, rightBottomMotor, rightTopMotor);

    	//Encoders are located on the top motors on either of the motor complexes located on the left/right hemispheres.
    	leftTopMotor.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION);
    	rightTopMotor.configEncoderCodesPerRev(RobotMap.DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION);
    	
    	gyro = new AHRS(SPI.Port.kMXP);
    	resetGyro();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DrivetrainTankDriveCommand());
    }

    public void tankDrive(double left, double right) {
    	robotDrive.tankDrive(left, right);
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
    	leftTopMotor.reset();
    	rightTopMotor.reset();
    	leftTopMotor.enable();
    	rightTopMotor.enable();
    	leftTopMotor.setPosition(0);
    	rightTopMotor.setPosition(0);
    }
    
    public double encoderDistance() {
    	return Math.max( leftEncoderDistance() , rightEncoderDistance() );
    }

    public double leftEncoderDistance() {
    	return Math.abs(leftTopMotor.getPosition() * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION);
    }

    public double rightEncoderDistance() {
    	return Math.abs(rightTopMotor.getPosition() * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION);
    }

    //Sets the solenoid to a shifted state manually
    public void manualGearShift(boolean shift) {
    	gearShift.set(shift);
    	shifted = shift;
    }

    public void highGearShift() {
        gearShift.set(false);
    }
    
    public void lowGearShift() {
        gearShift.set(true);
    }
    
    public double getLeftTopMotorCurrent(){
        return leftTopMotor.getOutputCurrent();
    }
    
    public double getRightBottomMotorCurrent(){
        return rightBottomMotor.getOutputCurrent();
    }
    public double getLeftBottomMotorCurrent(){
        return leftBottomMotor.getOutputCurrent();
    }
    
    public double getRightTopMotorCurrent(){
        return rightTopMotor.getOutputCurrent();
    }
    
}
