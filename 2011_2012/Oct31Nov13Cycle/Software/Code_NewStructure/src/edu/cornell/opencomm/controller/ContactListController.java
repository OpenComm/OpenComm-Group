package edu.cornell.opencomm.controller;

import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;

/** The class that controls the contacts list.
 * 
 * Nora 11/4 - For now is just the universal buddylist, will modify this
 * later to accomodate different types of contacts lists
 * 
 * @author noranq
 *
 */
public class ContactListController {
    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection

    private static String username=""; // the username of this account
	
	
	private SpaceView spaceView = null;
	private static Context context;
	public ContactListController(Context context, SpaceView spaceView) {
		this.spaceView = spaceView;
		this.context = context;
		//showBuddyList();
	}

	
	public static void showBuddyList(){
		if (context == null)
			Log.v("ShowBUddyLIst", "NULL");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener
		{
			public void onClick( DialogInterface dialog, int clicked, boolean selected )
			{
				//Do Nothing
			}
		}


		class DialogButtonClickHandler implements DialogInterface.OnClickListener
		{
			public void onClick( DialogInterface dialog, int clicked )
			{
				switch( clicked )
				{
					case DialogInterface.BUTTON_POSITIVE:
//						for( int i = 0; i < _options.length; i++ ){
//							Log.i( "ME", _options[i] + " selected: " + _selections[i] );
//						}
					try {
						addFromBuddyList();
					} catch (XMPPException e) {
						e.printStackTrace();
					}

						break;
				}
			}
		}
		updateBuddyList();
		builder.setTitle( "Buddylist" )
    		    .setMultiChoiceItems( buddyList, buddySelection, new DialogSelectionClickHandler() )
    		    .setPositiveButton( "OK", new DialogButtonClickHandler() )
    		    .create();
		AlertDialog alert = builder.create();
		alert.show();
	}


	// Add users from the buddylist dialog to the main space
	public static void addFromBuddyList() throws XMPPException{
		for( int i = 0; i < buddySelection.length; i++ ){
			if(buddySelection[i]){
				username = (String) buddyList[i];
				User p = new User(username + "@jabber.org", username, R.drawable.question);
				//if (D) Log.d(LOG_TAG, "Adding person " + username + " to mainspace");
	        	//initAddPerson(MainApplication.mainspace, p);
				MainApplication.mainspace.getInvitationController().inviteUser(p, "Because you're cool");
			}
		}
	} // end addFromBuddyList method

	// Updates the buddylist and the boolean selection array
	public static void updateBuddyList() {
		// obtain current buddylist from server
		Roster xmppRoster = Login.xmppService.getXMPPConnection().getRoster();
		Collection<RosterEntry> entryCollection = xmppRoster.getEntries();
		Iterator<RosterEntry> entryItr = entryCollection.iterator();
		buddyList = new CharSequence[entryCollection.size()];
		int i = 0;
		while (entryItr.hasNext()) {
			buddyList[i++] = entryItr.next().getUser().split("@")[0];
		}
		buddySelection = new boolean[buddyList.length];
	} // end updateBuddyList method
	 
}
