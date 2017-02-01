package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SonarTest extends Command {

    public SonarTest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.sonar);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        System.out.println("One: " +  Robot.sonar.getRangeInchesOne());
        System.out.println("Two: " + Robot.sonar.getRangeInchesTwo());
/*        System.out.println("Three: " +  Robot.sonar.getRangeInchesThree());
        System.out.println("Four: " + Robot.sonar.getRangeInchesFour());*/
//        System.out.println(Robot.sonar.activatedRight());
//        System.out.println(Robot.sonar.activatedLeft());
//        System.out.println("Angle: " + Robot.sonar.getAngle());
        System.out.println("================================");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
