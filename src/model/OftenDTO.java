package model;

public class OftenDTO {
	
	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String STablePath;
	private String doOften;

	
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
	
	
	public void setDoOften(String doOften) 
	{
	        this.doOften = doOften;
    }
	
	public String getDoOften()
	{
		return doOften;
	}

	public String getSTablePath() {
		return STablePath;
	}

	public void setSTablePath(String sTablePath) {
		STablePath = sTablePath;
	}
	
}
