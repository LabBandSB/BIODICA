package logic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import vdaoengine.TableUtils;
import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;

public class UseTOPPgeneSuiteForEnrichment {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static int thresholdNumberOfGenesInEnrichment = 8;
	public static float thresholdQValue = 0.05f;
	
	public static boolean stop_execution = false;

	
	public static void main(String[] args) {
		
		try{
			
			//String annotation = produceAnnotationFromCompleteTable("C:/Datas/BIODICA/work/BRCA_cafs_S1S4diff_log10_TOPPGENE/IC1plus.html.txt");
			//System.exit(0);
			
			//String fn = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/Rescue_ica_S.xls";
			//String fn = "C:/Datas/BIODICA/work/BRCA_cafs_S1S4diff_log10_ICA/BRCA_cafs_S1S4diff_log10_ica_S.xls"; 
			//completeAnalysisOfMetaGeneFile(fn, "CAF BRCA", "C:/Datas/BIODICA/work/BRCA_cafs_S1S4diff_log10_TOPPGENE/", 2.5f, 10, true);
			
			//String fn = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/Rescue_ica_S.xls";
			//completeAnalysisOfMetaGeneFile(fn, "ASP14 Inducible", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/MOSAIC_ASP14/toppgene_thresh3/", 3f, 10, false);
			
			//convertUPDNGMT2TableOfMetaGenes("C:/Datas/ColonCancer/analysis/toppgene_topcontributed/top_ranked_genes.gmt");
			//convertUPDNGMT2TableOfMetaGenes("C:/Datas/Googlomics/GRN_project/work/topRankGenes_weighted_bothnetworks.gmt");
			//System.exit(0);
			
			//String fn = "C:/Datas/MOSAIC/analysis/ica/metaanalysis/aggregated_data_analysis/EWINGSC_ul_ica_S.xls"; 
			//completeAnalysisOfMetaGeneFile(fn, "EWING SCALL", "C:/Datas/MOSAIC/analysis/ica/metaanalysis/aggregated_data_analysis/toppgene_thresh3/", 3f, 10, true);

			//String fn = "C:/Datas/ColonCancer/analysis/toppgene_topcontributed/top_ranked_genes.gmt.xls"; 
			//completeAnalysisOfMetaGeneFile(fn, "CRC BBH Community", "C:/Datas/ColonCancer/analysis/toppgene_topcontributed/toppgene/", 3f, 10, false);
			
			//String fn = "C:/Datas/Googlomics/GRN_project/work/topRankGenes_weighted.gmt.xls"; 
			//completeAnalysisOfMetaGeneFile(fn, "Page/Chei Rank Ratio in cancer vs normal network", "C:/Datas/Googlomics/GRN_project/work/toppgene/", 3f, 10, false);

			
			//String fn = "C:/Datas/Googlomics/GRN_project/work/comparison_to_other_centrality/COMPARISON_TOPPGENE_IN.txt"; 
			//completeAnalysisOfMetaGeneFile(fn, "Comparing various biological enrichments for different centralities", "C:/Datas/Googlomics/GRN_project/work/comparison_to_other_centrality/toppgene_in/", 3f, 10, true);
			
			//String fn = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/_communities_HUGO_table_headnames.xls";
			//thresholdNumberOfGenesInEnrichment = 5;
			//thresholdQValue = 1e-8f;
			//completeAnalysisOfMetaGeneFile(fn, "Wikipedia protein communities, toppgene analysis", "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/toppgene/", 0.5f, 5, false);

			String fn = "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/stability_anaysis/largest_cluster_s.xls";
			thresholdNumberOfGenesInEnrichment = 5;
			thresholdQValue = 1e-8f;
			completeAnalysisOfMetaGeneFile(fn, "Wikipedia protein communities, toppgene analysis", "C:/Datas/Googlomics/WikiProteins/WorkOnArticle/data/2017/protein_graph/final/communities/stability_anaysis/toppgene/", 0.5f, 5, true);

			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void produceToppGeneReportInFile(Vector<String> geneList, String analysisName, String fileName, boolean downloadCompleteTable) throws Exception{
		StringBuffer sball = new StringBuffer();
		String out = produceToppGeneReport(geneList,analysisName,downloadCompleteTable,sball);
		FileWriter fw = new FileWriter(fileName);
		fw.write(out);
		fw.close();
		if(downloadCompleteTable){
		fw = new FileWriter(fileName+".txt");
		fw.write(sball.toString());
		fw.close();
		}
		
	}
	
	public static String produceToppGeneReport(Vector<String> geneList, String analysisName, boolean downloadCompleteTable, StringBuffer sball) throws Exception{
		String res = "";

		UseTOPPgeneSuiteForEnrichment http = new UseTOPPgeneSuiteForEnrichment();
		
		String out = http.sendPostToppGene(geneList);
		String userid = extractUserID(out);
		System.out.println("User_id="+userid);
		
		String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess","GeneOntologyCellularComponent","HumanPheno","MousePheno","Domain","Pathway","Pubmed","Interaction","Cytoband","TFBS","GeneFamily","Coexpression","CoexpressionAtlas","Computational","MicroRNA","Drug","Disease"};
		//String categories[] = {"GeneOntologyMolecularFunction","GeneOntologyBiologicalProcess"};
		//String categories[] = {"GeneOntologyMolecularFunction"};
		
		String out1 = http.sendPostToppGeneSubmitAction(userid,categories);
		Thread.sleep(20000);
		String out2 = "";
		while(true){
			out2 = vdaoengine.utils.VDownloader.downloadURL("https://toppgene.cchmc.org/output.jsp?userdata_id="+userid);
			if(out2.length()>3000) break;
			Thread.sleep(5000);
		}
		
		//System.out.println("Done.");
		
		out2 = Utils.replaceString(out2, "href=\"styles.css", "href=\"toppgene/styles.css");
		out2 = Utils.replaceString(out2, "src=\"output.js", "src=\"toppgene/output.js");
		out2 = Utils.replaceString(out2, "href='/download.jsp", "href='https://toppgene.cchmc.org/download.jsp");
		out2 = Utils.replaceString(out2, "href='/display.jsp", "href='https://toppgene.cchmc.org/display.jsp");
		out2 = Utils.replaceString(out2, "href=\"ResultsMatrix", "href=\"https://toppgene.cchmc.org/ResultsMatrix");
		out2 = Utils.replaceString(out2, "href=\"/showTermDetail.jsp", "href=\"https://toppgene.cchmc.org/showTermDetail.jsp");
		out2 = Utils.replaceString(out2, "href='/showQueryTerms.jsp", "href='https://toppgene.cchmc.org/showQueryTerms.jsp");
		
		out2 = Utils.replaceString(out2, ">Results<", ">"+analysisName+"<");
		
		res = out2;
		
		if(downloadCompleteTable){
		String allurl = "https://toppgene.cchmc.org/download.jsp?userdata_id="+userid+"&_formatting=SCI4";
		String completeTable = "";
		do{
		System.out.println("Downloading "+allurl+"...");
		completeTable = vdaoengine.utils.VDownloader.downloadURL(allurl);
		}while(completeTable.length()<200);
		sball.append(completeTable);
		System.out.println("Downloaded.");
		}

		
		return res;
	}
	
	
	private String sendPostToppGene(Vector<String> geneList) throws Exception {

		//String url = "https://toppgene.cchmc.org/enrichment.jsp";
		String url = "https://toppgene.cchmc.org/CheckInput.action";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		
		HashMap<String,String> params = new HashMap<String,String>();
		
		String list = "";
		for(String s:geneList) list+=s+"\n";
		
		params.put("training_set", list);
		params.put("query", "TOPPFUN");
		params.put("type", "HGNC");
		
		String urlParameters = getQuery(params);
		
		//System.out.println(urlParameters);
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String res = response.toString();
		return res;
		//print result
		//System.out.println();

	}
	
	private String sendPostToppGeneSubmitAction(String userid, String categories[]) throws Exception {

		//String url = "https://toppgene.cchmc.org/enrichment.jsp";
		String url = "https://toppgene.cchmc.org/Submit.action";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		
		HashMap<String,String> params = new HashMap<String,String>();
		
		Vector<String> names = new Vector<String>();
		Vector<String> values = new Vector<String>();
		
		//params.put("userdata_id", userid);
		names.add("userdata_id"); values.add(userid);
		
		params.put("all_min", "1");
		names.add("all_min"); values.add("1");
		params.put("all_max", "500");
		names.add("all_max"); values.add("500");
		params.put("all_correction", "FDR");
		names.add("all_correction"); values.add("FDR");
		params.put("all_p_value", "0.05");
		names.add("all_p_value"); values.add("0.05");
		
		params.put("sample_size", "0");
		names.add("sample_size"); values.add("0");
		params.put("min_feature_count", "2");
		names.add("min_feature_count"); values.add("2");
		
		for(String cat:categories){
			//System.out.println(cat);
		//params.put("category", cat);
			names.add("category"); values.add(cat);
		//params.put(cat+"_min", "1");
			names.add(cat+"_min"); values.add("1");
		//params.put(cat+"_max", "500");
			names.add(cat+"_max"); values.add("500");
		//params.put(cat+"_correction", "FDR");
			names.add(cat+"_correction"); values.add("FDR");
		//params.put(cat+"_p_value", "0.05");
			names.add(cat+"_p_value"); values.add("0.05");
		
		}
		//params.put("query", "TOPPFUN");
		//params.put("type", "HGNC");
		
		String urlParameters = getQueryV(names,values);
		
		//System.out.println(urlParameters);
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String res = response.toString();
		return res;
		//print result
		//System.out.println();

	}
	
	
	
	private void sendPostExample() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());

	}
	
	private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	    
	    Set<String> keys = params.keySet();

	    for (String key : keys)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(key, "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(params.get(key), "UTF-8"));
	    }

	    return result.toString();
	}
	
	private String getQueryV(Vector<String> names,Vector<String> values) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;
	    
	    for (int i=0;i<names.size();i++)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(names.get(i), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(values.get(i), "UTF-8"));
	    }

	    return result.toString();
	}
	
	
	public static String extractUserID(String s){
		String id = "none";
		StringTokenizer st = new StringTokenizer(s,"\" ");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.equals("userdata_id")){
				st.nextToken();
				id = st.nextToken();
			}
		}
		return id;
	}
	
	public static void completeAnalysisOfMetaGeneFile(String sfile, String analysisName, String outPutFolder, float threshold, int minNumberGenes, boolean useInternet) throws Exception{
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(sfile, true, "\t");
		TableUtils.findAllNumericalColumns(vt);
		int numNumerical = 0;
		for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL) numNumerical++;
		System.out.println(numNumerical+" features found.");
		
		FileWriter fw = new FileWriter(outPutFolder+"toppgeneReport.html");
		fw.write("<html><body>\n");
		fw.write("<h1>ToppGene analysis for "+analysisName+"</h1><br><br><br>\n");
		
		fw.write("<table width=100% border=1>\n");
		
		fw.write("<tr><td width=10%>POSITIVE SIDE</td>\n");fw.write("<td width=30%>ANNOTATION</td>\n");
		fw.write("<td width=1%>*<br>*</td>\n");
		fw.write("<td width=10%>NEGATIVE SIDE</td>\n");fw.write("<td width=30%>ANNOTATION</td></tr>\n");
		
		for(int i=0;i<vt.colCount;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
			if(stop_execution) break;
			fw.write("<tr>\n");			
			String colName = vt.fieldNames[i];
			//int kk = colName.indexOf("(");
			//String colName1 = colName.substring(0, kk);
			String colName1 = colName;
			
			Vector<String> listpositive = getTopContributingGenes(vt,i,1,threshold);
			//Vector<String> listpositive = getTopContributingGenes(vt,i,1,158);
			String BestAnnotation = "none";
			if(listpositive.size()==0){
				fw.write("<td width=10%>---</td>\n");
				fw.write("<td width=30%>---</td>\n");
			}else{
			if(listpositive.size()>=minNumberGenes){
				fw.write("<td width=10%><a href=\""+colName1+"plus.html\">"+colName+"+ </a>(<a href=\""+colName1+"plus_genes.html\">"+listpositive.size()+" genes</a>)</td>\n");
				if(useInternet)
					produceToppGeneReportInFile(listpositive,"Toppgene analysis for "+analysisName+", "+colName+", positive side",outPutFolder+colName+"plus.html",false);
				String annot = produceAnnotationFromHTMLfile(outPutFolder+colName1+"plus.html");
				if(annot!=null) BestAnnotation = annot;
				fw.write("<td width=30%>"+BestAnnotation+"</td>\n");
				
			}else{
				fw.write("<td width=10%>"+colName+"+ (<a href=\""+colName1+"plus_genes.html\">"+listpositive.size()+" genes</a>)</td>\n");
				fw.write("<td width=30%>"+BestAnnotation+"</td>\n");
			}}
			FileWriter fwlist = new FileWriter(outPutFolder+colName1+"plus_genes.html");
			for(int k=0;k<listpositive.size();k++) fwlist.write("<a href=\"http://www.genecards.org/cgi-bin/carddisp.pl?gene="+listpositive.get(k)+"\">"+listpositive.get(k)+"</a><br>");
			fwlist.close();

			fw.write("<td width=1%>*<br>*</td>\n");
			
			Vector<String> listnegative = getTopContributingGenes(vt,i,-1,threshold);
			//Vector<String> listnegative = getTopContributingGenes(vt,i,-1,158);
			BestAnnotation = "none";
			if(listnegative.size()==0){
				fw.write("<td width=10%>---</td>\n");
				fw.write("<td width=30%>---</td>\n");
			}else{
			if(listnegative.size()>=minNumberGenes){
				fw.write("<td width=10%><a href=\""+colName1+"minus.html\">"+colName+"- </a>(<a href=\""+colName1+"minus_genes.html\">"+listnegative.size()+" genes</a>)</td>\n");
				if(useInternet)
					produceToppGeneReportInFile(listnegative,"Toppgene analysis for "+analysisName+", "+colName+", negative side",outPutFolder+colName+"minus.html",false);
				String annot = produceAnnotationFromHTMLfile(outPutFolder+colName1+"minus.html");
				if(annot!=null) BestAnnotation = annot;
				fw.write("<td width=30%>"+BestAnnotation+"</td>\n");
			}else{
				fw.write("<td width=10%>"+colName+"- (<a href=\""+colName1+"minus_genes.html\">"+listnegative.size()+" genes</a>)</td>\n");
				fw.write("<td width=30%>"+BestAnnotation+"</td>\n");
			}}
			fwlist = new FileWriter(outPutFolder+colName1+"minus_genes.html");
			for(int k=0;k<listnegative.size();k++) fwlist.write("<a href=\"http://www.genecards.org/cgi-bin/carddisp.pl?gene="+listnegative.get(k)+"\">"+listnegative.get(k)+"</a><br>");
			fwlist.close();
			
			
			fw.write("</tr>\n");
		}
		
		fw.write("</table>\n");
		fw.write("</body></html>\n");
		fw.close();
	}
	
	/*
	 * posnegall = 1 (positive tail), -1 (negative tail), 0 (both tails)
	 */
	public static Vector<String> getTopContributingGenes(VDataTable vt, int column, int posnegall, float threshold){
		Vector<String> res = new Vector<String>();
		Vector<Float> weights = new Vector<Float>();
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][column].equals("N/A"))if(!vt.stringTable[i][column].equals("@")){
			float f = Float.parseFloat(vt.stringTable[i][column]);
			boolean selected = false;
			if(posnegall>0) if(f>threshold) { res.add(vt.stringTable[i][0]); weights.add(f); }
			if(posnegall<0) if(f<-threshold) { res.add(vt.stringTable[i][0]); weights.add(-f); }
			if(posnegall==0) if((f>threshold)||(f<-threshold)) { res.add(vt.stringTable[i][0]); weights.add(f); }
			}
		}
		
		Vector<String> res1 = new Vector<String>();
		float f[] = new float[weights.size()];
		for(int i=0;i<weights.size();i++) f[i] = weights.get(i);
		int inds[] = Algorithms.SortMass(f);
		for(int i=0;i<weights.size();i++) res1.add(res.get(inds[res.size()-i-1]));
		return res1;
	}
	
	public static Vector<String> getTopContributingGenes(VDataTable vt, int column, int posnegall, int numberOfGenes){
		Vector<String> res = new Vector<String>();
		Vector<Float> weights = new Vector<Float>();
		float threshold = 0f;
		for(int i=0;i<vt.rowCount;i++){
			if(!vt.stringTable[i][column].equals("N/A"))if(!vt.stringTable[i][column].equals("@")){
			float f = Float.parseFloat(vt.stringTable[i][column]);
			boolean selected = false;
			if(posnegall>0) if(f>=threshold) { res.add(vt.stringTable[i][0]); weights.add(f); }
			if(posnegall<0) if(f<=-threshold) { res.add(vt.stringTable[i][0]); weights.add(-f); }
			if(posnegall==0) if((f>=threshold)||(f<=-threshold)) { res.add(vt.stringTable[i][0]); weights.add(f); }
			}
		}
		
		Vector<String> res1 = new Vector<String>();
		float f[] = new float[weights.size()];
		for(int i=0;i<weights.size();i++) f[i] = weights.get(i);
		int inds[] = Algorithms.SortMass(f);
		for(int i=0;i<numberOfGenes;i++) res1.add(res.get(inds[res.size()-i-1]));
		return res1;
	}
	
	
	public static String produceAnnotationFromCompleteTable(String fn){
		String res = "---";
		VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile(fn, false, "\t");
		return res;
	}
	
	public static String produceAnnotationFromHTMLfile(String fn){
		String res = "";
		if(new File(fn).exists()){
			Vector<String> lines = Utils.loadStringListFromFile(fn);
			int k=0;
			String currentFunction = null;
			boolean functionAnnotated = false;
			while(k<lines.size()){
				String s = lines.get(k).trim();
				if(s.startsWith("<h4>")){
					currentFunction = s.substring(4, s.indexOf("[")).trim();
					functionAnnotated = false;
					//System.out.println(currentFunction);
				}
				if(s.startsWith("<TR valign=\"top\""))if(!functionAnnotated)if(!currentFunction.contains("Pubmed"))if(!currentFunction.contains("Drug"))if(!currentFunction.contains("Computational")){
					Vector<String> values = new Vector<String>();
					k = readCurrentRecord(lines,k,values);
					//System.out.println(values.get(0)+","+values.get(1)+","+values.get(2)+","+values.get(3)+","+values.get(4)+","+values.get(5));
					String name = values.get(0);
					String link = values.get(1);
					link = Utils.replaceString(link, "http://www.ebi.ac.uk/ego/DisplayGoTerm?id=", "https://www.ebi.ac.uk/QuickGO/term/");
					link = Utils.replaceString(link, "http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=mpAnnotSummary&id=", "http://www.informatics.jax.org/vocab/mp_ontology/");
					link = Utils.replaceString(link, "http://www.ebi.ac.uk/interpro/IEntry?ac=", "http://www.ebi.ac.uk/interpro/entry/");
					//link = Utils.replaceString(link, "http://www.ncbi.nlm.nih.gov/biosystems/", "");
					if(currentFunction.equals("9: Interaction"))
						link = "https://toppgene.cchmc.org/showTermDetail.jsp?category=Interaction&id="+values.get(5);
					link = Utils.replaceString(link, "http://www.broad.mit.edu/gsea/msigdb/cards/V$", "http://www.broad.mit.edu/gsea/msigdb/cards/");
					if(currentFunction.equals("18: Disease"))if((link==null)||(link.trim().equals(""))||(link.equals("null"))){
						String id = values.get(5);
						String parts[] = id.split(":");
						if(parts.length>1)
							link = "http://identifiers.org/"+parts[0]+"/"+parts[1];
						else
							link = id;
					}
					if(currentFunction.equals("4: Human Phenotype")){
						link = "https://mseqdr.org/hpo_browser.php?"+values.get(5);
					}
					float qval = Float.parseFloat(values.get(2));
					int ngenes = Integer.parseInt(values.get(3));
					int sizecat = Integer.parseInt(values.get(4));
					
					if(currentFunction.contains("13: Coexpression"))
					System.out.println(fn+"\t"+currentFunction+"\t"+qval);					
					
					if(qval<thresholdQValue)if(ngenes>=thresholdNumberOfGenesInEnrichment){
						if((link==null)||(link.trim().equals(""))||(link.equals("null")))
							res+=currentFunction+":	"+name+"<br>";
						else
							res+=currentFunction+":<a href=\""+link+"\">"+name+"</a><br>";
					}
					functionAnnotated = true;
				}
				k++;
			}
		}
		if(res.equals("")) res = null;
		return res;
	}
	
	/*
	 * Values are returned as 0-name, 1-link, 2-qval, 3-number of genes, 4-size of the categ 5 - id of the gene set 
	 */
	public static int readCurrentRecord(Vector<String> lines, int k, Vector<String> values){
		Vector<String> allvalues = new Vector<String>();
		while(!lines.get(k).trim().startsWith("</TR>")){
			String s = lines.get(k).trim();
			if(s.startsWith("<TD")){
				k++;
				s = lines.get(k).trim();
				allvalues.add(s);
				k--;
			}
			k++;
		}
		String id = allvalues.get(1);
		//name
		values.add(allvalues.get(2));
		//link
		String ss = allvalues.get(3);
		ss = ss.substring(ss.indexOf("\"")+1,ss.length());
		ss = ss.substring(0,ss.indexOf("\""));
		values.add(ss);
		//qval
		values.add(allvalues.get(5));
		//number of genes
		ss = allvalues.get(8).trim();
		StringTokenizer st = new StringTokenizer(ss,"<>");
		st.nextToken(); 
		ss = st.nextToken().trim();
		values.add(ss);
		//size of the categ
		ss = allvalues.get(9).trim();
		st = new StringTokenizer(ss,"<>");
		st.nextToken(); 
		ss = st.nextToken().trim();
		values.add(ss);
		
		values.add(id);
		
		return k;
	}
	
	public static float getWeight(String weightedGene){
		float weight = Float.NaN;
		if(weightedGene.contains("["))if(weightedGene.endsWith("]")){
			weightedGene = Utils.replaceString(weightedGene, "[", "#");
			String parts[] = weightedGene.split("#");
			weight = Float.parseFloat(parts[1].substring(0,parts[1].length()-1));
		}
		return weight;
	}
	public static String getName(String weightedGene){
		String name = weightedGene;
		if(weightedGene.contains("["))if(weightedGene.endsWith("]")){
			weightedGene = Utils.replaceString(weightedGene, "[", "#");
			String parts[] = weightedGene.split("#");
			name = parts[0];
		}
		return name;
	}
	
	
	public static void convertUPDNGMT2TableOfMetaGenes(String fn) throws Exception{
		Vector<String> list = Utils.loadStringListFromFile(fn);
		Vector<String> listOfColumns = new Vector<String>();
		Vector<String> listOfGenes = new Vector<String>();
		for(String s: list){
			String parts[] = s.split("\t");
			String parts1[] = parts[0].split("_");
			if(!listOfColumns.contains(parts1[1]))
				listOfColumns.add(parts1[1]);
			for(int i=2;i<parts.length;i++){
				String g = parts[i];
				g = getName(g);
				if(!listOfGenes.contains(g)) listOfGenes.add(g);
			}
		}
		Collections.sort(listOfGenes);
		VDataTable vt = new VDataTable();
		vt.colCount = 0;
		vt.rowCount = listOfGenes.size();
		vt.addNewColumn("GENE", "", "", vt.STRING, "N/A");
		for(int i=0;i<listOfColumns.size();i++) vt.addNewColumn(listOfColumns.get(i), "", "", vt.NUMERICAL, "N/A");
		for(int i=0;i<listOfGenes.size();i++) vt.stringTable[i][0] = listOfGenes.get(i);
		for(String s: list){
			String parts[] = s.split("\t");
			String parts1[] = parts[0].split("_");
			float val = 10f;
			if(parts1[0].equals("DN")) val = -val;
			for(int i=2;i<parts.length;i++){
				String geneName = getName(parts[i]);
				Float weight = getWeight(parts[i]);
				int row = listOfGenes.indexOf(geneName);
				int col = listOfColumns.indexOf(parts1[1]);
				float val1 = val+weight;
				vt.stringTable[row][col+1] = ""+val1;
			}
		}
		//FileWriter fw = new FileWriter(fn+".xls");
		//fw.write("GENE\t"); for(int i=0;i<listOfColumns.size();i++) fw.write(listOfColumns.get(i)+"\t"); fw.write("\n");
		//fw.close();
		VDatReadWrite.useQuotesEverywhere = false;
		VDatReadWrite.saveToSimpleDatFile(vt, fn+".xls");
	}
	
	
	/** This is a simple example of using HttpUnit to read and understand web pages. **/
	  /*public static void example( String[] params ) {
	    try {
	      // create the conversation object which will maintain state for us
	      WebConversation wc = new WebConversation();

	      // Obtain the main page on the meterware web site
	      String url="http://www.ihes.fr/~zinovyev";
	      
	      WebRequest request = new GetMethodWebRequest( url );
	      WebResponse response = wc.getResponse( request );
	      
	      // find the link which contains the string "HttpUnit" and click it
	      WebLink httpunitLink = response.getFirstMatchingLink( WebLink.MATCH_CONTAINED_TEXT, "HttpUnit" );
	      response = httpunitLink.click();

	      // print out the number of links on the HttpUnit main page
	      System.out.println( "The HttpUnit main page '"+url+"' contains " + response.getLinks().length + " links" );

	    } catch (Exception e) {
	       System.err.println( "Exception: " + e );
	    }
	  }*/


	

}
