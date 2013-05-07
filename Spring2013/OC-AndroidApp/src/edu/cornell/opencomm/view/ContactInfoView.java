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
import edu.cornell.opencomm.controller.ContactInfoController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.OverflowAdapter;
import edu.cornell.opencomm.model.User;


/**
 * View for contact info screen. 
 * @author Lu Yang[frontend]
 */


public class ContactInfoView extends Activity {

	private static TextView name;
	private static ImageView icon;
	private static TextView email;
	private static TextView phoneNumber;
	private static TextView title; 
	private ListView overflowList;
	private User user;
	private String[] options; 
	public final static String contactCardKey = "ContactCardKey";
	private ContactInfoController contactInfoController;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info_layout);
		contactInfoController =new ContactInfoController(this);
		name = (TextView) findViewById(R.id.my_profile_name_label); 
		icon = (ImageView) findViewById(R.id.my_profile_photo); 
		title = (TextView) findViewById(R.id.my_profile_title_label); 
		phoneNumber = (TextView) findViewById(R.id.my_profile_phone_label); 
		email = (TextView) findViewById(R.id.my_profile_email_label); 
		// get the user that this card represents
		String nameStr = this.getIntent().getStringExtra(
				ContactInfoView.contactCardKey);
		for (User u : UserManager.getContactList()) {
			if (u.getUsername().equals(nameStr)) {
				user = u;
				name.setText(user.getNickname());
				email.setText(user.getUsername());
				icon.setBackgroundResource(user.getImage()); 
				//TODO: Set the other fields - ask Antoine
			}
		}
		//set font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		initoverflow(); 
	}

	public void edit(View v){

	}

	public void initoverflow(){
		this.options = this.getResources().getStringArray(
				R.array.overflow_contacts);
		OverflowAdapter oAdapter = new OverflowAdapter(this,
				R.layout.overflow_item_layout, this.options);
		overflowList = (ListView) this
				.findViewById(R.id.my_profile_overflowList);
		overflowList.setAdapter(oAdapter);
		// Click event for single list row
		overflowList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				contactInfoController.handleOptionClick(view);
			}
		});
	}

	public void overflow(View v){
		contactInfoController.handleOverflowClicked(); 
	}

	/** = this dashboard's overflow list */
	public ListView getOverflowList() {
		return this.overflowList;
	}

	/** Called when invite conference button is pressed */
	public void inviteConferencePressed(View v){
		contactInfoController.handleInviteConferenceClick();	
	}

	@Override
	/** Override onbackPressed: go back to ContactListView */
	public void onBackPressed() {
		Intent i = new Intent(this, ContactListView.class);
		this.startActivity(i);
		this.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
	
	

}
