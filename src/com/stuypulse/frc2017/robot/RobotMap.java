package com.stuypulse.frc2017.robot;

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
    int BOILER_CAMERA_PORT = 0;
    int LIFT_CAMERA_PORT = 1;

    ////////////////////////////////////////////////////////////////////////////
    // GAMEPAD PORTS: //////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int DRIVER_PAD_PORT = 0;
    int OPERATOR_PAD_PORT = 1;

    ////////////////////////////////////////////////////////////////////////////
    /// CANTALON PORTS and ENCODER CONVERSIONS: ////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Drivetrain
    int LEFT_TOP_MOTOR_PORT = 1;
    int LEFT_BOTTOM_MOTOR_PORT = 2;
    int RIGHT_TOP_MOTOR_PORT = 3;
    int RIGHT_BOTTOM_MOTOR_PORT = 4;

    int DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION = 256; // TODO: Test value
    double DRIVETRAIN_WHEEL_DIAMETER = 4.0;
    double DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION = Math.PI * DRIVETRAIN_WHEEL_DIAMETER;
    // We use BLENDER_ENCODER_DEGREES_PER_PULSE as the encoder's distance-per-pulse.
    double BLENDER_ENCODER_DEGREES_PER_PULSE = 360.0 / (double) RobotMap.BLENDER_ENCODER_PULSES_PER_REVOLUTION;

    // Blender
    int BLENDER_MOTOR_PORT = 6;
    int BLENDER_ENCODER_PULSES_PER_REVOLUTION = 1024; // TODO: Test value

    // Shooter
    int SHOOTER_MOTOR_A_PORT = 7;
    int SHOOTER_MOTOR_B_PORT = 8;

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
    int PCM_1 = 0;
    int PCM_2 = 0;
    int PCM_3 = 0;
    int PCM_4 = 0;
    int PCM_5 = 0;

    ////////////////////////////////////////////////////////////////////////////
    // PHYSICAL CONSTANTS: /////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    // TODO: this is used from two commands; confirm it works for both
    double BOILER_TO_HOPPER_BACKUP_DISTANCE = -158.4;

    // Blender
    double BLENDER_MOTOR_SPEED = 0.2; // TODO: Set ideal speed.
    double BLENDER_MOTOR_UNJAM_SPEED = -1.0; // Run backwards at max speed (-1.0 is not a placeholder)
    double BLENDER_MOTOR_UNJAM_TIME = 0.5;
    double BLENDER_CURRENT_THRESHOLD_FOR_JAM = 23; //Amperes

    double BLENDER_DEGREES_PER_PULSE_THRESHOLD_FOR_JAM = -1.0; //TODO: Find a suitable value for this.

    // Shooter
    double SHOOTER_IDEAL_SPEED = -1.0; // TODO: set shooter speed

    // Winch
    double WINCH_MOTOR_SPEED = 1.0;

    ////////////////////////////////////////////////////////////////////////////
    // LEDS: ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Ports
    int BLENDER_LED_PORT = 0;
    int GEAR_LED_PORT = 8;
    // The third LED is (supposed to be) on port 9, for future use.

    // On-values (i.e. which bit to send to indicate to turn the LED on)
    // TODO: test these values
    boolean BLENDER_LED_ON_VALUE = true;
    boolean GEAR_LED_ON_VALUE = true;

    ////////////////////////////////////////////////////////////////////////////
    // IR SENSOR: //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    int IR_SENSOR_PORT = 0;
    double IR_SENSOR_THRESHOLD = 4.5;
    double IR_TIME_IN_MECHANISM_THRESHOLD = 0.5;

    ////////////////////////////////////////////////////////////////////////////
    // PIOTR DRIVE: //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    double PIOTR_DRIVE_MARGIN_OF_ERROR = -0.001;
    double PIOTR_DRIVE_TURN_ADJUSTMENT = 0.1;
}

