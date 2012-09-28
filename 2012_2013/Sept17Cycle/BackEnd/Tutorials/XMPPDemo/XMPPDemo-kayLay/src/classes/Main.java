package classes;

import java.util.Scanner;

import org.jivesoftware.smack.XMPPException;

public class Main {

	/**
	 * @param args
	 */
	
	static Scanner scanner= new Scanner(System.in);
	
	public static void main(String[] args) {
		boolean hostBoolean = true;
		DataClass dc= new DataClass();
		
		while(hostBoolean){
				
				final String host;
				System.out.println("Enter host to connect to") ;
				host=scanner.nextLine();

					System.out.println("Attempting to connect...");

					try {
						Connection connectionMain= new Connection(host);
						connectionMain.getXMPPConnection().connect();
						//connectionMain.connectMe("designopencomm@jabber.org", "opencommdesign");
						
						System.out.println("Connected");
						
						System.out.println("the XMPP Connection is: " + connectionMain.getXMPPConnection().isConnected());
						
						dc.terminate=true;
						hostBoolean=false;
						
						
						userConnection(connectionMain);
						
						optionMethod(connectionMain);
						
						connectionMain.disconnect();
						connectionMain.getXMPPConnection().disconnect();
						
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Failure in connection");
					}
					
			}

	}
	
	/**This method provides user uses to input information*/
	public static void userConnection(Connection connectionMain){
		boolean userBoolean = true;
		String username=null;
		String pw = null;
		System.out.println("Now you are connected to the Server, enter username!");
		username= scanner.nextLine();
		System.out.println("enter your password");
		pw=scanner.nextLine();
		
		while(userBoolean){
		
		try{
		connectionMain.connectMe(username, pw);
		System.out.println("Login Successful");
		System.out.println("the XMPP Connection authentication: " + connectionMain.getXMPPConnection().isAuthenticated());

		userBoolean = false;
		}
		
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Sorry something wrong happened, let's try again \n");
			userConnection(connectionMain);
		}
		
		}
		
		
	}
	
	public static void optionMethod(Connection connectionMain) throws XMPPException{
		String input;
		System.out.println("Hello, you have reached the option menu, what would you like to do next?");
		input=scanner.nextLine();
		
		
		if( input.equals("MUC")){
			System.out.println("hello");
			multiUserChat(connectionMain);
		}
		else if(input.equals("roster")){
			
		}
		
		else {
			System.out.println("Sorry, please enter a valid command");
			optionMethod(connectionMain);
		}
		
	}
	
	public static void multiUserChat(Connection connectionMain) throws XMPPException{
		String roomName;
		String nickName;
		String optionORchat;
		String message;
		String invitee;
		boolean chatBoolean=true;
		
		//enter room name
		System.out.println("Enter the name of the room you want to create");
		roomName= scanner.nextLine();
		
		//enter nickname
		System.out.println("Enter your nickname");
		nickName= scanner.nextLine();
		
		//make MUC connection
		MUC muc= new MUC(connectionMain, roomName + "@conference.jabber.org", nickName);
		System.out.println("Your join status is: " + muc.getMUC().isJoined());
		
		//what you want to do next after you connect
		System.out.println("What you you like to do? Type 'option' or 'chat'");
		optionORchat=scanner.nextLine();
		
		if(optionORchat.equals("option")){
		optionMethod(connectionMain);
		}
		
		else if(optionORchat.equals("chat")){
			muc.listenForMessage();
			System.out.println("Enter who you want to invite");
			invitee=scanner.nextLine();
			muc.invite(invitee);
			
			System.out.println("Enter your message:");
			while(chatBoolean){
			//muc.getMessages();
			message=scanner.nextLine();
			if(message.equals("leave")){
				chatBoolean=false;
				optionMethod(connectionMain);
			}
			else{
				
			//muc.getMessages();
			muc.sendMessage(message);
			
			}
			}
		}
		
		
	}
	

}
