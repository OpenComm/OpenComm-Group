package edu.cornell.opencomm.model;

/**
 * A user's contact list. This is a Roster of users for which the network
 * will send the primary user status updates. A user can add and/or delete
 * other users from their contact list.
 *
 */
public class ContactList {
	
	private static final String TAG = "Model.ContactList";
	private Roster _roster;
	
	/** CONSTRUCTOR: = the network's current roster for the primary user. 
	 *
	 * @param conn - the current XMPP connection
	 */
	public ContactList(Connection conn){
		_roster = conn.getRoster();
	}
	
	/** Adds a user to the contact list.
	 * 
	 * @param user 
	 * 		- the JID of the user to be added (ex: opencommsec@jabber.org)
	 * @param name 
	 * 		- the nickname of the user to be added (ex: opencommsec)
	 */
	public void addContact(String user, String name) throws XMPPException{
		_roster.createEntry(user, name, null);
	}
	
	/** Removes a user from the contact list.
	 * 
	 * @param user 
	 * 		- the JID of the user to be removed (ex: opencommsec@jabber.org)
	 */
	public void removeContact(String user) throws XMPPException{
		if (_roster.contains(user)){
			RosterEntry entry = _roster.getEntry(user);
			_roster.removeEntry(entry);
		} else{
			Log.d(TAG, "The user \'" + user + "\' is not currently " +
					"in the contact list.");
		}
	}
	
	/** @return - the Roster object held by this ContactList */
	public Roster getRoster(){
		return _roster;
	}
	
	/** DEBUG: Prints the current list of contacts. */
	public void printList(){
		for (RosterEntry re : _roster.getEntries()){
			Log.d(TAG, re.getUser() + " , " + re.getName() + " , " +
					_roster.getPresence(re.getUser()).toString());
		}
	}
}
