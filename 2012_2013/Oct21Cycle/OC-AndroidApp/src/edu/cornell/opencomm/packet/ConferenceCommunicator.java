package edu.cornell.opencomm.packet;


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;
import edu.cornell.opencomm.model.Conference;


//NOTE: Not yet fully integrated w/ Crystal and database.  Will have that all ready by tomorrow night

public class ConferenceCommunicator {

	public static final String LOG_TAG = "Network.ConferenceCommunicator";
	//private PacketCollector collector;
	private PacketListener listener;
	
	private XMPPConnection xmppConn;
	private static final String PULL_SUBJECT = "pullConferences";
	private static final String PUSH_SUBJECT="pushConferences";
	private static final String PULL_CONFIRMATION_SUBJECT = "ConferenceInfo";
	// subject in packets from database in response to pull request
	private static final String PUSH_CONFIRMATION_SUBJECT = "ConferencePushResult";
	// subject in packets from database in response to pull request
	private static final String PUSH_SUCCESS = "SUCCESS";
	private static final String PUSH_FAILURE = "FAILURE";
	//private static final String PULL_FAILURE = null;
	

	//

	public ConferenceCommunicator(XMPPConnection xmppConn) {

		this.xmppConn = xmppConn;
		PacketFilter filter = new PacketTypeFilter(Packet.class);
		//collector = xmppConn.createPacketCollector(filter);
		// collector will allow us to build a queue of packets and poll later
		// instead of instantly processing all of the packet upon arrival
		listener = new PacketListener() {
			public void processPacket(Packet arg0) {
				Log.v(LOG_TAG, "packet xml: "+arg0.toXML());
				if (arg0 instanceof Message) {
					Message received = (Message) arg0;
					if (received.getPacketID().equals(PULL_CONFIRMATION_SUBJECT)) {

						if (received.getBody() == null
								|| received.getBody().equals("")) {
							Log.v(LOG_TAG, "pull failed");
							//return PULL_FAILURE;
						} else {
							Log.v(LOG_TAG, "pull successful.  returned info: "
									+ received.getBody());
							//return received.getBody();
						}
					}else if (received.getSubject().equals(PUSH_CONFIRMATION_SUBJECT)){
						if (received.getBody().equals(PUSH_SUCCESS)) {
							Log.v(LOG_TAG, "push successful");
							//return true;
						} else if (received.getBody().equals(PUSH_FAILURE)) {
							Log.v(LOG_TAG, "push failed");
							//return false;
						} else {
							Log.v(LOG_TAG,
									"push may or may not have failed, body of push confirmation packet was of the wrong format");
							//return false;
						}
						
					}
				}
				/*if (arg0 instanceof Message) {
					// listens for message packets only, since that is what the
					// database sends back, this is just for debug confirmation
					// of packet listening/receiving
					Message arg0Message = (Message) arg0;
					Log.v(LOG_TAG, "packet received with subject:"
							+ arg0Message.getSubject());
					
				}*/

			}
		};
		xmppConn.addPacketListener(listener, filter);
	}

	/** Sends conference info to the database. */
	public void pushConference(Conference conference) {
		ConferencePacket packet = conference.toPacket();
		packet.setFrom(xmppConn.getUser());
		packet.setTo("scheduling.cuopencomm.no-ip.org");
		packet.setPacketID(PUSH_SUBJECT);
		xmppConn.sendPacket(packet);
		Log.v(LOG_TAG, "push message sent");
		/*Message received = null;//?? WRONG
		while (received == null
				|| !received.getSubject().equals(PUSH_CONFIRMATION_SUBJECT)) {
			// poll next packet in que until one with push confirmation is found
			received = (Message) collector.nextResult();
			// cast is safe since all collected packets are filtered for message
		}
		if (received.getBody().equals(PUSH_SUCCESS)) {
			Log.v(LOG_TAG, "push successful");
			return true;
		} else if (received.getBody().equals(PUSH_FAILURE)) {
			Log.v(LOG_TAG, "push failed");
			return false;
		} else {
			Log.v(LOG_TAG,
					"push may or may not have failed, body of push confirmation packet was of the wrong format");
			return false;
		}*/
	}

	/**
	 * Sends a packet to database requesting info on a conference with certain
	 * roomID
	 * 
	 * @param roomID
	 *            - roomID of conference to pull
	 * @return String - returns null if conference not found in database, and if
	 *         conference is found returns data in format: roomID + "//" +
	 *         roomname + "//" invitername + "//" starttime + "//" + endtime +
	 *         "//" recurrence + "//" description + "//" + participant1'sName +
	 *         "//" + participant2'sName... + "//" + participantN'sName + "\n"
	 *         (Note: starttime and endtime are in format:
	 *         "YYYY-MM-DD HH:MM:SS")
	 */
	public void pullConference(int roomID) {
		// note: return type is string. Cannot return conference, since database
		// does not store sufficient information to call conference constructor
		// (since constructor requires user
		ConferencePacket packet = new ConferencePacket(roomID, PULL_SUBJECT);
		packet.setFrom(xmppConn.getUser());
		packet.setTo("scheduling.cuopencomm.no-ip.org");
		packet.setPacketID(PULL_SUBJECT);
		xmppConn.sendPacket(packet);
		Log.v(LOG_TAG, "pull message sent");
		/*Message received = null;
		while (received == null
				|| !received.getSubject().equals(PULL_CONFIRMATION_SUBJECT)) {
			// poll next packet in que until one with pull confirmation is found
			received = (Message) collector.nextResult();
			// cast is safe since all collected packets are filtered for message
		}
		if (received.getBody() == null || received.getBody().equals("")) {
			Log.v(LOG_TAG, "pull failed");
			return PULL_FAILURE;
		} else {
			Log.v(LOG_TAG,
					"pull successful.  returned info: " + received.getBody());
			return received.getBody();
		}*/

	}
	
	/** Retrieve all the conferences that this user is in (and invited to) */
	public static void pullConferencesForUser(String username){
		
	}

}
