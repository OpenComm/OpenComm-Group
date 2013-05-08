package com.cornell.opencomm.jingleimpl.sessionmgmt;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;
import com.cornell.opencomm.jingleimpl.JingleIQPacket;
import com.cornell.opencomm.jingleimpl.JingleIQProvider;

import edu.cornell.opencomm.audio.JingleController;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

/**
 * This class is a router class for incoming <code>IQ</code> packets. It
 * forwards the packet to the concerned <code>MUCBuddy</code> object in order to
 * handle the packet.
 * 
 * @author Abhishek
 * 
 */
public class JingleIQBuddyPacketRouter {
	private static String TAG = "JingleIQBuddyPacketRouter";

	/**
	 * Registers listeners with proper filters in order to listen for incoming
	 * <code>IQ</code> packets On examining the sender of the packet, it
	 * forwards the packet onto the appropriate <code>MUCBuddy</code> object
	 * 
	 * @param connection
	 *            the <code>XMPPConnection</code> object
	 */
	public static void setup(XMPPConnection connection) {
		try {

			JingleIQProvider jiqProvider = new JingleIQProvider();

			ProviderManager.getInstance().addIQProvider(
					JingleIQPacket.ELEMENT_NAME_JINGLE,
					JingleIQPacket.NAMESPACE, jiqProvider);

			// add packet listener for JingleIQPacket
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet p) {
					Log.i(JingleIQBuddyPacketRouter.TAG, "Received a packet: "
							+ p.getXmlns());
					if (p instanceof JingleIQPacket) {
						Log.i(JingleIQBuddyPacketRouter.TAG,
								"PacketType: JingleIQPacket");
						JingleIQPacket jiq = (JingleIQPacket) p;

						Log.i(JingleIQBuddyPacketRouter.TAG,
								"From: " + jiq.getFrom() + "To: " + jiq.getTo()
										+ "Initiator: "
										+ jiq.getAttributeInitiator()
										+ "Responder: "
										+ jiq.getAttributeResponder());

						JingleController jCtrl = null;
						Log.i(JingleIQBuddyPacketRouter.TAG,
								"Looking for User: " + jiq.getFrom()
										+ " in OngoingChatBuddyList");
						String userJid = jiq.getFrom().split("/")[0];
						if (!JingleController.getUsernameToJingleController()
								.containsKey(userJid)) {
							Log.i(JingleIQBuddyPacketRouter.TAG,
									"Does not contain key " + userJid);
							VCard v = new VCard();
							try {
								v.load(NetworkService.getInstance()
										.getConnection(), userJid);
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							User u = new User(v);
							jCtrl = u.getJingle();
							jCtrl.setSID(jiq.getAttributeSID());
							Log.i(JingleIQBuddyPacketRouter.TAG,
									"Created user: Jsername = " + userJid
											+ " Session SID: " + jCtrl.getSID());
							jCtrl.processPacket(jiq);
						} else {
							jCtrl = JingleController
									.getUsernameToJingleController().get(
											userJid);
							jCtrl.setSID(jiq.getAttributeSID());
							Log.i(JingleIQBuddyPacketRouter.TAG,
									"Found user: Username = " + userJid);
							jCtrl.processPacket(jiq);
						}

					} else if (p instanceof IQ) {
						Log.i(JingleIQBuddyPacketRouter.TAG,
								"PacketType: IQ packet");
						IQ iq = (IQ) p;
						Log.i(JingleIQBuddyPacketRouter.TAG,
								"From: " + iq.getFrom() + "To: " + iq.getTo());
						String fromJID = iq.getFrom().split("/")[0];
						if (JingleController.getUsernameToJingleController()
								.containsKey(fromJID)) {
							JingleController.getUsernameToJingleController()
									.get(fromJID).processPacket(iq);
						}
					}
				}
			}, new PacketTypeFilter(IQ.class));

		} catch (Exception e) {
			Log.e(JingleIQBuddyPacketRouter.TAG, e.getMessage());
		}
	}
}
