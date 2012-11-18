package edu.cornell.opencomm.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.controller.ContactCardController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.User;


//TODO: Ankit:Remove this and the unused attributes
@SuppressWarnings("unused")
public class ContactCardView extends Activity{

	private static TextView name;
	private static ImageView icon;
	private static TextView email;
	private static TextView phoneNumber;
	private static TextView conference1;
	private static TextView conference2;
	private static TextView conference3;
	
	private ContactCardController controller;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_card_layout);
		FontSetter.applySanSerifFont(this.getApplicationContext(), findViewById(R.id.contact_card_layout));
		name = (TextView) findViewById(R.id.contact_card_name);
		icon = (ImageView) findViewById(R.id.contact_card_picture);
		email = (TextView) findViewById(R.id.contact_card_email);
		phoneNumber = (TextView) findViewById(R.id.contact_card_phone);
		conference1 = (TextView) findViewById(R.id.contact_card_conference1);
		conference2 = (TextView) findViewById(R.id.contact_card_conference2);
		conference3 = (TextView) findViewById(R.id.contact_card_conference3);
		controller = new ContactCardController(this);
		String nameStr = this.getIntent().getStringExtra("Contact");
		Log.d("ContactCardView", nameStr);
		for (User u : UserManager.getContactList()) {
			if (u.getNickname().equals(nameStr)) {
				// TODO extract user info
				name.setText(u.getNickname());
				email.setText(u.getUsername());
			}
		}
	}
	
	public void backButtonClicked(View v){
		controller.handleBackButtonClicked();
	}
	public void addButtonClicked(View v){
		controller.handleAddButtonClicked();
	}
	public void overflowButtonClicked(View v){
		controller.handleOverflowButtonClicked();
	}
	public void blockButtonClicked(View v){
		controller.handleBlockButtonClicked();
	}
	@Override
    public void onBackPressed() {
    	// back button disabled
    }

}