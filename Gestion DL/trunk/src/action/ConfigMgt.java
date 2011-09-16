package action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

public class ConfigMgt {

	// ****************************************
	// Membres priv�s de ConfigMgt.
	// ****************************************

	private String nomFichier; // Nom du fichier
	private String repertoireFichier; // R�pertoire du fichier
	private File fichier; // Le fichier
	private File fichierTmp; // Le fichier de travail
	private RandomAccessFile contenuFichier; // Son contenu
	private char commentaireChar; // Caract�re de commentaire
	@SuppressWarnings("unchecked")
	private Hashtable configuration; // Variables et leur valeur
	private BufferedWriter output; // Variable qui permet d'�crire dans un
									// fichier

	// ****************************************
	// Constructeurs de ConfigMgt.
	// ****************************************
	/**
	 * Construction d'un nouveau manager de configuration fichier. Si le fichier
	 * pass� en parametre est null, cela d�clanche une exception. On peut aussi
	 * parametrer la valeur du caract�re de commentaire dans le fichier.
	 * @param fic fichier contenant les informations de configurations.
	 * @param commentaire Caract�re permettant de commenter des lignes.
	 * @throws NullPointerException Si le fichier pass� en param�tre est null.
	 * @throws IOException Si le fichier n'en est pas un, ou s'il n'est lisible,
	 *             ou s'il n'est pas modifiable.
	 */
	public ConfigMgt(File fic, char commentaire) throws NullPointerException,
			IOException {
		// Initalisation du caract�re de commentaire
		commentaireChar = commentaire;

		// Test de la nullit� du fichier.
		if (fic != null) {
			// Le fichier n'est pas null.

			// Derni�re occurence du caract�re '/' ou '\' dans le chemin absolue
			// du fichier.
			int derIndexSepFichier = fic.getAbsolutePath().lastIndexOf(
					File.separatorChar) + 1;

			// Initialisation du nom et du r�pertoire du fichier.
			nomFichier = fic.getAbsolutePath().substring(derIndexSepFichier);
			repertoireFichier = fic.getAbsolutePath().substring(0,
					derIndexSepFichier);

			// On test la validit� du fichier
			if (isFichierOk()) {
				// Le fichier est OK, on charge les �l�ments de la config en
				// m�moire.
				if (!chargementConfig()) {
					throw new IOException(
							"Impossible de charger le fichier en m�moire !!!");
				}
			} else {
				// Le fichier ne r�pond aux crit�res.
				throw new IOException();
			}
		} else {
			// Le fichier est null, cela produit une exception.
			throw new NullPointerException("Le fichier est null !");
		}
	}

	/**
	 * Construction d'un nouveau manager de configuration fichier. Si l'une des
	 * deux chaines de caract�res pass�es en argument est null, cela provoque
	 * une exception.
	 * @param nomFic Chaine de caract�res contenant le nom du fichier.
	 * @param repFic Chaine de caract�res contenant le nom du r�pertoire o� se
	 *            trouve du fichier.
	 * @param commentaire Caract�re permettant de commenter des lignes.
	 * @throws NullPointerException Si le nom du fichier et/ou le nom du
	 *             repertoire pass�s en param�tre sont null.
	 * @throws IOException Si le fichier n'en est pas un, ou s'il n'est lisible,
	 *             ou s'il n'est pas modifiable.
	 */
	public ConfigMgt(String nomFic, String repFic, char commentaire)
			throws NullPointerException, IOException {
		// Initalisation du caract�re de commentaire
		commentaireChar = commentaire;

		// On teste la nullit� du nom du fichier et du r�pertoire.
		if (nomFic != null && repFic != null) {
			// Ni le fichier, ni le r�pertoire sont null

			// Initialisation du nom du fichier
			nomFichier = nomFic;

			if (nomFichier.trim().equals("")) {
				throw new IOException();
			}

			if ((nomFichier + repFic).trim().equals("")
					|| (nomFichier + repFic).trim().equals("\\")
					|| (nomFichier + repFic).trim().equals("/")) {
				throw new IOException();
			}

			// On s'assure que le dernier caract�re du chemin est bien le
			// caract�re de s�paration des r�pertoires.
			if (repFic.charAt(repFic.length() - 1) == File.separatorChar) {
				// Initialisation du nom du r�pertoire
				repertoireFichier = repFic;
			} else {
				// Initialisation du nom du r�pertoire auquel on ajoute
				// un caract�re de s�paration
				repertoireFichier = repFic + File.separatorChar;
			}

			// On test la validit� du fichier
			if (isFichierOk()) {
				// Le fichier est OK, on charge les �l�ments de la config en
				// m�moire.
				if (!chargementConfig()) {
					throw new IOException(
							"Impossible de charger le fichier en m�moire !!!");
				}
			} else {
				// Le fichier ne r�pond aux crit�res.
				throw new IOException();
			}
		} else {
			// Le nom fichier est null ou le nom de repertoire est null,
			// cela produit une exception.
			throw new NullPointerException(
					"Le nom du fichier ou le nom du repertoire est null !!!");
		}
	}

	// ****************************************
	// M�thodes priv�es de ConfigMgt.
	// ****************************************

	/**
	 * M�thode de v�rification du fichier de configuration. Celui-ci doit
	 * exist�er, �tre lisible et accessible en �criture. S'il ne rempli pas ces
	 * conditions, cela g�n�re une exception IOException.
	 * @return Vrai si le fichier est conforme aux attentes sinon Faux.
	 * @throws IOException Si le fichier n'en est pas un, ou s'il n'est lisible,
	 *             ou s'il n'est pas modifiable.
	 */
	public boolean isFichierOk() {
		boolean rc = false;

		try {
			// Cr�ation du fichier
			fichier = new File(repertoireFichier + nomFichier);

			// est-ce bien un fichier?
			if (!fichier.isFile()) {
				rc = false;
				// System.err.println("*** Ce n'est pas un fichier valide !!! ***");
			} else if (!fichier.canRead()) {
				rc = false;
				// System.err.println("*** Le Fichier n'est pas accessible en lecture !!! ***");
			} else if (!fichier.canWrite()) {
				rc = false;
				// System.err.println("*** Le Fichier n'est pas accessible en �criture !!! ***");
			} else {
				// cr�ation du fichier temporaire sur lequel on va travailler
				fichierTmp = File.createTempFile("$" + nomFichier + "$", "tmp",
						null);
				fichierTmp.deleteOnExit();
				copier(fichier, fichierTmp);
				rc = true;
			}
		} catch (IOException IOex) {
			rc = false;
		}

		return rc;
	}

	/**
	 * M�thode de chargement du fichier de config en m�moire.
	 * @return Vrai s'il a r�ussi � le charger, sinon Faux.
	 */
	@SuppressWarnings("unchecked")
	private boolean chargementConfig() {
		boolean rc = false;
		int posEgal = -1;
		configuration = new Hashtable();

		try {
			// Cr�ation de l'accesseur du fichier
			contenuFichier = new RandomAccessFile(fichierTmp, "rw");

			// On se replace en haut du fichier
			contenuFichier.seek(0);

			// On r�cup�re la premi�re ligne
			String LigneCourrante = contenuFichier.readLine();

			// on sort de la boucle si la ligne courante est null(fin du
			// fichier)
			while (LigneCourrante != null) {
				// On s'assure que la ligne n'est pas vide
				if (LigneCourrante.trim().length() != 0) {
					// On ne prend que les lignes non comment�es
					if (LigneCourrante.trim().charAt(0) != commentaireChar) {
						// on supprime les commentaires de fin de lignes
						int i = LigneCourrante.indexOf(commentaireChar);
						if (i > -1) {
							LigneCourrante = LigneCourrante.substring(0, i)
									.trim();
						}

						// on recherche le caract�re '='
						posEgal = LigneCourrante.indexOf('=');
						if (posEgal > -1) {
							// Il y a un caract�re '='
							String variable = new String(LigneCourrante
									.substring(0, posEgal).trim());

							if (!variable.equals("")) {
								String valeur = LigneCourrante.substring(
										posEgal + 1).trim();

								configuration.put(variable, valeur);
							} else {
								// System.err.println("*** Le fichier est mal formater !! ***");
								throw new IOException();
							}
						} else {
							// System.err.println("*** Le fichier est mal formater !! ***");
							throw new IOException();
						}
					}
				}
				// On passe � la ligne suivante
				LigneCourrante = contenuFichier.readLine();
			}

			contenuFichier.close();

			rc = true;
		} catch (IOException IOe) {
			rc = false;
		} finally {
			try {
				contenuFichier.close();
			} catch (IOException e) {
			}
		}

		return rc;
	}

	/**
	 * M�thode de copie d'un fichier dans un autre.
	 * @param source Fichier que l'on veut copier.
	 * @param destination Fichier dans lequel on copie.
	 * @return Vrai Si la copie s'est bien pass� sinon faux.
	 */
	private boolean copier(File source, File destination) {
		boolean rc = false;

		// Declaration des flux
		FileInputStream sourceFile = null;
		FileOutputStream destinationFile = null;

		try {
			// Cr�ation du fichier :
			destination.createNewFile();
			// Ouverture des flux
			sourceFile = new java.io.FileInputStream(source);
			destinationFile = new java.io.FileOutputStream(destination);
			// Lecture par segment de 1ko
			byte buffer[] = new byte[1024];
			int nbLecture;

			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
			}
			// Copie r�ussie
			rc = true;
		} catch (FileNotFoundException f) {
		} catch (IOException e) {
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

		return rc;
	}

	// ****************************************
	// M�thodes publiques de ConfigMgt.
	// ****************************************
	/**
	 * Fonction retournant la valeur d'un param�tre de la configuration. <br>
	 * e.g. si dans un fichier on a une ligne du type age=34 la fonction
	 * GetValueOf("age") retournera "34".
	 * @param VarName nom de la variable dans le fichier.
	 * @return valeur de la variable rentrer en param�tre ou une chaine vide si
	 *         la variable n'existe pas.
	 */
	public String getValeurDe(String varName) {
		// Initialisation des variables
		String rc = "";
		Object tmp;

		// V�rification du param�tre VarName
		if (!varName.trim().equals("")) {
			// Le parametre n'est pas un chaine vide
			// Lecture de la Hashtable
			tmp = configuration.get(varName);
			// Si la valeur est non null
			if (tmp != null) {
				rc = (String) tmp.toString();
			}
		}

		// On retourne le r�sultat
		return rc;
	}

	/**
	 * M�thode de mise � jour d'une variable dans la configuration. Si la valeur
	 * pass�e en argument est null alors on sauve "" et non null (On travaille
	 * sur un fichier txt). Si varName n'existe pas alors rien n'est fait de
	 * m�me si varName est null.
	 * @param varName Chaine de caract�res contenant le nom de la variable.
	 * @param valeur Chaine de caract�res contenant la valeur de la variable.
	 * @return Vrai si la configuration a �t� modifi�e sinon Faux.
	 */
	@SuppressWarnings("unchecked")
	public boolean setValeurDe(String varName, String valeur) {
		boolean rc = false;

		if (varName != null && valeur != null) {
			// Les deux param�tres sont non null
			if (configuration.containsKey(varName)) {
				// La variable existe et on la met � jour
				configuration.put(varName, valeur);
				rc = true;
			}
		} else {
			// Au moins l'un des deux est null
			if (varName != null) {
				// La variable est non null
				if (configuration.containsKey(varName)) {
					// La variable existe et on la met � jour
					configuration.put(varName, "");
					rc = true;
				}
			}
		}

		return rc;
	}

	/**
	 * On cr�e un variable et son valeur . On l'�crit � la fin du fichier
	 * @param varName Chaine de caract�res contenant le nom de la variable.
	 * @param valeur Chaine de caract�res contenant la valeur de la variable.
	 * @return Vrai si la configuration a �t� modifi�e sinon Faux.
	 */
	public boolean addVariable(String varName, String valeur) {
		boolean rc = false;

		if (varName != null && valeur != null) {
			// Les deux param�tres sont non null
			try {
				output = new BufferedWriter(new FileWriter(fichierTmp, true)); // flux
																				// d'�criture
				output.write(varName + " = " + valeur); // on �crit � la fin du
														// fichier
				output.newLine(); // on ajoute une nouvelle ligne
				output.close();
				this.chargementConfig();
			} catch (IOException e) {

				e.printStackTrace();
			}
			rc = true;
		}

		return rc;
	}

	/**
	 * Permet de retourner la liste des fichiers qui ont le m�me extension il
	 * faut definir l'emplacement et l'extension du fichier
	 */
	public File[] listeFichier(String emplacement, final String extension) {
		File dir = new File(emplacement); // on cr�e une instance du r�pertoire

		FilenameFilter filter = new FilenameFilter() { // on cr�e un filtre
			public boolean accept(File dir, String name)
			// retourne tous les fichier qui se termine par l'extension souhait�
			// .
			{
				return name.endsWith(extension);
			}
		};
		File[] files = dir.listFiles(filter); // on met le r�sultat dans un
												// tableau du fichier
		return files;

	}

	/**
	 * M�thode de sauvegarde de la configuration dans le fichier temporaire.
	 * Cette m�thode permet de sauvegarder un �tat interm�diaire de la
	 * configuration sans �craser le fichier original.
	 * @return Vrai si la sauvegarde a r�ussi sinon Faux.
	 */
	public boolean sauveConfigDansFichierTmp() {
		boolean rc = false;
		int posEgal = -1;

		String retourLigne = System.getProperty("line.separator");
		StringBuffer chaineTampon = new StringBuffer();

		try {
			// Cr�ation de l'accesseur du fichier
			contenuFichier = new RandomAccessFile(fichierTmp, "rw");

			// On se place au d�but du fichier
			contenuFichier.seek(0);

			String ligneCourante = contenuFichier.readLine();

			// On parcourt toutes les lignes du fichiers
			while (ligneCourante != null) {
				// On regarde si la ligne est non vide
				if (ligneCourante.length() > 0) {
					// On regarde le premier caract�re de la ligne
					if (ligneCourante.charAt(0) == commentaireChar) {
						// C'est une ligne de commentaire on la met dans le
						// tampon.
						chaineTampon.append(ligneCourante + retourLigne);
					} else {
						// On sauvegarde les commentaires situ�s sur cette ligne
						String comment = "";
						int posCom = ligneCourante.indexOf(commentaireChar);
						if (posCom > -1) {
							comment = ligneCourante.substring(posCom);
							ligneCourante = ligneCourante.substring(0, posCom);
						}
						// Ce n'est pas une ligne de commentaire
						if (ligneCourante.trim().length() > 0) {
							// Ce n'est pas une ligne avec que des espaces

							// On teste la pr�sence et la postion du caract�re
							// '='
							posEgal = ligneCourante.indexOf('=');

							if (posEgal > -1) {
								// il y a un caract�re '=' dans la ligne
								String variable = new String(ligneCourante
										.substring(0, posEgal).trim());

								if (!variable.equals("")) {
									// Le nom de la variable est non nul
									String valeur = (String) configuration
											.get(variable);

									chaineTampon.append(variable + " = "
											+ valeur + " " + comment
											+ retourLigne);
								} else {
									// System.err.println("*** Le fichier est mal formater !! ***");
									throw new IOException();
								}
							} else {
								// la ligne est non vide et ne poss�de aucun
								// caract�re '='
								// System.err.println("*** Le fichier est mal format�. ***");
								throw new IOException();
							}
						} else {
							// La ligne ne poss�de que des espaces, on met un
							// retour
							// � la ligne dans le tampon
							chaineTampon.append(comment + retourLigne);
						}
					}
				} else {
					// La ligne est vide on met un retour � la ligne dans le
					// tampon
					chaineTampon.append(retourLigne);
				}

				ligneCourante = contenuFichier.readLine();
			}

			// Si on arrive ici c'est l'on a r�ussi � cr�er une nouvelle version
			// du fichier dans le tampon.
			contenuFichier.seek(0);

			contenuFichier.setLength(0);

			contenuFichier.writeBytes(chaineTampon.toString());

			contenuFichier.close();

			rc = true;
		} catch (IOException e) {
			rc = false;
		} finally {
			try {
				contenuFichier.close();
			} catch (IOException e1) {
			}
		}

		return rc;
	}

	/**
	 * Cette m�thode permet de valider les modifications apport�es au fichier.
	 * Celle-ci seront disponibles � la prochaine lecture du fichier de
	 * configuration. Dans le cas o� l'enregistrement des donn�es seraient
	 * impossible, la fonction retourne faux.
	 * @return Vrai si la sauvegarde est effective sinon Faux.
	 */
	public boolean valideSauveDansFichier() {
		boolean rc = false;

		rc = copier(fichierTmp, fichier);

		return rc;
	}
}
