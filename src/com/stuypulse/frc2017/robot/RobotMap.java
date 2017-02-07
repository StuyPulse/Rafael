package com.stuypulse.frc2017.robot;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

public interface RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;

    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;

    int BOILER_CAMERA_PORT = 0;
    int LIFT_CAMERA_PORT = 1;

    /// Gamepad Ports

    int DRIVER_PAD_PORT = 0;
    int OPERATOR_PAD_PORT = 1;

    /// Ports

    int FRONT_LEFT_MOTOR_PORT = 1;
    int FRONT_RIGHT_MOTOR_PORT = 3;
    int BACK_LEFT_MOTOR_PORT = 0;
    int BACK_RIGHT_MOTOR_PORT = 2;

    int BLENDER_MOTOR_PORT = -1;

    int SHOOTER_MOTOR_PORT = -1;
    
    int WINCH_MOTOR_PORT = 4;

    int DRIVETRAIN_ENCODER_LEFT_CHANNEL_A = -1;
    int DRIVETRAIN_ENCODER_LEFT_CHANNEL_B = -1;
    int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_A = -1;
    int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_B = -1;

	int GEAR_TRAP_RIGHT_SOLENOID_PORT = -1;
	int GEAR_TRAP_LEFT_SOLENOID_PORT = -1;
	int GEAR_PUSHER_SOLENOID_PORT = -1;	
	int BALL_GATE_SOLENOID_PORT = -1;
	int GEAR_SHIFT_SOLENOID_PORT = -1;


    // Solenoid Channels

	int PCM_1 = -1;
	int PCM_2 = -1;
	int PCM_3 = -1;
	int PCM_4 = -1;
	int GEAR_SHIFT = -1;

    // Physical Constants

    double DRIVETRAIN_ENCODER_INCHES_PER_PULSE = 23.56;

    double SHOOTER_ENCODER_MAXSPEED = -1;

    double BLENDER_MOTOR_SPEED = 0.2;
    
    double PIOTR_DRIVE_MARGIN_OF_ERROR = -0.001;

    double PIOTR_DRIVE_TURN_ADJUSTMENT = 0.1;
    
    //TODO: Find Ideal Shooter Speed
    double SHOOTER_IDEAL_SPEED = -1;
}
