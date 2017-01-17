import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.StringTokenizer;

public class ServeurParticipant extends Thread {
	private ServerSocket servSock;

	private Socket sockCom;

	private ObjectOutputStream oos;

	private ObjectInputStream ois;

	private ComServeurCentral csc;

	private int port;

	private int nbNombreParParticipant;

	private ArrayList resultatsPartiel;

	/*
	 * Protocole : On lance le socket serveur en attente d'un client ( soit le
	 * serveur central, soit un autre participant ) Une seule connexion à la
	 * fois On attend un calcul on renvoi à la source le résultat
	 */
	public ServeurParticipant(int port, ComServeurCentral csc,
			int nbNombreParParticipant) {
		this.csc = csc;
		this.port = port;
		this.nbNombreParParticipant = nbNombreParParticipant;
		this.resultatsPartiel = new ArrayList();
	}

	// Lancement de l'écoute
	public void run() {
		try {
			System.out.print("ServeurParticipant> Mise en ecoute du port "
					+ port + "...");
			servSock = new ServerSocket(port);
			System.out.println(" OK");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String calcul, resultat;
		try {
			while (true) {
				/* Ouverture du Socket et des flux liés à celui-ci */
				sockCom = servSock.accept();
				oos = new ObjectOutputStream(sockCom.getOutputStream());
				ois = new ObjectInputStream(sockCom.getInputStream());

				// On previent le serveur qu'on (le participant) est maintenant
				// indisponible
				csc.definirEtat(false);

				/* On se met en attente du calcul à réaliser */
				calcul = (String) ois.readObject();

				System.out.println("ServeurParticipant(run)> Calcul recu "
						+ calcul);
				resultat = traiterCalcul(calcul);

				/* On transmet le résultat à la source */
				oos.writeObject(resultat);
				oos.flush();

				// On previent le serveur qu'on (le participant) est maintenant
				// Disponible
				csc.definirEtat(true);

				/* On ferme tout */
				oos.close();
				ois.close();
				sockCom.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Traite un calcul recu sous forme de string */
	public String traiterCalcul(String calcul) {

		if (calcul.substring(0, 3).equals("som")) {
			return traiterCalculSomme(calcul);
		} else if (calcul.substring(0, 3).equals("mat")) {
			return traiterCalculMatrice(calcul);
		} else if (calcul.substring(0, 2).equals("sp")) {
			return traiterCalculSommeProduits(calcul);
		} else
			return "Protocol non repecté : " + calcul;
	}

	public String traiterCalculSomme(String calcul) {

		double somme = 0;
		int nombreIP; // Nombre de participant voulu pour ce calcul
		String res = "pas de resultat";

		StringTokenizer st = new StringTokenizer(calcul, " ");
		String s = st.nextToken();

		// 1) On détermine le nombre d'ip qu'il va falloir pour faire ce calcul
		nombreIP = (int) Math.ceil((double) (st.countTokens() - 1)
				/ (double) nbNombreParParticipant) - 1;

		if (nombreIP == 0) { // Réalisation sans aide du calcul
			somme = calculSommePartielle(calcul);
		} else { // Si besoin d'aide

			// Demande de nombreIP participant pour effectué ce gros calcul
			System.out.println("ServeurParticipant(traiterCalcul)> Demande de "
					+ nombreIP + " participant(s).");
			Vector listeIP = new Vector(csc.demandeIP(nombreIP));
			System.out
					.println("ServeurParticipant(traiterCalcul)> Liste d'ip reçu.");
			for (int i = 0; i < listeIP.size(); i++) {
				System.out.println("ip : " + listeIP.get(i));
			}

			// Ajout du participant pour calculé lui aussi 
			listeIP.add("127.0.0.1");
			
			// 2) DECOUPAGE du calcul

			ArrayList calculs = new ArrayList(); // liste de calcul

			System.out
					.println("ServeurParticipant(traiterCalcul)> Découpage du calcul");

			for (int i = 0; i < nombreIP + 1; i++) {
				String miniCalcul = "som"; // Morceau de calcul a envoyer a
				// un autre participant
				int j = 0;
				while (j < nbNombreParParticipant && st.hasMoreTokens()) {
					s = st.nextToken();
					// System.out.println("ServeurParticipant(traiterCalcul)>
					// Découpage du calcul");
					miniCalcul += " " + s;
					j++;
				}
				System.out
						.println("ServeurParticipant(traiterCalcul)> Ajout du miniCalcul "
								+ i + " : " + miniCalcul);
				calculs.add(miniCalcul);

			}
			System.out
					.println("ServeurParticipant(traiterCalcul)> Envoyer chaque calcul sauf le 1ier au(x) participant(s) .........");
		
			System.out
			.println("ServeurParticipant(traiterCalcul)> J'ai demandé "
					+ nombreIP + " participant(s)");
	System.out.println("ServeurParticipant(traiterCalcul)> J'ai recu "
			+ listeIP.size() + " participant(s) dispo");
	
			// 3) Envoi du calcul aux autres participants		
			distribuerCalculs(calculs,listeIP);

			// 4) Fait la somme de toutes les sommes partielles
			System.out
					.println("ServeurParticipant(traiterCalcul)> Calcul de toutes les sommes partielles");
			somme = 0;
			for (int i = 0; i < resultatsPartiel.size(); i++) {
				somme += Double.parseDouble((String) resultatsPartiel.get(i));
			}

		} // fin besoin d'aide

		// 5) Renvoi de la somme enfin calculé !
		res = String.valueOf(somme);
		System.out.println("Terminé ! " + calcul + " = " + somme);
		return res;

	}

	/**
	 * Réalise un produit matriciel
	 * 
	 * @param calcul
	 *            contient les 2 matrices a multiplier sous forme de String
	 * @return La matrice résultat sous forme de String
	 */
	public String traiterCalculMatrice(String calcul) {

		// On récupere les deux matrices contenues dans la chaine
		int[][] M1 = TraiterMatrice.toMatrice(1, calcul);
		int[][] M2 = TraiterMatrice.toMatrice(2, calcul);

		// On initialise la taille de la Matrice Résultat (MR)
		int nbL = M1.length;
		int nbC = M2[0].length;
		int[][] MR = new int[nbL][nbC];

		// Si la MR a une taille superieur a 2x2 on demande de l'aide
		if (nbL > 2 && nbC > 2) // Besoin d'aide
		{
			// 1) On détermine le nombre de participant qui vont nous aidé
			// On demande autant de participant qu'il y a de ligne ds MR *****
			int nombreIP = nbL;
			Vector listeIP; // Liste d'ip qu'on peut appeler
			
			// Demande de nombreIP participant pour effectué ce gros calcul
			System.out.println("ServeurParticipant(traiterCalcul)> Demande de "
					+ nombreIP + " participant(s).");
			listeIP = new Vector(csc.demandeIP(nombreIP));
			
			System.out
			.println("ServeurParticipant(traiterCalcul)> J'ai demandé "
					+ nombreIP + " participant(s)");
	System.out.println("ServeurParticipant(traiterCalcul)> J'ai recu "
			+ listeIP.size() + " participant(s) dispo : ");
	
			System.out
					.println("ServeurParticipant(traiterCalcul)> Liste d'ip reçu.");
			// Affichage des ip qui vont nous aider
			for (int i = 0; i < listeIP.size(); i++) {
				System.out.println("ip : " + listeIP.get(i));
			}
			// Ajout du participant pour calculé lui aussi 
			listeIP.add("127.0.0.1");

			// 2) Découpage du calcul
			// On génére le calcul de chaque element de la Matrice Résultat
			ArrayList calculs = TraiterMatrice.genereSommeProduits(M1, M2);
			
			// 3) Distribution des calculs aux autres participants
			distribuerCalculs(calculs,listeIP);
			
			// 4) Rassemblement des résultats
			// On place chaque element au bonne endroi dans la MR
			for (int i = 0; i < resultatsPartiel.size(); i++) {
				TraiterMatrice.ajoutElement(MR, (String) resultatsPartiel
						.get(i));
			}
		} else {
			System.out
			.println("ServeurParticipant(traiterCalcul)> Pas besoin d'aide pour une matrice résultat plus petite que 2x2");
			System.out
			.print("ServeurParticipant(traiterCalcul)> Calcul en cours...");
	
			// On génére le calcul de chaque element de la Matrice Résultat
			ArrayList l = TraiterMatrice.genereSommeProduits(M1, M2);

			// Pour chaque element de MR on fait le calcul qu'on ajoute a la
			// liste
			// des résultats partiels
			for (int i = 0; i < l.size(); i++) {
				resultatsPartiel.add(TraiterMatrice
						.calculSommeProduits((String) l.get(i)));
			}

			// On place chaque element au bonne endroi dans la MR
			for (int i = 0; i < resultatsPartiel.size(); i++) {
				TraiterMatrice.ajoutElement(MR, (String) resultatsPartiel
						.get(i));
			}System.out.println(" OK");
			
		}

		System.out.println(TraiterMatrice.toString(MR));
		return TraiterMatrice.toString(MR);
	}

	/**
	 * 
	 * @param calcul
	 * @return
	 */
	public String traiterCalculSommeProduits(String calcul) {
		return TraiterMatrice.calculSommeProduits(calcul);
	}
	
	
	public void distribuerCalculs(ArrayList calculs,Vector listeIP)
	{

		int cpt=0; // Compte le nombre de boucle local il y a
					// ce qui permet de lancer un participant
				// sur le serveur central, sans qu'il y est de bug
		for (int i = 0; i < listeIP.size(); i++) {
			if ( ((String)listeIP.get(i)).equals("127.0.0.1"))
				cpt++;
		}
		ClientParticipant[] tabcp = new ClientParticipant[listeIP.size()-cpt];
		System.out
				.println("ServeurParticipant(traiterCalcul)> Lancement de l'aide aux participants");
		for (int i = 0; i < calculs.size(); i++) // Chaque somme
		// partielle est
		// traité
		{
			
			// Si l'ip correspond a la boucle local cad le participant lui
			// meme. Alors on fait le calcul en interne.
			if (((String)listeIP.get(i % listeIP.size())).equals("127.0.0.1"))
			{
				System.out.println("ServeurParticipant(distribuerCalcul)> J'effectue moi meme le calcul : "+(String) calculs
								.get(i));
				// Si c'est une somme
				if( ((String)calculs.get(i)).substring(0, 3).equals("som")  )  // Premiere somme partielle calculée en interne
				resultatsPartiel.add(String
						.valueOf(calculSommePartielle((String) calculs
								.get(i))));
				else if ( ((String)calculs.get(i)).substring(0, 2).equals("sp")  ) 
				{
					resultatsPartiel.add(TraiterMatrice
							.calculSommeProduits((String) calculs.get(i)));
				}
			}
			else 
			{
				// On trouve le participant a qui on va fair fair le
				// calcul
				int participantCourant = i % listeIP.size();

				// Boolean pour savoir si on a deja demandé d'effectuer
				// un calcul a ce participant
				boolean dejaAttribue = ((int) ( i / listeIP.size()) == 0) ? false
						: true;

				if (!dejaAttribue) // S'il s'agit d'un participant dont
				// on a jms attribué de calcul, on
				// le crée
				{
					System.out
							.println("ServeurParticipant(distribuerCalculs)> Envoi du calcul partiel : "
									+ (String) calculs.get(i)
									+ " au participant : "
									+ (String) listeIP
											.get(participantCourant)
									+ " (1ier calcul)");

					tabcp[participantCourant] = new ClientParticipant(
							(String) listeIP.get(participantCourant), port,
							(String) calculs.get(i), this);
					tabcp[participantCourant].start(); // On lance
				} else { // Si on a deja envoyer un calcul au participant
					try {
						System.out
								.print("ServeurParticipant(distribuerCalculs)> Attente du calcul demandé a "
										+ (String) listeIP
												.get(participantCourant)
										+ "...");
						tabcp[participantCourant].join(); // On attend
						// la fin du
						// calcul
						System.out.println(" OK");
					} catch (Exception e) {	e.printStackTrace(); }

					System.out
							.println("ServeurParticipant(distribuerCalculs)> Envoi du calcul partiel : "
									+ (String) calculs.get(i)
									+ " au participant : "
									+ (String) listeIP
											.get(participantCourant)
									+ " (encore lui)");
					tabcp[participantCourant] = new ClientParticipant(
							(String) listeIP.get(participantCourant), port,
							(String) calculs.get(i), this); // On lui donne
					// le nouveau
					// calcul

					tabcp[participantCourant].start(); // On le lance
				}
			}

		}
		// Attente de la fin des calculs de tous les participants
		// avant de pouvoir fair la somme des sommes partielles
		try {
			for (int i = 0; i < tabcp.length; i++) {

				System.out
						.print("ServeurParticipant(distribuerCalculs)> Attente dernier calcul demandé a "
								+  tabcp[i].adresse + "...");

				tabcp[i].join(); // On attent la fin de chaque
									// participant

				System.out.println(" OK");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double calculSommePartielle(String calcul) {
		double somme = 0;
		StringTokenizer st = new StringTokenizer(calcul, " ");
		String s = st.nextToken();

		// Traitement d'une somme
		if (s.equals("som")) {

			System.out
					.print("ServeurParticipant(calculSommePartielle)> Calcul de la somme partielle...");
			while (st.hasMoreTokens()) {
				s = st.nextToken();
				somme += Integer.parseInt(s);
			}
			System.out.println(" OK");
		} else {
			System.out
					.println("ServeurParticipant(calculSommePartielle)> IL NE S'AGIT PAS D'UNE SOMME : "
							+ calcul);
		}

		return somme;
	}

	public void retourCalculPartiel(String calculPartiel) {
		/*
		 * on rempli une arrayList qui stock les calcul partiel ou alors on
		 * traite au fur et a mesure ...
		 */
		resultatsPartiel.add(calculPartiel);
	}

	public void closeThread() {
		try {
			oos.close();
			ois.close();
			sockCom.close();
			servSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
