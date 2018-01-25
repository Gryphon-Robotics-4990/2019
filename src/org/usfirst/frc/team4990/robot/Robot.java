package org.usfirst.frc.team4990.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4990.robot.controllers.*;
import org.usfirst.frc.team4990.robot.controllers.SimpleAutoDriveTrainScripter.StartingPosition;
import org.usfirst.frc.team4990.robot.subsystems.*;
import org.usfirst.frc.team4990.robot.subsystems.motors.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	//public Command autoCommand;
	//public SendableChooser autoChooser;
	
	private Preferences prefs;
	private F310Gamepad driveGamepad;
	private DriveTrain driveTrain;
	
	private SimpleAutoDriveTrainScripter autoScripter;
	
	private TeleopDriveTrainController teleopDriveTrainController;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	System.out.println("Version 1.9.2018.6.33");
    	this.prefs = Preferences.getInstance();
    	
    	this.driveGamepad = new F310Gamepad(1);
    	
    	this.driveTrain = new DriveTrain( 
    		new TalonMotorController(0),
    		new TalonMotorController(1),
    		new TalonMotorController(2),
    		new TalonMotorController(3),
    		0, 1, 2, 3);
    	
    	/*autoChooser = new SendableChooser();
    	autoChooser.addObject("Left",  new SimpleAutoDriveTrainScripter(driveTrain, StartingPosition.LEFT));
    	autoChooser.addDefault("Middle", new SimpleAutoDriveTrainScripter(driveTrain, StartingPosition.MID));
    	autoChooser.addObject("Right",  new SimpleAutoDriveTrainScripter(driveTrain, StartingPosition.RIGHT));
    	SmartDashboard.putData("Auto Location Chooser", autoChooser);*/
    	
    }

    public void autonomousInit() {
    	/*autoCommand = (Command) autoChooser.getSelected();
    	autoCommand.start();*/
    	StartingPosition s = StartingPosition.MID;
    	autoScripter = new SimpleAutoDriveTrainScripter(driveTrain, s);
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	autoScripter.update();
    	driveTrain.update();
    	Scheduler.getInstance().run();
    }
    
    public StartingPosition autoRoboStartLoc() {
    	int trigger = -1;
    	for (int i = 1; i < 4; i++) {
    		if (! SmartDashboard.getBoolean("DB/Button " + i, false)) {
    			trigger = i;
    		}
    	}
    	if (trigger == -1) { trigger = 0;}
    	for (int i = 1; i < 4; i++) {
    		SmartDashboard.putBoolean("DB/Button " + i, false);
    		SmartDashboard.putBoolean("DB/LED " + i, false);
    	}
    	SmartDashboard.putBoolean("DB/LED " + trigger, true);
    	
    	
    	switch(trigger) {
    	case 0:
    		System.err.println("YA MESSED UP?");
    		break;
    	case 1:
    		return StartingPosition.LEFT;
    	case 2:
    		return StartingPosition.MID;
    	case 3:
    		return StartingPosition.RIGHT;
    	}
    	return StartingPosition.ERROR;
    }
    
    public void teleopInit() {
    	this.teleopDriveTrainController = new TeleopDriveTrainController(
        		this.driveGamepad, 
        		this.driveTrain, 
        		this.prefs.getDouble("maxTurnRadius", Constants.defaultMaxTurnRadius),
        		this.prefs.getBoolean("reverseTurningFlipped", false),
        		this.prefs.getDouble("smoothDriveAccTime", Constants.defaultAccelerationTime),
        		this.prefs.getDouble("lowThrottleMultiplier", .25),
        		this.prefs.getDouble("maxThrottle", 1.0));
    }
     
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
	    this.teleopDriveTrainController.updateDriveTrainState();
	    
	    //ever heard of the tale of last minute code
	    //I thought not, it is not a tale the chairman will tell to you

    	this.driveTrain.update();
    }
}
