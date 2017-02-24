package com.stuypulse.frc2017.robot.subsystems;

import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallGate extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Solenoid gatePiston;
    private boolean closed;

    public BallGate() {
        gatePiston = new Solenoid(RobotMap.PCM_4, RobotMap.BALL_GATE_SOLENOID_PORT);
        // TODO Find out what would be true and what would be false for this gate piston
        closed = true;
    }

    public void toggle() {
        setSolenoid(!closed);
    }

    public void setSolenoid(boolean close) {
        gatePiston.set(close);
        closed = close;
    }

    public void open() {
        gatePiston.set(true);
    }

    public void close() {
        gatePiston.set(false);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
