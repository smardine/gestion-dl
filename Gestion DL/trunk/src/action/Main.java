package action;
/*
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;*/


public class Main {

	/*
	 * Cette application permet de lire un fichier config.
	 * Le fichier comporte des lignes comme: " Nom = Supinfo
	 * 										   Ville = Paris
	 * 										   ... "
	 * 
	 */
/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String txt,txt2;
    	ConfigMgt config;

		try {
		 	File monfichier=new File("C:\\","monfichier.ini");  //on cr�e une instance de fichier
	    	monfichier.createNewFile();                         //et l� on cr�e le fichier physiquement
	 
	  // On cr�e une instance config qui permet de g�rer la gestion d'un fichier   	
			config = new ConfigMgt("monfichier.ini","C:\\",'#');  // '#' c'est le caract�re qui permet de designer les commentaires
	        
	        
      // on peut ajouter des donn�es � la fin du fichier
		        config.addVariable("Nom","Supinfo"); // on a ajouter des nouvelles donn�es � la fin du fichier temporaire
		        config.addVariable("Ville","Paris");
		        //Puis pour valider d�finitivement les donn�es (e.g. lors de la fermeture de l'application) : 
		        config.valideSauveDansFichier(); // on copie le fichier temporaire sur le fichier monfichier.ini
	
		    
	  // On va lire la valeur de "Nom" et de "Ville":
	        txt = config.getValeurDe("Nom");
	        txt2 = config.getValeurDe("Ville");
	        System.out.println("Le nom:"+txt+"  La ville:"+txt2);
	        
   
	  // De la m�me fa�on, on peut mettre � jour les donn�es :
	        config.setValeurDe("Nom","Fatih");  //on change la valeur de Nom par "Fatih" (Nom = Fatih)
	        // Ces donn�es sont sauvegard�es sur fichier par : (dans un fichier temporaire)
	        config.sauveConfigDansFichierTmp(); 
	        //Puis pour valider d�finitivement les donn�es (e.g. lors de la fermeture de l'application) : 
	        config.valideSauveDansFichier();
	        
	 //Pour voir la nouvelle valeur de "Nom"
	        txt = config.getValeurDe("Nom");
	        System.out.println("Le nom:"+txt);
	
	 // Vous pouvez aussi lister les fichiers qui sont dans un r�pertoire
	        File[] fichier;
	        fichier=config.listeFichier("C:\\","txt"); // emplacement et le type du fichier (extension)
	        // on affiche les fichiers
	        for(int i=0;i<fichier.length;i++)
	        {	
	     	   System.out.println(i+"- "+fichier[i].getName());
	        }
	        
		} catch (NullPointerException e) {
			
			System.out.println("erreur1");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("erreur2");
			
			e.printStackTrace();
		}

	        
	}*/

}
