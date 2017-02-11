package com.stuypulse.frc2017.robot;

public interface CVConstants {
    // TODO
    // Tune threshold for the possible offset of the y coordinate of the target
    double LIFT_HEIGHT_X_THRESHOLD = 5;

    // Position of top of a reflexite strip relative to the ground
    double LIFT_TARGET_Y = 13.0;

    // Distance from the center of the two reflexite targets to the floor
    double BOILER_TARGET_HEIGHT = 83.0;

    double CAMERA_VIEWING_ANGLE_X = 61.0; // angular width of frame

    double CAMERA_VIEWING_ANGLE_Y = 34.3; // angular height of frame

    double CAMERA_Y = 7.25; // TODO: SET EXACT

    double BOILER_CAMERA_TILT_ANGLE = 50.0;

    double CAMERA_FRAME_PX_HEIGHT = 270.0;

    double CAMERA_FRAME_PX_WIDTH = 360.0;

    double REFLEXITE_LENGTH = 5.0;

    double CAMERA_FOCAL_LENGTH_X = CAMERA_FRAME_PX_WIDTH / (2 * Math.tan(Math.toRadians(CAMERA_VIEWING_ANGLE_X / 2)));

    double CAMERA_FOCAL_LENGTH_Y = CAMERA_FRAME_PX_HEIGHT / (2 * Math.tan(Math.toRadians(CAMERA_VIEWING_ANGLE_Y / 2)));

    double PEG_LENGTH = 10.5;

    public static void main(String[] args){
    	System.out.println(CAMERA_FOCAL_LENGTH_X + " pixel");
    	System.out.println(CAMERA_FOCAL_LENGTH_Y + " pixel");
    }
}
