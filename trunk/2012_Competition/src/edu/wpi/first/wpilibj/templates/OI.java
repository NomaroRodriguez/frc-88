
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.commands.LifterUp;
import edu.wpi.first.wpilibj.templates.commands.LifterStop;
import edu.wpi.first.wpilibj.templates.commands.PitcherFire;
import edu.wpi.first.wpilibj.templates.commands.PitcherFireandReload;
import edu.wpi.first.wpilibj.templates.commands.PitcherReload;
import edu.wpi.first.wpilibj.templates.commands.FootDown;
import edu.wpi.first.wpilibj.templates.commands.PitcherFireandReload;
import edu.wpi.first.wpilibj.templates.commands.PitcherFire;
import edu.wpi.first.wpilibj.templates.commands.PitcherReload;
import edu.wpi.first.wpilibj.templates.commands.LifterStop;
import edu.wpi.first.wpilibj.templates.commands.FootDown;
import edu.wpi.first.wpilibj.templates.commands.FootUp;
import edu.wpi.first.wpilibj.templates.commands.RampPusherSpeed;

public class OI {
    // Process operator interface input here.
    Joystick driverController = new Joystick(1);
    Joystick operatorController = new Joystick(2);

    // driver can deploy foot
    private Button driverButtonA = new JoystickButton(driverController, 1);

    // operator stuff
    private Button operatorButtonA = new JoystickButton(operatorController, 1);
    private Button operatorButtonB = new JoystickButton(operatorController, 2);
    private Button operatorButtonX = new JoystickButton(operatorController, 3);
    private Button operatorButtonY = new JoystickButton(operatorController, 4);


    public OI() {
        operatorButtonA.whenPressed(new PitcherFire());
        operatorButtonA.whenReleased(new PitcherReload());
        operatorButtonB.whenPressed(new LifterStop());
        operatorButtonX.whenPressed(new LifterUp());
        operatorButtonY.whenPressed(new PitcherFireandReload());
       

        driverButtonA.whenPressed(new FootDown());
    }

    public double getTurretTurnStick() {
        return map(operatorController.getRawAxis(4));
    }

    public double getFwdStick() {
        return map(-driverController.getY());
    }

    public double getSideStick() {
        return map(driverController.getX());
    }

    public double getTurnStick() {
        return map(driverController.getRawAxis(4));
    }

    private static final double deadZone = 0.2;
    private static final double scale = 1.0/(1.0 - deadZone);

    private double map(double in) {
        double out = 0.0;

        if(in > deadZone) {
            out = (in - deadZone) * scale;
        } else if(in < -deadZone) {
            out = (in + deadZone) * scale;
        }
        return out;
    }
}
