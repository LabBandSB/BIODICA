package model;

public class IcaDTO 
{
	private String dataTablePath;
	private String defaultWorkFolderPath;
	private String mATLABFolderPath;
	private String matlabicaFolderPath;
	private String sNoOfComponents;
	private boolean useDocker;
	private boolean visOption;
	private String ICAApproach;
	private String ICAMeasure;
	private int ICAMaxNumIterations;
	
	
	
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
	
	
	public void setMATLABFolderPath(String mATLABFolderPath) 
	{
	        this.mATLABFolderPath = mATLABFolderPath;
    }
	
	public String getMATLABFolderPath()
	{
		return mATLABFolderPath;
	}
	
	
	public void setMatlabicaFolderPath(String matlabicaFolderPath) 
	{
	        this.matlabicaFolderPath = matlabicaFolderPath;
    }
	
	public String getMatlabicaFolderPath()
	{
		return matlabicaFolderPath;
	}
	
	
	public void setSNoOfComponents(String sNoOfComponents) 
	{
	        this.sNoOfComponents = sNoOfComponents;
    }
	
	public String getSNoOfComponents()
	{
		return sNoOfComponents;
	}

	public boolean isUseDocker() {
		return useDocker;
	}

	public void setUseDocker(boolean useDocker) {
		this.useDocker = useDocker;
	}

	public boolean isVisOption() {
		return visOption;
	}

	public void setVisOption(boolean visOption) {
		this.visOption = visOption;
	}

	public String getICAApproach() {
		return ICAApproach;
	}

	public void setICAApproach(String iCAApproach) {
		ICAApproach = iCAApproach;
	}

	public String getICAMeasure() {
		return ICAMeasure;
	}

	public void setICAMeasure(String iCAMeasure) {
		ICAMeasure = iCAMeasure;
	}

	public int getICAMaxNumIterations() {
		return ICAMaxNumIterations;
	}

	public void setICAMaxNumIterations(int iCAMaxNumIterations) {
		ICAMaxNumIterations = iCAMaxNumIterations;
	}
}
