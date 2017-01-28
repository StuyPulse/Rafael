package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_VIEWING_ANGLE_X;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_VIEWING_ANGLE_Y;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_HEIGHT;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_WIDTH;

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
     * @param xPx Number of pixels in x
     * @return Corresponding angle difference along that height (in degrees)
     * 
     * This approximates the actual relationship between pixels and degrees as
     * a linear one. TODO: Use pinhole camera method to improve precision.
     */
    public static double frameXPxToDegrees(double dx) {
        return CAMERA_VIEWING_ANGLE_X * dx / CAMERA_FRAME_PX_WIDTH;
    }

    /**
     * @param yPx Number of pixels in y
     * @return Corresponding angle difference along that height (in degrees)
     * 
     * This approximates the actual relationship between pixels and degrees as
     * a linear one. TODO: Use pinhole camera method to improve precision.
     */
    public static double frameYPxToDegrees(double dy) {
        return CAMERA_VIEWING_ANGLE_Y * dy / CAMERA_FRAME_PX_HEIGHT;
    }
}