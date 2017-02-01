package com.stuypulse.frc2017.robot;

public interface CVConstants {

    // TODO
    // Tune threshold for the possible offset of the y coordinate of the target
    double LIFT_HEIGHT_X_THRESHOLD = 5;

    // Position of top of a reflexite strip relative to the ground
    double LIFT_TARGET_Y = 13.25;

    double CAMERA_VIEWING_ANGLE_X = 61.0; // angular width of frame

    double CAMERA_VIEWING_ANGLE_Y = 34.3; // angular height of frame

    double CAMERA_Y = 7.5; // TODO: SET EXACT

    int CAMERA_FRAME_PX_HEIGHT = 270;

    int CAMERA_FRAME_PX_WIDTH = 360;

    double CAMERA_FOCAL_LENGTH_X = CAMERA_FRAME_PX_WIDTH / (2 * Math.tan(Math.PI / 180.0 * CAMERA_VIEWING_ANGLE_X / 2));

    double CAMERA_FOCAL_LENGTH_Y = CAMERA_FRAME_PX_HEIGHT / (2 * Math.tan(Math.PI / 180.0 * CAMERA_VIEWING_ANGLE_Y / 2));

}
