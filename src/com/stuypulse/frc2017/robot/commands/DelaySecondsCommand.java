package com.stuypulse.frc2017.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DelaySecondsCommand extends Command {

    private double time;

    public DelaySecondsCommand(double seconds) {
        setTimeout(seconds);
        time = seconds;
    }

    public DelaySecondsCommand(double seconds, Subsystem toRequire) {
        requires(toRequire);
        setTimeout(seconds);
        time = seconds;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        setTimeout(time);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
