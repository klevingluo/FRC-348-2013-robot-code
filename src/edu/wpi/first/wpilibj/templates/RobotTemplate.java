/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    
    Dashboard dash = DriverStation.getInstance().getDashboardPackerHigh();
    
    int diskCount = 2;
    
    Jaguar leftJag = new Jaguar(1);
    Jaguar rightJag = new Jaguar(2);
    Jaguar shooter = new Jaguar(4);
    Jaguar feeder = new Jaguar(3);
    Jaguar feederHopper = new Jaguar(5);
    
    DigitalInput feederSwitch = new DigitalInput(1);
    DigitalInput feederHopperSwitch = new DigitalInput(3);
    
    Joystick leftStick = new Joystick(1);
    Joystick rightStick = new Joystick(2);
    
    Timer time = new Timer();
    Timer autoTimer = new Timer();
    
    int feederState = 1;
    int feederHopperState = 1;
    int autoState = 0;
    
    public void robotInit() {
        reset();
    }
    
    public void autonomousInit() {
        autoTimer.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        shooter.set(1);
        if(feederState == 1 && autoTimer.get() > 2 && !feederSwitch.get()) {
            feederState = 2;
        } else if (feederState == 2 && feederSwitch.get()){
            feederState = 3;
        } else if(feederState == 3 && !feederSwitch.get()) {
            feederState = 0;
            autoTimer.reset();
        } else if (feederState ==0 && !rightStick.getRawButton(1)) {
            feederState = 1;
        }
        
        if(feederState > 1) {
            feeder.set(1);
        } else {
            feeder.set(0);
        }
        
        if(feederHopperState == 1 && rightStick.getRawButton(3) && !feederHopperSwitch.get()) {
            feederHopperState = 2;
        } else if (feederHopperState == 2 && feederHopperSwitch.get()){
            feederHopperState = 3;
        } else if(feederHopperState == 3 && !feederHopperSwitch.get()) {
            feederHopperState = 0;
        } else if (feederHopperState ==0 && !rightStick.getRawButton(3)) {
            feederHopperState = 1;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        leftJag.set(leftStick.getAxis(Joystick.AxisType.kY)/2);
        rightJag.set(rightStick.getAxis(Joystick.AxisType.kY));
        
        if(leftStick.getRawButton(1)) {
            shooter.set(1);
        } else {
            shooter.set(0);
        }
        
        if(feederState == 1 && rightStick.getRawButton(1) && !feederSwitch.get()) {
            feederState = 2;
        } else if (feederState == 2 && feederSwitch.get()){
            feederState = 3;
        } else if(feederState == 3 && !feederSwitch.get()) {
            feederState = 0;
        } else if (feederState ==0 && !rightStick.getRawButton(1)) {
            feederState = 1;
        }
        
        if(feederState > 1) {
            feeder.set(1);
        } else {
            feeder.set(0);
        }
        
        if(feederHopperState == 1 && rightStick.getRawButton(3) && !feederHopperSwitch.get()) {
            feederHopperState = 2;
        } else if (feederHopperState == 2 && feederHopperSwitch.get()){
            feederHopperState = 3;
        } else if(feederHopperState == 3 && !feederHopperSwitch.get()) {
            feederHopperState = 0;
            if (rightStick.getRawButton(3)) {
                time.start();
            }
        } else if (feederHopperState ==0 && !rightStick.getRawButton(3)) {
            feederHopperState = 1;
        }
        
        if (time.get() > 0.4) {
            time.stop();
            time.reset();
            feederState = 2;
        }
        
        if(feederHopperState > 1) {
            feederHopper.set(1);
        } else {
            feederHopper.set(0);
        }
        
        if(leftStick.getRawButton(7)) {
            reset();
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    private void reset() {
        while(feederSwitch.get()) {
                feeder.set(1);
        }
        feederState = 1;
        feeder.set(0);
        while(feederHopperSwitch.get()) {
            feederHopper.set(1);
        }
        feederHopperState = 1;
        feederHopper.set(0);
    }
    
}
