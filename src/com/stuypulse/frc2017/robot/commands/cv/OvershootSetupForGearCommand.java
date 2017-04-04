package com.stuypulse.frc2017.robot.commands.cv;

import com.stuypulse.frc2017.robot.CVConstants;
import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class OvershootSetupForGearCommand extends CommandGroup {

    public OvershootSetupForGearCommand() {
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
        double angToLift = getAngle();
        addSequential(new ResetForceStopCommand());
        addSequential(new RotateDegreesGyroCommand(CVConstants.ROTATE_OFFSET_ANGLE));
        double gyroAngle = Robot.drivetrain.gyroAngle();
        addSequential(new RotateDegreesGyroCommand(-1 * gyroAngle + angToLift));
        addSequential(new DriveToPegCommand());
        addSequential(new ResetForceStopCommand());
    }

    public double getAngle() {
        Vector cvReading = Robot.liftVision.mTip_processImage(true);
        boolean foundGoal = cvReading != null;
        Robot.cvFoundGoal = foundGoal;
        if (foundGoal) {
            System.out.println(cvReading);
            return cvReading.getDegrees() + SmartDashboard.getNumber("angle-offset", 0.0);
        }
        System.out.println("No reading");
        return 0.0;
    }
}
