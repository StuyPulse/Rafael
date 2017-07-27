package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WinchRunMotorSlowCommand extends Command {

    public WinchRunMotorSlowCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.winch);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.winch.startWinchSlow();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return !Robot.oi.operatorPad.getRightTrigger().get();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.winch.stopWinch();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
