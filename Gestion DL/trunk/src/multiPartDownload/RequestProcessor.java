package multiPartDownload;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * @author lio
 */
public class RequestProcessor implements TerminationListener {
	public static final String separator = System.getProperty("file.separator");

	private int numberOfThreads = Parameters.getInstance()
			.getDownloadNumberOfThreads();
	private String cheminDownload = Parameters.getInstance().getDownloadDir();

	private RequestSupervisor supervisor;

	private RunnablePartialDownload engineTab[];
	private static long time;

	// /////////////////////////// HTML Cons
	// ///////////////////////////////////////

	public static final String LFCR = "\r\n";
	public static final String END_OF_HEADER = LFCR + LFCR;
	public static final String VERSION = " HTTP/1.1";
	public static final char SP = ' ';
	public static final String HEAD = "HEAD ";
	public static final String POST = "POST ";
	public static final String GET = "GET ";
	public static final String RANGE = "Range: ";
	public static final String BYTES = "bytes=";
	public static final String LOCATION = "(Location: )(.*)" + LFCR;
	public static final char RANGE_SEPARATOR = '-';
	public static final char FINAL_RANGE_SEPARATOR = '/';
	public static final String HOST = "Host: ";
	public static final String ACCEPT_ALL = "Accept: */*";
	public static final String LENGTH = "(Content-length: )(\\d*)";
	public static final String RESPONSE = "(HTTP/1.1 )(\\d*)";

	// /////////////////////////// encoding
	// ///////////////////////////////////////
	public static final Charset charset = Charset.forName("US-ASCII");
	private static final CharsetEncoder encoder = charset.newEncoder();
	private static final CharsetDecoder decoder = charset.newDecoder();

	public static byte end[] = null;
	static {
		try {
			end = encoder.reset().encode(CharBuffer.wrap(END_OF_HEADER))
					.array();

		} catch (CharacterCodingException e) {
			// this case should no happen
		}

	}

	private static Pattern pattern = Pattern.compile(LENGTH,
			Pattern.CASE_INSENSITIVE);
	private static Pattern responsePattern = Pattern.compile(RESPONSE,
			Pattern.CASE_INSENSITIVE);
	private static Pattern locationPattern = Pattern.compile(LOCATION,
			Pattern.CASE_INSENSITIVE);

	// /////////////////////////// Buffer
	// ///////////////////////////////////////
	private static ByteBuffer requestBuffer = ByteBuffer
			.allocateDirect(Parameters.BUFFER_SIZE);
	private static CharBuffer requestCharBuffer = CharBuffer
			.allocate(Parameters.BUFFER_SIZE);

	private String host;
	private String path;
	private long contentLength;
	private JProgressBar progress;
	private JLabel LabelVitesse;

	/** Creates a new instance of RequestProcessor */
	public RequestProcessor(RequestSupervisor supervisor) {
		this.supervisor = supervisor;
		engineTab = new RunnablePartialDownload[numberOfThreads];
	}

	public void processRequest(Request req, JProgressBar progress1,
			JProgressBar progress2, JProgressBar progress3,
			JProgressBar progress4, JLabel label1, JLabel label2,
			JLabel label3, JLabel label4) {
		time = System.currentTimeMillis();

		URL url = req.getUrl();
		path = url.getQuery() == null ? url.getPath() : url.getPath() + "?"
				+ url.getQuery();
		host = url.getHost();

		WatchDog dog = new WatchDog(null);
		dog.addTerminationListener(this);
		int prefNumberOfThread = Parameters.getInstance()
				.getDownloadNumberOfThreads();

		if (engineTab.length < prefNumberOfThread) {
			RunnablePartialDownload tab[] = new RunnablePartialDownload[prefNumberOfThread];
			engineTab = tab;
			numberOfThreads = prefNumberOfThread;
		} else if (engineTab.length > prefNumberOfThread) {
			RunnablePartialDownload tab[] = new RunnablePartialDownload[prefNumberOfThread];
			engineTab = tab;
			numberOfThreads = prefNumberOfThread;
		}
		try {
			followRedirects();
			String file;
			if (path.endsWith("/"))
				file = "index.html";
			file = path.substring(path.lastIndexOf('/') + 1);
			if (file.contains("%20") == true) {
				file = file.replaceAll("%20", " ");
			}
			if (file.contains("&amp;") == true) {
				file = file.replaceAll("&amp;", " and ");
			}
			if (file.contains("%5B") == true) {
				file = file.replaceAll("%5B", "[");
			}
			if (file.contains("%5D") == true) {
				file = file.replaceAll("%5D", "]");
			}
			if (file.contains("%28") == true) {
				file = file.replaceAll("%28", "(");
			}
			if (file.contains("%29") == true) {
				file = file.replaceAll("%29", ")");
			}
			contentLength = contentLength();
			File f = new File(separator + file);

			RandomAccessFile random = new RandomAccessFile(f, "rw");
			random.setLength(contentLength);
			random.close();
			// MessageDisplayer.displayMessage("Downloading...");
			if (contentLength > 0) {
				long begin = 0l;
				long end = 0l;
				long part = contentLength / numberOfThreads;
				File encours = null;
				for (int i = 0; i < numberOfThreads - 1; i++) {
					if (i == 0) {
						progress = progress1;
						LabelVitesse = label1;
						encours = new File("./encours0");
					}
					if (i == 1) {
						progress = progress2;
						LabelVitesse = label2;
						encours = new File("./encours1");
					}
					if (i == 2) {
						progress = progress3;
						LabelVitesse = label3;
						encours = new File("./encours2");
					}
					if (i == 3) {
						progress = progress4;
						LabelVitesse = label4;
						encours = new File("./encours3");
					}

					end += part;
					RandomAccessFile rand = new RandomAccessFile(f, "rw");
					engineTab[i] = new RunnablePartialDownload(rand, host,
							path, begin, part, dog,
							cheminDownload + "/" + file, contentLength,
							progress, LabelVitesse, encours);
					begin = end;
				}
				RandomAccessFile rand = new RandomAccessFile(f, "rw");
				engineTab[numberOfThreads - 1] = new RunnablePartialDownload(
						rand, host, path, begin, contentLength - begin, dog,
						cheminDownload + "/" + file, contentLength, progress4,
						label4, new File("./encours3"));

				dog.setWatch(engineTab);
				for (int i = 0; i < numberOfThreads; i++) {
					new Thread(engineTab[i]).start();
				}
			}/*
			 * else { RandomAccessFile rand= new RandomAccessFile(f,"rw");
			 * RunnablePartialDownload run=new
			 * RunnablePartialDownload(rand,host,
			 * path,0,contentLength,dog,cheminDownload
			 * +"/"+file,contentLength,progress1,label1);
			 * RunnablePartialDownload tab[]=new RunnablePartialDownload[1];
			 * tab[0]=run; dog.setWatch(tab); new Thread(run).start(); }
			 */

		} catch (NoDefinedLength e) {
			// MessageDisplayer.displayMessage("Content length not defined");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadResponse bad) {
			// MessageDisplayer.displayMessage("bad response code :"+bad.getMessage());
		}
	}

	/** Cuts up the different part of the file */
	private void followRedirects() throws BadResponse {
		try {
			SocketChannel chan = SocketChannel.open(new InetSocketAddress(host,
					80));
			writeRequest(host, path);
			chan.write(requestBuffer);
			requestBuffer.clear();
			chan.read(requestBuffer);
			requestBuffer.flip();
			decoder.reset().decode(requestBuffer,
					(CharBuffer) requestCharBuffer.clear(), true);
			decoder.flush(requestCharBuffer);

			requestCharBuffer.flip();
			// System.out.println(requestCharBuffer.toString());
			Matcher matchResponse = responsePattern.matcher(requestCharBuffer);
			if (matchResponse.find()) {
				String mess = matchResponse.group(2);
				int code = Integer.parseInt(mess);
				// MessageDisplayer.displayMessage("code "+mess+".");
				if (code >= 400)
					throw new BadResponse(mess);
				else if ((code > 300) && (code < 308)) {
					// MessageDisplayer.displayMessage("Redirecting...");
					Matcher matchLocation = locationPattern
							.matcher(requestCharBuffer);
					if (matchLocation.find()) {
						String newStringURL = matchLocation.group(2);
						try {
							URL url = new URL(newStringURL);
							host = url.getHost();
							path = url.getQuery() == null ? url.getPath() : url
									.getPath()
									+ "?" + url.getQuery();
							followRedirects();
						} catch (MalformedURLException e) {
							e.printStackTrace();
							// MessageDisplayer.displayMessage("Could not redirect");
						}
					} else {
						throw new BadResponse("Could not redirect");
					}
				}
			}
			chan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeRequest(String host, String path) {
		requestCharBuffer.clear();
		requestCharBuffer.put(HEAD);
		requestCharBuffer.put(path);
		requestCharBuffer.put(VERSION);
		requestCharBuffer.put(LFCR);

		requestCharBuffer.put(HOST);
		requestCharBuffer.put(host);
		requestCharBuffer.put(LFCR);

		requestCharBuffer.put(LFCR);

		requestCharBuffer.flip();
		encoder.reset().encode(requestCharBuffer,
				(ByteBuffer) requestBuffer.clear(), true);
		encoder.flush(requestBuffer);
		requestBuffer.flip();
	}

	public void teminated() {
		supervisor.releaseLock();
		StringBuffer buf = new StringBuffer(10);
		long dur = System.currentTimeMillis() - time;

		buf.append("Terminated ");
		if (dur > 2000l) {
			buf.append(dur / 1000);
			buf.append(" s, ");
		} else {
			buf.append(dur);
			buf.append(" ms, ");
		}

		// MessageDisplayer.displayMessage(buf.toString());

	}

	public long contentLength() throws NoDefinedLength {
		requestCharBuffer.position(0);
		Matcher matchLength = pattern.matcher(requestCharBuffer);
		if (!matchLength.find())
			throw new NoDefinedLength();
		return Long.parseLong(matchLength.group(2));
	}

	public void processRequest(URL url, String Chemin, JProgressBar progress1,
			JProgressBar progress2, JProgressBar progress3,
			JProgressBar progress4, JProgressBar progress5,
			JProgressBar progress6, JProgressBar progress7,
			JProgressBar progress8, JLabel label1, JLabel label2,
			JLabel label3, JLabel label4, JLabel label5, JLabel label6,
			JLabel label7, JLabel label8) {
		time = System.currentTimeMillis();

		path = url.getQuery() == null ? url.getPath() : url.getPath() + "?"
				+ url.getQuery();
		host = url.getHost();

		WatchDog dog = new WatchDog(null);
		dog.addTerminationListener(this);
		int prefNumberOfThread = Parameters.getInstance()
				.getDownloadNumberOfThreads();
		// int prefNumberOfThread=nbDeThread;
		if (engineTab.length < prefNumberOfThread) {
			RunnablePartialDownload tab[] = new RunnablePartialDownload[prefNumberOfThread];
			engineTab = tab;
			numberOfThreads = prefNumberOfThread;
		} else if (engineTab.length > prefNumberOfThread) {
			RunnablePartialDownload tab[] = new RunnablePartialDownload[prefNumberOfThread];
			engineTab = tab;
			numberOfThreads = prefNumberOfThread;
		}
		try {
			followRedirects();
			String file;
			if (path.endsWith("/"))
				file = "index.html";
			file = path.substring(path.lastIndexOf('/') + 1);

			if (file.contains("%20") == true) {
				file = file.replaceAll("%20", " ");
			}
			if (file.contains("&amp;") == true) {
				file = file.replaceAll("&amp;", " and ");
			}
			if (file.contains("%5B") == true) {
				file = file.replaceAll("%5B", "[");
			}
			if (file.contains("%5D") == true) {
				file = file.replaceAll("%5D", "]");
			}
			if (file.contains("%28") == true) {
				file = file.replaceAll("%28", "(");
			}
			if (file.contains("%29") == true) {
				file = file.replaceAll("%29", ")");
			}

			contentLength = contentLength();
			File f = new File(Chemin + separator + file);

			RandomAccessFile random = new RandomAccessFile(f, "rw");
			random.setLength(contentLength);
			random.close();
			// MessageDisplayer.displayMessage("Downloading...");
			if (contentLength > 0) {
				long begin = 0l;
				long end = 0l;
				long part = contentLength / numberOfThreads;
				File encours = null;
				for (int i = 0; i < numberOfThreads - 1; i++) {
					if (i == 0) {
						progress = progress2;
						LabelVitesse = label2;
						encours = new File("./encours1");
					}
					if (i == 1) {
						progress = progress3;
						LabelVitesse = label3;
						encours = new File("./encours2");
					}
					if (i == 2) {
						progress = progress4;
						LabelVitesse = label4;
						encours = new File("./encours3");
					}
					if (i == 3) {
						progress = progress5;
						LabelVitesse = label5;
						encours = new File("./encours4");
					}
					if (i == 4) {
						progress = progress6;
						LabelVitesse = label6;
						encours = new File("./encours5");
					}
					if (i == 5) {
						progress = progress7;
						LabelVitesse = label7;
						encours = new File("./encours6");
					}
					if (i == 6) {
						progress = progress8;
						LabelVitesse = label8;
						encours = new File("./encours7");
					}
					/*
					 * if (i==7){ progress=progress8; LabelVitesse = label8;
					 * encours = new File ("./encours7"); }
					 */

					end += part;
					RandomAccessFile rand = new RandomAccessFile(f, "rw");
					engineTab[i] = new RunnablePartialDownload(rand, host,
							path, begin, part, dog,
							cheminDownload + "/" + file, contentLength,
							progress, LabelVitesse, encours);
					begin = end;
				}
				RandomAccessFile rand = new RandomAccessFile(f, "rw");
				engineTab[numberOfThreads - 1] = new RunnablePartialDownload(
						rand, host, path, begin, contentLength - begin, dog,
						cheminDownload + "/" + file, contentLength, progress1,
						label1, new File("./encours0"));

				dog.setWatch(engineTab);
				for (int i = 0; i < numberOfThreads; i++) {
					new Thread(engineTab[i]).start();
				}
			} /*
			 * else { RandomAccessFile rand= new RandomAccessFile(f,"rw");
			 * RunnablePartialDownload run=new
			 * RunnablePartialDownload(rand,host,
			 * path,0,contentLength,dog,cheminDownload
			 * +"/"+file,contentLength,progress1,label1,new File
			 * ("./encours0")); RunnablePartialDownload tab[]=new
			 * RunnablePartialDownload[1]; tab[0]=run; dog.setWatch(tab); new
			 * Thread(run).start(); }
			 */

		} catch (NoDefinedLength e) {
			// MessageDisplayer.displayMessage("Content length not defined");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadResponse bad) {
			// MessageDisplayer.displayMessage("bad response code :"+bad.getMessage());
		}
	}
}
