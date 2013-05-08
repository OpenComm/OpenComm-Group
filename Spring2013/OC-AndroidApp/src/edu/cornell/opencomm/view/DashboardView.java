package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.DashboardController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.Invitation;
import edu.cornell.opencomm.model.InvitationsList;
import edu.cornell.opencomm.model.OverflowAdapter;
import edu.cornell.opencomm.network.NetworkService;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

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
 *  Issues [TODO] - [frontend] Implement functionality for action bar and conf
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

	private static DashboardController controller;

	/** Overflow variables */
	private String[] options;
	private ListView overflowList;
	public static TextView first_conference;
	public static TextView second_conference;

	@Override
	/** Constructor: dashboard view; initializes overflow */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout_2);
		FontSetter.applySanSerifFont(this, findViewById(R.id.dashboard_layout));
		first_conference = (TextView) findViewById(R.id.second_invitation);
		second_conference = (TextView) findViewById(R.id.third_invitation);
		controller = new DashboardController(this, DashboardView.this);
		setConferences(InvitationsList.getInvitations());
		this.initializeOverflow();
	}
	
	
	public static Handler handler = new Handler();
	
	public static void setConferences(final ArrayList<Invitation> invitationsList) {
		if (invitationsList.size() == 1) {
			first_conference.setText(invitationsList.get(0).getInviterName());
			first_conference.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToInvitation(invitationsList.get(0).room);
					invitationsList.get(0).process();
					InvitationsList.removeProcessedInvitations();
				}
				
			});
			first_conference.invalidate();
		} else if (invitationsList.size() >= 2) {
			first_conference.setText(invitationsList.get(0).getInviterName());
			first_conference.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToInvitation(invitationsList.get(0).room);
					invitationsList.get(0).process();
					InvitationsList.removeProcessedInvitations();
				}
				
			});
			second_conference.setText(invitationsList.get(1).getInviterName());
			second_conference.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToInvitation(invitationsList.get(1).room);
					invitationsList.get(1).process();
					InvitationsList.removeProcessedInvitations();
				}
				
			});
			first_conference.invalidate();
			second_conference.invalidate();
		}
	}
	

	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(R.array.overflow_dashboard);
		OverflowAdapter oAdapter = new OverflowAdapter(this, R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this.findViewById(R.id.dashboard_overflowList);
		overflowList.setAdapter(oAdapter);
		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
		controller.handleNotificationClicked();
	}

	/** Called when overflow icon in the action bar is clicked: if overflows are not 
	 * already visible, display them. Otherwise, hide them. */
	public void overflow(View v) {
		controller.handleOverflowButtonClicked();
	}

	/** Called when Conf Info icon is clicked: opens the current conference in session */
	public void goToConfInfo(View v) {
		controller.handleConfInfoButtonClicked();
	}

	/** Called when Conferences icon is clicked: opens the conference scheduler */
	public void goToConfs(View v) {
		
		//TODO: replace this with roomID from invitation (the following line will be 
			//used when creating a new conference, so we need a way to differentiate 
			//between the two cases)
		String roomID = NetworkService.generateRoomID() + NetworkService.CONF_SERVICE;
		
		controller.handleConferencesButtonClicked(roomID);
	}
	
	public static void goToInvitation(String roomID) {
		controller.handleConferencesButtonClicked(roomID);
	}

	/** Called when Contacts icon is clicked: opens the primary user's contact list */
	public void goToContacts(View v) {
		controller.handleContactsButtonClicked();
	}

	/** Called when Profile icon is clicked: opens the primary user's profile page */
	public void goToProfile(View v) {
		controller.handleProfileButtonClicked();
	}

	@Override
	public void onBackPressed() {
		// back button disabled
	}
}