package dialogue;

import multiPartDownload.FIFO;
import multiPartDownload.FIFOWithSemaphore;

public class DownloadManager {
	private FIFO downloadQueue = new FIFOWithSemaphore();

	/** No instance of DowloadManager is created */
	private DownloadManager() {
	}

	private static DownloadManager theDM = new DownloadManager();

	public static DownloadManager getInstance() {
		return theDM;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new FEN_Download();
	}

	/**
	 * Getter for property downloadQueue.
	 * @return Value of property downloadQueue.
	 */
	public FIFO getDownloadQueue() {
		return downloadQueue;
	}

}
