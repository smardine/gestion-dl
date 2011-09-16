/*
 * PartialContextFactory.java Created on 26 septembre 2003, 23:59
 */

package multiPartDownload;

import java.util.Stack;

/**
 * @author lio
 */
public class PartialContextFactory {
	@SuppressWarnings("unchecked")
	private Stack stack = new Stack();
	private static final PartialContextFactory theFac = new PartialContextFactory();

	/** Creates a new instance of PartialContextFactory */
	private PartialContextFactory() {
	}

	public static PartialContextFactory getInstance() {
		return theFac;
	}

	public synchronized PartialDownloadContext getContext() {
		if (stack.isEmpty()) {
			return new PartialDownloadContext(this);
		} else {
			return (PartialDownloadContext) stack.pop();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void returnContext(PartialDownloadContext context) {
		stack.push(context);
	}
}
