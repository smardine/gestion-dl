/*
 * FIFOWithSemaphore.java Created on 25 septembre 2003, 15:54
 */

package multiPartDownload;

import java.util.concurrent.Semaphore;

/**
 * @author lio
 */
public class FIFOWithSemaphore extends FIFO {
	private Semaphore sem;

	/** Creates a new instance of FIFOWithSemaphore */
	public FIFOWithSemaphore() {
		super();
		sem = new Semaphore(3);
	}

	public Request releaseRequest() throws InterruptedException {
		sem.acquire();
		return super.releaseRequest();
	}

	public void allow() {
		sem.release();
	}
}
