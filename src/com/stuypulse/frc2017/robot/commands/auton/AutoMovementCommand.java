package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.util.BoolBox;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
abstract public class AutoMovementCommand extends Command {

    private boolean forceStoppedLocal;
    private BoolBox forceStoppedController;
    private boolean usingController;

    public AutoMovementCommand() {
        forceStoppedLocal = false;
        usingController = false;
    }

    public AutoMovementCommand(BoolBox controller) {
        forceStoppedController = controller;
        usingController = true;
    }

    protected boolean getForceStopped() {
        if (forceStoppedController == null) {
            return forceStoppedLocal;
        }
        return forceStoppedController.get();
    }

    private void setForceStopped(boolean newVal) {
        if (forceStoppedController == null) {
            forceStoppedLocal = newVal;
        } else {
            forceStoppedController.set(newVal);
        }
    }


    // Called just before this Command runs the first time
    protected void initialize() {
        if (!usingController) {
            forceStoppedLocal = false;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    abstract protected boolean isFinished();

    protected boolean externallyStopped() {
        return forceStoppedController != null && forceStoppedController.get();
    }

    // Called once after isFinished returns true
    abstract protected void end();

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    abstract protected void interrupted();
}