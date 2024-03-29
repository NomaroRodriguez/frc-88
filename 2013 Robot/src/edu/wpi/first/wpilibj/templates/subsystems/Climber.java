/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.ClimberJoystick;

/**
 * @author David
 */
public class Climber extends Subsystem {
    CANJaguar ClimbJag = null;
    //speeds can change if needed as of now full power will give you full power
    private boolean m_fault = false;
    private boolean m_calibrated = false;
    private boolean m_closedLoop = false;

    // By measurement, the distances are highest (0.0) and lowest (-35.75)
    
    private static final double distPerRev = 3.75;
    private static final double defaultDownSpeed = -0.3;
    private static final double defaultUpSpeed = 0.2;
    private static final double defaultMaxSpeed = 1;
    private double m_setPoint = 0.0;
    
    private static final int ENCODER_LINES = 100;
    private double revolutionHome = 0.0;

    public Climber() {
        
        try {
            ClimbJag = new CANJaguar(Wiring.climberCANID);

            if(ClimbJag != null) {
                // encoder configuration
                ClimbJag.configEncoderCodesPerRev(ENCODER_LINES);
                ClimbJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                ClimbJag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
                // We need this ramp rate. Without it rapid reversal causes transients
                ClimbJag.setVoltageRampRate(20);
            }
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.out.println("*** Climber CAN Error ***");
        }
        
        calibrateEncoder();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ClimberJoystick());
    }
    /**
     * Sets up the climber closed loop and puts it in Position mode.
     */
    public void enableClosedLoop() {
         if(ClimbJag != null) {
            try {
                ClimbJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                ClimbJag.setPID(150.0, 0.0, 0.0);
                double position = ClimbJag.getPosition();
                ClimbJag.enableControl(position);
                m_closedLoop = true;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
     /**
     * Disables the climber closed loop puts it into open loop.
     */
    public void disableClosedLoop() {
        if(ClimbJag != null) {
            try {
                ClimbJag.disableControl();
                ClimbJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                m_closedLoop = false;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    /**
     * Reads the boolean (m_closedLoop) to tell if the climber is in closed loop or not.
     */
    public boolean isClosedLoop() {
        return m_closedLoop;
    }
    
    /**
     * Stops the climber motor.
     */
    public void stop() {
        ClimbOpenLoop(0.0);
    }  
    /**
     * Drives the climber up at the default speed.
     */
    public void up() {
        ClimbOpenLoop(defaultUpSpeed);
    }
    /**
     * Drives the climber down at the default speed.
     */
    public void down() {
        ClimbOpenLoop(defaultDownSpeed);
    }
    /**
     * Sets m_fault to true, indicating an error with the
     * climber system.
     */
    public void setfault() {
        m_fault = true;
    }
    /**
     * Sets calibration flag, which means the homing has been completed, 
     * and also resets the home position for the climber mast.
     */
    public void calibrateEncoder() {
        m_calibrated = true;
        revolutionHome = getRevolution();
    }

    /**
     * Returns the value of the calibration flag
     * 
     * @return  True if the Climber subsystem has been calibrated
     */
    public boolean isCalibrated() {
        return m_calibrated;
    }

    /**
     * Gets whether or not the lower limit of the Climber mast has been tripped,
     * i.e. the mast is at it's *highest* position.
     * This sensor is also a hard limit switch for the Jaguar.
     * 
     * @return  True if the Climber has tripped the sensor indicating full up position.
     */
    public boolean lowerLimitTripped() {
        boolean flag = true;
        if(ClimbJag != null) {
            try {
                /**
                 * The sensor is wired to the forward limit switch of the Jaguar
                 * The getForwardLimitOK() method returns false when the switch is tripped
                **/
                flag = !ClimbJag.getForwardLimitOK();
           } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        return flag;
    }

    /**
     * Gets whether or not the upper limit of the Climber mast has been tripped,
     * i.e. the mast is at it's *lowest* position.
     * This sensor is also a hard limit switch for the Jaguar.
     * 
     * @return  True if the Climber has tripped the sensor indicating full down position.
     */
    public boolean upperLimitTripped() {
        boolean flag = true;
        if(ClimbJag != null) {
            try {
                /**
                 * The sensor is wired to the reverse limit switch of the Jaguar
                 * The getReverseLimitOK() method returns false when the switch is tripped
                 **/
                flag = !ClimbJag.getReverseLimitOK();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        return flag;
    }
    

    /**
     * Open loop control for the climb mast, using voltage percentage.
     * 
     * @param   power   The voltage of the climb motor specified
     *                  from -1.0 to 1.0.
     */
    public void ClimbOpenLoop(double power) {

        if(ClimbJag != null) {
            try {
                //play with stuff under to see if it needs to be inverted
                ClimbJag.setX(power * defaultMaxSpeed);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }            
      
}
    /**
     * Closed loop control for the climb mast.
     * 
     * @param   vertical    The vertical distance to drive to.  A distance of 0
     *                      is the lower limit of the mast.  The distance is
     *                      specified in ??? and should always be positive.
     */
    public void ClimbClosedLoop(double vertical) {
        // Need to change vertical distance (specified in inches) to something
        // the Jaguar can use (revs)
        // Also ensure that vertical is positive.
      // convert from inches to revolutions
      double revolution = vertical / distPerRev;
      m_setPoint = revolution + revolutionHome;
        if(ClimbJag != null) {
            try {
                //the formula below will probably be subject to change
                // may need to be inverted (is this inversion correct?)  2/9
                ClimbJag.setX(m_setPoint);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }   
    }

  public boolean atSetpoint() {
      return getRevolution() == m_setPoint;
      //// THIS WILL NOT WORK!!!!
      // SHOULD CHECK FOR WITHIN TOLERANCE
  }
  
  private double getRevolution() {
      double revolution = 0.0;
      if(ClimbJag != null) {
          try {
              //the formula below will probably be subject to change
              //also play with stuff under to see if it needs to be inverted
              revolution = ClimbJag.getPosition();
          } catch(CANTimeoutException ex) {
              m_fault = true;
              System.err.println("****************CAN timeout***********");
          }
      }
      return revolution;
  }
     
    public double getPosition() {
      return (getRevolution() - revolutionHome) * distPerRev; 
    }
    
    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }

}
