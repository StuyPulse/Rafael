package com.stuypulse.frc2017.util.recorder.commands;

import com.grack.nanojson.JsonStringWriter;
import com.grack.nanojson.JsonWriter;
import com.stuypulse.frc2017.util.recorder.FileIO;
import com.stuypulse.frc2017.util.recorder.GhostJoystick;
import com.stuypulse.frc2017.util.recorder.GhostOI;
import com.stuypulse.frc2017.util.recorder.Recorder;
import com.stuypulse.frc2017.util.recorder.Recorder.GhostJoystickData;
import com.stuypulse.frc2017.util.recorder.Recorder.GhostJoystickData.GhostJoystickDataPoint;

import edu.wpi.first.wpilibj.command.Command;

/**
 * RecorderRecordCommand.java
 * A command that records joystick/gamepad input (without removing robot
 * functionality)
 * and stores that input into a file that can play back said recording
 * 
 * Inherited classes MUST define isFinished()
 * 
 */
public abstract class RecorderRecordCommand extends Command {

    private Recorder recorder;
    private String fileToSave;

    public RecorderRecordCommand(String fileToSave, GhostOI oi, GhostJoystick... gJoysticks) {
        this.fileToSave = fileToSave;

        recorder = new Recorder(oi, gJoysticks);

        setInterruptible(false); // TODO: Maybe this is useless. Figure out whether it's needed or not
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        recorder.startRecording();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        recorder.tickRecordingUpdate();
    }

    // Called once after isFinished returns true
    protected void end() {
        recorder.stopRecording();
        GhostJoystickData[] datas = recorder.getAllData(); // datas, cause screw grammar rules
        // TODO: Take this data and put it into JSON or another file format
        JsonStringWriter writer = JsonWriter.string().object();
        writer.array("joysticks");
        for (GhostJoystickData data : datas) {
            // ^^ this is why we used datas, cause "data" is convenient for singular items
            writer.object();
            writer.value("port", data.gJoystick.host.getPort());
            writer.array("frames");
            while (!data.isEmpty()) {
                GhostJoystickDataPoint dp = data.popFrame();
                writer.object();
                writer.value("timestamp", dp.timestamp);
                writer.array("buttonData");
                for (boolean val : dp.buttonData) {
                    writer.value(val);
                }
                writer.end();
                writer.array("axisData");
                for (double val : dp.axisData) {
                    writer.value(val);
                }
                writer.end();
                writer.value("pov", dp.pov);
                writer.end();
            }
            writer.end();
            writer.end();
        }
        writer.end();
        writer.end(); // end initial object creation
        String result = writer.done();

        FileIO.writeFile(result, fileToSave);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        // Does not interrupt
    }

}
