package edu.cornell.opencomm.controller;

import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.VCard;

import android.os.AsyncTask;
import android.util.Log;

import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

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
		Log.v(TAG, arg0 + " joined the room");
		String[] params = { arg0 };
		AsyncTask<String, Void, Boolean> a = new JSessionInitiateTask()
				.execute(params);
		try {
			if (a.get()) {
				Log.v(TAG, "Sent sessionInitiate to " + arg0);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class JSessionInitiateTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... arg0) {
			String jid = (String) arg0[0];
			Log.v(TAG, jid);
			VCard v = new VCard();
			try {
				v.load(NetworkService.getInstance().getConnection(), jid);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			User u = new User(v);
			u.getJingle()
					.getJiqActionMessageSender()
					.sendSessionInitiate(
							UserManager.PRIMARY_USER.getUsername(), jid,
							u.getJingle());
			return true;
		}

	}

	@Override
	public void kicked(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void left(String arg0) {
		// TODO Auto-generated method stub

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
