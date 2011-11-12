package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.view.DashboardView;

public class DashboardController {
	private DashboardView dashboardView;

	public DashboardController(DashboardView dashboardView) {
		this.dashboardView = dashboardView;
	}
	
	public void handlePopupWindowClicked() {
		dashboardView.getWindow().dismiss();		
	}
	
	public void handleStartConferenceButtonClicked() {
		dashboardView.getStartConferenceButton().setBackgroundColor(R.color.light_grey);		
	}
}
