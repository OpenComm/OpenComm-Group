package edu.cornell.opencomm.controller;

import android.view.View;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ContactInfoView;

public class ContactInfoController {

	private ContactInfoView view; 

	public ContactInfoController(ContactInfoView view){
		this.view = view;
	}

	public void handleInviteConferenceClick() {
		// TODO Auto-generated method stub
		NetworkService.getInstance().addUserToBuddyList(view.user.getUsername());
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
