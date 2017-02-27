package com.stuypulse.frc2017.util;

public class Error {
    private double[] hypo1 = new double[2];
    private double[] hypo2 = new double[2];
    private double[] hypo3 = new double[2];

    public Error(double angle) {
        hypo1[0] = angle;
        hypo1[1] = 1;
        hypo2[0] = 0;
        hypo2[1] = 0;
        hypo3[0] = 180;
        hypo3[1] = 0;
    }

    public void update(double angle) {
        double angle1 = Math.abs(hypo1[0] - angle);
        double angle2 = Math.abs(hypo2[0] - angle);
        double angle3 = Math.abs(hypo3[0] - angle);
        if ((angle1 > angle2) && (angle1 > angle3)) {
            double anglefin = hypo1[0] - angle;
            hypo1[0] -= anglefin / hypo1[1];
            hypo1[1] += 10;
        }
        if ((angle2 > angle1) && (angle2 > angle3)) {
            double anglefin = hypo2[0] - angle;
            hypo2[0] -= anglefin / hypo2[1];
            hypo2[1] += 10;
        }
        if ((angle3 > angle2) && (angle3 > angle1)) {
            double anglefin = hypo3[0] - angle;
            hypo3[0] -= anglefin / hypo3[1];
            hypo3[1] += 10;
        }
    }

    public double angler() {
        double average = hypo1[0] * hypo1[1] + hypo2[0] * hypo2[1]
                + hypo3[0] * hypo3[1];
        double totaltries = hypo1[1] + hypo2[1] + hypo3[1];
        return average / totaltries;
    }
}
