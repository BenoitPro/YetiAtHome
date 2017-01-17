import java.util.*;

public class LesParticipants {

	private ArrayList fichesParticipant;

	// Constructeur
	public LesParticipants() {
		fichesParticipant = new ArrayList();
	}

	// Ajoute un participant (sous la forme de son ip), et on le renvoi
	public Participant ajouterP(String ip) {

		Participant p = getParticipantByAdresse(ip);

		if (p == null) {// Si le participant n'existe pas deja
			p = new Participant(ip); // creation du nouveau participant
			fichesParticipant.add(p); // ajout dans la liste
		} else {

			System.out
					.println("LesParticipants(ajouterP)> Le participant est deja connu, on passe son etat à disp");
			p.setEtat("disp");
		}
		return p; // on renvoi le participant qui vien d'etre ajouter.

	}

	// getBestParticipantDispo(), recupere le
	// meilleur participant disponible
	// Algo :
	// Parmis les participants qui on une note
	// superieur a 15, on prend le plus rapide
	// Si pas de note supérieur a 15, on prend celui
	// qui a la meilleur note
	public Participant getBestParticipantDispo() {
		// Recuperation des participants dispo
		ArrayList dispos = getParticipantDispo();
		if (dispos.size() == 0) {
			System.out
					.println("LesParticipants(getBestParticipantDispo)> Aucun participant n'est disponible");
			return null;
		} else {
			// Meilleur participant
			double meilleurTemps = 2000000000;
			Participant bestp = null;

			// Participant avec le meilleur temps (pris s'il n'existe aucun
			// participant avec une note > à 15)
			Participant bestpt = (Participant) dispos.get(0);

			for (int i = 0; i < dispos.size(); i++) {
				Participant p = (Participant) dispos.get(i);

				if (p.calculNoteMoyenne() > 14) {
					if (meilleurTemps > p.calculTempsMoyen()) {
						meilleurTemps = p.calculTempsMoyen();
						bestp = p;
					}
				}
				// On recupere le participant le plus rapide
				if (p.calculTempsMoyen() < bestpt.calculTempsMoyen()) {
					bestpt = p;
				}
			}

			if (bestp == null) // S'il n'y avai aucun prticipant avec une note
								// > 15
				return bestpt; // on renvoi le participant le plus rapide (ou
								// le 1ier)
			return bestp; // Sinon parmis les participants avec une note > à
							// 15 on renvoi
			// le plus rapide

		}
	}

	// Renvoi la liste des participants disponible
	public ArrayList getParticipantDispo() {
		ArrayList l = new ArrayList();
		for (int i = 0; i < fichesParticipant.size(); i++) {
			Participant p = (Participant) fichesParticipant.get(i);
			if (p.getEtat().equals("disp")) {
				l.add(p);
			}
		}
		return l;
	}

	// Définie disponible un participant identifié par son ip 
	public void setParticipantDispo(String adresse){
		Participant p = getParticipantByAdresse(adresse);
		if (p != null) {
				p.setEtat("disp");
				System.out.println("LesParticipants(setParticipantDispo)> Participant "+adresse+" disponible");
		}
		else
			System.out.println("LesParticipants(setParticipantIndispo)> Aucun participant trouve avec l'ip : "+adresse);
		

	}
	
	// Définie indisponible un participant identifié par son ip 
	public void setParticipantIndispo(String adresse){
		Participant p = getParticipantByAdresse(adresse);
		if (p != null){
				p.setEtat("ndis");	
				System.out.println("LesParticipants(setParticipantIndispo)> Participant "+adresse+" indisponible");
	}
		else
			System.out.println("LesParticipants(setParticipantIndispo)> Aucun participant trouve avec l'ip : "+adresse);
		
	}	
	
	// Défini deconnecté un participant identifié par son ip 
	public void setParticipantDeco(String adresse){
		Participant p = getParticipantByAdresse(adresse);
		if (p != null) {
				p.setEtat("deco");
				System.out.println("LesParticipants(setParticipantIndispo)> Participant "+adresse+" déconnecté");
	}
		else
			System.out.println("LesParticipants(setParticipantIndispo)> Aucun participant trouve avec l'ip : "+adresse);
		

	}
	
	// Ajout le nouveau temps pour le participant ayant l'adresse donnée
	public void setTemps(String adresse,long temps)
	{
		Participant p = getParticipantByAdresse(adresse);
		if (p != null) {
			p.setTemps(temps);
			System.out.println("LesParticipants(setTemps)> Participant "+adresse+" a fait un temp de "+temps);
}
	else
		System.out.println("LesParticipants(setTemps)> Aucun participant trouve avec l'ip : "+adresse);
	
	}
	
	// Renvoi n participant disponible
	public Vector getNParticipantDispo(int n) {
		Vector l = new Vector();
		int cpt=0;
		int i=0;
		while ( (i < fichesParticipant.size()) && (cpt < n)  ) {
			Participant p = (Participant) fichesParticipant.get(i);			
			if (p.getEtat().equals("disp")) {
				l.add(p.getAdresse());
				cpt++; // incrémentation du nb de participant dispo trouvé
			}
			i++;
		}

		return l;
	}
	

	// Renvoi le participant dont l'ip est l'adresse donnée en parametre
	public Participant getParticipantByAdresse(String adresse) {
		for (int i = 0; i < fichesParticipant.size(); i++)
			if (((Participant) fichesParticipant.get(i)).getAdresse().equals(
					adresse))
				return ((Participant) fichesParticipant.get(i));
		return null;
	}

	public boolean existe(String p) {
		for (int i = 0; i < fichesParticipant.size(); i++)
			if (((Participant) fichesParticipant.get(i)).getAdresse().equals(p))
				return true;
		return false;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < fichesParticipant.size(); i++) {
			s += ((Participant) fichesParticipant.get(i)).toString();
			s += "\n----------------------------------\n";
		}
		return s;
	}
}
