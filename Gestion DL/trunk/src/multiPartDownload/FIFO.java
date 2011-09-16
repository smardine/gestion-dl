package multiPartDownload;

import java.net.URL;
import java.util.Stack;

/**
 * @author lio
 */
public class FIFO {
	@SuppressWarnings("unchecked")
	private Stack stack;

	private HistoryHashtable hash = new HistoryHashtable(100, 0.75f);

	/** Creates a new instance of URLPile */
	@SuppressWarnings("unchecked")
	public FIFO() {
		stack = new Stack();
	}

	@SuppressWarnings("unchecked")
	public synchronized void acceptRequest(Request req) throws AlreadyQueued {

		if (!(hash.contains(req.getUrl()))) {

			hash.put(req.getUrl());

			stack.push(req);

			notify();

		} else {

		}
		// throw new AlreadyQueued();

	}

	public synchronized Request releaseRequest() throws InterruptedException {
		for (;;) {
			if (stack.isEmpty())
				wait();
			if (!stack.isEmpty()) {
				Request r = (Request) stack.pop();
				return r;
			}
		}
	}

	public boolean wasAdded(URL url) {
		return hash.contains(url);
	}

	public String toString() {
		return stack.toString();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public int size() {
		return stack.size();
	}

	public void empty() {
		stack.removeAllElements();
	}

	public void transferInto(FIFO target) {
		for (int i = stack.size(); i-- > 0;) {
			try {
				target.acceptRequest((Request) stack.elementAt(i));
			} catch (Exception e) {
			}
		}
	}

	public void remove(Object o) {
		stack.remove(o);
	}

	@SuppressWarnings("unchecked")
	public Stack getStack() {
		return stack;
	}

	public void resetHistory() {
		hash.clear();
	}
}
