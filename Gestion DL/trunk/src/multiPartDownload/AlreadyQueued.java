/*
 * AlreadyQueued.java Created on 19 septembre 2003, 22:19
 */

package multiPartDownload;

/**
 * @author lio
 */
public class AlreadyQueued extends java.lang.Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 181412260625025546L;

	/**
	 * Creates a new instance of <code>AlreadyQueued</code> without detail
	 * message.
	 */
	public AlreadyQueued() {
	}

	/**
	 * Constructs an instance of <code>AlreadyQueued</code> with the specified
	 * detail message.
	 * @param msg the detail message.
	 */
	public AlreadyQueued(String msg) {
		super(msg);
	}
}
