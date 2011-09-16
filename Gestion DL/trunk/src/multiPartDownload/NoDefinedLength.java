/*
 * NoDefinedLenth.java Created on 13 septembre 2003, 22:39
 */

package multiPartDownload;

/**
 * @author lio
 */
@SuppressWarnings("serial")
public class NoDefinedLength extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>NoDefinedLenth</code> without detail
	 * message.
	 */
	public NoDefinedLength() {
	}

	/**
	 * Constructs an instance of <code>NoDefinedLenth</code> with the specified
	 * detail message.
	 * @param msg the detail message.
	 */
	public NoDefinedLength(String msg) {
		super(msg);
	}
}
