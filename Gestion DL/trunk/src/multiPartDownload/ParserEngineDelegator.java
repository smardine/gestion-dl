package multiPartDownload;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.html.parser.DTD;

/**
 * @author lio
 */
public class ParserEngineDelegator {
	private static DTD dtd = null;
	private static final ParserEngineDelegator theDelegator = new ParserEngineDelegator();

	/** Creates a new instance of EconomicalParserDelegator */
	private ParserEngineDelegator() {
		setDefaultDTD();
	}

	public static ParserEngineDelegator getInstance() {
		return theDelegator;
	}

	protected static void setDefaultDTD() {
		if (dtd == null) {
			DTD _dtd = null;
			// (PENDING) Hate having to hard code!
			String nm = "html32";
			try {
				_dtd = DTD.getDTD(nm);
			} catch (IOException e) {
				// (PENDING) UGLY!
				System.out
						.println("Throw an exception: could not get default dtd: "
								+ nm);
			}
			_dtd = createDTD(_dtd, nm);
			dtd = _dtd;
		}
	}

	@SuppressWarnings("static-access")
	protected static DTD createDTD(DTD dtd, String name) {
		InputStream in = null;
		@SuppressWarnings("unused")
		boolean debug = true;
		try {
			String path = name + ".bdtd";
			in = getResourceAsStream(path);
			/* System.out.println(in); */
			if (in != null) {
				dtd.read(new DataInputStream(new BufferedInputStream(in)));
				dtd.putDTDHash(name, dtd);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return dtd;
	}

	public static ParseEngine createParser() {
		return new ParseEngine(dtd);
	}

	static InputStream getResourceAsStream(String name) {
		try {
			return ResourceLoader.getResourceAsStream(name);
		} catch (Throwable e) {
			// If the class doesn't exist or we have some other
			// problem we just try to call getResourceAsStream directly.
			return ParserEngineDelegator.class.getResourceAsStream(name);
		}
	}
	/*
	 * public static void main(String args[]){ new ParserEngineDelegator(); }
	 */
}
