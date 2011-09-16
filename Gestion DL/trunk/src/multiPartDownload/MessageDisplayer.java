package multiPartDownload;

import javax.swing.JLabel;
import javax.swing.*;

/**
 * @author lio
 */
public class MessageDisplayer {
	private static JLabel jl;

	private MessageDisplayer() {
	}

	/** Creates a new instance of MessageDisplayer */
	public static void initMessageDisplayer(JLabel jlabel) {
		jl = jlabel;
	}

	public static void displayMessage(String s) {
		jl.setText(s);
	}

	public static String displayDownloadPopUp(String message, String value) {
		return JOptionPane.showInputDialog(null, message, value);
	}

	public static boolean displayResetHistory() {
		int value = JOptionPane.showConfirmDialog(null,
				"Already Inspected, do you want to reset the link history ?",
				"already tested", JOptionPane.OK_CANCEL_OPTION);
		if (value == 2)
			return false;
		else
			return true;

	}

	/*
	 * public static void main(String args[]){
	 * System.out.println(displayDownloadPopUp("toto","gg")); }
	 */
}
