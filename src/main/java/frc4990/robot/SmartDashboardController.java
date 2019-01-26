package frc4990.robot;

import java.util.Map;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SmartDashboardController {

	private static Preferences preferences = Preferences.getInstance();
	interface FunctionalInterface { Object anything();}
	public static ShuffleboardTab driveTab, debugTab;
	/**
	 * Initializes the dashboards(debug and drive modes) with values and things.
	 * 
	 */
	public static void initializeDashboard(boolean debug) {
		driveTab = Shuffleboard.getTab("Drive");
		debugTab = Shuffleboard.getTab("Debug");
		System.out.println("Initializing Dashboard.");
		setDashboardMode(! debug);
		setDashboardMode(debug);
	}

	/**
	 * Retrieves a numerical constant from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            number to return if no value is retrieved
	 * @author Deep Blue Robotics (Team 199)
	 */

	public static double getConst(String key, double def) {

		if (!preferences.containsKey("Const/" + key)) {
			preferences.putDouble("Const/" + key, def);
			if (preferences.getDouble("Const/ + key", def) != def) {
				System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getDouble("Const/" + key, def);
	}

	/**
	 * Retrieves a boolean from SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to return if no value is retrieved
	 * @author MajikalExplosions
	 */

	public static boolean getBoolean(String key, boolean def) {

		if (!preferences.containsKey("Const/" + key)) {
			preferences.putBoolean("Const/" + key, def);
			if (preferences.getBoolean("Const/ + key", def) != def) {
				System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
				return def;
			}
		}
		return preferences.getBoolean("Const/" + key, def);
	}

	/**
	 * Adds a constant to SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to be stored
	 * @author Deep Blue Robotics (Team 199)
	 */

	
	public static void putConst(String key, double def) {
		preferences.putDouble("Const/" + key, def);
		if (preferences.getDouble("Const/ + key", def) != def) {
			System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
		}

	}

	/**
	 * Adds a boolean to SmartDashbaord/Shuffleboard.
	 * 
	 * @param key
	 *            string key to identify value
	 * @param def
	 *            value to be stored
	 * @author MajikalExplosions
	 */

	
	public static void putConst(String key, boolean def) {
		preferences.putBoolean("Const/" + key, def);
		if (preferences.getBoolean("Const/ + key", def) != def) {
			System.err.println("pref Key" + "Const/" + key + "already taken by a different type");
		}

	}

	//New code below

	public static void updateDashboard() {
		//Shuffleboard.update(); runs automatically.
	}

	public static void setDashboardMode(boolean debug) {
		System.out.println("Debug tab component list length: " + debugTab.getComponents().size());
		if (debug && debugTab.getComponents().size() == 0) {
			System.out.println("Adding Debug Tab Components.");
			debugTab.add("Base/PDP", RobotMap.pdp).withWidget(BuiltInWidgets.kPowerDistributionPanel)./*withSize(3, 2).*/withPosition(8, 3);
			debugTab.add("Base/Ultrasonic", RobotMap.ultrasonic).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min","0","max","50"))./*withSize(1, 2).*/withPosition(5,0);
			debugTab.add("Base/NavX-MXP-AHRS", RobotMap.ahrs).withWidget(BuiltInWidgets.kGyro)/*.withSize(2, 2)*/;
			debugTab.add("DriveTrain/Left/Encoder", RobotMap.leftEncoder).withWidget(BuiltInWidgets.kEncoder)./*withSize(2, 1).*/withPosition(11, 0);
			debugTab.add("DriveTrain/Right/Encoder", RobotMap.rightEncoder).withWidget(BuiltInWidgets.kEncoder)./*withSize(2, 1).*/withPosition(11, 1);
			debugTab.add("DriveTrain/Left/motorGroup", RobotMap.driveTrain.left.motorGroup).withWidget(BuiltInWidgets.kSpeedController)./*withSize(2, 1).*/withPosition(11, 2);
			debugTab.add("DriveTrain/Right/motorGroup", RobotMap.driveTrain.right.motorGroup).withWidget(BuiltInWidgets.kSpeedController)./*withSize(2, 1).*/withPosition(11, 3);
			//debugTab.add("DriveTrain/DifferentialDrive", DriveTrain.differentialDrive).withWidget(BuiltInWidgets.kDifferentialDrive).withSize(2, 2).withPosition(11, 4);
			debugTab.add("DriveStationInput/turnSteepness", new SendableObject(() -> {return OI.turnSteepnessAnalogButton.getRawAxis().toString(); }));
			debugTab.add("DriveStationInput/throttle", new SendableObject(() -> {return OI.throttleAnalogButton.getRawAxis().toString(); }));


			if (Robot.autonomusCommand != null) {
				debugTab.add("Autonomus/AutonomusCommand", Robot.autonomusCommand).withWidget(BuiltInWidgets.kCommand);//.withPosition(11, 4);
			}
		} else if (! debug) {
			System.out.println("Adding Drive Tab Components.");
			driveTab.add(Scheduler.getInstance()).withSize(2, 3).withPosition(0, 0);
			driveTab.add("SelectedStartPosition", new SendableObject(() -> { return Robot.autoChooser.getSelected().name(); })).withSize(2, 1).withPosition(3, 3);
			driveTab.add("SendableChooser", Robot.autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser).withSize(2, 1).withPosition(4, 3);
			driveTab.add("Populate DebugDashboard", (new InstantCommand((Runnable) () -> {setDashboardMode(true); }))).withSize(2, 1).withPosition(5, 3);
		}	
	}
}