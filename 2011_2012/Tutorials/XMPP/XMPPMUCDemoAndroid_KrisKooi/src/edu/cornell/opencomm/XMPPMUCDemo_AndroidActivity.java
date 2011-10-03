package edu.cornell.opencomm;

import java.util.Collection;
//import java.util.Timer;
//import java.util.TimerTask;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class XMPPMUCDemo_AndroidActivity extends Activity {

	private static Connection con;
	private static Roster buddyList;
	private static MultiUserChat chatRoom;

	private EditText chatMessage;
	private Button sendMessage;
	private Button inviteUser;
	private Button kickUser;
	private Button leaveChat;
	private Button logout;

	// private Timer timer;

	private ListView chatMessages;
	private ArrayAdapter<String> messagesAdapter;
	private ListView buddies;
	private static ArrayAdapter<String> buddiesAdapter;
	private ListView members;
	private static ArrayAdapter<String> membersAdapter;

	public static final String TAG = "XMPPMUCDemo_AndroidActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		chatMessage = (EditText) findViewById(R.id.etChatMessage);
		sendMessage = (Button) findViewById(R.id.bSendMessage);
		inviteUser = (Button) findViewById(R.id.bInvite);
		kickUser = (Button) findViewById(R.id.bKick);
		leaveChat = (Button) findViewById(R.id.bLeave);
		logout = (Button) findViewById(R.id.bLogout);
		buddies = (ListView) findViewById(R.id.buddyList);
		members = (ListView) findViewById(R.id.chatMembers);
		// timer = new Timer();

		try {
			// Try to connect
			connect();
		} catch (XMPPException e) {
			Log.v(TAG, "There's some problem with the XMPP "
					+ "server. Please try again later.");
			Log.e(TAG, "exception:", e);
			System.exit(0);
		}
		try {
			// Try to create the room
			createChat();
		} catch (XMPPException e1) {
			Log.v(TAG, "There's some problem with the chat"
					+ "room you're trying to join. Try again later.");
			Log.e(TAG, "exception: ", e1);
			System.exit(0);
		}

		// Set up ArrayAdapter to display chat messages
		chatMessages = (ListView) findViewById(R.id.chatMessages);
		messagesAdapter = new ArrayAdapter<String>(this, R.layout.chatmessage);
		chatMessages.setAdapter(messagesAdapter);

		// Set up ArrayAdapter to show buddies
		buddies = (ListView) findViewById(R.id.buddyList);
		buddiesAdapter = new ArrayAdapter<String>(this, R.layout.buddy);
		buddies.setAdapter(buddiesAdapter);
		buddiesAdapter.add("Buddies:");
		buddiesAdapter.notifyDataSetChanged();

		// Set up ArrayAdapter to show chat members
		members = (ListView) findViewById(R.id.chatMembers);
		membersAdapter = new ArrayAdapter<String>(this, R.layout.chatmember);
		members.setAdapter(membersAdapter);
		membersAdapter.add("Chat members:");
		membersAdapter.notifyDataSetChanged();

		// Welcome user to chat
		messagesAdapter.add("Welcome to the room!");
		messagesAdapter.notifyDataSetChanged();

		// On click of "Send" button
		sendMessage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String message = chatMessage.getText().toString();
				chatMessage.setText("");
				if (message.length() > 0) {
					try {
						chatRoom.sendMessage(message);
					} catch (XMPPException e) {
						Log.v(TAG, "Sending the message didn't work!");
						Log.e(TAG, "exception:", e);
						System.exit(0);
					}
					messagesAdapter.add("Me: " + message);
				}
				messagesAdapter.notifyDataSetChanged();
			}
		});

		// On click of "Invite" button
		inviteUser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String user = chatMessage.getText().toString();
				chatMessage.setText("");
				chatRoom.invite(user, "You're fun!");
			}
		});

		// On click of "Kick" button
		kickUser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String user = chatMessage.getText().toString();
				chatMessage.setText("");
				try {
					chatRoom.banUser(user, "You suck!");
				} catch (XMPPException e) {
					Log.v(TAG, "The ban didn't work!");
					Log.e(TAG, "exception:", e);
					System.exit(0);
				}
			}
		});

		// On click of "Leave Chat" button
		leaveChat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chatRoom.leave();
				messagesAdapter.add("You've left the chat. Now this "
						+ "program doesn't really do anything!");
				messagesAdapter.notifyDataSetChanged();
			}
		});

		// On click of "Logout" button
		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				con.disconnect();
				System.exit(0);
			}
		});

		// On message received
		chatRoom.addMessageListener(new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				if (packet instanceof Message) {
					String sender = StringUtils.parseResource(packet.getFrom());
					Message message = (Message) packet;
					if (!sender.equals("opencommsec")) {
						messagesAdapter.add(sender + ": " + message.getBody());
					}
				}
				messagesAdapter.notifyDataSetChanged();
			}
		});

		chatRoom.addParticipantListener(new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				if (packet instanceof Presence) {
					membersAdapter.clear();
					membersAdapter.add("Chat members:");
					try {
						Collection<Occupant> participants = chatRoom
								.getParticipants();
						for (Occupant occupant : participants) {
							membersAdapter.add(occupant.getJid());
						}
					} catch (XMPPException e) {
						Log.v(TAG, "Couldn't get the participants Collection.");
						Log.e(TAG, "exception: ", e);
						System.exit(0);
					}

				}
				membersAdapter.notifyDataSetChanged();
			}

		});

		getBuddyList();

		// timers to update buddylist and chat members every 30sec
		// timer.scheduleAtFixedRate(new BuddyListTimer(), 0, 30000);
		// timer.scheduleAtFixedRate(new ChatMembersTimer(), 0, 30000);
	}

	/**
	 * 
	 * Creates Connection to the jabber.org server using the username "risan"
	 * and its password.
	 * 
	 * @throws XMPPException
	 */
	private void connect() throws XMPPException {
		con = new XMPPConnection("jabber.org");
		con.connect();
		con.login("opencommsec", "secopencomm");
	}

	/**
	 * 
	 * Creates a multi-user chat with default room settings. Chat name:
	 * XMPPMultiUserDemoAndroidKris Nickname: Kris
	 * 
	 * @throws XMPPException
	 */
	private void createChat() throws XMPPException {
		chatRoom = new MultiUserChat(con,
				"XMPPMultiUserChatDemoAndroidKris@conference.jabber.org");
		chatRoom.join("opencommsec");
		chatRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	}

	/**
	 * 
	 * Reads and prints the user's buddylist.
	 */
	public static void getBuddyList() {
		buddyList = con.getRoster();
		Collection<RosterEntry> entries = buddyList.getEntries();
		for (RosterEntry entry : entries) {
			buddiesAdapter.add(entry.getUser());
			buddiesAdapter.notifyDataSetChanged();
		}
	}
}