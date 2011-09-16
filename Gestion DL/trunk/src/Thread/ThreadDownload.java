package Thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import GestionFichier.ManipFichier;

public class ThreadDownload extends Thread {

	// tte les decalration necessaire...

	protected String adresse;
	protected JProgressBar Barreprogression;
	protected JLabel TexteTailleTotale, TexteVitesse, FichierActuel,
			LabelNombreaDL, LabelDejaDL;
	protected JButton Ouvrir, GO, Explorer;
	protected DefaultListModel ListeAttente, ListeOk;
	protected JList EnAttente, Fini;
	private volatile boolean pause = false;

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

	public ThreadDownload(JProgressBar progress, JLabel FileInProgress,
			JLabel TailleTotale, JLabel vitesse, JLabel EncoreADL,
			JLabel DEJADL, JButton Start, JButton Open, JButton Explore,
			DefaultListModel enattente, DefaultListModel fait, JList Entrée,
			JList Sortie)

	{

		Barreprogression = progress;
		TexteTailleTotale = TailleTotale;
		TexteVitesse = vitesse;
		Ouvrir = Open;
		GO = Start;
		ListeAttente = enattente;
		ListeOk = fait;
		EnAttente = Entrée;
		Fini = Sortie;
		FichierActuel = FileInProgress;
		Explorer = Explore;
		LabelNombreaDL = EncoreADL;
		LabelDejaDL = DEJADL;
	}

	public void run() {
		String RepParDefault = ManipFichier.OpenFolder();
		GO.setEnabled(false);
		InputStream input = null;
		FileOutputStream writeFile = null;
		long HeureDebut;
		long HeureActuelle;
		String cheminFichier = null;

		// Calendar depart = Calendar.getInstance();
		// on commence par recuperer le nombre de message a traiter
		int nbFichierADL = EnAttente.getModel().getSize();
		if (nbFichierADL == 0) {
			JOptionPane.showMessageDialog(null, "Aucun lien à télécharger",
					"Erreur", JOptionPane.WARNING_MESSAGE);
			return;
		}

		for (int i = 0; i <= nbFichierADL; i++) {
			if (i >= 1) {
				EnAttente.setSelectedIndex(i - 1);
			} else {
				EnAttente.setSelectedIndex(i);
			}
			// on commence le traitement

			// on fait une pause pour rafraichir la fenetre
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void run() {
						EnAttente.ensureIndexIsVisible(EnAttente
								.getSelectedIndex());

					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			try {
				HeureDebut = System.currentTimeMillis();
				String adresse = ListeAttente.getElementAt(
						EnAttente.getSelectedIndex()).toString();

				URL url = new URL(adresse);
				URLConnection connection = url.openConnection();
				final int fileLength = connection.getContentLength();

				if (fileLength == -1) {
					System.out.println("Invalide URL or file.");
					JOptionPane.showMessageDialog(null,
							"L'url suivante est incorrecte\n\r" + adresse,
							"Erreur", JOptionPane.INFORMATION_MESSAGE);
					// l'url est invalide, on la sort de la liste des ficher a
					// dl mains on ne la met pas dans celle des fichiers deja
					// DL.
					ListeAttente.removeElementAt(EnAttente.getSelectedIndex());
					nbFichierADL = EnAttente.getModel().getSize();

					i = 0;
					LabelNombreaDL.setText("nb de fichier à DL : "
							+ nbFichierADL);

				}
				if (fileLength != -1) {
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
					if (fileName.contains("%5B") == true) {
						fileName = fileName.replaceAll("%5B", "[");
					}
					if (fileName.contains("%5D") == true) {
						fileName = fileName.replaceAll("%5D", "]");
					}
					if (fileName.contains("%28") == true) {
						fileName = fileName.replaceAll("%28", "(");
					}
					if (fileName.contains("%29") == true) {
						fileName = fileName.replaceAll("%29", ")");
					}
					cheminFichier = RepParDefault + "\\" + fileName;
					FichierActuel.setText(" Fichier en cours : " + fileName);

					File fichier = new File(cheminFichier);
					writeFile = new FileOutputStream(cheminFichier);
					// lecture par segment de 4Mo
					byte[] buffer = new byte[1024];
					int read;

					while ((read = input.read(buffer)) > 0) {
						writeFile.write(buffer, 0, read);
						long TailleEncours = fichier.length();
						int progressionEnCours = (int) ((100 * (TailleEncours + 1)) / fileLength);

						HeureActuelle = System.currentTimeMillis();

						long Vitesse = (long) (TailleEncours / (HeureActuelle - HeureDebut));

						TexteVitesse.setText("Vitesse Actuelle : " + Vitesse
								+ " Ko/s");
						Barreprogression.setValue(progressionEnCours);
						Barreprogression.setString(progressionEnCours + " %");

						if (pause) {
							waitThread();
						}

					}

					writeFile.flush();

					// on a fini de traiter le fichier, on transfere son url
					// sous forme du chemin du fichier telechargé dans la liste
					// des fichiers telechargés
					ListeAttente.removeElementAt(EnAttente.getSelectedIndex());
					nbFichierADL = EnAttente.getModel().getSize();

					ListeOk.addElement(cheminFichier);
					int nbFichierDejaDL = Fini.getModel().getSize();

					i = 0;
					LabelNombreaDL.setText("nb de fichier à DL : "
							+ nbFichierADL);

					LabelDejaDL.setText("nb de fichier téléchargé(s) : "
							+ nbFichierDejaDL);
					Ouvrir.setVisible(true);
					Explorer.setVisible(true);

				}
			}

			catch (IOException e) {
				System.out.println("Error while trying to download the file.");

				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Problème lors du telechargement\n\r" + e, "Erreur",
						JOptionPane.INFORMATION_MESSAGE);
			}

		}

		// on ferme tt les flux avant de sortir du thread
		try {
			writeFile.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ListeAttente.removeAllElements();

		GO.setEnabled(true);

		JOptionPane.showMessageDialog(null,
				"Tous les fichiers sont téléchargés", "Ok",
				JOptionPane.INFORMATION_MESSAGE);

		TexteVitesse.setText("Vitesse Actuelle : 0 Ko/s");
		Barreprogression.setValue(0);
		Barreprogression.setString("0 %");
		FichierActuel.setText("");
		TexteTailleTotale.setText("");

	}

	private void waitThread() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}

	}

	public void pause() {
		pause = true;
	}

	public void reprise() {
		pause = false;
		synchronized (this) {
			this.notifyAll();
		}
	}

	/*
	 * private String CalculTempsEcoulé(Calendar depart) { int nbDheure; int
	 * nbMinutes; int nbSeconde; Calendar maintenant = Calendar.getInstance();
	 * int heuredepart = depart.get(Calendar.HOUR_OF_DAY); int minutedepart =
	 * depart.get(Calendar.MINUTE); int secondedepart =
	 * depart.get(Calendar.SECOND); int heureActu =
	 * maintenant.get(Calendar.HOUR_OF_DAY); int minuteActu = maintenant.get
	 * (Calendar.MINUTE); int secondeActu = maintenant.get(Calendar.SECOND);
	 * nbDheure = heureActu-heuredepart; nbMinutes=minuteActu-minutedepart;
	 * nbSeconde=secondeActu-secondedepart; return
	 * nbDheure+" Heure(s) "+nbMinutes+" minute(s) "+nbSeconde+" seconde(s) "; }
	 * private String CalculTempsRestant(int TailleTotale,double
	 * tailleEncours,long vitesse) { long TailleRestante=(int)
	 * (TailleTotale-tailleEncours); long
	 * secondeRestante=TailleRestante/(vitesse+1); long minutesRestante=0; long
	 * heureRestante=0; if (secondeRestante>59){ long
	 * enminute=secondeRestante/60; minutesRestante=minutesRestante+enminute;
	 * secondeRestante=secondeRestante-(enminute*60); } if (minutesRestante>59){
	 * long enheure=minutesRestante/60; heureRestante=heureRestante+enheure;
	 * minutesRestante=minutesRestante-(enheure*60); } return
	 * heureRestante+" Heure(s) "
	 * +minutesRestante+" minute(s) "+secondeRestante+" seconde(s) "; /* if
	 * (secondeRestant>59){ int enminute=secondeRestant/60;
	 * minuteRestant=minuteRestant+enminute;
	 * secondeRestant=secondeRestant-(enminute*60); } if (minuteRestant>59){ int
	 * enheure=minuteRestant/60; heureRestant=heureRestant+enheure;
	 * minuteRestant=minuteRestant-(enheure*60); }
	 */

	// return
	// heureRestant+" Heure(s) "+minuteRestant+" minute(s) "+secondeRestant+" seconde(s) ";

	// }

	public static double floor(double a, int decimales, double plus) {
		double p = Math.pow(10.0, decimales);
		// return Math.floor((a*p) + 0.5) / p; // avec arrondi éventuel (sans
		// arrondi >>>> + 0.0
		return Math.floor((a * p) + plus) / p;
	}

}
