package dialogue;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import multiPartDownload.DownloadListUpdater;
import multiPartDownload.Parameters;
import GestionFichier.ManipFichier;
import GestionFichier.ReadFile;
import Thread.ThreadDownloadHTM;
import Thread.ThreadDownloadMultiPart;
import Thread.ThreadVerifMaj;
import action.OpenWithDefaultViewer;
import java.awt.Font;

public class FEN_Download extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	protected DefaultListModel listModelEnAttente_Liste, listModelFait_Liste,
			listModelEnAttente_Liste_Htm, listModelFait_Liste_Htm,
			listModelEnAttente_Liste_PressePapier,
			listModelFait_Liste_PressePapier,
			listModelEnAttente_Liste_DragAndDrop,
			listModelFait_Liste_DragAndDrop;
	private ThreadDownloadHTM dlhtm = null;
	private ThreadDownloadMultiPart dlMulti = null;
	private int enpause = 0;
	private JTabbedPane jTabbedPane = null;
	private JButton parcourirListe = null;
	private JButton LanceTelechSelect = null;
	private JButton parcourirHtm = null;
	private JPanel DragAndDrop = null;
	private JProgressBar Encours_DragAndDrop = null;
	private JLabel Total_DragAndDrop = null;
	private JLabel vitesse_DragAndDrop = null;
	private JButton LanceTelechAll = null;
	private JButton ouvrir_DragAndDrop = null;
	private JLabel nbFichier_DragAndDrop = null;
	private JLabel FichierEnCours_DragAndDrop = null;
	private JButton explorer_DragAndDrop = null;
	private JLabel TailleFichier_DragAndDrop = null;
	private JLabel DejaDL_DragAndDrop = null;
	private JButton pause_DragAndDrop = null;
	private JButton reprise_DragAndDrop = null;
	private JScrollPane Fait_DragAndDrop = null;
	private JList ListeFait_DragAndDrop = null;
	private JScrollPane AFaire_DragAndDrop = null;
	private JList ListeAFaire_DragAndDrop = null;
	private JButton EffaceListe_DragAndDrop = null;
	private JCheckBox ActiveDragAndDrop_jCheckBox = null;
	private JProgressBar jProgressBar_Thread0 = null;
	private JProgressBar jProgressBar_Thread1 = null;
	private JProgressBar jProgressBar_Thread2 = null;
	private JProgressBar jProgressBar_Thread3 = null;
	private JLabel jLabel_Thread0 = null;
	private JLabel jLabel_Tread1 = null;
	private JLabel jLabel_Tread2 = null;
	private JLabel jLabel_Thread3 = null;
	private JProgressBar jProgressBar_Thread4 = null;
	private JProgressBar jProgressBar_Thread5 = null;
	private JProgressBar jProgressBar_Thread6 = null;
	private JProgressBar jProgressBar_Thread7 = null;
	private JLabel jLabel_Thread4 = null;
	private JLabel jLabel_Thread5 = null;
	private JLabel jLabel_Thread6 = null;
	private JLabel jLabel_Thread7 = null;
	private JSlider jSlider = null;
	private JLabel jLabel = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField_ListeExclut = null;
	private JButton jButton = null;
	private JButton jButton1 = null;

	/**
	 * This method initializes jTabbedPane
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(new Rectangle(3, 3, 932, 583));
			jTabbedPane.setVisible(true);
			jTabbedPane.addTab("Telechargement", null, getDragAndDrop(), null);
			jTabbedPane.addTab("Parametres", null, getJPanel(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes parcourirListe
	 * @return javax.swing.JButton
	 */
	private JButton getParcourirListe() {
		if (parcourirListe == null) {
			parcourirListe = new JButton();
			parcourirListe.setText(" Choisir liste");
			parcourirListe.setFont(new Font("Candara", Font.PLAIN, 12));
			parcourirListe.setBounds(new Rectangle(32, 9, 104, 26));
			parcourirListe
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							listModelEnAttente_Liste_DragAndDrop
									.removeAllElements();
							String cheminListe = ManipFichier.OpenFile();
							// cheminListejTextField.setText(cheminListe);
							int nbDeFichier = ReadFile.ReadLine(cheminListe,
									listModelEnAttente_Liste_DragAndDrop);
							nbFichier_DragAndDrop
									.setText("nb de fichier à DL : "
											+ nbDeFichier);

						}
					});
		}
		return parcourirListe;
	}

	/**
	 * This method initializes LanceTelechSelect
	 * @return javax.swing.JButton
	 */
	private JButton getLanceTelechSelect() {
		if (LanceTelechSelect == null) {
			LanceTelechSelect = new JButton();
			LanceTelechSelect.setText(" Telecharger les lignes selectionées");
			LanceTelechSelect.setFont(new Font("Candara", Font.PLAIN, 12));
			LanceTelechSelect.setBounds(new Rectangle(622, 237, 239, 26));
			LanceTelechSelect
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							listModelFait_Liste_DragAndDrop.removeAllElements();
							pause_DragAndDrop.setEnabled(true);
							LanceTelechAll.setEnabled(false);
							int nbDeThread = jSlider.getValue();
							dlMulti = null;
							dlhtm = new ThreadDownloadHTM(nbDeThread,
									Encours_DragAndDrop,
									FichierEnCours_DragAndDrop,
									Total_DragAndDrop, vitesse_DragAndDrop,
									nbFichier_DragAndDrop, DejaDL_DragAndDrop,
									LanceTelechSelect, ouvrir_DragAndDrop,
									explorer_DragAndDrop,
									listModelEnAttente_Liste_DragAndDrop,
									listModelFait_Liste_DragAndDrop,
									ListeAFaire_DragAndDrop,
									ListeFait_DragAndDrop,
									jProgressBar_Thread0, jProgressBar_Thread1,
									jProgressBar_Thread2, jProgressBar_Thread3,
									jProgressBar_Thread4, jProgressBar_Thread5,
									jProgressBar_Thread6, jProgressBar_Thread7,
									jLabel_Thread0, jLabel_Tread1,
									jLabel_Tread2, jLabel_Thread3,
									jLabel_Thread4, jLabel_Thread5,
									jLabel_Thread6, jLabel_Thread7,
									LanceTelechAll);
							dlhtm.start();

						}

					});
		}
		return LanceTelechSelect;
	}

	/**
	 * This method initializes parcourirHtm
	 * @return javax.swing.JButton
	 */
	private JButton getParcourirHtm() {
		if (parcourirHtm == null) {
			parcourirHtm = new JButton();
			parcourirHtm.setText(" Choisir fichier .htm");
			parcourirHtm.setFont(new Font("Candara", Font.PLAIN, 12));
			parcourirHtm.setBounds(new Rectangle(142, 8, 144, 26));
			parcourirHtm.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// System.out.println("actionPerformed()"); // TODO
					// Auto-generated Event stub actionPerformed()
					// System.out.println("actionPerformed()"); // TODO
					// Auto-generated Event stub actionPerformed()
					listModelEnAttente_Liste_DragAndDrop.removeAllElements();
					String cheminListe = ManipFichier.OpenFile();
					// cheminListejTextField1.setText(cheminListe);
					int nbDeFichier = ReadFile.ReadLineHTM(cheminListe,
							listModelEnAttente_Liste_DragAndDrop);
					nbFichier_DragAndDrop.setText("nb de fichier à DL : "
							+ nbDeFichier);

				}
			});
		}
		return parcourirHtm;
	}

	/**
	 * This method initializes DragAndDrop
	 * @return javax.swing.JPanel
	 */
	private JPanel getDragAndDrop() {
		if (DragAndDrop == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(689, 441, 177, 23));
			jLabel.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel.setText("  Nombres de parties");
			jLabel_Thread7 = new JLabel();
			jLabel_Thread7.setBounds(new Rectangle(521, 526, 141, 22));
			jLabel_Thread7.setText("");
			jLabel_Thread7.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread7.setVisible(false);
			jLabel_Thread6 = new JLabel();
			jLabel_Thread6.setBounds(new Rectangle(521, 488, 141, 22));
			jLabel_Thread6.setText("");
			jLabel_Thread6.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread6.setVisible(false);
			jLabel_Thread5 = new JLabel();
			jLabel_Thread5.setBounds(new Rectangle(521, 456, 141, 22));
			jLabel_Thread5.setText("");
			jLabel_Thread5.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread5.setVisible(false);
			jLabel_Thread4 = new JLabel();
			jLabel_Thread4.setBounds(new Rectangle(521, 424, 141, 22));
			jLabel_Thread4.setText("");
			jLabel_Thread4.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread4.setVisible(false);
			jLabel_Thread3 = new JLabel();
			jLabel_Thread3.setBounds(new Rectangle(195, 526, 141, 22));
			jLabel_Thread3.setText("");
			jLabel_Thread3.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread3.setVisible(false);
			jLabel_Tread2 = new JLabel();
			jLabel_Tread2.setBounds(new Rectangle(195, 488, 141, 22));
			jLabel_Tread2.setText("");
			jLabel_Tread2.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Tread2.setVisible(false);
			jLabel_Tread1 = new JLabel();
			jLabel_Tread1.setBounds(new Rectangle(195, 456, 141, 22));
			jLabel_Tread1.setText("");
			jLabel_Tread1.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Tread1.setVisible(false);
			jLabel_Thread0 = new JLabel();
			jLabel_Thread0.setBounds(new Rectangle(195, 424, 141, 22));
			jLabel_Thread0.setText("");
			jLabel_Thread0.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel_Thread0.setVisible(false);
			DejaDL_DragAndDrop = new JLabel();
			DejaDL_DragAndDrop.setBounds(new Rectangle(32, 267, 579, 20));
			DejaDL_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			DejaDL_DragAndDrop.setText("");
			TailleFichier_DragAndDrop = new JLabel();
			TailleFichier_DragAndDrop
					.setBounds(new Rectangle(250, 40, 105, 25));
			TailleFichier_DragAndDrop.setFont(new Font("Candara", Font.PLAIN,
					12));
			TailleFichier_DragAndDrop.setText(" Taille du fichier :");
			FichierEnCours_DragAndDrop = new JLabel();
			FichierEnCours_DragAndDrop
					.setBounds(new Rectangle(32, 75, 579, 25));
			FichierEnCours_DragAndDrop.setFont(new Font("Candara", Font.PLAIN,
					12));
			FichierEnCours_DragAndDrop.setText("");
			nbFichier_DragAndDrop = new JLabel();
			nbFichier_DragAndDrop.setBounds(new Rectangle(32, 102, 579, 25));
			nbFichier_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			nbFichier_DragAndDrop.setText("");
			vitesse_DragAndDrop = new JLabel();
			vitesse_DragAndDrop.setBounds(new Rectangle(436, 40, 174, 25));
			vitesse_DragAndDrop.setText("");
			vitesse_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			vitesse_DragAndDrop.setHorizontalAlignment(SwingConstants.CENTER);
			Total_DragAndDrop = new JLabel();
			Total_DragAndDrop.setBounds(new Rectangle(368, 40, 65, 25));
			Total_DragAndDrop.setText("");
			Total_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			Total_DragAndDrop.setHorizontalAlignment(SwingConstants.CENTER);
			DragAndDrop = new JPanel();
			DragAndDrop.setLayout(null);
			DragAndDrop.add(getEncours_DragAndDrop(), null);
			DragAndDrop.add(Total_DragAndDrop, null);
			DragAndDrop.add(vitesse_DragAndDrop, null);
			DragAndDrop.add(getLanceTelechSelect(), null);
			DragAndDrop.add(getParcourirHtm(), null);
			DragAndDrop.add(getParcourirListe(), null);
			DragAndDrop.add(getLanceTelechAll(), null);
			DragAndDrop.add(getOuvrir_DragAndDrop(), null);
			DragAndDrop.add(nbFichier_DragAndDrop, null);
			DragAndDrop.add(FichierEnCours_DragAndDrop, null);
			DragAndDrop.add(getExplorer_DragAndDrop(), null);
			DragAndDrop.add(TailleFichier_DragAndDrop, null);
			DragAndDrop.add(DejaDL_DragAndDrop, null);
			DragAndDrop.add(getPause_DragAndDrop(), null);
			DragAndDrop.add(getReprise_DragAndDrop(), null);
			DragAndDrop.add(getFait_DragAndDrop(), null);
			DragAndDrop.add(getAFaire_DragAndDrop(), null);
			DragAndDrop.add(getEffaceListe_DragAndDrop(), null);
			DragAndDrop.add(getActiveDragAndDrop_jCheckBox(), null);
			DragAndDrop.add(getJProgressBar_Thread0(), null);
			DragAndDrop.add(getJProgressBar_Thread1(), null);
			DragAndDrop.add(getJProgressBar_Thread2(), null);
			DragAndDrop.add(getJProgressBar_Thread3(), null);
			DragAndDrop.add(jLabel_Thread0, null);
			DragAndDrop.add(jLabel_Tread1, null);
			DragAndDrop.add(jLabel_Tread2, null);
			DragAndDrop.add(jLabel_Thread3, null);
			DragAndDrop.add(getJProgressBar_Thread4(), null);
			DragAndDrop.add(getJProgressBar_Thread5(), null);
			DragAndDrop.add(getJProgressBar_Thread6(), null);
			DragAndDrop.add(getJProgressBar_Thread7(), null);
			DragAndDrop.add(jLabel_Thread4, null);
			DragAndDrop.add(jLabel_Thread5, null);
			DragAndDrop.add(jLabel_Thread6, null);
			DragAndDrop.add(jLabel_Thread7, null);
			DragAndDrop.add(getJSlider(), null);
			DragAndDrop.add(jLabel, null);
			DragAndDrop.add(getJButton1(), null);
			new DownloadListUpdater(ListeAFaire_DragAndDrop);
		}
		return DragAndDrop;
	}

	/**
	 * This method initializes Encours_DragAndDrop
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getEncours_DragAndDrop() {
		if (Encours_DragAndDrop == null) {
			Encours_DragAndDrop = new JProgressBar();
			Encours_DragAndDrop.setBounds(new Rectangle(32, 40, 211, 25));
			Encours_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			Encours_DragAndDrop.setStringPainted(true);
		}
		return Encours_DragAndDrop;
	}

	/**
	 * This method initializes LanceTelechAll
	 * @return javax.swing.JButton
	 */
	private JButton getLanceTelechAll() {
		if (LanceTelechAll == null) {
			LanceTelechAll = new JButton();
			LanceTelechAll.setBounds(new Rectangle(622, 206, 239, 26));
			LanceTelechAll.setFont(new Font("Candara", Font.PLAIN, 12));
			LanceTelechAll.setText(" Telecharger toute la liste");
			LanceTelechAll
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							listModelFait_Liste_DragAndDrop.removeAllElements();
							pause_DragAndDrop.setEnabled(true);
							int nbDeThread = jSlider.getValue();
							LanceTelechSelect.setEnabled(false);
							dlhtm = null;
							dlMulti = new ThreadDownloadMultiPart(nbDeThread,
									Encours_DragAndDrop,
									FichierEnCours_DragAndDrop,
									Total_DragAndDrop, vitesse_DragAndDrop,
									nbFichier_DragAndDrop, DejaDL_DragAndDrop,
									LanceTelechAll, ouvrir_DragAndDrop,
									explorer_DragAndDrop,
									listModelEnAttente_Liste_DragAndDrop,
									listModelFait_Liste_DragAndDrop,
									ListeAFaire_DragAndDrop,
									ListeFait_DragAndDrop,
									jProgressBar_Thread0, jProgressBar_Thread1,
									jProgressBar_Thread2, jProgressBar_Thread3,
									jProgressBar_Thread4, jProgressBar_Thread5,
									jProgressBar_Thread6, jProgressBar_Thread7,
									jLabel_Thread0, jLabel_Tread1,
									jLabel_Tread2, jLabel_Thread3,
									jLabel_Thread4, jLabel_Thread5,
									jLabel_Thread6, jLabel_Thread7,
									LanceTelechSelect);
							dlMulti.start();
						}
					});
		}
		return LanceTelechAll;
	}

	/**
	 * This method initializes ouvrir_DragAndDrop
	 * @return javax.swing.JButton
	 */
	private JButton getOuvrir_DragAndDrop() {
		if (ouvrir_DragAndDrop == null) {
			ouvrir_DragAndDrop = new JButton();
			ouvrir_DragAndDrop.setBounds(new Rectangle(624, 345, 127, 26));
			ouvrir_DragAndDrop.setText("ouvrir");
			ouvrir_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			ouvrir_DragAndDrop.setVisible(false);
			ouvrir_DragAndDrop
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							String chemin = listModelFait_Liste_DragAndDrop
									.getElementAt(
											ListeFait_DragAndDrop
													.getSelectedIndex())
									.toString();
							OpenWithDefaultViewer.open(chemin);
						}
					});
		}
		return ouvrir_DragAndDrop;
	}

	/**
	 * This method initializes explorer_DragAndDrop
	 * @return javax.swing.JButton
	 */
	private JButton getExplorer_DragAndDrop() {
		if (explorer_DragAndDrop == null) {
			explorer_DragAndDrop = new JButton();
			explorer_DragAndDrop.setBounds(new Rectangle(624, 382, 127, 26));
			explorer_DragAndDrop.setText(" Explorer");
			explorer_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			explorer_DragAndDrop.setVisible(false);
			explorer_DragAndDrop
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							String chemin = listModelFait_Liste_DragAndDrop
									.getElementAt(
											ListeFait_DragAndDrop
													.getSelectedIndex())
									.toString();
							String cheminSeul = chemin.substring(0, chemin
									.lastIndexOf("\\"));
							OpenWithDefaultViewer.open(cheminSeul);
						}
					});
		}
		return explorer_DragAndDrop;
	}

	/**
	 * This method initializes pause_DragAndDrop
	 * @return javax.swing.JButton
	 */
	private JButton getPause_DragAndDrop() {
		if (pause_DragAndDrop == null) {
			pause_DragAndDrop = new JButton();
			pause_DragAndDrop.setBounds(new Rectangle(632, 74, 79, 28));
			pause_DragAndDrop.setText("pause");
			pause_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			pause_DragAndDrop.setEnabled(false);
			pause_DragAndDrop
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							if (dlMulti != null) {
								dlMulti.pause();
								enpause++;
							}
							if (dlhtm != null) {
								dlhtm.pause();
								enpause++;
							}

							reprise_DragAndDrop.setEnabled(true);
							pause_DragAndDrop.setEnabled(false);

						}
					});
		}
		return pause_DragAndDrop;
	}

	/**
	 * This method initializes reprise_DragAndDrop
	 * @return javax.swing.JButton
	 */
	private JButton getReprise_DragAndDrop() {
		if (reprise_DragAndDrop == null) {
			reprise_DragAndDrop = new JButton();
			reprise_DragAndDrop.setBounds(new Rectangle(634, 110, 79, 26));
			reprise_DragAndDrop.setText("reprise");
			reprise_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			reprise_DragAndDrop.setEnabled(false);
			reprise_DragAndDrop
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							if (enpause >= 1) {// on est deja en pause, on remet
												// en route le download
								pause_DragAndDrop.setText("pause");

								if (dlMulti != null) {
									dlMulti.reprise();
									enpause = 0;
								}
								if (dlhtm != null) {
									dlhtm.reprise();
									enpause = 0;
								}

								reprise_DragAndDrop.setEnabled(false);
								pause_DragAndDrop.setEnabled(true);
							}
						}
					});
		}
		return reprise_DragAndDrop;
	}

	/**
	 * This method initializes Fait_DragAndDrop
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getFait_DragAndDrop() {
		if (Fait_DragAndDrop == null) {
			Fait_DragAndDrop = new JScrollPane();
			Fait_DragAndDrop.setBounds(new Rectangle(32, 292, 579, 116));
			Fait_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			Fait_DragAndDrop.setViewportView(getListeFait_DragAndDrop());
		}
		return Fait_DragAndDrop;
	}

	/**
	 * This method initializes ListeFait_DragAndDrop
	 * @return javax.swing.JList
	 */
	private JList getListeFait_DragAndDrop() {
		if (ListeFait_DragAndDrop == null) {
			ListeFait_DragAndDrop = new JList(listModelFait_Liste_DragAndDrop);
			ListeFait_DragAndDrop.setVisibleRowCount(-1);
			ListeFait_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			ListeFait_DragAndDrop.setVisible(true);
		}
		return ListeFait_DragAndDrop;
	}

	/**
	 * This method initializes AFaire_DragAndDrop
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getAFaire_DragAndDrop() {
		if (AFaire_DragAndDrop == null) {
			AFaire_DragAndDrop = new JScrollPane();
			AFaire_DragAndDrop.setBounds(new Rectangle(32, 147, 579, 116));
			AFaire_DragAndDrop.setFont(new Font("Candara", Font.PLAIN, 12));
			AFaire_DragAndDrop.setViewportView(getListeAFaire_DragAndDrop());
		}
		return AFaire_DragAndDrop;
	}

	/**
	 * This method initializes ListeAFaire_DragAndDrop
	 * @return javax.swing.JList
	 */
	private JList getListeAFaire_DragAndDrop() {
		if (ListeAFaire_DragAndDrop == null) {
			ListeAFaire_DragAndDrop = new JList(
					listModelEnAttente_Liste_DragAndDrop);
			ListeAFaire_DragAndDrop.setVisibleRowCount(-1);
			ListeAFaire_DragAndDrop.getSelectionModel().setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			ListeAFaire_DragAndDrop.setTransferHandler(new TransferHandler() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -1691671205869896738L;

				public boolean canImport(TransferHandler.TransferSupport info) {
					// we only import Strings
					if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						return false;
					}

					JList.DropLocation dl = (JList.DropLocation) info
							.getDropLocation();
					if (dl.getIndex() == -1) {
						return false;
					}
					return true;
				}

				public boolean importData(TransferHandler.TransferSupport info) {
					if (!info.isDrop()) {
						return false;
					}

					// Check for String flavor
					if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						displayDropLocation("List doesn't accept a drop of this type.");
						return false;
					}

					JList.DropLocation dl = (JList.DropLocation) info
							.getDropLocation();
					DefaultListModel listModel = (DefaultListModel) ListeAFaire_DragAndDrop
							.getModel();
					int index = dl.getIndex();

					boolean insert = dl.isInsert();
					// Get the current string under the drop.
					// String value = (String)listModel.getElementAt(index);

					// Get the string that is being dropped.
					Transferable t = info.getTransferable();
					String data;
					try {
						data = (String) t
								.getTransferData(DataFlavor.stringFlavor);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);
						return false;
					}

					// Display a dialog with the drop information.
					/*
					 * String dropValue = "\"" + data + "\" dropped "; if
					 * (dl.isInsert()) { if (dl.getIndex() == 0) { //
					 * displayDropLocation(dropValue + "at beginning of list");
					 * } else if (dl.getIndex() >=
					 * ListeAFairePressePapier1.getModel().getSize()) { //
					 * displayDropLocation(dropValue + "at end of list"); } else
					 * { String value1 =
					 * (String)ListeAFairePressePapier1.getModel
					 * ().getElementAt(dl.getIndex() - 1); String value2 =
					 * (String
					 * )ListeAFairePressePapier1.getModel().getElementAt(dl
					 * .getIndex()); //displayDropLocation(dropValue +
					 * "between \"" + value1 + "\" and \"" + value2 + "\""); } }
					 * else { // displayDropLocation(dropValue + "on top of " +
					 * "\"" + value + "\""); }
					 */

					/*
					 * This is commented out for the basicdemo.html tutorial
					 * page.* If you add this code snippet back and delete the*
					 * "return false;" line, the list will accept drops* of type
					 * string.
					 */
					// Perform the actual import.
					if (insert) {
						listModel.add(index, data);
					} else {
						listModel.set(index, data);
					}
					return true;

					// return false;
				}

				private void displayDropLocation(final String string) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(null, string);
						}
					});
				}

				public int getSourceActions(JComponent c) {
					return COPY;
				}

				protected Transferable createTransferable(JComponent c) {
					JList list = (JList) c;
					Object[] values = list.getSelectedValues();

					StringBuffer buff = new StringBuffer();

					for (int i = 0; i < values.length; i++) {
						Object val = values[i];
						buff.append(val == null ? "" : val.toString());
						if (i != values.length - 1) {
							buff.append("\n");
						}
					}
					return new StringSelection(buff.toString());
				}
			});
			ListeAFaire_DragAndDrop
					.setFont(new Font("Candara", Font.PLAIN, 12));
			ListeAFaire_DragAndDrop.setVisible(true);
			ListeAFaire_DragAndDrop.setDropMode(DropMode.ON_OR_INSERT);

		}
		return ListeAFaire_DragAndDrop;
	}

	/**
	 * This method initializes EffaceListe_DragAndDrop
	 * @return javax.swing.JButton
	 */
	private JButton getEffaceListe_DragAndDrop() {
		if (EffaceListe_DragAndDrop == null) {
			EffaceListe_DragAndDrop = new JButton();
			EffaceListe_DragAndDrop.setText("Effacer la liste");
			EffaceListe_DragAndDrop
					.setFont(new Font("Candara", Font.PLAIN, 12));
			EffaceListe_DragAndDrop.setBounds(new Rectangle(622, 147, 239, 26));
			EffaceListe_DragAndDrop
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							// System.out.println("actionPerformed()"); // TODO
							// Auto-generated Event stub actionPerformed()
							listModelEnAttente_Liste_DragAndDrop
									.removeAllElements();
						}
					});
		}
		return EffaceListe_DragAndDrop;
	}

	/**
	 * This method initializes ActiveDragAndDrop_jCheckBox
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getActiveDragAndDrop_jCheckBox() {
		if (ActiveDragAndDrop_jCheckBox == null) {
			ActiveDragAndDrop_jCheckBox = new JCheckBox();
			ActiveDragAndDrop_jCheckBox
					.setBounds(new Rectangle(616, 7, 201, 21));
			ActiveDragAndDrop_jCheckBox.setText("Active Drag And Drop");
			ActiveDragAndDrop_jCheckBox.addActionListener(this);
			ActiveDragAndDrop_jCheckBox.setActionCommand("toggleDnD");
			ActiveDragAndDrop_jCheckBox.setFont(new Font("Candara", Font.PLAIN,
					12));
			ActiveDragAndDrop_jCheckBox.setVisible(false);
			/*
			 * ActiveDragAndDrop_jCheckBox.addActionListener(new
			 * java.awt.event.ActionListener() { public void
			 * actionPerformed(java.awt.event.ActionEvent e) {
			 * System.out.println("actionPerformed()"); // TODO Auto-generated
			 * Event stub actionPerformed() } });
			 */
		}
		return ActiveDragAndDrop_jCheckBox;
	}

	public void actionPerformed(ActionEvent e) {
		if ("toggleDnD".equals(e.getActionCommand())) {
			boolean toggle = ActiveDragAndDrop_jCheckBox.isSelected();
			ListeAFaire_DragAndDrop.setDragEnabled(toggle);
			ListeFait_DragAndDrop.setDragEnabled(toggle);
		}
	}

	/**
	 * This method initializes jProgressBar_Thread0
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread0() {
		if (jProgressBar_Thread0 == null) {
			jProgressBar_Thread0 = new JProgressBar();
			jProgressBar_Thread0.setBounds(new Rectangle(31, 424, 141, 22));
			jProgressBar_Thread0.setStringPainted(true);
			jProgressBar_Thread0.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread0.setVisible(false);
		}
		return jProgressBar_Thread0;
	}

	/**
	 * This method initializes jProgressBar_Thread1
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread1() {
		if (jProgressBar_Thread1 == null) {
			jProgressBar_Thread1 = new JProgressBar();
			jProgressBar_Thread1.setBounds(new Rectangle(31, 456, 141, 22));
			jProgressBar_Thread1.setStringPainted(true);
			jProgressBar_Thread1.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread1.setVisible(false);
		}
		return jProgressBar_Thread1;
	}

	/**
	 * This method initializes jProgressBar_Thread2
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread2() {
		if (jProgressBar_Thread2 == null) {
			jProgressBar_Thread2 = new JProgressBar();
			jProgressBar_Thread2.setBounds(new Rectangle(31, 488, 141, 22));
			jProgressBar_Thread2.setStringPainted(true);
			jProgressBar_Thread2.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread2.setVisible(false);
		}
		return jProgressBar_Thread2;
	}

	/**
	 * This method initializes jProgressBar_Thread3
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread3() {
		if (jProgressBar_Thread3 == null) {
			jProgressBar_Thread3 = new JProgressBar();
			jProgressBar_Thread3.setBounds(new Rectangle(31, 526, 141, 22));
			jProgressBar_Thread3.setStringPainted(true);
			jProgressBar_Thread3.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread3.setVisible(false);
		}
		return jProgressBar_Thread3;
	}

	/*
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * FEN_Download thisClass = new FEN_Download();
	 * thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * thisClass.setVisible(true); ThreadVerifMaj verif = newThreadVerifMaj(
	 * "http://downloads.sourceforge.net/project/gestiondl/Setup/version.ini"
	 * ,EncoursListe,TotalListe,vitesseListe,FichierEnCoursListe);
	 * verif.start(); } }); }
	 */
	/**
	 * This is the default constructor
	 */
	public FEN_Download() {
		super();
		listModelEnAttente_Liste = new DefaultListModel();
		listModelFait_Liste = new DefaultListModel();
		listModelEnAttente_Liste_Htm = new DefaultListModel();
		listModelFait_Liste_Htm = new DefaultListModel();
		listModelEnAttente_Liste_PressePapier = new DefaultListModel();
		listModelFait_Liste_PressePapier = new DefaultListModel();
		listModelEnAttente_Liste_DragAndDrop = new DefaultListModel();
		listModelFait_Liste_DragAndDrop = new DefaultListModel();
		// RequestSupervisor.FixeProgressAndLabel(jProgressBar_Tread0,
		// jProgressBar_Thread1, jProgressBar_Thread2, jProgressBar_Thread2,
		// jLabel_Thread0, jLabel_Tread1, jLabel_Tread2, jLabel_Thread3);
		initialize();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// on indique au superviseur quelle sont les progressBar et les labels
		// RequestSupervisor.FixeProgressAndLabel(jProgressBar_Thread0,
		// jProgressBar_Thread1, jProgressBar_Thread2, jProgressBar_Thread3,
		// jLabel_Thread0, jLabel_Tread1, jLabel_Tread2, jLabel_Thread3);

		ThreadVerifMaj verif = new ThreadVerifMaj(
				"http://gestion-dl.googlecode.com/files/version.ini",
				Encours_DragAndDrop, Total_DragAndDrop, vitesse_DragAndDrop,
				FichierEnCours_DragAndDrop);
		verif.start();

		jTextField_ListeExclut.setText(Parameters.getInstance()
				.getListeExclusion());

	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setSize(946, 625);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("/network.png")));
		this.setContentPane(getJContentPane());
		this.setTitle("Gestion DL");
		this.setVisible(true);
		this.setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTabbedPane(), null);

		}
		return jContentPane;
	}

	/**
	 * This method initializes jProgressBar_Thread4
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread4() {
		if (jProgressBar_Thread4 == null) {
			jProgressBar_Thread4 = new JProgressBar();
			jProgressBar_Thread4.setBounds(new Rectangle(371, 424, 141, 22));
			jProgressBar_Thread4.setStringPainted(true);
			jProgressBar_Thread4.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread4.setVisible(false);
		}
		return jProgressBar_Thread4;
	}

	/**
	 * This method initializes jProgressBar_Thread5
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread5() {
		if (jProgressBar_Thread5 == null) {
			jProgressBar_Thread5 = new JProgressBar();
			jProgressBar_Thread5.setBounds(new Rectangle(371, 456, 141, 22));
			jProgressBar_Thread5.setStringPainted(true);
			jProgressBar_Thread5.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread5.setVisible(false);
		}
		return jProgressBar_Thread5;
	}

	/**
	 * This method initializes jProgressBar_Thread6
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread6() {
		if (jProgressBar_Thread6 == null) {
			jProgressBar_Thread6 = new JProgressBar();
			jProgressBar_Thread6.setBounds(new Rectangle(371, 488, 141, 22));
			jProgressBar_Thread6.setStringPainted(true);
			jProgressBar_Thread6.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread6.setVisible(false);
		}
		return jProgressBar_Thread6;
	}

	/**
	 * This method initializes jProgressBar_Thread7
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar_Thread7() {
		if (jProgressBar_Thread7 == null) {
			jProgressBar_Thread7 = new JProgressBar();
			jProgressBar_Thread7.setBounds(new Rectangle(371, 526, 141, 22));
			jProgressBar_Thread7.setStringPainted(true);
			jProgressBar_Thread7.setFont(new Font("Candara", Font.PLAIN, 12));
			jProgressBar_Thread7.setVisible(false);
		}
		return jProgressBar_Thread7;
	}

	/**
	 * This method initializes jSlider
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setBounds(new Rectangle(686, 474, 184, 56));
			jSlider.setMinimum(2);
			jSlider.setMajorTickSpacing(1);
			jSlider.setMinorTickSpacing(1);
			jSlider.setPaintLabels(true);
			jSlider.setPaintTicks(false);
			jSlider.setPaintTrack(true);
			jSlider.setExtent(0);
			jSlider.setOrientation(JSlider.HORIZONTAL);
			jSlider.setSnapToTicks(true);
			jSlider.setName("");
			jSlider.setFont(new Font("Candara", Font.PLAIN, 12));
			jSlider.setMaximum(8);
		}
		return jSlider;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(13, 15, 283, 34));
			jLabel1.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel1.setText(" liste des extensions exclues du multipart : ");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel1, null);
			jPanel.add(getJTextField_ListeExclut(), null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField_ListeExclut
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField_ListeExclut() {
		if (jTextField_ListeExclut == null) {
			jTextField_ListeExclut = new JTextField();
			jTextField_ListeExclut.setBounds(new Rectangle(315, 18, 500, 29));
			jTextField_ListeExclut.setFont(new Font("Candara", Font.PLAIN, 12));
		}
		return jTextField_ListeExclut;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(505, 62, 121, 40));
			jButton.setFont(new Font("Candara", Font.PLAIN, 12));
			jButton.setText(" Enregistrer ");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// System.out.println("actionPerformed()"); // TODO
					// Auto-generated Event stub actionPerformed()
					String liste = jTextField_ListeExclut.getText();
					Parameters.getInstance().setListeExclu(liste);
					Parameters.getInstance().store();
					JOptionPane.showMessageDialog(null, "Enregistrement Ok",
							"Ok", JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(622, 178, 239, 24));
			jButton1.setFont(new Font("Candara", Font.PLAIN, 12));
			jButton1.setText(" Effacer une ligne");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// System.out.println("actionPerformed()"); // TODO
					// Auto-generated Event stub actionPerformed()
					listModelEnAttente_Liste_DragAndDrop
							.removeElementAt(ListeAFaire_DragAndDrop
									.getSelectedIndex());

				}
			});
		}
		return jButton1;
	}

} // @jve:decl-index=0:visual-constraint="10,10"

