package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveForwardEncodersCommand extends EncoderDrivingCommand {

    private double inches;
    private boolean set;

    public DriveForwardEncodersCommand() {
        super();
        set = false;
    }

    public DriveForwardEncodersCommand(double inches) {
        super();
        this.inches = inches;
        set = true;
    }

    protected void setInchesToMove(double inches) {
        this.inches = inches;
    }

    @Override
    protected void setInchesToMove() {
        initialInchesToMove = set ? inches : SmartDashboard.getNumber("encoder-drive-inches", 0);
    }
}
