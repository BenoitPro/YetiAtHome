public class MainParticipant {
    
//	 1ier arg, l'ip du ServeurCentral
//	 2ieme arg, port du ServeurCentral
//	 3ieme arg, port du ServeurParticipant pour l'écoute de calcul
    public static void main(String args[]) {
    	
    	// Les port (de connexion et d'écoute)
    	int portSCentral = 2001; // Numero du port de connexion vers le serveur central
    	int  portSParticipant = 2002; //  Numero de port d'ecoute du serveur participant de calcul qu'il faut lancer
    	String adresseSC = "127.0.0.1" ;
    	int nbNombreParParticipant = 10; // Nombre de Nombre par calcul pour chq participant
    	
    	if (args.length >2){
    		adresseSC =  args[0]; // 1ier arg, l'ip du ServeurCentral
    		portSCentral = Integer.parseInt(args[1]);// 2ieme arg, port du ServeurCentral
			portSParticipant =  Integer.parseInt(args[2]); //	 3ieme arg, port du ServeurParticipant pour l'écoute de calcul
    	}
        // Connexion au serveur central 
    	ComServeurCentral csc = new ComServeurCentral(adresseSC, portSCentral);

        
        // Demarrage du serveur de reception de calcul (par le SC ou par un autre participant)
        System.out.print("MainParticipant> Demarrage du serveur de reception...");
        ServeurParticipant sp = new ServeurParticipant(portSParticipant, csc, nbNombreParParticipant);
        sp.start();
        System.out.println(" OK");
    
    }
    
}
