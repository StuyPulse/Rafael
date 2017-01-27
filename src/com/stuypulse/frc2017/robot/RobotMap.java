package com.stuypulse.frc2017.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	
	public static int BOILER_CAMERA_PORT = 0;
	public static int LIFT_CAMERA_PORT = 1;
	
	///Gamepad Ports
	
	public static final int DRIVER_PAD_PORT = 0;
	public static final int OPERATOR_PAD_PORT = 1;
	
	/// Ports

	public static final int FRONT_LEFT_MOTOR_PORT = 1;
	public static final int FRONT_RIGHT_MOTOR_PORT = 3;
	public static final int BACK_LEFT_MOTOR_PORT = 0;
	public static final int BACK_RIGHT_MOTOR_PORT = 2;
	
	public static final int BLENDER_MOTOR_PORT = -1;
	
	public static final int SHOOTER_MOTOR_PORT = -1;
	
	public static final int DRIVETRAIN_ENCODER_LEFT_CHANNEL_A = -1;
	public static final int DRIVETRAIN_ENCODER_LEFT_CHANNEL_B = -1;
	public static final int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_A = -1;
	public static final int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_B = -1;

	public static final int GEAR_TRAP_RIGHT_SOLENOID_PORT = -1;
	public static final int GEAR_TRAP_LEFT_SOLENOID_PORT = -1;
	public static final int GEAR_PUSHER_SOLENOID_PORT = -1;
	
	//Solenoid Channels
	
	public static final int PCM_1 = -1;
	public static final int PCM_2 = -1;
	public static final int PCM_3 = -1;
	
	//Physical Constants
	
	public static final double DRIVETRAIN_ENCODER_INCHES_PER_PULSE = 23.56;

	public static final double SHOOTER_ENCODER_MAXSPEED = -1;
}
