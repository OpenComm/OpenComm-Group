package edu.cornell.opencomm.security;

/**
 * For Client- and Server- Side.
 * @author anniesk8
 *
 */
public class ReplayPrevention {
	
	/**
	 * TODO: ReplayPrevention constructor.  Should initialize counter for Client when 
	 * 		Client logs in.  Server should hold onto a map of clients -> most recent i.  
	 */
	public ReplayPrevention(){
	}
	
	/**
	 * TODO: This should check if i <= the most recently stored i (see basic concept of 
	 * 		logical clocks for better idea).
	 * @param i the value to check
	 * @return true if it is a replay attack, or false if it is not a replay attack
	 */
	public boolean checkForReplay(int i){
		return false;
	}
	
}
