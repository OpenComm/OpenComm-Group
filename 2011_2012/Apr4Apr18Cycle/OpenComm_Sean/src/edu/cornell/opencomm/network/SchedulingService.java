package edu.cornell.opencomm.network;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;

import android.content.Intent;
import android.util.Log;

import edu.cornell.opencomm.controller.ConferenceListActivity;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.controller.MainApplication;

import edu.cornell.opencomm.view.DashboardView;
import edu.cornell.opencomm.view.LoginView;

import edu.cornell.opencomm.model.Conference;

/**
 * A class to interact with the conference scheduling plugin.
 * 
 * @author - Kris Kooi
 * */
public class SchedulingService {

	public static final String LOG_TAG = "Network.SchedulingService";
	public static final boolean D = true;

	private ChatManager chatManager;
	private Chat schedulingChat;
	private static LinkedList<Timer> allTimers;
	private static Collection<Conference> allScheduledConferences;
	private XMPPConnection xmppConn;

	public SchedulingService(XMPPConnection xmppConn) {
		this.xmppConn = xmppConn;
		chatManager = xmppConn.getChatManager();
		// Listen for incoming notifications
		chatManager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
				if (!arg1) {
					arg0.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat arg0, Message arg1) {
							if (arg1.getSubject().equals("Broadcast")) {
								startDashboard();
								// TODO: UI - popup reminder
							}
						}
					});
				}
			}
		});
		// Create a chat for communicating with the scheduling plugins
		schedulingChat = chatManager.createChat(
				"scheduling.localhost.localdomain", new MessageListener() {
					@Override
					public void processMessage(Chat arg0, Message arg1) {
						Log.v(LOG_TAG, "Message Received");
						Log.v(LOG_TAG, "Message to XML:" + arg1.toXML());
						if (arg1.getPacketID().equals("ConferenceInfo")) {
							// TODO: Parse out conference info and pass to UI
							Log.v(LOG_TAG, "Conference info pull received");
							allScheduledConferences=(parseConferences(arg1.getBody()));
							ConferenceListActivity.setServerConferences(allScheduledConferences);
							Log.v(LOG_TAG,"allScheduled: " + allScheduledConferences.size());
						} else if (arg1.getSubject().equals(
								"ConferencePushResult")) {
							Log.v(LOG_TAG,"Conference pushed!");
							if (arg1.getBody().equals("Sucess!")) {
								// TODO: UI - give confirmation screen
							} else if (arg1.getBody().equals("Failure...")) {
								// TODO: UI - give error message
							}
						} else if (arg1.getSubject().equals("Error")) {
							// TODO: UI - give error message.
						}
					}
				});
		// Create a Timer to pull conferences every hour
		Timer pullConferences = new Timer();
		pullConferences.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				pullConferences();
			}
		}, 0, 3600000);

	}

	/** Sends conference info to the database. */
	public void pushConference(String owner, String date, long start, long end,
			String recurring, String[] participants) {
		Message push = new Message();
		push.setFrom(xmppConn.getUser());
		push.setPacketID("pushConference");
		push.setBody("INSERT INTO CONFERENCES SET OWNER='" + owner
				+ "', DATE='" + date + "', START='"
				+ new Timestamp(start).toString() + "', END='"
				+ new Timestamp(end).toString() + "', RECURRING='" + recurring
				+ "', PARTICIPANT1='" + participants[0] + "', PARTICIPANT2='"
				+ participants[1] + "', PARTICIPANT3='" + participants[2]
				+ "', PARTICIPANT4='" + participants[3] + "', PARTICIPANT5='"
				+ participants[4] + "', PARTICIPANT6='" + participants[5]
				+ "', PARTICIPANT7='" + participants[6] + "', PARTICIPANT8='"
				+ participants[7] + "', PARTICIPANT9='" + participants[8]
				+ "';");
		try {
			schedulingChat.sendMessage(push);
		} catch (XMPPException e) {
			Log.v(LOG_TAG, e.getMessage());
		}
	}

	/**
	 * Update a conferences on the database.
	 */
	public void updateConference(Conference toUpdate) {
		Message push = new Message();
		push.setPacketID("pushConference");
		push.setBody("UPDATE CONFERENCES SET OWNER='" + "', DATE='" + "', END="
				+ ", END=" + ", RECURRING='" + "', PARTICIPANT1='"
				+ "', PARTICIPANT2='" + "', PARTICIPANT3='"
				+ "', PARTICIPANT4='" + "', PARTICIPANT5='"
				+ "', PARTICIPANT6='" + "', PARTICIPANT7='"
				+ "', PARTICIPANT8='" + "', PARTICIPANT9='" + "' WHERE id=" + 
				toUpdate.getRoom() + ";");
	}

	/** Sends a message to the server asking for conference data. */
	public void pullConferences() {
		Message pull = new Message();
		pull.setFrom(xmppConn.getUser());
		pull.setPacketID("pullConferences");
		pull.setBody("test");
		try {
			schedulingChat.sendMessage(pull);
		} catch (XMPPException e) {
			Log.v(LOG_TAG, e.getMessage());
		}
		Log.v(LOG_TAG,"pull message sent");
	}

	/**
	 * Retrieve conference info from the database.
	 * 
	 * @return - a Collection of ConferenceData representing conferences the
	 *         primary user is a part of
	 */
	public Collection<Conference> parseConferences(String rawData) {
		if (D) Log.v(LOG_TAG, "Received raw data: " + rawData);
		Collection<Conference> data = new LinkedList<Conference>();
		String[] conferences = rawData.split("\n");
		for (String conferenceData : conferences) {
			String[] splitData = conferenceData.split("//");
			if (Arrays.asList(splitData).contains(
					MainApplication.userPrimary.getUsername())
					&& Long.parseLong(splitData[3]) < new Date().getTime()) {
				ArrayList<String> participants = new ArrayList<String>();
				int i = 6;
				int j = 0;
				while (i < splitData.length) {
					participants.add(splitData[i]);
					i++;
					j++;
				}
				Conference info = new Conference(new Date(
						Long.parseLong(splitData[3])), new Date(
						Long.parseLong(splitData[4])), splitData[1],
						splitData[0], participants);
				data.add(info);
				if (info.getStartDate().getTime() - new Date().getTime() < 3600000) {
					allTimers.add(createConferenceTimer(info));
				}
			}
		}
		return data;
	}

	/** Create a RosterGroup associated with a ConferenceData instance */
	public RosterGroup createGroup(Conference info) {
		Roster roster = LoginController.xmppService.getXMPPConnection()
				.getRoster();
		RosterGroup group = roster.createGroup(info.getRoom());
		RosterEntry[] entries = (RosterEntry[]) roster.getEntries().toArray();
		for (String user : info.getContactList()) {
			for (RosterEntry entry : entries) {
				if (entry.getUser().equals(user)) {
					try {
						group.addEntry(entry);
					} catch (XMPPException e) {
						Log.v(LOG_TAG, e.getMessage());
					}
				}
			}
		}
		return group;
	}

	/** Broadcast a message to a RosterGroup. */
	public void broadcast(RosterGroup group, Message message) {
		Collection<RosterEntry> entries = group.getEntries();
		for (RosterEntry entry : entries) {
			Chat broadcastChat = chatManager.createChat(entry.getUser(),
					new MessageListener() {
						@Override
						public void processMessage(Chat arg0, Message arg1) {
						}
					});
			try {
				broadcastChat.sendMessage(message);
			} catch (XMPPException e) {
				Log.v(LOG_TAG, e.getMessage());
			}
		}
	}

	/** Create a timer for a conference notification. */
	public Timer createConferenceTimer(final Conference info) {
		Timer startConference = new Timer();
		Date start = info.getStartDate();
		startConference.schedule(new TimerTask() {

			@Override
			public void run() {

				RosterGroup group = createGroup(info);
				Message broadcastMessage = new Message();
				broadcastMessage.setSubject("Broadcast");
				// TODO: create useful message to pass to other users
				startDashboard();
				broadcast(group, broadcastMessage);

			}

		}, start);
		return startConference;
	}

	public static Collection<Conference> getAllConferences() {
		return allScheduledConferences;
	}

	private void startDashboard() {
		DashboardView.setPopupGo(true);
		Intent i = new Intent((DashboardView.getDashboardInstance()),
				DashboardView.class);
		MainApplication.screen.getActivity().startActivity(i);

	}

}
