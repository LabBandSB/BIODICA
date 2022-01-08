package model;

public class IcaDTO 
{
	private String dataTablePath;
	private String defaultWorkFolderPath;
	private String sNoOfComponents;
	private boolean visOption;
	
	private String ICAImplementation;
	
	private String mATLABFolderPath;
	private String matlabicaFolderPath;
	private boolean useDocker;
	private String MATLABICAApproach;
	private String MATLABICAMeasure;
	private int MATLABICAMaxNumIterations;
	private int MATLABNumberOfICARuns;

	private String pythonicaFolderPath;
	private String PythonTypeOfVisualization;
	private String PythonICAApproach;
	private String PythonICAMeasure;
	private int PythonICAMaxNumIterations;
	private int PythonNumberOfICARuns;
	
	
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

	public String getMATLABICAApproach() {
		return MATLABICAApproach;
	}

	public void setMATLABICAApproach(String iCAApproach) {
		MATLABICAApproach = iCAApproach;
	}

	public String getMATLABICAMeasure() {
		return MATLABICAMeasure;
	}

	public void setMATLABICAMeasure(String iCAMeasure) {
		MATLABICAMeasure = iCAMeasure;
	}

	public int getMATLABICAMaxNumIterations() {
		return MATLABICAMaxNumIterations;
	}

	public void setMATLABICAMaxNumIterations(int iCAMaxNumIterations) {
		MATLABICAMaxNumIterations = iCAMaxNumIterations;
	}

	public String getPythonTypeOfVisualization() {
		return PythonTypeOfVisualization;
	}

	public void setPythonTypeOfVisualization(String pythonTypeOfVisualization) {
		PythonTypeOfVisualization = pythonTypeOfVisualization;
	}

	public String getPythonicaFolderPath() {
		return pythonicaFolderPath;
	}
	
	public void setPythonicaFolderPath(String pythonicaFolderPath) {
		this.pythonicaFolderPath = pythonicaFolderPath;
	}

	public String getICAImplementation() {
		return ICAImplementation;
	}

	public void setICAImplementation(String iCAImplementation) {
		ICAImplementation = iCAImplementation;
	}

	public int getMATLABNumberOfICARuns() {
		return MATLABNumberOfICARuns;
	}

	public void setMATLABNumberOfICARuns(int mATLABNumberOfICARuns) {
		MATLABNumberOfICARuns = mATLABNumberOfICARuns;
	}

	public String getPythonICAApproach() {
		return PythonICAApproach;
	}

	public void setPythonICAApproach(String pythonICAApproach) {
		PythonICAApproach = pythonICAApproach;
	}

	public String getPythonICAMeasure() {
		return PythonICAMeasure;
	}

	public void setPythonICAMeasure(String pythonICAMeasure) {
		PythonICAMeasure = pythonICAMeasure;
	}

	public int getPythonICAMaxNumIterations() {
		return PythonICAMaxNumIterations;
	}

	public void setPythonICAMaxNumIterations(int pythonICAMaxNumIterations) {
		PythonICAMaxNumIterations = pythonICAMaxNumIterations;
	}

	public int getPythonNumberOfICARuns() {
		return PythonNumberOfICARuns;
	}

	public void setPythonNumberOfICARuns(int pythonNumberOfICARuns) {
		PythonNumberOfICARuns = pythonNumberOfICARuns;
	}
	
}
