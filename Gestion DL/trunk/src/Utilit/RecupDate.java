package Utilit;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RecupDate {
	// * Choix de la langue francaise
	static Locale locale = Locale.getDefault();
	static Date actuelle = new Date();

	// * Definition du format utilise pour les dates
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DateFormat dateEtHeure = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	static DateFormat dateSeulement = new SimpleDateFormat("dd-MM-yyyy");
	static DateFormat heureSeulement = new SimpleDateFormat("HH:mm:ss");

	// * Donne la date au format "aaaa-mm-jj"

	/**
	 * Date systeme sous le format yyyy-MM-dd HH:mm:ss
	 * @return la date formatée -String
	 */
	public static String date() {
		String dat = dateFormat.format(actuelle);
		return dat;
	}

	public static String dateSeulement() {
		String dat = dateSeulement.format(actuelle);
		return dat;
	}

	/**
	 * Date systeme sous le format yyyy_MM_dd_HH_mm_ss
	 * @return la date formatée -String
	 */
	public static String dateEtHeure() {
		String dat = dateEtHeure.format(actuelle);
		return dat;
	}

}