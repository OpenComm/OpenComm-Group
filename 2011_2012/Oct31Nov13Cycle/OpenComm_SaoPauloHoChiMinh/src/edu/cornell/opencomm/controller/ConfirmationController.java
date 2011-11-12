package edu.cornell.opencomm.controller;

//import android.R;
import edu.cornell.opencomm.R;
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
	
	/** Handle when the cancel button is pressed */
	public void handleAcceptButtonHover() {
		// Turn button grey for now
		confirmationView.getAcceptButton().setBackgroundColor(R.color.light_grey);
	}
	/** Handle when the cancel button is pressed */
	public void handleCancelButtonHover() {
		// Turn button grey for now
		confirmationView.getCancelButton().setBackgroundColor(R.color.light_grey);
	}
}
