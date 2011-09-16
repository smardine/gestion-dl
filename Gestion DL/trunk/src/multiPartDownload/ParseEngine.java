package multiPartDownload;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Parser;
import javax.swing.text.html.parser.TagElement;

/**
 * @author lio
 */
public class ParseEngine extends Parser implements Runnable, Watchable {
	private Thread thread;
	private static FIFO source = InspectionManager.getInstance().getSource();
	private static FIFO target = InspectionManager.getInstance().getTarget();
	private static WatchDog dog;
	public static final Graph FILE_TO_PARSE = GraphExtractor
			.createGraph("*.*htm*");

	private static ConnectionManager connect = ConnectionManager.getInstance();

	private int generation;
	private Graph wildcard;
	private URL url;
	private boolean sameHost = false;
	private String host;

	private Reader reader;
	private boolean busy;

	public ParseEngine(DTD dtd) {
		super(dtd);
	}

	@SuppressWarnings("static-access")
	public void init(Graph wildcard, String host, WatchDog dog) {
		sameHost = true;
		this.host = host;
		this.wildcard = wildcard;
		this.dog = dog;
	}

	@SuppressWarnings("static-access")
	public void init(Graph wildcard, WatchDog dog) {
		sameHost = false;
		this.wildcard = wildcard;
		this.dog = dog;
	}

	public void run() {
		thread = Thread.currentThread();
		while (this != null) {
			try {
				if (source.isEmpty()) {
					busy = false;
					dog.check();
				}

				Request req = source.releaseRequest();
				busy = true;

				url = req.getUrl();
				generation = ((LinkParserRequest) req).getGeneration();
				reader = connect.getConnectStreamReader(url);
				parse(reader);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				return;
			}

		}
	}

	protected void handleStartTag(TagElement tag) {
		if (tag.getHTMLTag() == HTML.Tag.A) {
			String temp = (String) getAttributes().getAttribute(Attribute.HREF);
			try {
				if (wildcard.match(temp))
					target.acceptRequest(new Request(new URL(url, temp)));
				if (FILE_TO_PARSE.match(temp)) {
					if (generation - 1 > 0) {
						URL urlToParse = new URL(url, temp);
						if ((!sameHost)
								|| (sameHost && urlToParse.getHost() == host))
							source.acceptRequest(new LinkParserRequest(new URL(
									url, temp), generation - 1));
					}
				}
			} catch (MalformedURLException badUrl) {
				badUrl.printStackTrace();
			} catch (AlreadyQueued queued) {
				// do nothing
			}
		}

	}

	protected void handleEmptyTag(TagElement tag) {
		if (tag.getHTMLTag() == HTML.Tag.IMG) {
			String temp = (String) getAttributes().getAttribute(Attribute.SRC);
			try {

				if (wildcard.match(temp)) {
					URL fileUrl = new URL(url, temp);
					target.acceptRequest(new Request(fileUrl));
				}
			} catch (MalformedURLException badUrl) {
				badUrl.printStackTrace();
			} catch (AlreadyQueued queued) {
				// do nothing
			}
		}
	}

	public boolean isBusy() {
		return busy;
	}

	public void stop() {
		thread.interrupt();
	}
}