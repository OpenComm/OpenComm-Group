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
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

/**
 * The class that controls the contacts list.
 *
 * Nora 11/4 - For now is just the universal buddylist, will modify this later
 * to accomodate different types of contacts lists
 *
 * @author noranq
 *
 */
public class ContactListController {
    public static CharSequence[] buddyList; // list of the user's buddies in
    // their username form
    public static boolean[] buddySelection; // array of boolean for buddy
    // selection

    private static String username = ""; // the username of this account

    private static Context context;

    public ContactListController(Context context) {
        ContactListController.context = context;
        // showBuddyList();
    }

    public static void showBuddyList() {
        if (context == null)
            Log.v("ShowBUddyLIst", "NULL");
        Log.v("ContactListController", "showBuddyList() 1");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Log.v("ContactListController", "showBuddyList() 2");

        class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
            @Override
            public void onClick(DialogInterface dialog, int clicked,
                    boolean selected) {
                // Do Nothing
            }
        }

        class DialogOkButtonClickHandler implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int clicked) {
                switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:
                    // for( int i = 0; i < _options.length; i++ ){
                    // Log.i( "ME", _options[i] + " selected: " + _selections[i]
                    // );
                    // }
                    try {
                        addFromBuddyList();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        class DialogCancelButtonClickHandler implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }
        Log.v("ContactListController", "showBuddyList() 3");
        updateBuddyList();
        Log.v("ContactListController", "showBuddyList() 4");
        builder.setTitle("Buddylist").setMultiChoiceItems(buddyList,
                buddySelection, new DialogSelectionClickHandler())
                .setPositiveButton("Ok", new DialogOkButtonClickHandler())
                .setNegativeButton("Cancel", new DialogCancelButtonClickHandler())
                .create();
        Log.v("ContactListController", "showBuddyList() 5");
        AlertDialog alert = builder.create();
        Log.v("ContactListController", "showBuddyList() 6");
        MainApplication.screen.getActivity().displayEmptySpaceMenu(alert);
        //alert.show();
        Log.v("ContactListController", "showBuddyList() 7");
    }

    // Add users from the buddylist dialog to the main space
    public static void addFromBuddyList() throws XMPPException {
        int start = Values.staggeredAddStart;
        for (int i = 0; i < buddySelection.length; i++) {
            if (buddySelection[i]) {
                username = (String) buddyList[i];
                User p = new User(username + "@jabber.org", username,
                        R.drawable.question);
                Log.v("ContactListController", "addFromBuddyList this space = " + MainApplication.screen.getSpace());
                MainApplication.screen.getSpace().getInvitationController().inviteUser(p,
                        Network.DEFAULT_INVITE);
                Log.v("ContactListController", username + " was invited");
            }
        }
    } // end addFromBuddyList method

    // Updates the buddylist and the boolean selection array
    public static void updateBuddyList() {
        // Check to see if in the mainspace
        boolean inMainSpace = true;
        if (MainApplication.screen.getSpace() != Space.getMainSpace())
            inMainSpace = false;
        // If in a mainspace, then get buddylist from network
        if (inMainSpace) {
            Roster xmppRoster = LoginController.xmppService.getXMPPConnection().getRoster();
            Collection<RosterEntry> entryCollection = xmppRoster.getEntries();
            Iterator<RosterEntry> entryItr = entryCollection.iterator();
            buddyList = new CharSequence[entryCollection.size()-Space.getMainSpace().getAllIcons().size()];
            //Log.v("ContactListController", "entry size " + entryCollection.size());
            //Log.v("ContactListController", "space size " + Space.getMainSpace().getAllIcons().size());
            int i = 0;
            while (entryItr.hasNext()) {
                String nickname= entryItr.next().getUser().split("@")[0];
                if(!Space.getMainSpace().getAllNicksnames().containsKey(nickname)){
                    //Log.v("ContactListController", "nickname" +nickname);
                    buddyList[i++] = (CharSequence) nickname;
                }
            }
            Log.v("ContactListController", "updateBuddyList() 6");
        }
        // If in a sidechat, then populate the buddylist with people from the mainchat
        else {
            Log.v("ContactListController", "mainspace = " + Space.getMainSpace().getAllIcons()
                    .size() + ", sidechat = " + MainApplication.screen.getSpace().getAllIcons().size());
            buddyList = new CharSequence[Space.getMainSpace().getAllIcons()
                                         .size()-MainApplication.screen.getSpace().getAllIcons().size()];

            Collection<User> participants = Space.getMainSpace()
                    .getAllParticipants().values();
            Iterator<User> participantItr = participants.iterator();
            int i = 0;
            while (participantItr.hasNext()) {
                String next = participantItr.next().getNickname();
                Log.v("ContactListController", "nickname = " + next);
                if (!(next == MainApplication.user_primary.getNickname() || MainApplication.screen.getSpace().getAllNicksnames().containsKey(next))) {
                    buddyList[i++] = next;
                }
            }
        }
        buddySelection = new boolean[buddyList.length];
    } // end updateBuddyList method

}
