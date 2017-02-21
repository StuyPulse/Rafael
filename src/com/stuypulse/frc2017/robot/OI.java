package com.stuypulse.frc2017.robot;

import com.stuypulse.frc2017.robot.commands.BallGateCloseCommand;
import com.stuypulse.frc2017.robot.commands.BallGateOpenCommand;
import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;
import com.stuypulse.frc2017.robot.commands.BlenderSpinsBackwardsCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainLowGearCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherPushGearCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateIdealSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateMaximumSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateMinimumSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateSmartDashboardSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterStopCommand;
import com.stuypulse.frc2017.robot.commands.WinchStartMotorCommand;
import com.stuypulse.frc2017.robot.commands.auton.DoubleSequentialCommand;
import com.stuypulse.frc2017.util.Gamepad;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	public Gamepad driverPad;
	public Gamepad operatorPad;

	public OI() {
		driverPad = new Gamepad(RobotMap.DRIVER_PAD_PORT);
		operatorPad = new Gamepad(RobotMap.OPERATOR_PAD_PORT);
		//DriverPad Bindings
		//The right bumper is being used to gearshift
		driverPad.getRightBumper().whenPressed(new DriveTrainLowGearCommand());
		driverPad.getRightBumper().whenReleased(new DriveTrainHighGearCommand());

	     //Auton testing
        driverPad.getLeftTrigger().whenPressed(new RotateDegreesGyroCommand());
        driverPad.getRightTrigger().whenPressed(new DriveForwardEncodersCommand());

        //OperatorPad Bindings
		// Gear scoring:
        operatorPad.getBottomButton().whenPressed(new GearPusherPushGearCommand());
        operatorPad.getLeftButton().whenPressed(new GearPusherRetractGearCommand());
		operatorPad.getRightButton().whileHeld(new GearTrapReleaseGearCommand());
		operatorPad.getRightButton().whenReleased(new DoubleSequentialCommand(new GearPusherRetractGearCommand(), new GearTrapTrapGearCommand()));
		// Ball scoring:
		operatorPad.getRightTrigger().whileHeld(new DoubleSequentialCommand(new BallGateOpenCommand(), new BlenderRunWithUnjammingCommand()));
        operatorPad.getLeftButton().whenPressed(new ShooterAccelerateSmartDashboardSpeedCommand());
        operatorPad.getBottomButton().whenPressed(new ShooterStopCommand());

        // Climbing:
        operatorPad.getLeftTrigger().whileHeld(new WinchStartMotorCommand());

		// Manual overrides:
		operatorPad.getDPadUp().whenPressed(new ShooterAccelerateIdealSpeedCommand());
		operatorPad.getDPadDown().whenPressed(new ShooterStopCommand());
		operatorPad.getDPadLeft().whenPressed(new ShooterAccelerateMaximumSpeedCommand()); 
		operatorPad.getDPadRight().whenPressed(new ShooterAccelerateMinimumSpeedCommand());
		operatorPad.getLeftBumper().whenPressed(new BallGateOpenCommand());
		operatorPad.getRightBumper().whileHeld(new DoubleSequentialCommand(new BlenderSpinsBackwardsCommand(), new BallGateCloseCommand())); 
	}

    public boolean driverIsOverriding() {
        double joysticksMax = Math.max(Math.abs(driverPad.getLeftY()),
                Math.max(Math.abs(driverPad.getLeftX()),
                         Math.max(Math.abs(driverPad.getRightY()),
                                 Math.abs(driverPad.getRightX()))));
        return joysticksMax > 0.2;
    }
}
