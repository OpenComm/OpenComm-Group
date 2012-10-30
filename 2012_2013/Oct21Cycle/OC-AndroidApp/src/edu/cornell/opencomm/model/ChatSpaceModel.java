package edu.cornell.opencomm.model;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class ChatSpaceModel extends MultiUserChat{
	
	//TODO:Is it a good idea to extend MUC?
	//Ask Risa and Kris
	//How does MUC get list of occupants
	/**
	 * 
	 */
	HashMap<String, User> userMap = new HashMap<String, User>();
	
	public ChatSpaceModel(Connection arg0, String arg1) {
		super(arg0, arg1);
		
		
		
		
		// TODO Auto-generated constructor stub
	}
	

} 