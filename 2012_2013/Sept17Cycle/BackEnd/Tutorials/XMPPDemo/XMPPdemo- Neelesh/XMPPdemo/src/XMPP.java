
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class XMPP {

	

	public static void main (String[] args)throws IOException{
		BufferedReader in= new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Welcome to the demo of XMPP server");
		int no_of_users=0;
		
		XMPPConnection connection = new XMPPConnection ("jabber.org");
		   try{
		     connection.connect();
			System.out.println("Please enter your username: ");
			String name = in.readLine();
			System.out.println("Please enter your password: ");
			String password =in.readLine();
			connection.login(name,password); 
			no_of_users++;
			}
		catch(XMPPException e){
			System.out.println(e);
		}
			
		
		
              boolean flag=true;
              MultiUserChat chat=null;
			        while(flag==true){
			        System.out.println("Input your command");
			        
					System.out.println("invite <user> - Invites users to the chat room");
					System.out.println("message <user>  - Sends the message to the given user");
					System.out.println("BuddyList  - Prints the list of friends who are online and offline in the chat");
					System.out.println ("Leave - Exits the chat room.");
					String command=in.readLine();
					
				     String[] br=command.split(" "); 
				     
			   
				 boolean chatroomgenerated=false;
			     boolean chatroomjoined=false;
			     
				 if(br[0].equals("invite"))
				 {	
					if(!chatroomgenerated)//When chat room has not been created
					{
					try{
						chat= new MultiUserChat(connection,"neeleshroom4@conference.jabber.org");//creates new chat room
					    System.out.println(br[1]);
						chat.create("neeleshroom4");
					    chat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
					    chatroomgenerated=true;
					    }
					catch(XMPPException e){
						//If cannot create a chat-room trying to join a pre-existing chatroom.
						System.out.println("Cannot create, trying to join");
						
						}
					}
					
					
					if(chatroomgenerated==false)
						try {
							chat.join("myroom");
	                        chatroomjoined=true;
							
						} 
					   catch (XMPPException e1) {
							
							System.out.println("Failed to join room");
						}
					
					//If a chat-room has been generated or joined, then creating a PacketListener
					if(chatroomgenerated||chatroomjoined){
					chat.addMessageListener(new PacketListener() {

							@Override
							public void processPacket(Packet arg0) {
							System.out.println("Can get the message");// Message displayed when you receive a message
							}
					});
					}
					
					chat.invite(br[1],"Testing");
				 }
				 
				
				
				 
				 
			/*To send a hard-coded message to someone in the chat-room */	 
				 if(br[0].equals("message")){
					String msg="Neelesh says hi";
					Message message= chat.createMessage();
					message.setBody(msg);
					 try {
						chat.sendMessage(message);
					} catch (XMPPException e) {
						System.out.println("Error in sending message");
					}
					
				
				}	
					
					
						
					
				
				/* Printing the buddy list */
				if(br[0].equals("BuddyList")){
				 Roster roster = connection.getRoster();
				
				Collection<RosterEntry> entries = roster.getEntries();
				ArrayList<RosterEntry> onlineUsersInRoom = new ArrayList<RosterEntry>();
				ArrayList<RosterEntry> offlineUsersInRoom = new ArrayList<RosterEntry>();
				
				for (RosterEntry entry : entries) {
				    
				    if(entry.toString().contains("Buddies")){
				    	String name=entry.getUser();
				    
				    if(roster.getPresence(name).toString().contains("unavailable")){
				    	
				    offlineUsersInRoom.add(entry);
				    	
				    }
				    else{
					    onlineUsersInRoom.add(entry);
					    	
					    }
				    }
				    
				    
				    
					}
				System.out.println("offline users");
				for(int i=0;i< offlineUsersInRoom.size(); i++){
					
					System.out.println(offlineUsersInRoom.get(i));
					}
				
				
				
				}
				
				
				if(command.equals("exit")){
					flag=false;
					connection.disconnect();
					System.exit(0);
					
					
				}
			        }}}

	
				
				
	
			        
			      
			        

	
			        
					
					
					


				
				

	
		
			
		
			

	

