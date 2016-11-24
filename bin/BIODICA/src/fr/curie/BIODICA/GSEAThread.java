package fr.curie.BIODICA;

public class GSEAThread implements Runnable{

	public String rnkFile = null;
	public String gmtFile = null;
	public int numberOfPermutations = 0;
	
    public void run() {
    	try{
    	//System.setOut(null);
    	//System.setErr(null);
    	MetaGeneAnnotation.MakeGSEAPrerankedAnalysis(rnkFile, gmtFile, numberOfPermutations);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

	
}
