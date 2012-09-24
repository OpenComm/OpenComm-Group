package classes;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;


/**This class is going to have basic chatting settings and Multi User Chat Capabilities*/
public class MUC{
	

	MultiUserChat Muc;
	

	
	
	public MUC(Connection connection, String roomName, String nickName){
		
		try {
			Muc= new MultiUserChat(connection.getXMPPConnection(), roomName);
			Muc.create(nickName);
			Muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));


		} catch (XMPPException e) {
			joinRoom(connection, roomName);
			System.out.println("MultiUserConnection doesn't work");
			e.printStackTrace();
		}

		

	}
	
	public void joinRoom(Connection connection, String roomName){
		try {
			Muc.join(roomName);
		} catch (XMPPException e) {
			System.out.println("Failed joining room");
		}
		
	}

	public void sendMessage(String message){

			try {
				Muc.sendMessage(message);
				
			} catch (XMPPException e) {
				System.out.println("error in the send message method body");
				e.printStackTrace();
			}

		
	}
	
	public void invite(String username){
		Muc.invite(username, "Lei's room");
	}
	
	public MultiUserChat getMUC(){
		return Muc;
	}
	
	public void listenForMessage(){
		Muc.addMessageListener(new PacketListener(){
			public void processPacket(Packet packet) {
				
				
				String XML=packet.toXML();
				int boundaryOne=XML.indexOf("<body>");
				int boundaryTwo=XML.indexOf("</body>");
				
				String message=XML.substring(boundaryOne+6, boundaryTwo);
				
				System.out.println(message);
			}
		});
	}
	
	public void getMessages(){
		try{
		  System.out.println("INTERFACE:" + Muc.nextMessage(1).getBody());
		}
		catch(NullPointerException npe){
			
		}
		
	}
	
	
	
	
}
