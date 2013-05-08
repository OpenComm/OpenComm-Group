package edu.cornell.opencomm.model;

import java.util.ArrayList;

public class InvitationsList {

	private static ArrayList<Invitation> invitationsList = new ArrayList<Invitation>();

	
	public static void addInvitation(Invitation invitation) {
		synchronized(invitationsList) {
			invitationsList.add(invitation);
		}	
	}

	public static int getPosition(String room) {
		int i = 0;
		for (Invitation invitation : invitationsList) {
			if (! invitation.room.equals(room)) {
				i+=1;
			} else {
				break;
			}
		}
		return i;
	}

	public static int getNumberNewInvitations() {
		int i = 0;
		for (Invitation invitation : invitationsList) {
			if (!invitation.processed) {
				i += 1;
			}
		}
		return i;
	}
	
	public static ArrayList<Invitation> getInvitations() {
		return invitationsList;
	}

}
