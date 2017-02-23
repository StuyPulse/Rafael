package com.stuypulse.frc2017.robot;

public interface CVConstants {

    double LIFT_HEIGHT_X_THRESHOLD = 5;

    // Position of top of a reflexite strip relative to the ground
    double LIFT_TARGET_Y = 13.0;

    // Distance from the center of the two reflexite targets to the floor
    double BOILER_TARGET_HEIGHT = 83.0;

    double CAMERA_VIEWING_ANGLE_X = 61.0; // angular width of frame

    double CAMERA_VIEWING_ANGLE_Y = 34.3; // angular height of frame

    double LIFT_CAMERA_Y = 7.5;
    double BOILER_CAMERA_Y = 6; // TODO: Set exact

    double BOILER_CAMERA_TILT_ANGLE = 50.0;

    // lift camera is assumed to be horizontal

    double CAMERA_FRAME_PX_HEIGHT = 270.0;

    double CAMERA_FRAME_PX_WIDTH = 360.0;

    double REFLEXITE_LENGTH = 5.0;

    // Taken from measurements
    // F (focal length) = (P * D) / W
    // P = apparent width in the frame of an object (px)
    // D = distance from camera to the object
    // W = actual width of the object
    double CAMERA_FOCAL_LENGTH_X = (26.5 * 29) / 2;
    double CAMERA_FOCAL_LENGTH_Y = (26.5 * 69) / 5;

    double DISTANCE_BETWEEN_REFLEXITE = 8.25;

    // 10.5in is length of the actual peg itself; 3.38 is depth of the lift on
    // which the peg is mounted.
    double PEG_LENGTH = 10.5 + 3.38;

    double PAST_PEG_DISTANCE = 3.0;
}
