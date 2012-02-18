package com.cornell.opencomm.jingleimpl.sessionmgmt;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import android.util.Log;

import com.cornell.opencomm.buddies.MUCBuddy;
import com.cornell.opencomm.jingleimpl.JingleIQPacket;
import com.cornell.opencomm.jingleimpl.JingleIQProvider;

import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

/**
 * This class is a router class for incoming <code>IQ</code> packets. It forwards the packet
 * to the concerned <code>MUCBuddy</code> object in order to handle the packet.
 * 
 * @author Abhishek
 *
 */
public class JingleIQBuddyPacketRouter {
	
	private static String TAG = "JingleIQBuddyPacketRouter";

	private static NetworkService networkService = null;
	
	/**
	 * Provides easy access to the <code>NetworkService</code> object in order to fetch the <code>ongoingChatBuddyList</code>
	 * @param client the instance of the NetworkService object
	 */
	public static void setNetworkService(NetworkService client){
		networkService = client;
	}
	
	/**
	 * Registers listeners with proper filters in order to listen for incoming <code>IQ</code> packets
	 * On examining the sender of the packet, it forwards the packet onto the appropriate <code>MUCBuddy</code> object
	 * @param connection the <code>XMPPConnection</code> object
	 */
	public static void setup(XMPPConnection connection) {
		try {
			
			JingleIQProvider jiqProvider = new JingleIQProvider();
			
			ProviderManager.getInstance().addIQProvider(JingleIQPacket.ELEMENT_NAME_JINGLE,
					JingleIQPacket.NAMESPACE, jiqProvider);

			// add packet listener for JingleIQPacket
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet p) {
					Log.i(JingleIQBuddyPacketRouter.TAG, "Received a packet");
					if (p instanceof JingleIQPacket) {
						Log.i(JingleIQBuddyPacketRouter.TAG, "PacketType: JingleIQPacket");
						JingleIQPacket jiq = (JingleIQPacket) p;
						
						Log.i(JingleIQBuddyPacketRouter.TAG, "From: "  + jiq.getFrom()+ "To: " + jiq.getTo() + 
								 "Initiator: " + jiq.getAttributeInitiator() + "Responder: " + jiq.getAttributeResponder());
						
						MUCBuddy buddy = null;
						Log.i(JingleIQBuddyPacketRouter.TAG, "Looking for Buddy: " + jiq.getFrom() + " in OngoingChatBuddyList");
						if(!networkService.getXMPPConnection().getRoster().contains(jiq.getFrom())){
							Log.i(JingleIQBuddyPacketRouter.TAG, "Does not contain key" + jiq.getFrom());
							buddy = User.getAllUsers().get(jiq.getFrom()).getMUCBuddy();
							buddy.setSID(jiq.getAttributeSID());
							//networkService.getOngoingChatBuddyList().put(jiq.getFrom(), buddy);
							Log.i(JingleIQBuddyPacketRouter.TAG, "Created buddy: BuddyJID = " + buddy.getbuddyJID() + 
									" Session SID: " + buddy.getSID());
							buddy.processPacket(jiq);
						} else {
							//buddy = networkService.getOngoingChatBuddyList().get(jiq.getFrom());
							buddy = User.getAllUsers().get(jiq.getFrom()).getMUCBuddy();
							buddy.setSID(jiq.getAttributeSID());
							Log.i(JingleIQBuddyPacketRouter.TAG, "Found buddy: BuddyJID = " + buddy.getbuddyJID());
							buddy.processPacket(jiq);
						}
						
					} else if (p instanceof IQ) {
						Log.i("XMPPClient", "PacketType: IQ packet");
						IQ iq = (IQ) p;
						Log.i("XMPPClient", "From: "  + iq.getFrom()+ "To: " + iq.getTo());
						//if(networkService.getOngoingChatBuddyList().containsKey(iq.getFrom())){
						//	networkService.getOngoingChatBuddyList().get(iq.getFrom()).processPacket(iq);
						//}
					}
				}
			}, new PacketTypeFilter(IQ.class));
			
		} catch (Exception e) {
			Log.e(JingleIQBuddyPacketRouter.TAG, e.getMessage());
		}
	}

}
