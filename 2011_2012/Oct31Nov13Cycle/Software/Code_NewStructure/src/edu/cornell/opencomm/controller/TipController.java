package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.TipView;

public class TipController {
	private TipView tipView;

	public TipController(TipView tipView) {
		this.tipView = tipView;
	}
	
	public void handlePopupWindowClicked() {
		tipView.getWindow().dismiss();		
	}
	
	public void handleAcceptButtonClicked() {
		tipView.getAcceptButton().setBackgroundColor(R.color.light_gray);	
	}
}
