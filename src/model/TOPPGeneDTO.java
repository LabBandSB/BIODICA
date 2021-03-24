package model;

public class TOPPGeneDTO {
	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String STablePath;
	private int numberOfGenes;
	private float thresholdForPvalue;
	private boolean doActualComputations;
	private String hTMLSourceFolder;
	
	public void setDataTablePath(String dataTablePath) 
	{
	        this.dataTablePath = dataTablePath;
    }
	
	public String getDataTablePath()
	{
		return dataTablePath;
	}
	
	
	public void setDefaultWorkFolderPath(String defaultWorkFolderPath) 
	{
	        this.defaultWorkFolderPath = defaultWorkFolderPath;
    }
	
	public String getDefaultWorkFolderPath()
	{
		return defaultWorkFolderPath;
	}
	
	
	public void setNumberOfGenes(int _numberOfGenes) 
	{
	        this.numberOfGenes = _numberOfGenes;
    }
	
	public int getNumberOfGene()
	{
		return numberOfGenes;
	}
	
	
	public void setHTMLSourceFolder(String hTMLSourceFolder) 
	{
	        this.hTMLSourceFolder = hTMLSourceFolder;
    }
	
	public String getHTMLSourceFolder()
	{
		return hTMLSourceFolder;
	}
	
	public void setThresholdForPvalue(float threshold) 
	{
	        this.thresholdForPvalue = threshold;
    }
	
	public float getThresholdForPvalue()
	{
		return thresholdForPvalue;
	}

	public void setDoActualCalculations(boolean docalc) 
	{
	        this.doActualComputations = docalc;
    }
	
	public boolean getDoActualCalculations()
	{
		return doActualComputations;
	}

	public String getSTablePath() {
		return STablePath;
	}

	public void setSTablePath(String sTablePath) {
		STablePath = sTablePath;
	}	
	
}
