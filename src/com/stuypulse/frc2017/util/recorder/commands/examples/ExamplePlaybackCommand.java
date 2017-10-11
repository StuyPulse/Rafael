package com.stuypulse.frc2017.util.recorder.commands.examples;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.util.recorder.commands.RecorderPlaybackCommand;

/**
 *
 */
public class ExamplePlaybackCommand extends RecorderPlaybackCommand {

    private static final String SAVE_FILE_NAME = "recording.json";

    public ExamplePlaybackCommand() {
        super(SAVE_FILE_NAME, Robot.ghostOI, Robot.ghostOI.driverPad, Robot.ghostOI.operatorPad);
    }

}
