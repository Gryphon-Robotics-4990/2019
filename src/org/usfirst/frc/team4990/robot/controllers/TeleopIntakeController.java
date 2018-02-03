package org.usfirst.frc.team4990.robot.controllers;

import org.usfirst.frc.team4990.robot.subsystems.Intake;
import org.usfirst.frc.team4990.robot.subsystems.F310Gamepad;

public class TeleopIntakeController {
	private Intake intake;
	private F310Gamepad gpad;
	
	public TeleopIntakeController(Intake i, F310Gamepad pad) {
		intake = i;
		gpad = pad;
	}
	
	public void update() {
		boolean lpressed = gpad.getLeftBumperPressed();
		boolean rpressed = gpad.getRightBumperPressed();
		
		if (lpressed && rpressed) {
			intake.stop();
		} else if(lpressed) {
			intake.in();
			return;
		} else if(rpressed) {
			intake.out();
			return;
		} else {
			intake.stop();
		}
		intake.update();
	}

}
