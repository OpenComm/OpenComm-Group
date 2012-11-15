package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.FontSetter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * View for dashboard.
 * Functionality (handled by DashboardController).<br>
 * When corresponding buttons are clicked, different app features are launched:
 * <ul>
 * <li>Launches notification in action bar</li>
 * <li>Launches overflow in action bar</li>
 * <li>Launches conference info</li>
 * <li>Launches conference list</li>
 * <li>Launches contact list</li>
 * <li>Launches profile info</li>
 * </ul>
 *
 * Issues [TODO]
 * - [frontend] Implement functionality for action bar and conf info
 *
 * @author Risa Naka [frontend]
 * */
public class DashboardView extends Activity {
	/** 
	 * Debugging variable: if true, all logs are logged;
	 * set to false before packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;
	
	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = DashboardView.class.getSimpleName();
	
	private DashboardController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.dashboard_layout));
		this.controller = new DashboardController(this, DashboardView.this);
	}
	
	public void notification(View v){
		this.controller.handleNotificationClicked();
	}
	
	public void overflow(View v) {
		this.controller.handleOverflowClicked();
	}
	
	public void goToConfInfo(View v) {
		this.controller.handleConfInfoButtonClicked();
	}
	
	public void goToConfs(View v) {
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
	}
}
