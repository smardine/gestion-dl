package multiPartDownload;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @author lio
 */
public class RunnablePartialDownload implements Runnable, Watchable {
	private long begin;
	private long length;
	private String path;
	private String host;
	private FileChannel fc;
	private RandomAccessFile rand;
	private boolean busy = true;
	private WatchDog dog;
	private String cheminDownload;
	private long tailleFichieràDownload;
	private JProgressBar progress;
	private JLabel vitesseActuelle;
	private File encours;

	private PartialDownloadContext context = PartialContextFactory
			.getInstance().getContext();

	public final ByteBuffer requestBuffer = context.requestBuffer;
	public final CharBuffer requestCharBuffer = context.requestCharBuffer;

	public final CharsetEncoder encoder = context.encoder;
	public final CharsetDecoder decoder = context.decoder;

	/** Creates a new instance of RunnablePartialDownload */
	public RunnablePartialDownload(RandomAccessFile rand, String host,
			String path, long begin, long length, WatchDog dog,
			String cheminDownload, long tailleFichieràDownload,
			JProgressBar progress, JLabel Vitesse, File encours) {
		try {
			this.rand = rand;
			fc = rand.getChannel();
			this.begin = begin;
			this.length = length;
			this.host = host;
			this.path = path;
			this.setDog(dog);
			this.setCheminDownload(cheminDownload);
			this.setTailleFichieràDownload(tailleFichieràDownload);
			this.progress = progress;
			this.vitesseActuelle = Vitesse;
			this.encours = encours;
		} catch (Exception e) {
		}
	}

	public void run() {
		try {
			encours.createNewFile();
			long HeureDebut = System.currentTimeMillis();

			progress.setVisible(true);
			vitesseActuelle.setVisible(true);
			SocketChannel chan = SocketChannel.open(new InetSocketAddress(host,
					80));
			startDownload();
			chan.write(requestBuffer);
			requestBuffer.clear();
			chan.read(requestBuffer);
			requestBuffer.flip();
			boolean found = false;
			byte char1 = RequestProcessor.end[0];
			byte char2 = RequestProcessor.end[1];
			byte char3 = RequestProcessor.end[2];
			byte char4 = RequestProcessor.end[3];
			try {
				while (!found) {
					if (requestBuffer.get() == char1) {
						if (requestBuffer.get() == char2) {
							if (requestBuffer.get() == char3) {
								if (requestBuffer.get() == char4) {
									found = true;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// do nothing
			}

			fc.position(begin);

			int writtenBytes = fc.write(requestBuffer);

			long position = fc.position();
			long size = length;
			if ((length - writtenBytes) > 0) {
				long chunckSize = (length - writtenBytes) / 100;

				for (int i = 1; i <= 100; i++) {

					position += fc
							.transferFrom(chan, fc.position(), chunckSize);

					fc.position(position);

					int progressionEnCours = (int) ((100 * (position - begin)) / size);

					progress.setValue(progressionEnCours);
					progress.setString(progressionEnCours + " %");
					long HeureActuelle = System.currentTimeMillis();

					long Vitesse = (long) ((position - begin) / (HeureActuelle - HeureDebut));

					vitesseActuelle.setText(" Vitesse : " + Vitesse + " Ko/s");
				}
			}

			progress.setValue(100);
			progress.setString(100 + " %");
			vitesseActuelle.setText("Finalistation...");

			chan.close();
			fc.close();
			rand.close();
			busy = false;
			// dog.check();
			context.releaseContext();

			encours.delete();
			vitesseActuelle.setText("Terminé...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startDownload() {
		requestCharBuffer.clear();
		requestCharBuffer.put(RequestProcessor.GET);
		requestCharBuffer.put(path);
		requestCharBuffer.put(RequestProcessor.VERSION);
		requestCharBuffer.put(RequestProcessor.LFCR);

		requestCharBuffer.put(RequestProcessor.HOST);
		requestCharBuffer.put(host);
		requestCharBuffer.put(RequestProcessor.LFCR);

		requestCharBuffer.put(RequestProcessor.ACCEPT_ALL);
		requestCharBuffer.put(RequestProcessor.LFCR);

		requestCharBuffer.put(RequestProcessor.RANGE);
		requestCharBuffer.put(RequestProcessor.BYTES);
		requestCharBuffer.put(Long.toString(begin));
		requestCharBuffer.put(RequestProcessor.RANGE_SEPARATOR);
		requestCharBuffer.put(Long.toString(begin + length));
		requestCharBuffer.put(RequestProcessor.LFCR);
		requestCharBuffer.put(RequestProcessor.LFCR);

		requestCharBuffer.flip();
		encoder.reset().encode(requestCharBuffer,
				(ByteBuffer) requestBuffer.clear(), true);
		encoder.flush(requestBuffer);
		requestBuffer.flip();
	}

	public boolean isBusy() {
		return busy;
	}

	public FileChannel getFC() {
		return fc;
	}

	public void setDog(WatchDog dog) {
		this.dog = dog;
	}

	public WatchDog getDog() {
		return dog;
	}

	public void setCheminDownload(String cheminDownload) {
		this.cheminDownload = cheminDownload;
	}

	public String getCheminDownload() {
		return cheminDownload;
	}

	public void setTailleFichieràDownload(long tailleFichieràDownload) {
		this.tailleFichieràDownload = tailleFichieràDownload;
	}

	public long getTailleFichieràDownload() {
		return tailleFichieràDownload;
	}
}
