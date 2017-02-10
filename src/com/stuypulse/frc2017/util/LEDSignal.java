package com.stuypulse.frc2017.util;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class LEDSignal {

    // Time from one blink-on to next blink-on is blinkPeriod in seconds.
    private static final double blinkPeriod = 1.5;

    private boolean lastSetOn;
    private boolean blinking;
    // Last time setBlinking(true) was called
    private double blinkingStart;

    private DigitalOutput light;
    private boolean lightOnValue;

    public LEDSignal(int port, boolean onValue) {
        light = new DigitalOutput(port);
        // In case, due to wiring, true is off and false is on, `lightOnValue`,
        // keeps track of this so `setOn(boolean on)` can abstract this out
        lightOnValue = onValue;
    }

    private void setLight(boolean on) {
        light.set(on ? lightOnValue : !lightOnValue);
        lastSetOn = on;
    }

    public void stayOn() {
        setLight(true);
        blinking = false;
    }

    public void stayOff() {
        setLight(false);
        blinking = false;
    }

    public boolean getBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blink) {
        if (blink) {
            setLight(false);
            blinking = true;
            blinkingStart = Timer.getFPGATimestamp();
        } else {
            blinking = false;
        }
    }

    public void tick() {
        if (blinking) {
            double now = Timer.getFPGATimestamp();
            double dt = (now - blinkingStart) % blinkPeriod;
            if (dt < blinkPeriod / 2) {
                // During the first half-period, the light is off
                if (lastSetOn) {
                    setLight(false);
                }
            } else {
                // During the second half-period, the light is on
                if (!lastSetOn) {
                    setLight(true);
                }
            }
        }
    }
}

