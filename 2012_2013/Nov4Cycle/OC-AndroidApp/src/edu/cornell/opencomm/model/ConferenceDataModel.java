package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import edu.cornell.opencomm.audio.JingleController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;
import edu.cornell.opencomm.util.TestDataGen;
import edu.cornell.opencomm.util.Util;

public class ConferenceDataModel implements InvitationListener {
	
	/**
	 * The map to hold chat space data model 
	 * KEY: ChatSpace Room ID
	 * VALUE: The model associated with the chat
	 */
	HashMap<String, ConferenceRoom> conferenceRoomMap;
	
	/**
	 * The map to hold chat space models in relation
	 * to each other.
	 * KEY: ChatSpace Room ID (as assigned when initializing
	 * a MUC
	 * VALUE: name of chatspace, e.g., MAIN, LEFT, RIGHT
	 */
	HashMap<Integer, String> tagToRoomIdMap;
	
	String activeChat; /** This is the ID of the current room */
	
	User mod;
	
	String mainID;
	String leftID;
	String rightID;
	
	public ConferenceDataModel(String mainSpaceRoomID, String leftChatID, String rightChatID, User u){
		conferenceRoomMap = new HashMap<String, ConferenceRoom>();
//		chatSpaceLocationMap = new HashMap<String, String>();
		activeChat = mainSpaceRoomID;
		isMain=true;
		mod = u;
		mainID = mainSpaceRoomID;
		leftID = leftChatID;
		rightID = rightChatID;
	}
	
	public void initialize(boolean isMod){
//		if(isMod){
//			ConferenceRoom csm1 = new ConferenceRoom(NetworkService.getInstance().getConnection(), mainID, UserManager.PRIMARY_USER);
//			csm1.init(true);
//			chatSpaceIDMap.put(mainID, csm1);
//			chatSpaceLocationMap.put(mainID, "MAIN");
//		
//			ConferenceRoom csm2 = new ConferenceRoom(NetworkService.getInstance().getConnection(), leftID, UserManager.PRIMARY_USER);
//			csm2.init(true);
//			chatSpaceIDMap.put(leftID, csm2);
//			chatSpaceLocationMap.put(leftID, "LEFT");
//		
//			ConferenceRoom csm3 = new ConferenceRoom(NetworkService.getInstance().getConnection(), rightID, UserManager.PRIMARY_USER);
//			csm3.init(true);
//			chatSpaceIDMap.put(rightID, csm3);
//			chatSpaceLocationMap.put(rightID, "RIGHT");
//		}
//		else{
//			ConferenceRoom csm1 = new ConferenceRoom(NetworkService.getInstance().getConnection(), mainID, UserManager.PRIMARY_USER);
//			csm1.init(false);
//			chatSpaceIDMap.put(mainID, csm1);
//			chatSpaceLocationMap.put(mainID, "MAIN");
//			
//			ConferenceRoom csm2 = new ConferenceRoom(NetworkService.getInstance().getConnection(), leftID, UserManager.PRIMARY_USER);
//			csm2.init(false);
//			chatSpaceIDMap.put(leftID, csm2);
//			chatSpaceLocationMap.put(leftID, "LEFT");
//			
//			ConferenceRoom csm3 = new ConferenceRoom(NetworkService.getInstance().getConnection(), rightID, UserManager.PRIMARY_USER);
//			csm3.init(false);
//			chatSpaceIDMap.put(rightID, csm3);
//			chatSpaceLocationMap.put(rightID, "RIGHT");
//		}
	}
	
	public void setMod(User u){
		mod = u;
	}
	
	public User getMod(){
		return mod;
	}
	
	boolean isMain;	//if the space is main chat set this to true
	
	public ConferenceDataModel(String conferenceTitle,String conferenceTime,String conferenceOwner){
		conferenceRoomMap = new HashMap<String, ConferenceRoom>();
		tagToRoomIdMap = new HashMap<Integer, String>();
		isMain=true;
		//Bind the main room
		createMainRoom(Util.generateUniqueRoomId(conferenceTitle, conferenceTime, conferenceOwner));
		//Left and right room will be created onDemand 
	
	}
	private void createMainRoom(String roomId){
		ConferenceRoom mainRoom = new ConferenceRoom(roomId);
		//TODO: Warning: for testing only
		mainRoom.setList(TestDataGen.createExampleUsers());
		conferenceRoomMap.put(roomId, mainRoom);
		tagToRoomIdMap.put(ConferenceConstants.MAIN_ROOM_INDEX, roomId);
	}
	/**Toggle between mute and unmute for the conference
	 * @return the current state for mute
	 */
	public boolean toggleMute(){
		isMute = !isMute;
		return isMute;
	}
	public boolean isMute(){
		return this.isMute;
	}
	/**
	 * The state variable to store mute/unmute
	 */
	private boolean isMute = false;
	public String getActiveChat(){
		return activeChat;
	}
	
    public boolean getIsmain(){
    	return isMain;
    }
    
    public void setIsmain(boolean s){
    	isMain=s;
    }

    public HashMap<String, ConferenceRoom> getIDMap(){
    	return conferenceRoomMap;
    }
    public ConferenceRoom getRoomByTag(Integer roomTag){
    	ConferenceRoom room = null;
    	if(tagToRoomIdMap.containsKey(roomTag)){
    		String roomId = tagToRoomIdMap.get(roomTag);
    		if(conferenceRoomMap.containsKey(roomId)){
    			room = conferenceRoomMap.get(roomId);
    		}
    	}
    	return room;
    }
    //TODO : Check if left empty return true else right is empty return true
    // else false
    public boolean isRoomAvailable(){
    	return false; 
    }

	public void invitationReceived(Connection conn, String room,
			String inviter, String reason, String password, Message message) {
		if (!isRoomAvailable()) {
			MultiUserChat.decline(conn, room, inviter, reason);
		}
		// UI update
		// If accepted and available slot, create new MUC and join conferences
		
		// If declined or no slot, send rejection
		MultiUserChat.decline(conn, room, inviter, reason);
	}
    
    
}
