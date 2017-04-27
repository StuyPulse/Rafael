package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearPusher extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private boolean pushed;
    public Solenoid gearPusherPiston;

    public GearPusher() {
        gearPusherPiston = new Solenoid(RobotMap.PCM, RobotMap.GEAR_PUSHER_SOLENOID_PORT);
        pushed = false;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void toggle() {
        if (isPushed()) {
            retract();
        } else {
            extend();
        }
    }

    private void setPushed(boolean push) {
        gearPusherPiston.set(push);
        pushed = push;
    }

    public void extend() {
        setPushed(true);
    }

    public void retract() {
        setPushed(false);
    }

    public boolean isPushed() {
        return pushed;
    }

}
