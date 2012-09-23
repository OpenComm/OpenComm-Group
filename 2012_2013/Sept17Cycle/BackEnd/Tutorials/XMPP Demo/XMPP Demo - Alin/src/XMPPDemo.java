import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;



public class XMPPDemo {

	
	private static final String DEFAULT_ROOM_NAME = "Demo_Room";
	private static final String COMMAND_OPTIONS = "\nThis program recognizes the following commands:\n" +
												 "help - Prints a list of available commands\n" +
												 "createRoom <RoomName> - Creates a room with a name you input\n" +
												 "viewRoom - Shows the name of the room you are currently in\n" +
												 "viewBuddyList - Shows all buddies, and differentiates between those online, offline, and in the MUC with you\n" +
												 "invite <user> - Beings the process of inviting friends to a chatroom\n" +
												 "sendMessage <message> - Sends a message to a user (Prompts you to enter message)\n" +
												 "exit - Exits the program\n";
	private static final String HELP_VERB = "help";
	private static final String VIEW_ROOM_VERB = "viewRoom";
	private static final String VIEW_BUDDY_VERB = "viewBuddyList";
	private static final String INVITATION_VERB = "invite";
	private static final String MESSAGE_VERB = "sendMessage";
	private static final String EXIT_VERB = "exit";
	
	public static void main(String[] args) {
		Scanner inputReader = new Scanner(System.in);
		XMPPConnection connection = null;
		PacketListener myPacketListener = null;
		ArrayList<String> invitedUsers = new ArrayList<String>();
		System.out.println("Welcome to the Multi User Chat Desktop Application.");
		boolean packetListenerBuilt = false;
		boolean notConnected = true;
		while(notConnected){
			System.out.println("Enter the XMPP server you would like to connect to (e.g. myserver.org):");
			String xmppServer = inputReader.nextLine();
			try{
				System.out.println("Processing... Please wait");
				connection = new XMPPConnection(xmppServer); //connects to server address provided
				connection.connect();
				if(connection.isConnected()){
					System.out.println("Connection Successful!\n");
					notConnected = false;
				}
				else{
					notConnected = true;
					System.out.println("Connection Failed!\n");
				}
			}
			catch(Exception e){
				System.out.println("There was an issue connecting to the XMPP server '" + xmppServer + "' (We recommend jabber.org)");
			}
		}
		boolean validUserAndPass = false;
		String userName = null;
		while(!validUserAndPass){
			System.out.println("Please enter your username: ");
			userName = inputReader.nextLine().trim(); //trims excess spaces upon reading input
			System.out.println("Please enter your password: ");
			String password = inputReader.nextLine().trim();
			try {
				System.out.println("Validating your information...");
				connection.login(userName,password); //attempts to login to the server
				validUserAndPass = true;
				System.out.println("Login Successful!\n");
			} catch (Exception e) {
				System.out.println("Error logging into server - Your username or password may be incorrect");
			}
		}
		System.out.println("Enter a command to begin (or 'help' to see available commands)");
		MultiUserChat groupChat = null;
		boolean chatRoomGenerated = false; //checked against to make sure room is not regenerated each time a user is invited
		boolean programTerminated = false;
		while(!programTerminated){
			String input = inputReader.nextLine();
			input = input.trim(); // ignores spaces before and after the command if the command itself is correct - does not remove spaces mixed into the command
			if(input.startsWith(HELP_VERB) && input.length() == HELP_VERB.length()){
				System.out.println(COMMAND_OPTIONS); //prints list of available commands
			}
			else if(input.equals(EXIT_VERB)){
				programTerminated = true;
				System.out.println("Program Terminated");
				inputReader.close();
				connection.disconnect();
				break;
			}
			else if(input.equals(VIEW_ROOM_VERB)){
				if(groupChat == null){
					System.out.println("You are not currently in any chat rooms");
				}
				else{
					System.out.println("You are currently in the '" + DEFAULT_ROOM_NAME + "' chatroom");
				}
			}
			else if(input.startsWith(INVITATION_VERB)){
				String userToInvite = input.substring(INVITATION_VERB.length() + 1); //+1 accounts for space after verb, isolates the username
				if(!chatRoomGenerated){
					try{	
						groupChat = null;
						System.out.println("Initializing a default chat room...");
						groupChat = new MultiUserChat(connection,DEFAULT_ROOM_NAME + "@conference.jabber.org");
						groupChat.create(DEFAULT_ROOM_NAME); //initializes a default instant room, automatically placing the user in it
						groupChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
						System.out.println("Default chat room initialized!");
						chatRoomGenerated = true;
					}
						catch(XMPPException e){
							System.out.println("Chat room already exists");
							try{
								System.out.println("Attempting to join chat room...");
								groupChat.join(DEFAULT_ROOM_NAME);
								chatRoomGenerated = true;
							}
							catch(XMPPException r){
								System.out.println("Failed to join chat room");
							}
						}
				}
					if(chatRoomGenerated){
						//listen for invitation rejections
						groupChat.addInvitationRejectionListener(new InvitationRejectionListener() {
				          @Override
						public void invitationDeclined(String invitee, String reason) {
				              System.out.println("User '" + invitee + "' declined your chat invitation.");
				              System.out.println("Reason: " + reason);
				          }
						});
						if(!packetListenerBuilt && chatRoomGenerated){ //builds the packetListener only once - and only if the chatRoom has been built
								myPacketListener = new PacketListener() {
									@Override
									public void processPacket(Packet arg0) {
										System.out.println("A new message has been posted on the group chat!");							
									}
								    };
							    groupChat.addMessageListener(myPacketListener);
						}
				
					groupChat.invite(userToInvite, "User '" + userName + "' has invited you to join a chat room");
					invitedUsers.add(userToInvite);
					System.out.println("Successfully invited user '" + userName + "' to a chat room");	
					}
			}
			else if(input.equals(VIEW_BUDDY_VERB)){ //if user enters viewBuddyList
				Roster roster = connection.getRoster(); //gets other users on this connection
				Collection<RosterEntry> entries = roster.getEntries();
				ArrayList<RosterEntry> onlineUsers = new ArrayList<RosterEntry>();
				ArrayList<RosterEntry> offlineUsers = new ArrayList<RosterEntry>();
				for(RosterEntry entry: entries){
					if(entry.toString().contains("[Buddies]")){ //if other users are marked as buddies, print them to the list
						String user = entry.getUser();
						if(roster.getPresence(user).toString().contains("unavailable")){ //if user is offline, add them to offlineUsers
							offlineUsers.add(entry);
						}
						else{
							onlineUsers.add(entry);
						}
					}
				}
				
				System.out.println("Online Buddies in your chat room:");
				if(groupChat != null){
					if(invitedUsers.size() == 0){
						System.out.println("There are 0 buddies in your chat room");
					}
					else{
						for(String user: invitedUsers){
							System.out.println(user); //warning: this code assumes that users actually accept your invitation (and are online)

						}
				}
				}
				System.out.println("Online Buddies:");
				if(onlineUsers.size() == 0){
					System.out.println("There are 0 buddies online");
				}
				else{
					for(RosterEntry entry: onlineUsers){
						String user = entry.toString().substring(0, entry.toString().indexOf("[Buddies]"));
						System.out.println(user);
					}
				}
				System.out.println("Offline Buddies:");
				if(offlineUsers.size() == 0){
					System.out.println("There are 0 buddies offline");
				}
				else{
					for(RosterEntry entry: offlineUsers){
						String user = entry.toString().substring(0, entry.toString().indexOf("[Buddies]"));
						System.out.println(user);
					}
				}
				System.out.println("");
			}
			else if(input.startsWith(MESSAGE_VERB)){
				String messageBody = input.substring(MESSAGE_VERB.length() + 1); //Takes the remainder of the string, minus the space = the mesage
				Message message = groupChat.createMessage();
				message.setBody(messageBody);
				try{
					groupChat.sendMessage(message);
					System.out.println("Your message: '" + messageBody + "' has been sent");
					
				}
				catch(XMPPException e){
					
				}
			}
			else{
				System.out.println("Command not recognized.  Type 'help' to see a list of available commands");
			}
			if(!programTerminated){
				System.out.println("Enter a command: ");
			}
			
		}

	}
	
}






/*
4. Build a command line desktop app that can
- invite users to a MultiUserChat. (The room name and list of buddies should be hardcoded).
- When the invitation is sent, your app should join the MUC and
assume that at least one of the buddies you invited joined as well
- Send a message. (The
app doesn't need to take a message as input. It can be hardcoded.)
- Print to the command line whenever a message is received (Note:
you don't have to print the message, just that one was received)
- Print a buddy list. The buddy list should differentiate between buddies who
are offline, those who are online, and those who are both online
and in the MUC with you.
- Leave the chat and close gracefully
*/