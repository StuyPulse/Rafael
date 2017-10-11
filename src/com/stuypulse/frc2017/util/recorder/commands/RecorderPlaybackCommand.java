package com.stuypulse.frc2017.util.recorder.commands;

import java.util.HashMap;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.stuypulse.frc2017.util.recorder.FileIO;
import com.stuypulse.frc2017.util.recorder.GhostJoystick;
import com.stuypulse.frc2017.util.recorder.GhostOI;
import com.stuypulse.frc2017.util.recorder.Recorder.GhostJoystickData;
import com.stuypulse.frc2017.util.recorder.Recorder.GhostJoystickData.GhostJoystickDataPoint;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * RecorderPlaybackCommand.java
 * A command that, given a pre-recorded file, plays back the recording,
 * mimicking joystick inputs
 */
public class RecorderPlaybackCommand extends Command {
    private String fileToLoad;
    private GhostOI oi;
    private GhostJoystick[] gJoysticks;

    private GhostJoystickData[] joystickData;

    // Timestamp for when this command began
    private double timeStart;

    public RecorderPlaybackCommand(String fileToLoad, GhostOI oi, GhostJoystick... gJoysticks) {
        this.fileToLoad = fileToLoad;
        this.oi = oi;
        this.gJoysticks = gJoysticks;
    }

    private void loadData() {
        // This hashmap is made to easily find a joystick by its port.
        //     it is only used here, to construct our joystickData array
        HashMap<Integer, GhostJoystick> portToJoystick = new HashMap<Integer, GhostJoystick>();
        for (GhostJoystick gJoystick : gJoysticks) {
            portToJoystick.put(gJoystick.host.getPort(), gJoystick);
        }

        try {
            JsonObject obj = JsonParser.object().from(FileIO.readFile("/home/lvuser/" + fileToLoad));
            JsonArray jsonJoysticks = obj.getArray("joysticks");

            joystickData = new GhostJoystickData[jsonJoysticks.size()];

            for (int i = 0; i < jsonJoysticks.size(); i++) {
                JsonObject jsonJoystick = jsonJoysticks.getObject(i);
                int port = jsonJoystick.getInt("port");
                GhostJoystick targetStick = portToJoystick.get(port);

                joystickData[i] = new GhostJoystickData(targetStick);

                JsonArray jsonFrames = jsonJoystick.getArray("frames");

                for (int frame = 0; frame < jsonFrames.size(); frame++) {
                    JsonObject jsonFrame = jsonFrames.getObject(frame);

                    double timestamp = jsonFrame.getDouble("timestamp");

                    JsonArray jsonButtons = jsonFrame.getArray("buttonData");
                    for (int b = 1; b <= jsonButtons.size(); b++) {
                        boolean buttonState = jsonButtons.getBoolean(b);
                        targetStick.setButtonValue(b, buttonState);
                    }
                    JsonArray jsonAxis = jsonFrame.getArray("axisData");
                    for (int a = 0; a < jsonAxis.size(); a++) {
                        double axisState = jsonAxis.getDouble(a);
                        targetStick.setAxisValue(a, axisState);
                    }
                    int pov = jsonFrame.getInt("pov");
                    targetStick.setPOV(pov);

                    // Since we fitted the joystick with proper data,
                    // adding the frame pulls everything we've put into it
                    joystickData[i].addFrame(timestamp);
                }
            }

        } catch (JsonParserException e) {
            // Something went wrong, do not proceed
            System.out.println("Failed to load JSON file for Playback");
            e.printStackTrace();
            cancel();
        }
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        System.out.println("[RecorderRecordCommand] START PLAYBACK");
        loadData();
        timeStart = Timer.getFPGATimestamp();
        // just to be safe, we make sure none of the joysticks mirror
        for (GhostJoystickData data : joystickData) {
            data.gJoystick.setMirrorHost(false);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double timestamp = Timer.getFPGATimestamp() - timeStart;
        // Keep on going until we are caught up with our current timestamp
        while (!joystickData[0].isEmpty() && joystickData[0].getNextTimeStamp() < timestamp) {
            for (GhostJoystickData data : joystickData) {
                // Update our current joystick with the proper inputs,
                //   then update the joystick itself with its bindings

                GhostJoystickDataPoint point = data.popFrame();

                for (int bPort = 0; bPort < point.buttonData.length; bPort++) {
                    data.gJoystick.setButtonValue(bPort, point.buttonData[bPort]);
                }
                for (int aPort = 0; aPort < point.axisData.length; aPort++) {
                    data.gJoystick.setAxisValue(aPort, point.axisData[aPort]);
                }

                // TODO: Verify if POV works with bindings
                data.gJoystick.setPOV(point.pov);

                // This should move the bot
                data.gJoystick.update();
                oi.updateDefaults();
            }

            // for higher accuracy, re-update time just in case 
            // (probably a bad idea, tbh)
            timestamp = Timer.getFPGATimestamp() - timeStart;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return joystickData[0].isEmpty();
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("[RecorderRecordCommand] STOP PLAYBACK");
        oi.cancelAllDefaultCommands();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        // Cannot be interrupted
    }
}
