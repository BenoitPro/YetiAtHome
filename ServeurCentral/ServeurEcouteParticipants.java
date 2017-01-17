import java.io.*;
import java.net.*;

public class ServeurEcouteParticipants extends Thread {
    
    /* Communication avec les Participants */
    private ServerSocket        sockServParticipant;
    private Socket              sockParticipant;
    private ServeurCentral      servCentr;
    
    
    
    public ServeurEcouteParticipants(int participantPort, ServeurCentral servCentr) {
        this.servCentr = servCentr;
        try {
            sockServParticipant = new ServerSocket(participantPort);
        } catch ( Exception e ) { e.printStackTrace(); }
    }
    
    public void run(){
        String adresse = "";
        try {
            System.out.println("ServeurEcoutePart(run)> Mise en ecoute du serveur de Participant... OK");
            while ( true ) {
                //On attend une connexion
                sockParticipant = sockServParticipant.accept();
                adresse = sockParticipant.getInetAddress().toString();
                if ( adresse.charAt(0) == '/'  ) {
                	adresse = adresse.substring(1);
                }
               // adresse = sockParticipant.getInetAddress().toString();
                

                
                //On créé un nouveau Thread de communication avec le socket client
                ThreadComParticipant thsP = new ThreadComParticipant(sockParticipant, servCentr);
                thsP.start();
                
                // On stock le nouveau Participant (identifier par son adresse)
                servCentr.ajouterParticipant(adresse);

            }
        } catch (Exception e) {e.printStackTrace();}
    }
    
}
