package multiPartDownload;

import java.net.URL;

/**
 * @author lio
 */
public class LinkParserRequest extends Request {
	private int generation;

	/** Creates a new instance of LinkParserRequest */
	public LinkParserRequest(URL url, int generation) {
		super(url);
		this.generation = generation;
	}

	/**
	 * Getter for property generation.
	 * @return Value of property generation.
	 */
	public final int getGeneration() {
		return generation;
	}

	/**
	 * Setter for property generation.
	 * @param generation New value of property generation.
	 */
	public final void setGeneration(int generation) {
		this.generation = generation;
	}

}
