import java.util.Iterator;
import java.util.LinkedList;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class Chat {
	public static XMPPConnection connection;
	public static MultiUserChat multiUserChat;
	
	public static void main(String[] args) throws InterruptedException {
		connection = new XMPPConnection("jabber.org");
		try {
			//Login
			connection.connect();
			connection.login("mucopencomm", "opencommmuc");
			System.out.println("Connection Established.");
			
			//Create a new MultiUserChat room
			multiUserChat = new MultiUserChat(connection, "superhappyfunroom@conference.jabber.org");
			multiUserChat.create("bigsmiley");
			multiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			System.out.println("Room created.");
		} catch (XMPPException e) {
			try {
				//Could be that the room already exists!
				multiUserChat.join("bigsmiley");
				System.out.println("Room joined");
			} catch (XMPPException e1) {
				System.out.print("Could not create or join superhappyfunroom. :(");
				System.exit(1);
			}
		}
		
		//To make sure we have joined the chat before the first print
		//There is probably a better way to do this.
		//see: http://www.igniterealtime.org/builds/smack/docs/latest/javadoc/org/jivesoftware/smackx/muc/MultiUserChat.html#getOccupants() 
		Iterator<String> occupants;
		do {
			occupants = multiUserChat.getOccupants();
		} while(!occupants.hasNext());
		
		while(true) {
			System.out.println("pplz");
			while(occupants.hasNext()) {
			    System.out.println(occupants.next());
			}
			System.out.println();
			sleep(30000); //Wait 30 seconds
			occupants = multiUserChat.getOccupants();
		}
	}
	
	/**
	 * Calls Thread.sleep, printing InterruptedExceptions
	 * @param miliseconds
	 */
	public static void sleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
