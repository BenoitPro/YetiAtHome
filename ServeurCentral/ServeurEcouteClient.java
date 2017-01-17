import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServeurEcouteClient extends Thread {
    
    /* Communication avec les clients */
    private ServerSocket                sockServClient; // socket d'écoute de l'arriver d'un client
    private Socket                      sockClient; // socket de communication vers le client
    private ServeurCentral              servCentr; // reference vers le serveur central
    
    
    public ServeurEcouteClient(int port, ServeurCentral servCentr) {
        this.servCentr = servCentr;
        try {         
        System.out.print("ServeurEcouteClient(run)> Mise en ecoute du serveur de Client...");
            sockServClient = new ServerSocket(port);
            System.out.println(" OK");
        } catch ( Exception e ) { e.printStackTrace(); }
    }
    
    public void run() {
        try {

            while ( true ) {
                //On attend une connexion
                sockClient = sockServClient.accept();
                System.out.println("ServeurEcouteClient(run)> Un client viens d'arriver !! creation de son ThreadClient");
                               
                //On créé un nouveau Thread de communication avec le socket client
                ThreadClient thsC = new ThreadClient(sockClient, servCentr);
                thsC.start();
                
            }
        } catch (Exception e) {e.printStackTrace();}
        
    }
    
    
}


