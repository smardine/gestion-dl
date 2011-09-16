/*
 * DirectoryFilter.java Created on 30 septembre 2003, 15:48
 */

package multiPartDownload;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author lio
 */
public class DirectoryFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		else
			return false;
	}

	public String getDescription() {
		return "directories";
	}

}
