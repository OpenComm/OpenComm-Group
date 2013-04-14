package edu.cornell.opencomm.model;

import java.util.ArrayList;

public class InvitationsList {

	private static volatile InvitationsList instance = null;

	private static ArrayList<Invitation> invitationsList = new ArrayList<Invitation>();

	/**
	 * Constructeur de l'objet.
	 */
	private InvitationsList() {
		super();
	}

	/**
	 * MŽthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
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

}
