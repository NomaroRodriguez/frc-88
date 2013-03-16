/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author tj2
 */
public class BaseTalonToggle extends CommandBase {
    
    private static final double TIMEOUT = 0.5;
    public BaseTalonToggle() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super("BaseTalonToggle");
        requires(baseTalon);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(TIMEOUT);
        baseTalon.toggleTalon();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
