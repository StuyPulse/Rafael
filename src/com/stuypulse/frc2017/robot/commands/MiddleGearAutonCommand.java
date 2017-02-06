package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.auton.DriveForwardCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleGearAutonCommand extends CommandGroup {

    public MiddleGearAutonCommand() {
   
        	// i dont know which of the drive commands to use 9.5 ft requires encoders? 
        addSequential(new DriveForwardEncoderCommand());
        
        
        
        
        
        addSequential(new GearPushCommand(Robot.gearpusher.gear));
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
    }
}
