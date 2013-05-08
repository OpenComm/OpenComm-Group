package edu.cornell.opencomm.controller;

import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.VCard;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.InvitationsList;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceView;
import edu.cornell.opencomm.view.DashboardView;

public class OCParticipantStatusListener implements ParticipantStatusListener {

	private static final String TAG = OCParticipantStatusListener.class
			.getName();

	public OCParticipantStatusListener(Conference c) {
	}

	@Override
	public void adminGranted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adminRevoked(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void banned(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void joined(String arg0) {
		String username = arg0.split("/")[1];
		Log.v(TAG, "User joined: " + username + "@opencomm");
		if (!username.equals(UserManager.PRIMARY_USER.getUsername().split("@")[0])) {
			final String username2 = username;
			username += "@opencomm";
			
			ConferenceView.handler.post(new Runnable() {

				@Override
				public void run() {
					//call any method that changes the UI here
					ConferenceView.userJoined(username2);
				}
				
			});
			
			String[] params = { username };
			AsyncTask<String, Void, Boolean> a = new JSessionInitiateTask()
					.execute(params);
			try {
				if (a.get()) {
					Log.v(TAG, "Sent sessionInitiate to " + arg0);
				}
			} catch (InterruptedException e) {
				Log.v(TAG, e.getMessage());
			} catch (ExecutionException e) {
				Log.v(TAG, e.getMessage());
			}
		}
	}

	private class JSessionInitiateTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... arg0) {
			String jid = (String) arg0[0];
			VCard v = new VCard();
			try {
				v.load(NetworkService.getInstance().getConnection(), jid);
			} catch (XMPPException e) {
				Log.v(TAG, e.getMessage());
			}
			User u = new User(v);
			u.getJingle()
					.getJiqActionMessageSender()
					.sendSessionInitiate(
							UserManager.PRIMARY_USER.getUsername()
									+ "@opencomm", jid, u.getJingle());
			return true;
		}

	}

	@Override
	public void kicked(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void left(String arg0) {
		// TODO : add UI update code

	}

	@Override
	public void membershipGranted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void membershipRevoked(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moderatorGranted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moderatorRevoked(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nicknameChanged(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ownershipGranted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ownershipRevoked(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void voiceGranted(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void voiceRevoked(String arg0) {
		// TODO Auto-generated method stub

	}

}
