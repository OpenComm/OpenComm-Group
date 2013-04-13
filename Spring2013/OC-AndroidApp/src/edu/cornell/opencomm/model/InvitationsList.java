package edu.cornell.opencomm.model;

import java.util.ArrayList;

public class InvitationsList {
	
	// L'utilisation du mot cl� volatile permet, en Java version 5 et sup�rieur, d'�viter le cas o� "Singleton.instance" est non-nul,
    // mais pas encore "r�ellement" instanci�.
    // De Java version 1.2 � 1.4, il est possible d'utiliser la classe ThreadLocal.
    private static volatile InvitationsList instance = null;
	
	private static ArrayList<Invitation> invitationsList = new ArrayList();
	
	/**
     * Constructeur de l'objet.
     */
    private InvitationsList() {
        // La pr�sence d'un constructeur priv� supprime le constructeur public par d�faut.
        // De plus, seul le singleton peut s'instancier lui m�me.
        super();
    }

    /**
     * M�thode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static InvitationsList getInstance() {
        //Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet d'�viter un appel co�teux � synchronized, 
        //une fois que l'instanciation est faite.
        if (InvitationsList.instance == null) {
           // Le mot-cl� synchronized sur ce bloc emp�che toute instanciation multiple m�me par diff�rents "threads".
           // Il est TRES important.
           synchronized(InvitationsList.class) {
             if (InvitationsList.instance == null) {
            	 InvitationsList.instance = new InvitationsList();
             }
           }
        }
        return InvitationsList.instance;
    }
    
    public void addinvitation(Invitation invitation) {
    	invitationsList.add(invitation);
    }
	
}
