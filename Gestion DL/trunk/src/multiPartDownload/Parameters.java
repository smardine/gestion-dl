package multiPartDownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lio
 */
public class Parameters {
	// /////////////////////////// Constants ///////////////////////////////////
	public static final File file = new File(System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "prop.prop");

	private static final String INSPECT_NUMBER_OF_THREADS = "inspect.NOT";
	private static final String DOWNLOAD_NUMBER_OF_THREADS = "download.NOT";
	private static final String DOWNLOAD_DIR = "download.dir";
	private static final String DOWNLOAD_FILES = "settings.files";
	private static final String LISTE_EXCLUSION = "liste.exclusion";
	// //////////////////////////// WHOLE APPLICATION CONSTANT /////////////

	/** HTTP BUFFER SIZE */
	public static final int BUFFER_SIZE = 4096;

	// /////////////////////////// cache ///////////////////////////////////
	// we use only one fos;
	private FileOutputStream fos;

	// /////////////////////////// Data ///////////////////////////////////

	private int InspectNumberOfThreads;
	private int downloadNumberOfThreads;
	private String downloadDir;
	private String downloadFiles;
	private String ListeExclusion;

	private Properties prop = new Properties();

	public static final Parameters para = new Parameters();

	// /////////////////////////// Methods ///////////////////////////////////

	/** Creates a new instance of PreferenceValue */
	private Parameters() {
		FileInputStream fis = null;
		if (file.exists()) {
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException fnfe) {
			}
		} else {
			initDefault();
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// this should not be the case
			}
		}

		try {
			prop.load(fis);
			fis.close();
			InspectNumberOfThreads = Integer.parseInt(prop
					.getProperty(INSPECT_NUMBER_OF_THREADS));
			downloadNumberOfThreads = Integer.parseInt(prop
					.getProperty(DOWNLOAD_NUMBER_OF_THREADS));
			downloadDir = prop.getProperty(DOWNLOAD_DIR);
			downloadFiles = prop.getProperty(DOWNLOAD_FILES);
			ListeExclusion = prop.getProperty(LISTE_EXCLUSION);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static final Parameters getInstance() {
		return para;
	}

	public final int getNumberOfInspectionTrheads() {
		return InspectNumberOfThreads;
	}

	public final String getListeExclusion() {
		return ListeExclusion;
	}

	public void setNumberOfInspectionTrheads(int i) {
		InspectNumberOfThreads = i;
	}

	public void initDefault() {
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			Properties prop = new Properties();
			prop.setProperty(INSPECT_NUMBER_OF_THREADS, "3");
			prop.setProperty(DOWNLOAD_NUMBER_OF_THREADS, "5");
			prop.setProperty(DOWNLOAD_DIR, "C:\\Mes Documents");
			prop.setProperty(DOWNLOAD_FILES, "*");
			prop.setProperty(LISTE_EXCLUSION, "exe;rar;zip;gzip;7zip;7z");
			prop.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void store() {
		try {
			fos = new FileOutputStream(file);
			prop.setProperty(INSPECT_NUMBER_OF_THREADS, Integer
					.toString(InspectNumberOfThreads));
			prop.setProperty(DOWNLOAD_NUMBER_OF_THREADS, Integer
					.toString(downloadNumberOfThreads));
			prop.setProperty(DOWNLOAD_DIR, downloadDir);
			prop.setProperty(DOWNLOAD_FILES, downloadFiles);
			prop.setProperty(LISTE_EXCLUSION, ListeExclusion);

			prop.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
		try {
			fos.close();
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	/**
	 * Getter for property downloadNumberOfThreads.
	 * @return Value of property downloadNumberOfThreads.
	 */
	public int getDownloadNumberOfThreads() {
		return downloadNumberOfThreads;
	}

	/**
	 * Setter for property downloadNumberOfThreads.
	 * @param downloadNumberOfThreads New value of property
	 *            downloadNumberOfThreads.
	 */
	public void setDownloadNumberOfThreads(int downloadNumberOfThreads) {
		this.downloadNumberOfThreads = downloadNumberOfThreads;
	}

	/**
	 * Getter for property downloadDir.
	 * @return Value of property downloadDir.
	 */
	public java.lang.String getDownloadDir() {
		return downloadDir;
	}

	/**
	 * Setter for property downloadDir.
	 * @param downloadDir New value of property downloadDir.
	 */
	public void setDownloadDir(java.lang.String downloadDir) {
		this.downloadDir = downloadDir;
	}

	/**
	 * Getter for property downloadFiles.
	 * @return Value of property downloadFiles.
	 */
	public java.lang.String getDownloadFiles() {
		return downloadFiles;
	}

	/**
	 * Setter for property downloadFiles.
	 * @param downloadFiles New value of property downloadFiles.
	 */
	public void setDownloadFiles(java.lang.String downloadFiles) {
		this.downloadFiles = downloadFiles;
	}

	public void setListeExclu(String ListeExclusion) {
		this.ListeExclusion = ListeExclusion;
	}

}
