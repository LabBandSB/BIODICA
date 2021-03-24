package model;

public class ConfigDTO 
{
	private String matlabicaFolderPath;
	private String defaultWorkFolderPath;
	private String geneSetPath;
	private String hTMLSourcePath;
	private String mATLABFolderPath;
	private boolean useDocker;
	private String GenePropertiesFilePath;
	private String MetaGeneFolderPath; 
	private String NetworkUndirectedFilePath; 
	private String NetworkDirectedFilePath;
	
	
	private boolean computeRobustStatistics;
	
	private int minNumberOfDistinctValuesInNumericals;
	private int minNumberOfSamplesInCategory;
	
	private int maxNumberOfCategories;
	private double associationAnalysisInThreshold;
	private double significanceThresholdForShowingPlots;
	private double associationCorrelationThreshold;
	private double correlationThresholdForShowingPlots;
	
	private String ICAApproach;
	private String ICAMeasure;
	private int ICAMaxNumIterations;

	
	private double minimalTolerableStability;
	
	
	public void setMatlabicaFolderPath(String matlabicaFolderPath) 
	{
	        this.matlabicaFolderPath = matlabicaFolderPath;
    }
	
	public String getMatlabicaFolderPath()
	{
		return matlabicaFolderPath;
	}
	
	
	public void setDefaultWorkFolderPath(String defaultWorkFolderPath)
	{
		this.defaultWorkFolderPath = defaultWorkFolderPath;
	}
	
	public String getDefaultWorkFolderPath()
	{
		return defaultWorkFolderPath;
	}

	
	public void setGeneSetPath(String geneSetPath)
	{
		this.geneSetPath = geneSetPath;
	}
	
	public String getGeneSetPath()
	{
		return geneSetPath;
	}
	
	
	public void setHTMLSourcePath(String hTMLSourcePath)
	{
		this.hTMLSourcePath = hTMLSourcePath;
	}
	
	public String getHTMLSourcePath()
	{
		return hTMLSourcePath;
	}
	
	public void setMATLABFolderPath(String mATLABFolderPath)
	{
		this.mATLABFolderPath = mATLABFolderPath;
	}
	
	public String getMATLABFolderPath()
	{
		return mATLABFolderPath;
	}
	
	
	public void setComputeRobustStatistics(boolean computeRobustStatistics)
	{
		this.computeRobustStatistics = computeRobustStatistics;
	}
	
	public boolean getComputeRobustStatistics()
	{
		return computeRobustStatistics;
	}
	
	
	public void setMinNumberOfDistinctValuesInNumericals(int minNumberOfDistinctValuesInNumericals)
	{
		this.minNumberOfDistinctValuesInNumericals = minNumberOfDistinctValuesInNumericals;
	}
	
	public int getMinNumberOfDistinctValuesInNumericals()
	{
		return minNumberOfDistinctValuesInNumericals;
	}

	
	public void setMinNumberOfSamplesInCategory(int minNumberOfSamplesInCategory)
	{
		this.minNumberOfSamplesInCategory = minNumberOfSamplesInCategory;
	}
	
	public int getMinNumberOfSamplesInCategory()
	{
		return minNumberOfSamplesInCategory;
	}
	
	
	public void setMaxNumberOfCategories(int maxNumberOfCategories)
	{
		this.maxNumberOfCategories = maxNumberOfCategories;
	}
	
	public int getMaxNumberOfCategories()
	{
		return maxNumberOfCategories;
	}
	
	
	
	public void setAssociationAnalysisInThreshold(double associationAnalysisInThreshold)
	{
		this.associationAnalysisInThreshold = associationAnalysisInThreshold;
	}
	
	public double getAssociationAnalysisInThreshold()
	{
		return associationAnalysisInThreshold;
	}
	
	
	public void setMinimalTolerableStability(double minimalTolerableStability)
	{
		this.minimalTolerableStability = minimalTolerableStability;
	}
	
	public double getMinimalTolerableStability()
	{
		return minimalTolerableStability;
	}

	public String getGenePropertiesFilePath() {
		return GenePropertiesFilePath;
	}

	public void setGenePropertiesFilePath(String genePropertiesFilePath) {
		GenePropertiesFilePath = genePropertiesFilePath;
	}

	public String getMetaGeneFolderPath() {
		return MetaGeneFolderPath;
	}

	public void setMetaGeneFolderPath(String metaGeneFolderPath) {
		MetaGeneFolderPath = metaGeneFolderPath;
	}

	public String getNetworkUndirectedFilePath() {
		return NetworkUndirectedFilePath;
	}

	public void setNetworkUndirectedFilePath(String networkUndirectedFilePath) {
		NetworkUndirectedFilePath = networkUndirectedFilePath;
	}

	public String getNetworkDirectedFilePath() {
		return NetworkDirectedFilePath;
	}

	public void setNetworkDirectedFilePath(String networkDirectedFilePath) {
		NetworkDirectedFilePath = networkDirectedFilePath;
	}

	public double getSignificanceThresholdForShowingPlots() {
		return significanceThresholdForShowingPlots;
	}

	public void setSignificanceThresholdForShowingPlots(double significanceThresholdForShowingPlots) {
		this.significanceThresholdForShowingPlots = significanceThresholdForShowingPlots;
	}

	public double getAssociationCorrelationThreshold() {
		return associationCorrelationThreshold;
	}

	public void setAssociationCorrelationThreshold(double associationCorrelationThreshold) {
		this.associationCorrelationThreshold = associationCorrelationThreshold;
	}

	public double getCorrelationThresholdForShowingPlots() {
		return correlationThresholdForShowingPlots;
	}

	public void setCorrelationThresholdForShowingPlots(double correlationThresholdForShowingPlots) {
		this.correlationThresholdForShowingPlots = correlationThresholdForShowingPlots;
	}

	public boolean isUseDocker() {
		return useDocker;
	}

	public void setUseDocker(boolean useDocker) {
		this.useDocker = useDocker;
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
