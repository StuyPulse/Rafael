package com.stuypulse.frc2017.robot.commands.auton;

import com.stuypulse.frc2017.robot.RobotMap;
import com.stuypulse.frc2017.robot.commands.BlenderRunWithUnjammingCommand;
import com.stuypulse.frc2017.robot.commands.DriveForwardEncodersCommand;
import com.stuypulse.frc2017.robot.commands.RotateDegreesGyroCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShootingFromBoilerGearCommand extends CommandGroup {

    public ShootingFromBoilerGearCommand(boolean isRedAlliance) {
        
        
        
        int direction;
        if (isRedAlliance) {
            direction = 1;
        } else {
            direction = -1;
       
        addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_REVERSE_SHOOTING_DISTANCE));
        addSequential(new RotateDegreesGyroCommand(RobotMap.BOILER_GEAR_TURN_TO_BOILER_ANGLE * direction));
        addSequential(new DriveForwardEncodersCommand(RobotMap.BOILER_GEAR_TO_BOILER_DISTANCE));
        //TODO: CV align w boiler here
        addSequential(new BlenderRunWithUnjammingCommand());
        
    }
}
}
