import java.net.*;

public class MainClient {
    
    public static void  main(String args[]) {
        
    	System.out.println("MainClient(static main)>Lancement du client");
        /* Chaque appel de méthode sera fait par la suite dans l'interface ... */
        Client c = new Client("localhost",2000);


        // Si la connexion a fonctionne
        if ( c.connecter() == 0 )
        {
        	//c.sendFile("c:\\somme.txt");
        	c.sendFile("c:\\matrice.txt");
         //   System.out.println( c.sendCalcul("som 1 2 1 3 1") );
          /*  System.out.println( c.sendCalcul("som 1 2 1 3 1") );
            System.out.println( c.sendCalcul("som 1 2 1 3 1") );
            System.out.println( c.sendCalcul("som 1 2 1 3 1") );
            System.out.println( c.sendCalcul("som 1 2 1 3 1") );
            *///System.out.println( c.sendCalcul("som 1 2 1 3 1") );
            c.deconnecter();
        }
        else
            System.out.println("/!\\ Connexion au serveur impossible /!\\");
    }    
}
