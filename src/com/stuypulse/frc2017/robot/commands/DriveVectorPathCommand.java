package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class DriveVectorPathCommand extends CommandGroup {

    public DriveVectorPathCommand() {
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

        RotateDegreesGyroCommand rotateToFirstVector = new RotateDegreesGyroCommand();
        RotateDegreesGyroCommand rotateToSecondVector = new RotateDegreesGyroCommand();

        DriveInchesEncodersCommand driveToFirstVector = new DriveInchesEncodersCommand();
        DriveInchesEncodersCommand driveToSecondVector = new DriveInchesEncodersCommand();

        addSequential(new UpdateVectorCommandsCommand(rotateToFirstVector, driveToFirstVector, rotateToSecondVector,
                driveToSecondVector));
        addSequential(rotateToFirstVector);
        addSequential(driveToFirstVector);

        addSequential(rotateToSecondVector);
        //addSequential(driveToSecondVector);
    }
}

class UpdateVectorCommandsCommand extends InstantCommand {

    private RotateDegreesGyroCommand rotateToFirstVector;
    private DriveInchesEncodersCommand driveToFirstVector;
    private RotateDegreesGyroCommand rotateToSecondVector;
    private DriveInchesEncodersCommand driveToSecondVector;

    public UpdateVectorCommandsCommand(RotateDegreesGyroCommand rotateToFirstVector,
            DriveInchesEncodersCommand driveToFirstVector,
            RotateDegreesGyroCommand rotateToSecondVector, DriveInchesEncodersCommand driveToSecondVector) {
        this.rotateToFirstVector = rotateToFirstVector;
        this.driveToFirstVector = driveToFirstVector;
        this.rotateToSecondVector = rotateToSecondVector;
        this.driveToSecondVector = driveToSecondVector;
    }

    @Override
    protected void initialize() {
        double angleToTurn = Robot.cvVector[0].getDegrees();

        System.out.println("cvVector[0]: " + Robot.cvVector[0].getDegrees());
        System.out.println("cvVector[1]: " + Robot.cvVector[1].getDegrees());

        rotateToFirstVector.setDesiredAngle(angleToTurn);
        System.out.println("Desired angle 1: " + angleToTurn);
        driveToFirstVector.setInchesToMove(Robot.cvVector[0].getMagnitude());
        System.out.println("Desired distance 1: " + Robot.cvVector[0].getMagnitude());

        angleToTurn = Robot.cvVector[1].getDegrees() - Robot.cvVector[0].getDegrees();
        if (Robot.cvVector[0].getDegrees() > 0 && Robot.cvVector[1].getDegrees() > 0) {
            if (Robot.cvVector[1].getDegrees() > Robot.cvVector[0].getDegrees()) {
                angleToTurn = -angleToTurn;
            }
        }

        rotateToSecondVector.setDesiredAngle(angleToTurn);
        System.out.println("Desired angle 2: " + angleToTurn);
        driveToSecondVector.setInchesToMove(Robot.cvVector[1].getMagnitude());
    }

}
