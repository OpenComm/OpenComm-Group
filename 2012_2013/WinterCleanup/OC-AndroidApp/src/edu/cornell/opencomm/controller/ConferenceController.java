package edu.cornell.opencomm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceDataModel;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.view.ConferenceCardView;
import edu.cornell.opencomm.view.ConferenceView;

@SuppressWarnings("unused")
public class ConferenceController {
	
	private ConferenceView view;
	
	private Context context;
	
	private ConferenceDataModel conferenceModel; // the conference that is being
													// controlled

	private static final String TAG = "ConferenceController";
	private static final boolean D = true;

	// users who have been invited who have not been added to the model
	// information
	
	// TODO: add a listener for invitations and invitation responses

	public ConferenceController(ConferenceView view, ConferenceDataModel model) {
		// TODO: do we really need this
		this.context = view;
		this.view = view;
		this.conferenceModel = model;
	}
	
	// Constructor - initialize required fields
	public ConferenceController(ConferenceDataModel conf) {
		this.conferenceModel = conf;
	}
	
	public void inviteUser(User u, String sChat) {
		ConferenceRoom chatRoom = findChat(sChat);
		chatRoom.invite(u.getUsername(), null);
	}
	
	private ConferenceRoom findChat(String sChat) {
		String roomID = sChat;
		HashMap<String, ConferenceRoom> chatSpaceIDMap = conferenceModel
				.getIDMap();
		ConferenceRoom chatRoom = chatSpaceIDMap.get(roomID);
		return chatRoom;
	}
	
	public void leave(int sChat, User currUser){
		
		ConferenceRoom room = conferenceModel.getRoomByTag(sChat);
		User moderator = room.getModerator();
		
		if(moderator.getUsername().equals(currUser.getUsername())){
			if(room.getCUserList().size()>1){
				User newMod = room.getCUserList().get(0).user;
				if(newMod.equals(currUser)){
					newMod = room.getCUserList().get(1).user;
				}
				try {
					transferPrivileges(currUser, newMod, sChat);
				} catch (XMPPException e) {
					Log.e(TAG, "could not transfer privileges", e);
				}
			}
		}
		room.leave();
	}
	
	public void leaveChat(int chat, User currUser){
		if(chat==(ConferenceConstants.MAIN_ROOM_INDEX)){
			leave(ConferenceConstants.LEFT_ROOM_INDEX, currUser);
			leave(ConferenceConstants.RIGHT_ROOM_INDEX, currUser);
			leave(chat, currUser);
		}
		else{
			leave(chat, currUser);
		}
	}
	
	public void end(User currUser){
		if(conferenceModel.getMod().equals(currUser)){
			Collection<ConferenceRoom> allRooms = conferenceModel.getIDMap().values();
			for(ConferenceRoom r : allRooms){
				for(User u : r.getUserMap().values()){
					if(!u.equals(currUser)){
						kickoutUserByRoomID(r.getRoomID(), u, currUser);
					}
				}
				try {
					r.destroy(null, null);
				} catch (XMPPException e) {
					Log.e(TAG, "Could not destroy MUC", e);
				}
			}
		}
	}
	
	//	// sChat is the indicator that it is the left side chat or the right
	//	// sidechat
	//	// it may be better to use an int or constant ot indicate left/right side
	//	// chat
	//	public void leaveChat(String sChat, User currentUser) throws XMPPException {
	//
	//		HashMap<String, ConferenceRoom> conferencemap = conferenceModel.getIDMap();
	//		ConferenceRoom spacechat = conferencemap.get(sChat);
	//		User moderator = spacechat.getModerator();
	//
	//		if (moderator.getUsername().equals(currentUser.getUsername())) { // if
	//																			// moderator
	//			User newmoderator = null; // ask for new moderator if current
	//										// moderator is leaving
	//			User new_sidemoderator = null; // find the new_sidemoderator for
	//											// this side chat
	//			if (conferenceModel.getIsmain()) { // if in mainchat
	//				String[] keys = (String[]) conferencemap.keySet().toArray();
	//				for (int i = 0; i <= 2; i++) {
	//					if (keys[i] != sChat) { // if not mainchat i.e. sidechats,
	//											// transfer privileges and then
	//											// leave
	//						ConferenceRoom spacechat1 = conferencemap.get(keys[i]);
	//						String name = spacechat1.getRoomID();
	//						transferPrivileges(currentUser, newmoderator,
	//								name);
	//						spacechat1.leave();
	//						conferenceModel.setIsmain(true); // not sure if this is
	//														// needed, please check
	//					}
	//					String name = spacechat.getRoomID();
	//					transferPrivileges(newmoderator, currentUser, name); // finally
	//																				// leave
	//																				// the
	//																				// mainchat
	//					spacechat.leave();
	//				}
	//			} else { // if this is a side chat than only for that
	//				String name = spacechat.getRoomID();
	//				transferPrivileges(newmoderator, currentUser, name);
	//				spacechat.leave();
	//			}
	//		}
	//
	//		else { // if only a user
	//			if ((conferenceModel.getIsmain())) { // if main then leave side chat
	//												// also
	//				String[] keys = (String[]) conferencemap.keySet().toArray();
	//				for (int i = 0; i <= 2; i++) {
	//					if (keys[i] != sChat) { // if not mainchat i.e. sidechats
	//											// leave
	//						ConferenceRoom spacechat1 = conferencemap.get(keys[i]);
	//						spacechat1.leave();
	//					}
	//					spacechat.leave(); // leave main
	//				}
	//			} else {
	//				spacechat.leave(); // leave the side chat
	//			}
	//		}
	//	}
	
	//	public void endConference(String conf, User u) {
	//		ConferenceRoom chat = this.conferenceModel.getIDMap().get(conf);
	//		if (chat.getModerator().equals(u)) {
	//			HashMap<String, ConferenceRoom> allChats = this.conferenceModel
	//					.getIDMap();
	//			for (String s : allChats.keySet()) {
	//				try {
	//					allChats.get(s).destroy(null, null);
	//				} catch (XMPPException e) {
	//					Log.e("ConferenceController",
	//							"XMPPError when destroying a MUC");
	//				}
	//			}
	//		}
	//	}
	
	/**
	 * 
	 * @param u1
	 *            current moderator leaving the chat
	 * @param u2
	 *            new moderator
	 * @param room
	 *            ChatSpaceModel in which the privilege has to be transferred
	 * @throws XMPPException
	 */
	public void transferPrivileges(User u1, User u2, int chat)
	throws XMPPException {
		ConferenceRoom room = this.conferenceModel.getRoomByTag(chat);
		room.updateMod(u1, u2);
	}
	
	public void kickoutUserByRoomID(String chat, User userToBeKicked, User currUser) {
		if (this.conferenceModel.getIDMap().get(chat).getModerator()

			.equals(currUser)) {
			try {
				this.conferenceModel.getIDMap().get(chat)
				.kickParticipant(userToBeKicked.getNickname(), null);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not kick participant", e);
			}
		}
	}
	
	public void kickoutUserByRoomTAG(int chat, User userToBeKicked, User currUser) {
		if (this.conferenceModel.getRoomByTag(chat).getModerator().equals(currUser)) {
			try {
				this.conferenceModel.getRoomByTag(chat)
				.kickParticipant(userToBeKicked.getNickname(), null);
			} catch (XMPPException e) {
				Log.e(TAG, "Could not kick participant", e);
			}
		}
	}
	
	// TODO: the following methods will not be implemented for the Nov4 cycle
	
	public void moveUser(User u) {
		// TODO: modify audio manipulation based on new location (future work)
	}
	
	public void init() {
		// TODO Back End
		// 1. Open a channel to listen to incoming messages / register this
		// class with the connection
		
	}
	
	public void handleIncomingPackets() {
		// Handle
		// 1. Invitation response
		// 2. Priv transfer
	}
	
	public void sendPackets() {
		// Send
		// 1.invitation
		// 2.trans_priv
	}
	
	public void handleOverflow() {
		Toast.makeText(this.context, "Overflow Clicked", Toast.LENGTH_SHORT)
				.show();
	}

	public void addPersonClicked() {
		Toast.makeText(this.context, "Add Contact Clicked", Toast.LENGTH_SHORT)
				.show();
		// TODO- Should go to add contact to conference page
	}
	
	public void muteClicked() {
		Toast.makeText(this.context, "Mute Clicked", Toast.LENGTH_SHORT).show();
	}
	
	public void leaveConference() {
		Toast.makeText(this.context, "Leave Conference Clicked",
				Toast.LENGTH_SHORT).show();
	}
	
	public void removeUser() {
		Toast.makeText(this.context, "Remove User", Toast.LENGTH_SHORT).show();
	}
	
	public void setNewModerator() {
		Toast.makeText(this.context, "Set New Moderator", Toast.LENGTH_SHORT).show();
		//TODO
	}

	public void showProfile() {
		Toast.makeText(this.context, "Show Profile", Toast.LENGTH_SHORT).show();
	}
	
	public void handleEndClicked(User user) {
		// TODO won't work until backend finds a way to retrieve the moderator (as of now moderator is null)
		// end(user);
	}
	
	public void handleLeaveClicked(int roomIndex, User user){
		// TODO won't work until backend fins a way to retrieve the moderator (as of now moderator is null)
		//leaveChat(roomIndex, user); 
	}
	
	public void handleOnContextAddClicked() {
		Toast.makeText(this.context, "Add Contact clicked", Toast.LENGTH_SHORT)
				.show();
	}
}
