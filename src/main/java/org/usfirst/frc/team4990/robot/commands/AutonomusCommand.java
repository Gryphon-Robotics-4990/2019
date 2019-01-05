package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.Robot;
import org.usfirst.frc.team4990.robot.Robot.StartingPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomusCommand extends CommandGroup {
	
	public AutonomusCommand() {
		
		StartingPosition s = Robot.startPos;
		System.out.println("Auto Logic INIT, startPos = "+ s.toString());
		
		if ((s != StartingPosition.STAY) && (s != StartingPosition.TEST)) {
			//if there is no game message (string) OR just cross auto line
			//System.out.println("Only Crossing Auto Line: GyroStraight((140/12), true)");
			addSequential(new RobotDriveStraight()); //forward 11 feet?
		} 
		
		switch (s) {
			case LEFT:
				//add left autonomus
				break;
			case CENTER:
				//add center autonomus
				return;
			case RIGHT:
				//add right autonomus
				return;
			case TEST:
				//add test autonomus
				return;
			default:
				return;
		}
	}
}
