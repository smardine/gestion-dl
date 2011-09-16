package Utilit;

/*
 * Output: Protocol: http Port: -1 Host: www.java2s.com File:
 * Ext:http://www.java2s.com
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requete {
	public static void main(String args[]) throws Exception {
		URL a_Url = new URL("http://www.w33d.fr/forum/listing_dl.php");
		StringBuilder o_oSb = new StringBuilder();

		// recup du saut de ligne
		String o_sLineSep = null;
		try {
			o_sLineSep = System.getProperty("line.separator");
		} catch (Exception e) {
			o_sLineSep = "\n";
		}

		try {
			HttpURLConnection o_oUrlConn = (HttpURLConnection) a_Url
					.openConnection();
			o_oUrlConn.setRequestMethod("POST");
			o_oUrlConn.setAllowUserInteraction(false);
			// envoyer des params
			o_oUrlConn.setDoOutput(true);

			// poster les params
			PrintWriter o_oParamWriter = new PrintWriter(o_oUrlConn
					.getOutputStream());

			// o_oParamWriter.print(a_sParamsToPost);
			// fermer le post avant de lire le resultat ... logique
			o_oParamWriter.flush();
			o_oParamWriter.close();

			// Lire la reponse
			InputStream o_oResponse = o_oUrlConn.getInputStream();
			BufferedReader o_oBufReader = new BufferedReader(
					new InputStreamReader(o_oResponse));
			String sLine;

			while ((sLine = o_oBufReader.readLine()) != null) {
				o_oSb.append(sLine);
				o_oSb.append(o_sLineSep);
			}
			// deconnection
			o_oUrlConn.disconnect();
		} catch (ConnectException ctx) {
			// Log.fatal(this, "Connection lost : server may be down");
			ctx.printStackTrace();
		} catch (Exception e) {
			// Log.error(this,"postURL : "+e.getMessage());
			e.printStackTrace();
		}
		// Log.debug(this, "retour url="+o_oSb.toString());
		System.out.println("retour url=" + o_oSb.toString());
		// return o_oSb.toString();
	}

	/*
	 * URL hp = newURL(
	 * "http://www.groupemicroconcept.com/scripts/serveursiege.dll/tel?NomUtil=4100050&Version=9.03.0.4500"
	 * ); hp.openConnection(); hp.getFile(); System.out.println("Protocol: " +
	 * hp.getProtocol()); System.out.println("Port: " + hp.getPort());
	 * System.out.println("Host: " + hp.getHost()); System.out.println("File: "
	 * + hp.getFile()); System.out.println("Ext:" + hp.toExternalForm()); }
	 */
}
