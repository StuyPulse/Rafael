package com.stuypulse.frc2017.robot;

import com.stuypulse.frc2017.robot.subsystems.Blender;
import com.stuypulse.frc2017.util.BoolBox;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    public static Blender blender;
    public static SendableChooser<Boolean> cvChooser;

    public static OI oi;

    public static SendableChooser<Command> autonChooser;

    /**
     * This controls whether automatic functionality like extending the gear
     * pusher when the gear is detected, or auto-unjamming the blender, should
     * run. {@code isAutoOverriden} does NOT have any effect on CV alignment.
     */
    private static boolean isAutoOverridden;

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<Command>();

    public static BoolBox stopAutoMovement = new BoolBox(false);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        blender = new Blender();
        oi = new OI();
     
        setupSmartDashboardFields();
        setupAutonChooser();
    }

    private void setupSmartDashboardFields() {
    }

    private void setupAutonChooser(){
    	autonChooser = new SendableChooser<Command>();
    	SmartDashboard.putData("Auton Setting", autonChooser);
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
        // TODO: why the scheduler called here (in the default code)?
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
        // schedule the autonomous command
        autonomousCommand = autonChooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }

        // TODO: Set SHOOTER_IDEAL_SPEED to the ideal speed when it is known,
        // then set shooter speed to SHOOTER_IDEAL_SPEED here.

        // The gear-pusher piston, and the gear trap pistons, start *retracted*,
        // because when extended they reach outside the frame perimeter.
        // Thus we must immediately close the gear trap, then push the
        // gear pusher.
        // We do this in autonomousInit rather than a Command because it must
        // always happen, regardless of what comes next, and it is quick. This
        // is also why blocking the thread is appropriate:

        // Gear-shift physically starts in HIGH gear.
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        blender.checkForJam();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }
    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        blender.checkForJam();
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

    public static boolean isAutoOverridden() {
        return isAutoOverridden;
    }

    public static void setAutoOverridden(boolean override) {
        Robot.isAutoOverridden = override;
    }

    public static void toggleAutoOverridden() {
        Robot.isAutoOverridden = !Robot.isAutoOverridden;
    }
}
