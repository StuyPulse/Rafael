package com.stuypulse.frc2017.util.recorder.commands.examples;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.util.recorder.commands.RecorderRecordCommand;

/**
 *
 */
public class ExampleRecordCommand extends RecorderRecordCommand {

    private static final String SAVE_FILE_NAME = "recording.json";

    public ExampleRecordCommand() {
        super(SAVE_FILE_NAME, Robot.ghostOI, Robot.ghostOI.driverPad, Robot.ghostOI.operatorPad);
    }

    @Override
    protected boolean isFinished() {
        // Stop when we've pressed the dpad
        return Robot.oi.driverPad.getRawDPadDown();
    }

}
