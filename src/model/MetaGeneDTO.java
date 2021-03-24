package model;

public class MetaGeneDTO {

	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String STablePath;
	private String geneAnnotationFilePath;
	
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
	
	public void setGeneAnnotationFilePath(String geneAnnotationFilePath) 
	{
	        this.geneAnnotationFilePath = geneAnnotationFilePath;
    }
	
	public String getGeneAnnotationFilePath()
	{
		return geneAnnotationFilePath;
	}

	public String getSTablePath() {
		return STablePath;
	}

	public void setSTablePath(String sTablePath) {
		STablePath = sTablePath;
	}
}
