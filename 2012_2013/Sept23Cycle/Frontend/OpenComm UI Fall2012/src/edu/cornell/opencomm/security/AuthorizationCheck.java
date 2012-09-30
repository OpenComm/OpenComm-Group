package edu.cornell.opencomm.security;

/**
 * 
 * Use to check if users are allowed to do what they are requesting to do.
 *
 */
public class AuthorizationCheck {

	/**
	 * Possible events that require authorization checks.
	 */
	public static final int CAN_CREATE_CONFERENCE = 0;
	public static final int CAN_INVITE_USERS_TO_MAIN = 1;
	public static final int CAN_INVITE_USERS_TO_SIDE = 2;
	public static final int CAN_REMOVE_USERS_FROM_MAIN = 3;
	public static final int CAN_REMOVE_USERS_FROM_SIDE = 4;
	public static final int CAN_UPDATE_INFO = 5;
	public static final int CAN_JOIN_CONFERENCE = 6;
	public static final int CAN_PASS_MODERATOR_PRIVILEGES = 7;
	public static final int CAN_LEAVE_CONFERENCE_MAIN = 8;
	public static final int CAN_LEAVE_CONFERENCE_SIDE = 9;
	public static final int CAN_EDIT_CONFERENCE = 10;
	
	/**
	 * TODO: checks whether or not the user has the credentials/privileges that are 
	 * 		required for the corresponding action.
	 * TODO: More parameters will be required for implementation
	 * @param event the event that the user is trying to execute
	 * @return a boolean true if the user is allowed to perform the event
	 * 		or false if the user is not allowed to perform the event
	 */
	public boolean checkAuthRules(int event){
		switch(event){
		case CAN_CREATE_CONFERENCE:
			break;
		case CAN_INVITE_USERS_TO_MAIN:
			break;
		case CAN_INVITE_USERS_TO_SIDE:
			break;
		case CAN_REMOVE_USERS_FROM_MAIN:
			break;
		case CAN_REMOVE_USERS_FROM_SIDE:
			break;
		case CAN_UPDATE_INFO:
			break;
		case CAN_JOIN_CONFERENCE:
			break;
		case CAN_PASS_MODERATOR_PRIVILEGES:
			break;
		case CAN_LEAVE_CONFERENCE_MAIN:
			break;
		case CAN_LEAVE_CONFERENCE_SIDE:
			break;
		case CAN_EDIT_CONFERENCE:
			break;
		}
		return false;
	}
}
