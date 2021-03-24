package util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import model.ConfigDTO;
import model.ConstantCodes;

public class ConfigHelper 
{
	
	public static String DEFAULT_DATA_TABLE_PATH = "";

	
	public void updateFoldersPathValuesInConfigFile(ConfigDTO configDTO)
	{
		FileWriter fw = null;
		
		try {
			
			fw = new FileWriter(ConstantCodes.CONFIG_FILE_NAME);			
			fw.write(generateAndGetDataForConfigFile(configDTO));
			
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public ConfigDTO getFoldersPathValuesFromConfigFile()
	{
		ConfigDTO configDTO = new ConfigDTO();
		
		PropertiesEx prop = new PropertiesEx();
		
		File configFile = new File(ConstantCodes.CONFIG_FILE_NAME);  	
		try 
		{
			FileReader reader = new FileReader(configFile);
			prop.load(reader);
			configDTO.setMatlabicaFolderPath(prop.getProperty(ConstantCodes.MATLABICA_FOLDER));
			configDTO.setDefaultWorkFolderPath(prop.getProperty(ConstantCodes.DEFAULT_WORK_FOLDER));
			configDTO.setGeneSetPath(prop.getProperty(ConstantCodes.GENE_SET_FOLDER));
			configDTO.setHTMLSourcePath(prop.getProperty(ConstantCodes.HTML_SOURCE_FOLDER));
			configDTO.setMATLABFolderPath(prop.getProperty(ConstantCodes.MATLAB_FOLDER));
			configDTO.setUseDocker(prop.getProperty(ConstantCodes.USE_DOCKER).toLowerCase().equals("true")||prop.getProperty(ConstantCodes.USE_DOCKER).toLowerCase().equals("1"));
			configDTO.setGenePropertiesFilePath(prop.getProperty(ConstantCodes.GENE_PROPERTIES_FILE));
			configDTO.setMetaGeneFolderPath(prop.getProperty(ConstantCodes.METAGENE_FOLDER));
			configDTO.setNetworkUndirectedFilePath(prop.getProperty(ConstantCodes.NETWORK_UNDIRECTED_FILE));
			configDTO.setNetworkDirectedFilePath(prop.getProperty(ConstantCodes.NETWORK_DIRECTED_FILE));
			configDTO.setComputeRobustStatistics(Boolean.parseBoolean(prop.getProperty(ConstantCodes.COMPUTE_ROBUST_STATISTICS)));
			
			String ICAApproach = prop.getProperty(ConstantCodes.ICA_APPROACH);
			if(ICAApproach!=null && !ICAApproach.isEmpty()){
				configDTO.setICAApproach(prop.getProperty(ConstantCodes.ICA_APPROACH));
			}else
				configDTO.setICAApproach("symm");
			
			String ICAMeasure = prop.getProperty(ConstantCodes.ICA_MEASURE);
			if(ICAMeasure!=null && !ICAMeasure.isEmpty()){
				configDTO.setICAMeasure(prop.getProperty(ConstantCodes.ICA_MEASURE));
			}else
				configDTO.setICAMeasure("pow3");

			String ICAMaxNumIterations = prop.getProperty(ConstantCodes.ICA_MAXNUMBER_ITERATIONS);
			if(ICAMaxNumIterations!=null && !ICAMaxNumIterations.isEmpty()){
				configDTO.setICAMaxNumIterations(Integer.parseInt(prop.getProperty(ConstantCodes.ICA_MAXNUMBER_ITERATIONS)));
			}else
				configDTO.setICAMaxNumIterations(100);
			
			String minNumberOfDistinctValuesInNumbericals = prop.getProperty(ConstantCodes.MIN_NUMBER_OF_DISTINCT_VALUES_IN_NUMERICALS);
			if(minNumberOfDistinctValuesInNumbericals != null && !minNumberOfDistinctValuesInNumbericals.isEmpty())
			{
				configDTO.setMinNumberOfDistinctValuesInNumericals(Integer.parseInt(minNumberOfDistinctValuesInNumbericals));
			}
			
			String minNumberOfSamplesInCategory = prop.getProperty(ConstantCodes.MIN_NUMBER_OF_SAMPLES_IN_CATEGORY);
			
			if(minNumberOfSamplesInCategory != null && !minNumberOfSamplesInCategory.isEmpty())
			{
				configDTO.setMinNumberOfSamplesInCategory(Integer.parseInt(minNumberOfSamplesInCategory));
			}
			
			String maxNumberOfCategories = prop.getProperty(ConstantCodes.MAX_NUMBER_OF_CATEGORIES);
			
			if(maxNumberOfCategories != null && !maxNumberOfCategories.isEmpty())
			{
				configDTO.setMaxNumberOfCategories(Integer.parseInt(maxNumberOfCategories));
			}

			String associationAnalysisInThreshold = prop.getProperty(ConstantCodes.ASSOCIATION_ANALYSIS_IN_THRESHOLD);
			
			if(associationAnalysisInThreshold != null && !associationAnalysisInThreshold.isEmpty())
			{
				configDTO.setAssociationAnalysisInThreshold(Double.parseDouble(associationAnalysisInThreshold));
			}

			String significanceThresholdForShowingPlots = prop.getProperty(ConstantCodes.SIGNIFICANCE_THRESHOLD_FOR_SHOWING_PLOTS);
			
			if(significanceThresholdForShowingPlots != null && !significanceThresholdForShowingPlots.isEmpty())
			{
				configDTO.setSignificanceThresholdForShowingPlots(Double.parseDouble(significanceThresholdForShowingPlots));
			}

			String associationCorrelationThreshold = prop.getProperty(ConstantCodes.ASSOCIATION_CORRELATION_THRESHOLD);
			
			if(associationCorrelationThreshold != null && !associationCorrelationThreshold.isEmpty())
			{
				configDTO.setAssociationCorrelationThreshold(Double.parseDouble(associationCorrelationThreshold));
			}

			String correlationThresholdForShowingPlots = prop.getProperty(ConstantCodes.CORRELATION_THRESHOLD_FOR_SHOWING_PLOTS);
			
			if(correlationThresholdForShowingPlots != null && !correlationThresholdForShowingPlots.isEmpty())
			{
				configDTO.setCorrelationThresholdForShowingPlots(Double.parseDouble(correlationThresholdForShowingPlots));
			}
			
			
			String minimalTolerableStability = prop.getProperty(ConstantCodes.MINIMAL_TOLERABLE_STABILITY);
			
			if(minimalTolerableStability != null && !minimalTolerableStability.isEmpty())
			{
				configDTO.setMinimalTolerableStability(Double.parseDouble(minimalTolerableStability));
			}
			
    	} catch (FileNotFoundException ex) {
		    // file does not exist
		} catch (IOException ex) {
		    // I/O error
		}	   
		return configDTO;
	
	}
	
	
	
	public String generateAndGetDataForConfigFile(ConfigDTO cfDTO)
	{
		 StringWriter sw = new StringWriter(); 
		 sw.append(ConstantCodes.MATLABICA_FOLDER).append(" = ").append(cfDTO.getMatlabicaFolderPath()).append('\n');
		 sw.append(ConstantCodes.DEFAULT_WORK_FOLDER).append(" = ").append(cfDTO.getDefaultWorkFolderPath()).append('\n');
		 sw.append(ConstantCodes.GENE_SET_FOLDER).append(" = ").append(cfDTO.getGeneSetPath()).append('\n');
		 sw.append(ConstantCodes.HTML_SOURCE_FOLDER).append(" = ").append(cfDTO.getHTMLSourcePath()).append('\n');
		 sw.append(ConstantCodes.MATLAB_FOLDER).append(" = ").append(cfDTO.getMATLABFolderPath()).append('\n');
		 sw.append(ConstantCodes.USE_DOCKER).append(" = ").append(""+cfDTO.isUseDocker()).append('\n');

		 
		 sw.append(ConstantCodes.GENE_PROPERTIES_FILE).append(" = ").append(cfDTO.getGenePropertiesFilePath()).append('\n');
		 sw.append(ConstantCodes.METAGENE_FOLDER).append(" = ").append(cfDTO.getMetaGeneFolderPath()).append('\n');
		 sw.append(ConstantCodes.NETWORK_UNDIRECTED_FILE).append(" = ").append(cfDTO.getNetworkUndirectedFilePath()).append('\n');
		 sw.append(ConstantCodes.NETWORK_DIRECTED_FILE).append(" = ").append(cfDTO.getNetworkDirectedFilePath()).append('\n');

		 sw.append(ConstantCodes.COMPUTE_ROBUST_STATISTICS).append(" = ").append(Boolean.toString(cfDTO.getComputeRobustStatistics())).append('\n');
		 sw.append(ConstantCodes.MIN_NUMBER_OF_DISTINCT_VALUES_IN_NUMERICALS).append(" = ").append(Integer.toString(cfDTO.getMinNumberOfDistinctValuesInNumericals())).append('\n');
		 sw.append(ConstantCodes.MIN_NUMBER_OF_SAMPLES_IN_CATEGORY).append(" = ").append(Integer.toString(cfDTO.getMinNumberOfSamplesInCategory())).append('\n');
		 sw.append(ConstantCodes.MAX_NUMBER_OF_CATEGORIES).append(" = ").append(Integer.toString(cfDTO.getMaxNumberOfCategories())).append('\n');
		 sw.append(ConstantCodes.ASSOCIATION_ANALYSIS_IN_THRESHOLD).append(" = ").append(Double.toString(cfDTO.getAssociationAnalysisInThreshold())).append('\n');
		 sw.append(ConstantCodes.MINIMAL_TOLERABLE_STABILITY).append(" = ").append(Double.toString(cfDTO.getMinimalTolerableStability())).append('\n');
		 
		 sw.append(ConstantCodes.ICA_APPROACH).append(" = ").append(cfDTO.getICAApproach()).append("\n");
		 sw.append(ConstantCodes.ICA_MEASURE).append(" = ").append(cfDTO.getICAMeasure()).append("\n");
		 sw.append(ConstantCodes.ICA_MAXNUMBER_ITERATIONS).append(" = ").append(Integer.toString(cfDTO.getICAMaxNumIterations())).append("\n");
		 
		 return sw.toString();
	}
	
	
	
}
