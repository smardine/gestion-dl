package multiPartDownload;

import java.net.URL;

/**
 * @author lio
 */
public class InspectionManager implements Inspector, TerminationListener {
	@SuppressWarnings("unused")
	private static long time;
	private WatchDog dog;
	private ParseEngine engineTab[];

	private FIFO URLToParse = new FIFO();
	private FIFO URLToDownload = new FIFO();

	private static InspectionManager theParser = new InspectionManager();

	protected static ParserEngineDelegator delegate;
	private final Watchable[] temporaryWatchSet = new Watchable[0];

	/** Creates a new instance of InspectionManager */
	private InspectionManager() {
		delegate = ParserEngineDelegator.getInstance();
		engineTab = new ParseEngine[Parameters.getInstance()
				.getNumberOfInspectionTrheads() - 1];
		dog = new WatchDog(temporaryWatchSet);
		dog.addTerminationListener(this);
	}

	public static InspectionManager getInstance() {
		return theParser;
	}

	public final FIFO getSource() {
		return URLToParse;
	}

	public final FIFO getTarget() {
		return URLToDownload;
	}

	public void inspect(URL url, String wildcard) {
		inspect(url, 1, false, wildcard);
	}

	@SuppressWarnings("static-access")
	public void inspect(URL url, int level, boolean sameHost, String wildcard) {
		Graph graph = GraphExtractor.createGraph(wildcard);

		int prefNumberOfThread = Parameters.getInstance()
				.getNumberOfInspectionTrheads();
		// changing paser tab size
		if (engineTab.length < prefNumberOfThread) {
			ParseEngine tab[] = new ParseEngine[prefNumberOfThread];
			System.arraycopy(engineTab, 0, tab, 0, engineTab.length);
			engineTab = tab;
		} else if (engineTab.length > prefNumberOfThread) {
			for (int i = engineTab.length - 1; i-- > prefNumberOfThread - 1;)
				engineTab[i] = null;
			ParseEngine tab[] = new ParseEngine[prefNumberOfThread];
			System.arraycopy(engineTab, 0, tab, 0, engineTab.length);
			engineTab = tab;
		}
		dog.setWatch(temporaryWatchSet);
		// initalizing parseEngine's
		for (int i = Parameters.getInstance().getNumberOfInspectionTrheads(); i-- > 0;) {
			if (engineTab[i] == null) {
				engineTab[i] = ParserEngineDelegator.getInstance()
						.createParser();
				if (sameHost)
					engineTab[i].init(graph, url.getHost(), dog);
				else
					engineTab[i].init(graph, dog);

				new Thread(engineTab[i]).start();
			}
			if (sameHost)
				engineTab[i].init(graph, url.getHost(), dog);
			else
				engineTab[i].init(graph, dog);
		}

		// adding the parse file
		try {
			URLToParse.acceptRequest(new LinkParserRequest(url, level));
			dog.setWatch(engineTab);
		} catch (AlreadyQueued e) {
			MessageDisplayer.displayMessage("Already Tested.");
			if (MessageDisplayer.displayResetHistory()) {
				URLToDownload.resetHistory();
				URLToParse.resetHistory();
				URLToDownload.empty();
				ParsingListUpdater.updateParsingList(URLToDownload.getStack());
				inspect(url, level, sameHost, wildcard);
			}

		}
	}

	public void teminated() {
		MessageDisplayer.displayMessage("Parsing terminated.");
		ParsingListUpdater.updateParsingList(URLToDownload.getStack());
	}

	public void emptyDownloadStack() {
		URLToDownload.empty();
	}

	/**
	 * Getter for property URLToDownload.
	 * @return Value of property URLToDownload.
	 */
	public FIFO getURLToDownload() {
		return URLToDownload;
	}

}
