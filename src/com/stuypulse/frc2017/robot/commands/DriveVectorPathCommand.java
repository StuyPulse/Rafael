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

        DriveForwardEncodersCommand driveToFirstVector = new DriveForwardEncodersCommand();
        DriveForwardEncodersCommand driveToSecondVector = new DriveForwardEncodersCommand();

        addSequential(new UpdateVectorCommandsCommand(rotateToFirstVector, driveToFirstVector, rotateToSecondVector, driveToSecondVector));
        addSequential(rotateToFirstVector);
        addSequential(driveToFirstVector);

        addSequential(rotateToSecondVector);
        addSequential(driveToSecondVector);
    }
}

class UpdateVectorCommandsCommand extends InstantCommand {

    private RotateDegreesGyroCommand rotateToFirstVector;
    private DriveForwardEncodersCommand driveToFirstVector;
    private RotateDegreesGyroCommand rotateToSecondVector;
    private DriveForwardEncodersCommand driveToSecondVector;

    public UpdateVectorCommandsCommand(RotateDegreesGyroCommand rotateToFirstVector, DriveForwardEncodersCommand driveToFirstVector,
                                       RotateDegreesGyroCommand rotateToSecondVector, DriveForwardEncodersCommand driveToSecondVector) {
        this.rotateToFirstVector = rotateToFirstVector;
        this.driveToFirstVector = driveToFirstVector;
        this.rotateToSecondVector = rotateToSecondVector;
        this.driveToSecondVector = driveToSecondVector;
    }

    protected void initialize() {
        double angleToTurn = 90 - Robot.cvVector[0].getDegrees();
        if (Robot.cvVector[0].getDegrees() < 0) {
            angleToTurn -= 180;
        }
        System.out.println("cvVector[0]: " + Robot.cvVector[0].getDegrees());
        System.out.println("cvVector[1]: " + Robot.cvVector[1].getDegrees());

        rotateToFirstVector.setDesiredAngle(angleToTurn);
        System.out.println("Desired angle 1: " + angleToTurn);
        driveToFirstVector.setInchesToMove(Robot.cvVector[0].getMagnitude());

        boolean isNegativeOne = Robot.cvVector[0].getDegrees() < 0;
        boolean isNegativeTwo = Robot.cvVector[1].getDegrees() < 0;
        if (isNegativeOne && !isNegativeTwo) {
            angleToTurn = Math.abs(angleToTurn) + (90 - Robot.cvVector[1].getDegrees());
        } else if (!isNegativeOne && isNegativeTwo) {
            angleToTurn = -(angleToTurn + (90 - Math.abs(Robot.cvVector[1].getDegrees())));
        } else if (isNegativeOne == isNegativeTwo) {
            angleToTurn = Math.abs(Robot.cvVector[0].getDegrees() - Robot.cvVector[1].getDegrees());
            if (Math.abs(Robot.cvVector[0].getDegrees()) > Math.abs(Robot.cvVector[1].getDegrees())) {
                if (isNegativeOne) {
                    angleToTurn = -angleToTurn;
                }
            } else if (Math.abs(Robot.cvVector[0].getDegrees()) < Math.abs(Robot.cvVector[1].getDegrees())) {
                if (!isNegativeOne) {
                    angleToTurn = -angleToTurn;
                }
            }
        }

        rotateToSecondVector.setDesiredAngle(angleToTurn);
        System.out.println("Desired angle 2: " + angleToTurn);
        driveToSecondVector.setInchesToMove(Robot.cvVector[1].getMagnitude());
    }

}