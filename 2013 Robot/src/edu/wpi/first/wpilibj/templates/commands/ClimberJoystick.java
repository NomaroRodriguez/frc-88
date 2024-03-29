/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */

    /**
     * This runs the climber off of the left joystick's vertical axis on the operator controller
     */
public class ClimberJoystick extends CommandBase {
    
    public ClimberJoystick() {
        super("ClimberJoystick");
        requires(climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(climber.isClosedLoop()) {
            climber.disableClosedLoop();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        climber.ClimbOpenLoop(oi.getOperatorLeftVerticalAxis());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
