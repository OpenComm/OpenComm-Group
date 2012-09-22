package classes;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class RegularChat {
	ChatManager chatManager;

	
	public RegularChat(String userJID, String message){
		Message newMessage= new Message();
		newMessage.setBody(message);
		
		/**process message here*/
		Chat newChat= chatManager.createChat(userJID, new MessageListener() {
			public void processMessage(Chat chat, Message message) {	
			}	
		}
	);
		
		try {
			newChat.sendMessage(message);
		} catch (XMPPException e) {
			System.out.println("message failed");
		}
	}
	
	/**add a chat listener in order to receive and send chat messages*/
	public void chatListener(){}

	
	public void chatConnection(Connection connection){
		chatManager = connection.getXMPPConnection().getChatManager();
		
		
	}
	
	
	
	

}
