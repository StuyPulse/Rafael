
package com.stuypulse.frc2017.robot;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.stuypulse.frc2017.robot.cv.Camera;
import com.stuypulse.frc2017.robot.subsystems.BallGate;
import com.stuypulse.frc2017.robot.subsystems.Blender;
import com.stuypulse.frc2017.robot.subsystems.Drivetrain;
import com.stuypulse.frc2017.robot.subsystems.GearPusher;
import com.stuypulse.frc2017.robot.subsystems.GearTrap;
import com.stuypulse.frc2017.robot.subsystems.Shooter;
<<<<<<< HEAD
import com.stuypulse.frc2017.util.IRSensor;
=======
import com.stuypulse.frc2017.robot.subsystems.Winch;
>>>>>>> 639b236244a594e0c2f28ff2506ed5a32096035d

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
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

    public static Drivetrain drivetrain;
    public static GearPusher gearpusher;
    public static GearTrap geartrap;
    public static Shooter shooter;
    public static Blender blender;
    public static BallGate ballgate;
    public static Winch winch;

    public static OI oi;

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    UsbCamera boilerCamera;
    UsbCamera liftCamera;
    
    IRSensor irsensor;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        shooter = new Shooter();
        blender = new Blender();
        geartrap = new GearTrap();
        gearpusher = new GearPusher();
        ballgate = new BallGate();
        winch = new Winch();
        oi = new OI();
        irsensor = new IRSensor();
        // chooser.addDefault("Default Auto", new ExampleCommand());
        // chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);

        boilerCamera = new UsbCamera("Boiler Camera", 0);
        liftCamera = new UsbCamera("Lift Camera", 1);
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
        Scheduler.getInstance().run();
        SmartDashboard.putDouble("IRDistance", irsensor.getDistance());
        SmartDashboard.putDouble("IRVoltage", irsensor.getVoltage());
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
        autonomousCommand = chooser.getSelected();

        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector",
         * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
         * = new MyAutoCommand(); break; case "Default Auto": default:
         * autonomousCommand = new ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.start();
        
        //TODO: Set the speed to the ideal speed when it is known
        Robot.shooter.setSpeed(SmartDashboard.getNumber("Shooter speed", 0.0));
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null)
            autonomousCommand.cancel();

        boilerCamera.setResolution(160, 120);
        liftCamera.setResolution(160, 120);
        System.out.println("Set resolutions");

        CvSink boilerSink = new CvSink("Boiler Camera Sink");
        boilerSink.setSource(boilerCamera);
        System.out.println("Set boiler source");
        CvSink liftSink = new CvSink("Lift Camera Sink");
        liftSink.setSource(liftCamera);
        System.out.println("Set lift source");

        Mat boilerFrame = new Mat();
        Mat liftFrame = new Mat();

        Camera.configureCamera(0);
        Camera.configureCamera(1);

        boilerSink.grabFrame(boilerFrame);
        liftSink.grabFrame(liftFrame);
        System.out.println("Read frames");

        Imgcodecs.imwrite("/tmp/boiler.png", boilerFrame);
        Imgcodecs.imwrite("/tmp/lift.png", liftFrame);
        System.out.println("Wrote images");
        
        
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putDouble("IRDistance", irsensor.getDistance());
        SmartDashboard.putDouble("IRVoltage", irsensor.getVoltage());
        blender.updateCurrentValue();
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
}
