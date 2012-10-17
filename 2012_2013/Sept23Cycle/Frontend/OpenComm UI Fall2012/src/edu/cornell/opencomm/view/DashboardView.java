package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.FontSetter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DashboardView extends Activity {
	
	private DashboardController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.layout.dashboard_layout));
		this.controller = new DashboardController(this, DashboardView.this);
	}
	
	public void goToContacts(View v) {
		this.controller.handleContactListButtonClicked();
	}
	
	public void goToConferences(View v) {
		this.controller.handleConferenceButtonClicked();
	}
	
	public void goToAccounts(View v) {
		this.controller.handleAccountButtonClicked();
	}
	
	public void logOut(View v) {
		this.controller.handleLogoutButtonClicked();
	}
	
	public void onBackPressed() {
		// back button disabled
	}
	
	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.dashboardContactOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardConfOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardAccountOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardLogoutOverlay).setVisibility(View.INVISIBLE);
	}
}
