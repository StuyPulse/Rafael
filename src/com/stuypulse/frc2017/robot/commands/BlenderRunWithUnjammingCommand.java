package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Runs the blender, and tries to detect jams. If this command finds a jam,
 * it runs the blender backwards momentarily (constants in RobotMap) before
 * resuming at typical speed.
 *
 * Should be bound with whileHeld, as isFinished just returns false.
 */
public class BlenderRunWithUnjammingCommand extends Command {

    private double motorUnjamTime;
    private boolean motorIsUnjamming;

    public BlenderRunWithUnjammingCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.blender);
        requires(Robot.ballgate);
        motorUnjamTime = Timer.getFPGATimestamp();
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.ledBlenderSignal.stayOff();
        Robot.blender.run();
        Robot.ballgate.open();
        motorIsUnjamming = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Timer.getFPGATimestamp() - motorUnjamTime >= RobotMap.BLENDER_MOTOR_UNJAM_TIME) {
            motorIsUnjamming = false;
        }
        if (!motorIsUnjamming && !Robot.isAutoOverridden()) {
            if (Robot.blender.isMotorJammed()) {
                Robot.ledBlenderSignal.stayOff();
                Robot.blender.run();
                motorIsUnjamming = false;
            } else {
                Robot.ledBlenderSignal.stayOn();
                Robot.blender.setUnjamSpeed();
                motorIsUnjamming = true;
            }
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.ballgate.close();
        Robot.blender.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
