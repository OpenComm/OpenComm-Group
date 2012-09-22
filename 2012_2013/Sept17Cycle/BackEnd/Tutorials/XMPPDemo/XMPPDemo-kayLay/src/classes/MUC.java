package classes;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;


/**This class is going to have basic chatting settings and Multi User Chat Capabilities*/
public class MUC {
	

	MultiUserChat Muc;
	

	
	
	public MUC(Connection connection, String roomName) throws XMPPException{
		Muc= new MultiUserChat(connection.getXMPPConnection(), roomName);
		Muc.create(roomName);
		Muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	}
	
	public void joinRoom(Connection connection, String roomName){
		try {
			Muc.join(roomName);
		} catch (XMPPException e) {
			System.out.println("Failed joining room");
		}
		
	}
	
	
	
	
}
