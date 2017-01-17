import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class ThreadComParticipant extends Thread {
    private ServeurCentral              servCentr;
    private Socket                      sockCom;
    private ObjectOutputStream          oos;
    private ObjectInputStream           ois;
    private String						ip;
    
    public ThreadComParticipant(Socket sockParticipant, ServeurCentral servCentr) {
        this.servCentr = servCentr;
        this.sockCom = sockParticipant;
        this.ip = sockCom.getInetAddress().toString();
        if ( ip.charAt(0) == '/'  ) {
        	this.ip = ip.substring(1);
        }
        
        System.out.println("ThreadComParticipant> Connexion avec le participant : "+ip+":"+ sockCom.getPort());
  
    }
    
    
    public void run() {
        boolean connecte = true;
        String recu = "";
        
        try {
            oos = new ObjectOutputStream(sockCom.getOutputStream());
            ois = new ObjectInputStream(sockCom.getInputStream());
        
        /* Nous sommes en attente de requetes venant du participant */
        /* Benoit : traitement interne des requetes ;)
         * et si t'en voi d'autre ya pas dsouci :p
         * Logikement le serveur ne lui répond rien dans les requetes que g fai
         * sinon c ois.writeObject( new String("requete") )
         * d'ailleur suprimer ois si on s'en réellement pas
         */
            
            while ( connecte ) {
                recu = (String)ois.readObject();
                
                // Deconnexion du participant
                if ( (recu.length() == 4) && (recu.substring(0,4).equals("quit")) ) {
                    System.out.println("Deconnexion du Participant "+sockCom);
                    servCentr.deconnexion(ip);
                    oos.close();
                    ois.close();
                    sockCom.close();
                    this.stop();
                }
                
                // Le participant signal sa disponibilité
                else if ( (recu.length() >= 4) && (recu.substring(0,4).equals("disp")) ) {
                	servCentr.P.setParticipantDispo(ip);
                }
                
                // Le participant signal sa non-disponibilité
                else if ( (recu.length() >= 4) && (recu.substring(0,4).equals("ndis")) ) {
                    servCentr.P.setParticipantIndispo(ip);
                }
                
                // Le participant à besoin d'aide
                else {
                    int nbIP = 0;
                    // Conversion de la chaine en un nombre (bourrin) **********
                    nbIP = Integer.parseInt(recu);
                    Vector listeIP =(Vector) servCentr.demandeAide(nbIP);
                    
                    oos.writeObject(listeIP);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
    }
}
