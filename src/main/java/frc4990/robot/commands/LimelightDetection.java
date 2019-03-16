package frc4990.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc4990.robot.RobotMap;
import frc4990.robot.subsystems.Turret.TurretPoint;
import frc4990.robot.components.CLimelight;
import frc4990.robot.components.CLimelight.DetectionMode;
import frc4990.robot.components.CLimelight.Pipeline;
import frc4990.robot.subsystems.DriveTrain;

public class LimelightDetection extends Command {

	private boolean isDone;

	public LimelightDetection() {
		requires(RobotMap.driveTrain);
		//requires(RobotMap.driveTrain);
	}

	public void initialize() {
		System.out.println("[Debug] Initializing.");
		RobotMap.driveTrain.configOpenloopRamp(0);
		CLimelight.setPipeline(Pipeline.Vision);
		isDone = false;
	}

	public void execute() {
		if (! CLimelight.hasValidTarget()) {
			System.out.println("[Debug] No valid target in frame.");
			return;//If no target is found
		}
		//This code will only run if there's a valid target.
		System.out.println("[Debug] Seeking target.");

		RobotMap.turret.controlDisabled = true;
		double hError = CLimelight.getCrosshairHorizontalOffset() * -1;
		if (Math.abs(hError) > RobotMap.LIMELIGHT_ACCURACY) {
			//horizontal (turret) error
			RobotMap.turret.setSpeed(clamp(hError * RobotMap.LimelightCorrectionkPH, -1, 1));
			System.out.println("[Debug] Correcting turret: " + hError);
		}
		else if (CLimelight.inRange()/* && RobotMap.turret.findNearestTurretPoint() == TurretPoint.Forward*/) {
			System.out.println("[Debug] Running Outake: " + hError);
			RobotMap.driveTrain.controlDisabled = true;//If limelight has target then take over control of drivetrain
			DriveTrain.setSpeed(0);
			if (CLimelight.detectionMode == DetectionMode.Intake) Scheduler.getInstance().add(new manualIntakeSequence());
			else Scheduler.getInstance().add(new manualOutakeSequence());
			isDone = true;
			end();
		}
	}
	
	public void end() {
		System.out.println("[Debug] Done.");
		RobotMap.driveTrain.configOpenloopRamp();
		DriveTrain.setSpeed(0);
		CLimelight.setPipeline(Pipeline.Driver.get());
		RobotMap.driveTrain.controlDisabled = false;
		RobotMap.turret.controlDisabled = false;
	}
	
	public void interrupted() {
		System.out.println("[Debug] Interrupted.");
		end();
	}
	
	public boolean isFinished() {
		return isDone;
	}

	private static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}
}
