package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.cv.LiftMath;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class SetupForGearCommand extends CommandGroup {

	public SetupForGearCommand() {
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

        addSequential(new ResetForceStopCommand());
	    addSequential(new RotateToLiftCommand());
	    addSequential(new RotateToLiftCommand(true));

        addSequential(new DriveToPegCommand());
        addSequential(new ResetForceStopCommand());
    }

    @Override
    public boolean isFinished() {
		//make public instead of protected so tat OptionalCVPositionForGearCommand can call
    	return super.isFinished();
	}

	@Override
	public void initialize() {
		//make public instead of protected so tat OptionalCVPositionForGearCommand can call
		super.initialize();
	}

	@Override
	public void execute() {
		//make public instead of protected so tat OptionalCVPositionForGearCommand can call
		super.execute();
	}

	@Override
	public void end() {
		//make public instead of protected so tat OptionalCVPositionForGearCommand can call
		super.end();
	}
	@Override
	public void interrupted(){
	  //make public instead of protected so tat OptionalCVPositionForGearCommand can call
	    super.interrupted();
	}

}

class SetVectors extends InstantCommand {

    private double DISTANCE_FROM_PEG = 10;

    protected void initialize() {
        Vector[] targetVectors = Robot.liftVision.processImageVectors();
        if (targetVectors != null) {
            Vector[] path = LiftMath.mTwoStep_getPath(targetVectors[0], targetVectors[1], DISTANCE_FROM_PEG, DISTANCE_FROM_PEG - 0.1);
            Robot.cvVector = path;
        }
    }
}
