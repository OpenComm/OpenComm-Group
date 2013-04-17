package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.util.Log;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView;

public class ConferenceController {
	ConferenceView view;
	Conference room;

	private static final String TAG = "ConferenceController";
	private static final boolean D = true;
	private static ConferenceController _instance;

	public static ConferenceController getInstance() {
		if (_instance == null) {
			_instance = new ConferenceController();
		}
		return _instance;
	}

	private ConferenceController() {
		this.view = ConferenceView.getInstance();
		String roomID = null;

		// TODO: Move constructors to more sensible place
		while (this.room == null) {
			roomID = NetworkService.generateRoomID();
			try {
				Log.v(TAG, "trying to create the room");
				this.room = new Conference(roomID);
				Log.v(TAG, "trying to configure the room");
				this.room.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				Log.v(TAG, "trying to join the room");
				this.room.join(UserManager.PRIMARY_USER.getNickname());
			} catch (Exception e) {
				Log.v(TAG, e.getMessage());
				continue;
			}
		}
		// end TODO
	}

	/**
	 * Change the title of the conference (conference name)
	 * 
	 * @param title
	 *            the new title
	 * @return void
	 */
	public void setTitle(String title) {
		view.txtv_ConfTitle.setText(title);
	}

	/**
	 * handle the action when add person button was clicked
	 */
	public void HandleAddPerson(String username) {
		if (D)
			Log.d(TAG, "addPerson button clicked");
		room.invite(username, "let's chat");
	}

	/**
	 * handle the action when overflow button was clicked
	 */
	public void HandleOverflow() {
		if (D)
			Log.d(TAG, "Overflow button clicked");
		// TODO: add "Handle overflow" functions here
	}

	/**
	 * handle the action when back button was clicked
	 */
	public void HandleBackButton() {
		if (D)
			Log.d(TAG, "back button clicked");
		// TODO:add "go back" functions here
	}

	public void HandleLeaveButton() {
		if (D)
			Log.d(TAG, "leave button clicked");

		room.leave();

		try {
			if (room.getParticipants().isEmpty()) {
				room = null;
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
