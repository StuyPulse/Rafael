package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.commands.BallGateOpenCommand;
import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootCommand extends CommandGroup {

    public ShootCommand() {
    	
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
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
    	//A command to make the shooter motor run is not needed because it is always moving.
    	addSequential(new BlenderRunWithUnjammingCommand());
    	addSequential(new BallGateOpenCommand());
    }
}
