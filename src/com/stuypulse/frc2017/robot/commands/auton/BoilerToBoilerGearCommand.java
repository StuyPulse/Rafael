package com.stuypulse.frc2017.robot.commands.auton;
import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.GearPusherRetractGearCommand;
import com.stuypulse.frc2017.robot.commands.GearTrapReleaseGearCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BoilerToBoilerGearCommand extends CommandGroup {

    public BoilerToBoilerGearCommand(boolean isRedAlliance) {
        // Add Commands here:
        int direction;
        if (isRedAlliance) {
            direction = 1;
        } else {
            direction = -1;
        }
        addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_BACK_UP_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_TURN_AROUND_ANGLE*direction)); //180 degrees
        addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_TO_BOILER_GEAR_DISTANCE));
    }
}
