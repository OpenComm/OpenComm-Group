package demo.xmpp.muc.desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

/** An instance of this class demonstrates our ability to log in, create a MUC 
 * room, invite other accounts to the room, and to send messages to each other
 * @author Risa Naka (rn96@cornell.edu)
 */
public class MUCDemo {
	public static PrintStream ps = System.out;
	public static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	public static String roomname = "opencomm@conference.jabber.org";
	public static XMPPUser user1 = new XMPPUser("opencommss@jabber.org", "ssopencomm");
	/** Main method: Connects to jabber.org, logs in as opencommss, 
	 * prints out the buddy list every 30 seconds until we destroy the room
	 */
	public static void main(String[] args) {
		ps.println("OpenComm Group Network Team\n" + 
				"MultiUser Chat (MUC) Demonstration :: " +
				df.format(new Date()) + "\n");
		try {
			// Create a new XMPP Connection to host server jabber.org through
			// port 5222 with stream compression and SASL Auth. enabled
			ps.println("Connect to host server jabber.org through port 5222\n" +
					"with stream compression and SASL authentication enabled\n");
			
			XMPPConnectConfig xmppConn =
				new XMPPConnectConfig("jabber.org", 5222, true, true);
			XMPPConnection conn = xmppConn.getXmppConn();
			ps.println("XMPP Connection created:\n" + xmppConn);
			// Connect to the server
			conn.connect();
			ps.println("\tXMPP Connection successfully connected? " + 
					(conn.isConnected() ? "yes" : "no"));
			
			// Log in as OpenCommSS
			ps.println("\n\nLog in as user " + user1.getUsername() + ":");
			conn.login(user1.getUsername(), user1.getPassword());
			ps.println("\tUser " + user1.getUsername() 
					+ " successfully logged in? " 
					+ (conn.isAuthenticated() ? "yes" : "no"));
			
			// Create a MultiUserChat using an XMPPConnection for a room
			ps.println("\nCreate the MultiUserChat room:");
			MultiUserChat muc = new MultiUserChat(conn, roomname);	
			// Create the room and join as password + usernick
			muc.create(user1.getUsername());
			// Send an empty room configuration form to make the room instant
			// (default configuration)
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			ps.println("\tRoom created? " + 
					(muc.getRoom().equals(roomname) ? "yes" : "no"));
			
			ps.println("\tGroup chat created. Chat will end after " +
					"4 30-sec buddy list rotations.");
			int blCnt = 0;
			while ( blCnt < 4 ) {
				try {
					printBuddyList(conn);
					Thread.sleep(30000);
					blCnt++;
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			//Destroy the MUC room
			muc.destroy("Demo ended", "");
			ps.println("MultiUser Chat Room destroyed");
			// Disconnect from the server
			conn.disconnect();
			ps.println("Disconnected? " + (conn.isConnected() ? " no" : " yes"));
			ps.println("Demo concluded. " + df.format(new Date()));
			
		}
		catch (XMPPException e) {
			e.printStackTrace();
		}
	} // end main method
	
	/** print the buddy list of the given xmpp connection
	 * @param conn the xmpp connection whose buddy list we'd like to see
	*/
	public static void printBuddyList(XMPPConnection conn) {
		// get the roster
		Roster buddyList = conn.getRoster();
		Collection<RosterEntry> buddyEntries = buddyList.getEntries();
		
		ps.println("Buddy List at " + df.format(new Date()));
		for (RosterEntry entry : buddyEntries) {
			ps.println(entry.getUser());
		}
	} // end printBuddyList method
}
