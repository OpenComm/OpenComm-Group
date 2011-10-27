package edu.cornell.opencomm.controller;

//import android.R;
import edu.cornell.opencomm.view.ConfirmationView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.R.color;

public class ConfirmationController {

	private ConfirmationView confirmationView = null;
	public ConfirmationController(ConfirmationView confirmationView) {
		this.confirmationView = confirmationView;
	}
	public void handlePopupWindowClicked() {
		confirmationView.getWindow().dismiss();		
	}
	public void handleAcceptButtonHover() {
		confirmationView.getAcceptButton().setBackgroundColor(R.color.light_grey);
		
	}
	public void handleCancelButtonHover() {
		confirmationView.getCancelButton().setBackgroundColor(R.color.light_grey);
	}
}
