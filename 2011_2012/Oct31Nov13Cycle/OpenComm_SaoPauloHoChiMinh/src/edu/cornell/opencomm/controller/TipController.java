package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.TipView;

public class TipController {
	private TipView tipView;

	public TipController(TipView tipView) {
		this.tipView = tipView;
	}
	
	/** Handle when the window is pressed */
	public void handlePopupWindowClicked() {
		// For now, dismisses the screen
		tipView.getWindow().dismiss();		
	}
	
	/** Handle when the accept button is pressed */
	public void handleAcceptButtonClicked() {
		// For now, simply turn the button gray
		tipView.getAcceptButton().setBackgroundColor(R.color.light_gray);	
	}
}
