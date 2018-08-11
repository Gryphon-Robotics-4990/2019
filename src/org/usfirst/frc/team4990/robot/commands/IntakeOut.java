package org.usfirst.frc.team4990.robot.commands;

import org.usfirst.frc.team4990.robot.RobotMap;
import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.Intake.BoxPosition;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeOut extends Command {
	private Intake intake;
	private boolean isFinished;
	private double speed = -0.6;
	
	/**
	 * Makes intake throw whatever is inside out
	 */
	
	public IntakeOut() {
		requires(RobotMap.intake);
		this.intake = RobotMap.intake;
		this.isFinished = false;
	}
	
	public void initialize() {
		//nothing.
	}
	
	public void execute() {
		BoxPosition boxPos = intake.getBoxPosition();
		if (boxPos.equals(BoxPosition.OUT)) {
			isFinished = true;
		} else if (boxPos.equals(BoxPosition.MOVING) || boxPos.equals(BoxPosition.IN)) {
			intake.setSpeed(speed);

		}
		if (this.isFinished) {
			intake.stop();
		}
		intake.update();
	}
	
	public boolean isFinished() {
		return isFinished;
	}
}
