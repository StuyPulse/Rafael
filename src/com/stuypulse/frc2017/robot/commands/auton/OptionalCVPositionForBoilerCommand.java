package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForBoilerCommand;

import edu.wpi.first.wpilibj.command.Command;
/**
 *
 */
public class OptionalCVPositionForBoilerCommand extends Command {

    private SetupForBoilerCommand cvCommand;
    private DriveForwardEncodersCommand driveCommand;
    
    public OptionalCVPositionForBoilerCommand() {// Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (Robot.cvChooser.getSelected()) {
            cvCommand.initialize();

        } else {
            driveCommand.initialize();

        }
    }


    // Called repeatedly when this Command is scheduled to rune
    protected void execute() {
        if (Robot.cvChooser.getSelected()){
            cvCommand.execute();
        }else{
            driveCommand.execute();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (Robot.cvChooser.getSelected()){
                return cvCommand.isFinished();
               }else{
                return driveCommand.isFinished();
               }
    }

    // Called once after isFinished returns true
    protected void end() {
        if (Robot.cvChooser.getSelected()){
            cvCommand.end();
        }else
            driveCommand.end();
        }
    

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        if(Robot.cvChooser.getSelected()){
            cvCommand.interrupted();
        }else{
            driveCommand.interrupted();
        }
    }
}

