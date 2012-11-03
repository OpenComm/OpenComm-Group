package edu.cornell.opencomm.network;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.w3c.dom.ls.LSInput;

import android.os.AsyncTask;
import android.util.Log;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.interfaces.SimpleObserver;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.packet.ConferencePacket;

public class ConferenceComm implements PacketListener {

	
	private static final String PULL_SUBJECT = "pullConferences";
	private static final String PUSH_SUBJECT="pushConferences";
	private static final String PULL_CONFIRMATION_SUBJECT = "ConferenceInfo";
	// subject in packets from database in response to pull request
	private static final String PUSH_CONFIRMATION_SUBJECT = "ConferencePushResult";
	// subject in packets from database in response to pull request
	private static final String PUSH_SUCCESS = "SUCCESS";
	private static final String PUSH_FAILURE = "FAILURE";
	private SimpleObserver listner;
	
	public static final String LOG_TAG = "Network.ConferenceCommunicator";
	public ConferenceComm() {
		PacketFilter filter = new PacketTypeFilter(Packet.class);
		NetworkService.getInstance().getConnection().addPacketListener(this, filter);
	}
	public  void pullConferences(SimpleObserver listner){
		this.listner = listner;
		//TODO: Do we really need to pass anything???
		new PullTask().execute(null);
	}
	public  void pushConference(Conference conference,SimpleObserver listner){
		this.listner = listner;
		new PushTask().execute(conference);
	}
	private class PullTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
//			ConferencePacket packet = new ConferencePacket(roomID, PULL_SUBJECT);
//			packet.setFrom(xmppConn.getUser());
//			packet.setTo("scheduling.cuopencomm.no-ip.org");
//			packet.setPacketID(PULL_SUBJECT);
//			xmppConn.sendPacket(packet);
//			Log.v(LOG_TAG, "pull message sent");
			return null;
		}
		
	}
	private class PushTask extends AsyncTask<Conference, String, String>{

		@Override
		protected String doInBackground(Conference... params) {
			//TODO Ankit/Kris: Remove connect and merge the functionality in getConnection()
			NetworkService.getInstance().connect();
			XMPPConnection xmppConn = NetworkService.getInstance().getConnection();
			Conference conference = params[0];
			ConferencePacket packet = conference.toPacket();
			packet.setFrom(xmppConn.getUser());
			packet.setTo("scheduling.cuopencomm.no-ip.org");
			packet.setPacketID(PUSH_SUBJECT);
			xmppConn.sendPacket(packet);
			Log.v(LOG_TAG, "push message sent");
			return null;
		}
		
	}
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	public void processPacket(Packet arg0) {
		Log.v(LOG_TAG, "packet xml: "+arg0.toXML());
		if (arg0 instanceof Message) {
			Message received = (Message) arg0;
			if (received.getPacketID().equals(PULL_CONFIRMATION_SUBJECT)) {
				if (received.getBody() == null
						|| received.getBody().equals("")) {
					Log.v(LOG_TAG, "pull failed");
					listner.onError("Pull Failure");
				} else {
					Log.v(LOG_TAG, "pull successful.  returned info: "
							+ received.getBody());
					listner.onUpdate(received.getBody());
				}
				
			}
		}
		
	}
	@Override
	protected void finalize() throws Throwable {
		NetworkService.getInstance().getConnection().removePacketListener(this);
		super.finalize();
	}
}
