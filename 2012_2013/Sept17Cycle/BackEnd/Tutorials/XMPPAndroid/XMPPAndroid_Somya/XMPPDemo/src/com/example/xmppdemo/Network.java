package com.example.xmppdemo;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smack.XMPPConnection;

import android.util.Log;

public class Network {
	boolean isGenerated = false;
	MultiUserChat muc= null;
	XMPPConnection connection=null;	
	ArrayList<String> inroombuddy = new ArrayList<String>();
	
	public void Login(){
		try{
			XMPPConnection connection = new XMPPConnection("jabber.org");
			connection.connect();
			connection.login("designopencomm@jabber.org","opencommdesign");
		}
		catch(XMPPException e){
			Log.v("Networks", "Error in connecting to server");
		}

	}

	public void joinMUC(){

		if(!isGenerated){
			muc = new MultiUserChat(connection, "friendSam@conference.jabber.org");


			try{
				muc.create("friendSam");
				muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				isGenerated= true;

			}


			catch (XMPPException e1){
				try {
					muc.join("friendSam");
				} catch (XMPPException e) {

					Log.v("Networks","Error in joining");
				}
			}

			muc.addMessageListener (new PacketListener(){
				public void processPacket(Packet args){
					Log.v("Networks","Messages are recieved");
				}
			});
		}


		muc.addInvitationRejectionListener(new InvitationRejectionListener(){

			public void invitationDeclined(String arg0,
					String arg1) {
				Log.v("Networks","Invitation declined!!");
			}
		});
	}    
	public void inviteUser(){
		muc.invite("ssopencomm@jabber.org", "Testing");
		Log.v("Networks","Your invitation was recieved!!!");
		inroombuddy.add("ssopencomm");
	}

	public void sendMessage(){
		try{
			String msg= "This is XMPP demo";
			Message message= muc.createMessage();
			message.setBody(msg);
			muc.sendMessage(msg);
			Log.v("Networks","Sent a message This is an XMPP Demo");
		}

		catch (XMPPException e){
			Log.v("Networks","error in sending");
		}

	}

	public void printBuddyList(){
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		ArrayList<RosterEntry> onlineusers = new ArrayList<RosterEntry>();
		ArrayList<RosterEntry> offlineusers = new ArrayList<RosterEntry>();


		for (RosterEntry entry : entries) {



			if (entry.toString().contains("Buddies")){
				String name = entry.getUser();
				if (roster.getPresence(name).toString().contains("unavailable"))
					offlineusers.add(entry);
				else
					onlineusers.add(entry);
			}
		}

		for (int i=0; i < onlineusers.size(); i++)
			Log.v("Networks",onlineusers.get(i).toString()+"is online");

		for (int i=0; i < offlineusers.size(); i++)
			Log.v("Networks",offlineusers.get(i).toString()+ "is offline");

		if (inroombuddy.size()==0)
			Log.v("Networks","There is no one in your room currently");

		else{	
			for (int i=0; i<inroombuddy.size(); i++)    //if invited the user is expected to 
				//accept your invitation and thus be in your room
				Log.v("Networks",inroombuddy.get(i)+" is in you room and online");
		}


	}
	public void quit(){
		Log.v("Networks","Thank You for using XMPP");
		connection.disconnect();
	}





}
