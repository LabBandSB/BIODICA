/*
BIODICA Navigator software
Copyright (C) 2017-2022 Curie Institute, 26 rue d'Ulm, 75005 Paris - FRANCE

Copyright (C) 2017-2022 National Laboratory Astana, Center for Life Sciences, Nazarbayev University, Nur-Sultan, Kazakhstan



BIODICA Navigator is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.



BIODICA Navigator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.



You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/



/*
BIODICA Navigator authors:
Andrei Zinovyev : http://andreizinovyev.site
Ulykbek Kairov : ulykbek.kairov@nu.edu.kz
Askhat Molkenov : askhat.molkenov@nu.edu.kz
*/

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import model.ConstantCodes;
import util.ConfigHelper;


public class App {
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		/*if (args.length==0) {
            ProcessBuilder pb = new ProcessBuilder(
        		 "java",
                "-Xmx5000M",
                "-jar",
                "BiODICA_New.jar",
                "anArgument"
                );
            pb.start();
        } else {
		*/
		// To remove after testing!!!!
		//ConfigHelper.DEFAULT_DATA_TABLE_PATH = "C:/Datas/BIODICA_GUI/data/OVCA_TCGA/transcriptome/OVCA.txt";
		for(int i=0;i<args.length;i++){
			if(args[i].equals("-config"))
				ConstantCodes.CONFIG_FILE_NAME = args[i+1];
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
						try {
							HashMap<Object, Object> progressDefaults = new HashMap<>();
							for(Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet())
							{
							    if(entry.getKey().getClass() == String.class && ((String)entry.getKey()).startsWith("ProgressBar"))
							    {
							        progressDefaults.put(entry.getKey(), entry.getValue());
							    }
							}
							for(Map.Entry<Object, Object> entry : progressDefaults.entrySet()){
							    UIManager.getDefaults().put(entry.getKey(), entry.getValue());
							}
						} catch(Exception e){ 
					    }
						MainFrame_new mainFrame = new MainFrame_new();
						setUIFont (new FontUIResource(ConstantCodes.FONT_FAMILY,ConstantCodes.FONT_WEIGHT,ConstantCodes.FONT_SIZE));
						mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
			});
      //  }
	}
	
	public static void setUIFont (FontUIResource f){
	    Enumeration<Object> keys = UIManager.getLookAndFeelDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    } 	
}
