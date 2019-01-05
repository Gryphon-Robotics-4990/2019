/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc4990.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Ultrasonic;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc4990.robot.subsystems.DriveTrain;
import frc4990.robot.subsystems.F310Gamepad;
import frc4990.robot.subsystems.Gearbox;
import frc4990.robot.subsystems.TalonMotorController;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

public class RobotMap { 
	
	public static PowerDistributionPanel pdp;
	
	public static F310Gamepad driveGamepad;
	public static F310Gamepad opGamepad;
	public static TalonMotorController leftFrontDriveTalon;
	public static TalonMotorController leftRearDriveTalon;
	public static TalonMotorController rightFrontDriveTalon;
	public static TalonMotorController rightRearDriveTalon;
	
	public static Encoder leftEncoder;
	public static Encoder rightEncoder;
	
	public static Gearbox leftGearbox;
	public static Gearbox rightGearbox;
	
	public static SpeedControllerGroup leftMotorGroup;
	public static SpeedControllerGroup rightMotorGroup;
	//public static DifferentialDrive differentialDrive;
	
	public static DriveTrain driveTrain;
	
	public static DigitalOutput ultrasonicDigitalOutput;
	public static DigitalInput ultrasonicEchoDigitalInput;
	
	public static Ultrasonic ultrasonic;
	
	public static AHRS ahrs;
	
	public RobotMap() {
		
		pdp = new PowerDistributionPanel();
		
		driveGamepad = new F310Gamepad(0);
		opGamepad = new F310Gamepad(1);
		
		leftFrontDriveTalon = new TalonMotorController(1);
		leftRearDriveTalon = new TalonMotorController(2);
		rightFrontDriveTalon = new TalonMotorController(3);
		rightRearDriveTalon = new TalonMotorController(4);
		
		//leftEncoder = new MagneticEncoder(canBusID);
		//rightEncoder = new MagneticEncoder(canBusID);
		
		leftGearbox = new Gearbox(leftFrontDriveTalon, leftRearDriveTalon, leftEncoder);
		rightGearbox = new Gearbox(rightFrontDriveTalon, rightRearDriveTalon, rightEncoder);
		
		leftMotorGroup = new SpeedControllerGroup(leftFrontDriveTalon, leftRearDriveTalon);
		rightMotorGroup = new SpeedControllerGroup(rightFrontDriveTalon, rightRearDriveTalon);
		//differentialDrive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
					
		driveTrain = new DriveTrain(leftGearbox, rightGearbox);
		
		ultrasonicDigitalOutput = new DigitalOutput(4); //PING
		ultrasonicEchoDigitalInput = new DigitalInput(5); //ECHO
		
		ultrasonic = new Ultrasonic(ultrasonicDigitalOutput, ultrasonicEchoDigitalInput, Ultrasonic.Unit.kInches); 
	
		
		ahrs = new AHRS(SPI.Port.kMXP); 
		//navX-MXP RoboRIO extension and 9-axis gyro thingy
		//for simple gyro angles: use ahrs.getAngle() to get heading (returns number -n to n) and reset() to reset angle (and drift)
	} 
}