package edu.cornell.opencomm.controller;

import android.view.View;
import edu.cornell.opencomm.R;
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
		tipView.getAcceptBarOverlay().setVisibility(View.VISIBLE);
		// Dismisses the window for now
		tipView.getWindow().dismiss();	
	}
}
