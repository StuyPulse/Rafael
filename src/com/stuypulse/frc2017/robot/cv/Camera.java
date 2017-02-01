package com.stuypulse.frc2017.robot.cv;

import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FOCAL_LENGTH_Y;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_HEIGHT;
import static com.stuypulse.frc2017.robot.CVConstants.CAMERA_FRAME_PX_WIDTH;

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
        return xCoor * CVConstants.CAMERA_VIEWING_ANGLE_X / CAMERA_FRAME_PX_WIDTH;
    	//return (xCoor - CAMERA_FRAME_PX_WIDTH / 2) * (CAMERA_VIEWING_ANGLE_X / CAMERA_FRAME_PX_WIDTH);
        //return (180 * Math.atan(xCoor - (CAMERA_FRAME_PX_WIDTH / 2 - 0.5)) / Math.PI)
        //        / CAMERA_FOCAL_LENGTH_X;
    }

    /**
     * @param yCoor Center y-coordinate of the reflexite strip.
     * @return Corresponding angle difference along that height (in degrees)
     * 
     * Uses pinhole method.
     */
    public static double frameYPxToDegrees(double yCoor) {
    	return yCoor * CVConstants.CAMERA_VIEWING_ANGLE_Y / CVConstants.CAMERA_FRAME_PX_HEIGHT;
        // return yPx * CAMERA_VIEWING_ANGLE_Y / CAMERA_FRAME_PX_HEIGHT;
        //return (180 * Math.atan(yCoor - (CAMERA_FRAME_PX_HEIGHT / 2 - 0.5)) / Math.PI)
        //        / CAMERA_FOCAL_LENGTH_Y;
    }
    
    public static void main(String[] args) {
    	System.out.println("360px in X: " + frameXPxToDegrees(180));
    	System.out.println("270px in Y: " + frameYPxToDegrees(135));
    	System.out.println("WITH CONV TO DEGS");
    	System.out.println("360px in X: " + LiftMath.radiansToDegrees(frameXPxToDegrees(180)));
    	System.out.println("270px in Y: " + LiftMath.radiansToDegrees(frameYPxToDegrees(135)));
    }
}
