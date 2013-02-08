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
    public static final int driveLeftCANID    = 16; 
    public static final int driveRightCANID   = 17;
    public static final int TilterCANID = 5; 
    public static final int climberCANID = 12;
    public static final int DumperCANID = 3;
    //^^^this guys gotta be changed, atm there is no 5th Jag- David
    
    /*
     * * ##limitSwitches##
     */
    public static final int dumperLimitSwitch = 14;
    //^^^this guys gotta be changed, atm no limit switch wired- David
}