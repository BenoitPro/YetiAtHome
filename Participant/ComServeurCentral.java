import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

public class ComServeurCentral {
    private Socket              sockCom;
    private ObjectOutputStream  oos;
    private ObjectInputStream   ois;
    private ServeurParticipant  sp;
    
    
    public ComServeurCentral(String adresse, int port) {
        try {
        	System.out.print("ComServeurCentral> Connexion au ServeurCentral ("+adresse+":"+port+")...");
            sockCom = new Socket(adresse, port);
			oos = new ObjectOutputStream(sockCom.getOutputStream());
			ois = new ObjectInputStream(sockCom.getInputStream());
            System.out.println("OK");
            
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    
    /* Méthode qui demande NbIP au serveur central */
    public Vector demandeIP( int NbIP ){
        Vector listeIP = new Vector();
        try {
        	System.out.print("ComServeurCentral(demandeIP)> Demande de "+NbIP+"...");
            /* On transmet le nombre d'ip nécessaire */
            oos.writeObject(String.valueOf(NbIP));
            
            /* On attend la réponse sous forme d'ArrayList */
            listeIP =(Vector)ois.readObject();
            System.out.println("ComServeurCentral(demandeIP)> Liste d'ip reçu.");
            
        } catch (Exception e) { e.printStackTrace(); }
        
        return listeIP;
    }
    
    
    public void definirEtat(boolean disponible) {
        try {
            if ( disponible ) oos.writeObject("disp");
            else oos.writeObject("ndis");
        }catch (Exception e) {e.printStackTrace();}
    }
    
    
    public void deconnexion(){
        try {
            oos.writeObject("quit");
            sp.closeThread();
        }catch (Exception e) {e.printStackTrace();}
    }
}
