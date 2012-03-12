/**
 * Controller called from DashboardVirw. Takes care of dashboard related functionality. 
 * For now, it handles start conference button functionality.
 * Starts main application activity once the button is clicked
 * 
 * Issues [TODO]
 * - Handle for case when user wants to go back to dashboard from Mainapplication and clicks "start 
 * conference" button. 
 * - For any other issues search for string "TODO"
 * 
 * @author rahularora[hcisec], vinaymaloo[ui]
 * */

package edu.cornell.opencomm.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;
import edu.cornell.opencomm.view.ConferencePlannerView;
import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.ResetPasswordView;

public class DashboardController {
	// Debugging
	private static final boolean D = Values.D;

	// Logs
	private static String TAG = "Controller.DashboardController";

	private DashboardView dashboardView;
	public static String username;

	public DashboardController(DashboardView dashboardView) {
		this.dashboardView = dashboardView;
	}

	public void handleStartConferenceButtonClicked() {
		dashboardView.getDashboardOverlay().setVisibility(View.VISIBLE);
		ProgressDialog progress = ProgressDialog.show(this.dashboardView, "",
				"Loading. Please wait...", true);

		username = this.dashboardView.getIntent().getStringExtra(
				Network.KEY_USERNAME);
		dashboardView.getStartConferenceButton().setBackgroundColor(
				R.color.light_grey);

		// Crystal: To launch planner instead of conference space in dashboard
		// Intent i = new Intent(dashboardView, MainApplication.class);
		// i.putExtra(Network.KEY_USERNAME, username);
		// i.setAction(Network.ACTION_LOGIN);

		// dashboardView.startActivity(i);

		// Crystal's code
		Intent myIntent = new Intent();
		myIntent.setClass(dashboardView.getApplication(),
				ConferencePlannerView.class);
		dashboardView.startActivity(myIntent);

		// Log.v("Dashboardcontroller", "launch");
		// Intent i= new Intent();
		// LayoutInflater ifl=dashboardView.getInflater();
		// ConferencePlannerView cpv= new ConferencePlannerView(ifl);
		// cpv.setContext(Space.getMainSpace().getContext());
		// cpv.launch();
		// progress.dismiss();

	}

	public void handleContactsButtonClicked() {

	}

	public void handleAccountButtonClicked() {

	}

	public void handleHistoryButtonClicked() {

	}

	/*
	 * public void handlePopupWindowClicked() {
	 * dashboardView.getWindow().dismiss(); }
	 */

}
