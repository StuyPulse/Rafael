package com.stuypulse.frc2017.util.recorder;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * GhostOI.java
 * designed to mimic OI.java
 * copy all OI commands here,
 * except replace your Gamepads or Joysticks
 * with GhostJoysticks or GhostGamepads.
 * 
 * You must use the GhostJoysticks or GhostGamepads
 * which you initialize here in your Recording and Playback Commands
 * in their super constructor arguments
 */

public class GhostOI {
    // Copy OI here and replace Joystick/Gamepad with its Ghost counterpart!

    // Holds a command and its required subsystems. That is all
    private class CommandSubsystemWrapper {
        public Command command;
        public Subsystem[] subsystems;

        public CommandSubsystemWrapper(Command command, Subsystem... subsystems) {
            this.command = command;
            this.subsystems = subsystems;
        }
    }

    // From a purely logistical standpoint, we will
    // only be adding to and iterating through this list.
    // Making it a LinkedList rather than an ArrayList
    // reduces the time it takes to add commands,
    // and offers no disadvantages.
    private LinkedList<CommandSubsystemWrapper> defaultCommands;

    /**
     * void addDefaultCommand(Command command)
     * 
     * Adds a default command that is run
     * automatically, mimicking the functionality
     * of a subsystem's "setDefaultCommand" function.
     * 
     * Each default command should us
     * GhostJoysticks or GhostGamepads if there is input
     * Involved
     */
    public void addDefaultCommand(Command command, Subsystem... required) {
        defaultCommands.addLast(new CommandSubsystemWrapper(command, required));
    }

    /**
     * void updateDefaults()
     * 
     * Updates all default commands, running them
     * as necessary.
     */
    public void updateDefaults() {
        for (CommandSubsystemWrapper pair : defaultCommands) {
            Command command = pair.command;
            Subsystem require[] = pair.subsystems;

            // if no subsystem is required, we're good to go
            if (require.length == 0) {
                if (!command.isRunning())
                    command.start();
                return;
            }

            // Whether all subsystems are free
            boolean allFree = true;

            for (Subsystem subsystem : require) {
                if (subsystem.getCurrentCommand() != null && subsystem.getCurrentCommand().isRunning()) {
                    allFree = false;
                    break;
                }
            }

            if (allFree)
                command.start();
        }
    }

    public void cancelAllDefaultCommands() {
        for (CommandSubsystemWrapper pair : defaultCommands) {
            if (pair.command.isRunning())
                pair.command.cancel();
        }
    }
}
