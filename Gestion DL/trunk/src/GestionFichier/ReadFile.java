package GestionFichier;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;

public class ReadFile {

	private static final long serialVersionUID = 1L;

	/**
	 * Lecture ligne à ligne d'un fichier texte et affichage dans une jList
	 * @param chemin -String le chemin du fichier texte
	 * @param listModel -DefaultModelList le model de liste
	 * @param nbLigne -JLabel sert a afficher le nb de ligne
	 * @param nbAdresse -int le nb d'adresse trouvée.
	 */

	public static int ReadLine(String chemin, DefaultListModel listModel) {

		int nbAdresse = 0;
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(chemin);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				// Print the content on the console
				// System.out.println (strLine);

				if (!strLine.equals("")) {
					listModel.addElement(strLine);
					nbAdresse++;
				}
			}
			// Close the input stream
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return nbAdresse;
	}

	/**
	 * Trouver une chaine de caracteres dans un fichier
	 * @param cheminFichier -String le chemin du fichier
	 * @param OccurToFind -String la chaine a trouver ex "abc@hotmail.com"
	 * @return result -boolean vrai si on trouve la chaine de caracteres.
	 */
	public static boolean FindOccurInFile(String cheminFichier,
			String OccurToFind) {

		String line = null;
		boolean result = false;

		try {
			BufferedReader br = new BufferedReader(
					new FileReader(cheminFichier));

			int i = 1; // initialisation du numero de ligne
			while ((line = br.readLine()) != null) {
				if (line.indexOf(OccurToFind) != -1) {
					System.out.println("Mot trouve a la ligne " + i);
					result = true;
					return result;
				}
				i++;
			}
			br.close();
		} catch (FileNotFoundException exc) {
			System.out.println("File not found");
		} catch (IOException ioe) {
			System.out.println("Erreur IO");
		}
		return result;

	}

	public static int ReadLineHTM(String chemin, DefaultListModel listModel) {
		int nbAdresse = 0;
		int classeFichier_trouvée = 0;
		int baliseaffichagetrouvée = 0;
		int premierguillement = 0;
		int dernierguillement = 0;
		String Lien = "";
		String debutLien = "";
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(chemin);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				// Print the content on the console
				// System.out.println (strLine);

				if (!strLine.equals("")) {
					if (strLine.contains("class=\"td_fichier\"") == true) {// on
																			// va
																			// avoir
																			// le
																			// lien
																			// sur
																			// la
																			// 2°
																			// ligne
																			// suivante
						classeFichier_trouvée++;

					}
					if (classeFichier_trouvée >= 1) {
						if (strLine.contains("lign=\"center\"") == true) {// on
																			// a
																			// la
																			// balise
																			// d'affichage
							baliseaffichagetrouvée++;
						}
					}
					if ((classeFichier_trouvée >= 1)
							&& (baliseaffichagetrouvée >= 1)
							&& (strLine.contains("href=") == true)) {// ca y es
																		// on a
																		// le
																		// liens,reste
																		// plus
																		// qu'a
																		// le
																		// parser
																		// et
																		// l'afficher
																		// dans
																		// la
																		// liste
						premierguillement = strLine.indexOf("http://");
						debutLien = strLine.substring(premierguillement);

						dernierguillement = debutLien.indexOf("\"");
						Lien = debutLien.substring(0, dernierguillement);
						listModel.addElement(Lien);
						nbAdresse++;
						classeFichier_trouvée = 0;
						baliseaffichagetrouvée = 0;

					}

					// listModel.addElement (strLine);
					// nbAdresse++;
				}
			}
			// Close the input stream
			in.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return nbAdresse;
	}
}
