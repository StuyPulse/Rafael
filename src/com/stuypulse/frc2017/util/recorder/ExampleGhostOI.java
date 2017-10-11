package com.stuypulse.frc2017.util.recorder;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveTrainHighGearCommand;
import com.stuypulse.frc2017.robot.commands.DriveTrainLowGearCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherPushGearCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateIdealSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateMinimumSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterAccelerateReverseSpeedCommand;
import com.stuypulse.frc2017.robot.commands.ShooterStopCommand;
import com.stuypulse.frc2017.robot.commands.WinchRunMotorFastCommand;
import com.stuypulse.frc2017.robot.commands.WinchRunMotorSlowCommand;
import com.stuypulse.frc2017.robot.commands.auton.ScoreHPGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;
import com.stuypulse.frc2017.util.recorder.commands.examples.GhostBlenderJoystickDriveCommand;
import com.stuypulse.frc2017.util.recorder.commands.examples.GhostDrivetrainPiotrDriveCommand;

/**
 * I literally just CTRL+C CTRL+V'd our entire OI,
 * and replaced Gamepad with GhostGamepad
 * 
 * I also made duplicates of all Default commands
 * and added them using "addDefaultCommand"
 */
public class ExampleGhostOI extends GhostOI {

    public GhostGamepad driverPad, operatorPad;

    public ExampleGhostOI() {
        driverPad = new GhostGamepad(RobotMap.DRIVER_PAD_PORT);
        operatorPad = new GhostGamepad(RobotMap.OPERATOR_PAD_PORT);
        ////////////////////////////////////////////////////////////////////////
        // Driver Pad Bindings /////////////////////////////////////////////////

        // Joysticks and right/left triggers used for PiotrDrive

        // Gear shift:
        driverPad.getRightBumper().whenPressed(new DriveTrainLowGearCommand());
        driverPad.getRightBumper().whenReleased(new DriveTrainHighGearCommand());

        // CV Lift:
        //driverPad.getLeftBumper().whenPressed(new RunAutoCommand(new RotateToLiftCommand()));

        // CV Boiler [unbound because not tested]
        // driverPad.getRightButton().whenPressed(new RunAutoCommand(new RotateToBoilerCommand()));
        // driverPad.getBottomButton().whenPressed(new SetupForBoilerCommand());

        // CV testing bindings:
        //driverPad.getDPadLeft().whenPressed(new RunAutoCommand(new RotateDegreesGyroCommand()));
        //driverPad.getDPadDown().whenPressed(new RunAutoCommand(new DriveInchesEncodersCommand()));
        //driverPad.getBottomButton().whenPressed(new DriveForwardCommand(1.0));
        //driverPad.getDPadUp().whenPressed(new SetupForGearCommand());
        //driverPad.getDPadRight().whenPressed(new RunAutoCommand(new RotateToLiftCommand()));
        //driverPad.getStartButton().whenPressed(new ProcessTestImageCommand());

        driverPad.getDPadLeft().whenPressed( new ScoreHPGearCommand(true, false));
        driverPad.getDPadRight().whenPressed( new ScoreHPGearCommand(true, true));
        driverPad.getDPadUp().whenPressed(new SetupForGearCommand());

        // PID testing bindings
        //driverPad.getTopButton().whenPressed(new RunAutoCommand(new DriveInchesPIDCommand()));
        //driverPad.getRightButton().whenPressed(new RunAutoCommand(new RotateDegreesGyroCommand()));

        ////////////////////////////////////////////////////////////////////////
        // Operator Pad Bindings ///////////////////////////////////////////////

        // Gear scoring:
        operatorPad.getBottomButton().whenPressed(new GearPusherPushGearCommand());
        operatorPad.getBottomButton().whenReleased(new GearPusherRetractGearCommand());

        operatorPad.getRightButton().whenPressed(new GearTrapReleaseGearCommand());
        operatorPad.getRightButton().whenReleased(new GearTrapTrapGearCommand());

        operatorPad.getTopButton().whenPressed(new ScoreGearCommand());
        operatorPad.getTopButton().whenReleased(new GearTrapTrapGearCommand());

        // Shooter:
        operatorPad.getDPadDown().whenPressed(new ShooterStopCommand());
        operatorPad.getDPadRight().whenPressed(new ShooterAccelerateReverseSpeedCommand());
        operatorPad.getDPadUp().whenPressed(new ShooterAccelerateIdealSpeedCommand());
        operatorPad.getDPadLeft().whenPressed(new ShooterAccelerateMinimumSpeedCommand());

        // Ball scoring: We're not using it for china :(
        //operatorPad.getRightBumper().whileHeld(new BlenderSpinBackwardCommand());
        //operatorPad.getRightTrigger().whileHeld(new BlenderSpinCommand());
        // NOTE: We use whileHeld here, and do not specify a whenReleased,
        // because the default command of the blender runs off of joystick
        // output. When one of these commands isn't running, control of the
        // blender goes to the joystick.

        // Climbing:
        operatorPad.getLeftTrigger().whenPressed(new WinchRunMotorFastCommand());
        operatorPad.getRightTrigger().whenPressed(new WinchRunMotorSlowCommand());
        //depricated down below: ends in command
        //operatorPad.getLeftTrigger().whenReleased(new WinchStopMotorCommand());

        // Flaps
        // depricated operatorPad.getLeftBumper().whenPressed(new HopperFlapToggleCommand());

        // ADD DEFAULTS: This is the only thing besides the default OI code
        addDefaultCommand(new GhostDrivetrainPiotrDriveCommand(), Robot.drivetrain);
        addDefaultCommand(new GhostBlenderJoystickDriveCommand(), Robot.blender);

    }
}
