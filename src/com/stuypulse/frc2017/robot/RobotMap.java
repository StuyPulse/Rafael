package com.stuypulse.frc2017.robot;

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

    ////////////////////////////////////////////////////////////////////////////
    // CAMERA PORTS: ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int BOILER_CAMERA_PORT = 1;
    int LIFT_CAMERA_PORT = 0;

    ////////////////////////////////////////////////////////////////////////////
    // GAMEPAD PORTS: //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int DRIVER_PAD_PORT = 0;
    int OPERATOR_PAD_PORT = 1;

    ////////////////////////////////////////////////////////////////////////////
    /// CANTALON PORTS and ENCODER CONVERSIONS: ////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Drivetrain
    // Top and bottom because the motors are placed one on top of another
    int LEFT_TOP_MOTOR_PORT = 1;
    int LEFT_BOTTOM_MOTOR_PORT = 2;
    int RIGHT_TOP_MOTOR_PORT = 3;
    int RIGHT_BOTTOM_MOTOR_PORT = 4;

    int DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION = 256;
    double DRIVETRAIN_WHEEL_DIAMETER = 4.0;
    double DRIVETRAIN_ENCODERS_FACTOR = 4.0; // output must be scaled *down* by 4 due to type of encoder
    double DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION = Math.PI * DRIVETRAIN_WHEEL_DIAMETER;
    // We use BLENDER_ENCODER_DEGREES_PER_PULSE as the encoder's distance-per-pulse.
    double BLENDER_ENCODER_DEGREES_PER_PULSE = 360.0 / RobotMap.BLENDER_ENCODER_PULSES_PER_REVOLUTION;

    // Blender
    int BLENDER_MOTOR_PORT = 6;
    int BLENDER_ENCODER_PULSES_PER_REVOLUTION = 1024; // TODO: Test value

    // Shooter
    int SHOOTER_MOTOR_A_PORT = 7;
    int SHOOTER_MOTOR_B_PORT = 8;

    int SHOOTER_ENCODER_PULSES_PER_REVOLUTION = 1024;

    // Winch
    int WINCH_MOTOR_PORT = 5;

    ////////////////////////////////////////////////////////////////////////////
    // SOLENOIDS: //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int GEAR_SHIFT_SOLENOID_PORT = 0;
    int BALL_GATE_SOLENOID_PORT = 1;
    int GEAR_TRAP_SOLENOID_PORT = 2;
    // The gear coverer (which obstructs balls from entering gear mechanism)
    // and gear pusher are on a double solenoid
    int GEAR_COVERER_SOLENOID_PORT = 3;
    int GEAR_PUSHER_SOLENOID_PORT = 4;

    ////////////////////////////////////////////////////////////////////////////
    // PCMS: ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int PCM = 0;

    ////////////////////////////////////////////////////////////////////////////
    // PHYSICAL CONSTANTS: /////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    // Time between trapping gear and pushing gear, at start of auton
    // FIXME: Appears to never be used. DELETE if not used
    double DELAY_AFTER_PUSH_GEAR = 0.1; // TODO: tune value

    // TODO: this is used from two commands; confirm it works for both
    double BOILER_TO_HOPPER_BACKUP_DISTANCE = -158.4;

    // Blender
    double BLENDER_MOTOR_SPEED = 0.5; // TODO: Set ideal speed.
    double BLENDER_MOTOR_UNJAM_SPEED = -1.0; // Run backwards at max speed (-1.0 is not a placeholder)
    double BLENDER_MOTOR_UNJAM_TIME = 0.5;
    double BLENDER_CURRENT_THRESHOLD_FOR_JAM = 23; //Amperes

    double BLENDER_DEGREES_PER_PULSE_THRESHOLD_FOR_JAM = -1.0; //TODO: Find a suitable value for this.

    // Shooter
    double SHOOTER_IDEAL_SPEED = 0.8; // TODO: set shooter speed
    double SHOOTER_MAXIMUM_SPEED = 1.0; // TODO: Test+tune to max desirable speed
    double SHOOTER_MINIMUM_SPEED = 0.3; //TODO: Test+tune minimum speed

    // Winch
    double WINCH_MOTOR_SPEED = 1.0;

    ////////////////////////////////////////////////////////////////////////////
    // LEDS: ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Ports
    int BLENDER_LED_PORT = 9;
    int GEAR_LED_PORT = 8; // for when gear is in robot
    // The third LED is (supposed to be) on port 10, for future use. // port 10 means port 0 on NavX (see DigitalIO docs)
    int GEAR_SHIFT_LED_PORT = 10;
    
    
    // On-values (i.e. which bit to send to indicate to turn the LED on)
    boolean BLENDER_LED_ON_VALUE = true;
    // TODO: test gear led on-value
    boolean GEAR_LED_ON_VALUE = true;

    ////////////////////////////////////////////////////////////////////////////
    // IR SENSOR: //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int IR_SENSOR_PORT = 0;
    double IR_SENSOR_THRESHOLD = 3.5;
    double IR_TIME_IN_MECHANISM_THRESHOLD = 1.0;

    double EQUATION_FACTOR = 12.23368994;
    double EQUATION_EXPONENT = -0.9779601588;
    double CONVERSION_FACTOR_CM_TO_INCHES = 0.393701;
    
    ////////////////////////////////////////////////////////////////////////////
    // PRESSURE SENSOR: ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int PRESSURE_SENSOR_PORT = 1;
    double PRESSURE_SENSOR_THRESHOLD = 40.0;
    

    ////////////////////////////////////////////////////////////////////////////
    // PIOTR DRIVE: //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    double PIOTR_DRIVE_MARGIN_OF_ERROR = -0.001;
    double PIOTR_DRIVE_TURN_ADJUSTMENT = 0.1;
    
    ////////////////////////////////////////////////////////////////////////////
    // PID: ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    double DRIVETRAIN_DRIVE_PID_P = 0.0;
    double DRIVETRAIN_DRIVE_PID_I = 0.0;
    double DRIVETRAIN_DRIVE_PID_D = 0.0;

    // TODO: Maybe this should be removed and only defined in the command?
    double DRIVETRAIN_DRIVE_PID_SPEED = 0.0;

    double DRIVETRAIN_ROTATE_PID_P = 0.0;
    double DRIVETRAIN_ROTATE_PID_I = 0.0;
    double DRIVETRAIN_ROTATE_PID_D = 0.0;

    double SHOOTER_ACCELERATE_PID_P = 0.0;
    double SHOOTER_ACCELERATE_PID_I = 0.0;
    double SHOOTER_ACCELERATE_PID_D = 0.0;
}
