package edu.cornell.opencomm.model;

import java.util.HashMap;

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
	
	boolean isMain;	//if the space is main chat set this to true
	public ConferenceDataModel(String mainSpaceRoomID){
		chatSpaceIDMap = new HashMap<String, ConferenceRoom>();
		chatSpaceLocationMap = new HashMap<String, String>();
		activeChat = mainSpaceRoomID;
		isMain=true;
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
