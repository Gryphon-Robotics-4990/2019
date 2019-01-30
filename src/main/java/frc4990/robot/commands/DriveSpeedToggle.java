package frc4990.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc4990.robot.ShuffleboardController;

/**
 * Ported from old TeleopDriveController
 * @author benjamin and Class of '21 (created in 2018 season)
 *
 */

public class DriveSpeedToggle extends Command {
	public DriveSpeedToggle() {
	}
	
	public void initialize() {
		TeleopDriveTrainController.currentThrottleMultiplier = ShuffleboardController.getConst("DriveDpiToggle/lowThrottleMultiplier",
				0.5);
		System.out.println("Throttle Speed: " + TeleopDriveTrainController.currentThrottleMultiplier + "x");
	}
	
	public void end() {
		TeleopDriveTrainController.currentThrottleMultiplier = ShuffleboardController.getConst("DriveDpiToggle/defaultThrottleMultiplier",
				1.0);
		System.out.println("Throttle Speed: " + TeleopDriveTrainController.currentThrottleMultiplier + "x");
	}
	
	public void interrupted() {
		end();
	}
	
	public boolean isFinished() {
		return false;
	}
}
