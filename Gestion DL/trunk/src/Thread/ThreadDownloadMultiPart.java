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

import multiPartDownload.Parameters;
import multiPartDownload.RequestProcessor;
import GestionFichier.ManipFichier;

public class ThreadDownloadMultiPart extends Thread {

	// tte les decalration necessaire...

	protected String adresse;
	protected JProgressBar Barreprogression, p1, p2, p3, p4, p5, p6, p7, p8;
	protected JLabel TexteTailleTotale, TexteVitesse, FichierActuel,
			LabelNombreaDL, LabelDejaDL, label1, label2, label3, label4,
			label5, label6, label7, label8;

	protected JButton Ouvrir, GO, Explorer, TelechListe;
	protected DefaultListModel ListeAttente, ListeOk;
	protected JList EnAttente, Fini;
	private volatile boolean pause = false;
	protected int nbDeThread;

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

	public ThreadDownloadMultiPart(int nbDeThread, JProgressBar progress,
			JLabel FileInProgress, JLabel TailleTotale, JLabel vitesse,
			JLabel EncoreADL, JLabel DEJADL, JButton Start, JButton Open,
			JButton Explore, DefaultListModel enattente, DefaultListModel fait,
			JList Entrée, JList Sortie, JProgressBar p1, JProgressBar p2,
			JProgressBar p3, JProgressBar p4, JProgressBar p5, JProgressBar p6,
			JProgressBar p7, JProgressBar p8, JLabel label1, JLabel label2,
			JLabel label3, JLabel label4, JLabel label5, JLabel label6,
			JLabel label7, JLabel label8, JButton TelechListe)

	{
		this.nbDeThread = nbDeThread;
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
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.p5 = p5;
		this.p6 = p6;
		this.p7 = p7;
		this.p8 = p8;
		this.label1 = label1;
		this.label2 = label2;
		this.label3 = label3;
		this.label4 = label4;
		this.label5 = label5;
		this.label6 = label6;
		this.label7 = label7;
		this.label8 = label8;
		this.TelechListe = TelechListe;
	}

	public void run() {
		String RepParDefault = ManipFichier.OpenFolder();
		GO.setEnabled(false);
		FileOutputStream writeFile = null;
		InputStream input = null;
		String cheminFichier = null;
		long HeureDebut;
		long HeureActuelle;

		int nbFichierADL = EnAttente.getModel().getSize();
		if (nbFichierADL == 0) {
			JOptionPane.showMessageDialog(null, "Aucun lien à télécharger",
					"Erreur", JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			LabelNombreaDL.setText("nb de fichier à DL : " + nbFichierADL);
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

				// long HeureDebut = System.currentTimeMillis();
				String adresse = ListeAttente.getElementAt(
						EnAttente.getSelectedIndex()).toString();
				// int
				// prefNumberOfThread=Parameters.getInstance().getDownloadNumberOfThreads();

				p1.setValue(0);
				p1.setString("0 %");
				p2.setValue(0);
				p2.setString("0 %");
				p3.setValue(0);
				p3.setString("0 %");
				p4.setValue(0);
				p4.setString("0 %");
				label1.setText("Préparation...");
				label2.setText("Préparation...");
				label3.setText("Préparation...");
				label4.setText("Préparation...");
				p5.setValue(0);
				p5.setString("0 %");
				p6.setValue(0);
				p6.setString("0 %");
				p7.setValue(0);
				p7.setString("0 %");
				p8.setValue(0);
				p8.setString("0 %");
				label5.setText("Préparation...");
				label6.setText("Préparation...");
				label7.setText("Préparation...");
				label8.setText("Préparation...");

				// on recupere les info du fichier en cours de telechargement
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
				boolean ArchiveOrExe = isArchiveOrExe(fileName);
				if (ArchiveOrExe == true) {
					// Parameters.getInstance().setDownloadNumberOfThreads(1);
					// Parameters.getInstance().store();
					// nbDeThread=1;
					HeureDebut = System.currentTimeMillis();
					File fichier = new File(cheminFichier);
					writeFile = new FileOutputStream(cheminFichier);
					// lecture par segment de 8Mo
					byte[] buffer = new byte[8192 * 1024];
					int read;

					while ((read = input.read(buffer)) > 0) {
						writeFile.write(buffer, 0, read);
						long TailleEncours = fichier.length();
						int progressionEnCours = (int) ((100 * (TailleEncours + 1)) / fileLength);

						HeureActuelle = System.currentTimeMillis();
						long Vitesse;
						if (HeureActuelle - HeureDebut != 0) {
							Vitesse = (long) (TailleEncours / (HeureActuelle - HeureDebut));
						} else {
							Vitesse = 0;
						}

						TexteVitesse.setText("Vitesse Actuelle : " + Vitesse
								+ " Ko/s");
						Barreprogression.setValue(progressionEnCours);
						Barreprogression.setString(progressionEnCours + " %");

						if (pause) {
							waitThread();
						}

					}
					writeFile.flush();

					try {
						writeFile.close();
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {

					Parameters.getInstance().setDownloadNumberOfThreads(
							nbDeThread);
					Parameters.getInstance().store();
					int prefNumberOfThread = Parameters.getInstance()
							.getDownloadNumberOfThreads();

					// on crée un nombre de fichier correspondant au nombr de
					// thread de dl pour determiner quand le dl est fini

					for (int j = 0; j < prefNumberOfThread; j++) {
						String FileName = "./encours" + j;
						File encours = new File(FileName);
						encours.createNewFile();

					}

					RequestProcessor proc = new RequestProcessor(null);

					proc.processRequest(new URL(adresse), RepParDefault, p1,
							p2, p3, p4, p5, p6, p7, p8, label1, label2, label3,
							label4, label5, label6, label7, label8);

					boolean file1exist = true;
					boolean file2exist = true;
					boolean file3exist = true;
					boolean file4exist = true;
					boolean file5exist = true;
					boolean file6exist = true;
					boolean file7exist = true;
					boolean file8exist = true;
					while (file1exist == true || file2exist == true
							|| file3exist == true || file4exist == true
							|| file5exist == true || file6exist == true
							|| file7exist == true || file8exist == true) {
						// for (int k=0;k<prefNumberOfThread;k++){

						File encours1 = new File("./encours0");
						file1exist = encours1.exists();
						File encours2 = new File("./encours1");
						file2exist = encours2.exists();
						File encours3 = new File("./encours2");
						file3exist = encours3.exists();
						File encours4 = new File("./encours3");
						file4exist = encours4.exists();

						File encours5 = new File("./encours4");
						file5exist = encours5.exists();
						File encours6 = new File("./encours5");
						file6exist = encours6.exists();
						File encours7 = new File("./encours6");
						file7exist = encours7.exists();
						File encours8 = new File("./encours7");
						file8exist = encours8.exists();

						// int ProgressTotal =
						// ((p1.getValue())/nbDeThread)+((p2.getValue())/nbDeThread)+((p3.getValue())/nbDeThread)+((p4.getValue())/nbDeThread)+((p5.getValue())/nbDeThread)+((p6.getValue())/nbDeThread)+((p7.getValue())/nbDeThread)+((p8.getValue())/nbDeThread);
						int ProgressTotal = (p1.getValue() + p2.getValue()
								+ p3.getValue() + p4.getValue() + p5.getValue()
								+ p6.getValue() + p7.getValue() + p8.getValue())
								/ nbDeThread;

						Barreprogression.setValue(ProgressTotal);
						Barreprogression.setString(ProgressTotal + " %");

						String vitesse1, vitesse2, vitesse3, vitesse4, vitesse5, vitesse6, vitesse7, vitesse8;
						if (label1.getText().contains("Vitesse") == true
								|| label2.getText().contains("Vitesse") == true
								|| label3.getText().contains("Vitesse") == true
								|| label4.getText().contains("Vitesse") == true
								|| label5.getText().contains("Vitesse") == true
								|| label6.getText().contains("Vitesse") == true
								|| label7.getText().contains("Vitesse") == true
								|| label8.getText().contains("Vitesse") == true) {
							if (label1.getText().contains("Vitesse") == true) {
								vitesse1 = label1.getText().substring(
										label1.getText().lastIndexOf(":") + 1,
										label1.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse1 = "0";
							}
							if (label2.getText().contains("Vitesse") == true) {
								vitesse2 = label2.getText().substring(
										label2.getText().lastIndexOf(":") + 1,
										label2.getText().lastIndexOf("Ko/s"))
										.trim();

							} else {
								vitesse2 = "0";
							}
							if (label3.getText().contains("Vitesse") == true) {
								vitesse3 = label3.getText().substring(
										label3.getText().lastIndexOf(":") + 1,
										label3.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse3 = "0";
							}
							if (label4.getText().contains("Vitesse") == true) {
								vitesse4 = label4.getText().substring(
										label4.getText().lastIndexOf(":") + 1,
										label4.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse4 = "0";
							}
							if (label5.getText().contains("Vitesse") == true) {
								vitesse5 = label5.getText().substring(
										label5.getText().lastIndexOf(":") + 1,
										label5.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse5 = "0";
							}
							if (label6.getText().contains("Vitesse") == true) {
								vitesse6 = label6.getText().substring(
										label6.getText().lastIndexOf(":") + 1,
										label6.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse6 = "0";
							}
							if (label7.getText().contains("Vitesse") == true) {
								vitesse7 = label7.getText().substring(
										label7.getText().lastIndexOf(":") + 1,
										label7.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse7 = "0";
							}
							if (label8.getText().contains("Vitesse") == true) {
								vitesse8 = label8.getText().substring(
										label8.getText().lastIndexOf(":") + 1,
										label8.getText().lastIndexOf("Ko/s"))
										.trim();
							} else {
								vitesse8 = "0";
							}

							int vitesseTotale = Integer.parseInt(vitesse1)
									+ Integer.parseInt(vitesse2)
									+ Integer.parseInt(vitesse3)
									+ Integer.parseInt(vitesse4)
									+ Integer.parseInt(vitesse5)
									+ Integer.parseInt(vitesse6)
									+ Integer.parseInt(vitesse7)
									+ Integer.parseInt(vitesse8);
							TexteVitesse.setText("Vitesse : " + vitesseTotale
									+ "Ko/s");
						}
					}

				}

				if (pause) {
					waitThread();
				}

				p1.setValue(0);
				p1.setString("0 %");
				p2.setValue(0);
				p2.setString("0 %");
				p3.setValue(0);
				p3.setString("0 %");
				p4.setValue(0);
				p4.setString("0 %");
				label1.setText("Préparation...");
				label2.setText("Préparation...");
				label3.setText("Préparation...");
				label4.setText("Préparation...");
				p5.setValue(0);
				p5.setString("0 %");
				p6.setValue(0);
				p6.setString("0 %");
				p7.setValue(0);
				p7.setString("0 %");
				p8.setValue(0);
				p8.setString("0 %");
				label5.setText("Préparation...");
				label6.setText("Préparation...");
				label7.setText("Préparation...");
				label8.setText("Préparation...");

				// on a fini de traiter le fichier, on transfere son url sous
				// forme du chemin du fichier telechargé dans la liste des
				// fichiers telechargés
				ListeAttente.removeElementAt(EnAttente.getSelectedIndex());
				nbFichierADL = EnAttente.getModel().getSize();

				ListeOk.addElement(cheminFichier);
				int nbFichierDejaDL = Fini.getModel().getSize();

				i = 0;
				LabelNombreaDL.setText("nb de fichier à DL : " + nbFichierADL);

				LabelDejaDL.setText("nb de fichier téléchargé(s) : "
						+ nbFichierDejaDL);
				Ouvrir.setVisible(true);
				Explorer.setVisible(true);

			}

			catch (IOException e) {
				System.out.println("Error while trying to download the file.");

				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Problème lors du telechargement\n\r" + e, "Erreur",
						JOptionPane.INFORMATION_MESSAGE);
				GO.setEnabled(true);
			}
		}

		// on ferme tt les flux avant de sortir du thread

		ListeAttente.removeAllElements();

		GO.setEnabled(true);
		TelechListe.setEnabled(true);

		JOptionPane.showMessageDialog(null,
				"Tous les fichiers sont téléchargés", "Ok",
				JOptionPane.INFORMATION_MESSAGE);

		TexteVitesse.setText("Vitesse Actuelle : 0 Ko/s");
		Barreprogression.setValue(0);
		Barreprogression.setString("0 %");
		FichierActuel.setText("");
		TexteTailleTotale.setText("");
		p1.setVisible(false);
		p2.setVisible(false);
		p3.setVisible(false);
		p4.setVisible(false);
		label1.setVisible(false);
		label2.setVisible(false);
		label3.setVisible(false);
		label4.setVisible(false);
		p5.setVisible(false);
		p6.setVisible(false);
		p7.setVisible(false);
		p8.setVisible(false);
		label5.setVisible(false);
		label6.setVisible(false);
		label7.setVisible(false);
		label8.setVisible(false);

	}

	private boolean isArchiveOrExe(String fileName) {
		// TODO Auto-generated method stub
		fileName = fileName.toUpperCase();
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		String listeExclu = Parameters.getInstance().getListeExclusion();

		String[] tabChaine = listeExclu.split(";");

		for (int i = 0; i < tabChaine.length; i++) {
			if (tabChaine[i].toUpperCase().equals(extension) == true) {
				return true;
			}
		}
		return false;

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

	public static double floor(double a, int decimales, double plus) {
		double p = Math.pow(10.0, decimales);
		// return Math.floor((a*p) + 0.5) / p; // avec arrondi éventuel (sans
		// arrondi >>>> + 0.0
		return Math.floor((a * p) + plus) / p;
	}

}
