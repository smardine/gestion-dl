package multiPartDownload;

import java.net.URL;

public class HistoryHashtable extends Object {
	private int count;

	private int threshold;

	private float loadFactor;

	private URLEntry table[];

	public HistoryHashtable(int initialCapacity, float loadFactor) {
		this.loadFactor = loadFactor;
		table = new URLEntry[initialCapacity];
		threshold = (int) (initialCapacity * loadFactor);
	}

	public HistoryHashtable(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}

	public HistoryHashtable() {
		this(11, 0.75f);
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	/** Checks equality */
	private static final boolean preCheck(String file1, int len, String file2) {
		return ((len == file2.length()) && (file1.equals(file2)));
	}

	public synchronized boolean contains(URL value) {
		String file = value.getFile();
		int len = file.length();
		for (int i = table.length; i-- > 0;) {
			for (URLEntry e = table[i]; e != null; e = e.next) {
				if (preCheck(file, len, e.value.getFile())) {
					if (e.value.equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Increases the capacity of and internally reorganizes this hashtable, in
	 * order to accommodate and access its entries more efficiently. This method
	 * is called automatically when the number of keys in the hashtable exceeds
	 * this hashtable's capacity and load factor.
	 */
	protected synchronized void rehash() {
		int oldCapacity = table.length;
		URLEntry oldMap[] = table;

		int newCapacity = oldCapacity * 2 + 1;
		URLEntry newMap[] = new URLEntry[newCapacity];

		threshold = (int) (newCapacity * loadFactor);
		table = newMap;

		for (int i = oldCapacity; i-- > 0;) {
			for (URLEntry old = oldMap[i]; old != null;) {
				URLEntry e = old;
				old = old.next;
				int index = (e.hash & 0x7FFFFFFF) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}

	public synchronized void put(URL value) {
		URLEntry tab[] = table;
		int hash = value.hashCode();
		int index = (hash & 0x7FFFFFFF) % tab.length;
		for (URLEntry e = tab[index]; e != null; e = e.next) {
			if ((e.hash == hash) && value.equals(e.value)) {
				return;
			}
		}

		if (count >= threshold) {
			// Rehash the table if the threshold is exceeded
			rehash();
			tab = table;
			index = (hash & 0x7FFFFFFF) % tab.length;
		}

		// Creates the new entry.

		tab[index] = new URLEntry(hash, value, tab[index]);
		count++;
	}

	public synchronized void clear() {
		URLEntry tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		count = 0;
	}

	/**
	 * Hashtable collision list.
	 */
	private class URLEntry {
		public int hash;
		public URL value;
		public URLEntry next;

		protected URLEntry(int hash, URL value, URLEntry next) {
			this.value = value;
			this.next = next;
			this.hash = hash;
		}

	}
}
