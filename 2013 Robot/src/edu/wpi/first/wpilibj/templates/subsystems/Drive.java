/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DriveWithControllerClosed;
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;
/**
 *
 * @author David
 */
public class Drive extends Subsystem {
    // 1/24/12 drive motors may have to be inverted because they are subject to switch
    CANJaguar leftJag = null;
    CANJaguar rightJag = null;
    private boolean m_fault = false;
    private boolean m_closedLoop = false;

    /* Set the maximum change in voltage in 1 sec
     * Smaller values prevent the robot from rocking backwards under acceleration
     */
    private static final double MOTOR_RAMP_RATE = 15.0;
    private static final int DRIVE_ENCODER_LINES = 250;
    private static final double DISTANCE_PER_REVOLUTION = 12.293;
//    private static final double DISTANCE_PER_REVOLUTION = 15.423;
    //sprockets on the comp & practice robot are 15:23 from final drive to wheel
    // wheel is 6 inches
    
    /** 
     * Initializes Jaguars and sets up PID control.
     */
    public Drive()  {

        try {
                
                leftJag = new CANJaguar(Wiring.driveLeftCANID);
                
                // Fix this to cope with failure to create leftJag
                if (leftJag != null) {
                // Need to determine encoder codes per rev
                    leftJag.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    leftJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    leftJag.setVoltageRampRate(MOTOR_RAMP_RATE);
                    leftJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                }
            }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        
        try {
                rightJag = new CANJaguar(Wiring.driveRightCANID);
                if (rightJag != null) {
                    rightJag.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    rightJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    rightJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    rightJag.setVoltageRampRate(MOTOR_RAMP_RATE);
                    rightJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                }
            }
            catch  (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
    }
    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        double position;

        if(rightJag != null && leftJag != null) {
            try {
                // set the right motor to closed loop
                rightJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                rightJag.setPID(0.80, 0.005, 0.0);
                position = rightJag.getPosition();
                rightJag.enableControl(position);
                // now the left motor
                leftJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                leftJag.setPID(0.8, 0.005, 0.0);
                position = leftJag.getPosition();
                leftJag.enableControl(position);
                m_closedLoop = true;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        if(rightJag != null && leftJag != null) {
            try {
                // set the right motor to open loop
                rightJag.disableControl();
                rightJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                // now the left motor
                leftJag.disableControl();
                leftJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                m_closedLoop = false;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    /**
     * Returns whether or not Drive is in ClosedLoop. If it is it will return true and if it is not
     * it will return false.
     */
    public boolean isClosedLoop() {
        return m_closedLoop;
    }

    /**
     * Sets the default command to DriveWithController so that when nothing
     * else is happening with the Drive, we can use controllers to move.
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithControllerClosed());
    }
 
    /**
     * Set the neutral mode to brake or coast for *both* drive motors
     * 
     * @param brake True for brake, false for coast
     * 
     */
    public void setBrake(boolean brake) {
        CANJaguar.NeutralMode mode = CANJaguar.NeutralMode.kCoast;
        
        if(brake) {
            mode = CANJaguar.NeutralMode.kBrake;
        }
        try {
            leftJag.configNeutralMode(mode);
            rightJag.configNeutralMode(mode);
        } catch  (CANTimeoutException ex) {
            System.out.println("***CAN ERROR***");
            m_fault = true;
        }
    }
    
    /**
     * Open loop control of the Drive, using voltage percentage.
     * 
     * @param   left    The voltage for the left side of the Drive.  Value
     *                  should be specified between -1.0 and 1.0.
     * @param   right   The voltage for the right side of the Drive. Value
     *                  should be specified between -1.0 and 1.0.
     */
    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null) {
            try {
                leftJag.setX(left);
//                leftJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-right);
//                rightJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    /**
     * Close loop control for the Drive, using encoders to control speed.
     * 
     * @param   left    The speed for the left side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     * @param   right   The speed for the right side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     */
    public void driveTankClosedLoop(double speedLeft, double speedRight) {
        double scaleToRPM = 60.0 / DISTANCE_PER_REVOLUTION;
        
        if(leftJag != null) {
            try {
                leftJag.setX(speedLeft * scaleToRPM);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-speedRight * scaleToRPM);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    /**
     * Converts from gearbox shaft rotation to wheel distance traveled
     * 
     * @param revolutions output shaft rotations
     * @return distance wheel travels in inches
     */
    private double encoderToDistance(double revolutions) {
        /* convert from revolutions at geabox output shaft to distance in inches
         * ouput sproket has 18 teeth, and wheel sproket has 22 teeth
         * wheel diameter is nominal 6 inches.
         */
        return DISTANCE_PER_REVOLUTION * revolutions;
    }
    
    /**
     * The distance traveled by the left wheel since Jag powered on
     * 
     * @return Total distance traveled by left wheel in inches 
     */
    public double getLeftDistance() {

        double position = 0.0;
        try {
            position = leftJag.getPosition();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return encoderToDistance(position);
    }
    
    /**
     * The distance traveled by the right wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in inches 
     */
    public double getRightDistance() {

        double position = 0.0;
        try {
            position = -rightJag.getPosition();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return encoderToDistance(position);
    }
    
    /**
     * Current speed of left wheel
     * 
     * @return speed of left wheel in inches per second
     */
    public double getLeftSpeed() {

        double speed = 0.0;
        try {
            speed = leftJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        // convert rpm to inches per second
        return encoderToDistance(speed)/60.0;
    }
    
    /**
     * Current speed of right wheel
     * 
     * @return speed of right wheel in inches per second
     */
    public double getRightSpeed() {

        double speed = 0.0;
        try {
            speed = -rightJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return encoderToDistance(speed)/60.0;
    }
    
    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }

}
