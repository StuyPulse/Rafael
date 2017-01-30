package com.stuypulse.frc2017.robot.subsystems;

import com.ctre.CANTalon;
import com.stuypulse.frc2017.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Blender extends Subsystem {
    public CANTalon blenderMotor;
    
    public Blender() {
        blenderMotor = new CANTalon(RobotMap.BLENDER_MOTOR_PORT);
    }
   

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

