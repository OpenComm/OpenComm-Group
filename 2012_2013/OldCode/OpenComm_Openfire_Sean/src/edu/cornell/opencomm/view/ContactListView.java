package edu.cornell.opencomm.view;

/* Buddy list */
public class ContactListView {

	
	public ContactListView(){
		
	}
	/*
	public void showBuddyList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
	public void addFromBuddyList() throws XMPPException{
		for( int i = 0; i < buddySelection.length; i++ ){
			if(buddySelection[i]){
				username = (String) buddyList[i];
				User p = new User(username + "@jabber.org", username, R.drawable.question);
				//if (D) Log.d(LOG_TAG, "Adding person " + username + " to mainspace");
	        	initAddPerson(mainspace, p);
			}
		}
	} // end addFromBuddyList method

	// Updates the buddylist and the boolean selection array
	public void updateBuddyList() {
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
	} // end updateBuddyList method */
}
