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
	
	public ConferenceDataModel(String mainSpaceRoomID){
		chatSpaceMap = new HashMap<String, ChatSpaceModel>();
		activeChat = mainSpaceRoomID;  /** If we change to constants this will be MAIN */
	}
	
	public void switchActiveChat(String newActiveRoomID){
		activeChat = newActiveRoomID;
	}

}
