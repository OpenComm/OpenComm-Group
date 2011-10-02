import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Chat {
	public final String HOST = "jabber.org";
	public static XMPPConnection connection;
	public static MultiUserChat multiUserChat;
	
	/**
	 * Get the occupants of the chat room
	 * @return - Collection of occupant names
	 */
	public Collection<String> getOccupants() {
		if(multiUserChat == null) {
    		throw new RuntimeException("printOccupants: No Chat is Open");
    	}
		//It's annoying that one function returns a Collection and another an Iterator,
		//So I make them both Collections to standardize the interface
		Iterator<String> occupants = multiUserChat.getOccupants();
		Collection<String> result = new HashSet<String>();
		while(occupants.hasNext()) {
			result.add(occupants.next());
		}
		return result;
    }
	
	/**
	 * Gets the people in the buddy list
	 * @return
	 */
    public Collection<RosterEntry> getBuddyList() {
    	if(connection == null) {
    		throw new RuntimeException("getBuddyList: No connection is established");
    	}
    	return connection.getRoster().getEntries();
    }
	
	/**
	 * 
	 * @param username
	 * @throws XMPPException
	 */
	public void addParticipant(String username) throws XMPPException {
		if(multiUserChat == null) {
    		throw new RuntimeException("addParticipant: No Chat is Open");
    	}
		multiUserChat.grantVoice(username);
	}
	
	/**
	 * 
	 * @param username
	 * @throws XMPPException
	 */
	public void deleteParticipant(String username) throws XMPPException {
		if(multiUserChat == null) {
    		throw new RuntimeException("deleteParticipant: No Chat is Open");
    	}
		multiUserChat.revokeVoice(username);
	}
    
    /**)
     * Connects to jabber.org
     * @param username - username to connect with
     * @param password - password associated with username
     * @throws XMPPException
     */
    public void connect(String username, String password) throws XMPPException {
    	if(connection != null) {
    		disconnect();
    	}
    	connection = new XMPPConnection(HOST);
		connection.connect();
		connection.login(username, password);
    }
    
    /**
     * Creates or joins a MultiUserChat Room
     * @param roomName - Name of the room to create
     * @param joinName - Name to join as
     * @throws XMPPException
     */
    public void createChat(String roomName, String joinName) throws XMPPException {
    	if(connection == null) {
    		throw new RuntimeException("createChat: No connection is established");
    	}
		multiUserChat = new MultiUserChat(connection, roomName + "@conference." + HOST);
		
		try {
			multiUserChat.create(joinName);
			multiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			System.out.println("Room Created");
		} catch (XMPPException e) {
			try {
				//Could be that the room already exists!
				multiUserChat.join(joinName);
				System.out.println("Room joined");
			} catch (XMPPException e1) {
				System.out.print("Could not create or join superhappyfunroom. :(");
				System.exit(1);
			}
		}
    }
    
    /**
     * Leaves the chat room
     */
    public void leave() {
    	multiUserChat.leave();
    }
    
    /**
     * Kill the connection
     */
    public void disconnect() {
    	connection.disconnect();
    }
    
    /**
     * Send a message to the chat room
     * @param text - Text of the message
     * @throws XMPPException
     */
    public void sendMessage(String text) throws XMPPException {
    	if(multiUserChat == null) {
    		throw new RuntimeException("sendMessage: No Chat is Open");
    	}
    	Message message = multiUserChat.createMessage();
    	message.setBody(text);
    	multiUserChat.sendMessage(message);
    }
}
