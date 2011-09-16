package multiPartDownload;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lio
 */
public class ConnectionManager {
	private int demands;
	private int realized;

	/** Creates a new instance of ConnectionManager */
	private ConnectionManager() {
	}

	private static final ConnectionManager theManager = new ConnectionManager();

	public static final ConnectionManager getInstance() {
		return theManager;
	}

	public Reader getConnectStreamReader(URL url) throws IOException {
		return new InputStreamReader(getConnectStream(url));
	}

	public InputStream getConnectStream(URL url) throws IOException {
		demands++;
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.connect();
		realized++;
		// return new BufferedReader(new
		// InputStreamReader(http.getInputStream()));
		// opti no need to return a buffer ...
		return http.getInputStream();
	}

	public HttpURLConnection getConnection(URL url) throws IOException {
		demands++;
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		// http.connect();
		realized++;
		return http;
	}

	public Reader getConnectStreamAfterDelay(URL url, long delay)
			throws IOException {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		return getConnectStreamReader(url);
	}

	/**
	 * Getter for property realized.
	 * @return Value of property realized.
	 */
	public int getRealized() {
		return realized;
	}

	/**
	 * Setter for property realized.
	 * @param realized New value of property realized.
	 */
	public void setRealized(int realized) {
		this.realized = realized;
	}

	/**
	 * Getter for property demands.
	 * @return Value of property demands.
	 */
	public int getDemands() {
		return demands;
	}

	/**
	 * Setter for property demands.
	 * @param demands New value of property demands.
	 */
	public void setDemands(int demands) {
		this.demands = demands;
	}

}
