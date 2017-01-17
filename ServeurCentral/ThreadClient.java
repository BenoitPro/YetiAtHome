import java.io.*;
import java.net.*;

public class ThreadClient extends Thread {
    private Socket 				sockClient;
    private ObjectOutputStream  oosC;
    private ObjectInputStream   oisC;
    private ServeurCentral      servCentr;
    public  String              resultat;
    public  boolean             attendre;
    
    
    
    public ThreadClient(Socket sockClient, ServeurCentral servCentr) {
    	System.out.println("ThreadClient> Creation d'un ThreadClient");
        attendre = true;
        try {
            this.sockClient = sockClient; // recup la socket vers le client
            this.servCentr = servCentr; // recup la reference vers le SC
        } catch (Exception e) {e.printStackTrace();}
        
    }
    
    
    // Lancement du Thread
    public void run() {
    	System.out.println("ThreadClient(run)> Lancement de la communication avec le client");
        System.out.println(sockClient);
        String recu; // chaine contenant le message recu
        
        try {
        	System.out.print("ThreadClient> Ouverture des flux d'entree et sortie...");
        	// Ouverture d'un flux de sortie vers le client
            oosC = new ObjectOutputStream(sockClient.getOutputStream());
            // Ouverture d'un flux  d'entre vers le client
            oisC = new ObjectInputStream(sockClient.getInputStream());
        	System.out.println("OK");

        	
            while ( true ) {
            	// Reception du message du client
                recu = (String)oisC.readObject();
                System.out.println("ThreadClient(run)> Reception du message "+recu);
// Traitement du message recu
                // Si "quit" alors on ferme la connexion 
                if ( recu.substring(0,4).equals("quit") ) {
                    System.out.println("le client est déco");
                    oosC.close();
                    oisC.close();
                    sockClient.close();
                    this.stop();
                }
                
                // Si "fich" alors on recupere un fichier (Traitement 
                // a fair selon moi coté Client) ***
                // Voir prog test pour lecture dans un fichier
                else if (recu.substring(0,4).equals("fich")) {
                    /* reception du fichier */
                    /* normalization du calcul au format texte */
                    /* on réalise le calcul comme si on avait eu un calcul au format texte */
                }
                
                // Sinon Le client veut envoyer un calcul sous forme d'une chaine */
                else
                {
                	System.out.println("ThreadClient(run)> Envoi du calcul a ServeurCentral");
                    // On transmet le calcul au ServeurCentral
                    servCentr.nouveauCalcul(recu, this);
                    
                    /* On attend que le résultat soit disponible
                     * dans la variable resultat
                     */
                    while ( attendre )
                    {
                    	sleep(100); // jle fai dormir 0.1 sec pour
                    		// pu que le CPU soit a 100%	
                    }
                    
                    // On envoi le résultat au client
                    oosC.writeObject(resultat);
                    oosC.flush();
                    attendre = true;
                }
            }
            
        } catch ( Exception e ){e.printStackTrace();}
    }
}
