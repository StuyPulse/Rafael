package com.stuypulse.frc2017.util;

public class BoolBox {
    private boolean val;

    public BoolBox(boolean initialVal) {
        val = initialVal;
    }

    public boolean get() {
        return val;
    }

    public void set(boolean newVal) {
        val = newVal;
    }
}
