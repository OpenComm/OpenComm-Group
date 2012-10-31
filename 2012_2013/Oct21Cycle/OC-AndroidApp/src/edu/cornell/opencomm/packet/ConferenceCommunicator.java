package edu.cornell.opencomm.packet;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;
import edu.cornell.opencomm.model.Conference;

//NOTE: Not yet fully integrated w/ Crystal and database.  Will have that all ready by tomorrow night

public class ConferenceCommunicator {

	public static final String LOG_TAG = "Network.ConferenceCommunicator";
	public static final boolean D = true;
	private PacketCollector collector;
	private PacketListener listener;
	private XMPPConnection xmppConn;
	private static final String PULL_SUBJECT = "pullConference";
	private static final String SUBJECT_PROPERTY_NAME = "subject";

	public ConferenceCommunicator(XMPPConnection xmppConn) {
		this.xmppConn = xmppConn;
		// TODO figure out if connection is acceptable, since Chat sends
		// messages, not packets, and see if we can just have a connection to
		// database so we are not broadcasting packets to everybody
		PacketFilter filter = new PacketTypeFilter(ConferencePacket.class);
		collector = xmppConn.createPacketCollector(filter);
		// collector will allow us to build a que of packets and poll later
		// instead of instantly processing all of the packet upon arrival
		listener = new PacketListener() {
			public void processPacket(Packet arg0) {
				Log.v(LOG_TAG,
						"packet received with subject:"
								+ arg0.getProperty(SUBJECT_PROPERTY_NAME));
			}
		};
	}

	/** Sends conference info to the database. */
	public void pushConference(Conference conference) {
		ConferencePacket packet = conference.toPacket();
		packet.setFrom(xmppConn.getUser());
		xmppConn.sendPacket(packet);
		Log.v(LOG_TAG, "push message sent");
	}

	/**
	 * Sends a message to the database asking for conference data for the
	 * conference with the given roomID.
	 */
	public Conference pullConference(int roomID) {
		ConferencePacket packet = new ConferencePacket(roomID, PULL_SUBJECT);
		packet.setFrom(xmppConn.getUser());
		xmppConn.sendPacket(packet);
		Log.v(LOG_TAG, "pull message sent");
		ConferencePacket polled = (ConferencePacket) collector.pollResult();
		// grabs the next packet in the que, if there is one.
		if (polled != null) {
			String subject = (String) polled.getProperty(SUBJECT_PROPERTY_NAME);
			// cast is safe since ConferencePacket only stores "subject" as a
			// string
			if (!subject.equals(PULL_SUBJECT)) {
				// packet is not the one we sent, must be from database
				// TODO - implement check for validity of returned packet?
				// i.e:
				/*
				 * if(subject.equals(RETURN_SUBJECT)){ //return packet } else{
				 * //return null }
				 */
				Log.v(LOG_TAG, "conference successfully pulled from database");
				return polled.toObject();
			} else {
				// if we caught the packet we sent, it has been removed from the
				// collector que by the pollResult() call, so call nextResult()
				// which will block method until we get the next packet, or
				// return it if it is already in the collector's que
				Log.v(LOG_TAG,
						"removed pull request from collector que, waiting for next packet");
				ConferencePacket resultPacket = (ConferencePacket) collector
						.nextResult();
				// NOTE: can call nextResult(long timeout) (in milliseconds) to
				// return null upon timeout
				Log.v(LOG_TAG,
						"packet found. conference successfully pulled from database");
				return resultPacket.toObject();
			}
		}
		// no packets were collected - return null to satisfy method return type
		// this should not occur - but if method consistently returns null this
		// could be the cause, if our collector is not be functioning properly
		return null;
	}

}
