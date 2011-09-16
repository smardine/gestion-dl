package action;

import java.io.IOException;
import java.util.Properties;

public class Internet {

	/**
	 * Ouvre une url dans le navigateur par defaut
	 * @param AdresseSite -String
	 * @return vrai si Ok, faux sinon
	 */

	public static boolean OuvrePageInternet(String AdresseSite) {

		Properties sys = System.getProperties();
		String os = sys.getProperty("os.name");
		Runtime r = Runtime.getRuntime();
		try {
			if (os.endsWith("NT") || os.endsWith("2000") || os.endsWith("XP"))
				r.exec("cmd /c start " + AdresseSite);
			else
				r.exec("cmd /c start " + AdresseSite);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Ouvre une url dans internet explorer specifiquement
	 * @param Adresse -String
	 * @return vrai si Ok, faux sinon
	 */
	public static boolean OuvrePageInternetExplorer(String Adresse) {
		Properties sys = System.getProperties();
		String os = sys.getProperty("os.name");
		Runtime r = Runtime.getRuntime();
		try {
			if (os.endsWith("NT") || os.endsWith("2000") || os.endsWith("XP"))
				r.exec("cmd /c start iexplore " + Adresse);
			else
				r.exec("cmd /c start iexplore " + Adresse);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return true;

	}

}
