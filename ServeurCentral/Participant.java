import java.util.*;

public class Participant {
    private String      adresse;
    private ArrayList   notes; // Liste des notes obtenues
    private ArrayList   temps; // Liste des temps obtenues
    private String      etat; // Etat dispo / non-dispo / deconnecté
    
    // Constructeur
    public Participant(String adresse) {
    	// Initialisation des variables
        this.adresse = adresse;
        this.notes = new ArrayList();
        this.temps = new ArrayList();
        this.etat = "disp";
    }   
    
// GETTERS    
    // Recupere l'adresse d'un participant
    public String getAdresse() { return this.adresse;  }
    
    public ArrayList getTemps(){ return this.temps;  }
    
    public ArrayList getNotes(){ return this.notes;  }
    
    public String getEtat() { return etat; }

// SETTER
    public void setEtat(String e) { etat = e;}
    
    public void setTemps(long t){ temps.add(t);}
    
// Ajout
    // Ajout d'une note
    public void ajoutNote(int n){	notes.add(n); }
    // Ajout d'un temps
    public void ajoutTemps(int t){	notes.add(t); }
    
    
// Calcul de moyenne sur les listes
    // Calcul de la note moyenne
    public double calculNoteMoyenne() { 
    	int som = 0;
    	

  	  if (notes.size() == 0)
  		  return -1;
  	  else
    	  for ( int i=0 ; i < notes.size(); i++ )
    		  som +=(int) ((Integer)notes.get(i));
     return ( (double)som / (double)notes.size()  );
    	  
   }
    
    // Calcul du temps moyen
    public double calculTempsMoyen() { 
    	int som = 0;
    	  if (temps.size() == 0)
      		  return -1;
    	  else
    		  for ( int i=0 ; i < temps.size(); i++ )
    			  som +=(int) ((Integer)temps.get(i));
    	  return ( (double)som / (double)temps.size()  );
    }
    
// toString
    public String toString()
    {
    	String s = "Participant : "+ adresse;
    	s+= "\nNotes : ";
    	for (int i = 0; i < notes.size(); i++) {
			s+=notes.get(i) + " | ";
		}
    	s+="\nNotes Moyenne : "+calculNoteMoyenne();
    	s+="\nTemps : ";
    	for (int i = 0; i < temps.size(); i++) {
			s+=temps.get(i) + " | ";
		}
    	s+="\nTemps Moyen : "+calculTempsMoyen();
    	s+="\nEtat : "+etat;
    	
    	return s;  
    }
}
