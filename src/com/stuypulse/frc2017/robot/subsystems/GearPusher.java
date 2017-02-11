package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public Solenoid gearPusherPiston;
	// We are assuming there are only two states to this Solenoid.
	// The speed of the piston might play a role.

	private boolean pushed;

	public GearPusher() {
		gearPusherPiston = new Solenoid(RobotMap.PCM_3, RobotMap.GEAR_PUSHER_SOLENOID_PORT);
		pushed = false;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void toggle() {
		push(!pushed);
	}

	public void push(boolean push) {
		gearPusherPiston.set(push);
		pushed = push;
	}
	
	public void extend() {
		gearPusherPiston.set(true);
	}
	
	public void retract() {
		gearPusherPiston.set(false);
	}

}
