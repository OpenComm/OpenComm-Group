package com.example.xmpp_demo_android;

import java.util.ArrayList;
import java.util.Collection;

<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
import android.util.Log;

=======
import org.jivesoftware.smack.ConnectionConfiguration;
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
import org.jivesoftware.smack.packet.Message;
=======
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

//import org.jivesoftware.smack.XMPPConnection;

/**
 * TODO: Fill in the methods below with whatever code you need them to do. Add
 * parameters to the function headers as needed. You code should be triggered
 * when you click on the UI elements, but its output should all go to logcat. If
 * you have any questions, please email Kris ASAP.
 * 
 * @author Kris
 * 
 */
public class Network {

	public static final String TAG = "Network";
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	XMPPConnection connection = null;
	ArrayList<String> buddies = new ArrayList<String>();
	ArrayList<RosterEntry> onlineBuddies= new ArrayList<RosterEntry>();
	ArrayList<RosterEntry> offlineBuddies= new ArrayList<RosterEntry>();

	// Remember: getters and setters are expensive in Android. Use package
	// variables whenever possible. Don't understand what that means? Google
=======
	
	private static XMPPConnection connection;
	private static MultiUserChat muc;
	
	// Remember: getters and setters are expensive in Android. Use package 
    // variables whenever possible. Don't understand what that means? Google 
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	// it, then ask Kris if it isn't clear.

	XMPPConnection conn;
	Roster buddyList;
	MultiUserChat chatRoom;
	Roster roster;

	public Network() {
		configure(ProviderManager.getInstance());
	}

	public void smackLogin() {
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		connection = new XMPPConnection("jabber.org");
		try {
			connection.connect();
			connection.login("mucopencomm", "opencommmuc");

		} catch (XMPPException e) {
			// Log.v("Network", e.printStackTrace());
			// PRINT OUT ERROR HERE
			Log.v(TAG,
					"ERROR with connection and/or login in the smackLogin method");

		}

		if ((connection.isAuthenticated() && connection.isConnected()) == true) {
			chatRoom = null;
		} else {
			smackLogin();
=======
		ConnectionConfiguration config = new ConnectionConfiguration("jabber.org", 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);

		connection = new XMPPConnection(config);
		// Connect to the server
		// Log into the server
		try {
			connection.connect();
			if (connection.isConnected()) {
				connection.login("opencommss@jabber.org", "ssopencomm", null);
			}
		} catch (XMPPException e) {
			e.printStackTrace();
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		}
	}

	public void joinMUC() {
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		boolean isCreated = false;

		if (!isCreated) {
			chatRoom = new MultiUserChat(connection,
					"leileileiBLOOP@conference.jabber.org");
			try {
				chatRoom.create("LEIXMPPANDROID");
				chatRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				isCreated = true;
			} catch (XMPPException e) {
				Log.v(TAG,
						"Problem in the joinMUC method with making a new room");

				// e.printStackTrace();
			}

			if (isCreated == true) {
				chatRoom.addMessageListener(new PacketListener() {
					public void processPacket(Packet packet) {

						// HERE IS WHERE YOU CAN RECEIVE MESSAGES!
						String XML = packet.toXML();
						int boundaryOne = XML.indexOf("<body>");
						int boundaryTwo = XML.indexOf("</body>");

						String message = XML.substring(boundaryOne + 6,
								boundaryTwo);

						Log.v(TAG, message);

					}
				});

				if (isCreated == false) {
					try {
						chatRoom.join("LEIXMPPANDROID");

					} catch (XMPPException e) {
						Log.v(TAG,
								"problem in the joinMUC method with joining the room");
						// e.printStackTrace();
					}

				}

			}

		}

=======
		muc = new MultiUserChat(connection, "fdafdsfda@conference.jabber.org");
		try {
			muc.create("user");
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			System.out.println(e.getMessage());
			try {
				muc.join("user");
			} catch (XMPPException e1) {
				System.out.println(e1.getMessage());
			}
		}
		/* NB: register the message listener with the MUC in this method
		Do not make a different class! Only global listeners need to go in
		a second class. This listener only applies to this MultiUserChat, so
		it gets registered in the same method where the MUC is created. */
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	}

	public void inviteUser() {
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		chatRoom.invite("risan@jabber.org", "enjoy as casual chat");
		buddies.add("risan@jabber.org");
		Log.v(TAG,
				"you have successfully invited a user AND added them to your buddy list!");

=======
		if (muc.isJoined())
			muc.invite("designopencomm@jabber.org", "Let's talk");
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	}

	public void sendMessage() {
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		String message = "What's Up bro!";
		Message sendThis = chatRoom.createMessage();
		sendThis.setBody(message);

		try {
			chatRoom.sendMessage(sendThis);
		} catch (XMPPException e) {
			Log.v(TAG, "message wasn't sent man ://");
			// e.printStackTrace();
		}

=======
		try {
			muc.sendMessage("Hello!");
			

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	}

	public void printBuddyList() {
<<<<<<< HEAD:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid_Skeleton/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
		roster = connection.getRoster();
		Collection <RosterEntry> allEntries= roster.getEntries();
		
		for(RosterEntry userObject: allEntries){
			
			//offline
			boolean offline= roster.getPresence(userObject.getUser()).toString().contains("unavailabe");
			boolean online = roster.getPresence(userObject.getUser()).toString().contains("available");
			
			if(offline){
				offlineBuddies.add(userObject);
			}
			
			//online
			else if(online){
				onlineBuddies.add(userObject);
				
			
			}

		}
		
		print();//prints the buddy list now

	}
	
	public void print(){
		Log.v(TAG, "Offline:");
		
		for(int i=0; i<offlineBuddies.size(); i++){
			Log.v(TAG, offlineBuddies.get(i).toString());
			
		}
		
		Log.v(TAG, "Online:");
		
		for(int i=0; i<onlineBuddies.size(); i++){
			Log.v(TAG, onlineBuddies.get(i).toString());
			
		}		
=======
		ArrayList<String> offline = new ArrayList<String>();
		ArrayList<String> online = new ArrayList<String>();
		ArrayList<String> onlineAndInMUC = new ArrayList<String>();
		Roster roster = connection.getRoster();
		Collection<RosterEntry> buddies = roster.getEntries();
		for (RosterEntry buddy : buddies)
		{
			try {
				System.out.println(muc.getParticipants().contains(buddy));
				if (muc.getParticipants().contains(buddy))
					onlineAndInMUC.add(buddy.getUser());
				else if (roster.getPresence(buddy.getUser()).isAvailable())
					online.add(buddy.getUser());
				else
					offline.add(buddy.getUser());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Online and in MUC:");
		for (String b : onlineAndInMUC)
			System.out.println(" - " + b);
		System.out.println("Online:");
		for (String b : online)
			System.out.println(" - " + b);
		System.out.println("Offline:");
		for (String b : offline)
			System.out.println(" - " + b);
	}
	
	public void quit() {
		connection.disconnect();
>>>>>>> XMPP Demo:2012_2013/Sept17Cycle/BackEnd/Tutorials/XMPPAndroid/XMPP_Android_Brian/XMPP_Demo_Android/src/com/example/xmpp_demo_android/Network.java
	}

	

	public void quit() {
		connection.disconnect();
		Log.v(TAG, "you have disconnected");
	}

	/** A hack to get asmack to work with MUCs. DO NOT EDIT */
	public void configure(ProviderManager pm) {

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

	}
}
