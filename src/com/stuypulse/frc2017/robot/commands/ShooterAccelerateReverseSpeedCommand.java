package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ShooterAccelerateReverseSpeedCommand extends InstantCommand {

    public ShooterAccelerateReverseSpeedCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.shooter.setSpeed(-RobotMap.SHOOTER_MINIMUM_SPEED);
    }

}
