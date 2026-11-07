import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.BangBangController;

// CTRE Phoenix 6 Imports
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut; 


public class ShooterSubsystem extends SubsystemBase {

    private static final int pivotLeaderID = 10;
    private static final int pivotFollowerID = 11;
    private static final int encoderId = 12;
    private static final int flywheelLeaderID = 13;
    private static final int flywheelFollowerID = 14;

    private static final double flywheelToleranceRPS = 50.0 / 60.0; // what is the rate of the flywheel?

    private static final double kMaxVolts = 12.0; // what is the max voltage of flywheel?

    private final TalonFX m_pivotLeader = new TalonFX(pivotLeaderID);
    private final TalonFX m_pivotFollower = new TalonFX(pivotFollowerID);
    private final CANcoder m_encoder = new CANcoder(encoderId);

    private final TalonFX m_flywheelLeader = new TalonFX(flywheelLeaderID);
    private final TalonFX m_flywheelFollower = new TalonFX(flywheelFollowerID);

    private final BangBangController m_bang = new BangBangController(flywheelToleranceRPS);

    private final VoltageOut m_voltageOut = new VoltageOut(0);
    private final PositionVoltage m_positionVoltage = new PositionVoltage(0).withSlot(0);

    public ShooterSubsystem() {

        TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
        pivotConfiguration.Slot0.kP = 0; 
        pivotConfiguration.Slot0.kI = 0;
        pivotConfiguration.Slot0.kD = 0;   

        pivotConfiguration.MotorOutput.withNeutralMode = NeutralModeValue.Brake; // brake so hood resists movement 
        pivotConfiguration.Feedback.FeedbackRemoteSensorID = m_encoder.getDeviceID();
        pivotConfiguration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder; //FusedCANcoder because it is attache
        
        pivotConfiguration.Feedback.SensorToMechanismRatio = 1.0; //need to check
        pivotConfiguration.Feedback.RotorToSensorRatio = 2.0; //also need to check

        TalonFXConfiguration flywheelConfiguration = new TalonFXConfiguration();
        m_flywheelMoter.getConfigurator.apply(flywheelConfiguration);

        m_pivotLeader.getConfigurator().apply(pivotConfiguration);

        m_pivotFollower.getConfigurator().apply(new TalonFXConfiguration().withMotorOutput(new MotorOutputConfigs(NeutralModeValue.Brake)));


    }

    public void setPivotPosition(double TargetRotation) {

        m_pivotLeader.setControl(m_positionRequest.withPosition(TargetRotation));
        

    }

   



    
}
