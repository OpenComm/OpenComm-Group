package edu.cornell.opencomm.model;

import java.util.HashMap;

public class ConferenceDataModel {
	
	/**
	 * The map to hold chat space data model 
	 * KEY: ChatSpace name eg, MAIN,LEFT,RIGHT
	 * VALUE: The model associated with the chat
	 */
	HashMap<String, ChatSpaceModel> chatSpaceMap;
	
	
	String activeChat; /** This is the ID of the current room (can be changed 
	 					if we want constants MAIN, LEFT, RIGHT */
	boolean isMain;	//if the space is main chat set this to true
	public ConferenceDataModel(String mainSpaceRoomID){
		chatSpaceMap = new HashMap<String, ChatSpaceModel>();
		activeChat = mainSpaceRoomID;  /** If we change to constants this will be MAIN */
	   isMain=true;
	}
	
	public void setActiveChat(String newActiveRoomID){
		activeChat = newActiveRoomID;
	    isMain=true;
	}
	
	public String getActiveChat(){
		return activeChat;
	}
    public boolean getIsmain(){
    	return isMain;
    }
    
    public void setIsmain(boolean s){
    isMain=s;
    	
    }

    public HashMap<String, ChatSpaceModel> getHashmap(){
    	return chatSpaceMap;
    }
    
}
