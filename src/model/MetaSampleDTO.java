package model;

public class MetaSampleDTO {
	
	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String sampleAnnotationFilePath;
	private String ATablePath;
	
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
	
	public void setSampleAnnotationFilePath(String sampleAnnotationFilePath) 
	{
	        this.sampleAnnotationFilePath = sampleAnnotationFilePath;
    }
	
	public String getSampleAnnotationFilePath()
	{
		return sampleAnnotationFilePath;
	}

	public String getATablePath() {
		return ATablePath;
	}

	public void setATablePath(String aTablePath) {
		ATablePath = aTablePath;
	}
	
}
