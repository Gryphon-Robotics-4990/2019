package org.usfirst.frc.team4990.robot.subsystems;

import org.usfirst.frc.team4990.robot.Constants;
import org.usfirst.frc.team4990.robot.subsystems.motors.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
	private Motor elevatorMotor;
	@SuppressWarnings("unused")
	private double currMotorPower;
	
	private LimitSwitch topSwitch;
	private boolean isAboveSwitchInProgress = false;
	private boolean isAbove;
	
	private LimitSwitch bottomSwitch;
	private boolean isBelowSwitchInProgress = false;
	private boolean isBelow;
	
	private Encoder encoder;
	
	public Elevator(
			Motor elevatorMotor, 
			int topSwitchChannel, 
			int topSwitchCounterSensitivity, 
			int bottomSwitchChannel, 
			int bottomSwitchCounterSensitivity,
			int encoderChannelA, 
			int encoderChannelB) {
		this.elevatorMotor = elevatorMotor;
		
		this.topSwitch = new LimitSwitch(topSwitchChannel, topSwitchCounterSensitivity);
		this.isAbove = false; // the carriage is above the top switch
		
		this.bottomSwitch = new LimitSwitch(bottomSwitchChannel, bottomSwitchCounterSensitivity);
		this.isBelow = false; // the carriage is below the bottom switch
		
		encoder = new Encoder(encoderChannelA, encoderChannelB);
		this.encoder.setDistancePerPulse(1.16 * Math.PI / Constants.pulsesPerRevolution); //diameter of elevator chain gear * PI
		this.encoder.setMinRate(Constants.gearboxEncoderMinRate);
		this.encoder.setSamplesToAverage(Constants.gearboxEncoderSamplesToAvg);
	}

	// positive power = up
	// negative power = down
	public void setElevatorPower(double power) {
		if ((this.isAbove && power > 0) || (this.isBelow && power < 0)) {
			this.currMotorPower = 0;
			this.elevatorMotor.setPower(0.0);
		} else {
			this.currMotorPower = power;
			this.elevatorMotor.setPower(power);
		}
	}
	
	public void checkSafety() {
		this.topSwitch.update();
		this.bottomSwitch.update();
		
		if (this.topSwitch.isSwitched()) {
			this.isAboveSwitchInProgress = true;
		//in this case, this.topSwitch.isSwitched will always be true
		} else if (this.isAboveSwitchInProgress) {
			this.isAbove = !this.isAbove;
			this.isAboveSwitchInProgress = false;
		}
		
		if (this.bottomSwitch.isSwitched()) {
			this.isBelowSwitchInProgress = true;
		//in this case, this.belowSwitch.isSwitched will always be true
		} else if (this.isBelowSwitchInProgress) {
			this.isBelow = !this.isBelow;
			this.isBelowSwitchInProgress = false;
		}
	}
	
	public boolean isAbove() {
		return this.isAbove;
	}
	
	public int topSwitchCurrCount() {
		return this.topSwitch.getCurrCount();
	}
	
	public int topSwitchLastCount() {
		return this.topSwitch.getLastCount();
	}
	
	public boolean isBelow() {
		return this.isBelow;
	}
	
	public int bottomSwitchCurrCount() {
		return this.bottomSwitch.getCurrCount();
	}
	
	public int bottomSwitchLastCount() {
		return this.bottomSwitch.getLastCount();
	}
	
	public boolean isTopSwitched() {
		//topSwitch.update();
		return this.topSwitch.getValue();
	}
	
	public void update() {
		//bottomSwitch.update();
		//topSwitch.update();
	}
	
	public boolean isBottomSwitched() {
		//bottomSwitch.update();
		return this.bottomSwitch.getValue();
	}
	
	public void reset() {
		System.out.println("ELEVATOR RESET");
		this.topSwitch.reset();
		this.isAbove = false;
		this.isAboveSwitchInProgress = false;
		
		this.bottomSwitch.reset();
		this.isBelow = false;
		this.isBelowSwitchInProgress = false;
	}
}
