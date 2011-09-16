package Thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import action.ConfigMgt;
import action.GestionRepertoire;

public class ThreadVerifMaj extends Thread {

	// tte les decalration necessaire...

	protected String adresse;
	protected JProgressBar Barreprogression;
	protected JLabel TexteTailleTotale, TexteVitesse, FichierActuel;

	/**
	 * Passe une requette HTTP et affiche le resultat dans des JTextField
	 * @param url -String url d'interrogation sur le site de maj
	 * @param donneCaisseLocale -JTextField message pour l'utilisateur
	 * @param donneeCaisseSite -JTextField message pour l'utilisateur
	 * @param donneeConvLocale -JTextField message pour l'utilisateur
	 * @param donneeConvSite -JTextField message pour l'utilisateur
	 * @param donneeMicrocLocale -JTextField message pour l'utilisateur
	 * @param donneeMicrocSite -JTextField message pour l'utilisateur
	 * @param text -JTextPane message pour l'utilisateur
	 */

	public ThreadVerifMaj(String URL, JProgressBar progress,
			JLabel TailleTotale, JLabel Vitesse, JLabel FichierEnCOurs)

	{
		adresse = URL;
		Barreprogression = progress;
		TexteTailleTotale = TailleTotale;
		TexteVitesse = Vitesse;
		FichierActuel = FichierEnCOurs;
	}

	public void run() {

		InputStream input = null;
		FileOutputStream writeFile = null;
		String cheminFichier = null;
		File fichier = null;
		long HeureDebut;
		long HeureActuelle;
		FichierActuel.setText(" Vérification de la présence d'une mise à jour");
		File setup = new File(GestionRepertoire.RecupRepTravail()
				+ "\\setup_Gestion_DL.exe");
		if (setup.exists() == true) {
			boolean effacé = setup.delete();
			if (effacé == false) {
				setup.deleteOnExit();
			}
		}

		try {
			FichierActuel
					.setText(" Récuperation de la version disponible sur le site");
			URL url = new URL(adresse);
			URLConnection connection = url.openConnection();
			final int fileLength = connection.getContentLength();

			if (fileLength == -1) {
				System.out.println("Invalide URL or file.");
				return;
			}

			input = connection.getInputStream();
			String fileName = url.getFile().substring(
					url.getFile().lastIndexOf('/') + 1);
			if (fileName.contains("%20") == true) {
				fileName = fileName.replaceAll("%20", " ");
			}
			if (fileName.contains("&amp;") == true) {
				fileName = fileName.replaceAll("&amp;", " and ");
			}
			cheminFichier = GestionRepertoire.RecupRepTravail() + "\\"
					+ fileName;

			fichier = new File(cheminFichier);
			writeFile = new FileOutputStream(cheminFichier);
			// lecture par segment de 4Mo
			byte[] buffer = new byte[4096 * 1024];
			int read;

			while ((read = input.read(buffer)) > 0) {
				writeFile.write(buffer, 0, read);

			}

			writeFile.flush();
		} catch (IOException e) {
			System.out.println("Error while trying to download the file.");
			e.printStackTrace();
		} finally {
			try {
				writeFile.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// le telech est fini, on verifie la version hebergée sur le site
		ConfigMgt versionSite = null;
		try {
			versionSite = new ConfigMgt("version.ini", GestionRepertoire
					.RecupRepTravail()
					+ "\\", '[');
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String VersionDispo = versionSite.getValeurDe("version");
		ConfigMgt versionInstallée = null;
		try {
			versionInstallée = new ConfigMgt("version.ini", GestionRepertoire
					.RecupRepTravail()
					+ "\\Ini File\\", '[');
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		String VersionEnCours = versionInstallée.getValeurDe("version");
		boolean effacé = fichier.delete();
		if (effacé == false) {
			fichier.deleteOnExit();
		}

		FichierActuel.setText(" Comparaison avec la version actuelle");

		if (VersionEnCours.equals(VersionDispo) == false) {// il y a une autre
															// version sur le
															// site de maj
			// proposition de la maj a l'utilisateur
			int demandeMaj = JOptionPane.showConfirmDialog(null, "La version "
					+ VersionDispo + " est disponible\n"
					+ " Voulez vous faire la mise à jour"); // si il repond oui,
															// dl du setup puis
															// execution
			// si il repond non => poursuite du programme
			if (demandeMaj == 0) {// maj acceptée
				HeureDebut = System.currentTimeMillis();
				try {
					URL url = new URL(
							"http://gestion-dl.googlecode.com/files/setup_Gestion_DL.exe");
					URLConnection connection = url.openConnection();
					final int fileLength = connection.getContentLength();

					if (fileLength == -1) {
						System.out.println("Invalide URL or file.");
						return;
					}

					if (fileLength > 1024 && fileLength < 10240) {
						double TailleAvec2ChiffreApresLaVirgule = floor(
								fileLength, 2, 0.0d);
						TexteTailleTotale
								.setText(TailleAvec2ChiffreApresLaVirgule
										+ " o");
					}
					if (fileLength > 10240) {
						double TailleAvec2ChiffreApresLaVirgule = floor(
								fileLength / 1024, 2, 0.0d);
						TexteTailleTotale
								.setText(TailleAvec2ChiffreApresLaVirgule
										+ " ko");
					}
					if (fileLength > (1024 * 1024)) {
						double TailleAvec2ChiffreApresLaVirgule = floor(
								fileLength / (1024 * 1024), 2, 0.0d);
						TexteTailleTotale
								.setText(TailleAvec2ChiffreApresLaVirgule
										+ " Mo");
					}

					input = connection.getInputStream();
					String fileName = url.getFile().substring(
							url.getFile().lastIndexOf('/') + 1);
					if (fileName.contains("%20") == true) {
						fileName = fileName.replaceAll("%20", " ");
					}
					if (fileName.contains("&amp;") == true) {
						fileName = fileName.replaceAll("&amp;", " and ");
					}
					cheminFichier = GestionRepertoire.RecupRepTravail() + "\\"
							+ fileName;
					FichierActuel.setText(" Fichier en cours : " + fileName);
					fichier = new File(cheminFichier);
					writeFile = new FileOutputStream(cheminFichier);
					// lecture par segment de 4Mo
					byte[] buffer = new byte[4096 * 1024];
					int read;

					while ((read = input.read(buffer)) > 0) {
						writeFile.write(buffer, 0, read);
						long TailleEncours = fichier.length();
						int progressionEnCours = (int) ((100 * (TailleEncours + 1)) / fileLength);
						// int Pourcent=(int) progressionEnCours;

						HeureActuelle = System.currentTimeMillis();

						long Vitesse = (long) (TailleEncours / (HeureActuelle - HeureDebut));

						TexteVitesse.setText("Vitesse Actuelle : " + Vitesse
								+ " Ko/s");
						Barreprogression.setValue(progressionEnCours);
						Barreprogression.setString(progressionEnCours + " %");

					}

					writeFile.flush();
				} catch (IOException e) {
					System.out
							.println("Error while trying to download the file.");
					e.printStackTrace();
				} finally {
					try {
						writeFile.close();
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				Runtime r1 = Runtime.getRuntime();

				String cmdExecuteSetup = ("cmd /c \"" + cheminFichier + "\"");
				try {
					r1.exec(cmdExecuteSetup);
				} catch (IOException e) {

					JOptionPane.showMessageDialog(null, e, "Erreur SQL",
							JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}

				System.exit(0);
				// p.waitFor();

			}
			if (demandeMaj == 1) {// maj refusée

			}

		}
		FichierActuel.setText(" Version à jour");

	}

	public static double floor(double a, int decimales, double plus) {
		double p = Math.pow(10.0, decimales);
		// return Math.floor((a*p) + 0.5) / p; // avec arrondi éventuel (sans
		// arrondi >>>> + 0.0
		return Math.floor((a * p) + plus) / p;
	}

}
