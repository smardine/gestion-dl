package multiPartDownload;

import java.util.Vector;

/**
 * @author lio
 */
public class WatchDog {
	@SuppressWarnings("unchecked")
	private Vector vector = new Vector();
	private Watchable watch[];

	/** Creates a new instance of WatchDog */
	public WatchDog(Watchable watch[]) {
		this.watch = watch;
	}

	/**
	 * Getter for property watch.
	 * @return Value of property watch.
	 */
	public Watchable[] getWatch() {
		return this.watch;
	}

	/**
	 * Setter for property watch.
	 * @param watch New value of property watch.
	 */
	public void setWatch(Watchable[] watch) {
		this.watch = watch;
	}

	public void check() {
		if (watch != null && watch.length != 0) {
			for (int i = watch.length; i-- > 0;) {
				if (watch[i].isBusy())
					return;
			}
			notifyListeners();
		}
	}

	@SuppressWarnings("unchecked")
	public void addTerminationListener(TerminationListener l) {
		synchronized (this) {
			if ((l != null) && (!vector.contains(l))) {
				vector.add(l);
			}
		}
	}

	public void notifyListeners() {
		synchronized (this) {
			for (int i = vector.size(); i-- > 0;) {
				((TerminationListener) vector.elementAt(i)).teminated();
			}
		}
	}
}
