import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import model.ConfigDTO;
import model.ConstantCodes;
import util.ConfigHelper;


public class ConfigDialog extends JDialog  implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	private JTextField tfMatlabica, tfDefaultWork, tfGeneSet, tfHTMLSource,tfMATLABFolder,tfICAApproach, tfICAMeasure;
	private JTextField tfGenePropertiesFile, tfMetaGeneFolder, tfNetworkUndirectedFile, tfNetworkDirectedFile;
	private JCheckBox cbComputeRobustStatistics;
	private JCheckBox cbUseDocker;
	private JSpinner sMinNoOfDistinctValuesInNumericals,sMinNoOfSamplesInCategory,sMaxNoOfCategories,sAssociationAnalysisInThreshold, sMinimalTolerableStability, sICAMaxNumberOfIterations;
    private JButton btnSave,btnCancel;
    private ConfigHelper cfHelper;
    private JFileChooser fileChooser;
    private JButton btnMatlabica, btnDefaultWork, btnGeneSet, btnHTMLSource,btnMATLABFolder;
	private JButton btnGenePropertiesFile, btnMetaGeneFolder, btnNetworkUndirectedFile, btnNetworkDirectedFile;
	
    
	/**
	 * Create the dialog.
	 */
	public ConfigDialog(JFrame parent) 
	{
	    super(parent, "BiODICA Configuration", true);
        JPanel panel = new JPanel(new GridBagLayout());
        ImageIcon mainIcon = new ImageIcon(getClass().getResource("Config.png"));
		setIconImage(mainIcon.getImage());
        panel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        cfHelper = new ConfigHelper();
        
        ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
        
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        int RowNumber=0;
        
        // MATLABICA folder Path
        
        GridBagConstraints csLbMatlabica = new GridBagConstraints();
        csLbMatlabica.fill = GridBagConstraints.HORIZONTAL;
        csLbMatlabica.insets = new Insets(10,10,10,10);
        csLbMatlabica.gridx = 0;
        csLbMatlabica.gridy = RowNumber;
        csLbMatlabica.gridwidth = 1;
        JLabel lbUsername = new JLabel(ConstantCodes.MATLABICA_FOLDER);
        panel.add(lbUsername, csLbMatlabica);
 
        
        GridBagConstraints csTfMatlabica = new GridBagConstraints();
        csTfMatlabica.fill = GridBagConstraints.HORIZONTAL;
        csTfMatlabica.insets = new Insets(10,10,10,10);
        csTfMatlabica.gridx = 1;
        csTfMatlabica.gridy = RowNumber;
        csTfMatlabica.gridwidth = 2;
        tfMatlabica = new JTextField(50);
        tfMatlabica.setText(cfDTO.getMatlabicaFolderPath());
        panel.add(tfMatlabica, csTfMatlabica);
        

	 	GridBagConstraints csBtnMatlabica =  new GridBagConstraints();
	 	csBtnMatlabica.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnMatlabica.insets = new Insets(10,10,10,10);
	 	csBtnMatlabica.gridx = 3;
	 	csBtnMatlabica.gridy = RowNumber++;
	 	csBtnMatlabica.gridwidth = 1; 
	 	btnMatlabica = new JButton("Browse");
	 	btnMatlabica.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 			

	 	btnMatlabica.addActionListener(this);
	 	panel.add(btnMatlabica, csBtnMatlabica);

        
        // Default work folder path
        
        GridBagConstraints csLbDefaultWork = new GridBagConstraints();
        csLbDefaultWork.fill = GridBagConstraints.HORIZONTAL;
        csLbDefaultWork.insets = new Insets(10,10,10,10);
        csLbDefaultWork.gridx = 0;
        csLbDefaultWork.gridy = RowNumber;
        csLbDefaultWork.gridwidth = 1;
        JLabel lbDefaultWork = new JLabel(ConstantCodes.DEFAULT_WORK_FOLDER);
        panel.add(lbDefaultWork, csLbDefaultWork);
 

        GridBagConstraints csTfDefaultWork = new GridBagConstraints();
        csTfDefaultWork.fill = GridBagConstraints.HORIZONTAL;
        csTfDefaultWork.insets = new Insets(10,10,10,10);
        csTfDefaultWork.gridx = 1;
        csTfDefaultWork.gridy = RowNumber;
        csTfDefaultWork.gridwidth = 2;
        tfDefaultWork = new JTextField(50);
        tfDefaultWork.setText(cfDTO.getDefaultWorkFolderPath());
        panel.add(tfDefaultWork, csTfDefaultWork);
        
        
	 	GridBagConstraints csBtnDefaultWork =  new GridBagConstraints();
	 	csBtnDefaultWork.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnDefaultWork.insets = new Insets(10,10,10,10);
	 	csBtnDefaultWork.gridx = 3;
	 	csBtnDefaultWork.gridy = RowNumber++;
	 	csBtnDefaultWork.gridwidth = 1; 
	 	btnDefaultWork = new JButton("Browse");
	 	btnDefaultWork.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnDefaultWork.addActionListener(this);
	 	panel.add(btnDefaultWork, csBtnDefaultWork);
        
        
        // Gene Set Folder path
        
        GridBagConstraints csLbGeneSet = new GridBagConstraints();
        csLbGeneSet.fill = GridBagConstraints.HORIZONTAL;
        csLbGeneSet.insets = new Insets(10,10,10,10);
        csLbGeneSet.gridx = 0;
        csLbGeneSet.gridy = RowNumber;
        csLbGeneSet.gridwidth = 1;
        JLabel lbGeneSet = new JLabel(ConstantCodes.GENE_SET_FOLDER);
        panel.add(lbGeneSet, csLbGeneSet);
        
        GridBagConstraints csTfGeneSet = new GridBagConstraints();
        csTfGeneSet.fill = GridBagConstraints.HORIZONTAL;
        csTfGeneSet.insets = new Insets(10,10,10,10);
        csTfGeneSet.gridx = 1;
        csTfGeneSet.gridy = RowNumber;
        csTfGeneSet.gridwidth = 2;
        tfGeneSet = new JTextField(50);
        tfGeneSet.setText(cfDTO.getGeneSetPath());
        panel.add(tfGeneSet, csTfGeneSet);
        
        
        
  	 	GridBagConstraints csBtnGeneSet =  new GridBagConstraints();
  	 	csBtnGeneSet.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnGeneSet.insets = new Insets(10,10,10,10);
  	 	csBtnGeneSet.gridx = 3;
  	 	csBtnGeneSet.gridy = RowNumber++;
  	 	csBtnGeneSet.gridwidth = 1; 
  	 	btnGeneSet = new JButton("Browse");
	 	btnGeneSet.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnGeneSet.addActionListener(this);
  	 	panel.add(btnGeneSet, csBtnGeneSet);
        

        // HTML Source Folder path
        
        GridBagConstraints csLbHTMLSource = new GridBagConstraints();
        csLbHTMLSource.fill = GridBagConstraints.HORIZONTAL;
        csLbHTMLSource.insets = new Insets(10,10,10,10);
        JLabel lbHTMLSource = new JLabel(ConstantCodes.HTML_SOURCE_FOLDER);
        csLbHTMLSource.gridx = 0;
        csLbHTMLSource.gridy = RowNumber;
        csLbGeneSet.gridwidth = 1;
        panel.add(lbHTMLSource, csLbHTMLSource);
        
        
        GridBagConstraints csTfHTMLSource = new GridBagConstraints();
        csTfHTMLSource.fill = GridBagConstraints.HORIZONTAL;
        csTfHTMLSource.insets = new Insets(10,10,10,10);
        tfHTMLSource = new JTextField(50);
        csTfHTMLSource.gridx = 1;
        csTfHTMLSource.gridy = RowNumber;
        csTfHTMLSource.gridwidth = 2;
        tfHTMLSource.setText(cfDTO.getHTMLSourcePath());
        panel.add(tfHTMLSource, csTfHTMLSource);
        
        
  	 	GridBagConstraints csBtnHTMLSource =  new GridBagConstraints();
  	 	csBtnHTMLSource.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnHTMLSource.insets = new Insets(10,10,10,10);
  	 	csBtnHTMLSource.gridx = 3;
  	 	csBtnHTMLSource.gridy = RowNumber++;
  	 	csBtnHTMLSource.gridwidth = 1; 
  	 	btnHTMLSource = new JButton("Browse");
	 	btnHTMLSource.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnHTMLSource.addActionListener(this);
  	 	panel.add(btnHTMLSource, csBtnHTMLSource);
  	 	
  	 	
  	 	// MATLABFolder Folder path
        
        GridBagConstraints csMATLABFolder = new GridBagConstraints();
        csMATLABFolder.fill = GridBagConstraints.HORIZONTAL;
        csMATLABFolder.insets = new Insets(10,10,10,10);
        csMATLABFolder.gridx = 0;
        csMATLABFolder.gridy = RowNumber;
        csMATLABFolder.gridwidth = 1;
        JLabel lbcsMATLABFolder = new JLabel(ConstantCodes.MATLAB_FOLDER);
        panel.add(lbcsMATLABFolder, csMATLABFolder);
        
        
        GridBagConstraints csTfMATLABFolder = new GridBagConstraints();
        csTfMATLABFolder.fill = GridBagConstraints.HORIZONTAL;
        csTfMATLABFolder.insets = new Insets(10,10,10,10);
        csTfMATLABFolder.gridx = 1;
        csTfMATLABFolder.gridy = RowNumber;
        csTfMATLABFolder.gridwidth = 2;
        tfMATLABFolder = new JTextField(50);
        tfMATLABFolder.setText(cfDTO.getMATLABFolderPath());
        panel.add(tfMATLABFolder, csTfMATLABFolder);
        
        
  	 	GridBagConstraints csBtnMATLABFolder =  new GridBagConstraints();
  	 	csBtnMATLABFolder.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnMATLABFolder.insets = new Insets(10,10,10,10);
  	 	csBtnMATLABFolder.gridx = 3;
  	 	csBtnMATLABFolder.gridy = RowNumber++;
  	 	csBtnMATLABFolder.gridwidth = 1; 
  	 	btnMATLABFolder = new JButton("Browse");
  	 	btnMATLABFolder.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnMATLABFolder.addActionListener(this);
  	 	panel.add(btnMATLABFolder, csBtnMATLABFolder);

  	 	RowNumber++;

        GridBagConstraints csUseDocker = new GridBagConstraints();
        csUseDocker.fill = GridBagConstraints.HORIZONTAL;
        csUseDocker.insets = new Insets(10,10,10,10);
        csUseDocker.gridx = 0;
        csUseDocker.gridy = RowNumber;
        csUseDocker.gridwidth = 1;
        JLabel lbcsUseDocker = new JLabel(ConstantCodes.USE_DOCKER);
        panel.add(lbcsUseDocker, csUseDocker);
        
        
        GridBagConstraints csTfUseDocker = new GridBagConstraints();
        csTfUseDocker.fill = GridBagConstraints.HORIZONTAL;
        csTfUseDocker.insets = new Insets(10,10,10,10);
        csTfUseDocker.gridx = 1;
        csTfUseDocker.gridy = RowNumber++;
        csTfUseDocker.gridwidth = 2;
        cbUseDocker = new JCheckBox();
        cbUseDocker.setSelected(cfDTO.isUseDocker()); 
        panel.add(cbUseDocker, csTfUseDocker);
        
        	 	
  	 	
  	 	// Gene Properties File

  	 	
        GridBagConstraints csGenePropFile = new GridBagConstraints();
        csGenePropFile.fill = GridBagConstraints.HORIZONTAL;
        csGenePropFile.insets = new Insets(10,10,10,10);
        csGenePropFile.gridx = 0;
        csGenePropFile.gridy = RowNumber;
        csGenePropFile.gridwidth = 1;
        JLabel lbcsGenePropFile = new JLabel(ConstantCodes.GENE_PROPERTIES_FILE);
        panel.add(lbcsGenePropFile, csGenePropFile);
        
        
        GridBagConstraints csTfGenePropFile = new GridBagConstraints();
        csTfGenePropFile.fill = GridBagConstraints.HORIZONTAL;
        csTfGenePropFile.insets = new Insets(10,10,10,10);
        csTfGenePropFile.gridx = 1;
        csTfGenePropFile.gridy = RowNumber;
        csTfGenePropFile.gridwidth = 2;
        tfGenePropertiesFile = new JTextField(50);
        tfGenePropertiesFile.setText(cfDTO.getGenePropertiesFilePath());
        panel.add(tfGenePropertiesFile, csTfGenePropFile);
        
        
  	 	GridBagConstraints csBtnGenePropFile =  new GridBagConstraints();
  	 	csBtnGenePropFile.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnGenePropFile.insets = new Insets(10,10,10,10);
  	 	csBtnGenePropFile.gridx = 3;
  	 	csBtnGenePropFile.gridy = RowNumber++;
  	 	csBtnGenePropFile.gridwidth = 1; 
  	 	btnGenePropertiesFile = new JButton("Browse");
  	 	btnGenePropertiesFile.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnGenePropertiesFile.addActionListener(this);
  	 	panel.add(btnGenePropertiesFile, csBtnGenePropFile);

  	 	// MetaGene folder
        
        GridBagConstraints csMetaGeneFolder = new GridBagConstraints();
        csMetaGeneFolder.fill = GridBagConstraints.HORIZONTAL;
        csMetaGeneFolder.insets = new Insets(10,10,10,10);
        csMetaGeneFolder.gridx = 0;
        csMetaGeneFolder.gridy = RowNumber;
        csMetaGeneFolder.gridwidth = 1;
        JLabel lbcsMetaGeneFolder = new JLabel(ConstantCodes.METAGENE_FOLDER);
        panel.add(lbcsMetaGeneFolder, csMetaGeneFolder);
        
        
        GridBagConstraints csTfMetaGeneFolder = new GridBagConstraints();
        csTfMetaGeneFolder.fill = GridBagConstraints.HORIZONTAL;
        csTfMetaGeneFolder.insets = new Insets(10,10,10,10);
        csTfMetaGeneFolder.gridx = 1;
        csTfMetaGeneFolder.gridy = RowNumber;
        csTfMetaGeneFolder.gridwidth = 2;
        tfMetaGeneFolder = new JTextField(50);
        tfMetaGeneFolder.setText(cfDTO.getMetaGeneFolderPath());
        panel.add(tfMetaGeneFolder, csTfMetaGeneFolder);
        
        
  	 	GridBagConstraints csBtnMetaGeneFolder =  new GridBagConstraints();
  	 	csBtnMetaGeneFolder.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnMetaGeneFolder.insets = new Insets(10,10,10,10);
  	 	csBtnMetaGeneFolder.gridx = 3;
  	 	csBtnMetaGeneFolder.gridy = RowNumber++;
  	 	csBtnMetaGeneFolder.gridwidth = 1; 
  	 	btnMetaGeneFolder = new JButton("Browse");
  	 	btnMetaGeneFolder.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnMetaGeneFolder.addActionListener(this);
  	 	panel.add(btnMetaGeneFolder, csBtnMetaGeneFolder);
  	 	

  	 	// NetworkUndirectedFile
  	 	
        GridBagConstraints csNetworkUndirectedFile = new GridBagConstraints();
        csNetworkUndirectedFile.fill = GridBagConstraints.HORIZONTAL;
        csNetworkUndirectedFile.insets = new Insets(10,10,10,10);
        csNetworkUndirectedFile.gridx = 0;
        csNetworkUndirectedFile.gridy = RowNumber;
        csNetworkUndirectedFile.gridwidth = 1;
        JLabel lbcsNetworkUndirectedFile = new JLabel(ConstantCodes.NETWORK_UNDIRECTED_FILE);
        panel.add(lbcsNetworkUndirectedFile, csNetworkUndirectedFile);
        
        
        GridBagConstraints csTfNetworkUndirectedFile = new GridBagConstraints();
        csTfNetworkUndirectedFile.fill = GridBagConstraints.HORIZONTAL;
        csTfNetworkUndirectedFile.insets = new Insets(10,10,10,10);
        csTfNetworkUndirectedFile.gridx = 1;
        csTfNetworkUndirectedFile.gridy = RowNumber;
        csTfNetworkUndirectedFile.gridwidth = 2;
        tfNetworkUndirectedFile = new JTextField(50);
        tfNetworkUndirectedFile.setText(cfDTO.getNetworkUndirectedFilePath());
        panel.add(tfNetworkUndirectedFile, csTfNetworkUndirectedFile);
        
        
  	 	GridBagConstraints csBtnNetworkUndirectedFile =  new GridBagConstraints();
  	 	csBtnNetworkUndirectedFile.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnNetworkUndirectedFile.insets = new Insets(10,10,10,10);
  	 	csBtnNetworkUndirectedFile.gridx = 3;
  	 	csBtnNetworkUndirectedFile.gridy = RowNumber++;
  	 	csBtnNetworkUndirectedFile.gridwidth = 1; 
  	 	btnNetworkUndirectedFile = new JButton("Browse");
  	 	btnNetworkUndirectedFile.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnNetworkUndirectedFile.addActionListener(this);
  	 	panel.add(btnNetworkUndirectedFile, csBtnNetworkUndirectedFile);
  	 	  	 	
  	 	// NetworkDirectedFile
  	 	
        GridBagConstraints csNetworkDirectedFile = new GridBagConstraints();
        csNetworkDirectedFile.fill = GridBagConstraints.HORIZONTAL;
        csNetworkDirectedFile.insets = new Insets(10,10,10,10);
        csNetworkDirectedFile.gridx = 0;
        csNetworkDirectedFile.gridy = RowNumber;
        csNetworkDirectedFile.gridwidth = 1;
        JLabel lbcsNetworkDirectedFile = new JLabel(ConstantCodes.NETWORK_DIRECTED_FILE);
        panel.add(lbcsNetworkDirectedFile, csNetworkDirectedFile);
        
        
        GridBagConstraints csTfNetworkDirectedFile = new GridBagConstraints();
        csTfNetworkDirectedFile.fill = GridBagConstraints.HORIZONTAL;
        csTfNetworkDirectedFile.insets = new Insets(10,10,10,10);
        csTfNetworkDirectedFile.gridx = 1;
        csTfNetworkDirectedFile.gridy = RowNumber;
        csTfNetworkDirectedFile.gridwidth = 2;
        tfNetworkDirectedFile = new JTextField(50);
        tfNetworkDirectedFile.setText(cfDTO.getNetworkDirectedFilePath());
        panel.add(tfNetworkDirectedFile, csTfNetworkDirectedFile);
        
        
  	 	GridBagConstraints csBtnNetworkDirectedFile =  new GridBagConstraints();
  	 	csBtnNetworkDirectedFile.fill = GridBagConstraints.HORIZONTAL;
  	 	csBtnNetworkDirectedFile.insets = new Insets(10,10,10,10);
  	 	csBtnNetworkDirectedFile.gridx = 3;
  	 	csBtnNetworkDirectedFile.gridy = RowNumber++;
  	 	csBtnNetworkDirectedFile.gridwidth = 1; 
  	 	btnNetworkDirectedFile = new JButton("Browse");
  	 	btnNetworkDirectedFile.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
  	 	btnNetworkDirectedFile.addActionListener(this);
  	 	panel.add(btnNetworkDirectedFile, csBtnNetworkDirectedFile);
  	 	  

  	 	// Compute Robust Statistics 
        
        GridBagConstraints csLbComputeRobustStatistics = new GridBagConstraints();
        csLbComputeRobustStatistics.fill = GridBagConstraints.HORIZONTAL;
        csLbComputeRobustStatistics.insets = new Insets(10,10,10,10);
        JLabel lbComputeRobustStatistics = new JLabel(ConstantCodes.COMPUTE_ROBUST_STATISTICS);
        csLbComputeRobustStatistics.gridx = 0;
        csLbComputeRobustStatistics.gridy = RowNumber;
        csLbComputeRobustStatistics.gridwidth = 1;
        panel.add(lbComputeRobustStatistics, csLbComputeRobustStatistics);
        
        
        GridBagConstraints csCbComputeRobustStatistics = new GridBagConstraints();
        csCbComputeRobustStatistics.fill = GridBagConstraints.HORIZONTAL;
        csCbComputeRobustStatistics.insets = new Insets(10,10,10,10);
        cbComputeRobustStatistics = new JCheckBox();
        cbComputeRobustStatistics.setHorizontalAlignment(SwingConstants.CENTER);
        csCbComputeRobustStatistics.gridx = 1;
        csCbComputeRobustStatistics.gridy = RowNumber++;
        csCbComputeRobustStatistics.gridwidth = 2;
        cbComputeRobustStatistics.setSelected(cfDTO.getComputeRobustStatistics());
        panel.add(cbComputeRobustStatistics, csCbComputeRobustStatistics);
        
        
        // Minimum Number of Distinct values in numericals
        
        GridBagConstraints csLbMinNoOfDistinctValuesInNumbericals = new GridBagConstraints();
        csLbMinNoOfDistinctValuesInNumbericals.fill = GridBagConstraints.HORIZONTAL;
        csLbMinNoOfDistinctValuesInNumbericals.insets = new Insets(10,10,10,10);
        JLabel lbMinNoOfDistinctValuesInNumbericals = new JLabel(ConstantCodes.MIN_NUMBER_OF_DISTINCT_VALUES_IN_NUMERICALS);
        csLbMinNoOfDistinctValuesInNumbericals.gridx = 0;
        csLbMinNoOfDistinctValuesInNumbericals.gridy = RowNumber;
        csLbMinNoOfDistinctValuesInNumbericals.gridwidth = 1;
        panel.add(lbMinNoOfDistinctValuesInNumbericals, csLbMinNoOfDistinctValuesInNumbericals);

        
        GridBagConstraints cSMinNoOfDistinctValuesInNumericals = new GridBagConstraints();     
        cSMinNoOfDistinctValuesInNumericals.fill = GridBagConstraints.VERTICAL;
        cSMinNoOfDistinctValuesInNumericals.insets = new Insets(10,10,10,10);
        sMinNoOfDistinctValuesInNumericals = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 99999.0,
                1));
        cSMinNoOfDistinctValuesInNumericals.gridx = 2;
        cSMinNoOfDistinctValuesInNumericals.gridy = RowNumber++;
        cSMinNoOfDistinctValuesInNumericals.weightx = 1.0;
        sMinNoOfDistinctValuesInNumericals.setValue(cfDTO.getMinNumberOfDistinctValuesInNumericals());
        JFormattedTextField sMinNoOfDistinctValuesInNumericalsTxt = ((JSpinner.NumberEditor) sMinNoOfDistinctValuesInNumericals.getEditor()).getTextField();
        ((NumberFormatter) sMinNoOfDistinctValuesInNumericalsTxt.getFormatter()).setAllowsInvalid(false);
        panel.add(sMinNoOfDistinctValuesInNumericals, cSMinNoOfDistinctValuesInNumericals);
       
        
        // Minimum Number of Samples in Category
        
        GridBagConstraints csLbMinNoOfSamplesInCategory = new GridBagConstraints();
        csLbMinNoOfSamplesInCategory.fill = GridBagConstraints.HORIZONTAL;
        csLbMinNoOfSamplesInCategory.insets = new Insets(10,10,10,10);
        JLabel lbMinNoOfSamplesInCategory = new JLabel(ConstantCodes.MIN_NUMBER_OF_SAMPLES_IN_CATEGORY);
        csLbMinNoOfSamplesInCategory.gridx = 0;
        csLbMinNoOfSamplesInCategory.gridy = RowNumber;
        csLbMinNoOfSamplesInCategory.gridwidth = 1;
        panel.add(lbMinNoOfSamplesInCategory, csLbMinNoOfSamplesInCategory);

        
        
        GridBagConstraints cSMinNoOfSamplesInCategory = new GridBagConstraints();     
        cSMinNoOfSamplesInCategory.fill = GridBagConstraints.VERTICAL;
        cSMinNoOfSamplesInCategory.insets = new Insets(10,10,10,10);
        sMinNoOfSamplesInCategory = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 99999.0,
                1));
        cSMinNoOfSamplesInCategory.gridx = 2;
        cSMinNoOfSamplesInCategory.gridy = RowNumber++;
        cSMinNoOfSamplesInCategory.weightx = 1.0;
        sMinNoOfSamplesInCategory.setValue(cfDTO.getMinNumberOfSamplesInCategory());
        JFormattedTextField sMinNoOfSamplesInCategoryTxt = ((JSpinner.NumberEditor) sMinNoOfSamplesInCategory.getEditor()).getTextField();
        ((NumberFormatter) sMinNoOfSamplesInCategoryTxt.getFormatter()).setAllowsInvalid(false);
        panel.add(sMinNoOfSamplesInCategory, cSMinNoOfSamplesInCategory);
       
        // Maximum Number of Categories
        
        GridBagConstraints csLbMaxNoOfCategories = new GridBagConstraints();
        csLbMaxNoOfCategories.fill = GridBagConstraints.HORIZONTAL;
        csLbMaxNoOfCategories.insets = new Insets(10,10,10,10);
        JLabel lbMaxNoOfCategories = new JLabel(ConstantCodes.MAX_NUMBER_OF_CATEGORIES);
        csLbMaxNoOfCategories.gridx = 0;
        csLbMaxNoOfCategories.gridy = RowNumber;
        csLbMaxNoOfCategories.gridwidth = 1;
        panel.add(lbMaxNoOfCategories, csLbMaxNoOfCategories);
        
        
        
        GridBagConstraints cSMaxNoOfCategories = new GridBagConstraints();     
        cSMaxNoOfCategories.fill = GridBagConstraints.VERTICAL;
        cSMaxNoOfCategories.insets = new Insets(10,10,10,10);
        sMaxNoOfCategories = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 99999.0,
                1));
        cSMaxNoOfCategories.gridx = 2;
        cSMaxNoOfCategories.gridy = RowNumber++;
        cSMaxNoOfCategories.weightx = 1.0;
        sMaxNoOfCategories.setValue(cfDTO.getMaxNumberOfCategories());
        JFormattedTextField sMaxNoOfCategoriesTxt = ((JSpinner.NumberEditor) sMaxNoOfCategories.getEditor()).getTextField();
        ((NumberFormatter) sMaxNoOfCategoriesTxt.getFormatter()).setAllowsInvalid(false);
        panel.add(sMaxNoOfCategories, cSMaxNoOfCategories);
        
        // Association analysis in threshold
        
        GridBagConstraints csLbAssociationAnalysisInThreshold = new GridBagConstraints();
        csLbAssociationAnalysisInThreshold.fill = GridBagConstraints.HORIZONTAL;
        csLbAssociationAnalysisInThreshold.insets = new Insets(10,10,10,10);
        JLabel lbAssociationAnalysisInThreshold = new JLabel(ConstantCodes.ASSOCIATION_ANALYSIS_IN_THRESHOLD);
        csLbAssociationAnalysisInThreshold.gridx = 0;
        csLbAssociationAnalysisInThreshold.gridy = RowNumber;
        csLbAssociationAnalysisInThreshold.gridwidth = 1;
        panel.add(lbAssociationAnalysisInThreshold, csLbAssociationAnalysisInThreshold);
        
        
        
        GridBagConstraints cSAssociationAnalysisInThreshold = new GridBagConstraints();     
        cSAssociationAnalysisInThreshold.fill = GridBagConstraints.VERTICAL;
        cSAssociationAnalysisInThreshold.insets = new Insets(10,10,10,10);
        sAssociationAnalysisInThreshold = new JSpinner(new SpinnerNumberModel((double)0.0, 0.0, 99999.0,
                0.1));
        cSAssociationAnalysisInThreshold.gridx = 2;
        cSAssociationAnalysisInThreshold.gridy = RowNumber++;
        cSAssociationAnalysisInThreshold.weightx = 1.0;
        sAssociationAnalysisInThreshold.setValue(cfDTO.getAssociationAnalysisInThreshold());
        JFormattedTextField sAssociationAnalysisInThresholdTxt = ((JSpinner.NumberEditor) sAssociationAnalysisInThreshold.getEditor()).getTextField();
        NumberFormatter sAssociationAnalysisInThresholdformatter = (NumberFormatter) sAssociationAnalysisInThresholdTxt.getFormatter();
        DecimalFormat sAssociationAnalysisInThresholddecimalFormat = new DecimalFormat("0.0"); 
        sAssociationAnalysisInThresholdformatter.setFormat(sAssociationAnalysisInThresholddecimalFormat); 
        sAssociationAnalysisInThresholdformatter.setAllowsInvalid(false);
        
        panel.add(sAssociationAnalysisInThreshold, cSAssociationAnalysisInThreshold);
        
       
        // Minimal Tolerable Stability 
        
        GridBagConstraints csLbMinimalTolerableStability = new GridBagConstraints();
        csLbMinimalTolerableStability.fill = GridBagConstraints.HORIZONTAL;
        csLbMinimalTolerableStability.insets = new Insets(10,10,10,10);
        JLabel lbMinimalTolerableStability = new JLabel(ConstantCodes.MINIMAL_TOLERABLE_STABILITY);
        csLbMinimalTolerableStability.gridx = 0;
        csLbMinimalTolerableStability.gridy = RowNumber;
        csLbMinimalTolerableStability.gridwidth = 1;
        panel.add(lbMinimalTolerableStability, csLbMinimalTolerableStability);
        
        
        GridBagConstraints cSMinimalTolerableStability = new GridBagConstraints();     
        cSMinimalTolerableStability.fill = GridBagConstraints.VERTICAL;
        cSMinimalTolerableStability.insets = new Insets(10,10,10,10);
        cSMinimalTolerableStability.gridx = 2;
        cSMinimalTolerableStability.gridy = RowNumber++;
        cSMinimalTolerableStability.weightx = 1.0;
        
        sMinimalTolerableStability = new JSpinner(new SpinnerNumberModel((double)0.0, 0.0, 99999.0,
                0.1));
        sMinimalTolerableStability.setValue(cfDTO.getMinimalTolerableStability());
        JFormattedTextField  sMinimalTolerableStabilityTxt = ((JSpinner.NumberEditor) sMinimalTolerableStability.getEditor()).getTextField();
        NumberFormatter sMinimalTolerableStabilityformatter = (NumberFormatter) sMinimalTolerableStabilityTxt.getFormatter();
        DecimalFormat sMinimalTolerableStabilityformatterdecimalFormat = new DecimalFormat("0.0"); 
        sMinimalTolerableStabilityformatter.setFormat(sMinimalTolerableStabilityformatterdecimalFormat); 
        sMinimalTolerableStabilityformatter.setAllowsInvalid(false);
        panel.add(sMinimalTolerableStability, cSMinimalTolerableStability);
        
        // ICA Approach
        
        GridBagConstraints csLbICAApproach = new GridBagConstraints();
        csLbICAApproach.fill = GridBagConstraints.HORIZONTAL;
        csLbICAApproach.insets = new Insets(10,10,10,10);
        JLabel lbICAApproach= new JLabel(ConstantCodes.ICA_APPROACH);
        csLbICAApproach.gridx = 0;
        csLbICAApproach.gridy = RowNumber;
        csLbICAApproach.gridwidth = 1;
        panel.add(lbICAApproach, csLbICAApproach);
        
        GridBagConstraints csICAApproach = new GridBagConstraints();
        csICAApproach.fill = GridBagConstraints.HORIZONTAL;
        csICAApproach.insets = new Insets(10,10,10,10);
        csICAApproach.gridx = 1;
        csICAApproach.gridy = RowNumber++;
        csICAApproach.gridwidth = 2;
        tfICAApproach = new JTextField(50);
        tfICAApproach.setText(cfDTO.getICAApproach());
        panel.add(tfICAApproach, csICAApproach);
        
        
        // ICA Measure
        
        GridBagConstraints csLbICAMeasure = new GridBagConstraints();
        csLbICAMeasure.fill = GridBagConstraints.HORIZONTAL;
        csLbICAMeasure.insets = new Insets(10,10,10,10);
        JLabel lbICAMeasure= new JLabel(ConstantCodes.ICA_MEASURE);
        csLbICAMeasure.gridx = 0;
        csLbICAMeasure.gridy = RowNumber;
        csLbICAMeasure.gridwidth = 1;
        panel.add(lbICAMeasure, csLbICAMeasure);
        
        GridBagConstraints csICAMeasure = new GridBagConstraints();
        csICAMeasure.fill = GridBagConstraints.HORIZONTAL;
        csICAMeasure.insets = new Insets(10,10,10,10);
        csICAMeasure.gridx = 1;
        csICAMeasure.gridy = RowNumber++;
        csICAMeasure.gridwidth = 2;
        tfICAMeasure = new JTextField(50);
        tfICAMeasure.setText(cfDTO.getICAMeasure());
        panel.add(tfICAMeasure, csICAMeasure);

        // ICA Max Number Of Iterations
        
        GridBagConstraints csLbICAMaxNumberOfIterations = new GridBagConstraints();
        csLbICAMaxNumberOfIterations.fill = GridBagConstraints.HORIZONTAL;
        csLbICAMaxNumberOfIterations.insets = new Insets(10,10,10,10);
        JLabel lbICAMaxNumberOfIterations = new JLabel(ConstantCodes.ICA_MAXNUMBER_ITERATIONS);
        csLbICAMaxNumberOfIterations.gridx = 0;
        csLbICAMaxNumberOfIterations.gridy = RowNumber;
        csLbICAMaxNumberOfIterations.gridwidth = 1;
        panel.add(lbICAMaxNumberOfIterations, csLbICAMaxNumberOfIterations);
        
        
        GridBagConstraints cSICAMaxNumberOfIterations = new GridBagConstraints();     
        cSICAMaxNumberOfIterations.fill = GridBagConstraints.VERTICAL;
        cSICAMaxNumberOfIterations.insets = new Insets(10,10,10,10);
        cSICAMaxNumberOfIterations.gridx = 2;
        cSICAMaxNumberOfIterations.gridy = RowNumber++;
        cSICAMaxNumberOfIterations.weightx = 1.0;
        
        sICAMaxNumberOfIterations = new JSpinner(new SpinnerNumberModel((int)0, 0, 99999,
                10));
        sICAMaxNumberOfIterations.setValue(cfDTO.getICAMaxNumIterations());
        JFormattedTextField  sICAMaxNumberOfIterationsTxt = ((JSpinner.NumberEditor) sICAMaxNumberOfIterations.getEditor()).getTextField();
        NumberFormatter sICAMaxNumberOfIterationsformatter = (NumberFormatter) sICAMaxNumberOfIterationsTxt.getFormatter();
        DecimalFormat sICAMaxNumberOfIterationsformatterdecimalFormat = new DecimalFormat("0"); 
        sICAMaxNumberOfIterationsformatter.setFormat(sICAMaxNumberOfIterationsformatterdecimalFormat); 
        sICAMaxNumberOfIterationsformatter.setAllowsInvalid(false);
        panel.add(sICAMaxNumberOfIterations, cSICAMaxNumberOfIterations);        

        
        
        GridBagConstraints cSLine = new GridBagConstraints();     
        cSLine.fill = GridBagConstraints.VERTICAL;
        cSLine.insets = new Insets(10,10,10,10);
        cSLine.gridx = 3;
        cSLine.gridy = RowNumber++;
        cSLine.weightx = 1.0;
        
        // Save and Cancel buttons
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
       
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        JPanel bp = new JPanel();
        bp.add(btnSave);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(parent);
      
	}
	
	public String getMatlabicaFolderPath() {
        return tfMatlabica.getText().trim();
    }
	
	
	public String getDefaultWorkFolderPath() {
        return tfDefaultWork.getText().trim();
    }
	
	public String getGeneSetFolderPath()
	{
		return tfGeneSet.getText().trim();
	}
	
	public String getMATLABFolderPath()
	{
		return tfMATLABFolder.getText().trim();
	}
	
	public boolean isUseDocker(){
		return cbUseDocker.isSelected();
	}

	public String getGenePropertiesFilePath()
	{
		return tfGenePropertiesFile.getText().trim();
	}
	
	public String getMetaGeneFolderPath()
	{
		return tfMetaGeneFolder.getText().trim();
	}
	
	public String getNetworkUndirectedFilePath()
	{
		return tfNetworkUndirectedFile.getText().trim();
	}
	public String getNetworkDirectedFilePath()
	{
		return tfNetworkDirectedFile.getText().trim();
	}
	
	
	
	
	public String getHTMLSourceFolderPath()
	{
		return tfHTMLSource.getText().trim();
	}
	
	public boolean getComputeRobustStatistics()
	{
		return cbComputeRobustStatistics.isSelected();
	}
	
	public int getMinNumberOfDistinctValuesInNumericals()
	{
		try {
			sMinNoOfDistinctValuesInNumericals.commitEdit();
		} catch ( java.text.ParseException e ) {}
		
		return ((Double)sMinNoOfDistinctValuesInNumericals.getValue()).intValue();
	}
	
	
	public int getMinNumberOfSamplesInCategory()
	{
		try {
			sMinNoOfSamplesInCategory.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return ((Double)sMinNoOfSamplesInCategory.getValue()).intValue();
	}
	
	
	public int getMaxNumberOfCategories()
	{
		try {
			sMaxNoOfCategories.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return ((Double)sMaxNoOfCategories.getValue()).intValue();
	}

	public double getAssociationAnalysisInThreshold()
	{
		try {
			sAssociationAnalysisInThreshold.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return (Double)sAssociationAnalysisInThreshold.getValue();
	}
	
	public double getMinimalTolerableStability()
	{
		try {
			sMinimalTolerableStability.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return (Double)sMinimalTolerableStability.getValue();
	}
	
	public String getICAApproach()
	{
		return tfICAApproach.getText().trim();
	}

	public String getICAMeasure()
	{
		return tfICAMeasure.getText().trim();
	}
	
	public int getICAMaxNumIterations()
	{
		return (Integer)sICAMaxNumberOfIterations.getValue();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		if(e.getSource() == btnMatlabica)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfMatlabica.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnDefaultWork)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfDefaultWork.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnGeneSet)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfGeneSet.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnHTMLSource)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfHTMLSource.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		
		else if(e.getSource() == btnMATLABFolder)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfMATLABFolder.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnGenePropertiesFile)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfGenePropertiesFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		
		else if(e.getSource() == btnMetaGeneFolder)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfMetaGeneFolder.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnNetworkUndirectedFile)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfNetworkUndirectedFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnNetworkDirectedFile)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfNetworkDirectedFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnSave)
		{
			ConfigDTO configDTO = new ConfigDTO();
			configDTO.setMatlabicaFolderPath(getMatlabicaFolderPath());
			configDTO.setDefaultWorkFolderPath(getDefaultWorkFolderPath());
			configDTO.setGeneSetPath(getGeneSetFolderPath());
			configDTO.setHTMLSourcePath(getHTMLSourceFolderPath());
			configDTO.setMATLABFolderPath(getMATLABFolderPath());
			configDTO.setUseDocker(isUseDocker());
			configDTO.setGenePropertiesFilePath(getGenePropertiesFilePath());
			configDTO.setMetaGeneFolderPath(getMetaGeneFolderPath());
			configDTO.setNetworkUndirectedFilePath(getNetworkUndirectedFilePath());
			configDTO.setNetworkDirectedFilePath(getNetworkDirectedFilePath());
			configDTO.setComputeRobustStatistics(getComputeRobustStatistics());
			configDTO.setMinNumberOfDistinctValuesInNumericals(getMinNumberOfDistinctValuesInNumericals());
			configDTO.setMinNumberOfSamplesInCategory(getMinNumberOfSamplesInCategory());
			configDTO.setMaxNumberOfCategories(getMaxNumberOfCategories());
			configDTO.setAssociationAnalysisInThreshold(getAssociationAnalysisInThreshold());
			configDTO.setMinimalTolerableStability(getMinimalTolerableStability());
			configDTO.setICAApproach(getICAApproach());
			configDTO.setICAMeasure(getICAMeasure());
			configDTO.setICAMaxNumIterations(getICAMaxNumIterations());
			cfHelper.updateFoldersPathValuesInConfigFile(configDTO);
			dispose();
		}
		else if(e.getSource() == btnCancel)
		{
			dispose();
		}
	}

}
