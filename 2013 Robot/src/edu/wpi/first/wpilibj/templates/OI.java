
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.templates.commands.ClimberDown;
import edu.wpi.first.wpilibj.templates.commands.ClimberUp;
import edu.wpi.first.wpilibj.templates.commands.ClimberStop;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.AngleIncrease;
import edu.wpi.first.wpilibj.templates.commands.AngleDecrease;
import edu.wpi.first.wpilibj.templates.commands.TilterStop;
import edu.wpi.first.wpilibj.templates.commands.dumperhighscore_position;
import edu.wpi.first.wpilibj.templates.commands.dumperlowscore_position;
import edu.wpi.first.wpilibj.templates.commands.feed_position;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    Joystick driverController = new Joystick(1);
    Joystick operatorController = new Joystick(2);
    
    private Button operatorButtonA = new JoystickButton(operatorController, 1);
    private Button operatorButtonB = new JoystickButton(operatorController, 2);
    private Button operatorButtonX = new JoystickButton(operatorController, 3);
    private Button operatorButtonY = new JoystickButton(operatorController, 4);
    
    
    public OI() {
       //controls the angle for the climber. When the buttons are released the climber should stop automatically - David
//        operatorButtonA.whileHeld(new AngleDecrease());
//        operatorButtonA.whenReleased(new TilterStop());
//        operatorButtonY.whileHeld(new AngleIncrease());
//        operatorButtonY.whenReleased(new TilterStop());

        //climber up/down, also set up when buttons are released the climber SHOULD auto stop
//        operatorButtonB.whileHeld(new ClimberUp());
//        operatorButtonB.whenReleased(new ClimberStop());
//        operatorButtonX.whileHeld(new ClimberDown());
//        operatorButtonX.whenReleased(new ClimberStop());
        
        //these buttons, B and X, the commands for them involve closed loop stuff for another day...need fixing
          operatorButtonB.whenPressed(new dumperhighscore_position());
          operatorButtonX.whenPressed(new dumperlowscore_position());
          operatorButtonY.whenPressed(new feed_position());
          
    }
    
    public double getOpRightVerticalAxis() {
        return deadZoneMap(-operatorController.getRawAxis(4));
    }     
    public double getOpRightHorizontalAxis() {
        return deadZoneMap(operatorController.getRawAxis(5));
    }

    
    public double getFwdLeftStick() {
        return deadZoneMap(-driverController.getY());
    }     
    public double getSideStick() {
        return deadZoneMap(driverController.getX());
    }

    public double getTurnStick() {
        return deadZoneMap(driverController.getRawAxis(4));
    }

    public double getFwdRightStick() {
        return deadZoneMap(-driverController.getRawAxis(5));
    }
    
    private static final double deadZone = 0.2;
    private static final double scale = 1.0/(1.0 - deadZone);
    
    private double deadZoneMap(double in) {
        double out = 0.0;

        if(in > deadZone) {
            out = (in - deadZone) * scale;
        } else if(in < -deadZone) {
            out = (in + deadZone) * scale;
        }
        return out;
    }
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}
