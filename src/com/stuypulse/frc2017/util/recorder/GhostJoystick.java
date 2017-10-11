package com.stuypulse.frc2017.util.recorder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * GhostJoystick.java
 * A class meant to emulate joystick functionality.
 * The axis and buttons on this joystick
 * can be manually assigned
 */

public class GhostJoystick {

    // The host joystick we are supposed to mimic/commit identity theft to
    public Joystick host;

    // Whether we mirror our host during update, copying its inputs
    // (turning this ghost joystick into a regular one, at least in functionality)
    private boolean mirrorHost;

    // Buttons and axi (is there a plural for axis?)
    private GhostButton[] buttons;
    private double[] axi;
    private int pov; // D-Pad

    public GhostJoystick(Joystick host) {
        this.host = host;

        // Init button + axis arrays and populate them if necessary
        buttons = new GhostButton[host.getButtonCount()];
        axi = new double[host.getAxisCount()];

        for (int i = 0; i < host.getButtonCount(); i++) {
            buttons[i] = new GhostButton(i + 1, this);
        }
    }

    // Constructor without host. Avoid using this, except for testing
    public GhostJoystick(int buttonCount, int axisCount) {
        // Init button + axis arrays and populate them if necessary
        buttons = new GhostButton[buttonCount];
        axi = new double[axisCount];

        for (int i = 0; i < buttonCount; i++) {
            buttons[i] = new GhostButton(i + 1, this);
        }
    }

    public void setMirrorHost(boolean mirrorHost) {
        this.mirrorHost = mirrorHost;
    }

    public GhostButton getButton(int port) {
        return buttons[port - 1];
    }

    public boolean getRawButton(int port) {
        return getButton(port).get();
    }

    public double getRawAxis(int port) {
        return axi[port];
    }

    public int getPOV() {
        return pov;
    }

    public int getButtonCount() {
        return buttons.length;
    }

    public int getAxisCount() {
        return axi.length;
    }

    public void setButtonValue(int port, boolean state) {
        getButton(port).setState(state);
    }

    public void setAxisValue(int port, double value) {
        axi[port] = value;
    }

    public void setPOV(int pov) {
        this.pov = pov;
    }

    /* void update()
     *  to be called once per robot tick
     *  it updates all buttons, and copies over host
     *  input if necessary
     */
    public void update() {
        // Set our button + axis values to our host values 
        //   if we are set to mirror our host
        if (mirrorHost) {
            for (int bPort = 1; bPort <= host.getButtonCount(); bPort++) {
                setButtonValue(bPort, host.getRawButton(bPort));
            }
            for (int aPort = 0; aPort < host.getAxisCount(); aPort++) {
                setAxisValue(aPort, host.getRawAxis(aPort));
            }

            pov = host.getPOV();
        }

        // Update all buttons
        for (GhostButton button : buttons) {
            button.update();
        }
    }

    // TODO: What exactly is a "static" class...
    //       can we have unique instances of static classes?
    //       Cause here we definitely need multiple unique instances
    public static class GhostButton {
        private int port; // <-- TODO: Is this needed?

        private GhostJoystick gJoystick;

        public boolean state;

        private boolean lastState;

        private Command whileHeldCommand;
        private Command whenPressedCommand;
        private Command whenReleasedCommand;

        public GhostButton(int port, GhostJoystick gJoystick) {
            this.port = port;
            this.gJoystick = gJoystick;
        }

        public void whileHeld(Command command) {
            whileHeldCommand = command;
        }

        public void whenPressed(Command command) {
            whenPressedCommand = command;
        }

        public void whenReleased(Command command) {
            whenReleasedCommand = command;
        }

        public boolean get() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        /* void update()
         *   Call every time the joystick updates.
         *   This controls when we call the whileHeld and whenPressed
         *   commands
         */
        public void update() {

            // if button is held
            if (state) {
                // if button has just been pressed
                if (!lastState) {
                    if (whenPressedCommand != null)
                        whenPressedCommand.start();
                }
                if (whileHeldCommand != null)
                    whileHeldCommand.start();
            } else {
                // if button is released and it was held last frame
                if (lastState) {
                    if (whenReleasedCommand != null)
                        whenReleasedCommand.start();
                }
            }

            lastState = state;
        }

        public GhostJoystick getGhostJoystick() {
            return gJoystick;
        }

    }
}
