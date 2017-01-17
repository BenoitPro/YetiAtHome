import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Client {
    
    private Socket              sock;
    private ObjectOutputStream  oos;
    private ObjectInputStream   ois;
    
    private String adresse ; // ip du serveur cental
    private int port; // Port du serveur central
    
    // Constructeur
    public Client(String adresse, int port) {
    	this.adresse = adresse;
    	this.port = port;
    }
    
 /* **** TEST by FILDZ ******
    
    // Prend en paramètre un calcul de multiplication de matrices
    public int[][] toMatrice(int numMatrice, String calcul){
    	int[][] M = null; // tableau représentant la  Matrice
    	ArrayList lignes = new ArrayList();
    	String s;
    	StringTokenizer st = new StringTokenizer(calcul,"|");

    	st.nextToken(); // on passe le mot "mat"
    	boolean mat1 = true; // permet de savoir si les ligne lu appartient a M1
    		while(st.hasMoreTokens())
    		{
    			s = st.nextToken();
    			if(s.equals("mat2")) {
    				mat1 = false;
    				continue;
    			}

    	    	if ( (numMatrice == 1) && mat1 )  // On traite la matrice 1
    	    		lignes.add(s);
    	    	else if ((numMatrice == 2)  && !mat1)
    	    		lignes.add(s);
    	    		
    	}
    	
    	// Vérif 
    	if (lignes.size() > 0){
    		StringTokenizer st2 = new StringTokenizer((String)lignes.get(0)," ");
    		int nbC = st2.countTokens();
    		int nbL = lignes.size();
    		
    		// 
    		M = new int[nbL][nbC];

    		
        	// Affiche les lignes et Conversion en tableau
        	for (int i = 0; i < lignes.size(); i++) {
        		st2 = new StringTokenizer((String)lignes.get(i)," ");
        		int j= 0;
        		//System.out.println("Ligne "+i+" : "+(String)lignes.get(i));
        		while(st2.hasMoreTokens()) {
        			M[i][j] = Integer.parseInt(st2.nextToken());
        			System.out.print(M[i][j] + " ");
        			j++;
        		}
        		System.out.println();
    		}	
    	}
    	
    	else
    		System.out.println("Client(toMatrice)> Probleme aucune ligne dans la matrice");
    	
    	
    	return M;
    }
    
    // Transforme la multiplication de 2 matrices en une somme de produits
    public String toSommeProduits(int[][] M1, int[][] M2,int lig,int col)
    {
    	String sp = "sp "+lig+"x"+col+" "; // somme de produits 
    	// Pour chaque colone de la matrice 1 (ie chq ligne de la matrice2)
    	for (int k = 0; k < M2.length; k++) {
			sp += String.valueOf(M1[lig][k]) + "*" + String.valueOf(M2[k][col]) ;
			if (k < (M2.length-1)) 
				sp += " ";
		}
    	return sp ;
    }
    
    public String calculSommeProduits(String sp)
    {
    	String res = "";
    	int somme = 0;
    	StringTokenizer st = new StringTokenizer(sp, " ");
    	String s;
    	st.nextToken(); // on passe le "sp" du début
    	res += st.nextToken() + " "; // La chaine indicant la position du truc 
    	
    	while(st.hasMoreTokens()) {
    		s = st.nextToken();
    		StringTokenizer st2 = new StringTokenizer(s,"*");
    		
    		somme += (Integer.parseInt(st2.nextToken()) * Integer.parseInt(st2.nextToken()));
    	}
    	res += String.valueOf( somme); 
    	return res;
    }
    
    public ArrayList genereSommeProduits(int[][] M1, int[][] M2)
    {
    	
    	//System.out.println("Génération des sommes de produits");
    ArrayList l = new ArrayList();
    	int nbL = M1.length;
    	int nbC = M2[0].length ;
    	for (int i = 0; i < nbL ; i++) {
        	for (int j = 0; j < nbC ; j++) {
        		l.add(toSommeProduits(M1,M2,i,j));
        	}
    	}
    	return l;
    }
    
    // Ajoute à la matrice résultat le nouveau somme de produits (Element calculé)
    // Renvoi la Matrice avec le nouvel élément ajouté
    public int[][] ajoutElement(int[][] M,String el)
    {
    	String s;
    	StringTokenizer st = new StringTokenizer(el, " ");
    	s = st.nextToken();
    	StringTokenizer st2 = new StringTokenizer(s,"x");
    	int lig ;
    	int col;
    	lig = Integer.parseInt(st2.nextToken());
    	col = Integer.parseInt(st2.nextToken());
    	M[lig][col] = Integer.parseInt(st.nextToken());
    	
    	return M;
    }
    * Connexion au serveur address:port
     * et ouverture des flux d'envoi/reception d'objets
     */
    protected int connecter() {
    	int reussite = 0;     
    	System.out.print("Client(connecter)> Connexion avec le ServeurCentral ("+adresse+")...");
        try {
            /* Initialisation du socket Client */
            sock = new Socket( adresse , port);
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
            reussite =  0;
        } catch(Exception e) { reussite = -1; }

        System.out.println(" OK");
        return reussite;
    }
    
    /* Méthode de définition d'un calcul*/
    /*
     * On considère que la chaine calcul sera de la forme
     * "som 1 2 -1 3 -5 6"
     * ou
     * "mat 1 -2 3....."
     */
    protected String sendCalcul( String calcul ){
        String res="";
        System.out.print("Client(sendCalcul)> Envoi du calcul...");
        try {
            oos.writeObject(calcul);
            oos.flush();
            System.out.println("OK");
            System.out.print("Client(sendCalcul)> Reception du résultat...");
            res = (String)ois.readObject();
            System.out.println("OK");
            System.out.print("Client(sendCalcul)> "+calcul+" = "+res);
        } catch ( Exception e ){ e.printStackTrace(); }
        return res;
    }
    
    
    /* Méthode qui envoie un fichier contenant un entier 
     * par ligne et en fait la somme
     * on peut très bien mettre en tête de fichier le mot 
     * clé som ou mat
     * et utiliser ce fichier pour une somme d'entier ou la multiplication
     * de deux matrices
     * exemple :
     * som
     * 1
     * -2
     * 4
     *
     * ou
     * mat
     * 1,2,3,-4
     * 1,-2,3,4
     * 8,7,8,-3
     * mult
     * 1,-2,-3
     * 1,-2,3
     * 8,7,-8
     * Benoit tu vera bien ;)
     */
    protected String sendFile(String FilePath) {
    	// Variables
        String res=""; // Resultat final
        String calcul= ""; // calcul a envoyer
        String lu =""; // Ligne lu
        try {
        	int i=0;
            BufferedReader br = new BufferedReader(new FileReader(FilePath));
            while ( (lu = br.readLine()) != null ) {
            	System.out.println(lu);
                if (i == 0){ // s'il s'agit de la 1iere ligne lu
                	if (lu.equals("som")) { // Si c'est une somme
                		calcul = "som";
                		
                	}
                	else if (lu.equals("mat")) { // Si c'est une mult de mat
                		
                		calcul = "mat";
                		
                	}
                	else 
                	{
                		calcul = "invalid";
                		System.out.println("Client(sendFile)> Erreur format de fichier invalide !");
                	}
                	
                }
               	else { // s'il s'agit des autres lignes
               		if (calcul.substring(0,3).equals("som")) { // Si c'est un somme
               			calcul += " "+lu;
                	}
               		else if (calcul.substring(0,3).equals("mat")) { // Si c'est une matrice
               				calcul += "|"+lu;
                	}
               	}
                i++;
            }
            br.close();
        }
        catch ( Exception e ) { e.printStackTrace(); }
        
        System.out.println("Client(sendFile)> Fichier convertit en calcul : "+calcul);

     
        	
    	
        // ******************************
        
        // Traitement du renvoi
        if (calcul.equals("invalid"))
        	return calcul;
         res = sendCalcul(calcul);
        
        return res;
    }
    
    
    /* Permet de clore les flux ainsi que la connexion */
    protected void deconnecter(){
        try{
            oos.writeObject(new String("quit"));
            oos.close();
            ois.close();
            sock.close();
        } catch ( Exception e ) { e.printStackTrace(); }
    }
    
    protected void finalize() {
        try {
            oos.writeObject(new String("quit"));
        } catch ( Exception e ){e.printStackTrace(); }
    }
}
