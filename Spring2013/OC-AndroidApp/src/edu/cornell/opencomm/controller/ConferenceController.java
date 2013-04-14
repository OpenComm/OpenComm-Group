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
	MultiUserChat room;

	private static final String TAG = "ConferenceController_v2";
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
		String roomID = NetworkService.generateRoomID();

		while (this.room == null) {
			roomID = NetworkService.generateRoomID();
			try {
				this.room = new MultiUserChat(NetworkService.getInstance()
						.getConnection(), roomID
						+ ConferenceConstants.CONFERENCE_HOST);
			} catch (Exception e) {
				Log.v(TAG, e.getMessage());
				continue;
			}
		}

		try {
			this.room.join(UserManager.PRIMARY_USER.getUsername());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		try {
			this.room.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		Log.v(TAG, "made the MUC");
		Log.v(TAG, room.getRoom());
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
		if (room == null)
			Log.v(TAG, "Room null!");
		if (username == null)
			Log.v(TAG, "Username null!");
		room.invite(username, "lets chat");
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

		// if no one is left then manually destroy the room
		try {
			if (room.getParticipants().isEmpty()) {
				room = null;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public Conference getMUC() {
		// TODO Auto-generated method stub
		return new Conference(this.room);
	}
}
