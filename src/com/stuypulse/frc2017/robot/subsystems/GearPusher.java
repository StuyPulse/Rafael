package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.GearPusherPushGearCommand;
import com.stuypulse.frc2017.util.IRSensor;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

    public IRSensor irsensor;
	
	public DoubleSolenoid gearPusherPiston;
	// We are assuming there are only two states to this Solenoid.
	// The speed of the piston might play a role.

	private Value position;

	public GearPusher() {
	    irsensor = new IRSensor();
		gearPusherPiston = new DoubleSolenoid(RobotMap.GEAR_COVERER_SOLENOID_PORT, RobotMap.GEAR_PUSHER_SOLENOID_PORT);
		position = Value.kOff;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new GearPusherPushGearCommand());
	}

	public void toggle() {
		if(position == Value.kForward) {
			push(Value.kReverse);
		}
		else {
			push(Value.kForward);
		}
	}

	public void push(Value push) {
		gearPusherPiston.set(push);
		position = push;
	}
	
	public void extend() {
		push(Value.kForward);
	}
	
	public void retract() {
		push(Value.kReverse);
	}
	
	public boolean isExtended() {
		return position == Value.kForward;
	}

}
