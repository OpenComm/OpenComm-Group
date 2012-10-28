package edu.cornell.opencomm.model;

import java.util.HashMap;


public class ConferenceDataModel {
	
	/**
	 * The map to hold chat space data model 
	 * KEY: ChatSpace name eg, MAIN,LEFT,RIGHT
	 * VALUE: The model associated with the chat
	 */
	HashMap<String, ChatSpaceModel> chatSpaceMap = new HashMap<String, ChatSpaceModel>();
}
