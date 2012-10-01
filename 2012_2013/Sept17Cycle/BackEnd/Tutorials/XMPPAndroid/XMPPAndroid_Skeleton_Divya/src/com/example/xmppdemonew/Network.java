package com.example.xmppdemonew;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

public class Network {
	boolean isGenerated=false;
	boolean chatroomjoin=false;
	String s="Network";
	String t="the user cannot connect";
	static ArrayList<String> inroombuddy= new ArrayList<String>();
	XMPPConnection connection;

	public Network() {
		configure(ProviderManager.getInstance());
	}

	public void printlog(){
		Log.v("Networks","the user cannot connect");
	}

	public void smackLogin() {
		// Add parameters and code as needed
		try{
			connection=new XMPPConnection("jabber.org");
			connection.connect();
			Log.v("Networks","is it connection null?" + (connection == null));
			connection.login("designopencomm@jabber.org", "opencommdesign");
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			Log.v("Networks","It is not getting connected");
		}
	}

	MultiUserChat muc=null;
	public void joinMUC() {
		if(!isGenerated){
			try{
				muc=new MultiUserChat(connection,"Firstchatroomdivya2@conference.jabber.org");
				muc.create("Firstchatroomdivya2");
				muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				isGenerated=true;
			}
			catch(XMPPException e){
				Log.v("Networks","the multi chat did not connect");
			}
		}

		if(isGenerated||chatroomjoin){
			muc.addMessageListener(new PacketListener() {
				@Override
				public void processPacket(Packet arg0) {
					Log.v("Networks","the message listener has an issue");
				}
			});
		}
		if(isGenerated==false)
			try {
				muc.join("mynewfirstchat");
				chatroomjoin=true;
			} 
		catch (XMPPException e1) {
			System.out.println("Failed to join room");
		}
	}
	public void inviteUser() {
		muc.invite("opencommss@jabber.org","testing");
		Log.v("Networks","the invitation works!!");
		inroombuddy.add("ssopencomm@jabber.org");
	}

	public void sendMessage() {
		try {
			String msg="This is finally working";
			Message message=muc.createMessage();
			message.setBody(msg);
			muc.sendMessage(msg);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			Log.v("Networks","the message does not work");
		}
	}

	public void printBuddyList() {
		Roster roster = connection.getRoster();
		Collection <RosterEntry> entries=roster.getEntries();
		ArrayList<RosterEntry> onlineUserInRoom=new ArrayList<RosterEntry>();
		ArrayList<RosterEntry> offlineUserInRoom=new ArrayList<RosterEntry>();

		for (RosterEntry entry : entries){
			String name=entry.getUser();
			if(roster.getPresence(name).toString().contains("unavailable")){
				offlineUserInRoom.add(entry);
			}
			if (roster.getPresence(name).toString().contains("available")){
				onlineUserInRoom.add(entry);
			}
		}
		System.out.println("online User");
		for(int i=0;i<onlineUserInRoom.size(); i++){
			Log.v("Networks",onlineUserInRoom.get(i).toString());
		}
		System.out.println("Offline User");
		for (int i=0; i<offlineUserInRoom.size(); i++){
			Log.v("Networks",offlineUserInRoom.get(i).toString());
		}
		if (inroombuddy.get(0)==null)
			Log.v("Networks","there is no buddy in your list");

		else{ 
			for (int i=0; i<inroombuddy.size(); i++)    //if invited the user is expected to 
				//accept your invitation and thus be in your room
				System.out.println(inroombuddy.get(i)+" is in you room and online");
		}
	}

	public void quit() {
		Log.v("Networks","thanks for using the demo");
		connection.disconnect();

	}
	/** A hack to get asmack to work with MUCs. DO NOT EDIT */
	public void configure(ProviderManager pm) {
        
        //  Roster Exchange
        pm.addExtensionProvider("x","jabber:x:roster", 
        		new RosterExchangeProvider());
 
        //  Message Events
        pm.addExtensionProvider("x","jabber:x:event", 
        		new MessageEventProvider());
 
        //  Group Chat Invitations
        pm.addExtensionProvider("x","jabber:x:conference", 
        		new GroupChatInvitation.Provider());
 
        //  Data Forms
        pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());
 
        //  MUC User
        pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user", 
        		new MUCUserProvider());
 
        //  MUC Admin    
        pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", 
        		new MUCAdminProvider());
 
        //  MUC Owner    
        pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", 
        		new MUCOwnerProvider());

    }
}



