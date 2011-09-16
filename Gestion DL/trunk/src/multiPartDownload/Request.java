package multiPartDownload;

import java.net.URL;

/**
 * @author lio
 */
public class Request {
	private URL url;
	private int percentage = 0;

	public static final int PROCESSING = 0;
	public static final int PROCESSED = 1;
	public static final int TOPROCESS = 2;

	private int state = TOPROCESS;

	public Request(URL url) {
		this.url = url;
	}

	/**
	 * Getter for property url.
	 * @return Value of property url.
	 */
	public final java.net.URL getUrl() {
		return url;
	}

	/**
	 * Setter for property url.
	 * @param url New value of property url.
	 */
	public final void setUrl(java.net.URL url) {
		this.url = url;
	}

	public String toString() {
		return url.toExternalForm() + "\n";
	}

	/**
	 * Getter for property state.
	 * @return Value of property state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Setter for property state.
	 * @param state New value of property state.
	 */
	public void setState(int state) {
		if (state == TOPROCESS || state == PROCESSING || state == PROCESSED)
			this.state = state;
	}

	/**
	 * Getter for property percentage.
	 * @return Value of property percentage.
	 */
	public int getPercentage() {
		return percentage;
	}

	/**
	 * Setter for property percentage.
	 * @param percentage New value of property percentage.
	 */
	public void setPercentage(int percentage) {
		if ((percentage >= 0) && (percentage <= 100))
			this.percentage = percentage;
	}

}
