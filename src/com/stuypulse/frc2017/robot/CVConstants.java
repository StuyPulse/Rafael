package com.stuypulse.frc2017.robot;

public interface CVConstants {

    double LIFT_HEIGHT_X_THRESHOLD = 5;

    // Position of top of a reflexite strip relative to the ground
    double LIFT_TARGET_Y = 13.0;

    // Distance from the center of the two reflexite targets to the floor
    double BOILER_TARGET_HEIGHT = 83.0;

    double CAMERA_VIEWING_ANGLE_X = 61.0; // angular width of frame

    double CAMERA_VIEWING_ANGLE_Y = 34.3; // angular height of frame

    double CAMERA_Y = 6; // TODO: SET EXACT

    double BOILER_CAMERA_TILT_ANGLE = 50.0;

    double CAMERA_FRAME_PX_HEIGHT = 270.0;

    double CAMERA_FRAME_PX_WIDTH = 360.0;

    double REFLEXITE_LENGTH = 5.0;

    // Taken from measurements
    // F = (P * D) / W
    double CAMERA_FOCAL_LENGTH_X = (26.5 * 29) / 2;
    double CAMERA_FOCAL_LENGTH_Y = (26.5 * 69) / 5;

    double DISTANCE_BETWEEN_REFLEXITE = 8.25;

    double PEG_LENGTH = 10.5 + 3.38;

    double PAST_PEG_DISTANCE = 3.0;
}
