package edu.cornell.opencomm.controller;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.network.Network;

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
            @Override
            public void processPacket(Packet packet) {
                if(D) Log.d(TAG, "processPacket called");
                Message message = (Message) packet;
                String from = message.getFrom();
                String body = message.getBody();
                Occupant userOcc = muc.getOccupant(mSpace.getRoomID()
                        + "/" + MainApplication.userPrimary.getNickname());
                // if the primary user is the room's owner
                if (mSpace.getOwner().equals(MainApplication.userPrimary)) {
                    // if it is a invitation request:
                    if (body.contains(Network.REQUEST_INVITE)) {
                        if (D) Log.d(TAG, "configMessageListener - invitation request received");
                        mSpace.getInvitationController().receiveInvitationRequest(body);
                    }
                    // if it is a kickout request:
                    else if (body.contains(Network.REQUEST_KICKOUT)) {
                        if (D) Log.d(TAG, "configMessageListener - kickout request received");
                        mSpace.getKickoutController().receiveKickoutRequest(body);
                    }

                    // normal messages
                    else {
                        if (D) Log.d(TAG, "Regular message receive from " + from
                                + ": " + body);
                    }

                }
                // if the primary user is not the owner of the room
                else {
                    // listen for rejection of invitation request
                    if (body.contains(Network.REJECT_INVITE)) {
                        String[] receive = mSpace.getInvitationController().receiveInvitationRequestRejection(body);
                        //TODO: Trigger update to view
                    }
                    // listen for rejection of invitation request
                    else if (body.contains(Network.REJECT_KICKOUT)) {
                        String[] receive = mSpace.getKickoutController().receiveKickoutRequestRejection(body);
                        //TODO: Trigger update to view
                    }

                    // normal messages
                    else {
                        if (D) Log.d(TAG, "Regular message receive from " + from
                                + ": " + body);
                    }
                }
            }
        };
        return messageListener;

    } // end configMessageListener method
} // end Class MessageController
