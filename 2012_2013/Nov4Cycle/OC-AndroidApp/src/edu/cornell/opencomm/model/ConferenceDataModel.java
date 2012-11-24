package edu.cornell.opencomm.model;

import java.util.HashMap;

import android.util.Log;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

public class ConferenceDataModel {
	
	/**
	 * The map to hold chat space data model 
	 * KEY: ChatSpace Room ID
	 * VALUE: The model associated with the chat
	 */
	HashMap<String, ConferenceRoom> chatSpaceIDMap;
	
	/**
	 * The map to hold chat space models in relation
	 * to each other.
	 * KEY: ChatSpace Room ID (as assigned when initializing
	 * a MUC
	 * VALUE: name of chatspace, e.g., MAIN, LEFT, RIGHT
	 */
	HashMap<String, String> chatSpaceLocationMap;
	
	String activeChat; /** This is the ID of the current room */
	
	User mod;
	
	String mainID;
	String leftID;
	String rightID;
	
	boolean isMain;	//if the space is main chat set this to true
	public ConferenceDataModel(String mainSpaceRoomID, String leftChatID, String rightChatID, User u){
		chatSpaceIDMap = new HashMap<String, ConferenceRoom>();
		chatSpaceLocationMap = new HashMap<String, String>();
		activeChat = mainSpaceRoomID;
		isMain=true;
		mod = u;
		mainID = mainSpaceRoomID;
		leftID = leftChatID;
		rightID = rightChatID;
	}
	
	public void initialize(boolean isMod){
		if(isMod){
			ConferenceRoom csm1 = new ConferenceRoom(NetworkService.getInstance().getConnection(), mainID, UserManager.PRIMARY_USER);
			csm1.init(true);
			chatSpaceIDMap.put(mainID, csm1);
			chatSpaceLocationMap.put(mainID, "MAIN");
		
			ConferenceRoom csm2 = new ConferenceRoom(NetworkService.getInstance().getConnection(), leftID, UserManager.PRIMARY_USER);
			csm2.init(true);
			chatSpaceIDMap.put(leftID, csm2);
			chatSpaceLocationMap.put(leftID, "LEFT");
		
			ConferenceRoom csm3 = new ConferenceRoom(NetworkService.getInstance().getConnection(), rightID, UserManager.PRIMARY_USER);
			csm3.init(true);
			chatSpaceIDMap.put(rightID, csm3);
			chatSpaceLocationMap.put(rightID, "RIGHT");
		}
		else{
			ConferenceRoom csm1 = new ConferenceRoom(NetworkService.getInstance().getConnection(), mainID, UserManager.PRIMARY_USER);
			csm1.init(false);
			chatSpaceIDMap.put(mainID, csm1);
			chatSpaceLocationMap.put(mainID, "MAIN");
			
			ConferenceRoom csm2 = new ConferenceRoom(NetworkService.getInstance().getConnection(), leftID, UserManager.PRIMARY_USER);
			csm2.init(false);
			chatSpaceIDMap.put(leftID, csm2);
			chatSpaceLocationMap.put(leftID, "LEFT");
			
			ConferenceRoom csm3 = new ConferenceRoom(NetworkService.getInstance().getConnection(), rightID, UserManager.PRIMARY_USER);
			csm3.init(false);
			chatSpaceIDMap.put(rightID, csm3);
			chatSpaceLocationMap.put(rightID, "RIGHT");
		}
	}
	
	public void setMod(User u){
		mod = u;
	}
	
	public User getMod(){
		return mod;
	}
	
	public void setActiveChat(String newActiveRoomID){
		activeChat = newActiveRoomID;
	    isMain=true;
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
    	return chatSpaceIDMap;
    }
    
    public HashMap<String, String> getLocationMap(){
    	return chatSpaceLocationMap;
    }
    
}
