package model;

import javax.swing.JLabel;

public class GseaDTO {
	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String STablePath;
	private String geneSetFolderPath;
	private int numberOfGSEAPermutations;
	private boolean doAnalysis;
	private String hTMLSourceFolder;
	private int TopMinNumberValue;
	private float TopThresholdValue;
	private float FDRThresholdValue;
	private float PValueThresholdValue;
	
	
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
	
	
	public void setGeneSetFolderPath(String geneSetFolderPath) 
	{
	        this.geneSetFolderPath = geneSetFolderPath;
    }
	
	public String getGeneSetFolderPath()
	{
		return geneSetFolderPath;
	}
	
	public void setNumberOfGSEAPermutations(int numberOfGSEAPermutations) 
	{
	        this.numberOfGSEAPermutations = numberOfGSEAPermutations;
    }
	
	public int getNumberOfGSEAPermutations()
	{
		return numberOfGSEAPermutations;
	}
	
	
	public void setHTMLSourceFolder(String hTMLSourceFolder) 
	{
	        this.hTMLSourceFolder = hTMLSourceFolder;
    }
	
	public String getHTMLSourceFolder()
	{
		return hTMLSourceFolder;
	}

	public int getTopMinNumberValue() {
		return TopMinNumberValue;
	}

	public void setTopMinNumberValue(int topMinNumberValue) {
		TopMinNumberValue = topMinNumberValue;
	}

	public float getTopThresholdValue() {
		return TopThresholdValue;
	}

	public void setTopThresholdValue(float topThresholdValue) {
		TopThresholdValue = topThresholdValue;
	}

	public float getFDRThresholdValue() {
		return FDRThresholdValue;
	}

	public void setFDRThresholdValue(float fDRThresholdValue) {
		FDRThresholdValue = fDRThresholdValue;
	}

	public float getPValueThresholdValue() {
		return PValueThresholdValue;
	}

	public void setPValueThresholdValue(float pValueThresholdValue) {
		PValueThresholdValue = pValueThresholdValue;
	}

	public boolean isDoAnalysis() {
		return doAnalysis;
	}

	public void setDoAnalysis(boolean doAnalysis) {
		this.doAnalysis = doAnalysis;
	}

	public String getSTablePath() {
		return STablePath;
	}

	public void setSTablePath(String sTablePath) {
		STablePath = sTablePath;
	}
	
	
}
