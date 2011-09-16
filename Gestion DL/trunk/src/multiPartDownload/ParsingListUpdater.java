/*
 * ParsingListUpdater.java Created on 27 septembre 2003, 01:37
 */

package multiPartDownload;

import java.util.Vector;
import javax.swing.JList;

/**
 * @author lio
 */
public class ParsingListUpdater {
	private static JList jl;

	/** Creates a new instance of ParsingListUpdater */
	@SuppressWarnings("static-access")
	ParsingListUpdater(JList jl) {
		this.jl = jl;
	}

	@SuppressWarnings("unchecked")
	public static void updateParsingList(Vector v) {
		if (jl.isShowing() && jl != null)
			jl.setListData(v);
	}
}
