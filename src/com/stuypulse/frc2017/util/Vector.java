package com.stuypulse.frc2017.util;

import org.opencv.core.Point;

/**
 * 2-dimensional vectors.
 *
 * Polar forms of vectors use angles relative to the positive Y axis, where
 * clockwise is positive.
 *
 * TODO: cache values like magnitude and angle
 *
 */
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
     * Create a Vector from polar coordinates, CLOCKWISE from the POSITIVE Y axis
     */
    public static Vector fromPolar(double degs, double radius) {
        double rads = Math.toRadians(degs);
        return new Vector(
                Math.sin(rads) * radius,
                Math.cos(rads) * radius
                );
    }

    /**
     * @return the angle of {@code this} vector, CLOCKWISE from the
     * POSITIVE Y axis, in radians.
     */
    public double getRadians() {
        return Math.atan2(this.dx, this.dy);
    }

    /**
     * @return the angle of {@code this} vector, CLOCKWISE from the
     * POSITIVE Y axis, in degrees.
     */
    public double getDegrees() {
        return Math.toDegrees(Math.atan2(this.dx, this.dy));
    }

    /**
     * @return the magnitude of {@code this} vector
     */
    public double getMagnitude() {
        return Math.sqrt((this.dx * this.dx) + (this.dy * this.dy));
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
        return this.scaleBy(scalar);
    }

    /**
     * @param degs
     * @return {@code this} rotated {@code degs} degrees (clockwise).
     */
    public Vector rotateBy(double degs) {
        double result_degs = this.getDegrees() + degs;
        return Vector.fromPolar(result_degs, this.getMagnitude());
    }

    /**
     * @return the sum of {@code this} and {@code v}.
     */
    public Vector plus(Vector v) {
        return new Vector(this.dx + v.dx, this.dy + v.dy);
    }

    /**
     * @return the difference of {@code this} and {@code v}.
     */
    public Vector minus(Vector v) {
        return new Vector(this.dx - v.dx, this.dy - v.dy);
    }

    /**
     * @return {@code this} scaled by {@code factor}
     */
    public Vector scaleBy(double factor) {
        return new Vector(factor * this.dx, factor * this.dy);
    }

    /**
     * @param v
     * @return the complex product (treating the Vectors as complex
     * numbers) of {@code this} and {@code v}.
     */
    public Vector times(Vector v) {
        return new Vector(this.dx * v.dx, this.dy * v.dx + this.dx * v.dy);
    }

    public static Vector sum(Vector... vectors) {
        int dx = 0;
        int dy = 0;
        for (Vector v: vectors) {
            dx += v.dx;
            dy += v.dy;
        }
        return new Vector(dx, dy);
    }

    public static Vector avg(Vector... vectors) {
        return Vector.sum(vectors).scaleBy(1 / (double) vectors.length);
    }

    /**
     *
     * @return a point equivalent of the vector
     */
    public Point getPoint() {
        return new Point(dx, dy);
    }

    /////// Accessors and Modifiers /////////

    public double getX() {
        return dx;
    }

    public double getY() {
        return dy;
    }

    public String toString() {
        return "Magnitude (in): " + getMagnitude() + "\nDegrees: " + getDegrees() + "\ndx: " + dx + "\ndy: " + dy;
    }
}
