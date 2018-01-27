package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.BlenderJoystickDriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Blender extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public static final int CURRENTS_TO_RECORD = 15;

    private static WPI_TalonSRX blenderMotor;

    // Values of current going to blenderMotor for past CURRENTS_TO_RECORD ticks
    private static double[] currentValues;

    // True when blender motor is jammed
    private boolean isJammed;

    public Blender() {
        blenderMotor = new WPI_TalonSRX(RobotMap.BLENDER_MOTOR_PORT);
        blenderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        currentValues = new double[CURRENTS_TO_RECORD];
        blenderMotor.setNeutralMode(NeutralMode.Brake);
        isJammed = false;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new BlenderJoystickDriveCommand());
    }

    public void joystickDrive(double speed) {
    	blenderMotor.set(speed);
    }

    public void run() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_SPEED);
    }

    public void runBackwards() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_SPEED);
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
        double blenderDegreesPerPulse = blenderMotor.getSensorCollection()
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
