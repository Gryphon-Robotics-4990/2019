/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc4990.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.PrintCommand;
import frc4990.robot.commands.ClimbingSequence;
import frc4990.robot.commands.GrabHatchLimelight;
import frc4990.robot.commands.PIDTurretTurn;
import frc4990.robot.commands.PlaceHatchLimelight;
import frc4990.robot.commands.TeleopDriveTrainController;
import frc4990.robot.commands.TeleopDriveTrainController.StickShapingMode;
import frc4990.robot.commands.manualIntakeSequence;
import frc4990.robot.commands.manualOutakeSequence;
import frc4990.robot.components.F310Gamepad.Axis;
import frc4990.robot.components.F310Gamepad.Buttons;
import frc4990.robot.components.F310Gamepad.POV;
import frc4990.robot.components.CLimelight;
import frc4990.robot.components.JoystickAnalogButton;
import frc4990.robot.subsystems.Dashboard;
import frc4990.robot.subsystems.Turret.TurretPoint;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * 
 * @author Class of '21 (created in 2018 season)
 */
public class OI{

	// Drive Gamepad
	public static JoystickAnalogButton throttle = RobotMap.driveGamepad.getAxis(Axis.leftJoystickY);
	public static JoystickAnalogButton turnSteepness = RobotMap.driveGamepad.getAxis(Axis.rightJoystickX);

	public static Button frontPneumatics = RobotMap.driveGamepad.getButton(Buttons.rightBumper);
	public static Button rearPneumatics = RobotMap.driveGamepad.getButton(Buttons.leftBumper);

	public static Button climbingSequence = RobotMap.driveGamepad.getButton(Buttons.a);

	public static Button compressorToggle = RobotMap.driveGamepad.getButton(Buttons.start);

	public static Button driveSpeedToggle = RobotMap.driveGamepad.getButton(Buttons.x);
	public static Button turnSpeedToggle = RobotMap.driveGamepad.getButton(Buttons.b);

	public static JoystickAnalogButton shiftRight = RobotMap.driveGamepad.getAxis(Axis.rightTrigger);
	public static JoystickAnalogButton shiftLeft = RobotMap.driveGamepad.getAxis(Axis.leftTrigger);

	public static Button stickShapingToggle = RobotMap.driveGamepad.getButton(Buttons.y);

	public static Button driveControllerCheck = RobotMap.driveGamepad.getButton(Buttons.back);

	// OP Gamepad
	public static JoystickAnalogButton turretTurn = RobotMap.opGamepad.getAxis(Axis.leftJoystickX);
	public static Button turretForward = RobotMap.opGamepad.getButton(Buttons.y);
	public static Button turretLeft = RobotMap.opGamepad.getButton(Buttons.x);
	public static Button turretRight = RobotMap.opGamepad.getButton(Buttons.b);
	public static Button turretBack = RobotMap.opGamepad.getButton(Buttons.a);
	public static Button turretSafe = RobotMap.opGamepad.getButton(Buttons.start);
	public static Button turretReset = RobotMap.opGamepad.getPOVButton(POV.east);


	public static JoystickAnalogButton turretPneumatic = RobotMap.opGamepad.getAxis(Axis.rightJoystickX);
	public static POVButton hatchUp = RobotMap.opGamepad.getPOVButton(POV.north);
	public static POVButton hatchDown = RobotMap.opGamepad.getPOVButton(POV.south);

	public static Button manualIntakeSequence = RobotMap.opGamepad.getButton(Buttons.leftBumper);
	public static Button manualOutakeSequence = RobotMap.opGamepad.getButton(Buttons.rightBumper);

	public static JoystickAnalogButton limelightIntakeSequence = RobotMap.opGamepad.getAxis(Axis.leftTrigger);
	public static JoystickAnalogButton limelightOutakeSequence = RobotMap.opGamepad.getAxis(Axis.rightTrigger);

	public static Button limelightLight = RobotMap.opGamepad.getPOVButton(POV.west);

	public static Button opControllerCheck = RobotMap.opGamepad.getButton(Buttons.back);
	
	/* Controller Mapping:
		Drive Train: (drive controller)
		    Joysticks 1 and 2: forward/backward and turn left/right
			X button: toggle slow throttle
			B button: toggle slow turning
			Y button: toggle stick shaping methods (differential drive and 2018-season)
		
		Turret: (OP controller)
			POV pad: (up, down, left & right) move turret to point forwards, backwards, left & right
			A button: move turret to safe point (45 degrees) (inside frame perimiter)
		
		HatchClaw: (OP controller)
			B button: toggle hatch motor (90 degrees and 0 degrees)
			Left bumper (button): toggle HatchClaw pnuematic actuator
		
		Climbing Pneumatics: (Op controller)
			X Button: toggle front pneumatics
			Y Button: toggle rear pneumatics

		Compressor: (OP controller)
			Right bumper (button): toggle compressor (on until about 115 psi is reached or just off)

		
		Check which controller is which: (both)
		    START key (RIGHT Middle): prints in console which controller it is being pressed on
	 */
	
	public OI() {

		//drivetrain
		driveSpeedToggle.toggleWhenPressed(driveSpeedToggle());
		turnSpeedToggle.toggleWhenPressed(turnSpeedToggle());
		stickShapingToggle.whenPressed(stickShapingToggle());
		shiftLeft.whenPressed(new InstantCommand("shiftLeft", () -> {
			RobotMap.rightMotorGroup.coeff += 25;
			RobotMap.leftMotorGroup.coeff -= 25;
			System.out.println("[Drive Tuning] right coeff: " + RobotMap.rightMotorGroup.coeff + ", left coeff: " + RobotMap.leftMotorGroup.coeff);
		}));
		shiftRight.whenPressed(new InstantCommand("shiftRight", () -> {
			RobotMap.rightMotorGroup.coeff -= 25;
			RobotMap.leftMotorGroup.coeff += 25;
			System.out.println("[Drive Tuning] right coeff: " + RobotMap.rightMotorGroup.coeff + ", left coeff: " + RobotMap.leftMotorGroup.coeff);
		}));

		//Limelight
		//limelightLight.whenActive(new InstantCommand(() -> CLimelight.toggleLight()));

		//controller check (not needed, but useful)
		driveControllerCheck.toggleWhenPressed(new PrintCommand("START pressed on Drive Gamepad."));
		opControllerCheck.toggleWhenPressed(new PrintCommand("START pressed on OP Gamepad."));

		//turret
		//turretTurn is used in default command for Turret subsystem.
		turretForward.toggleWhenActive(new PIDTurretTurn(TurretPoint.Forward));
		turretLeft.toggleWhenActive(new PIDTurretTurn(TurretPoint.Left));
		turretRight.toggleWhenActive(new PIDTurretTurn(TurretPoint.Right));
		turretBack.toggleWhenActive(new PIDTurretTurn(TurretPoint.Back));
		turretSafe.toggleWhenActive(new PIDTurretTurn(TurretPoint.Safe));
		turretReset.whenPressed(new InstantCommand(() -> RobotMap.turret.resetPosition()));
    
		//Hatch
		turretPneumatic.whenPressed(RobotMap.turretPneumatic.toggleCommand());
		hatchUp.whenPressed(new InstantCommand(() -> RobotMap.hatchPneumatic.open()));
		hatchDown.whenPressed(new InstantCommand(() -> RobotMap.hatchPneumatic.close()));

		//Pneumatics
		frontPneumatics.whenPressed(RobotMap.frontSolenoid.toggleCommand());
		rearPneumatics.whenPressed(RobotMap.rearSolenoid.toggleCommand());
		compressorToggle.whenPressed(compressorToggle());

		//routines/sequences
		manualIntakeSequence.toggleWhenPressed(new manualIntakeSequence());
		manualOutakeSequence.toggleWhenPressed(new manualOutakeSequence());

		limelightOutakeSequence.toggleWhenPressed(new PlaceHatchLimelight());
		limelightOutakeSequence.toggleWhenPressed(new GrabHatchLimelight());

		climbingSequence.toggleWhenPressed(new ClimbingSequence());
	}
	
	public static InstantCommand stickShapingToggle() {
		return new InstantCommand("StickShapingToggle", (Runnable) () -> {
			TeleopDriveTrainController.stickShapingMode = (TeleopDriveTrainController.stickShapingMode == StickShapingMode.DifferentialDrive) ? 
			StickShapingMode.SquaredThrottle : StickShapingMode.DifferentialDrive;
			System.out.println("[StickShaping Method] Changed to:" + TeleopDriveTrainController.stickShapingMode.toString());
		});
		
	}

	public static InstantCommand driveSpeedToggle() {
		return new InstantCommand("DriveSpeedToggle", (Runnable) () -> {
			TeleopDriveTrainController.currentThrottleMultiplier = TeleopDriveTrainController.currentThrottleMultiplier == 
				Dashboard.getConst("DriveDpiToggle/lowThrottleMultiplier", 0.5) ? 
				TeleopDriveTrainController.currentThrottleMultiplier = Dashboard.getConst("DriveDpiToggle/defaultThrottleMultiplier", 1.0) : 
				Dashboard.getConst("DriveDpiToggle/lowThrottleMultiplier", 0.5);
			System.out.println("Throttle Speed: " + TeleopDriveTrainController.currentThrottleMultiplier + "x");
		});
	}

	public static InstantCommand turnSpeedToggle() {
		return new InstantCommand("TurnSpeedToggle", (Runnable) () -> {
			TeleopDriveTrainController.currentTurnSteepnessMultiplier = TeleopDriveTrainController.currentTurnSteepnessMultiplier == 
				Dashboard.getConst("TurnSpeedToggle/lowTurnMultiplier", 0.6) ? Dashboard.getConst("DriveDpiToggle/defaultTurnSpeedMultiplier", 1.0) : 
				Dashboard.getConst("TurnSpeedToggle/lowTurnMultiplier", 0.6);
			System.out.println("Turn Speed: " + TeleopDriveTrainController.currentTurnSteepnessMultiplier + "x");
		});
	}

	public static InstantCommand compressorToggle() {
		return new InstantCommand("CompressorToggle", (Runnable) () -> {
			RobotMap.compressor.setClosedLoopControl(!RobotMap.compressor.getClosedLoopControl());
			System.out.println(RobotMap.compressor.getClosedLoopControl() ? "Compressor off" : "Compressor holding pressure");
		});
	}
}
