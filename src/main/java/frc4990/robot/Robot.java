package frc4990.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc4990.robot.commands.PIDTurretTurn;
import frc4990.robot.components.CLimelight;
import frc4990.robot.components.CLimelight.Pipeline;
import frc4990.robot.subsystems.Turret.TurretPoint;

//This entire robot code is dedicated to Kyler Rosen, a friend, visionary, and a hero to the empire that was the Freshmen Union of 2018

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

	public static RobotMap robotMap;

	//public static Command autonomusCommand;

	public static OI oi;

	public static Notifier processThread = new Notifier(() -> Robot.resetSensors());

	/**
	 * This function is run when the robot is first started up and 
	 * should be used for any initialization code.
	 * @author Team 4990
	 */

	public void robotInit() { 
		System.out.println("Initializing Robot.");

		robotMap = new RobotMap();

		oi = new OI();

		processThread.startSingle(0);

		System.out.println("Robot Initialized.");
	}

	public void robotPeriodic() {
		// Careful!
	}

	public void disabledInit() {
		/*if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}*/

		CLimelight.setPipeline(Pipeline.Driver.get());
		System.out.println("Disabled Init complete");
	}

	public void disabledPeriodic() { // This function is run periodically when the robot is DISABLED. Be careful.

	}

	public void autonomousInit() { // This function is called at the start of autonomous
		/*autonomusCommand = new AutonomusCommand();
		autonomusCommand.start();
		*/
		CLimelight.setPipeline(Pipeline.Driver.get());
		if (Math.abs(RobotMap.turretTalon.getPosition()) < 1000) { Scheduler.getInstance().add(new PIDTurretTurn(TurretPoint.Forward)); }
		System.out.println("Auto Init complete");
	}

	public void autonomousPeriodic() { // This function is called periodically during autonomous
		Scheduler.getInstance().run(); 
	   /* Polls the Buttons
		* Execute/Remove the Commands
		* Send values to SmartDashboard
		* Add Commands
		* Add Default Commands */
	}

	public void teleopInit() { // This function is called at the start of teleop
		/*if (autonomusCommand != null) {
			autonomusCommand.cancel();
		}*/

		CLimelight.setPipeline(Pipeline.Driver.get());
		RobotMap.driveTrain.clearStickyFaults();
		System.out.println("Teleop Init complete");
	}

	public void teleopPeriodic() { // This function is called periodically during teleop
		//System.out.println("Running teleop periodic at " + (System.currentTimeMillis() % 100000) + "ms");
		Scheduler.getInstance().run(); 
	   /* Polls the Buttons
		* Execute/Remove the Commands
		* Send values to SmartDashboard
		* Add Commands
		* Add Default Commands */
	}

	public void testInit() {
		RobotMap.compressor.setClosedLoopControl(true);
		RobotMap.compressor.start();
		CLimelight.setPipeline(Pipeline.Driver.get());
		System.out.println("Test Init complete");
	}

	public void testPeriodic() {
		Scheduler.getInstance().run(); 
	   /* Polls the Buttons
		* Execute/Remove the Commands
		* Send values to SmartDashboard
		* Add Commands
		* Add Default Commands */
	}

	public static void resetSensors() {
		System.out.println("[SensorReset] Starting gyro calibration. DON'T MOVE THE ROBOT...");
		RobotMap.ahrs.reset();
		System.out.println("[SensorReset] Gyro calibration done. Resetting encoders...");
		RobotMap.driveTrain.resetDistanceTraveled();
		System.out.println("[SensorReset] complete.");
	}

	// ever heard of the tale of last minute code
	// I thought not, it is not a tale the chairman will tell to you
	// (Keep this comment below last function of Robot.java and don't delete it because it is part of team history)
	
}
