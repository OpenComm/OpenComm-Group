package edu.cornell.opencomm.model;

import java.util.ArrayList;

public class InvitationsList {
	
	// L'utilisation du mot clé volatile permet, en Java version 5 et supérieur, d'éviter le cas où "Singleton.instance" est non-nul,
    // mais pas encore "réellement" instancié.
    // De Java version 1.2 à 1.4, il est possible d'utiliser la classe ThreadLocal.
    private static volatile InvitationsList instance = null;
	
	private static ArrayList<Invitation> invitationsList = new ArrayList();
	
	/**
     * Constructeur de l'objet.
     */
    private InvitationsList() {
        // La présence d'un constructeur privé supprime le constructeur public par défaut.
        // De plus, seul le singleton peut s'instancier lui même.
        super();
    }

    /**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static InvitationsList getInstance() {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet d'éviter un appel coûteux à synchronized, 
        //une fois que l'instanciation est faite.
        if (InvitationsList.instance == null) {
           // Le mot-clé synchronized sur ce bloc empêche toute instanciation multiple même par différents "threads".
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
