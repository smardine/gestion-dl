package GestionFichier;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

public class DirectoryReader implements FilenameFilter {

	private static final long serialVersionUID = 1L;

	public DirectoryReader() {
	}

	/**
	 * Liste les fichiers present dans un repertoire puis l'affiche dans une
	 * jList via un model de liste. Permet egalement d'afficher le nombre de
	 * fichiers dan un jLabel
	 * @param repertoire -File Le répertoire à scanner.
	 * @param nbFichierLabel -JLabel label ou l'on affiche le nb de fichier.
	 * @param listModel -DefaultModelList le model dans lequels seront affiché
	 *            les fichers trouvé.
	 * @param nbFichierMail -int le nombre de fichier
	 */
	public static void listeRepertoireEtSousRepertoire(File repertoire,
			JLabel nbFichierLabel, DefaultListModel listModel, int nbFichierMail) {
		// System.out.println ( repertoire.getName());

		// listModelFichierMail.addElement (repertoire.getName());
		if (repertoire.isDirectory()) {
			File[] list = repertoire.listFiles();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					listeRepertoireEtSousRepertoire(list[i], nbFichierLabel,
							listModel, i);
				}
			} else {
				System.err.println(repertoire + " : Erreur de lecture.");
			}

		}

		if (repertoire.isFile()) {
			listModel.addElement(repertoire.getName());
			nbFichierMail++;
			nbFichierLabel.setText("Nombre de fichier(s) a traiter:"
					+ nbFichierMail);

		}

	}

	/**
	 * Liste les fichier present dans un repertoire avec un filtre sur
	 * l'extension puis l'affiche dans une jList via un model de liste.
	 * @param repertoire -File Le répertoire à scanner.
	 * @param listModel -DefaultModelList le model dans lequels seront affiché
	 *            les fichers trouvé.
	 * @param extension -String extension que l'on recherche sous la forme
	 *            ".eml".
	 */
	public static void listeRepertoireAvecFiltreExtension(File repertoire,
			DefaultListModel listModel, final String extension) {

		if (repertoire.isDirectory()) {
			File[] list = repertoire.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(extension);
				}
			});
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					// Appel récursif sur les sous-répertoires
					listeRepertoireAvecFiltreExtension(list[i], listModel,
							extension);
				}
			} else {
				System.err.println(repertoire + " : Erreur de lecture.");
			}

		}

		if (repertoire.isFile()) {
			listModel.addElement(repertoire.getName());
			// nbFichier++;
			// nbFichierLabel.setText("Nombre de fichier(s) a traiter:"+nbFichier);

		}

	}

	@Override
	public boolean accept(File dir, String name) {
		return false;
	}

}