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
						dc.terminate=true;
						hostBoolean=false;
						userConnection(connectionMain);
						
						optionMethod(connectionMain);
						
						//multiUserChat(connectionMain);
						
						
						//connect user to specific login
					
						
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
		userBoolean = false;
		}
		
		catch(Exception e){
			//e.printStackTrace();
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
		else {
			System.out.println("Sorry, please enter a valid command");
			optionMethod(connectionMain);
		}
		
	}
	
	public static void multiUserChat(Connection connectionMain) throws XMPPException{
		System.out.println("Enter the name of the room you want to create");
		String roomName;
		roomName= scanner.nextLine();
		
		try {
			MUC muc= new MUC(connectionMain, roomName);
		} catch (XMPPException e) {
			System.out.println("you have reached an error in creating a MUC");
			optionMethod(connectionMain);
			e.printStackTrace();
		}
		System.out.println("You have successfully created a room");
		
		
	}
	
	

}
