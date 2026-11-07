package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.BangBangController;

// CTRE Phoenix 6 Imports
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut; 
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.configs.MotorOutputConfigs;

// Shooter mechanism: a hood (pivot) that sets shot angle and a flywheel that launches game peice
// Hood: 2 kraken motors and 1 CANcoder controlled on phoenix 6 on motor PID 
// Flywheel: 2 kraken motors controlled with bang-bang control


public class ShooterSubsystem extends SubsystemBase {

    // need to match IDs 
    private static final int pivotLeaderID = 16;
    private static final int encoderId = 18;
    private static final int flywheelLeaderID = 17;
    private static final int flywheelFollowerID = 25;

    private static final double flywheelToleranceRPS = 50.0 / 60.0; // what is the rate of the flywheel?

    // full output used when bang-bang commands on (in volts)
    private static final double kMaxVolts = 12.0; // what is the max voltage of flywheel?
    
    
    private final TalonFX m_pivotLeader = new TalonFX(pivotLeaderID); // drives the hood // follows hood leader
    private final CANcoder m_encoder = new CANcoder(encoderId); // hood sensor

    private final TalonFX m_flywheelLeader = new TalonFX(flywheelLeaderID); // drives the flywheel
    private final TalonFX m_flywheelFollower = new TalonFX(flywheelFollowerID); // follows flywheel

    private final BangBangController m_bang = new BangBangController(flywheelToleranceRPS); 
    // BangBang returns 0 when below setpoint and 1 when above setpoint. In RPS

    private final VoltageOut m_voltageOut = new VoltageOut(0); // Reused every loop (Open loop)
    private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);
    // Closed-loop position request using PID gains to slot0

    public ShooterSubsystem() {

        TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
        // measured in volts per rotation of error
        pivotConfiguration.Slot0.kP = 2.4; 
        pivotConfiguration.Slot0.kI = 0; // Integral not used
        pivotConfiguration.Slot0.kD = 0; // Derrivative not used

        pivotConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Brake; // brake so hood resists movement when no output is applied
        pivotConfiguration.Feedback.FeedbackRemoteSensorID = m_encoder.getDeviceID();
        pivotConfiguration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder; //FusedCANcoder or remote? 
        
        pivotConfiguration.Feedback.SensorToMechanismRatio = 1.0; //need to check
        // CANcoder rotations per hood rotation
        pivotConfiguration.Feedback.RotorToSensorRatio = 2.0; //also need to check
        //Motor rotations per CANcoder rotation

        TalonFXConfiguration flywheelConfiguration = new TalonFXConfiguration();
        m_pivotLeader.getConfigurator().apply(pivotConfiguration);
        // apply configurations to hood

    
        // followers need to stay in neutral mode
       
        //Opposed = spins opposite to leader (if motors are facing eachother?)

        

        
        TalonFXConfiguration flyWheelConfig = new TalonFXConfiguration();

        flyWheelConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast; //Coast so the flywheel spins down without banging against the Bang-Bang control
        //limits power to max limit
        flyWheelConfig.CurrentLimits.SupplyCurrentLimit = 10; // need to know max limit of flywheel
        flyWheelConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        m_flywheelLeader.getConfigurator().apply(flyWheelConfig);

        m_flywheelFollower.setControl(new Follower(m_flywheelLeader.getDeviceID(), MotorAlignmentValue.Opposed));

    }

    public void setShooterPosition(double TargetSpeedRPM, double TargetHoodAngle) {
        // PID loop runs on motor
        m_pivotLeader.setControl(m_positionVoltage.withPosition(TargetHoodAngle));
        // get velocity returns rotations per second
        double currentSpeed = m_flywheelLeader.getVelocity().getValueAsDouble();
        
        //convert RPM to RPS so units match
        double volts = m_bang.calculate(currentSpeed) * kMaxVolts;
        m_flywheelLeader.setControl(m_voltageOut.withOutput(volts));


    }

    public void stop() {
        m_flywheelLeader.setControl(m_voltageOut.withOutput(0));
    }

   



    
}
