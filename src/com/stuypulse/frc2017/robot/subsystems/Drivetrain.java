 package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDInterface;
import com.kauailabs.navx.frc.AHRS;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DrivetrainPiotrDriveCommand;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 *
 */

public class Drivetrain extends Subsystem {

    /**
     * Talon for left top motor. Has encoder on it.
     */
    private WPI_TalonSRX leftTopMotor;
    /**
     * Talon for right top motor. Has encoder on it.
     */
    private WPI_TalonSRX rightTopMotor;
    /**/
    private WPI_TalonSRX leftBottomMotor;
    private WPI_TalonSRX rightBottomMotor;
    private Solenoid gearShift;
    
    private DifferentialDrive differentialDrive;
    private SpeedControllerGroup leftSpeedController;
    private SpeedControllerGroup rightSpeedController;
    private boolean shifted;
    private AHRS gyro;  
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Drivetrain() {
        leftTopMotor = new WPI_TalonSRX(RobotMap.LEFT_TOP_MOTOR_PORT);
        rightTopMotor = new WPI_TalonSRX(RobotMap.RIGHT_TOP_MOTOR_PORT);
        leftBottomMotor = new WPI_TalonSRX(RobotMap.LEFT_BOTTOM_MOTOR_PORT);
        rightBottomMotor = new WPI_TalonSRX(RobotMap.RIGHT_BOTTOM_MOTOR_PORT);

        leftTopMotor.setNeutralMode(NeutralMode.Brake);
        rightTopMotor.setNeutralMode(NeutralMode.Brake);
        leftBottomMotor.setNeutralMode(NeutralMode.Brake);
        rightBottomMotor.setNeutralMode(NeutralMode.Brake);

        leftTopMotor.setInverted(true);
        rightTopMotor.setInverted(true);
        leftBottomMotor.setInverted(true);
        rightBottomMotor.setInverted(true);

        gearShift = new Solenoid(RobotMap.GEAR_SHIFT_SOLENOID_PORT);

        shifted = false;

        differentialDrive = new DifferentialDrive(leftSpeedController, rightSpeedController);

        //Encoders are located on the top motors on either of the motor complexes located on the left/right hemispheres.
        leftTopMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION, 0);
        rightTopMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, RobotMap.DRIVETRAIN_ENCODERS_PULSES_PER_REVOLUTION, 0);

        gyro = new AHRS(SPI.Port.kMXP);
        resetGyro();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DrivetrainPiotrDriveCommand());
    }

    public void tankDrive(double left, double right) {
        differentialDrive.tankDrive(left, right);
    }

    public void stop() {
        tankDrive(0, 0);
    }
    
    // TODO: UNITS MUST BE COMMENTED. (Degrees; positive is clockwise)
    public double gyroAngle() {
        return gyro.getAngle();
    }

    public void resetGyro() {
        gyro.reset();
    }

    public void resetEncoders() {
        leftTopMotor.setSelectedSensorPosition(0,0,0);
        rightTopMotor.setSelectedSensorPosition(0,0,0);
    }

    public double encoderDistance() {
        return Math.max(leftEncoderDistance(), rightEncoderDistance());
    }

    //getSelectedSensorPosition parameter replaced with 0. getPosition previously
    //for CANTalon did not have a parameter.
    public double leftEncoderDistance() {
        return (leftTopMotor.getSelectedSensorPosition(0) * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION)
                / RobotMap.DRIVETRAIN_ENCODERS_FACTOR;
    }

    public double rightEncoderDistance() {
        // Distance is scaled by -1.0 because right encoder was reporting
        // incorrect (negated) values
        return -1.0 * (rightTopMotor.getSelectedSensorPosition(0) * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION)
                / RobotMap.DRIVETRAIN_ENCODERS_FACTOR;
    }

    
   //getSelectedSensorVelocity is set to 0. Replaced by CANTalon's getSpeed, which had no parameter.
    public double leftEncoderSpeed() {
        return (leftTopMotor.getSelectedSensorVelocity(0) * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION)
        / RobotMap.DRIVETRAIN_ENCODERS_FACTOR;
    }

    public double rightEncoderSpeed() {
        return -1.0 * (rightTopMotor.getSelectedSensorVelocity(0) * RobotMap.DRIVETRAIN_ENCODERS_INCHES_PER_REVOLUTION)
        / RobotMap.DRIVETRAIN_ENCODERS_FACTOR;
    }

    public double avgAbsEncoderSpeed() {
        return (Math.abs(leftEncoderSpeed()) + Math.abs(rightEncoderSpeed())) / 2;
    }

    //Sets the solenoid to a shifted state manually
    public void manualGearShift(boolean shift) {
        gearShift.set(shift);
        shifted = shift;
    }

    public void highGearShift() {
        gearShift.set(false);
    }

    public void lowGearShift() {
        gearShift.set(true);
    }

    public double getLeftTopMotorCurrent() {
        return leftTopMotor.getOutputCurrent();
    }

    public double getRightBottomMotorCurrent() {
        return rightBottomMotor.getOutputCurrent();
    }

    public double getLeftBottomMotorCurrent() {
        return leftBottomMotor.getOutputCurrent();
    }

    public double getRightTopMotorCurrent() {
        return rightTopMotor.getOutputCurrent();
    }

}
