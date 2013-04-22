package edu.cornell.opencomm.model;

import java.util.ArrayList;

public class InvitationsList {

	private static volatile InvitationsList instance = null;

	private static ArrayList<Invitation> invitationsList = new ArrayList<Invitation>();

	/**
	 * Object constructor
	 */
	private InvitationsList() {
		super();
	}

	/**
	 * Method allowing to return an instance of the InvitationsList class
	 * @return Returns instance of InvitationsList
	 */
	public final static InvitationsList getInstance() {

		if (InvitationsList.instance == null) {

			synchronized (InvitationsList.class) {
				if (InvitationsList.instance == null) {
					InvitationsList.instance = new InvitationsList();
				}
			}
		}
		return InvitationsList.instance;
	}

	public void addInvitation(Invitation invitation) {
		invitationsList.add(invitation);
	}

	public void removeProcessedInvitations() {
		for (Invitation invitation : invitationsList) {
			if (invitation.processed = true) {
				invitationsList.remove(invitation);
			}
		}
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

}
