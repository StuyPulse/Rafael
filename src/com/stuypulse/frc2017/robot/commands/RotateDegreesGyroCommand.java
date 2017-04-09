package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double angle;
    private boolean angleIsPreset;

    private boolean useAlliance;
    private int allianceDirection;

    public RotateDegreesGyroCommand() {
        this(0.0, false);
        angleIsPreset = false;
    }

    public RotateDegreesGyroCommand(double angle) {
        this(angle, false);
        /*super();
        this.angle = angle;
        angleIsPreset = true;*/
    }

    public RotateDegreesGyroCommand(double angle, boolean useAlliance) {
        super();
        this.angle = angle;
        this.useAlliance = useAlliance;
        allianceDirection = 1;
        angleIsPreset = true;
    }

    public void setDesiredAngle(double angle) {
        this.angle = angle;
    }

    @Override
    protected double getDesiredAngle() {
        return angleIsPreset ? (angle * allianceDirection) : SmartDashboard.getNumber("gyro-rotate-degs", 0);
    }

    @Override
    public void initialize() {
        if (useAlliance) {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
                allianceDirection = 1;
            } else {
                allianceDirection = -1;
            }
        }
    }

}
