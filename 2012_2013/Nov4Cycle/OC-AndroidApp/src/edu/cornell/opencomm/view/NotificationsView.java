package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.NotificationsController;
import edu.cornell.opencomm.model.Notification;
import edu.cornell.opencomm.model.NotificationsAdapter;
import edu.cornell.opencomm.model.OverflowAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View for Notifications (NotificationsView)
 * Functionality (handled by NotificationsController).<br>
 * When a notification is clicked, its corresponding conference card is loaded
 *
 * Issues [TODO]
 * - [frontend] Implement conference card launch
 * - [backend] push notifications
 * - [backend] retrieve conference info
 *
 * @author Risa Naka [frontend]
 * */
public class NotificationsView extends Activity{
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
	private static final String TAG = NotificationsView.class.getSimpleName();
	
	// mock notification
	Notification notification1 = new Notification("Test conference has started", null);
	Notification notification2 = new Notification("Risa has invited you", null);

	private NotificationsController controller;
	
	private ArrayList<Notification> notes;
	
	private ListView overflowList;
	private ListView notificationsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications_layout);
		FontSetter.applySanSerifFont(this, findViewById(R.id.notifications_layout));
		controller = new NotificationsController(this);
		this.initializeOverflow();
		this.initializeNotifications();
	}
	
	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		String[] options = this.getResources().getStringArray(R.array.overflow_notifications);
		OverflowAdapter oAdapter = new OverflowAdapter(this,
				R.layout.overflow_item_layout, options);
		this.overflowList = (ListView) this
				.findViewById(R.id.notifications_overflowList);
		overflowList.setAdapter(oAdapter);
		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleOptionClick(view);
			}
		});
	}
	
	/** Initializes the content of notification. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeNotifications() {
		this.notes = Notification.getAllNotifications();
		NotificationsAdapter nAdapter = new NotificationsAdapter(this,
				R.layout.notifications_item_layout, notes);
		this.notificationsList = (ListView) this.findViewById(R.id.notifications_notificationList);
		notificationsList.setAdapter(nAdapter);
		// Click event for single list row
		notificationsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleNoteClick(view);
			}
		});
	}
	
	/** Overflow button clicked; if it's shown, hide the list; otherwise, show the list */
	public void overflow(View v) {
		controller.handleOverflowButtonClicked();
	}
	
	/** Back arrow button clicked: goes back to previous activity */
	public void back(View v){
		controller.handleBackButtonClicked();
	}

	/** = overflow list */
	public ListView getOverflowList() {
		return this.overflowList;
	}
}
