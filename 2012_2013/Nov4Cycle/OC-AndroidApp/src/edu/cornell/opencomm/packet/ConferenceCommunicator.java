package edu.cornell.opencomm.packet;


import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import edu.cornell.opencomm.interfaces.SimpleObserver;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

public class ConferenceCommunicator implements PacketListener {

	//Packet IDs for three kinds of packets this class could send
	private static final String PULL_ID = "pullConferences";
	private static final String PUSH_ID = "pushConferences";
	private static final String TIP_ID = "pullAllConferences";
	//Packet IDs for three kinds of packets this class could receive
	private static final String PULL_CONFIRMATION = "ConferenceInfo";
	private static final String PUSH_CONFIRMATION = "ConferencePushResult";
	private static final String PULL_ALL_CONFIRMATION = "GetAllConferences";
	
	private static final String PUSH_SUCCESS = "SUCCESS";
	private static final String DESTINATION = "conferencescheduling.cuopencomm";
	private SimpleObserver listner;

	public static final String LOG_TAG = "Network.ConferenceCommunicator";

	public ConferenceCommunicator() {
		PacketFilter filter = new PacketTypeFilter(Packet.class);
		NetworkService.getInstance().getConnection()
				.addPacketListener(this, filter);
	}
	/*pull the conferences which the current user is part of*/
	public void pullConferences(SimpleObserver listner) {
		this.listner = listner;
		//test only: TOBEDELETED
		//Conference conference=new Conference("Testers Meeting", "We are awesome!", new GregorianCalendar(2012,11,24, 9,10,0),new GregorianCalendar(2012,11,24, 11,10,0),"Annual",new User("oc4testorg","Bull", 0), null);
		//this.pushConference(conference, listner);
		
		new PullTask().execute();
	}
	/*push a new conference to the Database*/
	public void pushConference(Conference conference, SimpleObserver listner) {
		this.listner = listner;
		new PushTask().execute(conference);
	}

	
	private class PullTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void...voids) {
			XMPPConnection xmppConn = NetworkService.getInstance()
					.getConnection();
			/* String to split. */
			String str = xmppConn.getUser();
			String[] temp;
			/* delimiter */
			String delimiter = "@";
			/* given string will be split by the argument delimiter provided. */
			temp = str.split(delimiter);
			ConferencePacket packet = new ConferencePacket(temp[0],
					PULL_ID);
			packet.setFrom(xmppConn.getUser());
			packet.setTo(DESTINATION);
			packet.setPacketID(PULL_ID);
			xmppConn.sendPacket(packet);
			Log.v(LOG_TAG, "pull message sent: "+packet.toXML());
			return null;
		}

	}

	private class PushTask extends AsyncTask<Conference, String, String> {

		@Override
		protected String doInBackground(Conference... params) {
			XMPPConnection xmppConn = NetworkService.getInstance()
					.getConnection();
			Conference conference = params[0];
			ConferencePacket packet = conference.toPacket();
			packet.setFrom(xmppConn.getUser());
			packet.setTo(DESTINATION);
			packet.setPacketID(PUSH_ID);
			xmppConn.sendPacket(packet);
			Log.v(LOG_TAG, "push message sent");
			return null;
		}

	}

	/*
	 * AsyncTask that is associated with NotificationChecker
	 * 
	 * @author:Crystal Qin
	 */
	private class NotificationTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			XMPPConnection xmppConn = NetworkService.getInstance()
					.getConnection();
			ConferencePacket packet = new ConferencePacket("", TIP_ID);
			packet.setFrom(xmppConn.getUser());
			// packet.setFrom("");
			packet.setTo(DESTINATION);
			packet.setPacketID(TIP_ID);
			xmppConn.sendPacket(packet);
			Log.v(LOG_TAG, "pull all message sent");
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.
	 * smack.packet.Packet)
	 */
	public void processPacket(Packet arg0) {
		Log.v(LOG_TAG, "packet xml: " + arg0.toXML());
		if (arg0 instanceof Message) {
			Message received = (Message) arg0;
			Log.v(LOG_TAG, "Response received:" + received.getPacketID());

			if (received.getPacketID().equals(PULL_CONFIRMATION)) {
				if (received.getBody() == null || received.getBody().equals("")) {
					Log.v(LOG_TAG, "pull failed");
					listner.onError("Pull Failure");
				} else {
					Log.v(LOG_TAG, "pull successful.  returned info: "
							+ received.getBody());
					listner.onUpdate(911, received.getBody());
				}

			} else if (received.getPacketID().equals(PUSH_CONFIRMATION)) {
				if (received.getBody().equals(PUSH_SUCCESS)) {
					Log.v(LOG_TAG, "push successful.  returned info: "
							+ received.getBody());
				} else {
					Log.v(LOG_TAG, "push failed");
					listner.onError("push Failure");
					// TODO How to deal with push failure?
				}

			} else if (received.getPacketID().equals(PULL_ALL_CONFIRMATION)) {
				if (received.getBody() == null || received.getBody().equals("")) {
					Log.v(LOG_TAG, "pull failed");
					listner.onError("pull Failure");
				} else {
					Log.v(LOG_TAG, "pull successful.  returned info: "
							+ received.getBody());
					// TODO Front-end: which method to Call?
				}

			}

		}

	}

	/*
	 * Create background thread that keeps checking the conferences stored in
	 * database to provide pop-up notification to the UI
	 * 
	 * @author: Crystal Qin
	 */

	public void NotificationChecker() {
		TimerTask checker;
		final Handler handler = new Handler();
		Timer timer = new Timer();

		checker = new TimerTask() {

			@Override
			public void run() {

				handler.post(new Runnable() {
					public void run() {

						try {
							NotificationTask performBackgroundTask = new NotificationTask();
							performBackgroundTask.execute();

						} catch (Exception e) {
							Log.v(LOG_TAG, "NotificationChecker execute error");

						}

					}
				});

			}

		};

		timer.schedule(checker, 0, 900000);// execute in every 15 mins

	}

	@Override
	protected void finalize() throws Throwable {
		NetworkService.getInstance().getConnection().removePacketListener(this);
		super.finalize();
	}

}
