import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class ConnectXMPP{


	public static void main(String[] args) {
		/**
		 * connect <XMPPname>           - Connect to an XMPP .
		 * invite                             - invite users to a MultiUserChat. (The room name and list of buddies should be hardcoded).
		 *                                      When the invitation is sent, your app should join the MUC and
	                                            assume that at least one of the buddies you invited joined as well
		 * sendmessage <buddy>              - Send a message. (The app doesn't need to take a message as input. It can be hardcoded.)
		 *                                    - Print to the command line whenever a message is received (Note:
	                                            you don't have to print the message, just that one was received)
		 * PrintbuddyList <>                  - Print a buddy list. The buddy list should differentiate between buddies who
	                                            are offline, those who are online, and those who are both online
	                                            and in the MUC with you.
		 * exit                               - Leave the chat and close gracefully
		 */ 


		System.out.println("Welcome to the XMPP Demo System. \n");

		BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
		XMPPConnection connection=null;
		MultiUserChat muc= null;
		boolean isGenerated=false;

		try{
			connection = new XMPPConnection("jabber.org");
			connection.connect();
			System.out.println("Connection is established: " + connection.isConnected());


		}

		catch (XMPPException e){
			System.out.println(e);
		}




		try{
			System.out.println("Please enter your username: ");
			String name = reader.readLine().trim(); //trims excess spaces upon reading input
			System.out.println("Please enter your password: ");
			String password =reader.readLine().trim();
			connection.login(name,password);
			connection.isAuthenticated();
		}
		catch(IOException e1){
			System.out.println(e1);
		}

		catch (XMPPException e){
			System.out.println(e);
		}


		
		if(!isGenerated){
			muc = new MultiUserChat(connection, "friendSam@conference.jabber.org");


			try{
				muc.create("friendSam");
				muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
				isGenerated= true;

			}


			catch (XMPPException e1){
				try {
					muc.join("friendSam");
				} catch (XMPPException e) {

					System.out.println(e);
				}
			}

			muc.addMessageListener (new PacketListener(){
				public void processPacket(Packet args){
					System.out.println("It is recieving message");
				}
			});
		}

		int breaker=0;
		ArrayList<String> inroombuddy = new ArrayList<String>();
		while(breaker==0){
			String[] command= null;

			System.out.println("\nInput your command:\n"
					+ "invite <username>       - invite users to a MultiUserChat.\n"
					+ "sendmessage              - Send a message.\n"
					+ "PrintbuddyList        - Prints a buddy list.\n"
					+"exit                     - Exits the chat.\n"
					+ "");

			try{
				String inputs = reader.readLine().trim();           
				command = inputs.split(" ");  //splits the input based on space and forms an array*/
			}
			catch (IOException e){
				System.out.println (e);
			}



			if (command.length==2 && command[0].equals("invite")){

				muc.addInvitationRejectionListener(new InvitationRejectionListener(){

					@Override
					public void invitationDeclined(String arg0,
							String arg1) {
						System.out.println("Invitation declined!!");
					}
				});

				muc.invite(command[1]+"@jabber.org", "Testing");
				System.out.println("Your invitation was recieved!!!");
				inroombuddy.add(command[1]);

			}


			else if (command.length==1 && command[0].equals("sendmessage"))

				try{
					String msg= "This is XMPP demo";
					Message message= muc.createMessage();
					message.setBody(msg);
					muc.sendMessage(msg);
					System.out.println("Sent a message This is an XMPP Demo");
				}

			catch (XMPPException e){
				System.out.println("error in sending");
			}


			else if (command.length==1 && command[0].equals("PrintbuddyList")){
				Roster roster = connection.getRoster();
				Collection<RosterEntry> entries = roster.getEntries();
				ArrayList<RosterEntry> onlineusers = new ArrayList<RosterEntry>();
				ArrayList<RosterEntry> offlineusers = new ArrayList<RosterEntry>();


				for (RosterEntry entry : entries) {



					if (entry.toString().contains("Buddies")){
						String name = entry.getUser();
						if (roster.getPresence(name).toString().contains("unavailable"))
							offlineusers.add(entry);
						else
							onlineusers.add(entry);
					}
				}

				for (int i=0; i < onlineusers.size(); i++)
					System.out.println(onlineusers.get(i).toString()+"is online");

				for (int i=0; i < offlineusers.size(); i++)
					System.out.println(offlineusers.get(i).toString()+ "is offline");

				if (inroombuddy.size()==0)
					System.out.println("There is no one in your room currently");

				else{	
					for (int i=0; i<inroombuddy.size(); i++)    //if invited the user is expected to 
						//accept your invitation and thus be in your room
						System.out.println(inroombuddy.get(i)+" is in you room and online");
				}
			}


			else if (command.length==1 && command[0].equals("exit")) {
				breaker=1;
				System.out.println("Thank You for using The ConnectXMPP System!");
				connection.disconnect();
			}


			else
				System.out.println("You have not typed the rigth thing!!");




		}

	}
}

























