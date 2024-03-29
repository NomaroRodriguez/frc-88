/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */
public class DumperBackward extends CommandBase {
    
    private static final double CURRENT_LIMIT = 15.0;
    
    public DumperBackward() {
        super("DumperDown");
        requires(dumper);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    /**
     * This is the open loop command for moving the dumper down
     */
    protected void initialize() {
        dumper.backward();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       return dumper.getCurrent() > CURRENT_LIMIT;
    }

    // Called once after isFinished returns true
    protected void end() {
        dumper.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        dumper.stop();
    }
}
