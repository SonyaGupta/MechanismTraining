// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ShooterSubsystem m_shooter = new ShooterSubsystem();


  // Replace with CommandPS4Controller or CommandJoystick if needed
 private final CommandPS5Controller m_driverController = new CommandPS5Controller(0);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    //Command 1: hold X 
    // Cross(): a trigger that is true when X is pressed down
    //WhileTrue(): Remains true & loops while X remains pressed 
    // m_shooter.run(): Calls command that runs ever 20 ms (requires m_shooter so not other command can use it)
    

    m_driverController.cross().whileTrue(
        m_shooter.run(() -> m_shooter.setShooterPosition(
            ShooterConstants.Command1SpeedRPM,   // flywheel target, RPM
            ShooterConstants.Command1HoodAngle   // hood target, degrees
        ))

    ).onFalse(m_shooter.runOnce(m_shooter::stop));

    //Command 2: hold 0
    m_driverController.cross().whileTrue(
        m_shooter.run(() -> m_shooter.setShooterPosition(
            ShooterConstants.Command2SpeedRPM,   // flywheel target, RPM
            ShooterConstants.Command2HoodAngle   // hood target, degrees
        ))

    ).onFalse(m_shooter.runOnce(m_shooter::stop));
  }
 // on false: when the button is released run command below runOnce()

  


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
