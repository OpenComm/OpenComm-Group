package edu.cornell.opencomm.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.controller.MyProfileController;
import edu.cornell.opencomm.model.OverflowAdapter;
import edu.cornell.opencomm.manager.UserManager;

/**
 * View for profile page. Functionality (handled by MyProfileController).<br>
 * When corresponding buttons are clicked in the action bar, different app
 * features are launched:
 * <ul>
 * <li>Back: returns to dashboard</li>
 * <li>Edit: launches account edit page</li>
 * <li>Overflow: go to conferences or contacts</li>
 * </ul>
 * 
 * Issues [TODO] - [frontend] Implement functionality for action bar and conf -
 * [backend] Generate full info of primary user info
 * 
 * @author Heming Ge [frontend], Risa Naka [frontend]
 * */
public class MyProfileView extends Activity {
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
	private static final String TAG = MyProfileView.class.getSimpleName();

	private TextView name;
	private ImageView icon;
	private TextView email;
	private TextView password;
	private TextView title;
	private ListView overflowList;
	private String[] options;

	private MyProfileController controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_layout);
		FontSetter
				.applySanSerifFont(this, findViewById(R.id.my_profile_layout));
		name = (TextView) findViewById(R.id.my_profile_name_content);
		icon = (ImageView) findViewById(R.id.my_profile_photo);
		email = (TextView) findViewById(R.id.my_profile_email_content);
		password = (TextView) findViewById(R.id.my_profile_password_content);
		title = (TextView) findViewById(R.id.my_profile_title_content);
		controller = new MyProfileController(this);
		// TODO [frontend] set primary user's information as content
		name.setText(UserManager.PRIMARY_USER.getNickname());
		email.setText(UserManager.PRIMARY_USER.getUsername());
		this.initializeOverflow();
	}

	/**
	 * Initializes the content of overflow. When an item is clicked, user
	 * feedback is generated and an appropriate action is launched
	 */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(
				R.array.overflow_myaccount);
		OverflowAdapter oAdapter = new OverflowAdapter(this,
				R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this
				.findViewById(R.id.my_profile_overflowList);
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

	/**
	 * Called when the back arrow in the action bar is clicked: go back to the
	 * dashboard
	 */
	public void back(View v) {
		controller.handleBackButtonClicked();
	}

	/**
	 * Called when the edit icon in the action bar is clicked: launch account
	 * edit page
	 */
	public void edit(View v) {
		controller.handleEditButtonClicked();
	}

	/**
	 * Called when the overflow button in the action bar is clicked: flip the
	 * visibilty of the overflow list
	 */
	public void overflow(View v) {
		controller.handleOverflowButtonClicked();
	}

	/**
	 * Override the back button press for this activity: instead of the previous
	 * activity, go to the dashboard page
	 */
	public void onBackPressed() {
		super.onBackPressed();
		// launch dasbhoard page
		Intent i = new Intent(this, DashboardView.class);
		this.startActivity(i);
		this.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	/** OnResume: hide overflow list */
	public void onResume() {
		super.onResume();
		this.overflowList.setVisibility(View.INVISIBLE);
	}

}
