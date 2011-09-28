import java.util.*;
import java.io.*;
 
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.RoomInfo;

public class MucAPI {
	
    XMPPConnection connection;
    MultiUserChat muc;
    
    public void login(String userName, String password) throws XMPPException
    {
    	// Create a connection to the jabber.org server on a specific port
    	ConnectionConfiguration config = new ConnectionConfiguration("jabber.org", 5222);
    	config.setCompressionEnabled(true);
    	config.setSASLAuthenticationEnabled(true);

    	connection = new XMPPConnection(config);    	
 
    	connection.connect();
    	connection.login(userName, password);
    }
 
    public void sendMessage(String message) throws XMPPException
    {
    	muc.sendMessage(message);
    }
    
    public void createMultiUserChatRoom(XMPPConnection conn, String room, String roomName) throws XMPPException
    {
        muc = new MultiUserChat(conn, room);

        // Create the room
        muc.create(roomName);
        
        // Send an empty room configuration form which indicates that we want an instant room
        muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));    	
    }
 
    public void displayBuddyList()
    {
    	Roster roster = connection.getRoster();
    	Collection<RosterEntry> entries = roster.getEntries();
 
    	System.out.println("\n\n" + entries.size() + " buddy(ies):");
    	for(RosterEntry entry : entries)
    	{
    		System.out.println(entry.getUser());
    	}
    }
    
    public void inviteUser(String userName, String reception)
    {
        muc.invite(userName, reception);
    }
 
    public void disconnect()
    {
    	connection.disconnect();
    }
	
    public static void main(String args[]) throws XMPPException, IOException
    {
    	// declare variables
    	MucAPI c = new MucAPI();
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String msg;
 
    	// turn on the enhanced debugger
    	XMPPConnection.DEBUG_ENABLED = true;
 
    	// Enter your login information here
    	c.login("hw@jabber.org", "hanwei");
    	
    	// Create a MultiUserChat using an XMPPConnection for a room
    	c.createMultiUserChatRoom(c.connection, "hanwei@conference.jabber.org", "hanwei");
    	
    	c.displayBuddyList();
   	 
    	System.out.println("-----");
 
    	System.out.println("Who do you want to talk to? - Type contacts full email address:");
    	String talkTo = br.readLine();    	
    	
    	// invite other user to join to the room
    	c.inviteUser(talkTo, "Meet me in this excellent room");
 
    	System.out.println("-----");
    	System.out.println("All messages will be sent to " + talkTo);
    	System.out.println("Enter your message in the console:");
    	System.out.println("-----\n");
    	
    	c.muc.addMessageListener(
    			new PacketListener() {
    					public void processPacket(Packet packet) {
    						Message message = (Message) packet;
    						String from = StringUtils.parseResource(message.getFrom());
    						System.out.println(from + ": " + message.getBody());
    				}
    			}
    		);    	
    	
    	while( !(msg=br.readLine()).equals("bye"))
    	{
    		c.sendMessage(msg);
    	}
 
        // Disconnect from the server    	
    	c.disconnect();
    	System.exit(0);    	
    }
}
