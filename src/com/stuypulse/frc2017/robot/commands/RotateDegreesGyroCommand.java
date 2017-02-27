package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double angle;
    private boolean angleIsPreset;

    public RotateDegreesGyroCommand() {
        super();
        angleIsPreset = false;
    }

    public RotateDegreesGyroCommand(double angle) {
        super();
        this.angle = angle;
        angleIsPreset = true;
    }

    public void setDesiredAngle(double angle) {
        this.angle = angle;
    }

    @Override
    protected double getDesiredAngle() {
        return angleIsPreset ? angle : SmartDashboard.getNumber("gyro-rotate-degs", 0);
    }
}
