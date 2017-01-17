import java.util.*;

public class ServeurCentral {
    
    /* Gestion des serveurs d'écoute */
    private ServeurEcouteClient         sec;
    private ServeurEcouteParticipants   sep;
    
    /* Gestion des participants */
    public LesParticipants             P;
    private ArrayList                   ParticipantsOnline;
    
    
    /* Constructeur permettant le démarrage de tous les modules
     * du serveur central
     */
    public ServeurCentral(int clientPort, int participantPort) {
        
    	
        /* Instanciation des objets de gestion des participants */
        ParticipantsOnline = new ArrayList();   //Adresse des participants en ligne
        P = new LesParticipants();              //Fiche des participants
        
        /* Démarrage du serveur d'écoute de client */
        sec = new ServeurEcouteClient(clientPort, this);
        sec.start();
        
        /* Démarrage du serveur d'écoute de participant */
        sep = new ServeurEcouteParticipants(participantPort, this);
        sep.start();
        
    }
    
    // Ajoute un participant à la liste
    public void ajouterParticipant(String adresse)
    {
    	
    	Participant p = P.ajouterP(adresse);
    	System.out.println("ServeurCentral(ajouterParticipant)> Le participant suivant à été ajouter : \n"+p);
    	
    	System.out.println("\n---------------------\nRappel de la liste des participants : \n------------------------");
    	System.out.println(P);
    }
    /* Benoit à voir :
     * nouvelleConnexion()
     * nouveauCalcul()
     * deconnexion()
     */
    
    public void nouvelleConnexion(String adresse, ThreadTraitementParticipant ttp) {
        
    }
    
    /* Méthode apellée par un ThreadClient et qui attend qu'on lui retourne
     * solution du calcul
     */
    public void nouveauCalcul(String calcul, ThreadClient source) {
       
    	System.out.println("ServeurCentral(nouveauCalcul)> Reception du calcul : "+calcul);
    	String res = "";
    	
        /* On choisi un participant dispo */
        /* Benoit définir la politique du choix */
    	// On selectionne le meilleur participant 
    	// (selon la méthode LesParticipants.getBestParticipantDispo() )
        Participant p = P.getBestParticipantDispo();
        
        // Passage du participant choisi en mode non dispo
        //p.setEtat("ndis"); *** C'est la participant ki fai ca tou seul
        
        if (p != null) { // S'il existe au moin un participant dispo
        
        /* On lance un nouveau Thread qui va s'occuper 
         * d'envoyer le calcul aux participants élu  et attendre la réponse
         */
        System.out.println("ServeurCentral(nouveauCalcul)> Le participant "+p.getAdresse()+" à été choisi pour effectué le calcul");
        
        // Création d'une connexion vers le participant, qui enverra directement 
        // le résultat au ThreadClient aproprié (car on le passe en param : source)
        ThreadTraitementParticipant ttp = new ThreadTraitementParticipant(p.getAdresse(), source, calcul,this);
        ttp.start();
        
      /* Testage temporaire
       *   System.out.println("On renvoie vite fait le calcul au client");
       
        source.resultat = "10";
        // source.attendre = false;
       */
         
        
        }
        else {
        	System.out.println("ServeurCentral(nouveauCalcul)> Aucun participant n'est dispo !!!!!!!!!");
        }
    }
    
    // Renvoi une ArrayList d'ip de participant dispo
    public Vector demandeAide(int nIP) {
    	System.out.println("ServeurCentral(demandeAide)> Récupération de "+nIP+" adresse(s) de participant");
        Vector listeIP = new Vector();
        
        // On recupere nIP participant parmis les disponible
        listeIP = P.getNParticipantDispo(nIP);
        
        return listeIP;
    }
    
    /* Deconnexion d'un participant */
    public void deconnexion(String adresse) {
    	
    	System.out.println("ServeurCentral(deconnexion)> Le participant "+adresse+" c'est déconnecté");
        
        P.setParticipantDeco(adresse);
    }
    
    
    public static void main(String args[]) {

    	System.out.println("ServeurCentral(main)> Lancement du serveur");
    	// Lancement du serveur central sur les ports 2000 et 2001
        ServeurCentral sc = new ServeurCentral(2000,2001);
    }
    
}
