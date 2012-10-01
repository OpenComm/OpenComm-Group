package classes;

import java.util.Scanner;
import org.jivesoftware.smack.*;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.muc.*;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class MucDriver {
	Connection connectionMain;
	final String roomName="LEILEI1123124123212ROOM";
	final String host="jabber.org";
	final String otherUser="risan@jabber.org";
	
	public MucDriver(){
		connectionMain=new Connection(host);
	}
	
	public void createConnection(){

		

		try {
			connectionMain.getXMPPConnection().connect();
			connectionMain.connectMe("mucopencomm", "opencommmuc");
			System.out.println(connectionMain.getXMPPConnection().isAuthenticated());
			System.out.println(connectionMain.getXMPPConnection().isConnected());
				multiUserChat();
			
	
		
		} catch (XMPPException e) {
			System.out.println("hey");
			e.printStackTrace();

		}
		
	}
	
	public void multiUserChat(){
		Scanner scanner= new Scanner(System.in);
		String message;
		
		if(connectionMain.getXMPPConnection().isAuthenticated()==true){
		MultiUserChat MUC= new MultiUserChat(connectionMain.getXMPPConnection(), roomName + "@conference.jabber.org");
		MUC helperMethods= new MUC(connectionMain);
		try {
			MUC.create(roomName);
			
			System.out.println(MUC.isJoined());

			while(MUC.isJoined()==true){
				MUC.invite(otherUser, "hi");
			}
			
			MUC.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			
			  MUC.addInvitationRejectionListener(new InvitationRejectionListener() {
		          public void invitationDeclined(String invitee, String reason) {
		          }
		      });
			
		//	
			//MUC.join(otherUser);
			message=scanner.nextLine();
			MUC.invite(message, "join me");
			//helperMethods.listenForMessage();
			//MUC.sendMessage("hey");
			
		} catch (XMPPException e) {
			System.out.println("error in multiUSerChat in driver");
			e.printStackTrace();
		}
		
		//MUC muc= new MUC(connectionMain);
		//muc.createRoom(roomName);
		//muc.invite("opencommss@jabber.org");
			}
		

		//muc.invite("opencommss@jabber.org");
		/**
		String nickname="bloop!";
		String roomname= "lei's room!";
		MUC muc= new MUC(connectionMain, roomname + "@conference.jabber.org", nickname);
		muc.listenForMessage(); //return you a string of the messages
				muc.invite("opencommss@jabber.org");
				muc.sendMessage("hey");
	
	*/
	}
}
