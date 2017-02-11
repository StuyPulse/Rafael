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

    int BOILER_CAMERA_PORT = 0;
    int LIFT_CAMERA_PORT = 1;

    /// Gamepad Ports

    int DRIVER_PAD_PORT = 0;
    int OPERATOR_PAD_PORT = 1;

    /// Ports

    int FRONT_LEFT_MOTOR_PORT = 1;
    int FRONT_RIGHT_MOTOR_PORT = 3;
    int BACK_LEFT_MOTOR_PORT = 2;
    int BACK_RIGHT_MOTOR_PORT = 4;

    int BLENDER_MOTOR_PORT = 6;

    int SHOOTER_MOTOR_PORT = 7;

    int WINCH_MOTOR_PORT = 5;

    int DRIVETRAIN_ENCODER_LEFT_CHANNEL_A = -1;
    int DRIVETRAIN_ENCODER_LEFT_CHANNEL_B = -1;
    int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_A = -1;
    int DRIVETRAIN_ENCODER_RIGHT_CHANNEL_B = -1;
    
    int BLENDER_ENCODER_CHANNEL_A = -1;
    int BLENDER_ENCODER_CHANNEL_B = -1;

    int GEAR_TRAP_RIGHT_SOLENOID_PORT = 4;
    int GEAR_TRAP_LEFT_SOLENOID_PORT = 3;
    int GEAR_PUSHER_SOLENOID_PORT = 2;
    int BALL_GATE_SOLENOID_PORT = 1;
    int GEAR_SHIFT_SOLENOID_PORT = 0;

    int IR_SENSOR_PORT = 0;
    
    //LED Ports (8, 9, 0)
    
    int BLENDER_LED_PORT = 0;
    int GEAR_LED_PORT = 8;
    //Port 9 is the remaining port left for LEDs
   
    boolean BLENDER_LED_ON_VALUE = false;
    boolean GEAR_LED_ON_VALUE = false;

    // Solenoid Channels

    int PCM_1 = 0;
    int PCM_2 = 0;
    int PCM_3 = 0;
    int PCM_4 = 0;
    int GEAR_SHIFT = 0;

    // Physical Constants

    double DRIVETRAIN_ENCODER_INCHES_PER_PULSE = 23.56;
    
    int BLENDER_ENCODER_PULSES_PER_REVOLUTION = 1024;
    
    // We use BLENDER_ENCODER_DEGREES_PER_PULSE as the encoder's distance-per-pulse.
    double BLENDER_ENCODER_DEGREES_PER_PULSE = 360.0 / (double) BLENDER_ENCODER_PULSES_PER_REVOLUTION;

    double SHOOTER_ENCODER_MAXSPEED = -1;

    double BLENDER_MOTOR_SPEED = 0.2; //TODO: Set ideal speed.
    
    double BLENDER_MOTOR_UNJAM_SPEED = -1.0;
    
    double BLENDER_MOTOR_UNJAM_TIME = 0.5;

    double PIOTR_DRIVE_MARGIN_OF_ERROR = -0.001;

    double PIOTR_DRIVE_TURN_ADJUSTMENT = 0.1;
    
    double BLENDER_CURRENT_THRESHOLD_FOR_JAM = 23; //Amperes
    
    double BLENDER_DEGREES_PER_PULSE_THRESHOLD_FOR_JAM = -1.0; //TODO: Find a suitable value for this.

    // TODO: Find Ideal Shooter Speed
    double SHOOTER_IDEAL_SPEED = -1;

    // Field Physical Constants
    double START_TO_BOILER_GEAR_TURN_DISTANCE = 114.3;
    double BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE = -60;
    double AFTER_TURN_TO_BOILER_GEAR_DISTANCE = 51;
    double BOILER_GEAR_REVERSE_DISTANCE = -51;

    double START_TO_MIDDLE_GEAR_DISTANCE = 114.3;
    double MIDDLE_GEAR_REVERSE_DISTANCE = -36;

    double BOILER_GEAR_TO_NEUTRAL_ZONE_ANGLE = -1;
    double BOILER_GEAR_TO_NEUTRAL_ZONE_DISTANCE = -1;
    double BOILER_GEAR_NEUTRAL_ZONE_TO_HP_ANGLE = -1;
    double BOILER_GEAR_NEUTRAL_ZONE_TO_HP_DISTANCE = -1;
    
    double HP_GEAR_TO_NEUTRAL_ZONE_ANGLE = -1;
    double HP_GEAR_TO_NEUTRAL_ZONE_DISTANCE = -1;
    
    double START_TO_HP_GEAR_TURN_DISTANCE = 114.3;
    double HP_GEAR_TURN_TO_HP_GEAR_ANGLE = 60;
    double AFTER_TURN_TO_HP_GEAR_DISTANCE = 51;
    double HP_GEAR_REVERSE_DISTANCE = -51;

    double START_TO_BASELINE_DISTANCE = -1; // 114.3
    double BASELINE_TO_HP_GEAR_DISTANCE = -1; // 51

    double MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_ANGLE = -1;
    double MIDDLE_GEAR_TO_AIRSHIP_HP_SIDE_DISTANCE = -1;

    double AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_ANGLE = -1;
    double AIRSHIP_HP_SIDE_TO_NEUTRAL_ZONE_DISTANCE = -1;

    double MOBILITY_TO_NEUTRAL_ZONE_DISTANCE = -1;

    double MOBILITY_TO_BASELINE_DISTANCE = -1;
    
    double MIDDLE_GEAR_TO_BOILER_ANGLE = 90;
    double MIDDLE_GEAR_TO_BOILER_DISTANCE = 109.7;
    double BACK_UP_TO_SHOOT_FROM_ALLIANCE_WALL_DISTANCE = -1;

    double BOILER_GEAR_REVERSE_SHOOTING_DISTANCE = -1;
    double BOILER_GEAR_TURN_TO_BOILER_ANGLE = -1;
    double BOILER_GEAR_TO_BOILER_DISTANCE =-1;
    
    double BOILER_BACK_UP_DISTANCE = -1;
    double BOILER_TURN_AROUND_ANGLE = -1;
    double BOILER_TO_BOILER_GEAR_DISTANCE = -1;

    double IR_SENSOR_THRESHOLD = 4.5;
    double IR_TIME_IN_MECHANISM_THRESHOLD = 0.5;
}
