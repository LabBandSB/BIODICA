package fr.curie.BIODICA;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import vdaoengine.data.VDataTable;
import vdaoengine.utils.Utils;
import vdaoengine.utils.VSimpleFunctions;

public class Metagene {

	public Vector<ObjectWeight> weights = new Vector<ObjectWeight>();
	public String name = null;
	public HashMap<String, Float> map = new HashMap<String, Float>();
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Vector<Metagene> metagenes = loadFromAllRNKInFolder("C:/Datas/ICA/Anne/metaranking/GSEA/");
			makeMetageneTable(metagenes, "C:/Datas/ICA/Anne/metaranking/GSEA/CIT.xls");			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void makeMap(){
		for(ObjectWeight w: weights){
			map.put(w.objectName, w.weight);
		}
	}
	
	public static Vector<Metagene> decomposeTableIntoMetagenes(VDataTable vt){
		Vector<Metagene> res = new Vector<Metagene>();
		for(int i=0;i<vt.fieldNames.length;i++)if(vt.fieldTypes[i]==vt.NUMERICAL){
			Metagene mg = new Metagene();
			mg.name = vt.fieldNames[i];
			for(int j=0;j<vt.rowCount;j++){
				try{
					ObjectWeight w = new ObjectWeight();
					w.objectName = vt.stringTable[j][0];
					w.weight = Float.parseFloat(vt.stringTable[j][i]);
					mg.weights.add(w);
				}catch(Exception e){
					
				}
			}
			res.add(mg);
		}
		return res;
	}
	
	public static Metagene loadFromRNKfile(String fn){
		Metagene mg = new Metagene();
		mg.name = new File(fn).getName();
		mg.name = mg.name.substring(0, mg.name.length()-4);
		Vector<String> lines = Utils.loadStringListFromFile(fn);
		for(String s: lines){
			StringTokenizer st = new StringTokenizer(s,"\t");
			try{
				String name = st.nextToken();
				float value = Float.parseFloat(st.nextToken());
				ObjectWeight w = new ObjectWeight();
				w.objectName = name;
				w.weight = value;
				mg.weights.add(w);
			}catch(Exception e){
				
			}
		}
		return mg;
	}
	
	public static float CalcCorrelation(Metagene m1, Metagene m2){
		float corr = 0;
		m1.makeMap();
		m2.makeMap();
		Vector<Float> val1v = new Vector<Float>();
		Vector<Float> val2v = new Vector<Float>();
		Set<String> keys = m1.map.keySet();
		for(String s: keys){
			if(m2.map.get(s)!=null){
				val1v.add(m1.map.get(s));
				val2v.add(m2.map.get(s));
			}
		}
		float val1[] = new float[val1v.size()]; for(int i=0;i<val1v.size();i++) val1[i] = val1v.get(i);
		float val2[] = new float[val2v.size()]; for(int i=0;i<val2v.size();i++) val2[i] = val2v.get(i);
		corr = VSimpleFunctions.calcCorrelationCoeff(val1, val2);
		return corr;
	}
	
	public static Vector<Metagene> loadFromAllRNKInFolder(String folder){
		Vector<Metagene> mgs = new Vector<Metagene>();
		File f[] = new File(folder).listFiles();
		for(File file: f)if(file.getName().toLowerCase().endsWith("rnk")){
			Metagene mg = Metagene.loadFromRNKfile(file.getAbsolutePath());
			mgs.add(mg);
		}
		return mgs;
	}
	
	public static Vector<String> metageneIntersection(Metagene m1, Metagene m2){
		Vector<String> names = new Vector<String>();
		m1.makeMap();
		m2.makeMap();
		Set<String> keys = m1.map.keySet();
		for(String s: keys){
			if(m2.map.get(s)!=null){
				names.add(s);
			}
		}
		return names;
	}
	
	public static void makeMetageneTable(Vector<Metagene> metagenes, String fn) throws Exception{
		Vector<String> allgenes = new Vector<String>();
		for(Metagene m: metagenes){
			m.makeMap();
			for(ObjectWeight o: m.weights)
				if(!allgenes.contains(o.objectName))
					allgenes.add(o.objectName);
		}
		Collections.sort(allgenes);
		FileWriter fw = new FileWriter(fn);
		fw.write("GENE\t"); 
		for(int i=0;i<metagenes.size();i++) fw.write(metagenes.get(i).name+"\t");
		fw.write("\n");
		for(String s: allgenes){
			fw.write(s+"\t");
			for(int i=0;i<metagenes.size();i++){
				if(metagenes.get(i).map.containsKey(s)){
					fw.write(metagenes.get(i).map.get(s)+"\t");
				}else{
					fw.write("N/A\t");
				}	
			}
			fw.write("\n");
			}
		fw.close();
	}

}
