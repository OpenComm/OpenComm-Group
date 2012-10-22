package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.util.Values;

/** An instance of this class controls the messages sent to/from participants
 * within a space.
 */
public class MessageController {
	// Debugging
	private static final String TAG = "Controller.MessageController";
	private static final boolean D = Values.D;

	// Model variables
	private Space mSpace;

	// Network variable
	private MultiUserChat muc;

	/**
	 * Constructor: a new message controller for a specified space
	 * @param mSpace - the space within which the messages are sent and received
	 */
	public MessageController(Space mSpace) {
		if (D) Log.d(TAG, "MessageController constructor called");
		this.mSpace = mSpace;
		this.muc = this.mSpace.getMUC();
		this.muc.addMessageListener(this.configMessageListener());
	}

	/**
	 * Handles receipt of different packet types
	 */
	public PacketListener configMessageListener() {
		if (D) Log.d(TAG, "configMessageListener called");
		PacketListener messageListener = new PacketListener() {
			/** Process the messages coming in in packets */
			public void processPacket(Packet packet) {

				// TODO :Handle if the primary user is the room's owner
				// 1.if it is a invitation request:
				// 2.if it is a kickout request:

				// TODO: Handlenormal messages

			}
			// TODO:if the primary user is not the owner of the room
			// 1.listen for rejection of invitation request
			// 2.listen for rejection of invitation request

			// TODO: else Handle normal messages
		};
		return messageListener;

	}
}