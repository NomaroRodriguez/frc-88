package edu.wpi.first.wpilibj.templates;

/**
 * The Wiring is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Wiring {
    /*
     * ### CAN Motors ###
     */
    // Added real CAN IDs from the practice robot
    public static final int driveLeftCANID    = 5; 
    public static final int driveRightCANID   = 2;
    public static final int tilterCANID = 3; 
    public static final int climberCANID = 4;
    public static final int dumperCANID = 6;

    // ### PWM / Servos ###
    //servo ids
    public static final int cameraServoLeft = 1;
    public static final int cameraServoRight = 2;

    // ### Digital Input/Outputs ###
    public static final int dumperLimitSwitch = 14;
    public static final int rangeFinderPing = 2;
    public static final int rangeFinderEcho = 3;
    
    public static final int leftTalonSensor = 8;
    public static final int rightTalonSensor = 9;
    

    /* ### Pneumatics ###
     * 
     */
    public static final int talonSolenoid = 1;
    public static final int talonSolenoidReverse = 2;
    public static final int compressorSpike = 1;
    public static final int pressureSwitch = 1;
    
    /*
     * ### Lights ###
     */
    // These pins are currently all just made up and placeholders and 
    // should be updated when the lights are actually connected.
    public static final int lightDigitalOutPin1 = 4;
    public static final int lightDigitalOutPin2 = 5;
    public static final int lightDigitalOutPin3 = 6;
    public static final int lightDigitalOutPin4 = 7;
    public static final int lightPwmOutPin1 = 3;
    public static final int lightPwmOutPin2 = 4;
}
