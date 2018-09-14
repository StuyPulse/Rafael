package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.BlenderJoystickDriveCommand;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Blender extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public static final int CURRENTS_TO_RECORD = 15;

    private static WPI_TalonSRX blenderMotor;
    private static WPI_TalonSRX blenderFeeder;
    // Values of current going to blenderMotor for past CURRENTS_TO_RECORD ticks
    private static double[] currentValues;

    // True when blender motor is jammed
    private boolean isJammed;

    public Blender() {
        blenderMotor = new WPI_TalonSRX(RobotMap.BLENDER_MOTOR_PORT);
        blenderFeeder = new WPI_TalonSRX(RobotMap.BLENDER_FEEDER_PORT);
        //TODO: FIX THE ENCODERS ASAP, I'M PATHETIC AND DON'T KNOW HOW TO DO THIS
        //blenderMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        //blenderFeeder.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        //blenderMotor.configEncoderCodesPerRev(RobotMap.BLENDER_ENCODER_PULSES_PER_REVOLUTION);
        currentValues = new double[CURRENTS_TO_RECORD];
        blenderMotor.setNeutralMode(NeutralMode.Brake);
        blenderFeeder.setNeutralMode(NeutralMode.Brake);
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
    	blenderFeeder.set(speed);
    }

    public void run() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_SPEED);
        blenderFeeder.set(1.0);
    }

    public void runBackwards() {
        blenderMotor.set(-1.0 * RobotMap.BLENDER_MOTOR_SPEED);
        blenderFeeder.set(-1.0);
    }

    public void setUnjamSpeed() {
        blenderMotor.set(RobotMap.BLENDER_MOTOR_UNJAM_SPEED);
        blenderFeeder.set(0.0);
    }

    public void stop() {
        blenderMotor.set(0.0);
        blenderFeeder.set(0.0);
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
        double blenderDegreesPerPulse = blenderMotor.getSelectedSensorVelocity(0);
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
