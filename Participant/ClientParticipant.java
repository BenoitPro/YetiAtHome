import java.net.*;
import java.io.*;

/**
 * Cette classe permet de distribuer une portion de calcul
 * à un autre participant.
 */
public class ClientParticipant extends Thread {
    
    private Socket              sockCom;
    private ObjectOutputStream  oos;
    private ObjectInputStream   ois;
    private ServeurParticipant  sp;
    private String              calcul;
    public String 				adresse;
    private int 				port;
    
    // Constructeur
    public ClientParticipant(String adresse, int port, String calcul, ServeurParticipant sp) {
        this.sp = sp;
        this.calcul = calcul;
        this.adresse = adresse;
        this.port = port;
    }
    
 // SETTER a enlever ****
    public void setCalcul(String calcul){ this.calcul = calcul;}
    
    
    public void run() {
    	
        try {
        	System.out.print("ClientParticipant(run)> Connexion au participant "+adresse+":"+port+"...");
            sockCom = new Socket(adresse, port);
            oos = new ObjectOutputStream(sockCom.getOutputStream());
            ois = new ObjectInputStream(sockCom.getInputStream());

        } catch ( Exception e ) {e.printStackTrace(); }
        System.out.println(" OK");
        
        String resuPartiel = "";
        try {
            //On envoi le calcul
            oos.writeObject(calcul);
            oos.flush();
            
            //On attend la réponse
            resuPartiel = (String)ois.readObject();
            
            //On transmet à la classe ServeurParticipant le résultat partiel
            sp.retourCalculPartiel(resuPartiel);
        
        /* On ferme tout */
		oos.close();
		ois.close();
		sockCom.close();
        } catch ( Exception e ) {e.printStackTrace(); }
        
    }
}
