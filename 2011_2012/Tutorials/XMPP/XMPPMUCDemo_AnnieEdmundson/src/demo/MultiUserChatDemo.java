package demo;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class MultiUserChatDemo {

	private static Connection _connection;
//	private static Roster _roster;
	
	public MultiUserChatDemo(){
		System.out.println("THIS IS A DEMO");
	}
	
	public static void main(String[] args) throws InterruptedException{
		new MultiUserChatDemo();
		try{
			_connection = new XMPPConnection("jabber.org");
			_connection.connect();
			_connection.login("opencommsec", "secopencomm");
			System.out.println("Connection created.");
		}
		catch(XMPPException e){
			System.out.println("Connection could not be created.");
		}
//		
//		try{
//			MultiUserChat muc = new MultiUserChat(_connection, "MUC_Demo@conference.jabber.org");
//			muc.create("Demo");
//			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
//			System.out.println("Chat room created.");
//		}
//		catch(XMPPException e2){
//			System.out.println(e2);
//			System.out.println("Chat room could not be created.");
//		}
//		
//		try{
//			while(true){
//				_roster = _connection.getRoster();
//				Collection<RosterEntry> friends = _roster.getEntries();
//				System.out.println("Roster: ");
//				for (RosterEntry friend : friends){
//					System.out.println(friend.getUser());
//				}
//				Thread.sleep(30000);
//			}
//		}
//		catch (Exception e4){
//			System.out.println("The roster could not be created or printed.");
//		}
	}
}
