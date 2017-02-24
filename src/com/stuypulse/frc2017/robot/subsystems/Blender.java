package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.BlenderStopCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public static final int CURRENTS_TO_RECORD = 15;

    private static CANTalon blenderMotor;

    // Values of current going to blenderMotor for past CURRENTS_TO_RECORD ticks
    private static double[] currentValues;

    // True when blender motor is jammed
    private boolean isJammed;

    public Blender() {
        blenderMotor = new CANTalon(RobotMap.BLENDER_MOTOR_PORT);
        blenderMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        blenderMotor.configEncoderCodesPerRev(RobotMap.BLENDER_ENCODER_PULSES_PER_REVOLUTION);
        currentValues = new double[CURRENTS_TO_RECORD];
        blenderMotor.enableBrakeMode(true);
        isJammed = false;
        blenderMotor.setInverted(true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new BlenderStopCommand());
    }

    public void run() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_SPEED);
    }

    public void runBackwards() {
        blenderMotor.set(-1.0 * RobotMap.BLENDER_MOTOR_SPEED);
    }

    public void setUnjamSpeed() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_UNJAM_SPEED);
    }

    public void stop() {
        blenderMotor.set(0.0);
    }

    public boolean isMotorJammed() {
        return isJammed;
    }

    private void updateCurrentValues() {
        for (int i = 0; i < currentValues.length - 1; i++) {
            currentValues[i] = currentValues[i + 1];
        }
        currentValues[currentValues.length - 1] = blenderMotor.getOutputCurrent();
    }

    public void checkForJam() {
        updateCurrentValues();
        //Finds array sum for the Average.
        int arraySum = 0;
        for (int arrayCounter = 0; arrayCounter < currentValues.length; arrayCounter++) {
            arraySum += currentValues[arrayCounter];
        }
        double currentArithmeticMean = arraySum / currentValues.length;
        double blenderDegreesPerPulse = blenderMotor.getAnalogInVelocity();
        //Checks whether the average is over the threshold for not jammed.
        boolean isCurrentHigh = currentArithmeticMean > RobotMap.BLENDER_CURRENT_THRESHOLD_FOR_JAM;
        boolean isSpeedHigh = blenderDegreesPerPulse > RobotMap.BLENDER_DEGREES_PER_PULSE_THRESHOLD_FOR_JAM;
        if (isCurrentHigh && isSpeedHigh) {
            isJammed = false;
        }
        if (isCurrentHigh && !isSpeedHigh) {
            isJammed = true;
        }
        if (!isCurrentHigh && isSpeedHigh) {
            isJammed = false;
        }
        if (!isCurrentHigh && !isSpeedHigh) {
            isJammed = false;
        }
    }
}
