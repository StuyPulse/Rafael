package com.stuypulse.frc2017.robot.commands;

import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Oscilates, spinning forwards then backwards then forwards ect. over and over again
 * 
 * To be run continuously and set as default command
 */

public class BlenderSpinWiggleCommand extends Command {

    private double lastTime;
    
    private final double CYCLE_TIME = 1.0; // time till each switch in seconds 
    
    private double timeHoldingButton = 0.0; // Count

    private boolean direction = true; // start running forwards

    public BlenderSpinWiggleCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.blender);
        requires(Robot.ballgate);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        lastTime = Timer.getFPGATimestamp();
        Robot.ledBlenderSignal.stayOff();
        Robot.blender.run();
        Robot.ballgate.open();

    }

    // Called repeatedly when this Command is scheduled to run
    // lastTime - currentTime:
    //      the time INTERVAL of how long we were
    //      running the blender BETWEEN execute() calls (between each frame)
    protected void execute() {
        double now = Timer.getFPGATimestamp();

        /// INPUT
        boolean buttonPressed = Robot.oi.operatorPad.getRightBumper().get();
        // Add to time and run blender w/ ballgate if we are pressing button each frame
        if (buttonPressed) {
            
            Robot.ballgate.open();
            
            double delta = now - lastTime;
            timeHoldingButton += delta;
            /// CYCLING MOTOR
            // each cycle, flip the motor speed (make it move opposite)
            if (timeHoldingButton > CYCLE_TIME) {
                direction = !direction;
                timeHoldingButton = 0;
            }
            if (direction) {
                Robot.blender.run();
            } else {
                Robot.blender.runBackwards();
            }
        
        } else {
            Robot.ballgate.close();
            Robot.blender.stop();            
        }


        lastTime = Timer.getFPGATimestamp();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.ballgate.close();
        Robot.blender.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
