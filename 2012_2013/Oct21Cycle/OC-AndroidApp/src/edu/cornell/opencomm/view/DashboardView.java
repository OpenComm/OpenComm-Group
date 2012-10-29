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
		FontSetter.applySanSerifFont(this, findViewById(R.id.dashboard_layout));
		this.controller = new DashboardController(this, DashboardView.this);
	}
	
	public void goToConfInfo(View v) {
		this.controller.handleConfInfoButtonClicked();
	}
	
	public void goToConferences(View v) {
		this.controller.handleConferencesButtonClicked();
	}
	
	public void goToContacts(View v) {
		this.controller.handleContactsButtonClicked();
	}
	
	public void goToProfile(View v) {
		this.controller.handleProfileButtonClicked();
	}
	
	public void onBackPressed() {
		// back button disabled
	}
	
	@Override
	public void onResume() {
		super.onResume();
		findViewById(R.id.dashboardConfInfoOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardConfsOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardContactsOverlay).setVisibility(View.INVISIBLE);
		findViewById(R.id.dashboardProfileOverlay).setVisibility(View.INVISIBLE);
	}
}
