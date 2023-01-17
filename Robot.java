// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
//import com.fasterxml.jackson.annotation.JsonTypeId;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


class Example {
  void example() {
    
  }
}

class TalonFX_Servo {
  WPI_TalonFX motor;
  float angle;
  public TalonFX_Servo (WPI_TalonFX motor) {
    this.motor = motor;
    this.angle = 0f;
  }
  public TalonFX_Servo (int deviceNumber) {
    this.motor = new WPI_TalonFX(deviceNumber);
    this.angle = 0f;
  }
  public void update() {
    this.motor.set(ControlMode.Position, (this.angle / 360) * 2048);
  }
  public void set(float angle) {
    this.angle = angle;
  }
}

class SwerveDrive {
  WPI_TalonFX fld, frd, bld, brd;
  TalonFX_Servo flt, frt, blt, brt;
  public SwerveDrive(TalonFX_Servo flt, WPI_TalonFX fld, TalonFX_Servo frt, WPI_TalonFX frd, TalonFX_Servo blt, WPI_TalonFX bld, TalonFX_Servo brt, WPI_TalonFX brd){
    this.flt = flt;
    this.fld = fld;
    this.frt = frt;
    this.frd = frd;
    this.blt = blt;
    this.bld = bld;
    this.brt = brt;
    this.brd = brd;
  }
  public void update(float strafe, float drive, float turn) {
    float fLeftD;
    float fLeftT;
    float fRightD;
    float fRightT;
    float bLeftD;
    float bLeftT;
    float bRightD;
    float bRightT;
    if (strafe + drive > 0) {
      fLeftT = (float)(Math.atan2(strafe, drive) * (180 / Math.PI)) + (turn * 45);
      fLeftD = (strafe + drive) / 2;
      fRightT = (float)(Math.atan2(strafe, drive) * (180 / Math.PI)) + (turn * 45);
      fRightD = (strafe + drive) / 2;
      bLeftT = (float)(Math.atan2(strafe, drive) * (180 / Math.PI)) + (-turn * 45);
      bLeftD = (strafe + drive) / 2;
      bRightT = (float)(Math.atan2(strafe, drive) * (180 / Math.PI)) + (-turn * 45);
      bRightD = (strafe + drive) / 2;
    } else if (turn != 0) {
      fLeftT = 225;
      fLeftD = turn;
      fRightT = -225;
      fRightD = turn;
      bLeftT = -225;
      bLeftD = turn;
      bRightT = 225;
      bRightD = turn;
    } else {
      fLeftT = 0;
      fLeftD = 0;
      fRightT = 0;
      fRightD = 0;
      bLeftT = 0;
      bLeftD = 0;
      bRightT = 0;
      bRightD = 0;
      drive = 0;
      turn = 0;
      strafe = 0;
    }
    flt.set(fLeftT);
    fld.set(fLeftD);
    frt.set(fRightT);
    frd.set(fRightD);
    blt.set(bLeftT);
    bld.set(bLeftD);
    brt.set(bRightT);
    brd.set(bRightD);
    flt.update();
    frt.update();
    blt.update();
    brt.update();
  }
}

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  WPI_TalonFX fld, frd, bld, brd;
  TalonFX_Servo flt, frt, blt, brt;
  Joystick joystick;
  SwerveDrive sd;
  @Override
  public void robotInit() {
    joystick = new Joystick(0);

    flt = new TalonFX_Servo(0);
    fld = new WPI_TalonFX(1);
    frt = new TalonFX_Servo(2);
    frd = new WPI_TalonFX(3);
    blt = new TalonFX_Servo(4);
    bld = new WPI_TalonFX(5);
    brt = new TalonFX_Servo(6);
    brd = new WPI_TalonFX(7);

    sd = new SwerveDrive(flt, fld, frt, frd, blt, bld, brt, brd);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    sd.update((float) joystick.getRawAxis(0), (float) joystick.getRawAxis(1), (float) joystick.getRawAxis(2));
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
