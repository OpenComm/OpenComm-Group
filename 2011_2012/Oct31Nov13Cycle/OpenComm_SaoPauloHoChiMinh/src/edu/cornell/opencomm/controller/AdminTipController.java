package edu.cornell.opencomm.controller;

import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.AdminTipView;
/** Handles the actions performed on an admin tip popup screen
 * 
 * @author vinay
 *
 */


public class AdminTipController {
	private AdminTipView adminTipView;

	public AdminTipController(AdminTipView adminTipView) {
		this.adminTipView = adminTipView;
	}
	
	/** Handle when the window is pressed */
	public void handlePopupWindowClicked() {
		// Dismisses the window for now
		adminTipView.getWindow().dismiss();		
	}
	
	/** Handle when the accept button is pressed */
	public void handleAcceptButtonClicked() {
		adminTipView.getAcceptBarOverlay().setVisibility(View.VISIBLE);
		// Dismisses the window for now
		adminTipView.getWindow().dismiss();	
	}
}
