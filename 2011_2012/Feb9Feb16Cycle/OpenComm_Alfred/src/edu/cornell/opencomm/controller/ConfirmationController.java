package edu.cornell.opencomm.controller;

//import android.R;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;
import android.view.View;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConfirmationView;

/**
 * Handles the actions performed on an confirmation popup screen
 * 
 * @author vinay
 * 
 */
public class ConfirmationController {

    private ConfirmationView confirmationView = null;

    public ConfirmationController(ConfirmationView confirmationView) {
        this.confirmationView = confirmationView;
    }

    /** Handle when the window is pressed */
    public void handlePopupWindowClicked() {
        // Dismisses the window for now
        confirmationView.getWindow().dismiss();
    }

    /** Handle when the accept button is pressed */
    public void handleAcceptButtonHover() {
        // Dismisses the window for now
        confirmationView.getAcceptOverlay().setVisibility(View.VISIBLE);
        confirmationView.getWindow().dismiss();
        String kickee = confirmationView.getKickoutInfo()[1];
        User userKickee = User.getAllUsers().get(kickee);
        try {
            confirmationView.getSpace().getKickoutController()
                    .kickoutUser(userKickee, "You've been very bad");
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            Log.v("ConfirmationController", "Cannot kickout this person!");
        }
    }

    /** Handle when the cancel button is pressed */
    public void handleCancelButtonHover() {
        // Dismisses the window for now
        confirmationView.getCancelOverlay().setVisibility(View.VISIBLE);
        confirmationView.getWindow().dismiss();
        // The after effects
    }
}
