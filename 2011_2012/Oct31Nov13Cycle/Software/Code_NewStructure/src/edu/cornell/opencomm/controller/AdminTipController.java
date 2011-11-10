package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.AdminTipView;
import edu.cornell.opencomm.view.TipView;

public class AdminTipController {
	private AdminTipView adminTipView;

	public AdminTipController(AdminTipView adminTipView) {
		this.adminTipView = adminTipView;
	}
	
	public void handlePopupWindowClicked() {
		adminTipView.getWindow().dismiss();		
	}
	
	public void handleAcceptButtonClicked() {
		adminTipView.getAcceptButton().setBackgroundColor(R.color.light_gray);
	}
}
