package com.example.xmppandroiddemo;



import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

import android.os.Message;
import android.util.Log;

public class XMPPConnect {

//TAg=XMPP
	//message=it dint connect in log
public static final String TAG="XMPPConnect";
XMPPConnection connection = new XMPPConnection ("jabber.org");
boolean chatroomgenerated=false;
boolean chatroomjoined=true;
MultiUserChat chat=null;
	public XMPPConnect()
	{
		configure(ProviderManager.getInstance());
	}
	
	public void print() {
		Log.d(TAG, "hi");
	}
	
	public void connect() {
		
		
		try{
			
			connection.connect();
			String username="designopencomm@jabber.org";
			String password="opencommdesign";
			connection.login(username,password); 
			Log.d(TAG,"Sucessfully connected");
		}
		catch(XMPPException e){
			Log.d(TAG,"Error in connecting server");
		}
		 
	}
	
	public void multiuserchat(){
		if(chatroomgenerated=false){
		 chat= new MultiUserChat(connection,"neeleshroom4@conference.jabber.org");
		try {
			chat.create("neeleshroom4");
			 chat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			 Log.d(TAG, "Chatroom successfully generated");
			    chatroomgenerated=true;
		} catch (XMPPException e) {
			Log.d(TAG, "Chatroom not successfully generated, trying to join");
			
		}
	   
		
	}
		if(chatroomgenerated==false)
			try {
				chat.join("myroom");
                chatroomjoined=true;
				
			} 
		   catch (XMPPException e1) {
				
			   Log.d(TAG, "Failed to join chatroom");
			}
		//If a chat-room has been generated or joined, then creating a PacketListener
		if(chatroomgenerated||chatroomjoined){
		chat.addMessageListener(new PacketListener() {

				@Override
				public void processPacket(Packet arg0) {
				Log.d(TAG,"Can get the message");// Message displayed when you receive a message
				}
		});
		}
	
		chat.invite("opencommss@jabber.org","Testing");
}



public void message(){
	String msg="Neelesh says hi";
	
	org.jivesoftware.smack.packet.Message message= chat.createMessage();
	message.setBody(msg);
	 try {
		chat.sendMessage(message);
	} catch (XMPPException e) {
		Log.d(TAG,"Error in sending message");
	}
	

}

public void buddylist(){
	Roster roster = connection.getRoster();
	
	Collection<RosterEntry> entries = roster.getEntries();
	ArrayList<RosterEntry> onlineUsersInRoom = new ArrayList<RosterEntry>();
	ArrayList<RosterEntry> offlineUsersInRoom = new ArrayList<RosterEntry>();
	
	for (RosterEntry entry : entries) {
	    
	    if(entry.toString().contains("Buddies")){
	    	String name=entry.getUser();
	    
	    if(roster.getPresence(name).toString().contains("unavailable")){
	    	
	    offlineUsersInRoom.add(entry);
	    	
	    }
	    else{
		    onlineUsersInRoom.add(entry);
		    	
		    }
	    }
	    
	    
	    
		}
	System.out.println("offline users");
	for(int i=0;i< offlineUsersInRoom.size(); i++){
		
		System.out.println(offlineUsersInRoom.get(i));
		}
	
	
	
	}
public void logout(){
	connection.disconnect();
	System.exit(0);
	
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







