package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.DashboardView;

public class DashboardController {
	/* Debugging
	private static String TAG = "Controller.DashboardController";
	private static boolean D = true;*/
	
	private DashboardView dashboardView;

	public DashboardController(DashboardView dashboardView) {
		this.dashboardView = dashboardView;
	}
	
	/*public void handlePopupWindowClicked() {
		dashboardView.getWindow().dismiss();		
	}*/
	
	public void handleStartConferenceButtonClicked() {
		dashboardView.getDashboardOverlay().setVisibility(View.VISIBLE);
		ProgressDialog.show(this.dashboardView, "", "Loading. Please wait...", true);
		String username = this.dashboardView.getIntent().getStringExtra(Network.KEY_USERNAME);
		dashboardView.getStartConferenceButton().setBackgroundColor(R.color.light_grey);	
		
		//Intent mainIntent = new Intent(dashboardView, MainApplication.class);
		//dashboardView.startActivityForResult(mainIntent,0);
		Intent i = new Intent(dashboardView, MainApplication.class);
		i.putExtra(Network.KEY_USERNAME, username);
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
