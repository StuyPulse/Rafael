package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.GyroRotationalCommand;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateToLiftCommand extends GyroRotationalCommand {

    public RotateToLiftCommand() {
        super(false);
    }

    public RotateToLiftCommand(boolean gentle) {
        super(gentle);
    }

    public RotateToLiftCommand(boolean gentle, double tolerance) {
        super(gentle, tolerance);
    }

    @Override
    protected double getDesiredAngle() {
        Vector cvReading = Robot.liftVision.mTip_processImage(true);
        boolean foundGoal = cvReading != null;
        Robot.cvFoundGoal = foundGoal;
        if (foundGoal) {
            System.out.println(cvReading);
            return cvReading.getDegrees() + SmartDashboard.getNumber("angle-offset", 0.0);
        }
        System.out.println("No reading");
        return 0.0;
    }
}
