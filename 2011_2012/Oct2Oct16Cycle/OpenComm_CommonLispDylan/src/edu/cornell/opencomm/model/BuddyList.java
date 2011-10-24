package edu.cornell.opencomm.model;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

/**
 * A user's buddylist.  This is a roster of a user's "friends."  It allows a user
 *  to add other users to their buddylist or remove users from their buddylist.
 *
 */
public class BuddyList {

	private Roster _roster;
	
	/** The constructor.  This gets the roster of current users from the connection.
	 * 
	 * @param conn the current connection
	 */
	public BuddyList(Connection conn){
		_roster = conn.getRoster();
	}
	
	/** Adds a user to the buddylist.
	 * 
	 * @param user the user to be added (ex: opencommsec@jabber.org)
	 * @param name the nickname of the user to be added (ex: opencommsec)
	 * @throws XMPPException
	 */
	public void addBuddy(String user, String name) throws XMPPException{
		_roster.createEntry(user, name, null);
	}
	
	/** Removes a user from the buddylist
	 * 
	 * @param user the user to be removed (ex: opencommsec@jabber.org)
	 * @throws XMPPException
	 */
	public void removeBuddy(String user) throws XMPPException{
		if (_roster.contains(user)){
			RosterEntry entry = _roster.getEntry(user);
			_roster.removeEntry(entry);
		}
		else{
			System.out.println("This user is not currently in the buddylist.");
		}
	}
	
	/** Used for debugging purposes.  Prints the current list of entries in the buddylist.
	 * 
	 */
	public void printList(){
		for(RosterEntry re : _roster.getEntries()){
			System.out.println(re.getUser()+" , "+re.getName()+" , "+_roster.getPresence(re.getUser()).toString());
		}
	}
}
