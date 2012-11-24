package edu.cornell.opencomm.view;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.OverflowAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View for dashboard. Functionality (handled by DashboardController).<br>
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
 * Issues [TODO] - [frontend] Implement functionality for action bar and conf
 * info
 * 
 * @author Risa Naka [frontend]
 * */
public class DashboardView extends Activity {
	/**
	 * Debugging variable: if true, all logs are logged; set to false before
	 * packaging
	 */
	@SuppressWarnings("unused")
	private static final boolean D = true;

	/**
	 * The TAG for logging
	 */
	@SuppressWarnings("unused")
	private static final String TAG = DashboardView.class.getSimpleName();

	private DashboardController controller;

	/** Overflow variables */
	private String[] options;
	private ListView overflowList;

	@Override
	/** Constructor: dashboard view; initializes overflow */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.dashboard_layout));
		this.options = this.getResources().getStringArray(R.array.overflow_dashboard);
		this.controller = new DashboardController(this, DashboardView.this);
		this.initializeOverflow();
	}

	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		
		OverflowAdapter oAdapter = new OverflowAdapter(this,
				R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this
				.findViewById(R.id.dashboard_overflowList);
		overflowList.setAdapter(oAdapter);
		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleOptionClick(view);
			}
		});
	}

	/** = this dashboard's overflow list */
	public ListView getOverflowList() {
		return this.overflowList;
	}

	/** Called when notification icon in the action bar is clicked: if notifications are not 
	 * already visible, display them. Otherwise, hide them. */
	public void notification(View v) {
		this.controller.handleNotificationClicked();
	}

	/** Called when overflow icon in the action bar is clicked: if overflows are not 
	 * already visible, display them. Otherwise, hide them. */
	public void overflow(View v) {
		this.controller.handleOverflowClicked();
	}

	/** Called when Conf Info icon is clicked: opens the current conference in session */
	public void goToConfInfo(View v) {
		this.controller.handleConfInfoButtonClicked();
	}

	/** Called when Conferences icon is clicked: opens the conference scheduler */
	public void goToConfs(View v) {
		this.controller.handleConferencesButtonClicked();
	}

	/** Called when Contacts icon is clicked: opens the primary user's contact list */
	public void goToContacts(View v) {
		this.controller.handleContactsButtonClicked();
	}

	/** Called when Profile icon is clicked: opens the primary user's profile page */
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
