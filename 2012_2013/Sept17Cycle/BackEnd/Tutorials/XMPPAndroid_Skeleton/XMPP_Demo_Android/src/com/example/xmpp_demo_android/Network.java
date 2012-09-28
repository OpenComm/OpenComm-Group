package com.example.xmpp_demo_android;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;

/** TODO: Fill in the methods below with whatever code you need them to do.
 * Add parameters to the function headers as needed. You code should be
 * triggered when you click on the UI elements, but its output should all go
 * to logcat. If you have any questions, please email Kris ASAP.
 * 
 * @author Kris
 *
 */
public class Network {

	public static final String TAG = "Network";
	
	// Remember: getters and setters are expensive in Android. Use package 
    // variables whenever possible. Don't understand what that means? Google 
	// it, then ask Kris if it isn't clear.
	XMPPConnection conn;
	Roster buddyList;
	MultiUserChat chatRoom;
	 
	public Network() {
		configure(ProviderManager.getInstance());
	}
	
	public void smackLogin() {
		// Add parameters and code as needed
	}
	
	public void joinMUC() {
		// Add parameters and code as needed
		/* NB: register the message listener with the MUC in this method
		Do not make a different class! Only global listeners need to go in
		a second class. This listener only applies to this MultiUserChat, so
		it gets registered in the same method where the MUC is created. */
	}
	
	public void inviteUser() {
		// Add parameters and code as needed
	}
	
	public void sendMessage() {
		// Add parameters and code as needed
	}
	
	public void printBuddyList() {
		// Add parameters and code as needed
	}
	
	public void quit() {
		// Add parameters and code as needed
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
