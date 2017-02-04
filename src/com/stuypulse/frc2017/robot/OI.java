package com.stuypulse.frc2017.robot;

import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainLowGearCommand;
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
		driverPad.getLeftTrigger().whenPressed(new DriveTrainLowGearCommand());
		driverPad.getLeftTrigger().whenReleased(new DriveTrainHighGearCommand());
		
	}
}
