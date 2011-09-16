package Utilit;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class GestionRegistre {

	/**
	 * Recupere des infos dans la base de registre
	 * @return l'info demandée
	 */
	private static final String REGQUERY_UTIL = "reg query ";

	private static final String REGSTR_TOKEN = "REG_SZ";

	// quand on connait la valeur de la clé
	// private static final String ADOBE_NAME_CMD = REGQUERY_UTIL +
	// "\"HKLM\\Software\\Adobe\\Acrobat Reader\\7.0\\Installer\" /v Path";

	// dans le cas contraire
	private static final String ADOBE_NAME_CMD = REGQUERY_UTIL
			+ "\"HKLM\\Software\\Adobe\\Acrobat Reader\\8.0\\InstallPath\"";

	public static String getAdobePath() {
		try {
			Process process = Runtime.getRuntime().exec(ADOBE_NAME_CMD);
			StreamReader reader = new StreamReader(process.getInputStream());

			reader.start();
			process.waitFor();
			reader.join();

			String result = reader.getResult();

			int p = result.indexOf(REGSTR_TOKEN);

			if (p == -1) {
				return null;
			}

			return result.substring(p + REGSTR_TOKEN.length()).trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Verifie la presence de la definition du lien odbc dans la base de
	 * registre
	 * @return vrai si on le trouve, faux si on ne l'a pas
	 */
	public static Boolean verifPresenceLienODBC() {
		try {

			String commande = REGQUERY_UTIL
					+ "\"HKLM\\Software\\ODBC\\ODBC.INI\\outilhotline\"";
			Process process = Runtime.getRuntime().exec(commande);
			StreamReader reader = new StreamReader(process.getInputStream());

			reader.start();
			process.waitFor();
			reader.join();

			String result = reader.getResult();

			int p = result.indexOf(REGSTR_TOKEN);

			if (p == -1) {
				return false;
			}

			Historique.ecrire("Configuration ODBC :"
					+ result.substring(p + REGSTR_TOKEN.length()).trim());

			System.out.println(result.substring(p + REGSTR_TOKEN.length())
					.trim());
			return true;
		} catch (Exception e) {
			return null;
		}
	}

	// parser du résultat
	static class StreamReader extends Thread {
		private final InputStream is;

		private final StringWriter sw;

		StreamReader(InputStream is) {
			this.is = is;
			sw = new StringWriter();
		}

		@Override
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
				;
			}
		}

		String getResult() {
			return sw.toString();
		}
	}

	/*
	 * public static void main(String s[]) { System.out.println(getAdobePath());
	 * }
	 */

}
