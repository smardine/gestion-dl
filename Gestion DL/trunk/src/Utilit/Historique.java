package Utilit;

import java.io.FileWriter;
import java.io.IOException;

import action.GestionRepertoire;
import action.OpenWithDefaultViewer;

public class Historique {
	/**
	 * ecrire une info dans le fichier historique.txt
	 * @param Text -String L'info souhaitée.
	 */
	public static void ecrire(String Text) {
		String Date = RecupDate.date();
		String repTravail = GestionRepertoire.RecupRepTravail();
		String ligne = "Le " + Date + "   " + Text + "\r\n";

		FileWriter writer = null;
		try {
			writer = new FileWriter(repTravail + "/historique.txt", true);
			writer.write(ligne, 0, ligne.length());

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * ouvrir le fichier historique.txt avec le programme par defaut du systeme.
	 */
	public static void lire() {
		String repTravail = GestionRepertoire.RecupRepTravail();
		OpenWithDefaultViewer.open(repTravail + "/historique.txt");
	}

}
