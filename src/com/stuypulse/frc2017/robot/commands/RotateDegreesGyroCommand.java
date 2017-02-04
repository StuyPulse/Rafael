package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double _angle;
    private boolean set;

    public RotateDegreesGyroCommand() {
        super();
        set = false;
    }

    public RotateDegreesGyroCommand(double angle) {
        super();
        _angle = angle;
        set = true;
    }

    public void setDesiredAngle(double angle) {
        _angle = angle;
    }

    protected void setDesiredAngle() {
        desiredAngle = set ? _angle : SmartDashboard.getNumber("gyro-rotate-degs");
    }

    protected void onEnd() {}
}