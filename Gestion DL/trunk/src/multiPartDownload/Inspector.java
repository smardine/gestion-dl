package multiPartDownload;

import java.net.URL;

/**
 * @author lio
 */
public interface Inspector {

	public void inspect(URL url, int level, boolean sameHost, String wildcard);

	public void inspect(URL url, String wildcard);

}
