package edu.cornell.opencomm.model;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;
import edu.cornell.opencomm.Values;

/**
 * A user's contact list. This is a Roster of users for which the network
 * will send the primary user status updates. A user can add and/or delete
 * other users from their contact list.
 *
 */
public class ContactList {

    private static final boolean D = Values.D;
    private static final String TAG = "ContactList";

    private Roster roster;

    /**
     * CONSTRUCTOR: = the network's current roster for the primary user.
     * @param conn - the current XMPP connection
     */
    public ContactList(Connection conn) {
        if (D) Log.d(TAG, "ContactList constructor called");
        roster = conn.getRoster();
    }

    /**
     * Adds a user to the contact list.
     * @param user
     * 		- the JID of the user to be added (ex: opencommsec@jabber.org)
     * @param name
     * 		- the nickname of the user to be added (ex: opencommsec)
     */
    public void addContact(String user, String name) throws XMPPException {
        if (D) Log.d(TAG, "addContact called");
        roster.createEntry(user, name, null);
    }

    /**
     * Removes a user from the contact list.
     * @param user
     * 		- the JID of the user to be removed (ex: opencommsec@jabber.org)
     */
    public void removeContact(String user) throws XMPPException {
        if (D) Log.d(TAG, "removeContact called");
        if (roster.contains(user)) {
            RosterEntry entry = roster.getEntry(user);
            roster.removeEntry(entry);
        } else
        	if (D)Log.d(TAG, "The user \'" + user + "\' is not currently " + "in the contact list.");
    }

    /**
     * @return - the Roster object held by this ContactList
     *  */
    public Roster getRoster() {
        return roster;
    }
}
