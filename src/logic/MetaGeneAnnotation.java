package logic;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.Algorithms;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleProcedures;

public class MetaGeneAnnotation{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void MergeGMTFilesInFolder(String folder) throws Exception{
		new File(folder+System.getProperty("file.separator")+"total.gmt").delete();
		File list[] = (new File(folder)).listFiles();
		FileWriter fw = new FileWriter(folder+System.getProperty("file.separator")+"total.gmt");
		for(File f:list)if(f.getName().endsWith(".gmt")){
			Vector<String> ls = Utils.loadStringListFromFile(f.getAbsolutePath());
			//System.out.println(f.getAbsolutePath());
			for(String s:ls)
				fw.write(s+"\n");
		}
		fw.close();
	}
	
	public static void produceRankFilesFromTable(String fn, String outfolder, String name) throws Exception{
			// produce ranked lists from a table
			String folder = outfolder;
			VDataTable tab = VDatReadWrite.LoadFromSimpleDatFile(fn, true, "\t");
			for(int i=1;i<tab.colCount;i++){
				String field = tab.fieldNames[i];
				FileWriter fw_minus_rnk = new FileWriter(folder+"/"+name+"_"+field+".rnk");
				FileWriter fw_plus = new FileWriter(folder+"/"+name+"_"+field+"_plus");
				FileWriter fw_minus = new FileWriter(folder+"/"+name+"_"+field+"_minus");
				FileWriter fw_abs = new FileWriter(folder+"/"+name+"_"+field+"_abs");
				float vals[] = new float[tab.rowCount];
				float val_abs[] = new float[tab.rowCount];
				for(int j=0;j<tab.rowCount;j++){ 
					float f = Float.parseFloat(tab.stringTable[j][i]); 
					vals[j] = f;
					val_abs[j] = Math.abs(f);
				}
				int inds[] = Algorithms.SortMass(vals);
				int ind_abs[] = Algorithms.SortMass(val_abs);
				for(int j=0;j<inds.length;j++){
					/*fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\t"+vals[inds[inds.length-1-j]]+"\n");
					fw_minus.write(tab.stringTable[inds[j]][0]+"\t"+vals[inds[j]]+"\n");
					fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\t"+val_abs[ind_abs[inds.length-1-j]]+"\n");*/
					fw_plus.write(tab.stringTable[inds[inds.length-1-j]][0]+"\n");
					fw_minus.write(tab.stringTable[inds[j]][0]+"\n");
					fw_abs.write(tab.stringTable[ind_abs[inds.length-1-j]][0]+"\n");				
				}
				
				HashMap<String, Float> values = new HashMap<String, Float>(); 
				for(int j=0;j<tab.rowCount;j++){
					String nm = tab.stringTable[j][0];
					float f = Float.parseFloat(tab.stringTable[j][i]);
					Float fv =values.get(nm);
					if(fv==null)
						values.put(nm, f);
					else{
						if(Math.abs(f)>Math.abs(fv)){
							values.put(nm, f);
						}
					}
				}
				float vls[] = new float[values.keySet().size()];
				String nms[] = new String[values.keySet().size()];
				int k=0;
				for(String s: values.keySet()){
					nms[k] = s;
					vls[k] = values.get(s);
					k++;
				}
				inds = Algorithms.SortMass(vls);
				for(int j=0;j<inds.length;j++){
					fw_minus_rnk.write(nms[inds[j]]+"\t"+vls[inds[j]]+"\n");
				}
				
				fw_plus.close();fw_minus.close();fw_abs.close(); fw_minus_rnk.close();
			}
	}
	
	public static void MakeGSEAPrerankedAnalysis(String rnkFile, String gmtFile, int numberOfPermutations) throws Exception{
		//File list[] = (new File(folder)).listFiles();
		//for(File f:list)if(f.getName().endsWith(".rnk")){
			//String rnkFile = f.getAbsolutePath();
			File f = new File(rnkFile);
			String analysisName = f.getName().substring(0, f.getName().length()-4);
			String folder = f.getParentFile().getAbsolutePath();
			//System.out.println(folder);
			//rnkFile = ""+rnkFile+"\"";
			//System.out.println(rnkFile);
			//gmtFile = gmtFile;
			String outFile = folder+"/results/"+analysisName;
			//rnkFile = rnkFile.replace("-", "\\-");
			//gmtFile = gmtFile.replace("-", "\\-");
			//outFile = outFile.replace("-", "\\-");
			Vector<String> argsv = new Vector<String>();
			argsv.add("-gmx"); argsv.add(gmtFile);
			argsv.add("-collapse"); argsv.add("false");
			argsv.add("-mode"); argsv.add("Max_probe");
			argsv.add("-norm"); argsv.add("meandiv");
			argsv.add("-nperm"); argsv.add(""+numberOfPermutations);
			argsv.add("-rnk"); argsv.add(rnkFile);
			argsv.add("-scoring_scheme"); argsv.add("classic");
			argsv.add("-rpt_label"); argsv.add(analysisName);
			argsv.add("-include_only_symbols"); argsv.add("true");
			argsv.add("-make_sets"); argsv.add("true");
			argsv.add("-plot_top_x"); argsv.add("50");
			argsv.add("-rnd_seed"); argsv.add("timestamp");
			argsv.add("-set_max"); argsv.add("200");
			argsv.add("-set_min"); argsv.add("8");
			argsv.add("-zip_report"); argsv.add("false");
			argsv.add("-out"); argsv.add(outFile);
			argsv.add("-gui"); argsv.add("false");
			String args[] = new String[argsv.size()];
			for(int i=0;i<argsv.size();i++) { args[i]=argsv.get(i); }
			try{
			xtools.gsea.GseaPreranked.main(args);
			}catch(Exception e){
				
			}
			//System.out.println("Do GSEA... "+rnkFile);
		//}
	}
	
	public static void FilterGSEAResults(String folder, String analysisprefix, int minNumberOfGenes, float scoreThreshold, float qFDRthreshold, float pFWERthreshold) throws Exception{
		float f[] = {1f,2f};
		int ks[] = Algorithms.SortMass(f);
		//GMTFile gmt = new GMTFile();
		//gmt.load(gmts);
		File dir = new File(folder);
		File comps[] = dir.listFiles();
		FileWriter fw = new FileWriter(folder+"results_GSEA_filtered.html");
		
		fw.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
		fw.write("<html>\n");
		fw.write("<head>\n");
		fw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\n");
		fw.write("<title>GSEA analysis for ICA of "+analysisprefix+"</title>\n");
		fw.write("<link href=\"filteredgsea.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
		fw.write("</head>\n");
		fw.write("<body>\n");
		fw.write("<h2>GSEA analysis for ICA of "+analysisprefix+"</h2>\n");
		fw.write("<div id=\"content\">");

		
		//for(int i=0;i<5;i++){
		for(int i=0;i<comps.length;i++){
			if(comps[i].isDirectory())if(!comps[i].getName().endsWith("filtered")){
				File fold = comps[i].listFiles()[0];
				File folder_filtered = new File(comps[i].getAbsolutePath()+"_filtered");
				folder_filtered.mkdir();
				File xlsFiles[] = fold.listFiles();
				Vector<String> goodModules = new Vector<String>();
				Vector<Float> proportions = new Vector<Float>();
				Vector<String> goodGenes = new Vector<String>();
				Vector<Float> goodGenesCounts = new Vector<Float>();
				for(int j=0;j<xlsFiles.length;j++){
					if(xlsFiles[j].getName().endsWith(".xls")){
						String module = xlsFiles[j].getName();
						module = module.substring(0, module.length()-4);
						if(true)/*if(gmt.setnames.contains(module))*/{
							VDataTable tab = new VDataTable();
							//System.out.println(xlsFiles[j].getAbsolutePath());
							tab = VDatReadWrite.LoadFromSimpleDatFile(xlsFiles[j].getAbsolutePath(), true, "\t");
							if(tab.fieldNumByName("PROBE")!=-1){
								
							Vector<String> significantGenes =  new Vector<String>();
							
							// find significant genes
							for(int k=0;k<tab.rowCount;k++){
								String gene = tab.stringTable[k][tab.fieldNumByName("PROBE")];
								float score = Float.parseFloat(tab.stringTable[k][tab.fieldNumByName("RANK METRIC SCORE")]);
								String core = tab.stringTable[k][tab.fieldNumByName("CORE ENRICHMENT")];
								if(core.equals("Yes"))if(Math.abs(score)>scoreThreshold){
									significantGenes.add(gene);
								}
							}
							
							if(significantGenes.size()>=minNumberOfGenes){
								//System.out.print(module+"\t");
								String prefix = xlsFiles[j].getAbsolutePath();
								prefix = prefix.substring(0, prefix.length()-4);
								File html = new File(prefix+".html");
								String text = Utils.loadString(html.getAbsolutePath());
								
								// Extracting p,q-values
								float qFDR = 1f;
								float pFWER = 1f;
								text = Utils.replaceString(text,"<td>", "%");
								text = Utils.replaceString(text,"</td>", "%");
								text = Utils.replaceString(text,"<tr>", "%");
								text = Utils.replaceString(text,"</tr>", "%");
								StringTokenizer st = new StringTokenizer(text,"<'=>%");
								while(st.hasMoreTokens()){
									String s = st.nextToken();
									if(s.equals("FDR q-value"))
										qFDR = Float.parseFloat(st.nextToken());
									if(s.equals("FWER p-Value"))
										pFWER = Float.parseFloat(st.nextToken());
									
								}
								//System.out.println(qFDR+"\t"+pFWER+"\t"+module);
								if(qFDR<=qFDRthreshold)if(pFWER<=pFWERthreshold){
									
									BigDecimal bd = new BigDecimal(qFDR);
									bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
									qFDR = (float)bd.doubleValue();
									bd = new BigDecimal(pFWER);
									bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
									pFWER = (float)bd.doubleValue();
									
									//goodModules.add(module+"("+significantGenes.size()+"/"+tab.rowCount+"/q="+qFDR+",p="+pFWER+")");
									goodModules.add(module+"("+significantGenes.size()+"/"+tab.rowCount+"/p="+pFWER+")");
									proportions.add((float)significantGenes.size()/(float)tab.rowCount);

									st = new StringTokenizer(text,"< '=>");
									Vector<String> pngs = new Vector<String>();
									while(st.hasMoreTokens()){
										String s = st.nextToken();
										if(s.endsWith(".png"))
											pngs.add(s);
									}
									//Files.copy(html.toPath(), new File(folder_filtered.getAbsolutePath()+"/"+html.getName()).toPath(),  REPLACE_EXISTING);
									//String texttemp = Utils.loadString(html.getAbsolutePath());
									//Utils.saveStringToFile(texttemp, folder_filtered.getAbsolutePath()+"/"+html.getName());
									FileUtils.copyFile(html, new File(folder_filtered.getAbsolutePath()+"/"+html.getName()));
									for(String s: pngs){
										File png = new File(xlsFiles[j].getParent()+"/"+s);
										//Files.copy(png.toPath(), new File(folder_filtered.getAbsolutePath()+"/"+s).toPath(),  REPLACE_EXISTING);
										//texttemp = Utils.loadString(png.getAbsolutePath());
										//Utils.saveStringToFile(texttemp, folder_filtered.getAbsolutePath()+"/"+s);
										FileUtils.copyFile(png, new File(folder_filtered.getAbsolutePath()+"/"+s));
									}
									
									for(int l=0;l<significantGenes.size();l++){
										int k = goodGenes.indexOf(significantGenes.get(l));
										if(k<0){
											goodGenes.add(significantGenes.get(l));
											goodGenesCounts.add(1f);
										}else{
											goodGenesCounts.set(k, goodGenesCounts.get(k)+1f);
										}
									}
									
								}
								
							}else{
								//System.out.print(significantGenes.size()+"\t");
							}
							}
							
						}
					}
				}
				
				float props[] = new float[proportions.size()];
				for(int k=0;k<props.length;k++)
					props[k] = proportions.get(k);
				int inds[] = Algorithms.SortMass(props);
				System.out.print(comps[i].getName()+"\t");
				if(goodModules.size()>0){
					fw.write("<b><font color=\"red\">"+comps[i].getName()+"</font></b>"+"(<a href="+folder_filtered.getName()+"/"+comps[i].getName()+"_freqgenes.html"+">freqgenes</a>)"+"&nbsp;&nbsp;&nbsp;&nbsp;");
					
					FileWriter fw1 = new FileWriter(folder_filtered+"/"+comps[i].getName()+"_freqgenes.html");
					float counts[] = new float[goodGenes.size()];
					for(int k=0;k<counts.length;k++)
						counts[k] = goodGenesCounts.get(k);
					int inds1[] = Algorithms.SortMass(counts);
					for(int k=0;k<counts.length;k++){
						fw1.write("<a target='_blank' href=\'http://www.genecards.org/cgi-bin/carddisp.pl?gene="+goodGenes.get(inds1[inds1.length-k-1])+"'>"+goodGenes.get(inds1[inds1.length-k-1])+"</a>&nbsp;\t"+goodGenesCounts.get(inds1[inds1.length-k-1])+"<br>\n");
					}
					fw1.close();
					
				}else{
					fw.write(comps[i].getName()+"&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				System.out.print(goodModules.size()+"\t");
				fw.write(goodModules.size()+"&nbsp;&nbsp;");
				for(int k=0;k<goodModules.size();k++){
					String module = goodModules.get(inds[inds.length-k-1]);
					System.out.print(module+"\t");
					StringTokenizer st = new StringTokenizer(module,"(");
					String module_fn = st.nextToken();
					String module_fn2 = st.nextToken();
					fw.write("<a href="+folder_filtered.getName()+"/"+module_fn+".html"+">"+module_fn+"</a>("+module_fn2+")&nbsp;&nbsp;&nbsp;");
				}
				System.out.println();
				fw.write("<br><br>\n");fw.flush();
			}
		}
		fw.write("</div>\n");
		fw.write("</body>\n");
		fw.write("</html>\n");
		fw.close();
	}

	
}
