package edu.cornell.opencomm.security;

import edu.cornell.opencomm.R;

/**
 * For Client- and Server- Side.
 * Use to check if users are allowed to do what they are requesting to do.
 *
 */
public class AuthorizationCheck {
	
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
		case R.integer.CAN_CREATE_CONFERENCE:
			break;
		case R.integer.CAN_INVITE_USERS_TO_MAIN:
			break;
		case R.integer.CAN_INVITE_USERS_TO_SIDE:
			break;
		case R.integer.CAN_REMOVE_USERS_FROM_MAIN:
			break;
		case R.integer.CAN_REMOVE_USERS_FROM_SIDE:
			break;
		case R.integer.CAN_UPDATE_INFO:
			break;
		case R.integer.CAN_JOIN_CONFERENCE:
			break;
		case R.integer.CAN_PASS_MODERATOR_PRIVILEGES:
			break;
		case R.integer.CAN_LEAVE_CONFERENCE_MAIN:
			break;
		case R.integer.CAN_LEAVE_CONFERENCE_SIDE:
			break;
		case R.integer.CAN_EDIT_CONFERENCE:
			break;
		}
		return false;
	}
}
