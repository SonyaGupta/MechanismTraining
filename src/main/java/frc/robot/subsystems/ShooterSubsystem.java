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
    private final TalonFX m_pivotLeader = new TalonFX(10);
    private final TalonFX m_pivotFollower = new TalonFX(11);
    private final CANcoder m_encoder = new CANcoder(12);

    private final TalonFX m_flywheelMoter = new TalonFX(13);

    private final BangBangController m_bang = new BangBangController(50.0);
// moter control requests instead of .set()
    private final VoltageOut m_voltageOut = new VoltageOut(0);
    private final PositionVoltage m_positionVoltage = new PositionVoltage().withSlot(0);

    public ShooterSubsystem() {

        TalonFXConfiguration pivotConfiguration = new TalonFXConfiguration();
        pivotConfiguration.Slot0.kP = 0; 
        pivotConfiguration.Slot0.kI = 0;
        pivotConfiguration.Slot0.kD = 0;   

        m_pivotLeader.getConfigurator().apply(pivotConfiguration);
        m_pivotFollower.setControl(new Follower(m_pivotLeader.getDeviceID,true));

        TalonFXConfiguration flywheelConfiguration = new TalonFXConfiguration();
        m_flywheelMoter.getConfigurator.apply(flywheelConfiguration);


    }

    public void setPivotPosition(double TargetRotation) {

        m_pivotLeader.setControl(m_positionRequest.withPosition(TargetRotation));
        

    }

   



    
}
