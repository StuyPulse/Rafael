package com.stuypulse.frc2017.util.recorder;

import edu.wpi.first.wpilibj.Joystick;

/**
 * GhostGamepad.java
 * 
 * Clone of stuypulse's very own Gamepad class,
 * but fitted to GhostJoystick instead
 */
public class GhostGamepad extends GhostJoystick {

    public GhostGamepad(int port) {
        super(new Joystick(port));
    }

    /**
     * The left analog stick x-axis.
     *
     * @return value of left analog x-axis
     */
    public double getLeftX() {
        return getRawAxis(0);
    }

    /**
     * The left analog stick y-axis.
     *
     * @return value of left analog y-axis (pushing stick up is positive)
     */
    public double getLeftY() {
        return -getRawAxis(1);
    }

    /**
     * The right analog stick x-axis.
     *
     * @return value of right analog x-axis
     */
    public double getRightX() {
        return getRawAxis(2);
    }

    /**
     * The right analog stick y-axis.
     *
     * @return value of right analog y-axis (pushing stick up is positive)
     */
    public double getRightY() {
        return -getRawAxis(3);
    }

    /**
     * The upper d-pad button.
     *
     * @return if upper d-pad button is pressed
     */
    public boolean getRawDPadUp() {
        return getPOV() == 0;
    }

    public GhostDPadButton getDPadUp() {
        return new GhostDPadButton(this, GhostDPadButton.Direction.UP);
    }

    /**
     * The lower d-pad button.
     *
     * @return if the lower d-pad button is pressed
     */
    public boolean getRawDPadDown() {
        return getPOV() == 180;
    }

    public GhostDPadButton getDPadDown() {
        return new GhostDPadButton(this, GhostDPadButton.Direction.DOWN);
    }

    /**
     * The left d-pad button.
     *
     * @return if the left d-pad button is pressed
     */
    public boolean getRawDPadLeft() {
        return getPOV() == 270;
    }

    public GhostDPadButton getDPadLeft() {
        return new GhostDPadButton(this, GhostDPadButton.Direction.LEFT);
    }

    /**
     * The right d-pad button.
     *
     * @return if the right d-pad button is pressed
     */
    public boolean getRawDPadRight() {
        return getPOV() == 90;
    }

    public GhostDPadButton getDPadRight() {
        return new GhostDPadButton(this, GhostDPadButton.Direction.RIGHT);
    }

    /**
     * The left bumper.
     *
     * @return if the left bumper is pressed
     */
    public boolean getRawLeftBumper() {
        return getRawButton(5);
    }

    public GhostButton getLeftBumper() {
        return this.getButton(5);
    }

    /**
     * The right bumper.
     *
     * @return if the right bumper is pressed
     */
    public boolean getRawRightBumper() {
        return getRawButton(6);
    }

    public GhostButton getRightBumper() {
        return this.getButton(6);
    }

    /**
     * The left trigger.
     *
     * @return if the left trigger is pressed
     */
    public boolean getRawLeftTrigger() {
        return getRawButton(7);
    }

    public GhostButton getLeftTrigger() {
        return this.getButton(7);
    }

    /**
     * The right trigger.
     *
     * @return if the right trigger is pressed
     */
    public boolean getRawRightTrigger() {
        return getRawButton(8);
    }

    public GhostButton getRightTrigger() {
        return this.getButton(8);
    }

    /**
     * The left button of the button group. On some gamepads this is X.
     *
     * @return if the left button is pressed
     */
    public boolean getRawLeftButton() {
        return getRawButton(1);
    }

    public GhostButton getLeftButton() {
        return this.getButton(1);
    }

    /**
     * The bottom button of the button group. On some gamepads this is A.
     *
     * @return if the bottom button is pressed
     */
    public boolean getRawBottomButton() {
        return getRawButton(2);
    }

    public GhostButton getBottomButton() {
        return this.getButton(2);
    }

    /**
     * The right button of the button group. On some gamepads this is B.
     *
     * @return if the right button is pressed
     */
    public boolean getRawRightButton() {
        return getRawButton(3);
    }

    public GhostButton getRightButton() {
        return this.getButton(3);
    }

    /**
     * The top button of the button group. On some gamepads this is Y.
     *
     * @return if the top button is pressed
     */
    public boolean getRawTopButton() {
        return getRawButton(4);
    }

    public GhostButton getTopButton() {
        return this.getButton(4);
    }

    /**
     * The central left button. On some gamepads this is the select button.
     *
     * @return if the back button is pressed
     */
    public boolean getRawSelectButton() {
        return getRawButton(9);
    }

    public GhostButton getSelectButton() {
        return this.getButton(9);
    }

    /**
     * The central right button. On some gamepads this is the start button.
     *
     * @return if the start button is pressed
     */
    public boolean getRawStartButton() {
        return getRawButton(10);
    }

    public GhostButton getStartButton() {
        return this.getButton(10);
    }

    /**
     * The click-function of the left analog stick.
     *
     * @return if the left analog stick is being clicked down
     */
    public boolean getRawLeftAnalogButton() {
        return getRawButton(11);
    }

    public GhostButton getLeftAnalogButton() {
        return this.getButton(11);
    }

    /**
     * The click-function of the right analog stick.
     *
     * @return if the right analog stick is being clicked down
     */
    public boolean getRawRightAnalogButton() {
        return getRawButton(12);
    }

    public GhostButton getRightAnalogButton() {
        return this.getButton(12);
    }

    public static class GhostDPadButton extends GhostButton {
        public static enum Direction {
            UP, DOWN, LEFT, RIGHT
        }

        private GhostGamepad gamepad;
        private Direction direction;

        public GhostDPadButton(GhostGamepad gamepad, Direction direction) {
            super(-1, gamepad); // No port for dpads...
            this.gamepad = gamepad;
            this.direction = direction;
        }

        @Override
        public boolean get() {
            switch (direction) {
            case UP:
                return gamepad.getRawDPadUp();
            case DOWN:
                return gamepad.getRawDPadDown();
            case LEFT:
                return gamepad.getRawDPadLeft();
            case RIGHT:
                return gamepad.getRawDPadRight();
            default: // Never reached
                return false;
            }
        }
    }
}
