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
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.controller.ContactCardController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.OverflowAdapter;
import edu.cornell.opencomm.model.User;


/**
 * View for contact card page. Functionality (handled by ContactCardController).<br>
 * When corresponding buttons are clicked in the action bar, different app features are launched:
 * <ul>
 * <li>Back: returns to contact list</li>
 * <li>Add: add this user to contact</li>
 * <li>Overflow: go to conferences or account page</li>
 * </ul>
 * When block is clicked, this user is blocked
 * 
 * Issues [TODO] 
 * - [frontend] Implement functionality for action bar and conf
 * - [backend] Generate full info of contacts
 * info
 * 
 * @author Heming Ge [frontend], Risa Naka [frontend]
 * */
public class ContactCardView extends Activity{
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
	private static final String TAG = ContactCardView.class.getSimpleName();

	private static TextView name;
	private static ImageView icon;
	private static TextView email;
	private static TextView phoneNumber;
	private static TextView conference1;
	private static TextView conference2;
	private static TextView conference3;
	
	public final static String contactCardKey = "ContactCardKey";
	
	/** User that this card represents */
	private User user;
	private boolean isFriend;
	
	private ContactCardController controller;
	
	/** Overflow variables: list and options */
	private ListView overflowList;
	private String[] options;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_card_layout);
		FontSetter.applySanSerifFont(this.getApplicationContext(), findViewById(R.id.contact_card_layout));
		name = (TextView) findViewById(R.id.contact_card_title);
		icon = (ImageView) findViewById(R.id.contact_card_photo);
		email = (TextView) findViewById(R.id.contact_card_email_content);
		phoneNumber = (TextView) findViewById(R.id.contact_card_phone_content);
		conference1 = (TextView) findViewById(R.id.contact_card_prevconf1_content);
		conference2 = (TextView) findViewById(R.id.contact_card_prevconf2_content);
		conference3 = (TextView) findViewById(R.id.contact_card_prevconf3_content);
		controller = new ContactCardController(this);
		// get the user that this card represents
		String nameStr = this.getIntent().getStringExtra(ContactCardView.contactCardKey);
		for (User u : UserManager.getContactList()) {
			if (u.getUsername().equals(nameStr)) {
				user = u;
				// TODO [frontend] extract user info
				name.setText(user.getNickname());
				email.setText(user.getUsername());
				isFriend = true;
				// TODO [frontend] if user is a friend, change add icon to a check
				if (isFriend) {
					ImageView addIcon = (ImageView) findViewById(R.id.contact_card_addIcon);
					addIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.action_check));
				}
			}
		}
		this.initializeOverflow();
	}
	
	/** Initializes the content of overflow. When an item is clicked, user feedback is generated 
	 * and an appropriate action is launched */
	private void initializeOverflow() {
		this.options = this.getResources().getStringArray(R.array.overflow_contacts);
		OverflowAdapter oAdapter = new OverflowAdapter(this, R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this.findViewById(R.id.contact_card_overflowList);
		overflowList.setAdapter(oAdapter);
		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleOptionClick(view);
			}
		});
	}
	
	/** = the user this contact card represents */
	public User getUser() {
		return this.user;
	}

	/** = this dashboard's overflow list */
	public ListView getOverflowList() {
		return this.overflowList;
	}
	
	/** Back button clicked: go to contactlistview */
	public void back(View v) {
		this.controller.handleBackButtonClicked();
	}
	
	/** Add button clicked: if this user is not a friend, add as a friend. Otherwise, a toast 
	 * is returned */
	public void add(View v) {
		this.controller.handleAddButtonClicked(this.isFriend);
	}
	
	public void overflow(View v) {
		this.controller.handleOverflowButtonClicked();
	}
	
	public void block(View v) {
		this.controller.handleBlockButtonClicked();
	}
	@Override
	/** Override onbackPressed: go back to ContactListView */
    public void onBackPressed() {
    	Intent i = new Intent(this, ContactListView.class);
    	this.startActivity(i);
    }

}