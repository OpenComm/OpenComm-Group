package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.util.Log;

import java.util.Collection;
import java.util.Iterator;

public class CheckEmail {
   private static String DOMAIN = "cuopencomm.no-ip.org";
   private static int PORT = 5222;
   private static String email;
   private String jid = "0";
   
   public CheckEmail(String email){
		configure();
		CheckEmail.email = email;
	}
		
   private void configure(){
		configure(ProviderManager.getInstance());
		SmackConfiguration.setPacketReplyTimeout(10000);
	}
   
   @SuppressWarnings("unchecked")
public String getJid() {
      try {
    	 System.out.println("getJid method executed");
    	 ConnectionConfiguration config = new ConnectionConfiguration(DOMAIN,PORT);
    	 config.setSASLAuthenticationEnabled(true);
         XMPPConnection con = new XMPPConnection(config);
         con.connect();
         System.out.println("Connection ok");
         con.login("searchuser","search");
         System.out.println("Logged in");
         UserSearchManager search = new UserSearchManager(con);
         System.out.println("User Search created");
         Collection<?> services = search.getSearchServices();
         System.out.println("Search Services found:");
         Iterator<?> it = services.iterator();
         while(it.hasNext()){
        	 System.out.println(it.next());
         }
         Form searchForm = search.getSearchForm("search." + "cuopencomm");
         System.out.println("Available search fields:");
         Iterator<FormField> fields = searchForm.getFields();
         while (fields.hasNext()) {
            FormField field = fields.next();
            System.out.println(field.getVariable() + " : " + field.getType());
         }
         
         Form answerForm = searchForm.createAnswerForm();
         answerForm.setAnswer("search", email);
         answerForm.setAnswer("Email", true);
         
         ReportedData data = search.getSearchResults(answerForm, "search." + "cuopencomm");
         
         System.out.println("\nColumns that are included as part of the search results:");
         Iterator<Column> columns = data.getColumns();
         while (columns.hasNext()) {
            System.out.println(columns.next().getVariable());
         }
         
         System.out.println("\nThe jids and emails from our each of our hits:");
         Iterator<Row> rows = data.getRows();
         while (rows.hasNext()) {
            Row row = rows.next();
            
            Iterator<String> jids = row.getValues("jid");
            Iterator<String> emails = row.getValues("email");
            String jidFound = null;
            String emailFound = null;
            
            while (emails.hasNext() && jids.hasNext()) {
               jidFound = jids.next();
               emailFound = emails.next();
               System.out.println(jidFound);
               System.out.println(emailFound);
               if(emailFound.equalsIgnoreCase(email)){
            	   jid = jidFound;
            	   String[] jidCleaned = jid.split("@");
            	   jid = jidCleaned[0];
            	   System.out.println(jid);
               } 
            } 
         }
         con.disconnect();
         return jid;
      } catch (Exception ex) {
         System.out.println("Caught Exception :"+ex.getMessage());
         return jid;
      }
   }
   
   public void configure(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
		new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
		pm.addIQProvider("query", "jabber:iq:time",
		Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
		Log.w("TestClient",
		"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
		new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
		new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
		"http://jabber.org/protocol/chatstates",
		new ChatStateExtension.Provider());

		pm.addExtensionProvider("composing",
		"http://jabber.org/protocol/chatstates",
		new ChatStateExtension.Provider());

		pm.addExtensionProvider("paused",
		"http://jabber.org/protocol/chatstates",
		new ChatStateExtension.Provider());

		pm.addExtensionProvider("inactive",
		"http://jabber.org/protocol/chatstates",
		new ChatStateExtension.Provider());

		pm.addExtensionProvider("gone",
		"http://jabber.org/protocol/chatstates",
		new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
		new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
		new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
		new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
		new DiscoverInfoProvider());

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

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
		new DelayInformationProvider());

		// Version
		try {
		pm.addIQProvider("query", "jabber:iq:version",
		Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
		// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
		new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
		"http://jabber.org/protocol/offline",
		new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
		"http://www.jivesoftware.org/protocol/sharedgroup",
		new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
		"http://jabber.org/protocol/address",
		new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
		new StreamInitiationProvider());

		// [TODO] : Ask Kris
		// pm.addIQProvider("query","http://jabber.org/protocol/bytestreams",
		// new BytestreamsProvider());
		// pm.addIQProvider("open","http://jabber.org/protocol/ibb", new
		// IBBProviders.Open());
		// pm.addIQProvider("close","http://jabber.org/protocol/ibb", new
		// IBBProviders.Close());
		// pm.addExtensionProvider("data","http://jabber.org/protocol/ibb", new
		// IBBProviders.Data());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
		"http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
		"http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
		"http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
		"http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
		"http://jabber.org/protocol/commands",
		new AdHocCommandDataProvider.SessionExpiredError());
   }
   
   
}