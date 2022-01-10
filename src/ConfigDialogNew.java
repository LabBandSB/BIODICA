import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.ConfigDTO;
import util.ConfigHelper;
import util.ConfigHelper;


import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class ConfigDialogNew extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private JTextField tfDefaultWorkFolder;
	private JTextField tfGeneSet;
	private JTextField tfHTMLSource;
	private JTextField tfMetaGeneFolder;
	private JTextField tfGenePropertiesFile;
	private JTextField tfNetworkUndirectedFile;
	private JTextField tfNetworkDirectedFile;
	private JTextField tfMatlabica;
	
    private ConfigHelper cfHelper;
    private JFileChooser fileChooser;
    
    private JButton btnBrowseWorkFolder;
    private JButton btnMATLABFolder;
    private JButton btnGeneSetFolder;    
    private JButton btnSave;
    private JButton btnCancel; 
    private JButton btnMatlabica;
    private JButton btnHTMLSource;
    private JButton btnGenePropertiesFile;
    private JButton btnPythonCodeFolder;
    private JButton btnNetworkUndirectedFile; 
    private JButton btnMetaGeneFolder;
    private JButton btnNetworkDirectedFile;
    private JTextField tfMATLABFolder;
    private JTextField tfPythonCodeFolder;
    
    private JCheckBox cbUseDocker; 
    private JCheckBox cbComputeRobustStatistics;
    private JSpinner sMinNoOfDistinctValuesInNumericals;
    private JSpinner sMinNoOfSamplesInCategory;
    private JSpinner sMaxNoOfCategories;
    private JFormattedTextField sAssociationAnalysisInThreshold;
    private JFormattedTextField sMinimalTolerableStability; 
    
    private JComboBox cmbMATLABNonlinearity;
    private JComboBox cmbMATLABICAApproach;
    
    private JFormattedTextField tfMATLABMaxNumberOfIterations; 
    private JFormattedTextField tfMATLABNumberOfICARuns; 
    
    private JRadioButton rbtnUsePython;
    private JRadioButton rbtnUseMATLAB; 
    
    private JComboBox cmbPythonICAApproach; 
    private JComboBox cmbPythonNonlinearity;
    private JFormattedTextField tfPythonMaxNumberOfIterations;
    private JFormattedTextField tfPythonNumberOfICARuns;
    private JComboBox cmbPythonVisualizationMethod;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//ConfigDialogNew dialog = new ConfigDialogNew();
			//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigDialogNew(JFrame parent) {
				
        cfHelper = new ConfigHelper();
        ConfigDTO cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		
		setBounds(100, 100, 761, 574);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(372, 10, 5, 5);
			contentPanel.add(tabbedPane);
		}
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tabbedPane.setBounds(10, 10, 729, 486);
		contentPanel.add(tabbedPane);
		
		JPanel panelFolder = new JPanel();
		tabbedPane.addTab("Folders and files", null, panelFolder, null);
		panelFolder.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Geneset folder");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(42, 46, 183, 14);
		panelFolder.add(lblNewLabel);
		
		tfDefaultWorkFolder = new JTextField();
		tfDefaultWorkFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfDefaultWorkFolder.setBounds(155, 11, 349, 22);
		panelFolder.add(tfDefaultWorkFolder);
		tfDefaultWorkFolder.setText(cfDTO.getDefaultWorkFolderPath());
		tfDefaultWorkFolder.setColumns(10);
		
		btnBrowseWorkFolder = new JButton("Browse...");
		btnBrowseWorkFolder.addActionListener(this);
		btnBrowseWorkFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBrowseWorkFolder.setBounds(518, 11, 100, 23);
		panelFolder.add(btnBrowseWorkFolder);
		
		JLabel lblNewLabel_1 = new JLabel("Work folder");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(56, 15, 183, 14);
		panelFolder.add(lblNewLabel_1);
		
		tfGeneSet = new JTextField();
		tfGeneSet.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfGeneSet.setColumns(10);
		tfGeneSet.setBounds(155, 42, 349, 22);
		tfGeneSet.setText(cfDTO.getGeneSetPath());
		panelFolder.add(tfGeneSet);
		
		btnGeneSetFolder = new JButton("Browse...");
		btnGeneSetFolder.addActionListener(this);
		btnGeneSetFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnGeneSetFolder.setBounds(518, 42, 100, 23);
		panelFolder.add(btnGeneSetFolder);
		
		JLabel lblHtmlSourceFolder = new JLabel("HTML source folder");
		lblHtmlSourceFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHtmlSourceFolder.setBounds(21, 79, 183, 14);
		panelFolder.add(lblHtmlSourceFolder);
		
		tfHTMLSource = new JTextField();
		tfHTMLSource.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfHTMLSource.setColumns(10);
		tfHTMLSource.setBounds(155, 75, 349, 22);
		tfHTMLSource.setText(cfDTO.getHTMLSourcePath());
		panelFolder.add(tfHTMLSource);
		
		btnHTMLSource = new JButton("Browse...");
		btnHTMLSource.addActionListener(this);
		btnHTMLSource.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnHTMLSource.setBounds(518, 75, 100, 23);
		panelFolder.add(btnHTMLSource);
		
		JLabel lblMetageneFolder = new JLabel("MetaGene folder");
		lblMetageneFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMetageneFolder.setBounds(31, 113, 183, 14);
		panelFolder.add(lblMetageneFolder);
		
		tfMetaGeneFolder = new JTextField();
		tfMetaGeneFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfMetaGeneFolder.setColumns(10);
		tfMetaGeneFolder.setBounds(155, 109, 349, 22);
		tfMetaGeneFolder.setText(cfDTO.getMetaGeneFolderPath());
		panelFolder.add(tfMetaGeneFolder);
		
		btnMetaGeneFolder = new JButton("Browse...");
		btnMetaGeneFolder.addActionListener(this);
		btnMetaGeneFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnMetaGeneFolder.setBounds(518, 109, 100, 23);
		panelFolder.add(btnMetaGeneFolder);
		
		JLabel lblHtmlSourceFolder_1_1 = new JLabel("Gene properties file");
		lblHtmlSourceFolder_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHtmlSourceFolder_1_1.setBounds(21, 164, 183, 14);
		panelFolder.add(lblHtmlSourceFolder_1_1);
		
		tfGenePropertiesFile = new JTextField();
		tfGenePropertiesFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfGenePropertiesFile.setColumns(10);
		tfGenePropertiesFile.setBounds(155, 160, 349, 22);
		tfGenePropertiesFile.setText(cfDTO.getGenePropertiesFilePath());
		panelFolder.add(tfGenePropertiesFile);
				
		JLabel lblHtmlSourceFolder_1_2 = new JLabel("Network undirected file");
		lblHtmlSourceFolder_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHtmlSourceFolder_1_2.setBounds(10, 197, 183, 14);
		panelFolder.add(lblHtmlSourceFolder_1_2);
		
		tfNetworkUndirectedFile = new JTextField();
		tfNetworkUndirectedFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfNetworkUndirectedFile.setColumns(10);
		tfNetworkUndirectedFile.setBounds(155, 193, 349, 22);
		tfNetworkUndirectedFile.setText(cfDTO.getNetworkUndirectedFilePath());
		panelFolder.add(tfNetworkUndirectedFile);
		
		btnNetworkUndirectedFile = new JButton("Browse...");
		btnNetworkUndirectedFile.addActionListener(this);
		btnNetworkUndirectedFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNetworkUndirectedFile.setBounds(518, 193, 100, 23);
		panelFolder.add(btnNetworkUndirectedFile);
		
		JLabel lblHtmlSourceFolder_1_2_1 = new JLabel("Network directed file");
		lblHtmlSourceFolder_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHtmlSourceFolder_1_2_1.setBounds(21, 230, 183, 14);
		panelFolder.add(lblHtmlSourceFolder_1_2_1);
		
		tfNetworkDirectedFile = new JTextField();
		tfNetworkDirectedFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfNetworkDirectedFile.setColumns(10);
		tfNetworkDirectedFile.setBounds(155, 226, 349, 22);
		tfNetworkDirectedFile.setText(cfDTO.getNetworkDirectedFilePath());
		panelFolder.add(tfNetworkDirectedFile);
		
		btnNetworkDirectedFile = new JButton("Browse...");
		btnNetworkDirectedFile.addActionListener(this);
		btnNetworkDirectedFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNetworkDirectedFile.setBounds(518, 226, 100, 23);
		panelFolder.add(btnNetworkDirectedFile);
		
		btnGenePropertiesFile = new JButton("Browse...");
		btnGenePropertiesFile.addActionListener(this);
		btnGenePropertiesFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnGenePropertiesFile.setBounds(518, 162, 100, 23);
		panelFolder.add(btnGenePropertiesFile);

		JPanel panelAnalysisParameters	= new JPanel();
		tabbedPane.addTab("Analysis parameters", null, panelAnalysisParameters, null);	
		panelAnalysisParameters.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Compute robust statistics");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(23, 21, 192, 14);
		panelAnalysisParameters.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("MinNumberOfDistinctValuesInNumericals");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1.setBounds(33, 52, 261, 14);
		panelAnalysisParameters.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("MinNumberOfSamplesInCategory");
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_1.setBounds(23, 89, 261, 14);
		panelAnalysisParameters.add(lblNewLabel_2_1_1);
		
		JLabel lblNewLabel_2_1_2 = new JLabel("MaxNumberOfCategories");
		lblNewLabel_2_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_2.setBounds(23, 125, 261, 14);
		panelAnalysisParameters.add(lblNewLabel_2_1_2);
		
		JLabel lblNewLabel_2_1_3 = new JLabel("AssociationAnalysisThreshold");
		lblNewLabel_2_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_3.setBounds(23, 172, 198, 14);
		panelAnalysisParameters.add(lblNewLabel_2_1_3);
		
		JLabel lblNewLabel_2_1_4 = new JLabel("MinimalTolerableStability");
		lblNewLabel_2_1_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_1_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_1_4.setBounds(23, 197, 198, 14);
		panelAnalysisParameters.add(lblNewLabel_2_1_4);
		
		cbComputeRobustStatistics = new JCheckBox("");
		cbComputeRobustStatistics.setBounds(221, 17, 97, 23);
		cbComputeRobustStatistics.setSelected(cfDTO.getComputeRobustStatistics());
		panelAnalysisParameters.add(cbComputeRobustStatistics);
		
		sMinNoOfDistinctValuesInNumericals = new JSpinner();
		sMinNoOfDistinctValuesInNumericals.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		sMinNoOfDistinctValuesInNumericals.setBounds(304, 52, 49, 28);
		//System.out.println("MinNumberOfDistinctValuesInNumericals="+cfDTO.getMinNumberOfDistinctValuesInNumericals());
		sMinNoOfDistinctValuesInNumericals.setValue(cfDTO.getMinNumberOfDistinctValuesInNumericals());
		panelAnalysisParameters.add(sMinNoOfDistinctValuesInNumericals);
		
		sMinNoOfSamplesInCategory = new JSpinner();
		sMinNoOfSamplesInCategory.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		sMinNoOfSamplesInCategory.setBounds(304, 89, 49, 28);
		sMinNoOfSamplesInCategory.setValue(cfDTO.getMinNumberOfSamplesInCategory());
		panelAnalysisParameters.add(sMinNoOfSamplesInCategory);
		
		sMaxNoOfCategories = new JSpinner();
		sMaxNoOfCategories.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		sMaxNoOfCategories.setBounds(304, 125, 49, 28);
		sMaxNoOfCategories.setValue(cfDTO.getMaxNumberOfCategories());
		panelAnalysisParameters.add(sMaxNoOfCategories);
		
		sAssociationAnalysisInThreshold = new JFormattedTextField();
		sAssociationAnalysisInThreshold.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sAssociationAnalysisInThreshold.setBounds(247, 171, 71, 20);
		sAssociationAnalysisInThreshold.setText(""+cfDTO.getAssociationAnalysisInThreshold());
		panelAnalysisParameters.add(sAssociationAnalysisInThreshold);
		
		sMinimalTolerableStability = new JFormattedTextField();
		sMinimalTolerableStability.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sMinimalTolerableStability.setBounds(247, 196, 71, 20);
		sMinimalTolerableStability.setText(""+cfDTO.getMinimalTolerableStability());
		panelAnalysisParameters.add(sMinimalTolerableStability);
		
		JPanel panelPython = new JPanel();
		tabbedPane.addTab("Python options", null, panelPython, null);
		panelPython.setLayout(null);
		
		rbtnUsePython = new JRadioButton("Use Python for ICA computations");
		rbtnUsePython.setSelected(true);
		rbtnUsePython.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rbtnUsePython.setBounds(33, 28, 317, 23);
		rbtnUsePython.addActionListener(this);
		if(cfDTO.getICAImplementation().equals("python")) rbtnUsePython.setSelected(true);  
		panelPython.add(rbtnUsePython);
		
		JLabel lblNewLabel_1_1_1_1_2 = new JLabel("ICAMaxNumberOfIterations");
		lblNewLabel_1_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_2.setBounds(27, 191, 183, 22);
		panelPython.add(lblNewLabel_1_1_1_1_2);
		
		tfPythonMaxNumberOfIterations = new JFormattedTextField();
		tfPythonMaxNumberOfIterations.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfPythonMaxNumberOfIterations.setBounds(220, 194, 118, 20);
		tfPythonMaxNumberOfIterations.setText(""+cfDTO.getPythonICAMaxNumIterations());
		panelPython.add(tfPythonMaxNumberOfIterations);
		
		JLabel lblNewLabel_1_1_1_1_1_1 = new JLabel("NumberOfICARuns");
		lblNewLabel_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_1_1.setBounds(27, 223, 183, 22);
		panelPython.add(lblNewLabel_1_1_1_1_1_1);
		
		tfPythonNumberOfICARuns = new JFormattedTextField();
		tfPythonNumberOfICARuns.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfPythonNumberOfICARuns.setBounds(220, 226, 118, 20);
		tfPythonNumberOfICARuns.setText(""+cfDTO.getPythonNumberOfICARuns());
		panelPython.add(tfPythonNumberOfICARuns);
		
		JLabel lblNewLabel_1_1_1_2_1 = new JLabel("ICA approach");
		lblNewLabel_1_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_2_1.setBounds(27, 111, 183, 22);
		panelPython.add(lblNewLabel_1_1_1_2_1);
		
		JLabel lblNewLabel_1_1_1_3 = new JLabel("ICA non-linearity");
		lblNewLabel_1_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_3.setBounds(27, 146, 183, 22);
		panelPython.add(lblNewLabel_1_1_1_3);
		
		cmbPythonICAApproach = new JComboBox();
		cmbPythonICAApproach.setModel(new DefaultComboBoxModel(new String[] {"fastica_par", "fastica_def", "fastica_picard", "infomax", "infomax_ext", "infomax_orth"}));
		cmbPythonICAApproach.setSelectedIndex(0);
		cmbPythonICAApproach.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cmbPythonICAApproach.setEditable(true);
		cmbPythonICAApproach.setBounds(220, 113, 203, 22);
		cmbPythonICAApproach.setSelectedItem(cfDTO.getPythonICAApproach());
		panelPython.add(cmbPythonICAApproach);
		
		cmbPythonNonlinearity = new JComboBox();
		cmbPythonNonlinearity.setModel(new DefaultComboBoxModel(new String[] {"cube", "exp", "logcosh", "tanh"}));
		cmbPythonNonlinearity.setSelectedIndex(0);
		cmbPythonNonlinearity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cmbPythonNonlinearity.setEditable(true);
		cmbPythonNonlinearity.setBounds(220, 148, 203, 22);
		cmbPythonNonlinearity.setSelectedItem(cfDTO.getPythonICAMeasure());
		panelPython.add(cmbPythonNonlinearity);
		
		JLabel lblNewLabel_1_1_1_2_2 = new JLabel("Component visualization");
		lblNewLabel_1_1_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_2_2.setBounds(27, 279, 183, 22);
		panelPython.add(lblNewLabel_1_1_1_2_2);
		
		cmbPythonVisualizationMethod = new JComboBox();
		cmbPythonVisualizationMethod.setModel(new DefaultComboBoxModel(new String[] {"umap", "mds", "tsne"}));
		cmbPythonVisualizationMethod.setSelectedIndex(0);
		cmbPythonVisualizationMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cmbPythonVisualizationMethod.setEditable(true);
		cmbPythonVisualizationMethod.setBounds(220, 281, 203, 22);
		cmbPythonVisualizationMethod.setSelectedItem(cfDTO.getPythonTypeOfVisualization());
		panelPython.add(cmbPythonVisualizationMethod);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Python code folder");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_2_1.setBounds(27, 74, 183, 14);
		panelPython.add(lblNewLabel_1_1_2_1);
		
		tfPythonCodeFolder = new JTextField();
		tfPythonCodeFolder.setText((String) null);
		tfPythonCodeFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfPythonCodeFolder.setColumns(10);
		tfPythonCodeFolder.setBounds(155, 74, 360, 22);
		tfPythonCodeFolder.setText(cfDTO.getPythonICAFolderPath());
		panelPython.add(tfPythonCodeFolder);
		
		btnPythonCodeFolder = new JButton("Browse...");
		btnPythonCodeFolder.addActionListener(this);
		btnPythonCodeFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPythonCodeFolder.setBounds(529, 74, 100, 23);
		panelPython.add(btnPythonCodeFolder);
		
		JPanel panelMatlab	= new JPanel();
		tabbedPane.addTab("MATLAB options", null, panelMatlab, null);
		panelMatlab.setLayout(null);
		
		JLabel lblNewLabel_1_1 = new JLabel("MATLAB folder");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(22, 63, 183, 14);
		panelMatlab.add(lblNewLabel_1_1);
		
		tfMatlabica = new JTextField();
		tfMatlabica.setEnabled(false);
		tfMatlabica.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfMatlabica.setColumns(10);
		tfMatlabica.setBounds(173, 88, 360, 22);
		tfMatlabica.setText(cfDTO.getMatlabicaFolderPath());
		panelMatlab.add(tfMatlabica);
		
		btnMATLABFolder = new JButton("Browse...");
		btnMATLABFolder.setEnabled(false);
		btnMATLABFolder.addActionListener(this);
		btnMATLABFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnMATLABFolder.setBounds(543, 63, 100, 23);
		panelMatlab.add(btnMATLABFolder);
		
		JLabel lblNewLabel_2_2 = new JLabel("Use docker");
		lblNewLabel_2_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2_2.setBounds(32, 128, 128, 14);
		panelMatlab.add(lblNewLabel_2_2);
		
		cbUseDocker = new JCheckBox("");
		cbUseDocker.setEnabled(false);
		cbUseDocker.setBounds(176, 124, 97, 23);
		panelMatlab.add(cbUseDocker);
		
		rbtnUseMATLAB = new JRadioButton("Use MATLAB for ICA computations");
		rbtnUseMATLAB.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rbtnUseMATLAB.setBounds(36, 17, 320, 23);
		rbtnUseMATLAB.addActionListener(this);
		if(cfDTO.getICAImplementation().equals("matlab")) rbtnUseMATLAB.setSelected(true); 
		panelMatlab.add(rbtnUseMATLAB);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("fastICA non-linearity");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(35, 193, 183, 22);
		panelMatlab.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("ICAMaxNumberOfIterations");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1.setBounds(35, 226, 183, 22);
		panelMatlab.add(lblNewLabel_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("NumberOfICARuns");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_1.setBounds(35, 258, 183, 22);
		panelMatlab.add(lblNewLabel_1_1_1_1_1);
		
		cmbMATLABNonlinearity = new JComboBox();
		cmbMATLABNonlinearity.setEnabled(false);
		cmbMATLABNonlinearity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cmbMATLABNonlinearity.setEditable(true);
		cmbMATLABNonlinearity.setModel(new DefaultComboBoxModel(new String[] {"pow3", "tanh", "gauss", "skew"}));
		cmbMATLABNonlinearity.setSelectedIndex(0);
		cmbMATLABNonlinearity.setBounds(228, 195, 203, 22);
		cmbMATLABNonlinearity.setSelectedItem(cfDTO.getMATLABICAMeasure());
		panelMatlab.add(cmbMATLABNonlinearity);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("fastICA approach");
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_1_2.setBounds(35, 158, 183, 22);
		panelMatlab.add(lblNewLabel_1_1_1_2);
		
		cmbMATLABICAApproach = new JComboBox();
		cmbMATLABICAApproach.setEnabled(false);
		cmbMATLABICAApproach.setModel(new DefaultComboBoxModel(new String[] {"symm", "defl"}));
		cmbMATLABICAApproach.setSelectedIndex(0);
		cmbMATLABICAApproach.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cmbMATLABICAApproach.setEditable(true);
		cmbMATLABICAApproach.setBounds(228, 160, 203, 22);
		cmbMATLABICAApproach.setSelectedItem(cfDTO.getMATLABICAApproach());
		panelMatlab.add(cmbMATLABICAApproach);
		
		tfMATLABMaxNumberOfIterations = new JFormattedTextField();
		tfMATLABMaxNumberOfIterations.setEnabled(false);
		tfMATLABMaxNumberOfIterations.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfMATLABMaxNumberOfIterations.setBounds(228, 229, 118, 20);
		tfMATLABMaxNumberOfIterations.setText(""+cfDTO.getMATLABICAMaxNumIterations());
		panelMatlab.add(tfMATLABMaxNumberOfIterations);
		
		tfMATLABNumberOfICARuns = new JFormattedTextField();
		tfMATLABNumberOfICARuns.setEnabled(false);
		tfMATLABNumberOfICARuns.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfMATLABNumberOfICARuns.setBounds(228, 261, 118, 20);
		tfMATLABNumberOfICARuns.setText(""+cfDTO.getMATLABNumberOfICARuns());
		panelMatlab.add(tfMATLABNumberOfICARuns);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("MATLAB FastICA folder");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1_2.setBounds(22, 96, 183, 14);
		panelMatlab.add(lblNewLabel_1_1_2);
		
		tfMATLABFolder = new JTextField();
		tfMATLABFolder.setText((String) null);
		tfMATLABFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tfMATLABFolder.setEnabled(false);
		tfMATLABFolder.setColumns(10);
		tfMATLABFolder.setBounds(173, 63, 360, 22);
		tfMATLABFolder.setText(cfDTO.getMATLABFolderPath());
		panelMatlab.add(tfMATLABFolder);
		
		btnMatlabica = new JButton("Browse...");
		btnMatlabica.setEnabled(false);
		btnMatlabica.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnMatlabica.setBounds(543, 88, 100, 23);
		panelMatlab.add(btnMatlabica);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnSave = new JButton("OK");
				btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
				btnSave.addActionListener(this);
				btnSave.setActionCommand("OK");
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
				btnCancel.addActionListener(this);
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == btnBrowseWorkFolder)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfDefaultWorkFolder.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}else if(e.getSource() == btnMatlabica)
		{
		        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		        	tfMatlabica.setText(fileChooser.getSelectedFile().getAbsolutePath());
		        }
		}
		else if(e.getSource() == btnPythonCodeFolder)
		{
		        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		        	tfPythonCodeFolder.setText(fileChooser.getSelectedFile().getAbsolutePath());
		        }
		}		
		else if(e.getSource() == btnGeneSetFolder)
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
		else if(e.getSource() == rbtnUsePython)
		{
			rbtnUseMATLAB.setSelected(false);
			
			tfMATLABFolder.setEnabled(false);
			tfMatlabica.setEnabled(false);
			cbUseDocker.setEnabled(false);
			cmbMATLABICAApproach.setEnabled(false);
			cmbMATLABNonlinearity.setEnabled(false);
			tfMATLABMaxNumberOfIterations.setEnabled(false);
			tfMATLABNumberOfICARuns.setEnabled(false);
			
			tfPythonCodeFolder.setEnabled(true);
			cmbPythonICAApproach.setEnabled(true);
			cmbPythonNonlinearity.setEnabled(true);
			tfPythonMaxNumberOfIterations.setEnabled(true);
			tfPythonNumberOfICARuns.setEnabled(true);
			cmbPythonVisualizationMethod.setEnabled(true);
			
			btnPythonCodeFolder.setEnabled(true);
			btnMATLABFolder.setEnabled(false);
			btnMatlabica.setEnabled(false);
		}
		else if(e.getSource() == rbtnUseMATLAB)
		{
			rbtnUsePython.setSelected(false);
			
			rbtnUseMATLAB.setSelected(true);
			tfMATLABFolder.setEnabled(true);
			tfMatlabica.setEnabled(true);
			cbUseDocker.setEnabled(true);
			cmbMATLABICAApproach.setEnabled(true);
			cmbMATLABNonlinearity.setEnabled(true);
			tfMATLABMaxNumberOfIterations.setEnabled(true);
			tfMATLABNumberOfICARuns.setEnabled(true);		
			
			tfPythonCodeFolder.setEnabled(false);
			cmbPythonICAApproach.setEnabled(false);
			cmbPythonNonlinearity.setEnabled(false);
			tfPythonMaxNumberOfIterations.setEnabled(false);
			tfPythonNumberOfICARuns.setEnabled(false);
			cmbPythonVisualizationMethod.setEnabled(false);
			
			btnPythonCodeFolder.setEnabled(false);
			btnMATLABFolder.setEnabled(true);
			btnMatlabica.setEnabled(true);
			
		}						
		else if(e.getSource() == btnSave)
		{
			ConfigDTO configDTO = new ConfigDTO();
			configDTO.setDefaultWorkFolderPath(getDefaultWorkFolderPath());
			configDTO.setGeneSetPath(getGeneSetFolderPath());
			configDTO.setHTMLSourcePath(getHTMLSourceFolderPath());
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

			configDTO.setICAImplementation(getICAImplementation());
			
			configDTO.setMatlabicaFolderPath(getMatlabicaFolderPath());			
			configDTO.setMATLABFolderPath(getMATLABFolderPath());
			configDTO.setUseDocker(isUseDocker());
			configDTO.setMATLABICAApproach(getMATLABICAApproach());
			configDTO.setMATLABICAMeasure(getMATLABICAMeasure());
			configDTO.setMATLABICAMaxNumIterations(getMATLABICAMaxNumIterations());
			configDTO.setMATLABNumberOfICARuns(getMATLABNumberOfICARuns());

			configDTO.setPythonICAFolderPath(getPythonICAFolderPath());
			configDTO.setPythonICAApproach(getPythonICAApproach());
			configDTO.setPythonICAMeasure(getPythonICAMeasure());
			configDTO.setPythonICAMaxNumIterations(getPythonICAMaxNumIterations());
			configDTO.setPythonNumberOfICARuns(getPythonNumberOfICARuns());
			configDTO.setPythonTypeOfVisualization(getPythonTypeOfVisualization());

			cfHelper.updateFoldersPathValuesInConfigFile(configDTO);
			dispose();
		}
		else if(e.getSource() == btnCancel)
		{
			dispose();
		}		

	}
	
	public String getDefaultWorkFolderPath() {
        return tfDefaultWorkFolder.getText().trim();
    }

	public String getMATLABFolderPath() {
        return tfMATLABFolder.getText().trim();
    }	
	
	public String getMatlabicaFolderPath() {
        return tfMatlabica.getText().trim();
    }
	
	public String getGeneSetFolderPath()
	{
		return tfGeneSet.getText().trim();
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
		
		return (Integer)sMinNoOfDistinctValuesInNumericals.getValue();
	}
	
	
	public int getMinNumberOfSamplesInCategory()
	{
		try {
			sMinNoOfSamplesInCategory.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return (Integer)sMinNoOfSamplesInCategory.getValue();
	}
	
	
	public int getMaxNumberOfCategories()
	{
		try {
			sMaxNoOfCategories.commitEdit();
		} catch ( java.text.ParseException e ) {}
		return (Integer)sMaxNoOfCategories.getValue();
	}

	public double getAssociationAnalysisInThreshold()
	{
		return Double.parseDouble(sAssociationAnalysisInThreshold.getText());
	}
	
	public double getMinimalTolerableStability()
	{
		return Double.parseDouble(sMinimalTolerableStability.getText());
	}
	
	public int getMATLABNumberOfICARuns()
	{
		return Integer.parseInt(tfMATLABNumberOfICARuns.getText());
	}
	
	public String getICAImplementation()
	{
		String impl = "python";
		if(rbtnUseMATLAB.isSelected()){ impl = "matlab"; }
		return impl;
	}
	
	public String getMATLABICAApproach()
	{
		return (String)cmbMATLABICAApproach.getSelectedItem();
	}
	
	public String getMATLABICAMeasure()
	{
		return (String)cmbMATLABNonlinearity.getSelectedItem();
	}
	public int getMATLABICAMaxNumIterations()
	{
		return Integer.parseInt(tfMATLABMaxNumberOfIterations.getText());
	}
	public String getPythonICAFolderPath()
	{
		return tfPythonCodeFolder.getText();
	}
	public String getPythonICAApproach()
	{
		return (String)cmbPythonICAApproach.getSelectedItem();
	}
	public String getPythonICAMeasure()
	{
		return (String)cmbPythonNonlinearity.getSelectedItem();
	}
	public int getPythonICAMaxNumIterations()
	{
		return Integer.parseInt(tfPythonMaxNumberOfIterations.getText());
	}
	public int getPythonNumberOfICARuns()
	{
		return Integer.parseInt(tfPythonNumberOfICARuns.getText());
	}
	public String getPythonTypeOfVisualization()
	{
		return (String)cmbPythonVisualizationMethod.getSelectedItem();
	}
	
}
