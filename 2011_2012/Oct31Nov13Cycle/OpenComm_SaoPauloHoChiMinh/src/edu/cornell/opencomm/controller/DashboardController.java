package edu.cornell.opencomm.controller;

import android.content.Intent;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.DashboardView;

public class DashboardController {
	private DashboardView dashboardView;

	public DashboardController(DashboardView dashboardView) {
		this.dashboardView = dashboardView;
	}
	
	/*public void handlePopupWindowClicked() {
		dashboardView.getWindow().dismiss();		
	}*/
	
	public void handleStartConferenceButtonClicked() {
		dashboardView.getStartConferenceButton().setBackgroundColor(R.color.light_grey);	
		
		Intent i = new Intent(dashboardView, MainApplication.class);
		//i.putExtra(Network.KEY_USERNAME, (D ? Network.DEBUG_USERNAME : usernameEdit.getText().toString()));
		i.putExtra(Network.KEY_USERNAME, Network.DEBUG_USERNAME);
		i.setAction(Network.ACTION_LOGIN);
		dashboardView.startActivity(i);
	}
	
	public void handleContactsButtonClicked(){
	}
	public void handleAccountButtonClicked(){
	}
	public void handleHistoryButtonClicked(){
	}
	
}
