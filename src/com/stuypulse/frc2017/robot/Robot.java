package com.stuypulse.frc2017.robot;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.stuypulse.frc2017.robot.cv.BoilerVision;
import com.stuypulse.frc2017.robot.cv.Camera;
import com.stuypulse.frc2017.robot.cv.LiftVision;
import com.stuypulse.frc2017.robot.subsystems.BallGate;
import com.stuypulse.frc2017.robot.subsystems.Blender;
import com.stuypulse.frc2017.robot.subsystems.Drivetrain;
import com.stuypulse.frc2017.robot.subsystems.GearPusher;
import com.stuypulse.frc2017.robot.subsystems.GearTrap;
import com.stuypulse.frc2017.robot.subsystems.Shooter;
import com.stuypulse.frc2017.robot.subsystems.Winch;
import com.stuypulse.frc2017.util.IRSensor;
import com.stuypulse.frc2017.util.LEDSignal;
import com.stuypulse.frc2017.util.Vector;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.stuypulse.frc2017.robot.commands.auton.*;

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
    public static LEDSignal ledBlenderSignal;
    public static LEDSignal ledGearSensingSignal;

    public static OI oi;	
    
    public static SendableChooser<Command> autonChooser;

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<Command>();

    UsbCamera boilerCamera;
    UsbCamera liftCamera;

    public static LiftVision liftVision;

    IRSensor irsensor;

    public static Vector[] cvVector;
    public static BoilerVision boilerVision;

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
        ledBlenderSignal = new LEDSignal(RobotMap.BLENDER_LED_PORT, RobotMap.BLENDER_LED_ON_VALUE);
        ledGearSensingSignal = new LEDSignal(RobotMap.GEAR_LED_PORT, RobotMap.GEAR_LED_ON_VALUE);

        setupSmartDashboardFields();
        setupAutonChooser();

        boilerVision = new BoilerVision();

        boilerCamera = new UsbCamera("Boiler Camera", 0);
        liftCamera = new UsbCamera("Lift Camera", 1);
    }

    private void setupSmartDashboardFields() {
        SmartDashboard.putNumber("Shooter speed", RobotMap.SHOOTER_IDEAL_SPEED);
    }

    private void setupAutonChooser(){
    	autonChooser = new SendableChooser<Command>();
    	autonChooser.addDefault("Do Nothing", new CommandGroup());
    	autonChooser.addObject("Minimal Mobility", new MobilityMinimalCommand());
    	autonChooser.addObject("Minimal Mobility From Middle Gear Start", new MiddleGearMobilityMinimalCommand());
    	autonChooser.addObject("Only Mobility To HP Station", new MobilityToHPCommand());
    	autonChooser.addObject("Only Score HP Gear", new ScoreHPGearCommand());
    	autonChooser.addObject("Score HP Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreHPGearCommand(), new ApproachHPFromHPGearCommand()));
    	autonChooser.addObject("Only Score Middle Gear", new ScoreMiddleGearCommand());
    	autonChooser.addObject("Score Middle Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreMiddleGearCommand(), new ApproachHPFromMiddleGearCommand()));
    	autonChooser.addObject("Score Middle Gear THEN Shoot", new DoubleSequentialCommand(new ScoreMiddleGearCommand(), new ShootFromMiddleGearCommand()));
    	autonChooser.addObject("Only Score Boiler Gear", new ScoreBoilerGearCommand());
    	autonChooser.addObject("Score Boiler Gear THEN Approach HP Station", new DoubleSequentialCommand(new ScoreBoilerGearCommand(), new ApproachHPFromBoilerGearCommand()));
    	autonChooser.addObject("Score Boiler Gear THEN Shoot", new DoubleSequentialCommand(new ScoreBoilerGearCommand(), new ShootingFromBoilerGearCommand()));
    	autonChooser.addObject("Only Shoot", new ShootingFromAllianceWallCommand());
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
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }

        // TODO: Set SHOOTER_IDEAL_SPEED to the ideal speed when it is known,
        // then set shooter speed to SHOOTER_IDEAL_SPEED here.
        Robot.shooter.setSpeed(SmartDashboard.getNumber("Shooter speed", 0.0));
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        blender.checkForJam();
        irsensor.gearLEDSignalControl();
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

        // TODO: Remove old camera operations used for testing

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
        blender.checkForJam();
        irsensor.handleAutoGearPush();
        irsensor.gearLEDSignalControl();
        SmartDashboard.putNumber("IRDistance", irsensor.getDistance());
        SmartDashboard.putNumber("IRVoltage", irsensor.getVoltage());
        SmartDashboard.putNumber("Encoder drivetrain left", Robot.drivetrain.leftEncoderDistance());
        SmartDashboard.putNumber("Encoder drivetrain right", Robot.drivetrain.rightEncoderDistance());
        SmartDashboard.putNumber("Gyro angle", Robot.drivetrain.gyroAngle());
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }
}
