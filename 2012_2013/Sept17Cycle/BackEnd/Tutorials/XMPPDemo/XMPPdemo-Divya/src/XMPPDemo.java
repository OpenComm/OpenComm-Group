
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


public class XMPPDemo {
	 static ArrayList<String> inroombuddy= new ArrayList<String>();
	
	public static void main (String[] args) throws IOException{
		BufferedReader read= new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Welcome to the demo of XMPP server");
		XMPPConnection connection=new XMPPConnection("jabber.org");
		try{
			connection.connect();
			System.out.println("Please enter your username: ");
			String name = read.readLine().trim(); //trims excess spaces upon reading input
			System.out.println("Please enter your password: ");
			String password =read.readLine().trim();
			connection.login(name,password); //attempts to login to the server
			if(connection.isConnected()){
				System.out.println("yess");
			}
			if(connection.isAuthenticated())
				System.out.println("that works");
		}
		catch (XMPPException e){
			System.out.println(e);
		}

		int n=0;
		MultiUserChat muc=null;
		while(n==0){
			System.out.println("\nInput your command:\n"
					+ "\tinvite <user>                      - Invites users to the chat room.\n"
					+ "\tmessage                            - Sends the message to the given user.\n"
					+ "\tBuddyList                          - Prints the list of friends who are online and offline in the chat\n"
					+ "\tleave                              - Exits the chat room."
					+ "");     


			String fullString=read.readLine().trim();
			String[] breake=fullString.split(" "); // Used " " to separate command from ID entered breake is the array where the input is stored.
			boolean chatroomgen=false;
			boolean chatroomjoin=false;

			if(breake.length==2 && breake[0].equals("invite")){	//the input command is not exit and the user is inviting others for multi chat
				if(!chatroomgen){
					try{
						System.out.println("it is working fine as off now");
						muc=new MultiUserChat(connection,"Firstchatroomdivya2@conference.jabber.org");
						muc.create("Firstchatroomdivya2");
						muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
						chatroomgen=true;
					}
					catch(XMPPException e){
						System.out.println("I cannot create chat");
					}
				}

				if(chatroomgen||chatroomjoin){
					muc.addMessageListener(new PacketListener() {
						@Override
						public void processPacket(Packet arg0) {
							System.out.println("message recieved");
						}
					});
				}

				if(chatroomgen==false)
					try {
						muc.join("mynewfirstchat");
						chatroomjoin=true;
					} 
				catch (XMPPException e1) {
					System.out.println("Failed to join room");
				}
				muc.invite(breake[1],"Testing");
				inroombuddy.add(breake[1]);
			}

			else if (breake.length==1 && breake[0].equals("message")){
				try {
					String msg="This is finally working";
					Message message=muc.createMessage();
					message.setBody(msg);
					muc.sendMessage(msg);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if (breake.length==1 && breake[0].equals("BuddyList") ){
				Roster roster = connection.getRoster();
				Collection <RosterEntry> entries=roster.getEntries();
				ArrayList<RosterEntry> onlineUserInRoom=new ArrayList<RosterEntry>();
				ArrayList<RosterEntry> offlineUserInRoom=new ArrayList<RosterEntry>();

				for (RosterEntry entry : entries){
					String name=entry.getUser();
					if(roster.getPresence(name).toString().contains("unavailable")){
						offlineUserInRoom.add(entry);
					}
					if (roster.getPresence(name).toString().contains("available")){
						onlineUserInRoom.add(entry);
					}
				}
				System.out.println("online User");
				for(int i=0;i<onlineUserInRoom.size(); i++){
					System.out.println(onlineUserInRoom.get(i));
				}
				System.out.println("Offline User");
				for (int i=0; i<offlineUserInRoom.size(); i++){
					System.out.println(offlineUserInRoom.get(i));
				}
				if (inroombuddy.get(0)==null)
				     System.out.println("There is no one in your room currently");

				    else{ 
				     for (int i=0; i<inroombuddy.size(); i++)    //if invited the user is expected to 
				      //accept your invitation and thus be in your room
				      System.out.println(inroombuddy.get(i)+" is in you room and online");
				    }
			}

			else if (breake[0].equals("leave")){
				System.out.println("thanks for using this demo");
				connection.disconnect();
				break;
			}

		}

	}
}				
