package xmpp.muc;

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
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
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
    // USERS: user1 will be signed in through this demo; user2 and 3 should be
    // signed in through Pidgin
	public static XMPPUser user1 = new XMPPUser("opencommss@jabber.org", "ssopencomm");
	public static XMPPUser user2 = new XMPPUser("risan@jabber.org", "reesaspbc176");
	public static XMPPUser user3 = new XMPPUser("mucopencomm@jabber.org", "opencommmuc");
    public static int userCount = 3; // change as needed
	public static String reasonInvite = "OpenComm MUC Demo: Invite";
	/** Main method
	 */
	public static void main(String[] args) {
		ps.println("OpenComm Group Network Team\n" + 
				"MultiUser Chat (MUC) Demonstration :: " +
				df.format(new Date()) + "\n");
		try {
			// Create a new XMPP Connection to host server jabber.org through
			// port 5222 with stream compression and SASL Auth. enabled
			XMPPConnectConfig xmppConn =
				new XMPPConnectConfig("jabber.org", 5222, true, true);
			XMPPConnection conn = xmppConn.getXmppConn();
			ps.println("XMPP Connection created:\n" + xmppConn);
			// Connect to the server
			conn.connect();
			ps.println("\tXMPP Connection successfully connected? " + 
					(conn.isConnected() ? "yes" : "no"));
			
			// Log in as OpenCommSS
			ps.println("\nLog in as user " + user1.getUsername() + ":");
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
			
			// Invite users to the room
			ps.println("\nInvite users to the room:");
			muc.invite(user2.getUsername(), reasonInvite);
			muc.invite(user3.getUsername(), reasonInvite);
			ps.println("\t" + (userCount - 1) + " users invited");
            
			// Listen for Invitations and reject them
			MultiUserChat.addInvitationListener(conn, new InvitationListener() {
				public void invitationReceived(XMPPConnection conn,
						String room, String inviter, String reason,
						String password, Message message) {
						// Reject the invitation
						MultiUserChat.decline(conn, room, inviter, reason);
				}
		      });
			
			// Listen for MUC invitation rejections
			muc.addInvitationRejectionListener(
				new InvitationRejectionListener() {
					public void invitationDeclined(String invitee, String reason) {
						System.out.println("Invitation declined: " +
							invitee + "; Reason: " + reason);
					}
				}
			);
			while (muc.getOccupantsCount() < userCount) {
			}			
			ps.println("\tAll users joined the room? " 
					+ (muc.getOccupantsCount() == userCount ? "yes" : "no"));
			Iterator<String> occ = muc.getOccupants();
			String[] users = new String[muc.getOccupantsCount()];
			Chat[] chats = new Chat[users.length - 1];
			String userLog = "\tUsers:";
			int i = 0;
			while (occ.hasNext()) {
				String temp = occ.next();
				users[i] = temp;
				userLog += "\n\t\t" + StringUtils.parseResource(temp);
				i++;
			}
			ps.println(userLog);
			
			// Chat with each other
			ps.println("\nChat with each other:");		
			muc.addMessageListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					String from = StringUtils.parseResource(message.getFrom());
					ps.println("[" + df.format(new Date()) + "]" +
							from + ": " + message.getBody());
			    }
			});
			ps.println("\tGroup chat created. Type \"bye\" to end chat.");
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String istemp = "";
			while (!istemp.equals("bye")) {
				try {
					istemp = br.readLine();
					muc.sendMessage(istemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
}
