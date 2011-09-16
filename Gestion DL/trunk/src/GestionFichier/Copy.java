package GestionFichier;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Utilit.Comptage;
import Utilit.Historique;
import Utilit.dateFichier;

public class Copy {
	// Dans le contructeur on va utiliser notre methode copy
	// et donc on vas faire quelques ptit test
	protected String src, dest;
	File DEST, SRC;
	int nbTotal;

	/**
	 * Copie le contenu d'un repertoire vers un autre et affiche le status de la
	 * copie dans une barre de progression.
	 * @param src -String Le répertoire source
	 * @param dest -String le repertoire de destination
	 * @param nbTotal -int le nb total de fichier a copier qui permet de
	 *            calculer la progression.
	 * @param progress -JProgressBar la barre de progression.
	 * @param sortieModel -DefaultModelList model de liste
	 * @param sortieList -JList le composant JList.
	 */

	public Copy(String src, String dest, final int nbTotal,
			final JProgressBar progressTotal,
			final JProgressBar progressEnCours, JTextField message,
			final String RepRacineLocal, final JLabel label) {

		this.nbTotal = nbTotal;
		this.src = src;
		this.dest = dest;
		this.SRC = new File(src);
		this.DEST = new File(dest);
		// ben si le rep dest n'existe pas, et notre source est un repertoire
		if (!DEST.exists()) {
			if (SRC.isDirectory()) {
				// Alors on cree un rep destination

				Historique.ecrire("Création du répértoire  " + DEST);

				DEST.mkdir();
				// nbEncours++;
			}
		}
		// Mais si jammais c'est un fichier, on fait un simple copie
		if (SRC.isFile()) {
			if (src.indexOf(".exe") != -1) {
				FixeDateSystemeALaDateDeCreationDuFichier(src);

				Historique.ecrire("Copie de  " + SRC + "  vers  " + DEST);

			}

			copyAvecProgress(SRC, DEST, progressEnCours);

			long date = dateFichier.getFileDateTime(src);
			DEST.setLastModified(date);
			// sortieModel.addElement(DEST);
			message.setText(DEST.toString());

			// et si notre source est un repertoire qu'on doit copié!!!
		} else if (SRC.isDirectory()) {
			// on parcour tout les elements de ce catalogue,
			for (File f : SRC.listFiles()) {
				// et hop on fait un appel recursif a cette classe en mettant a
				// jour les path de src et dest: et le tour est joué
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						/**
						 * {@inheritDoc}
						 */
						@Override
						public void run() {

							Comptage count = new Comptage(RepRacineLocal);
							int nbEncours = count.getNbFichier();

							// int nbEncours =
							// GestionRepertoire.CompteNbFichierDansRepEtSousRep(dest);
							// int nbEncours = sortieList.getModel().getSize();;
							int PourcentProgression = (100 * (nbEncours + 1))
									/ nbTotal;
							label.setText(nbEncours + " / " + nbTotal);
							/*
							 * if (nbEncours>0){ sortieList.setSelectedIndex
							 * (nbEncours);
							 * sortieList.ensureIndexIsVisible(sortieList
							 * .getSelectedIndex()); }
							 */

							progressTotal.setValue(PourcentProgression);
							progressTotal.setString("Total : "
									+ PourcentProgression + " %");

						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				new Copy(f.getAbsolutePath(), DEST.getAbsoluteFile() + "/"
						+ f.getName(), nbTotal, progressTotal, progressEnCours,
						message, RepRacineLocal, label);
				// new
				// Copy(f.getAbsolutePath(),DEST.getAbsoluteFile()+"/"+f.getName(),nbTotal,progress,sortieModel,sortieList,message);
			}
		}

	}

	/**
	 * Permet de fixer la date systeme en fonction de la date de création d'un
	 * fichier
	 * @param cheminDuFichier -String le fichier dont on se sert pour fixer la
	 *            date Systeme
	 */
	public static void FixeDateSystemeALaDateDeCreationDuFichier(
			String cheminDuFichier) {
		// TODO Auto-generated method stub
		// on créer la commande qui servira a recuperer la date du fichier

		Runtime r = Runtime.getRuntime();
		String cmdRecupDate = String.format(
				"cmd.exe /c dir /TC %s | find \"/\"  > tmp.txt",
				cheminDuFichier);
		try {
			Process p = r.exec(cmdRecupDate);
			try {
				p.waitFor();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		// on extrait la date systeme du fichier text et on fixe la date systeme
		String cmdSetDate = String
				.format("cmd.exe /c FOR /F \"tokens=1-4 delims= \" %%i in (tmp.txt) do DATE %%i");
		try {
			Process p = r.exec(cmdSetDate);
			try {
				p.waitFor();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		String cmdEffaceTmpText = String.format("cmd.exe /c del tmp.txt");
		try {
			Process p = r.exec(cmdEffaceTmpText);
			try {
				p.waitFor();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * copie le fichier source dans le fichier resultat retourne vrai si cela
	 * réussit
	 * @param source -File fichier a copier
	 * @param destination -File destination
	 */
	/*
	 * private boolean copy( File source, File destination ) { boolean resultat
	 * = false; //Déclaration des stream d'entree sortie java.io.FileInputStream
	 * sourceFile=null; java.io.FileOutputStream destinationFile=null; try { //
	 * Création du fichier : destination.createNewFile(); // Ouverture des flux
	 * sourceFile = new java.io.FileInputStream(source); destinationFile = new
	 * java.io.FileOutputStream(destination); // Lecture par segment de 0.5Mo
	 * byte buffer[]=new byte[512*1024]; int nbLecture; while( (nbLecture =
	 * sourceFile.read(buffer)) != -1 ) { destinationFile.write(buffer, 0,
	 * nbLecture); } // si tout va bien resultat = true; } catch(
	 * java.io.FileNotFoundException f ) { } catch( java.io.IOException e ) { }
	 * finally { // Quelque soit on ferme les flux try { sourceFile.close(); }
	 * catch(Exception e) { } try { destinationFile.close(); } catch(Exception
	 * e) { } } return( resultat ); }
	 */private boolean copyAvecProgress(File sRC2, File dEST2,
			JProgressBar progressEnCours) {
		boolean resultat = false;
		long PourcentEnCours = 0;
		progressEnCours.setValue(0);
		progressEnCours.setString("Fichier en cours : 0 %");

		// Déclaration des stream d'entree sortie
		java.io.FileInputStream sourceFile = null;
		java.io.FileOutputStream destinationFile = null;

		try {
			// Création du fichier :
			dEST2.createNewFile();

			// Ouverture des flux
			sourceFile = new java.io.FileInputStream(sRC2);
			destinationFile = new java.io.FileOutputStream(dEST2);

			long tailleTotale = sRC2.length();

			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;

			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
				long tailleEnCours = dEST2.length();
				PourcentEnCours = ((100 * (tailleEnCours + 1)) / tailleTotale);
				int Pourcent = (int) PourcentEnCours;
				progressEnCours.setValue(Pourcent);
				progressEnCours.setString("Fichier en cours : " + Pourcent
						+ " %");
			}

			// si tout va bien
			resultat = true;
		} catch (java.io.FileNotFoundException f) {

		} catch (java.io.IOException e) {

		} finally {
			// Quelque soit on ferme les flux
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
}
