package multiPartDownload;

import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import dialogue.DownloadManager;

/**
 * @author lio
 */
public class RequestSupervisor implements Runnable {
	private FIFOWithSemaphore downloadQueue = (FIFOWithSemaphore) DownloadManager
			.getInstance().getDownloadQueue();
	@SuppressWarnings("unchecked")
	private Stack stack = downloadQueue.getStack();
	public static JProgressBar p1;
	public static JProgressBar p2;
	public static JProgressBar p3;
	public static JProgressBar p4;
	public static JLabel l1;
	public static JLabel l2;
	public static JLabel l3;
	public static JLabel l4;

	/** Creates a new instance of RequestSupervisor */
	private RequestSupervisor() {
		new Thread(this).start();
	}

	private static RequestSupervisor theRS = new RequestSupervisor();

	public static RequestSupervisor getInstance() {
		return theRS;
	}

	public static void FixeProgressAndLabel(JProgressBar prog1,
			JProgressBar prog2, JProgressBar prog3, JProgressBar prog4,
			JLabel lab1, JLabel lab2, JLabel lab3, JLabel lab4) {
		p1 = prog1;
		p2 = prog2;
		p3 = prog3;
		p4 = prog4;
		l1 = lab1;
		l2 = lab2;
		l3 = lab3;
		l4 = lab4;
	}

	public void run() {
		while (true) {
			try {

				Request r = downloadQueue.releaseRequest();
				DownloadListUpdater.updateDownloadList(stack);
				RequestProcessor proc = new RequestProcessor(this);
				proc.processRequest(r, p1, p2, p3, p4, l1, l2, l3, l4);
				// proc.processRequest(r, null, null, null,null,null, null,null,
				// null);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	public synchronized void releaseLock() {
		downloadQueue.allow();
	}

}
