package smack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Test the connections to jabber.org via the smack and smackx.jar files
 * Create a connection to jabber, get buddy-list, create MUC, invite
 * buddies to the MUC, and send + receive messages in the MUC
 * @author jnfrye
 *
 */
public class SmackMUCExample {
	
	private static final String SERVERNAME = "jabber.org";
	private static final String USERNAME = "opencomm311";
	private static XMPPConnection con;
	private static BufferedReader in = null;
	private static ConcurrentHashMap<String,RosterEntry> buddyList = new ConcurrentHashMap<String,RosterEntry>();
	
	public static void main(String[] args){
		// connect to jabber
		con = new XMPPConnection(SERVERNAME);
		try {
			con.connect();
		} catch (XMPPException e) {
			handleError("Failed to connect through XMPPConnection",e,true);
		}

		// login
		try {
			con.login(USERNAME, "guest1");
		} catch (XMPPException e) {
			handleError("Failed to login",e,true);
		}
		
		// set our status
		Presence pres = new Presence(Presence.Type.available);
		pres.setStatus("Ready to make text.");
		
		// used when a packet is received
		PacketFilter filterMsgs = new PacketTypeFilter(Message.class);
		PacketListener printMsgListener = new PacketListener(){
			@Override
			public void processPacket(Packet p) {
				// print the body of a message and who sent it
				if (p instanceof Message){
					Message m = (Message)p;
					System.out.println(m.getFrom() + ": " + m.getBody());
				}
			}
		};
		
		// set packet listener (tells what to do when receive a packet
		con.addPacketListener(printMsgListener, filterMsgs);
		con.sendPacket(pres);

		// get list of contacts
		createBuddyList();
		
		in = new BufferedReader(new InputStreamReader(System.in));		
		//create a new MUC
		MultiUserChat chat = createMultiUserChat("XchatX@conference.jabber.org");
		chat.addMessageListener(printMsgListener);
		
		sendMessagesToChat(chat);
		
		cleanConnections();		
	}

	
	/**
	 * Create a new MUC room using the connection con, and allow user to invite
	 * people from their buddy list through cmd line
	 * @return
	 */
	private static MultiUserChat createMultiUserChat(String roomname) {
		String user = "";
		System.out.println("Who do you want to invite?(end to quit invites)");
		MultiUserChat muc = new MultiUserChat(con,roomname);
		
		try {
			muc.create("testMUC");
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		} catch (XMPPException e) {
			SmackMUCExample.handleError("Failed to create/join chatroom", e, true);
		}
		
		while (true){
			System.out.print(">>");
			try {
				user = in.readLine();
				if (user.equalsIgnoreCase("end")) 
					break;
				else{
					String u = buddyList.get(user).getUser();
					muc.invite(u, "You know you want to....");
				}
			} catch (IOException e1) {
				handleError("Couldn't read from System.in",e1,true);
			}
		}
		
		return muc;
	}

	/**
	 * Loop sending all typed cmd line messages to the chatroom
	 * Ends when the user types end
	 * @param chat
	 */
	private static void sendMessagesToChat(MultiUserChat chat) {
		String txt = "";
		System.out.println("Type end to exit");
		while(!txt.equalsIgnoreCase("end")){
			try {
				System.out.print(">>");
				txt = in.readLine();
				chat.sendMessage(txt);
			} catch (IOException e) {
				handleError("Couldn't read from System.in",e,true);
			} catch (XMPPException e) {
				handleError("Problem sending message ",e,false);
			}
		}
	}

	/**
	 * Get all the contacts saved in the user's buddy list and put them
	 * in a hashmap, so we can refer to the person later by their name
	 */
	private static void createBuddyList() {
		Roster ros = con.getRoster();
		
		for(RosterEntry entry : ros.getEntries()){
			String name = entry.getName();
			if (name == null){
				name = entry.getUser();
			}
			System.out.println(name);
			buddyList.put(name, entry);
		}
	}
	
	/**
	 * Close all connections, and end the program properly
	 */
	private static void cleanConnections(){
		if (con != null && con.isConnected()){
			con.disconnect();
		}
		try{
			if (in != null) in.close();
		}catch(IOException io){}
	}
	
	/**
	 * Prints an error message and a stack trace.  
	 * Can also state that the program should be terminated because of the error
	 * @param moreInfo - message to print before stacktrace
	 * @param e - exception to print
	 * @param fatal - true to end program
	 */
	private static void handleError(String moreInfo, Exception e, boolean fatal){
		System.err.println(moreInfo);
		e.printStackTrace();
		if (fatal){
			cleanConnections();
			System.exit(-1);
		}
	}
}
