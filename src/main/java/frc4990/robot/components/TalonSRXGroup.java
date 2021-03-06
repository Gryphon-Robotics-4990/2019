package frc4990.robot.components;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class TalonSRXGroup extends SpeedControllerGroup {

    private final WPI_TalonSRX[] talons;
    public ControlMode mode = ControlMode.PercentOutput;
    public Double coeff;

    public TalonSRXGroup(WPI_TalonSRX... talons) {
        super(talons[0], Arrays.copyOfRange(talons, 1, talons.length));
        
        this.talons = Arrays.copyOf(talons, talons.length);
    }

    public TalonSRXGroup(ControlMode mode, WPI_TalonSRX... talons) {
        this(talons);
        this.mode = mode;
    }

    public TalonSRXGroup(ControlMode mode, Double coeff, WPI_TalonSRX... talons) {
        this(talons);
        this.mode = mode;
        this.coeff = coeff;
    }

    @Override
    public void set(double speed) {
        for (WPI_TalonSRX talon : talons) {
            talon.set(mode, (super.getInverted() ? -speed : speed) * coeff);
          }
        }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Speed Controller");
        builder.setActuator(true);
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
        builder.addDoubleProperty("Coeff", () -> this.coeff, (double coeff) -> this.coeff = coeff);
        builder.addStringProperty("Mode", () -> this.mode.toString(), null);
    }
}
