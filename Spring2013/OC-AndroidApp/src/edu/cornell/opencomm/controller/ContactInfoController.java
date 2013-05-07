package edu.cornell.opencomm.controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.ContactInfoView;
import edu.cornell.opencomm.view.MyProfileView;

public class ContactInfoController {

	private ContactInfoView view; 

	public ContactInfoController(ContactInfoView view){
		this.view = view;
	}

	public void handleInviteConferenceClick() {
		// TODO Auto-generated method stub
	}

	//TODO
	public void handleOptionClick(View view) {
	}

	public void handleOverflowClicked() {
		if (this.view.getOverflowList().getVisibility() == View.INVISIBLE) {
			this.view.getOverflowList().setVisibility(View.VISIBLE);
		} else {
			this.view.getOverflowList()
			.setVisibility(View.INVISIBLE);
		}
	}


}
