package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_HEIGHT;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_VIEWING_ANGLE_Y;
import static com.stuypulse.frc2017.robot.CVConstants.BOILER_CAMERA_TILT_ANGLE;

import com.stuypulse.frc2017.robot.CVConstants;

import stuyvision.capture.DeviceCaptureSource;

public class Camera {

    private static final String pathToV4L2 = "/usr/bin/v4l2-ctl";

    public static boolean configureCamera(int port) {
        Runtime rt = Runtime.getRuntime();
        String cmdStart = pathToV4L2 + " -d " + port + " ";
        try {
            rt.exec(cmdStart + "-c exposure_auto=1,exposure_absolute=5,brightness=30,contrast=10,saturation=200,white_balance_temperature_auto=0,sharpness=50").waitFor();
            rt.exec(cmdStart + "-c white_balance_temperature=4624").waitFor();
            System.out.println("Finished configuring");
            return true;
        } catch (Exception e) {
            System.err.println("Setting v4l settings crashed!");
            return false;
        }
    }

    public static DeviceCaptureSource initializeCamera(int cameraPort) {
        DeviceCaptureSource camera = new DeviceCaptureSource(cameraPort);
        System.out.println("Made camera at " + cameraPort);
        configureCamera(cameraPort);
        camera.setBufferSize(2);
        return camera;
    }

    /**
     * @param xCoor Center x-coordinate of the reflexite strip.
     * @return Corresponding angle difference along that height (in degrees)
     *
     * Uses pinhole camera method.
     */
    public static double frameXPxToDegrees(double xCoor) {
        return Math.toDegrees(Math.atan(xCoor / CVConstants.CAMERA_FOCAL_LENGTH_X));
    }

    /**
     * @param yCoor Center y-coordinate of the reflexite strip.
     * @return Corresponding angle difference along that height (in degrees)
     *
     * Uses pinhole method.
     */
    public static double frameYPxToDegrees(double yCoor) {
        return yCoor / CAMERA_FRAME_PX_HEIGHT * CAMERA_VIEWING_ANGLE_Y;
    }

    public static double yInFrameToDegreesFromHorizon(double height) {
        return BOILER_CAMERA_TILT_ANGLE - frameYPxToDegrees(height);
    }

}
