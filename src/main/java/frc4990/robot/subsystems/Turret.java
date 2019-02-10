package frc4990.robot.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc4990.robot.RobotMap;

public class Turret extends Subsystem implements PIDSource, PIDOutput {
    
	public PIDSourceType pidSourceType = PIDSourceType.kDisplacement;
	
	public Turret() {
		RobotMap.turretTalon.syncPosition();
	}

    /**
	 * Configures the open-loop ramp rate of throttle output to the default value.
	 */
	public void configOpenloopRamp() {
        RobotMap.turretTalon.configOpenloopRamp(Dashboard.getConst("Turret/rampDownTime", 0), 0);
    }
    
    @Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		pidSourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return this.pidSourceType;
    }
    
    public InstantCommand setTurretSpeed(double speed) { 
		return new InstantCommand("SetTurretSpeed", this, () -> Turret.setSpeed(speed));
	}
    
    /**
	 * Returns right encoder value, in feet.
	 */
	public double pidGet() {
		if (pidSourceType == PIDSourceType.kDisplacement) {
			return getEncoderDistance();
		} else {
			return getEncoderRate();
		}
    }

    @Override
    public void pidWrite(double output) {
        setSpeed(output);
	}
	/**
	 * Checks hall effect sensors (contactless limits) and if movement is allowed, it will be sent to motor during periodic()
	 * If movement is not allowed, function returns.
	 * @param value speed to set, [-1 to 1]
	 */

    public static void setSpeed(double value) {
        if ((RobotMap.turretSensorMiddle.get() && RobotMap.turretSensorRight.get()) && value < 0) value = 0; //At end of right range
        if ((RobotMap.turretSensorMiddle.get() && RobotMap.turretSensorLeft.get()) && value > 0) value = 0; //At end of left range
        if (RobotMap.turretSensorLeft.get() || RobotMap.turretSensorRight.get()) value /= 2; //near end of either range
        RobotMap.turretTalon.set(value);
    }
    
    /**
	 * Returns encoder value, in unknown units.
	 */

	public double getEncoderDistance() {
		return RobotMap.turretTalon.getPosition();
	}
	
	/**
	 * Returns encoder rate, in unknown units. 
	 */

	public double getEncoderRate() {
		return RobotMap.turretTalon.getRate();
    }

	@Override
	protected void initDefaultCommand() {}
    
    @Override
    public void periodic() {}
}