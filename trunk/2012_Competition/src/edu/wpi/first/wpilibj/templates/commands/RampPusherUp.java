/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class RampPusherUp extends CommandBase {

    public RampPusherUp() {
        super("RampPusherUp");
        requires(rampPusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        rampPusher.up();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return rampPusher.isLimitSwitchPressed() || Math.abs(rampPusher.getCurrent()) > 20;
    }

    // Called once after isFinished returns true
    protected void end() {
        rampPusher.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}