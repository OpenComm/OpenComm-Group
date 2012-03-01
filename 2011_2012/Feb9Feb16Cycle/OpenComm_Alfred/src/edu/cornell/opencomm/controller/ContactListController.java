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
    public static CharSequence[] buddyList; // list of the user's buddies in their username form
    public static boolean[] buddySelection; // array of boolean for buddy selection
    private static String username = ""; // the username of this account
    private static Context context;

    private final static boolean D = Values.D;
    private final static String TAG = "ContactListController";

    /**
     * Constructor
     * @param context
     */
    public ContactListController(Context context) {
        if (D) Log.d(TAG, "ContactListController constructor called");
        ContactListController.context = context;
    } // end ContactListController

    /**
     * Displays the buddy list for the purpose of adding users to a space
     */
    public static void showBuddyList() {
        //TODO: Move to view?
        //TODO: Split into display and set up functions
        if (D) Log.d(TAG, "showBuddyList called");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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
        updateBuddyList();
        builder.setTitle("Buddylist").setMultiChoiceItems(buddyList,
                buddySelection, new DialogSelectionClickHandler())
                .setPositiveButton("Ok", new DialogOkButtonClickHandler())
                .setNegativeButton("Cancel", new DialogCancelButtonClickHandler())
                .create();
        AlertDialog alert = builder.create();
        MainApplication.screen.getActivity().displayEmptySpaceMenu(alert);
    } // end showBuddyList

    /**
     *  Add users from the buddylist dialog to the main space
     */
    public static void addFromBuddyList() throws XMPPException {
        if (D) Log.d(TAG, "addFromBuddyList");
        for (int i = 0; i < buddySelection.length; i++) {
            if (buddySelection[i]) {
                username = (String) buddyList[i];
                User p = new User(username + "@" + Network.DEFAULT_HOST, username,
                        R.drawable.question);
                Log.v("ContactListController", "addFromBuddyList this space = " + MainApplication.screen.getSpace());
                MainApplication.screen.getSpace().getInvitationController().inviteUser(p,
                        Network.DEFAULT_INVITE);
                Log.v("ContactListController", username + " was invited");
            }
        }
    } // end addFromBuddyList method

    /**
     * Updates the buddylist and the boolean selection array
     */
    public static void updateBuddyList() {
        if (D) Log.d(TAG, "updateBuddyList called");
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
            int i = 0;
            while (entryItr.hasNext()) {
                String nickname= entryItr.next().getUser().split("@")[0];
                if(!Space.getMainSpace().getAllNicknames().containsKey(nickname)){
                    buddyList[i++] = (CharSequence) nickname;
                }
            }
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
                if (!(next == MainApplication.userPrimary.getNickname() || MainApplication.screen.getSpace().getAllNicknames().containsKey(next))) {
                    buddyList[i++] = next;
                }
            }
        }
        buddySelection = new boolean[buddyList.length];
    } // end updateBuddyList method

}
