package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateDegreesGyroCommand extends GyroRotationalCommand {

    private double angle;

    // If angleIsPreset is false, the angle will be retrieved dynamically
    // from SmartDashboard
    private boolean angleIsPreset;

    private boolean useAlliance;
    private int directionMultiplier;

    /**
     * If `useAlliance` is true, the angle to rotate will be negated
     * if the alliance color (specified in DriverStation) is blue. The
     * alliance color is retrieved in `initialize()`, not in the constructor.
     */
    public RotateDegreesGyroCommand(double angle, boolean useAlliance) {
        super();
        this.angle = angle;
        this.useAlliance = useAlliance;
        directionMultiplier = 1;
        angleIsPreset = true;
    }

    public RotateDegreesGyroCommand() {
        this(0.0, false);
        angleIsPreset = false;
    }

    public RotateDegreesGyroCommand(double angle) {
        this(angle, false);
    }

    public void setDesiredAngle(double angle) {
        this.angle = angle;
    }

    @Override
    protected double getDesiredAngle() {
        if (angleIsPreset) {
            return directionMultiplier * angle;
        }
        return SmartDashboard.getNumber("gyro-rotate-degs", 0);
    }

    @Override
    public void initialize() {
        // Check alliance color in initialize() because if it
        // is checked in the constructor, it runs in robotInit,
        // which might be before the alliance color was set.
        if (useAlliance) {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
                directionMultiplier = 1;
            } else {
                directionMultiplier = -1;
            }
        }
        super.initialize();
    }

}
