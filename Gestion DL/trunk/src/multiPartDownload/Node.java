/*
 * Node.java Created on 30 septembre 2003, 20:36
 */

package multiPartDownload;

/**
 * @author lio
 */
public class Node {
	public static final char ANY_CHAR = '?';
	public static final char ANY_SEQUENCE = '*';

	private Association nextChar;
	private Node AnySeqNode;

	/** Creates a new instance of Node */
	public Node() {
	}

	public void setTransition(char c, Node n) {
		if (c == ANY_SEQUENCE)
			AnySeqNode = n;
		else
			nextChar = new Association(c, n);
	}

	public Node getNext(char c) {
		if (nextChar != null) {
			if ((nextChar.c == c) || (nextChar.c == ANY_CHAR))
				return nextChar.n;
		}
		if (AnySeqNode != null)
			return AnySeqNode;

		return null;
	}

	private static class Association {
		public char c;
		public Node n;

		public Association(char c, Node n) {
			this.c = c;
			this.n = n;
		}
	}
}
