package com.stuypulse.frc2017.robot.commands;

import com.ctre.CANTalon.TalonControlMode;
import com.stuypulse.frc2017.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterAcceleratePIDCommand extends Command {

    private double speed;
    private boolean auto;


    public ShooterAcceleratePIDCommand(double speed) {
        requires(Robot.shooter);
        this.speed = speed;
    }

    public ShooterAcceleratePIDCommand() {
        this(0.0);
        auto = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // Set up motor
        Robot.shooter.changeControlMode(TalonControlMode.Speed);
        Robot.shooter.setFollowerMode(true);
        if (auto) {
            speed = SmartDashboard.getNumber("pid-shooter-speed", 0.0);
            System.out.println("SHOOTER SPEED: " + speed +", yes this in fact is working");
        }

        Robot.shooter.setPIDF(
                SmartDashboard.getNumber("P ShooterAccelerate", 0.0),
                SmartDashboard.getNumber("I ShooterAccelerate", 0.0),
                SmartDashboard.getNumber("D ShooterAccelerate", 0.0),
                SmartDashboard.getNumber("F ShooterAccelerate", 0.0)
                );
        Robot.shooter.setSpeed(speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // Log
        SmartDashboard.putNumber("PID ShooterAccelerate OUTPUT", (Robot.shooter.getSpeed() - speed));
        System.out.println("SPD: " + Robot.shooter.getSpeed());
    }

    // Must be interrupted
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.shooter.stop();
        // Reset motor
        Robot.shooter.changeControlMode(TalonControlMode.PercentVbus);
        Robot.shooter.setFollowerMode(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
