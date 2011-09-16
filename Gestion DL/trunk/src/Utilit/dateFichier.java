package Utilit;

import java.io.File;

public class dateFichier {
	/**
	 * Recupere la date de derniere modification d'un fichier
	 * @param fileName -String le chemin du fichier
	 * @return la date de derniere modification d'un fichier -Long
	 */
	public static long getFileDateTime(String fileName) {

		if (fileName.charAt(0) == '/' || fileName.charAt(0) == '\\') {
			fileName = fileName.substring(1, fileName.length());
		}

		try {
			File file = new File(fileName);
			// File name must be a file or a directory.
			if (!file.isDirectory() && !file.isFile()) {
				return -1;
			}

			return file.lastModified();
			// return file.lastCreated();
		} catch (java.lang.Exception e) { // Trap all Exception based exceptions
											// and return -1.
			return -1;
		}
	}

}
