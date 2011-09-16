/*
 * BadResponse.java Created on 19 septembre 2003, 21:51
 */

package multiPartDownload;

/**
 * @author lio
 */
public class BadResponse extends java.lang.Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2631641256928508606L;

	/**
	 * Creates a new instance of <code>BadResponse</code> without detail
	 * message.
	 */
	public BadResponse() {
	}

	/**
	 * Constructs an instance of <code>BadResponse</code> with the specified
	 * detail message.
	 * @param msg the detail message.
	 */
	public BadResponse(String msg) {
		super(msg);
	}
}
