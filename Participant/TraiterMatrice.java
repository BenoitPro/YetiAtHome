import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Met � disposition des m�thodes static 
 * pour les diff�rents traitement n�cessaire au
 * calcul de multiplication de matrice distribu�
 * entre plusieurs participants.
 */

/**
 * 
 * @author Benoit Mar�chal
 */
public class TraiterMatrice {

	/**
	 * Prend en param�tre un calcul de multiplication de matrices recu par le
	 * r�so, et r�cup�re sous forme de tableau la matrice souhait�
	 * 
	 * @param numMatrice
	 *            Le num�ro de la matrice (la 1 ou la 2)
	 * @param calcul
	 *            Chaine recu par le r�so
	 * @return
	 */
	public static int[][] toMatrice(int numMatrice, String calcul) {
		int[][] M = null; // tableau repr�sentant la Matrice
		ArrayList lignes = new ArrayList();
		String s;
		StringTokenizer st = new StringTokenizer(calcul, "|");

		st.nextToken(); // on passe le mot "mat"
		boolean mat1 = true; // permet de savoir si les ligne lu appartient a
								// M1
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			if (s.equals("mat2")) {
				mat1 = false;
				continue;
			}

			if ((numMatrice == 1) && mat1) // On traite la matrice 1
				lignes.add(s);
			else if ((numMatrice == 2) && !mat1)
				lignes.add(s);

		}

		// V�rif
		if (lignes.size() > 0) {
			StringTokenizer st2 = new StringTokenizer((String) lignes.get(0),
					" ");
			int nbC = st2.countTokens();
			int nbL = lignes.size();

			// 
			M = new int[nbL][nbC];

			// Affiche les lignes et Conversion en tableau
			for (int i = 0; i < lignes.size(); i++) {
				st2 = new StringTokenizer((String) lignes.get(i), " ");
				int j = 0;
				// System.out.println("Ligne "+i+" : "+(String)lignes.get(i));
				while (st2.hasMoreTokens()) {
					M[i][j] = Integer.parseInt(st2.nextToken());
					System.out.print(M[i][j] + " ");
					j++;
				}
				System.out.println();
			}
		}

		else
			System.out
					.println("Client(toMatrice)> Probleme aucune ligne dans la matrice");

		return M;
	}

	/**
	 * Transforme la multiplication de 2 matrices en une somme de produits Donne
	 * la somme de produits � r�aliser pour l'�l�ment (lig,col) de la matrice
	 * r�sultat. Et format le r�sultat pour l'envoyer par le r�so exemple : sp
	 * 0x0 1*3 2*4 3*6 (signifie : somme produit pour l'element a la position 0
	 * 0 de la matrice r�sultat)
	 * 
	 * @param M1
	 *            Matrice 1
	 * @param M2
	 *            Matrice 2
	 * @param lig
	 *            Indice de la ligne de la matrice r�sultat qu'on calcul
	 * @param col
	 *            Indice de la colonne de la matrice r�sultat qu'on calcul
	 * @return Une chaine d�crivant la somme de produit � r�aliser pour
	 *         l'element de la matrice r�sultat donn�e
	 */
	public static String toSommeProduits(int[][] M1, int[][] M2, int lig, int col) {
		String sp = "sp " + lig + "x" + col + " "; // somme de produits
		// Pour chaque colone de la matrice 1 (ie chq ligne de la matrice2)
		for (int k = 0; k < M2.length; k++) {
			sp += String.valueOf(M1[lig][k]) + "*" + String.valueOf(M2[k][col]);
			if (k < (M2.length - 1))
				sp += " ";
		}
		return sp;
	}

	/**
	 * A la reception du calcul d'un element d'une matrice r�sultat
	 * sous forme de somme de produit, cette fonction permet sont 
	 * calcul et renvoi la chaine format� selon le protocol.
	 * Exemple : 0x1 45   (signifie l'element a la position 0 1 est 45
	 * pour la matrice r�sultat) 
	 * @param sp
	 * @return
	 */
	public static String calculSommeProduits(String sp) {
		String res = "";
		int somme = 0;
		StringTokenizer st = new StringTokenizer(sp, " ");
		String s;
		st.nextToken(); // on passe le "sp" du d�but
		res += st.nextToken() + " "; // La chaine indicant la position du
										// truc

		while (st.hasMoreTokens()) {
			s = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(s, "*");

			somme += (Integer.parseInt(st2.nextToken()) * Integer.parseInt(st2
					.nextToken()));
		}
		res += String.valueOf(somme);
		return res;
	}

	/**
	 * Renvoi la liste des calculs de chaque �lements
	 * du produit matricielle (M1 * M2) sous forme de string
	 * de somme de produits.
	 * 
	 * @param M1 Matrice 1
	 * @param M2 Matrice 2
	 * @return Une ArrayList de String contenant chaque somme de produit (calcul d'element)
	 */
	public static ArrayList genereSommeProduits(int[][] M1, int[][] M2) {

		// System.out.println("G�n�ration des sommes de produits");
		ArrayList l = new ArrayList();
		int nbL = M1.length;
		int nbC = M2[0].length;
		for (int i = 0; i < nbL; i++) {
			for (int j = 0; j < nbC; j++) {
				l.add(toSommeProduits(M1, M2, i, j));
			}
		}
		return l;
	}

	// Ajoute � la matrice r�sultat le nouveau somme de produits (Element
	// calcul�)
	// Renvoi la Matrice avec le nouvel �l�ment ajout�
	/**
	 * Prend le r�sultat du calcul d'un �lement de la matrice r�sultat
	 * format� selon le protocol (1x2 34) et le place dans la matrice
	 * r�sultat qu'on lui donne �galement en parametre
	 * 
	 * @param M Matrice r�sulat qui recoi le nouvel element
	 * @param el Chaine contenant l'element a ajout� et sa position
	 */
	public static int[][] ajoutElement(int[][] M, String el) {
		String s;
		StringTokenizer st = new StringTokenizer(el, " ");
		s = st.nextToken();
		StringTokenizer st2 = new StringTokenizer(s, "x");
		int lig;
		int col;
		lig = Integer.parseInt(st2.nextToken());
		col = Integer.parseInt(st2.nextToken());
		M[lig][col] = Integer.parseInt(st.nextToken());

		return M;
	}

	/**
	 * Affiche un tableau d'entier de 2 dimension
	 * @param M
	 */
	public static void affiche(int[][] MR)
	{
		String s="";
		 // Affichage de la matrice r�sultat
        for (int i = 0; i < MR.length; i++) {
			for (int j = 0; j < MR[0].length; j++) {
				s +=MR[i][j]+ " ";
			}
			s+="\n";
		}
	}
	
	/**
	 * Renvoi un tableau a 2 dim. sous forme de matrice
	 * en ligne ligne comme le protocol le d�fini :
	 * mat|1 34 4|2 12 56|
	 */
	public static String toString(int[][] MR)
	{
		String s="mat|";
		 // Affichage de la matrice r�sultat
       for (int i = 0; i < MR.length; i++) {
			for (int j = 0; j < MR[0].length; j++) {
				s +=MR[i][j]+ " ";
			}
			if (i< MR.length-1)
				s+="|";
		}
       return  s;
	}
	
}
