package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.util.BoolBox;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
abstract public class AutoMovementCommand extends Command {

    private BoolBox forceStoppedController;

    public AutoMovementCommand() {
        forceStoppedController = Robot.stopAutoMovement;
    }

    public AutoMovementCommand(BoolBox controller) {
        forceStoppedController = controller;
    }

    public boolean getForceStopped() {
        return forceStoppedController.get();
    }

    // Called just before this Command runs the first time
    @Override
    abstract protected void initialize();

    // Called repeatedly when this Command is scheduled to run
    /**
     * Updates the forceStoppedController and calls inferiorExecute
     * if execution was not force-stopped.
     */
    @Override
    protected void execute() {
        if (Robot.oi.driverIsOverriding()) {
            forceStoppedController.set(true);
        }
        if (!getForceStopped()) {
            inferiorExecute();
        }
    }

    /**
     * Called in execute() only if Command was not force stopped.
     */
    abstract protected void inferiorExecute();

    @Override
    abstract protected boolean isFinished();

    // Called once after isFinished returns true
    @Override
    abstract protected void end();

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    abstract protected void interrupted();
}