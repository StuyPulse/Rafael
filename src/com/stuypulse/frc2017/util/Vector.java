package com.stuypulse.frc2017.util;

public class Vector {
    private final double dx;
    private final double dy;

    /**
     * Create a Vector from cartesian coordinates.
     */
    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Create a Vector from polar coordinates.
     */
    public static Vector fromPolar(double degs, double radius) {
        return new Vector(
                Math.cos(degs) * radius,
                Math.sin(degs) * radius
                );
    }

    /**
     * @return the angle of {@code this} vector
     */
    public double getDegrees() {
        return Math.atan2(this.dy, this.dx);
    }

    /**
     * @return the magnitude of {@code this} vector
     */
    public double getMagnitude() {
        return Math.sqrt(this.dx * this.dx + this.dy * this.dy);
    }

    // public double withDegrees(double degrees) {
    //     TODO
    // }

    /**
     * @param magnitude
     * @return the vector whose magnitude is {@code magnitude} and whose
     * direction is the same as {@code this}
     */
    public Vector withMagnitude(double magnitude) {
        double scalar = magnitude / this.getMagnitude();
        return this.scale(scalar);
    }

    /**
     * @param degs
     * @return {@code this} rotated {@code degs} degrees.
     */
    public Vector rotate(double degs) {
        double result_degs = this.getDegrees() + degs;
        return Vector.fromPolar(result_degs, this.getMagnitude());
    }

    /**
     * @return the sum of {@code this} and {@code v}.
     */
    public Vector add(Vector v) {
        return new Vector(this.dx + v.dx, this.dy + v.dy);
    }

    /**
     * @return the difference of {@code this} and {@code v}.
     */
    public Vector sub(Vector v) {
        return new Vector(this.dx - v.dx, this.dy - v.dy);
    }

    /**
     * @return {@code this} scaled by {@code factor}
     */
    public Vector scale(double factor) {
        return new Vector(factor * this.dx, factor * this.dy);
    }

    /**
     * @param v
     * @return the complex product (treating the Vectors as complex
     * numbers) of {@code this} and {@code v}.
     */
    public Vector mult(Vector v) {
        return new Vector(this.dx * v.dx, this.dy * v.dx + this.dx * v.dy);
    }

    public boolean equals(Object o) {
        if (o instanceof Vector) {
            Vector v = (Vector) o;
            return v.dx == this.dx && v.dy == this.dy;
        }
        return false;
    }
}

