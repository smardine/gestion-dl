package GestionFichier;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Utilit.Historique;

public class ManipFichier {

	/**
	 * Deplace un fichier
	 * @param source -File le fichier source
	 * @param destination -File le fichier de destination
	 * @return result -Boolean vrai si ca a marché
	 */

	public static boolean deplacer(File source, File destination) {
		if (!destination.exists()) {
			// On essaye avec renameTo
			boolean result = source.renameTo(destination);
			if (!result) {
				// On essaye de copier
				result = true;
				result &= copier(source, destination);
				result &= source.delete();

			}
			return (result);
		} else {
			// Si le fichier destination existe, on annule ...
			return (false);
		}
	}

	/**
	 * copie un fichier
	 * @param source -File le fichier source
	 * @param destination -File le fichier de destination
	 * @return resultat -Boolean vrai si ca a marché
	 */
	public static boolean copier(File source, File destination) {
		boolean resultat = false;

		// Declaration des flux
		java.io.FileInputStream sourceFile = null;
		java.io.FileOutputStream destinationFile = null;

		try {
			// Création du fichier :
			destination.createNewFile();

			// Ouverture des flux
			sourceFile = new java.io.FileInputStream(source);
			destinationFile = new java.io.FileOutputStream(destination);

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;

			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
			}

			// Copie réussie
			resultat = true;
		} catch (java.io.FileNotFoundException f) {

		} catch (java.io.IOException e) {

		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				sourceFile.close();
			} catch (Exception e) {
			}
			try {
				destinationFile.close();
			} catch (Exception e) {
			}
		}
		return (resultat);
	}

	/**
	 * Affiche une boite de dialogue pour la création d'un fichier
	 * @return nomdossier+nomfichier -String
	 */
	public static String SaveFile(String nomfichier) {

		String nomdossier;

		JFrame frame = new JFrame();
		FileDialog fd = new FileDialog(frame,
				"Sélectionner le nouveau fichier qui contiendra les adresses",
				FileDialog.SAVE);
		fd.setFile(nomfichier);
		fd.setVisible(true);
		nomfichier = fd.getFile();
		nomdossier = fd.getDirectory();
		return nomdossier + nomfichier;
	}

	/**
	 * Affiche une boite de dialogue pour l'ouverture d'un fichier
	 * @return nomdossier+nomfichier -String
	 */
	public static String OpenFile() {

		String nomfichier;
		String nomdossier;

		JFrame frame = new JFrame();
		FileDialog fd = new FileDialog(frame, "Sélection de fichiers",
				FileDialog.LOAD);
		fd.setVisible(true);
		nomfichier = fd.getFile();
		nomdossier = fd.getDirectory();
		return nomdossier + nomfichier;
	}

	/**
	 * Affiche une boite de dialogue pour la création d'un dossier
	 * @return nomdossier -String
	 */
	public static String OpenFolder() {
		String nomdossier = null;
		JFrame frame = new JFrame();

		JFileChooser choiceFolder = new JFileChooser();
		choiceFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choiceFolder.showOpenDialog(frame);

		File file = choiceFolder.getSelectedFile();
		try {
			// on utilise le getCanonicalPath pour recuperer le pathname sous la
			// forme windows
			nomdossier = file.getCanonicalPath();

		} catch (IOException e) {

			e.printStackTrace();
		}

		return nomdossier;

	}

	/**
	 * Copie ligne a ligne un fichier texte vers un autre
	 * @param Source - File fichier source
	 * @param Destination -File fichier de destination
	 * @param Signature -JTextField sert a afficher la signature de l'antivirus
	 *            trouvé.
	 * @param Adresse mail -JTextField affiche l'adresse mail trouvée dans le
	 *            fichier
	 */
	public static void CopyLigneALigne(File Source, File Destination,
			JTextField Signature, JTextField AdresseEmail) {
		// Declaration des flux
		String line = null;
		String boundary;
		String deuxieme_boundary = null;

		int boundaryTrouvé = -1;
		int signatureTrouvée = -1;
		int multiPartTrouvée = -1;
		Boolean cageReturnTrouvé = false;
		int finEntete = 0;

		int cage_return = 0, fin_entete_attein = 0, fin_corps_message = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(Source));
			PrintWriter out = new PrintWriter(new FileWriter(Destination));

			while ((line = br.readLine()) != null)// on declanche la lecture
			// d'une ligne
			{

				if (deuxieme_boundary != null) {
					boundaryTrouvé = line.indexOf(deuxieme_boundary);
				}

				signatureTrouvée = line.indexOf("boundary=");
				multiPartTrouvée = line
						.indexOf("Content-Type: multipart/mixed");
				cageReturnTrouvé = line.equals("");

				if (cageReturnTrouvé == true) {
					cage_return++;
					if (fin_entete_attein > 0) {
						fin_corps_message++;
					}
				}

				finEntete = line.indexOf("Content-Transfer-Encoding: base64");
				if (finEntete == -1) {
					finEntete = line
							.indexOf("Content-Transfer-Encoding: BASE64");
				}
				if (finEntete != -1) {
					fin_entete_attein++;
				}
				if ((signatureTrouvée == -1) && (boundaryTrouvé == -1)
						&& (cage_return == 0) && (multiPartTrouvée == -1)
						&& (fin_corps_message <= 2)) {
					out.println(line);
				}
				if (cage_return > 0) {
					if ((fin_entete_attein == 1) && (fin_corps_message >= 1)) {
						out.println(line);
					}
					cage_return = 0;
				}

				if (signatureTrouvée != -1) {
					int debut_boundary = line.indexOf("=");
					int fin_boundary = line.lastIndexOf('"');
					boundary = line.substring((debut_boundary) + 2,
							fin_boundary);
					// boundary = line.substring((debut_boundary)+2,20);
					Signature.setText(boundary);
					deuxieme_boundary = "--" + boundary;

					Historique.ecrire("Signature Trouvée : " + boundary);

				}

				int To_trouvee = line.indexOf("To:");
				if (To_trouvee != -1) {
					int arobase_trouvee = line.indexOf("@");
					if (arobase_trouvee != -1) {
						String AdresseMail = line;
						AdresseEmail.setText(AdresseMail);

						Historique.ecrire("Adresse mail Trouvée : "
								+ AdresseMail);

					}
				}

			}
			out.close();
			br.close();
		} catch (FileNotFoundException exc) {
			System.out.println("File not found");
		} catch (IOException ioe) {
			System.out.println("Erreur IO");
		}

	}

	/**
	 * Suppresion du contenu d'un repertoire avec un filtre sur l'extension
	 * @param RepAVider - File repertoire à vider.
	 * @param extension -Final String extension sous la forme ".eml"
	 */
	public static void DeleteContenuRepertoireAvecFiltre(File RepAVider,
			final String extension) {
		if (RepAVider.isDirectory()) {
			File[] list = RepAVider.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(extension);
				}
			});
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					DeleteContenuRepertoireAvecFiltre(list[i], extension);
				}
			} else {
				System.err.println(RepAVider + " : Erreur de lecture.");
			}

		}

		if (RepAVider.isFile()) {
			// listModel.addElement (RepAVider.getName());
			RepAVider.delete();
			// nbFichier++;
			// nbFichierLabel.setText("Nombre de fichier(s) a traiter:"+nbFichier);

		}
	}
}
