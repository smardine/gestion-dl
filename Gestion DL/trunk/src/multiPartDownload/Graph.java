/*
 * Graph.java Created on 30 septembre 2003, 21:16
 */

package multiPartDownload;

/**
 *Immutable thus threadsafe
 * @author lio
 */
public class Graph extends Node {
	private Node end;
	private Node current;

	/** Creates a new instance of Graph */
	public Graph() {
		super();
		current = this;
	}

	public boolean match(String s) {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			current = current.getNext(c);
			if (current == null) {
				reset();
				return false;
			}
		}

		if (current == end) {
			reset();
			return true;
		} else {
			reset();
			return false;
		}
	}

	private void reset() {
		current = this;
	}

	public void setEndNode(Node n) {
		end = n;
	}
}
