/*
 * PartialDownloadContext.java Created on 26 septembre 2003, 23:41
 */

package multiPartDownload;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author lio
 */
public class PartialDownloadContext {
	public final ByteBuffer requestBuffer = ByteBuffer
			.allocateDirect(Parameters.BUFFER_SIZE);
	public final CharBuffer requestCharBuffer = CharBuffer
			.allocate(Parameters.BUFFER_SIZE);

	public final CharsetEncoder encoder = RequestProcessor.charset.newEncoder();
	public final CharsetDecoder decoder = RequestProcessor.charset.newDecoder();

	public PartialContextFactory factory;

	public PartialDownloadContext(PartialContextFactory factory) {
		this.factory = factory;
	}

	public void releaseContext() {
		factory.returnContext(this);
	}

}
