package com.stuypulse.frc2017.util.recorder;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.Timer;

/**
 * Recorder.java
 * Main recorder class
 * This records and stores input data
 * for a number of GhostJoysticks
 */
public class Recorder {

    private GhostJoystick[] gJoysticks;

    private GhostJoystickData[] gJoystickData;

    private GhostOI oi;

    // When we start the recording
    private double timeStart;

    public Recorder(GhostOI oi, GhostJoystick... gJoysticks) {
        this.oi = oi;
        this.gJoysticks = gJoysticks;
        gJoystickData = new GhostJoystickData[gJoysticks.length];
        for (int i = 0; i < gJoysticks.length; i++) {
            gJoystickData[i] = new GhostJoystickData(gJoysticks[i]);
        }
    }

    /* void startRecording()
     *    sets up everything for recording, and starts the timer
     */
    public void startRecording() {
        for (GhostJoystick gJoystick : gJoysticks) {
            gJoystick.setMirrorHost(true);
        }
        timeStart = Timer.getFPGATimestamp();
    }

    /* void tickRecordingUpdate()
     *    Call each frame in a command's "update" method
     *    updates all joysticks, moves the bot
     *    and grabs joystick inputs
     */
    public void tickRecordingUpdate() {
        updateJoysticks();
        double timestamp = Timer.getFPGATimestamp() - timeStart;
        for (GhostJoystickData data : gJoystickData) {
            data.addFrame(timestamp);
        }

        oi.updateDefaults();
    }

    /* void stopRecording()
     *    Stops our ghost joysticks from mirroring
     */
    public void stopRecording() {
        for (GhostJoystick gJoystick : gJoysticks) {
            gJoystick.setMirrorHost(false);
        }
        oi.cancelAllDefaultCommands();
    }

    /* GhostJoystickData[] getAllData()
     * 
     *    Returns all data that we've collected so far.
     *    It is recommended that you grab this data at the end
     *    of the recording, after calling "stopRecording"
     */
    public GhostJoystickData[] getAllData() {
        return gJoystickData;
    }

    /* void updateJoysticks()
     *    Updates all joysticks to mirror their hosts.
     *    This also updates the bindings, and thus calls the
     *    GhostOI bindings which moves the bot with your input
     */
    private void updateJoysticks() {
        for (GhostJoystick gJoystick : gJoysticks) {
            gJoystick.update();
        }
    }

    /**
     * GhostJoystickData
     * 
     * Holds input data for a single joystick
     * Each tick gets its own timestamp
     */
    public static class GhostJoystickData {

        // The ghost joystick we'll be grabbing data from
        public GhostJoystick gJoystick;

        // Linked list cause we don't need to hop around the data
        private LinkedList<GhostJoystickDataPoint> data;

        public GhostJoystickData(GhostJoystick gJoystick) {
            this.gJoystick = gJoystick;
            data = new LinkedList<GhostJoystickDataPoint>();
        }

        /* void addFrame()
         *   Adds one point of data, grabbing the frame of our joystick
         */
        public void addFrame(double timestamp) {
            data.addLast(new GhostJoystickDataPoint(
                    timestamp,
                    gJoystick));
        }

        /* GhostJoystickDataPoint peekFrame()
         *    peeks the earliest frame we have
         */
        public GhostJoystickDataPoint peekFrame() {
            return data.getFirst();
        }

        /* GhostJoystickDataPoint peekFrame()
         *    removes the earliest frame we have
         *    and gives it back to you
         */
        public GhostJoystickDataPoint popFrame() {
            GhostJoystickDataPoint result = peekFrame();
            data.removeFirst();
            return result;
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

        /* double getNextTimeStamp()
         * returns the timestamp of our next, latest data point
         */
        public double getNextTimeStamp() {
            if (!isEmpty())
                return peekFrame().timestamp;
            return -1; // This should not happen if you use this correctly
        }

        /**
         * GhostJoystickDataPoint
         * 
         * A class within a class within a class
         * Oh my!
         * 
         * Holds one frame of data from a joystick
         *
         */
        public class GhostJoystickDataPoint {
            public double timestamp;
            public boolean[] buttonData;
            public double[] axisData;
            public int pov;

            public GhostJoystickDataPoint(double timestamp, boolean[] buttonData, double[] axisData, int pov) {
                this.timestamp = timestamp;
                this.buttonData = buttonData;
                this.axisData = axisData;
                this.pov = pov;
            }

            // Secondary constructor that does the work of grabbing joystick data for you
            //    grabs the current state of the GhostJoystick into this datapoint
            public GhostJoystickDataPoint(double timestamp, GhostJoystick gJoystick) {
                this.timestamp = timestamp;
                buttonData = new boolean[gJoystick.getButtonCount()];
                axisData = new double[gJoystick.getAxisCount()];
                for (int bPort = 0; bPort < buttonData.length; bPort++) {
                    buttonData[bPort] = gJoystick.getRawButton(bPort);
                }
                for (int aPort = 0; aPort < axisData.length; aPort++) {
                    axisData[aPort] = gJoystick.getRawAxis(aPort);
                }

                pov = gJoystick.getPOV();
            }
        }
    }
}
