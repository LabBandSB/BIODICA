package util;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import logic.Graph;
import logic.XGMML;
import model.BBHGraphDTO;
import model.ConfigDTO;
import model.IcaDTO;
import model.OftenDTO;
import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Utils;

public class HTMLGenerator 
{
	
	public static int[][] standard_colors = {{255,0,0},{0,255,0},{0,0,255},{255,255,0},{255,0,255},{0,100,255},{100,0,255},{100,255,0},{0,255,100},{255,100,0},{255,0,100}};
	
	public static void main(String args[]){
		
		try{
		//String res = MakeDrawTableFunction("drawTable","C:/Datas/BIODICA_GUI/work/OVCA_MSAMPLE/OVCA_A_associations_cat.xls","C:/Datas/BIODICA_GUI/work/OVCA_MSAMPLE/OVCA_A_associations_cat_info.xls","metasample_table",80,50);
		//System.out.println(res);
		//makeBoxPlotHtml("test_boxplot", "Association of IC1 with patient.neoplasm_histologic_grade", "C:/Datas/BIODICA_GUI/work/OVCA_MSAMPLE/OVCA_A_associations.data", "IC2", "IntrinsicExprClassJCI", "C:/Datas/BIODICA_GUI/bin/HTML/boxplot_template.html","C:/Datas/BIODICA_GUI/work/OVCA_MSAMPLE/boxplots/test_boxplot.html");
		
		//HashMap<String,Color> colormap = new HashMap<String,Color>(); 
		//colormap.put("FN1", Color.GREEN);
		//makeHTMLGraphFromXGMML("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_abs.xgmml","C:/Datas/BIODICA_GUI/bin/HTML/graph_template.html","graph_id","Graph title","Graph description",colormap);
		
		File files[] = new File[11];
		files[0] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/_OFTENreport.txt");
		files[1] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/_OFTENreportSummary.txt");
		files[2] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_abs.xgmml");
		files[3] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_minus.xgmml");
		files[4] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_plus.xgmml");
		files[5] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC3_abs.xgmml");
		files[6] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC3_minus.xgmml");
		files[7] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC3_plus.xgmml");
		files[8] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_abs_st.txt");
		files[9] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_minus_st.txt");
		files[10] = new File("C:/Datas/BIODICA_GUI/work/OVCA_OFTEN/OVCA_IC1_plus_st.txt");
		OftenDTO dto = new OftenDTO();
		dto.setSTablePath("C:/Datas/BIODICA_GUI/work/OVCA_ICA/OVCA_ica_S.xls");
		generateOFTENHtml("OFTEN","OFTEN",files,"OVCA",dto);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void generateICAHtml(String f1Url, String f2Url, File[] images, String prefix) throws IOException{
			
			String htmlDirectory = getHmtlDirectory(prefix);
		

		  	File f = new File(htmlDirectory + File.separator + "ICA.htm");

	        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	        
	        StringBuilder page = new StringBuilder();
	        
	        page.append("<html>").append(returnHeaderOpener());
	        
	        // Here goes all JavaScript
	        
	        page.append(returnHeaderCloser());
	        
	        // List (xls) Files
	        page.append("<div class='container'><h1>ICA's Computation for "+prefix+"</h1>"
	        		+ "<div class='panel panel-default'><div style='padding-top:10px'>"
	        		+ "<ul style='list-style-type:disc'><li>");
	        
	        page.append("<a href='"+f1Url+"'>"+new File(f1Url).getName()+"</a>"
	        		+ "</li><li><a href='"+f2Url+"'>"+new File(f2Url).getName()+"</a>"
	        		+ "</li></ul></div>");
	        
	        page.append("<div class='row' style='padding:20px'>");
			for (File image : images) 
			{
				page.append("<div class='col-xs-6 col-md-6'><a href='#' class='thumbnail'><img src='"
						+ image.getAbsolutePath() + "' height='550px' width='600px'></a></div>");
			}

			page.append("</div></div></div></body>");
	        page.append("</html>");	        	             
	        bw.write(page.toString());
	        bw.close();
	        try{
	        	Desktop.getDesktop().browse(f.toURI());
			}catch(Exception e){
					System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
			}

	}
	
	private static String getHmtlDirectory(String prefix)
	{
		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		
		File htmlDir = new File(cfDTO.getDefaultWorkFolderPath() + File.separator + prefix+"_HTML");
		
		if (!htmlDir.exists()){
			htmlDir.mkdir();
		}
		
		return htmlDir.getAbsolutePath();
		
	}
	
	public static void generateMethodHtml(String name, String fileName, File[] files, String prefix) throws IOException{
		String htmlDirectory = getHmtlDirectory(prefix);
		
		File f = new File(htmlDirectory + File.separator + fileName + ".htm");

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        
        StringBuilder page = new StringBuilder();
        
        page.append("<html>").append(returnHeader());

        page.append("<div class='container'><h1>" +  name + "</h1>"
        		+ "<div class='panel panel-default'><div style='padding-top:10px'>"
        		+ "<ul style='list-style-type:disc'>");
        
        for (File file : files) 
		{
        	page.append("<li><a href='" + file.getAbsolutePath() + "'>" +  file.getName() + "</a></li>");
		}
        
		page.append("</ul></div></div></div></div></body>");
        
        
        page.append("</html>");	        	             
        bw.write(page.toString());
        bw.close();
        try{
        	Desktop.getDesktop().browse(f.toURI());
		}catch(Exception e){
			System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
		}
        	
	}
	
	public static void makeBoxPlotHtml(String chartID, String chartTitle, String dataFile, String componentName, String categoryName, String templateFileName, String outFile) throws Exception{
		FileWriter fw = new FileWriter(outFile);
		String template = Utils.loadString(templateFileName);
		template = Utils.replaceString(template, "$CHART_ID$", chartID);
		template = Utils.replaceString(template, "$CHART_TITLE$", chartTitle);

		String xdata = "";
		String ydata = "";
		String colors = "";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(dataFile, false, "\t");

		int count_color=0;
		for(int i=0;i<vt.rowCount;i++){
			String component = vt.stringTable[i][0];
			String category = vt.stringTable[i][1];
			String cat_value = vt.stringTable[i][2];
			String datas = vt.stringTable[i][3];
			if(component.equals(componentName)&&(category.equals(categoryName))){
				xdata+="'"+cat_value+"',";
				ydata+=datas+",\n";
				colors += "'rgba("+standard_colors[count_color][0]+","+standard_colors[count_color][1]+","+standard_colors[count_color][2]+",1)',";
				count_color++;
				if(count_color>standard_colors.length-1) count_color=0;
			}
		}
		if(xdata.length()>0) xdata = xdata.substring(0, xdata.length()-1);
		if(ydata.length()>0) ydata = ydata.substring(0, ydata.length()-2);
		if(colors.length()>0) colors = colors.substring(0, colors.length()-1);

		template = Utils.replaceString(template, "$XDATA$", xdata);
		template = Utils.replaceString(template, "$YDATA$", ydata);
		template = Utils.replaceString(template, "$COLORS$", colors);
		
		fw.write(template);
		fw.close();
	}

	public static void makeScatterPlotHtml(String chartID, String chartTitle, String dataFile, String componentName, String categoryName, String templateFileName, String outFile) throws Exception{
		FileWriter fw = new FileWriter(outFile);
		String template = Utils.loadString(templateFileName);
		template = Utils.replaceString(template, "$CHART_ID$", chartID);
		template = Utils.replaceString(template, "$CHART_TITLE$", chartTitle);
		template = Utils.replaceString(template, "$X_AXIS_LABEL$", componentName);
		template = Utils.replaceString(template, "$Y_AXIS_LABEL$", categoryName);
		
		
		String xdata = "";
		String ydata = "";
		String names = "";
		
		if(new File(dataFile).exists()){
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(dataFile, false, "\t");
		
		if(vt.colCount>0)
		for(int i=0;i<vt.rowCount;i++){
			String component = vt.stringTable[i][0];
			String category = vt.stringTable[i][1];
			String cat_value = vt.stringTable[i][2];
			String datas = vt.stringTable[i][3];
			if(component.equals(componentName)&&(category.equals(categoryName))){
				if(cat_value.equals("xvalues")) xdata = datas;
				if(cat_value.equals("yvalues")) ydata = datas;
				if(cat_value.equals("names")) names = datas;
			}
		}
		
		}
		
		template = Utils.replaceString(template, "$XDATA$", xdata);
		template = Utils.replaceString(template, "$YDATA$", ydata);
		template = Utils.replaceString(template, "$NAMES$", names);
		
		fw.write(template);
		fw.close();

	}
	
	
	public static void generateMetaSampleHtml(String name, String fileName, File[] files, String prefix, String dataCategoricalFile, String dataNumericalFile) throws Exception{
		   
		String htmlDirectory = getHmtlDirectory(prefix);
		
		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		String html = cfDTO.getHTMLSourcePath();
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"toolTip.css"), new File(htmlDirectory+System.getProperty("file.separator")+"toolTip.css"));

		double thresholdView = cfDTO.getAssociationAnalysisInThreshold();
		double thresholdPlot = cfDTO.getSignificanceThresholdForShowingPlots();
		double thresholdCorrelationView = cfDTO.getAssociationCorrelationThreshold();
		double thresholdCorrelationPlot = cfDTO.getCorrelationThresholdForShowingPlots();
		
		File boxplotsFolder = new File(htmlDirectory + File.separator + "boxplots_ms" + File.separator);
		boxplotsFolder.mkdir();
		File scatterPlotsFolder = new File(htmlDirectory + File.separator + "scatters_ms" + File.separator);
		scatterPlotsFolder.mkdir();
		
		File f = new File(htmlDirectory + File.separator + fileName + ".htm");

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        
        StringBuilder page = new StringBuilder();
        
        page.append("<html>").append(returnHeaderOpener());
        
        page.append(returnGoogleScriptOpener());

        String tableScores_cat = null;
        String tableScoresInfo = null;
        
        // Table for categorical analysis (t-tests)
        
        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("cat.xls")) tableScores_cat = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("cat_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }
        if(tableScores_cat!=null){
        	String res = MakeDrawTableFunction("drawTableCategory",prefix,true,dataCategoricalFile,tableScores_cat,tableScoresInfo,"table_category",80,50,thresholdView,thresholdPlot,"boxplots_ms","scatters_ms");
        	page.append(res);
		}
	
        String tableScores_num = null;
        // Table for numerical analysis (correlations)
        
        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("num.xls")) tableScores_num = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("num_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }

        if(tableScores_num!=null){
        	String res = MakeDrawTableFunction("drawTableNumerical",prefix,false,dataNumericalFile,tableScoresInfo,tableScores_num,"table_numerical",80,50,thresholdCorrelationView,thresholdCorrelationPlot,"boxplots_ms","scatters_ms");
        	page.append(res);
        }
        
        String tableScores_self = null;
        // Table for self-analysis (correlations)

        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("slf.xls")) tableScores_self = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("slf_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }
        
        if(tableScores_self!=null){
        	String res = MakeDrawTableFunction("drawTableSelf",prefix,false,dataNumericalFile,tableScoresInfo,tableScores_self,"table_self",80,50,thresholdCorrelationView,thresholdCorrelationPlot,"boxplots_ms","scatters_ms");
        	page.append(res);
        }	
        	
        page.append(returnGoogleScriptCloser());
        
        page.append(returnHeaderCloser());
        
        page.append("<div class='container'><h1>" +  name + "</h1>"
        		+ "<div class='panel panel-default'><div style='padding-top:10px'>"
        		+ "<ul style='list-style-type:disc'>");
        
        for (File file : files) 
		{
        	page.append("<li><a href='" + file.getAbsolutePath() + "'>" +  file.getName() + "</a></li>");
		}
        
		page.append("</ul></div></div></div></div>");

		// Now show the three tables
		
		if(tableScores_cat!=null){
		page.append("<div><h3>Table of IC associations with categorical variables.</h3></div>");
		page.append("<div>The values are -log10 p-values of the maximal t-test between any pairs of categories.</div>");
		page.append("<div>The tooltip for each number shows information on the score in the format &lt;cat1&gt;/&lt;cat2&gt;(&lt;actual t-test score&gt;), where cat1 and cat2 are two categories between which the most significant difference is observed.</div>");
		page.append("<div id='table_category'></div>");
		}
		if(tableScores_num!=null){
		page.append("<div><h3>Table of IC associations with numerical variables.</h3></div>");
		page.append("<div>The values are Spearman correlation between a pair of a variable and an IC metasample.</div>");
		page.append("<div>The tooltip for each number shows the -log10 p-values of the correlation value significance.</div>");
		page.append("<div id='table_numerical'></div>");
		}
		if(tableScores_self!=null){
		page.append("<div><h3>Table of IC associations with themselves.</h3></div>");
		page.append("<div>The values are Spearman correlation between a pair IC metasamples.</div>");
		page.append("<div id='table_self'></div>");
		}
		
		page.append("</body>");
        page.append("</html>");	        	             
        bw.write(page.toString());
        bw.close();
        try{
        	Desktop.getDesktop().browse(f.toURI());
		}catch(Exception e){
			System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
		}
        
	}
	
	
	public static void generateMetaGeneHtml(String name, String fileName, File[] files, String prefix, String dataCategoricalFile, String dataNumericalFile) throws Exception{
		   
		String htmlDirectory = getHmtlDirectory(prefix);
		
		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		String html = cfDTO.getHTMLSourcePath();
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"toolTip.css"), new File(htmlDirectory+System.getProperty("file.separator")+"toolTip.css"));

		double thresholdView = cfDTO.getAssociationAnalysisInThreshold();
		double thresholdPlot = cfDTO.getSignificanceThresholdForShowingPlots();
		double thresholdCorrelationView = cfDTO.getAssociationCorrelationThreshold();
		double thresholdCorrelationPlot = cfDTO.getCorrelationThresholdForShowingPlots();
		
		File boxplotsFolder = new File(htmlDirectory + File.separator + "boxplots_mg" + File.separator);
		boxplotsFolder.mkdir();
		File scatterPlotsFolder = new File(htmlDirectory + File.separator + "scatters_mg" + File.separator);
		scatterPlotsFolder.mkdir();
		
		File f = new File(htmlDirectory + File.separator + fileName + ".htm");

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        
        StringBuilder page = new StringBuilder();
        
        page.append("<html>").append(returnHeaderOpener());
        
        page.append(returnGoogleScriptOpener());

        String tableScores_cat = null;
        String tableScoresInfo = null;
        
        // Table for categorical analysis (t-tests)
        
        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("cat.xls")) tableScores_cat = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("cat_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }
        if(tableScores_cat!=null){
        	String res = MakeDrawTableFunction("drawTableCategory",prefix,true,dataCategoricalFile,tableScores_cat,tableScoresInfo,"table_category",80,50,thresholdView,thresholdPlot,"boxplots_mg","scatters_mg");
        	page.append(res);
        }

        String tableScores_num = null;
        // Table for numerical analysis (correlations)
        
        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("num.xls")) tableScores_num = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("num_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }
        if(tableScores_num!=null){
        	String res = MakeDrawTableFunction("drawTableNumerical",prefix,false,dataNumericalFile,tableScoresInfo,tableScores_num,"table_numerical",80,50,thresholdCorrelationView,thresholdCorrelationPlot,"boxplots_mg","scatters_mg");
        	page.append(res);
        }

        String tableScores_rnk = null;
        // Table for correlations with previously defined metagenes

        for(File xls: files)if(xls.getName().startsWith(prefix)){ 
        	if(xls.getName().endsWith("RNK.xls")) tableScores_rnk = xls.getAbsolutePath(); 
        	if(xls.getName().endsWith("RNK_info.xls")) tableScoresInfo = xls.getAbsolutePath();
        }
        if(tableScores_rnk!=null){
        	String res = MakeDrawTableFunction("drawTableRNK",prefix,false,dataNumericalFile,tableScoresInfo,tableScores_rnk,"table_rnk",80,150,thresholdCorrelationView,thresholdCorrelationPlot,"boxplots_mg","scatters_mg");
        	page.append(res);
        }
        page.append(returnGoogleScriptCloser());
        
        page.append(returnHeaderCloser());
        
        page.append("<div class='container'><h1>" +  name + "</h1>"
        		+ "<div class='panel panel-default'><div style='padding-top:10px'>"
        		+ "<ul style='list-style-type:disc'>");
        
        for (File file : files) 
		{
        	page.append("<li><a href='" + file.getAbsolutePath() + "'>" +  file.getName() + "</a></li>");
		}
        
		page.append("</ul></div></div></div></div>");

		// Now show the three tables
		if(tableScores_cat!=null){
		page.append("<div><h3>Table of IC associations with categorical gene description.</h3></div>\n");
		page.append("<div>The values are -log10 p-values of the maximal t-test between any pairs of categories.</div>\n");
		page.append("<div>The tooltip for each number shows information on the score in the format &lt;cat1&gt;/&lt;cat2&gt;(&lt;actual t-test score&gt;), where cat1 and cat2 are two categories between which the most significant difference is observed.</div>\n");
		page.append("<div id='table_category'></div>\n");
		}
		if(tableScores_num!=null){
		page.append("<div><h3>Table of IC associations with numerical features of genes.</h3></div>\n");
		page.append("<div>The values are Spearman correlations between a pair of a variable and an IC metagene.</div>\n");
		page.append("<div id='table_numerical'></div>");
		}
		if(tableScores_rnk!=null){
		page.append("<div><h3>Table of IC associations with previously defined metagenes.</h3></div>\n");
		page.append("<div>The values are Spearman correlations between an IC metagene and a previously identified metagene.</div>\n");
		page.append("<div id='table_rnk'></div>\n");
		}
		
		page.append("</body>");
        page.append("</html>");	        	             
        bw.write(page.toString());
        bw.close();
        try{
        	Desktop.getDesktop().browse(f.toURI());
		}catch(Exception e){
			System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
		}
        
	}
	
	private static String returnHeaderOpener(){
		return "<head><title>BiODICA</title><meta charset='utf-8'><meta name='viewport' content='width=device-width, "
				+ "initial-scale=1'><link rel='stylesheet' "
				+ "href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>"
				+ "<link rel='stylesheet' href='toolTip.css'>";
	}
	
	private static String returnGoogleScriptOpener(){
		return "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n"
				+ "<script type=\"text/javascript\">\n"
				+ "google.charts.load('current', {'packages':['table']});\n";
	}
	
	private static String returnGoogleScriptCloser(){
		return "</script>\n";
	}
	
	private static String returnHeaderCloser(){
		return "</head>";
	}
	
	private static String returnHeader(){
		return returnHeaderOpener()+returnHeaderCloser();
	}
	
	public static String MakeDrawTableFunction(String functionName, String prefix, boolean categorical, String dataFile, String textTableScores, String textTableInfos, String id, int width, int height, double thresholdView, double thresholdPlot, String boxplotFolder, String scatterFolder) throws Exception{
		String res = "";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(textTableScores, true, "\t");
		
		VDataTable vt_info = null;
		
		if(textTableInfos!=null)
			if(new File(textTableInfos).exists()) 
				vt_info = VDatReadWrite.LoadFromSimpleDatFile(textTableInfos, true, "\t");
		
		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		
		TableUtils.findAllNumericalColumns(vt);
		
		res+="\n;google.charts.setOnLoadCallback("+functionName+");\n\n";
		
		res+="function "+functionName+"() {\n";
		res+="var data = new google.visualization.DataTable();\n";
		for(int i=0;i<vt.colCount;i++)
			if(vt.fieldTypes[i]==vt.NUMERICAL)
				res+="data.addColumn('number', '"+vt.fieldNames[i]+"');\n";
			else
				res+="data.addColumn('string', '"+vt.fieldNames[i]+"');\n";
		res+="data.addRows([\n";
		for(int j=0;j<vt.rowCount;j++){
			res+="[";
			for(int i=0;i<vt.colCount;i++){
				if(vt.fieldTypes[i]==vt.NUMERICAL){

					String htmlDirectory = getHmtlDirectory(prefix);
					String component = vt.fieldNames[i];
					String category = vt.stringTable[j][0];
					
					if(categorical){

						String link = boxplotFolder+"/"+component+"_"+j+".html";
						double val = Double.parseDouble(vt.stringTable[j][i]);
						String tooltip = "";
						if(vt_info!=null)
							tooltip = "tooltip=\""+vt_info.stringTable[j][i]+"\"";
						if(val>thresholdView){
							if(val>thresholdPlot){
								String boxPlotTemplateFile = cfDTO.getHTMLSourcePath()+File.separator+"boxplot_template.html";
								makeBoxPlotHtml("boxplot"+component+"_"+j, "Association of "+component+" with "+category, dataFile, component, category, boxPlotTemplateFile,htmlDirectory+File.separator+boxplotFolder+File.separator+component+"_"+j+".html");
								res+="{v:"+vt.stringTable[j][i]+",f:'<font size=+1><a href=\""+link+"\" target=\"_blank\" "+tooltip+">"+vt.stringTable[j][i]+"</a></font>'}";
							}else
								res+="{v:"+vt.stringTable[j][i]+",f:'<span "+tooltip+">"+vt.stringTable[j][i]+"</span>'}";
						}else{
							res+="{v:"+vt.stringTable[j][i]+",f:''}";
						}
						
					}else{ // numerical
						
						String link = scatterFolder+"/"+component+"_"+j+".html";
						double val = Double.parseDouble(vt.stringTable[j][i]);
						String tooltip = "";
						if(vt_info!=null)
							tooltip = "tooltip=\""+vt_info.stringTable[j][i]+"\"";
						if(val>thresholdView){
							if(val>thresholdPlot){
								String scatterPlotTemplateFile = cfDTO.getHTMLSourcePath()+File.separator+"scatter_template.html";
								makeScatterPlotHtml("scatter"+component+"_"+j, "Association of "+component+" with "+category, dataFile, component, category, scatterPlotTemplateFile,htmlDirectory+File.separator+scatterFolder+File.separator+component+"_"+j+".html");
								res+="{v:"+vt.stringTable[j][i]+",f:'<font size=+1><a href=\""+link+"\" target=\"_blank\" "+tooltip+">"+vt.stringTable[j][i]+"</a></font>'}";
							}else
								res+="{v:"+vt.stringTable[j][i]+",f:'<span "+tooltip+">"+vt.stringTable[j][i]+"</span>'}";
						}else{
							res+="{v:"+vt.stringTable[j][i]+",f:''}";
						}
						
						//res+="{v:"+vt.stringTable[j][i]+",f:'<a tooltip=\""+vt_info.stringTable[j][i]+"\">"+vt.stringTable[j][i]+"</a>'}";
					}
				
				}else
					res+="'"+vt.stringTable[j][i]+"'";
				if(i<vt.colCount-1)
					res+=",";
			}
			if(j==vt.rowCount-1)
				res+="]\n";
			else
				res+="],\n";
		}
		res+="]);\n";
		
		res+="var table = new google.visualization.Table(document.getElementById('"+id+"'));\n";
		res+="table.draw(data, {showRowNumber: false, width: '"+width+"%', height: '"+height+"%', allowHtml: true});\n";
		
		res+="}\n";
		return res;
	}
	
	public static void makeHTMLGraphFromXGMML(String xgmmlFile, String outFile, String graph_template, String id, String title, String description, HashMap<String,Color> colorMap) throws Exception{
		Graph graph = XGMML.convertXGMMLToGraph(XGMML.loadFromXMGML(xgmmlFile));
		String template = Utils.loadString(graph_template);
		String fn = outFile;
		template = Utils.replaceString(template, "$TITLE$", title);
		template = Utils.replaceString(template, "$GRAPH_ID$", id);
		template = Utils.replaceString(template, "$DESCRIPTION$", description);
		String nodes = "";
		String edges = "";
		HashMap<String,Integer> map = new HashMap<String,Integer>(); 
		for(int i=0;i<graph.Nodes.size();i++){
			String scolor = "rgba(255,0,0,1)";
			if(colorMap!=null){
				String nodeName = graph.Nodes.get(i).Id;
				Color c = colorMap.get(nodeName);
				if(c!=null)
					scolor = "rgba("+c.getRed()+","+c.getGreen()+","+c.getBlue()+",1)";
			}
			nodes+="{id: "+(i+1)+", label:'"+graph.Nodes.get(i).Id+"', color: '"+scolor+"', shape: 'dot', font:{size:40}},\n";
			map.put(graph.Nodes.get(i).Id, (i+1));
		}
		for(int i=0;i<graph.Edges.size();i++){
		    edges+="{from: "+map.get(graph.Edges.get(i).Node1.Id)+", to: "+map.get(graph.Edges.get(i).Node2.Id)+", width: 10, color: 'black'},\n";
		}
		template = Utils.replaceString(template, "$NODES$", nodes);
		template = Utils.replaceString(template, "$EDGES$", edges);
		FileWriter fw = new FileWriter(fn);
		fw.write(template);
		fw.close();
	}


	public static void generateOFTENHtml(String name, String fileName, File[] files, String prefix, OftenDTO dto) throws Exception{
		String htmlDirectory = getHmtlDirectory(prefix);
		
		File f = new File(htmlDirectory + File.separator + fileName + ".htm");

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        
        StringBuilder page = new StringBuilder();
        
        page.append("<html>").append(returnHeader());

		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		String html = cfDTO.getHTMLSourcePath();
		String graph_template = html +System.getProperty("file.separator")+"graph_template.html";
		String scatter_template = html +System.getProperty("file.separator")+"scatter_template.html";
        
		File graphFolder = new File(htmlDirectory + File.separator + "graphs" + File.separator);
		graphFolder.mkdir();
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"vis.js"), new File(graphFolder+System.getProperty("file.separator")+"vis.js"));
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"vis-network.min.css"), new File(graphFolder+System.getProperty("file.separator")+"vis-network.min.css"));

		
		String oftenReport = "";
        for(File rep: files){ 
        	if(rep.getName().endsWith("_OFTENreport.txt")) oftenReport = rep.getAbsolutePath(); 
        }
        VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(oftenReport, true, "\t");
        TableUtils.findAllNumericalColumns(vt);

        VDataTable sTable = VDatReadWrite.LoadFromSimpleDatFile(dto.getSTablePath(),true,"\t");
        TableUtils.findAllNumericalColumns(sTable);
        
		page.append(returnGoogleScriptOpener());
		
		String res = "";
		
		res+="google.charts.setOnLoadCallback(often_table);\n\n";
		
		res+="function often_table() {\n";
		res+="var data = new google.visualization.DataTable();\n";
		for(int i=0;i<vt.colCount;i++)
			if(vt.fieldTypes[i]==vt.NUMERICAL)
				res+="data.addColumn('number', '"+vt.fieldNames[i]+"');\n";
			else
				res+="data.addColumn('string', '"+vt.fieldNames[i]+"');\n";
		res+="data.addRows([\n";
		for(int j=0;j<vt.rowCount;j++){
			res+="[";
			for(int i=0;i<vt.colCount;i++){
				if(vt.fieldTypes[i]==vt.NUMERICAL){
					//res+="{v:"+vt.stringTable[j][i]+",f:'<font size=+1><a href=\""+link+"\" target=\"_blank\" "+tooltip+">"+vt.stringTable[j][i]+"</a></font>'}";
					if(vt.fieldNames[i].endsWith("_N")){
						
						String ic = vt.stringTable[j][0];
						String tp = vt.fieldNames[i].substring(0, vt.fieldNames[i].length()-2).toLowerCase();
						String xgmmlFile = "";
						
				        for(File xgm: files){ 
				        	if(xgm.getName().startsWith(prefix+"_"+ic+"_"+tp+".xgmml")) 
				        		xgmmlFile = xgm.getAbsolutePath(); 
				        }
				        
				        HashMap<String,Color> colorMap = new HashMap<String,Color>();
				        float sum2 = 0f;
				        for(int kk=0;kk<sTable.rowCount;kk++){
				        	float val = Float.parseFloat(sTable.stringTable[kk][sTable.fieldNumByName(ic)]);
				        	sum2+=val*val;
				        }
				        sum2/=sTable.rowCount;
				        sum2 = (float)Math.sqrt(sum2);
				        float threshold = 10f;
				        for(int kk=0;kk<sTable.rowCount;kk++){
				        	float val = Float.parseFloat(sTable.stringTable[kk][sTable.fieldNumByName(ic)]);
				        	val = val/sum2;
				        	Color color = null;
				        	if(val>0){
				        		if(val>threshold) val=threshold;
				        		color = new Color((int)(val/threshold*255f),0,0);
				        	}else{
				        		if(val<-threshold) val=-threshold;
				        		color = new Color(0,(int)(-val/threshold*255f),0);
				        	}
				        	colorMap.put(sTable.stringTable[kk][0], color);
				        }
				        
						String fn = graphFolder+File.separator+"often_"+ic+"_"+tp+".html";
						String link = "graphs/often_"+ic+"_"+tp+".html";
				        if(!xgmmlFile.equals(""))
				        	makeHTMLGraphFromXGMML(xgmmlFile, fn, graph_template, "often_"+ic+"_"+tp, "Associating "+ic+"_"+tp+" with an interaction network", "Associating "+ic+"_"+tp+" with an interaction network", colorMap);
						

						res+="{v:"+vt.stringTable[j][i]+",f:'<font size=+1><a href=\""+link+"\" target=\"_blank\">"+vt.stringTable[j][i]+"</a></font>'}";
					}else if(vt.fieldNames[i].endsWith("_SC")){
						
						String ic = vt.stringTable[j][0];
						String tp = vt.fieldNames[i].substring(0, vt.fieldNames[i].length()-3).toLowerCase();
						String report = "";
						
				        for(File xgm: files){ 
				        	if(xgm.getName().startsWith(prefix+"_"+ic+"_"+tp+"_st.txt")) 
				        		report = xgm.getAbsolutePath(); 
				        }
				        
				        if(!report.equals("")){
				        VDataTable vr = VDatReadWrite.LoadFromSimpleDatFile(report, true, "\t");
						String xdata = "";
						String ydata = "";
						String names = "";
						
						for(int kk=0;kk<vr.rowCount;kk++){
							xdata+=vr.stringTable[kk][vr.fieldNumByName("NGENES")]+",";
							ydata+=vr.stringTable[kk][vr.fieldNumByName("SCORE")]+",";
							names+="'selected="+vr.stringTable[kk][vr.fieldNumByName("SELECTED")]+",lcc_size="+vr.stringTable[kk][vr.fieldNumByName("LARGESTCOMPONENT")]+",p-value="+vr.stringTable[kk][vr.fieldNumByName("SIGNIFICANCE")]+"',";
						}
						if(xdata.length()>0) xdata = xdata.substring(0, xdata.length()-1);
						if(ydata.length()>0) ydata = ydata.substring(0, ydata.length()-1);
						if(names.length()>0) names = names.substring(0, names.length()-1);
				        xdata = "["+xdata+"]";
				        ydata = "["+ydata+"]";
				        names = "["+names+"]";
				        
				        String outFile = htmlDirectory + File.separator + "graphs"+ File.separator +"oftenscore_"+prefix+"_"+ic+"_"+tp + ".html";
				        String link = "graphs/oftenscore_"+prefix+"_"+ic+"_"+tp+".html";
						FileWriter fw = new FileWriter(outFile);
						String template = Utils.loadString(scatter_template);
						template = Utils.replaceString(template, "$CHART_ID$", prefix+"_"+ic+"_"+tp);
						template = Utils.replaceString(template, "$CHART_TITLE$", "OFTEN analysis for "+prefix+", "+ic+"_"+tp);
						template = Utils.replaceString(template, "$X_AXIS_LABEL$", "Number of top genes");
						template = Utils.replaceString(template, "$Y_AXIS_LABEL$", "OFTEN score");
						template = Utils.replaceString(template, "'scatter'", "'line'");
						template = Utils.replaceString(template, "mode: 'markers'", "mode: 'line'");
						template = Utils.replaceString(template, "$XDATA$", xdata);
						template = Utils.replaceString(template, "$YDATA$", ydata);
						template = Utils.replaceString(template, "$NAMES$", names);
						fw.write(template);
						fw.close();
						
						res+="{v:"+vt.stringTable[j][i]+",f:'<font size=+1><a href=\""+link+"\" target=\"_blank\">"+vt.stringTable[j][i]+"</a></font>'}";
				        }else
				        	res+=""+vt.stringTable[j][i]+"";
					}else
					res+=""+vt.stringTable[j][i]+"";
				}else
					res+="'"+vt.stringTable[j][i]+"'";
				
				if(i<vt.colCount-1)
					res+=",";
			}
			if(j==vt.rowCount-1)
				res+="]\n";
			else
				res+="],\n";
		}
		res+="]);\n";
		
		res+="var table = new google.visualization.Table(document.getElementById('table_often'));\n";
		res+="table.draw(data, {showRowNumber: false, width: '90%', height: '70%', allowHtml: true});\n";
		
		res+="}\n";
		
		page.append(res);
		
		 page.append(returnGoogleScriptCloser());

			page.append("<div><h3>Table of IC associations with protein-protein subnetworks.</h3></div>\n");
			page.append("<div>Clickable numbers are connected to the associated subnetworks.</div><br><br>\n");
			page.append("<div id='table_often'></div>\n");		 
		 
        page.append("<br><br><br><br><div class='container'><h3>Files:</h3>"
        		+ "<div class='panel panel-default'><div style='padding-top:10px'>"
        		+ "<ul style='list-style-type:disc'>");
        
        for (File file : files) 
		{
        	page.append("<li><a href='" + file.getAbsolutePath() + "'>" +  file.getName() + "</a></li>");
		}
        
		page.append("</ul></div></div></div></div>");
        
        page.append("</body></html>");	        	             
        bw.write(page.toString());
        bw.close();
        try{
        	Desktop.getDesktop().browse(f.toURI());
		}catch(Exception e){
			System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
		}
        
	}
	
	public static void generateBBHGraphHtml(String name, String fileName, String file, BBHGraphDTO bBHGraphDTO) throws Exception{

		ConfigHelper cfHelper = new ConfigHelper();
		ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		String html = cfDTO.getHTMLSourcePath();
		String graph_template = html +System.getProperty("file.separator")+"graph_template.html";

		String gfolder = (new File(file)).getParentFile().getAbsolutePath();
		String fn = gfolder+File.separator+fileName+".html";
		
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"vis.js"), new File(gfolder+System.getProperty("file.separator")+"vis.js"));
		FileUtils.copyFile(new File(html +System.getProperty("file.separator")+"vis-network.min.css"), new File(gfolder+System.getProperty("file.separator")+"vis-network.min.css"));
		
		makeHTMLGraphFromXGMML(file, fn, graph_template, "rbhgraph", "RBH Graph constructed for "+file, "RBH Graph constructed for "+file, null);
		File f = new File(fn);
		try{
        	Desktop.getDesktop().browse(f.toURI());
		}catch(Exception e){
			System.out.println("Opening an HTML page "+f.toURI()+" seems not possible (no browser available?)");
		}
        
	}

	
}
