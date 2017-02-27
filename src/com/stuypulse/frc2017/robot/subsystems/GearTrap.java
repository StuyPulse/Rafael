package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearTrap extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Solenoid gearTrap;
    private boolean trapped;

    public GearTrap() {
        gearTrap = new Solenoid(RobotMap.PCM_1, RobotMap.GEAR_TRAP_SOLENOID_PORT);
        trapped = false; // Starts out un-trapped
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void toggle() {
        set(!trapped);
    }

    public void set(boolean trap) {
        gearTrap.set(trap);
        trapped = trap;
    }

    public void trap() {
        gearTrap.set(true);
    }

    public void release() {
        gearTrap.set(false);
    }

    public boolean isTrapped() {
        return trapped;
    }
}
