package logic;

import java.util.HashSet;
import java.util.Vector;

import fr.curie.jnavicell.NaviCell;
import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.VSimpleProcedures;

public class NaviCellLoader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			//String data = "C:/Datas/BIODICA_GUI/work/OVCA_ICA/OVCA_ica_S.xls";
			String data = "C:/Datas/BIODICA_GUI/work/OVCA_NAVICELL/OVCA_ica_S_onlytop.txt";
			//String data = "C:/MyPrograms/_github/JNaviCell/dist/v1.1/DU145_data.txt";
			//String URLNaviCell = "https://navicell.curie.fr/navicell/newtest/maps/infosigmap/master/index.php";
			String URLNaviCell = "https://acsn.curie.fr/navicell/maps/natural_killer_cell/master/index.html";
			//String URLNaviCell = "https://navicell.curie.fr/navicell/maps/cellcycle/master/index.php";
			int compNumber = 3;
			String columnName = "IC"+compNumber;
			//columnName = "data";

			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(data, true, "\t");
			TableUtils.findAllNumericalColumns(vt);
			//VDataTable vt1 = takeOnlyTopContributingGenes(vt,5f);
			//VDatReadWrite.useQuotesEverywhere = false;
			//VDatReadWrite.saveToSimpleDatFile(vt1, data.substring(0, data.length()-4)+"_onlytop.txt");
			
			//LoadNaviCellWithData(data.substring(0, data.length()-4)+"_onlytop.txt",URLNaviCell,columnName,"ICA");
			LoadNaviCellWithData(data,URLNaviCell,columnName,"ICA");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void LoadNaviCellWithData(String dataFile, String URLNaviCell, String columnName, String analysisPrefix){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(dataFile, false, "\t");
		
        NaviCell n = new NaviCell();
        
        n.setMapUrl(URLNaviCell);
        
        n.setProxyUrl("https://navicell.vincent-noel.fr/cgi-bin/nv_proxy.php");

        n.launchBrowser();
        System.out.println("Browser launched.");

        n.importData("", dataFile,  "mRNA Expression data", analysisPrefix);
        System.out.println("Data '+dataFile+' imported");
        
        // select heatmap for visualization
        //n.drawingConfigSelectHeatmap("", true);
        //n.drawingConfigApply("");
        
        n.drawingConfigSelectMapStaining("", true);
        n.drawingConfigApply("");
        
        // select sample and datatable 
        //n.heatmapEditorSelectSample("", 0,columnName);
        //n.heatmapEditorSelectDatatable("", 0, analysisPrefix);
        n.mapStainingEditorSelectSample("", columnName);
        n.mapStainingEditorSelectDatatable("", analysisPrefix);

        // We open the color editor
        n.continuousConfigOpen("",analysisPrefix,"color");
        // Define that the method for computing a value for multiple HUGOs is average
        n.continuousConfigSetSampleMethod("","color",analysisPrefix,0);
        // Modify the low threshold for the green part of the color scale
        n.continuousConfigSetValueAt("",analysisPrefix,"color","sample",0,-2f);
        // Modify the low threshold for the red part of the color scale
        n.continuousConfigSetValueAt("",analysisPrefix,"color","sample",2,2f);
        // We close the color editor and apply the changes
        n.continuousConfigApplyAndClose("", analysisPrefix, "color");
        
        
        // visualize the results
        //n.heatmapEditorApply("");
        n.mapStainingEditorApply("");
		
		
	}
	
	public static VDataTable takeOnlyTopContributingGenes(VDataTable vt, float threshold){
		vt.makePrimaryHash(vt.fieldNames[0]);
		HashSet<String> set = new HashSet<String>();
		for(int i=0;i<vt.rowCount;i++)for(int j=0;j<vt.colCount;j++)if(vt.fieldTypes[j]==vt.NUMERICAL){
			float val = Float.parseFloat(vt.stringTable[i][j]);
			if(Math.abs(val)>threshold)
				set.add(vt.stringTable[i][0]);
		}
		Vector<String> ids = new Vector<String>();
		for(String s: set) ids.add(s); 
		VDataTable vt1 = VSimpleProcedures.selectRowsFromList(vt, ids);
		return vt1;
	}

}
