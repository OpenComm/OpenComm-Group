import java.util.Collection;
import org.jivesoftware.smack.*;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class XMPPMultiUserChatDemo {

	static Connection con;
	static Roster buddyList;
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @param server
	 * 
	 * Creates Connection to server, using username and 
	 * password as login credentials.
	 * 
	 * @throws XMPPException 
	 */
	private static void connect(String username, String password,
			String server) 
			throws XMPPException{
		con = new XMPPConnection(server);
		con.connect();
		con.login(username, password);
	}
	
	/**
	 * 
	 * Creates a multi-user chat with default room settings.
	 * Chat name: XMPPMultiUserDemo_Kris
	 * Nickname: Kris
	 * 
	 * @throws XMPPException 
	 */
	public static void createChat() 
			throws XMPPException{
		MultiUserChat chatRoom = new MultiUserChat(con, 
				"XMPPMultiUserChatDemo_Kris@conference.jabber.org");
		chatRoom.create("Kris");
		chatRoom.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 * 
	 * Reads the user's buddy list and prints the list of available 
	 * buddies.
	 */
	private static void getBuddyList() throws InterruptedException{
		buddyList = con.getRoster();
		Collection<RosterEntry> entries = buddyList.getEntries();
		System.out.println("Buddy List:");
		for (RosterEntry entry : entries) {
				System.out.println(entry.getUser());
			}
		}
	
	/**
	 * @param args - arg[0] is username, arg[1] is password,
	 * arg[2] is the server name
	 * 
	 * Logs into account, starts a multi-user chatroom,
	 * updates and prints buddy list every 30 seconds
	 *  
	 */
	public static void main(String[] args) {
		try {
			connect(args[0], args[1], args[2]);
			createChat();
			while(true){
				getBuddyList();	
				//Wait 30 seconds before printing buddyList again
				Thread.sleep(30000);
			}
		} catch (XMPPException e) {
			System.out.println("There was a problem logging in! " +
					"Please check your server and login information" +
					" and try again.");
			System.exit(0);
		} catch (InterruptedException e) {}

	}

}
