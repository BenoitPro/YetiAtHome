import java.net.*;
import java.io.*;

public class ThreadTraitementParticipant extends Thread {
    private ServeurCentral      servCentr;
    private Socket              sockCom;
    private ObjectOutputStream  oos;
    private ObjectInputStream   ois;
    public  String              calcul;
    public  String              adresse;
    private ThreadClient        source;
    
    /* Protocole :
     * On se connecte au participant sur le port 2002
     * On envoi un calcul (String)
     * On attend la réponse (String)
     * On ferme les flux et le socket
     */    
    
    public ThreadTraitementParticipant(String adresse, ThreadClient source, String calcul, ServeurCentral servCentr) {
        this.calcul = calcul;
        this.source = source;
        this.adresse = adresse;
        this.servCentr = servCentr;
    }
    
    
    public void run() {
    	
    	 try {
         	System.out.print("ThreadTraitementParticipant> Connexion au Participant "+adresse+ " sur le port 2002...");
             sockCom = new Socket(adresse, 2002);
             System.out.println(" OK");
         } catch (Exception e) { e.printStackTrace(); }
    	
        String res = "";
        try {
            oos = new ObjectOutputStream(sockCom.getOutputStream());
            ois = new ObjectInputStream(sockCom.getInputStream());
            
            long debut = System.currentTimeMillis();
            
            /* On envoi le calcul */
            oos.writeObject(calcul);
            oos.flush();
            
            /* On attend le résultat */
            res = (String)ois.readObject();
            
            long fin = System.currentTimeMillis();
            long temps = fin - debut;
          
            servCentr.P.setTemps(adresse, temps);
            
            System.out.println("ThreadTraitement(run)> Temps mit par le participant "+adresse+ " pour effectuer le calcul : "+temps+" ms.");
            
            /* On place le résultat dans le thread source */
            source.resultat = res;
            
            /* On stop l'attente de la source */
            source.attendre = false;
            
            oos.close();
            ois.close();
            sockCom.close();
        } catch ( Exception e ) {e.printStackTrace();}
    }
}