package Utilit;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

public class Comptage {
	public int nbDossier = 0;
	public int nbFichier = 0;

	public Comptage(String directoryPath) {

		LanceComptage(directoryPath);

	}

	private void LanceComptage(String string) {
		// TODO Auto-generated method stub
		File directory = new File(string);
		if (!directory.exists()) {
			// System.out.println("Le fichier/répertoire '"+directoryPath+"' n'existe pas");
		} else if (directory.isFile()) {
			// System.out.println("Le chemin '"+directoryPath+"' correspond à un fichier et non à un répertoire");
			// c'est un dossier, le compteur de fichier est incrementé, celui
			// des dossier est decrémenté
			nbDossier--;
			nbFichier++;

		} else {
			if (directory.isDirectory()) {
				File[] subfiles = directory.listFiles();

				nbDossier = subfiles.length + nbDossier;

				for (int i = 0; i < subfiles.length; i++) {
					LanceComptage(subfiles[i].toString());
				}

			}
		}
	}

	public int getNbFichier() {
		return nbFichier;
	}

	public int getNbDossier() {
		return nbDossier;
	}

	/**
	 * Recupere le nb de fichier present dans un repertoire et ses sous
	 * repertoire et l'affiche dans une liste
	 * @param directoryPath -String chemin du repertoire à scanner
	 * @param model -DefaultListModel model de liste a rafraichir
	 * @param message -JTextField message pour l'utilisateur
	 */
	@SuppressWarnings("unused")
	private void RecupNbFichierEtAfficheDansUneListe(String directoryPath,
			DefaultListModel model, JTextField message) {

		File directory = new File(directoryPath);
		if (!directory.exists()) {
			// System.out.println("Le fichier/répertoire '"+directoryPath+"' n'existe pas");
		} else if (!directory.isDirectory()) {
			// System.out.println("Le chemin '"+directoryPath+"' correspond à un fichier et non à un répertoire");
			// nbFich++;
			model.addElement(directoryPath);
		} else {
			if (directory.isDirectory()) {
				File[] subfiles = directory.listFiles();
				// String message =
				// "Le répertoire '"+directoryPath+"' contient "+
				// subfiles.length+" fichier"+(subfiles.length>1?"s":"");
				// System.out.println(message);
				// nbFich = subfiles.length+nbFich;

				for (int i = 0; i < subfiles.length; i++) {
					// System.out.println(subfiles[i].getName());
					// message.setText("Preparation ...");
					RecupNbFichierEtAfficheDansUneListe(subfiles[i].toString(),
							model, message);
				}

			}

		}
	}

}
