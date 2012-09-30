package edu.cornell.opencomm.security;

/**
 * For Server-Side.
 * Handles all writing to the audit log.  Should not ever read the audit log.
 *
 */
public class AuditLog {

	/**
	 * TODO: Creates and/or opens the audit log file.
	 */
	public AuditLog(){
	}
	
	/**
	 * TODO: Write the message to the log.  We need to record what event, who 
	 * 		performed it, and what date and time it occurred at.  
	 * TODO: This needs synchronization primitives.
	 * @param s the message to write
	 */
	public void writeAuditLog(String s){
	}
	
}
