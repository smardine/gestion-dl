package Thread;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import GestionFichier.ModifPressPap;

public class Thread_VerifPressePapier extends Thread {

	protected DefaultListModel ListeAttente;
	// protected JCheckBox SurveillanceActive;
	protected JButton SurveillanceActive;

	public Thread_VerifPressePapier(DefaultListModel enattente,
			JButton surveillance) {
		ListeAttente = enattente;
		SurveillanceActive = surveillance;
	}

	public void run() {
		ModifPressPap mpp = new ModifPressPap();
		mpp.setClipboardContents("");
		String LiensOrigine = "";
		boolean actif = SurveillanceActive.isEnabled();
		if (actif == true) {
			while (actif == true) {
				String Liens = mpp.getClipboardContents();
				if (Liens.equals(LiensOrigine)) {
					// le lien est le meme que celui precedement recuperé, on ne
					// fait rien
				} else {// si les liens sont différents, on recopie le lien dans
						// la fenetre des ficheirs a telecharger

					ListeAttente.addElement(mpp.getClipboardContents());
					// LiensOrigine=Liens;
					// waitThread(1000);
					mpp.setClipboardContents("");

				}
				actif = SurveillanceActive.isEnabled();// on verifie si la case
														// "surveillance est
														// tjrs cochée, si oui,
														// on continu la
														// surveillance, sinon,
														// on sort

			}

		}

	}

	/*
	 * private void waitThread(long milisecond) { synchronized(this) { try {
	 * this.wait(milisecond); } catch (InterruptedException ie) {
	 * ie.printStackTrace(); JOptionPane.showMessageDialog(null,
	 * "Erreur lors de la mise en pause : \n\r" + ie,"Erreur",
	 * JOptionPane.INFORMATION_MESSAGE); } } }
	 */

}
