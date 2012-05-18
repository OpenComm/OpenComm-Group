package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.DashboardView;

/**
 * An instance of this class controls participants (users) in a specific space
 */
public class ParticipantController {

    // Debugging
    public static final String TAG = "ParticipantController";
    public static final boolean D = true;

    // Model variables
    private Space mSpace;

    /**
     * Constructor
     */
    public ParticipantController(Space mSpace) {
        if (D) Log.d(TAG, "ParticipantController called");
        this.mSpace = mSpace;
    }

    /**
     * Removes the primary user from the Space associated with this controller.
     * If the primary user is this Space's owner, they will be asked if they
     * want to destroy the chat or pass ownership. If they choose to pass
     * ownership, then they will be asked to select who to pass ownership to. If
     * this is the conference, then the view updates to the splash screen. If
     * this is a side chat, then the user is returned to the conference.
     */
    public void leaveSpace(boolean inMainSpace) {
        if (D) Log.d(TAG, "leaveSpace called");
        // If moderator of the space
        if (mSpace.getOwner() == MainApplication.userPrimary) {
            MainApplication.screen.getSpace().getSpaceController()
            .deleteSpace();
            MainApplication.screen.getActivity().delPrivateSpaceUI(mSpace,
                    inMainSpace);
            Intent i = new Intent(MainApplication.screen.getSpace()
                    .getContext(), DashboardView.class);
            MainApplication.screen.getSpace().getContext().startActivity(i);
            Log.v(TAG, "Destroy if called");
        } else {
            MainApplication.screen.getActivity().delPrivateSpaceUI(mSpace,
                    mSpace.equals(Space.getMainSpace()));
            mSpace.getMUC().leave();
            Log.v(TAG, "delPrivateSpaceUI if called");
        }
    }

    /**
     * Grants ownership of a Space.
     *
     * @param newOwner
     *            - the JID of the User to be granted ownership
     */
    public void grantOwnership(String newOwner, boolean isLeaving) {
        if (D) Log.d(TAG, "grantOwnership called");
        try {
            String[] nick = newOwner.split("@");
            this.mSpace.getMUC().grantMembership(newOwner);
            this.mSpace.getMUC().grantModerator(nick[0]);

        } catch (XMPPException e) {
            Log.d(TAG, "XMPP Exception: " + NetworkService.printXMPPError(e));
            Log.d(TAG, "Could not grant ownership to: " + newOwner);
            Log.d(TAG, "exception: " + e.getMessage());
            return;
        }

        if (isLeaving) {
            leaveSpace(mSpace.equals(Space.getMainSpace()));
        }
    }
} // end Class ParticipantController
