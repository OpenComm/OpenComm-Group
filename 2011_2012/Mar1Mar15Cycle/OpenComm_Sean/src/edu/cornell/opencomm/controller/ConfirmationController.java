package edu.cornell.opencomm.controller;

import android.view.View;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.ConfirmationView;
/** Handles the actions performed on an confirmation popup screen
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
       
        boolean kickoutStatus = confirmationView.getSpace().getKickoutController().kickoutUser(userKickee, "You've been very bad");
        if (kickoutStatus){
        	
        }
        else{
        	
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
