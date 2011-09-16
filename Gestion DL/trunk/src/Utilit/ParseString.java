package Utilit;

import java.io.IOException;
import java.text.Normalizer;

import action.ConfigMgt;

public class ParseString {
	/**
	 * Parser un fichier csv de type C-xxx.csv
	 * @param stringToParse -String la chaine de caractere a verifier.
	 * @param Parser -String le delimiteur
	 * @param Fichier -String le fichier a parser
	 * @param structCD -int � 1 si on travaille sur la structure cd, � 0 sinon.
	 * @param site_maj -int � 1 si on travaille sur le site de maj, � 0 sinon.
	 */

	public static String parseConvention(String stringToParse, String Parser,
			String Fichier, int structCD, int site_maj) throws IOException {

		int icodeActionConvention = 1, iListeCodeCategorie = 2, iListeSpecialite = 3, iMessageAuPS = 5, iIdentifiantOrgSign = 6, iLibelleOrgSign = 7, iTypeConv = 9, iCritereSecondaireConvention = 10, iNumOrgComplementaire = 12, iLibelleOrgComplementaire = 13, iNomDomaine = 22;

		int indexOfComplementaire = Fichier.indexOf("Compl�mentaires");
		String repertoire = Fichier.substring(indexOfComplementaire)
				.toUpperCase();
		repertoire = addAntiSlach(repertoire);

		stringToParse = removeApostrophe(stringToParse);
		repertoire = removeApostrophe(repertoire);

		String[] tabChaine = stringToParse.split(Parser);
		String ETAT_MUTUELLE = tabChaine[icodeActionConvention - 1];

		String LISTE_CODE_CATEGORIE = tabChaine[iListeCodeCategorie - 1];
		String LISTE_SPECIALITE = tabChaine[iListeSpecialite - 1];
		String MESSAGE_AU_PS = tabChaine[iMessageAuPS - 1];
		String IDENTIFIANT_SIGNATAIRE = tabChaine[iIdentifiantOrgSign - 1];
		String LIBELLE_SIGNATAIRE = tabChaine[iLibelleOrgSign - 1];
		String TYPE_CONVENTION = tabChaine[iTypeConv - 1];
		String CRITERE_SECONDAIRE = tabChaine[iCritereSecondaireConvention - 1];
		String IDENTIFIANT_COMPLEMENTAIRE = tabChaine[iNumOrgComplementaire - 1];
		String LIBELLE_COMPLEMENTAIRE = tabChaine[iLibelleOrgComplementaire - 1];
		String DOMAINE = tabChaine[iNomDomaine - 1];
		if ((Fichier.contains("SP-Sant�") == true)
				|| (Fichier.contains("Eovi Creusoise") == true)
				|| (Fichier.contains("Eovi Limousin") == true)
				|| (Fichier.contains("SanteVie") == true)) {// on est dans le
															// repertoire de SP
															// Sant�, il faut
															// utiliser le
															// fichier
															// FORCAGE.INI pour
															// recuperer les
															// categories et
															// specialit�
			try {
				int IdxRepEnCours = Fichier.lastIndexOf("\\");
				String RepEnCours = Fichier.substring(0, IdxRepEnCours);

				ConfigMgt forcage = new ConfigMgt("FORCAGE.ini", RepEnCours
						+ "\\", '[');
				LISTE_CODE_CATEGORIE = forcage.getValeurDe("CATEGORIE");
				LISTE_SPECIALITE = forcage.getValeurDe("SPECIALITE");
			} catch (NullPointerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// on a fini de traiter la ligne, on insere dans la base de donn�es
		String RequeteAjoutMutuelle = String
				.format(
						"INSERT INTO T_CONVENTIONS (CODE_ACTION, CODE_CATEGORIE, LISTE_SPECIALITE,MESSAGE_AU_PS,IDENT_SIGNATAIRE,LIBELLE_SIGNATAIRE,TYPE_CONVENTION,CRIT_SECONDAIRE,IDENT_COMPLEMENTAIRE,LIBELLE_COMPLEMENTAIRE,NOM_FICHIER,DOMAINE,STRUCT_CD,SITE_MAJ) "
								+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%d')",
						ETAT_MUTUELLE, LISTE_CODE_CATEGORIE, LISTE_SPECIALITE,
						MESSAGE_AU_PS, IDENTIFIANT_SIGNATAIRE,
						LIBELLE_SIGNATAIRE, TYPE_CONVENTION,
						CRITERE_SECONDAIRE, IDENTIFIANT_COMPLEMENTAIRE,
						LIBELLE_COMPLEMENTAIRE, repertoire, DOMAINE, structCD,
						site_maj);

		String RequeteAjoutMutuelleSansAccent = removeAccent(RequeteAjoutMutuelle);
		return RequeteAjoutMutuelleSansAccent.toUpperCase();
	}

	/**
	 * Parser un fichier csv de type R-xxx.csv
	 * @param stringToParse -String la chaine de caractere a verifier.
	 * @param Parser -String le delimiteur
	 * @param Fichier -String le fichier a parser
	 * @param structCD -int � 1 si on travaille sur la structure cd, � 0 sinon.
	 * @param siteMaj -int � 1 si on travaille sur le site de maj, � 0 sinon.
	 */
	public static String parseRegroupement(String stringToParse, String Parser,
			String Fichier, int structCD, int siteMaj) {
		int icodeAction = 1, iListeCodeCategorieRegroupement = 2, iListeSpecialiteRegroupement = 3, iNumerocomplementaire = 4, ilibellecomplementaire = 5, iTypeConvention = 6, iCritereSecondaire = 8, iIdentifiantOrgSignataireRegroupement = 9, IlibelleOrgSignataireRegroupement = 10;
		int indexOfComplementaire = Fichier.indexOf("Compl�mentaires");
		String repertoire = Fichier.substring(indexOfComplementaire)
				.toUpperCase();

		repertoire = addAntiSlach(repertoire);
		stringToParse = removeApostrophe(stringToParse);
		repertoire = removeApostrophe(repertoire);

		String[] tabChaine = stringToParse.split(Parser);

		String ETAT_MUTUELLE = tabChaine[icodeAction - 1];
		String LISTE_CODE_CATEGORIE = tabChaine[iListeCodeCategorieRegroupement - 1];
		String LISTE_SPECIALITE = tabChaine[iListeSpecialiteRegroupement - 1];
		String IDENTIFIANT_COMPLEMENTAIRE = tabChaine[iNumerocomplementaire - 1];
		String LIBELLE_COMPLEMENTAIRE = tabChaine[ilibellecomplementaire - 1];

		String TYPE_CONVENTION = tabChaine[iTypeConvention - 1];
		String CRITERE_SECONDAIRE = tabChaine[iCritereSecondaire - 1];
		String IDENTIFIANT_SIGNATAIRE = tabChaine[iIdentifiantOrgSignataireRegroupement - 1];
		String LIBELLE_SIGNATAIRE = tabChaine[IlibelleOrgSignataireRegroupement - 1];

		if ((Fichier.contains("SP-Sant�") == true)
				|| (Fichier.contains("Eovi Creusoise") == true)
				|| (Fichier.contains("Eovi Limousin") == true)
				|| (Fichier.contains("SanteVie") == true)) {// on est dans le
															// repertoire de SP
															// Sant�, il faut
															// utiliser le
															// fichier
															// FORCAGE.INI pour
															// recuperer les
															// categories et
															// specialit�
			try {
				int IdxRepEnCours = Fichier.lastIndexOf("\\");
				String RepEnCours = Fichier.substring(0, IdxRepEnCours);

				ConfigMgt forcage = new ConfigMgt("FORCAGE.ini", RepEnCours
						+ "\\", '[');
				LISTE_CODE_CATEGORIE = forcage.getValeurDe("CATEGORIE");
				LISTE_SPECIALITE = forcage.getValeurDe("SPECIALITE");
			} catch (NullPointerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String RequeteAjoutMutuelle = String
				.format(
						"INSERT INTO T_REGROUPEMENT (CODE_ACTION, CODE_CATEGORIE, LISTE_SPECIALITE,IDENT_COMPLEMENTAIRE,LIBELLE_COMPLEMENTAIRE,TYPE_CONVENTION,CRIT_SECONDAIRE,IDENT_SIGNATAIRE,LIBELLE_SIGNATAIRE,NOM_FICHIER,STRUCT_CD,SITE_MAJ)"
								+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%d')",
						ETAT_MUTUELLE, LISTE_CODE_CATEGORIE, LISTE_SPECIALITE,
						IDENTIFIANT_COMPLEMENTAIRE, LIBELLE_COMPLEMENTAIRE,
						TYPE_CONVENTION, CRITERE_SECONDAIRE,
						IDENTIFIANT_SIGNATAIRE, LIBELLE_SIGNATAIRE, repertoire,
						structCD, siteMaj);

		String RequeteAjoutMutuelleSansAccent = removeAccent(RequeteAjoutMutuelle);
		return RequeteAjoutMutuelleSansAccent.toUpperCase();
	}

	/**
	 * Parser un fichier csv de type C-xxx.csv dans import CSV
	 * @param stringToParse -String la chaine de caractere a verifier.
	 * @param Parser -String le delimiteur
	 * @param fichier -String le fichier a parser
	 * @param structCD -int � 1 si on travaille sur la structure cd, � 0 sinon.
	 * @param siteMaj -int � 1 si on travaille sur le site de maj, � 0 sinon.
	 */
	public static String parseConventionImportCSV(String stringToParse,
			String Parser, String fichier, int structCD, int siteMaj) {
		// TODO Auto-generated method stub
		int icodeActionConvention = 1, iListeCodeCategorie = 2, iListeSpecialite = 3, iMessageAuPS = 5, iIdentifiantOrgSign = 6, iLibelleOrgSign = 7, iTypeConv = 9, iCritereSecondaireConvention = 10, iNumOrgComplementaire = 12, iLibelleOrgComplementaire = 13, iNomDomaine = 22;
		String ETAT_MUTUELLE = null, LISTE_CODE_CATEGORIE = null, LISTE_SPECIALITE = null, MESSAGE_AU_PS = null, IDENTIFIANT_SIGNATAIRE = null, LIBELLE_SIGNATAIRE = null, TYPE_CONVENTION = null, CRITERE_SECONDAIRE = null, IDENTIFIANT_COMPLEMENTAIRE = null, LIBELLE_COMPLEMENTAIRE = null, DOMAINE = null;

		fichier = addAntiSlach(fichier);
		stringToParse = removeApostrophe(stringToParse);
		fichier = removeApostrophe(fichier);

		String[] tabChaine = stringToParse.split(Parser);
		int nbchamps = tabChaine.length;
		if (nbchamps >= (icodeActionConvention - 1)) {
			ETAT_MUTUELLE = tabChaine[icodeActionConvention - 1];
		}
		if (nbchamps >= (iListeCodeCategorie - 1)) {
			LISTE_CODE_CATEGORIE = tabChaine[icodeActionConvention - 1];
		}
		if (nbchamps >= (iListeSpecialite - 1)) {
			LISTE_SPECIALITE = tabChaine[iListeSpecialite - 1];
		}
		if (nbchamps >= (iMessageAuPS - 1)) {
			MESSAGE_AU_PS = tabChaine[iMessageAuPS - 1];
		}
		if (nbchamps >= (iIdentifiantOrgSign - 1)) {
			IDENTIFIANT_SIGNATAIRE = tabChaine[iIdentifiantOrgSign - 1];
		}
		if (nbchamps >= (iLibelleOrgSign - 1)) {
			LIBELLE_SIGNATAIRE = tabChaine[iLibelleOrgSign - 1];
		}
		if (nbchamps >= (iTypeConv - 1)) {
			TYPE_CONVENTION = tabChaine[iTypeConv - 1];
		}
		if (nbchamps >= (iCritereSecondaireConvention - 1)) {
			CRITERE_SECONDAIRE = tabChaine[iCritereSecondaireConvention - 1];
		}
		if (nbchamps >= (iNumOrgComplementaire - 1)) {
			IDENTIFIANT_COMPLEMENTAIRE = tabChaine[iNumOrgComplementaire - 1];
		}
		if (nbchamps >= (iLibelleOrgComplementaire - 1)) {
			LIBELLE_COMPLEMENTAIRE = tabChaine[iLibelleOrgComplementaire - 1];
		}
		if (nbchamps >= (iNomDomaine - 1)) {
			DOMAINE = tabChaine[iNomDomaine - 1];
		}
		if ((fichier.contains("SP-Sant�") == true)
				|| (fichier.contains("Eovi Creusoise") == true)
				|| (fichier.contains("Eovi Limousin") == true)
				|| (fichier.contains("SanteVie") == true)) {// on est dans le
															// repertoire de SP
															// Sant�, il faut
															// utiliser le
															// fichier
															// FORCAGE.INI pour
															// recuperer les
															// categories et
															// specialit�
			try {
				int IdxRepEnCours = fichier.lastIndexOf("\\");
				String RepEnCours = fichier.substring(0, IdxRepEnCours);

				ConfigMgt forcage = new ConfigMgt("FORCAGE.ini", RepEnCours
						+ "\\", '[');
				LISTE_CODE_CATEGORIE = forcage.getValeurDe("CATEGORIE");
				LISTE_SPECIALITE = forcage.getValeurDe("SPECIALITE");
			} catch (NullPointerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		String RequeteAjoutMutuelle = String
				.format(
						"INSERT INTO T_CONVENTIONS_IMPORT_CSV (CODE_ACTION, CODE_CATEGORIE, LISTE_SPECIALITE,MESSAGE_AU_PS,IDENT_SIGNATAIRE,LIBELLE_SIGNATAIRE,TYPE_CONVENTION,CRIT_SECONDAIRE,IDENT_COMPLEMENTAIRE,LIBELLE_COMPLEMENTAIRE,NOM_FICHIER,DOMAINE,STRUCT_CD,SITE_MAJ)"
								+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%d')",
						ETAT_MUTUELLE, LISTE_CODE_CATEGORIE, LISTE_SPECIALITE,
						MESSAGE_AU_PS, IDENTIFIANT_SIGNATAIRE,
						LIBELLE_SIGNATAIRE, TYPE_CONVENTION,
						CRITERE_SECONDAIRE, IDENTIFIANT_COMPLEMENTAIRE,
						LIBELLE_COMPLEMENTAIRE, fichier, DOMAINE, structCD,
						siteMaj);

		String RequeteAjoutMutuelleSansAccent = removeAccent(RequeteAjoutMutuelle);
		return RequeteAjoutMutuelleSansAccent.toUpperCase();
	}

	/**
	 * Parser un fichier csv de type R-xxx.csv dans import csv.
	 * @param stringToParse -String la chaine de caractere a verifier.
	 * @param Parser -String le delimiteur
	 * @param fichier -String le fichier a parser
	 * @param structCD -int � 1 si on travaille sur la structure cd, � 0 sinon.
	 * @param siteMaj -int � 1 si on travaille sur le site de maj, � 0 sinon.
	 */
	public static String parseRegroupementImportCSV(String stringToParse,
			String Parser, String fichier, int structCD, int siteMaj) {
		// TODO Auto-generated method stub
		int icodeAction = 1, iListeCodeCategorieRegroupement = 2, iListeSpecialiteRegroupement = 3, iNumerocomplementaire = 4, ilibellecomplementaire = 5, iTypeConvention = 6, iCritereSecondaire = 8, iIdentifiantOrgSignataireRegroupement = 9, IlibelleOrgSignataireRegroupement = 10;
		fichier = addAntiSlach(fichier);
		stringToParse = removeApostrophe(stringToParse);
		fichier = removeApostrophe(fichier);

		String[] tabChaine = stringToParse.split(Parser);
		String ETAT_MUTUELLE = tabChaine[icodeAction - 1];
		String LISTE_CODE_CATEGORIE = tabChaine[iListeCodeCategorieRegroupement - 1];
		String LISTE_SPECIALITE = tabChaine[iListeSpecialiteRegroupement - 1];
		String IDENTIFIANT_COMPLEMENTAIRE = tabChaine[iNumerocomplementaire - 1];
		String LIBELLE_COMPLEMENTAIRE = tabChaine[ilibellecomplementaire - 1];
		String TYPE_CONVENTION = tabChaine[iTypeConvention - 1];
		String CRITERE_SECONDAIRE = tabChaine[iCritereSecondaire - 1];
		String IDENTIFIANT_SIGNATAIRE = tabChaine[iIdentifiantOrgSignataireRegroupement - 1];
		String LIBELLE_SIGNATAIRE = tabChaine[IlibelleOrgSignataireRegroupement - 1];

		if ((fichier.contains("SP-Sant�") == true)
				|| (fichier.contains("Eovi Creusoise") == true)
				|| (fichier.contains("Eovi Limousin") == true)
				|| (fichier.contains("SanteVie") == true)) {// on est dans le
															// repertoire de SP
															// Sant�, il faut
															// utiliser le
															// fichier
															// FORCAGE.INI pour
															// recuperer les
															// categories et
															// specialit�
			try {
				int IdxRepEnCours = fichier.lastIndexOf("\\");
				String RepEnCours = fichier.substring(0, IdxRepEnCours);

				ConfigMgt forcage = new ConfigMgt("FORCAGE.ini", RepEnCours
						+ "\\", '[');
				LISTE_CODE_CATEGORIE = forcage.getValeurDe("CATEGORIE");
				LISTE_SPECIALITE = forcage.getValeurDe("SPECIALITE");
			} catch (NullPointerException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		String RequeteAjoutMutuelle = String
				.format(
						"INSERT INTO T_REGROUPEMENT_IMPORT_CSV (CODE_ACTION, CODE_CATEGORIE, LISTE_SPECIALITE,IDENT_COMPLEMENTAIRE,LIBELLE_COMPLEMENTAIRE,TYPE_CONVENTION,CRIT_SECONDAIRE,IDENT_SIGNATAIRE,LIBELLE_SIGNATAIRE,NOM_FICHIER,STRUCT_CD,SITE_MAJ)"
								+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%d')",
						ETAT_MUTUELLE, LISTE_CODE_CATEGORIE, LISTE_SPECIALITE,
						IDENTIFIANT_COMPLEMENTAIRE, LIBELLE_COMPLEMENTAIRE,
						TYPE_CONVENTION, CRITERE_SECONDAIRE,
						IDENTIFIANT_SIGNATAIRE, LIBELLE_SIGNATAIRE, fichier,
						structCD, siteMaj);

		String RequeteAjoutMutuelleSansAccent = removeAccent(RequeteAjoutMutuelle);
		return RequeteAjoutMutuelleSansAccent.toUpperCase();
	}

	/**
	 * Parser un fichier csv de type correspondance.csv dans import CSV
	 * @param stringToParse -String la chaine de caractere a verifier.
	 * @param Parser -String le delimiteur
	 * @param fichier -String le fichier a parser
	 * @param structCD -int � 1 si on travaille sur la structure cd, � 0 sinon.
	 * @param siteMaj -int � 1 si on travaille sur le site de maj, � 0 sinon.
	 */
	public static String parseCorrespondanceImportCSV(String stringToParse,
			String Parser, String fichier, int structCD, int siteMaj) {
		// TODO Auto-generated method stub
		int iMutNum = 1, icodeorgassocie = 2, iident_org_compl = 3, ilibelle_org_compl = 4;
		fichier = addAntiSlach(fichier);
		stringToParse = removeApostrophe(stringToParse);
		fichier = removeApostrophe(fichier);

		String[] tabChaine = stringToParse.split(Parser);
		String MUT_NUM = tabChaine[iMutNum - 1];
		String CODE_ORG_ASSOCIE = tabChaine[icodeorgassocie - 1];
		String IDENT_ORG_COMPL = tabChaine[iident_org_compl - 1];
		String LIB_ORG_COMPL = tabChaine[ilibelle_org_compl - 1];

		String RequeteAjoutMutuelle = String
				.format(
						"INSERT INTO T_CORRESPONDANCE (MUTNUM, ORG_ASSOCIE, IDENT_ORG_COMPL,LIBELLE_ORG_COMPL,NOM_FICHIER,struct_cd,site_maj)"
								+ "VALUES ('%s','%s','%s','%s','%s','%d','%d')",
						MUT_NUM, CODE_ORG_ASSOCIE, IDENT_ORG_COMPL,
						LIB_ORG_COMPL, fichier, structCD, siteMaj);

		String RequeteAjoutMutuelleSansAccent = removeAccent(RequeteAjoutMutuelle);
		return RequeteAjoutMutuelleSansAccent.toUpperCase();
	}

	/**
	 * Retirer les accent d'une chaine de caractere
	 * @param source -String la chaine de caractere a traiter.
	 * @return la chaine de caractere corrig�e. -String
	 */
	public static String removeAccent(String source) {
		return Normalizer.normalize(source, Normalizer.Form.NFD).replaceAll(
				"[^\\p{ASCII}]", "");
	}

	/**
	 * Retirer les apostrophe d'une chaine de caractere
	 * @param source -String la chaine de caractere a traiter.
	 * @return la chaine de caractere corrig�e. -String
	 */
	public static String removeApostrophe(String source) {
		return source.replaceAll("'", " ");
	}

	/**
	 * remplacer les antislash par des slash
	 * @param source -String la chaine de caractere a traiter.
	 * @return la chaine de caractere corrig�e. -String
	 */
	private static String addAntiSlach(String source) {
		// TODO Auto-generated method stub
		return source.replace('\\', '/');
	}

}
