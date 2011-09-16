/*
 * GraphExtractor.java Created on 30 septembre 2003, 21:17
 */

package multiPartDownload;

/**
 * @author lio
 */
public class GraphExtractor {

	/** Creates a new instance of GraphExtractor */
	private GraphExtractor() {
	}

	public static Graph createGraph(String s) throws IllegalArgumentException {
		Graph g = new Graph();
		Node currentNode = g;
		Node previousAnySequence = null;
		int len = s.length();
		for (int i = 0; i < len; i++) {
			char currentChar = s.charAt(i);
			if (currentChar == Node.ANY_CHAR) {
				Node newNode = new Node();
				currentNode.setTransition(Node.ANY_CHAR, newNode);
				currentNode = newNode;
			} else if (currentChar == Node.ANY_SEQUENCE) {
				currentNode.setTransition(Node.ANY_SEQUENCE, currentNode);
				if (previousAnySequence == currentNode)
					throw new IllegalArgumentException();
				previousAnySequence = currentNode;
			} else {
				Node newNode = new Node();
				if (previousAnySequence != null)
					newNode.setTransition(Node.ANY_SEQUENCE,
							previousAnySequence);

				currentNode.setTransition(currentChar, newNode);
				currentNode = newNode;
			}
		}
		g.setEndNode(currentNode);
		return g;
	}
	/*
	 * public static void main(String args[]){ try { Graph
	 * g=GraphExtractor.createGraph("*.*htm*");
	 * System.out.println(g.match("toto.html"));
	 * System.out.println(g.match("toto.shtml"));
	 * System.out.println(g.match("toto.htm"));
	 * System.out.println(g.match("toto.dhtml")); } catch (Exception e){
	 * e.printStackTrace(); } }
	 */

}
