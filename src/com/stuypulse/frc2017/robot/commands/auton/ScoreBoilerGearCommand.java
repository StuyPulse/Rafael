package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.Robot;
import com.stuypulse.frc2017.robot.commands.DriveInchesEncodersCommand;
import com.stuypulse.frc2017.robot.commands.DriveInchesPIDCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapTrapGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;
import com.stuypulse.frc2017.robot.commands.ScoreGearCommand;
import com.stuypulse.frc2017.robot.commands.cv.SetupForGearCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreBoilerGearCommand extends CommandGroup {
    public static final double START_TO_BOILER_GEAR_TURN_DISTANCE = 100.0;//114.3;
    public static final double BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE = -60;
    public static final double AFTER_TURN_TO_BOILER_GEAR_DISTANCE = 80;
    public static final double BOILER_GEAR_REVERSE_DISTANCE = -12;

    private boolean useCV;

    public ScoreBoilerGearCommand(boolean useCV) {
        this.useCV = useCV;
        int direction;
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveInchesPIDCommand(0.5, START_TO_BOILER_GEAR_TURN_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(direction * BOILER_GEAR_TURN_TO_BOILER_GEAR_ANGLE));

        if (useCV) {
            addSequential(new SetupForGearCommand());
        } else {
            addSequential(new DriveInchesPIDCommand(0.5, AFTER_TURN_TO_BOILER_GEAR_DISTANCE));
        }

        addSequential(new ScoreGearCommand());
        addSequential(new DriveInchesEncodersCommand(BOILER_GEAR_REVERSE_DISTANCE));
        addSequential(new GearTrapTrapGearCommand());
    }

    @Override
    public boolean isFinished() {
        // If we are in autonomous and CV did not find the goal, terminate this command
        // rather than dump out a gear.
        if (useCV && Robot.isAutonomous && !Robot.cvFoundGoal) {
            return true;
        }
        return super.isFinished();
    }
}
