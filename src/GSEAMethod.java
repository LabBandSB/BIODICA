import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import model.ConfigDTO;
import model.ConstantCodes;
import model.GseaDTO;
import util.ConfigHelper;
import util.WindowEventHandler;


public class GSEAMethod extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JTextField tfDataTable;
	private JTextField tfSTable;
	public JButton btnDataTable,btnSTable,btnRunMethod, btnStopMethod,btnClear;
	private JLabel lbGseaValue;
	private JSpinner sGseaValue;
	private JSpinner sTopMinNumberValue;
	private ConfigDTO cfDTO;
	private JScrollPane sptAConsole;
	public JProgressBar pbProgress;
	public JTextArea tAConsole;
	private RunGSEAWorker runGSEAWorker;
	private JCheckBox cDoAnalysis;
	private JLabel lbDoAnalysis;
	private JLabel lbTopMinNumberValue; 
	private JLabel lbTopThresholdValue;
	private JLabel lbFDRThresholdValue;
	private JLabel lbPValueThresholdValue;
 	private JTextField sTopThresholdValue;
 	private JTextField sFDRThresholdValue;
 	private JTextField sPValueThresholdValue;
 	private File df;
 	
 	public WindowEventHandler windowHandler;
 	
	//Default values
	public Number DEFAULT_GSEA_VALUE  = 10;
	public Boolean DEFAULT_GSEA_DOACTUAL_ANALYSIS  = true;
	public Number  DEFAULT_TOPMINNUMBER_VALUE = 5;
	public Number  DEFAULT_TOPTHRESHOLD_VALUE = 3f;
	public Number  DEFAULT_FDRTHRESHOLD_VALUE = 0.01f;
	public Number  DEFAULT_PVALUETHRESHOLD_VALUE = 0.01f;

	
	public GSEAMethod(JFrame parent) 
	{
		super(parent, "GSEA Computation", true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		setIconImage(mainIcon.getImage());
		
		ConfigHelper cfHelper = new ConfigHelper();
		cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
		
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		windowHandler = new WindowEventHandler();
		this.addWindowListener(windowHandler);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		
		// First row - Title 	
	 	GridBagConstraints csLbTitle = new GridBagConstraints();
	 	csLbTitle.gridwidth = 3;
	 	csLbTitle.fill = GridBagConstraints.HORIZONTAL;
	 	csLbTitle.insets = new Insets(10,10,10,10);
	 	csLbTitle.gridx = 0;
	 	csLbTitle.gridy = 0;
	 	csLbTitle.weightx = 1.0;
	 	csLbTitle.weighty = 1.0;
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b>  Gene Set Enrichment Analysis (GSEA) </html>");
	 	contentPane.add(lbTitle, csLbTitle);
		
 	 	//Second row - Data Table path
	 	
	 	GridBagConstraints csLbDataTable = new GridBagConstraints();
	 	csLbDataTable.fill = GridBagConstraints.HORIZONTAL;
	 	csLbDataTable.insets = new Insets(10,10,10,10);
	 	csLbDataTable.gridx = 0;
	 	csLbDataTable.gridy = 1;
	 	csLbDataTable.gridwidth = 1; 
	 	csLbDataTable.weighty = 1.0;
	 	JLabel lbDataTable = new JLabel("Specify Data table*");
	 	contentPane.add(lbDataTable, csLbDataTable);
		
	 	
	 	GridBagConstraints csTfDataTable =  new GridBagConstraints();
	 	csTfDataTable.fill = GridBagConstraints.HORIZONTAL;
	 	csTfDataTable.insets = new Insets(10,10,10,10);
	 	csTfDataTable.gridx = 1;
	 	csTfDataTable.gridy = 1;
	 	csTfDataTable.gridwidth = 1; 
	 	csTfDataTable.weightx = 1.0;
	 	csTfDataTable.weighty = 1.0;
	 	tfDataTable = new JTextField(30);
	 	tfDataTable.setText(ConfigHelper.DEFAULT_DATA_TABLE_PATH);
	 	contentPane.add(tfDataTable, csTfDataTable);
	 	
	 	
	 	GridBagConstraints csBtnDataTable =  new GridBagConstraints();
	 	csBtnDataTable.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnDataTable.insets = new Insets(10,10,10,10);
	 	csBtnDataTable.gridx = 3;
	 	csBtnDataTable.gridy = 1;
	 	csBtnDataTable.gridwidth = 1; 
	 	
	 	
	 	btnDataTable =  new JButton("Browse");
	 	btnDataTable.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnDataTable.addActionListener(this);
	 	contentPane.add(btnDataTable, csBtnDataTable);

	////// S table path
	 	
		 	GridBagConstraints csLbSTable = new GridBagConstraints();
		 	csLbSTable.fill = GridBagConstraints.HORIZONTAL;
		 	csLbSTable.insets = new Insets(10,10,10,10);
		 	csLbSTable.gridx = 0;
		 	csLbSTable.gridy = 2;
		 	csLbSTable.gridwidth = 1; 
		 	csLbSTable.weighty = 1.0;
		 	JLabel lbSTable = new JLabel("(or) Specify metagene table S*");
		 	contentPane.add(lbSTable, csLbSTable);

		 	
		 	GridBagConstraints csTfSTable =  new GridBagConstraints();
		 	csTfSTable.fill = GridBagConstraints.HORIZONTAL;
		 	csTfSTable.insets = new Insets(10,10,10,10);
		 	csTfSTable.gridx = 1;
		 	csTfSTable.gridy = 2;
		 	csTfSTable.gridwidth = 2; 
		 	csTfSTable.weightx = 1.0;
		 	csTfSTable.weighty = 1.0;
		 	tfSTable = new JTextField(30);
		 	tfSTable.setText("");
		 	contentPane.add(tfSTable, csTfSTable);
		 	
		 	GridBagConstraints csBtnSTable =  new GridBagConstraints();
		 	csBtnSTable.fill = GridBagConstraints.HORIZONTAL;
		 	csBtnSTable.insets = new Insets(10,10,10,10);
		 	csBtnSTable.gridx = 3;
		 	csBtnSTable.gridy = 2;
		 	csBtnSTable.gridwidth = 1; 
		 	btnSTable =  new JButton("Browse");
		 	btnSTable.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
		 	btnSTable.addActionListener(this);
		 	contentPane.add(btnSTable, csBtnSTable);
	 	
	 	
	 	// Parameters of GSEA Computation
	 	
	 	GridBagConstraints csParametersPanel =  new GridBagConstraints();
	 	csParametersPanel.fill = GridBagConstraints.HORIZONTAL;
	 	csParametersPanel.gridwidth = 4;
	 	csParametersPanel.anchor = GridBagConstraints.NORTHWEST;
	 	csParametersPanel.insets = new Insets(10,10,10,10);
	 	csParametersPanel.gridx = 0;
	 	csParametersPanel.gridy = 3;
	 	csParametersPanel.weightx = 4.0;
	 	csParametersPanel.weighty = 1.0;
	 	
	 	JPanel parametersPanel =  new JPanel(new GridBagLayout());
	 	GroupLayout layout = new GroupLayout(parametersPanel);
	 	parametersPanel.setLayout(layout);
	 	
	 	parametersPanel.setBorder(BorderFactory.createTitledBorder(null, "<html><b>GSEA parameters</b></html>",0,0,null,Color.BLUE));
	 	layout.setAutoCreateGaps(true);
	 	layout.setAutoCreateContainerGaps(true);
	 	
	 	
	 	
	 	//Parameters 1 row
	 	
	 	lbGseaValue = new JLabel("Number of GSEA permutations");
	 	lbGseaValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbGseaValue.setBorder(new EmptyBorder(0, 50, 0, 0));

	 	lbDoAnalysis = new JLabel("Do computations (if false - only update filtering)");
	 	lbDoAnalysis.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbDoAnalysis.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	cDoAnalysis = new JCheckBox();
	 	cDoAnalysis.setSelected(DEFAULT_GSEA_DOACTUAL_ANALYSIS);
	 	
	 	
	 	sGseaValue = new JSpinner(new SpinnerNumberModel(DEFAULT_GSEA_VALUE, 0, 999, 1));
	 			
	 	JFormattedTextField sMinNoOfDistinctValuesInNumericalsTxt = ((JSpinner.NumberEditor) sGseaValue.getEditor()).getTextField();
	 	((NumberFormatter) sMinNoOfDistinctValuesInNumericalsTxt.getFormatter()).setAllowsInvalid(false);

	 	// Parameters of GSEA Filtering
		 	
	 	lbTopMinNumberValue = new JLabel("Minimal number of top contributing genes in the core");
	 	lbTopMinNumberValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbTopMinNumberValue.setBorder(new EmptyBorder(0, 50, 0, 0));

	 	lbTopThresholdValue = new JLabel("Threshold for selecting top contributing genes");
	 	lbTopThresholdValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbTopThresholdValue.setBorder(new EmptyBorder(0, 50, 0, 0));

	 	lbFDRThresholdValue = new JLabel("FDR threshold");
	 	lbFDRThresholdValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbFDRThresholdValue.setBorder(new EmptyBorder(0, 50, 0, 0));

	 	lbPValueThresholdValue = new JLabel("Adjusted p-value threshold");
	 	lbPValueThresholdValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
	 	lbPValueThresholdValue.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	sTopMinNumberValue = new JSpinner(new SpinnerNumberModel(DEFAULT_TOPMINNUMBER_VALUE, 0, 999, 1));
	 	sTopThresholdValue = new JTextField(5);
	 	sTopThresholdValue.setText(""+DEFAULT_TOPTHRESHOLD_VALUE);
	 	sFDRThresholdValue = new JTextField(5);
	 	sFDRThresholdValue.setText(""+DEFAULT_FDRTHRESHOLD_VALUE);
	 	sPValueThresholdValue = new JTextField(5);
	 	sPValueThresholdValue.setText(""+DEFAULT_PVALUETHRESHOLD_VALUE);
	 	
	 	
	 	layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbGseaValue)
	 		    		.addComponent(sGseaValue, 0,GroupLayout.DEFAULT_SIZE, 60)
	 		    		.addComponent(lbDoAnalysis)
	 		    		.addComponent(cDoAnalysis, 0,GroupLayout.DEFAULT_SIZE, 60)
	 		    		)
	 		    		.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbTopMinNumberValue)
	 		    		.addComponent(sTopMinNumberValue, 0,GroupLayout.DEFAULT_SIZE, 60)
	 		    		.addComponent(lbTopThresholdValue)
	 		    		.addComponent(sTopThresholdValue, 0,GroupLayout.DEFAULT_SIZE, 60))
	 		    		.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbFDRThresholdValue)
	 		    		.addComponent(sFDRThresholdValue, 0,GroupLayout.DEFAULT_SIZE, 60)
	 		    		.addComponent(lbPValueThresholdValue)
	 		    		.addComponent(sPValueThresholdValue, 0,GroupLayout.DEFAULT_SIZE, 60)
	 		    		));

	 	layout.setVerticalGroup(layout.createSequentialGroup()
	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    .addComponent(lbGseaValue).addComponent(sGseaValue)
	       		.addComponent(lbDoAnalysis).addComponent(cDoAnalysis))
	       		.addGroup(layout.createParallelGroup()
	 		       		.addComponent(lbTopMinNumberValue).addComponent(sTopMinNumberValue)
	 		       		.addComponent(lbTopThresholdValue).addComponent(sTopThresholdValue))
	       		.addGroup(layout.createParallelGroup()
	 		       		.addComponent(lbFDRThresholdValue).addComponent(sFDRThresholdValue)
	 		       		.addComponent(lbPValueThresholdValue).addComponent(sPValueThresholdValue)
	 		    ));
	 	
	 	

	 	contentPane.add(parametersPanel,csParametersPanel);
	 	
	 	
	 	
	 	// Output text area 
 		GridBagConstraints csTAconsole = new GridBagConstraints();
 		csTAconsole.ipady = 1;
 		csTAconsole.fill = GridBagConstraints.BOTH;
 		csTAconsole.insets = new Insets(10,10,0,10);
 		csTAconsole.gridx = 0;
 		csTAconsole.gridy = 3;
 		csTAconsole.gridwidth = 4; 	
 		csTAconsole.weightx = 1.0;
 		csTAconsole.weighty = 2.0;
 		
 		tAConsole = new JTextArea(20,40);
 		tAConsole.setLineWrap(true);
 		tAConsole.setEditable(false);
 		tAConsole.setWrapStyleWord(true);
 		Border border = BorderFactory.createLineBorder(Color.GRAY);
 		tAConsole.setBorder(BorderFactory.createCompoundBorder(border, 
 		            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
 		
 		sptAConsole = new JScrollPane(tAConsole);	
 		sptAConsole.setVerticalScrollBarPolicy(
                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 		sptAConsole.setMinimumSize(new Dimension(200,100));
 		 		
 	 	contentPane.add(sptAConsole, csTAconsole);
 	 	
 	 	
 	 	GridBagConstraints csProgressBar = new GridBagConstraints();
 		csProgressBar.gridheight = 0;
 		csProgressBar.weightx = 1.0;
 		csProgressBar.fill = GridBagConstraints.HORIZONTAL;
 		csProgressBar.insets = new Insets(0,10,0,10);
 		csProgressBar.gridx = 0;
 		csProgressBar.gridy = 4;
 		csProgressBar.gridwidth = 4;
 		
 		pbProgress = new JProgressBar();
 		pbProgress.setIndeterminate(false);
 		Border progressBorder = BorderFactory.createTitledBorder("");
 		pbProgress.setBorder(progressBorder);
 		pbProgress.setVisible(true);
 	
 		contentPane.add(pbProgress,csProgressBar);
	 	
 		//Footer
		// Clear Button
	 	
	 	JPanel btnsPanel = new JPanel(new BorderLayout());
	 	btnsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
	 	
	 	JPanel clearPanel = new JPanel(new FlowLayout());
	 	
	 	btnClear = new JButton("Clear");
	 	btnClear.setIcon(new ImageIcon(getClass().getResource("Clear.png")));
	 
	 	btnClear.addActionListener(this);
	 	
	 	clearPanel.add(btnClear);
	 	
	    // Run and Stop Buttons
	 		 	
	 	btnRunMethod = new JButton("Run");
	 	btnRunMethod.setIcon(new ImageIcon(getClass().getResource("Run.png")));
	 	btnRunMethod.addActionListener(this);
	 	
	 	btnStopMethod = new JButton("Stop");
	 	btnStopMethod.setIcon(new ImageIcon(getClass().getResource("Stop.png")));
	 	btnStopMethod.setEnabled(false);
	 	btnStopMethod.addActionListener(this);
	 	

	 	boolean isRunBtnEnabled = isRunBtnEnabled();
	 	btnRunMethod.setEnabled(isRunBtnEnabled);
		if(!isRunBtnEnabled) btnRunMethod.setToolTipText("<html><span style='color:#FF3F33'>Source: \"" + getFilePath() +"\" does not exist.</span></html>");


	 	JPanel runStopPanel = new JPanel(new FlowLayout());
	 	runStopPanel.add(btnRunMethod);
	 	runStopPanel.add(btnStopMethod);
	
	 	
	 	btnsPanel.add(clearPanel, BorderLayout.EAST);
	 	btnsPanel.add(runStopPanel, BorderLayout.WEST);
	 	
		getContentPane().add(contentPane, BorderLayout.CENTER);
	 	getContentPane().add(btnsPanel, BorderLayout.PAGE_END);
 		
 		pack();
 		setMinimumSize(getSize());
		setLocationRelativeTo(parent);	

	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getSource() == btnDataTable)
		{
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfDataTable.setText(fileChooser.getSelectedFile().getAbsolutePath());
	            ConfigHelper.DEFAULT_DATA_TABLE_PATH = fileChooser.getSelectedFile().getAbsolutePath();
	        }
		}
		else if(e.getSource() == btnSTable){
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfSTable.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		
		else if(e.getSource() == btnClear)
		{
			tAConsole.setText("\n");
		}

		else if(e.getSource() == btnRunMethod)
		{
			if(emptyFieldExists())
			{
				JOptionPane.showMessageDialog(this, "Please, fill out all required fields", "Empty field", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				runGSEAWorker = new RunGSEAWorker(this, getGSEAValues(),this);
				runGSEAWorker.execute();
				//this.setEnabled(false);
			}
		}
		else if(e.getSource() == btnStopMethod)
		{
			
			for(int i=0;i<runGSEAWorker.GSEAThreads.size();i++){
				if(runGSEAWorker.GSEAThreads.get(i).isAlive())
					runGSEAWorker.GSEAThreads.get(i).interrupt();
			}
			
			     runGSEAWorker.action = ConstantCodes.CANCELED;
			     runGSEAWorker.cancel(true);
		}
	 	boolean isRunBtnEnabled = isRunBtnEnabled();
	 	btnRunMethod.setEnabled(isRunBtnEnabled);
		
	}
	
	private boolean isRunBtnEnabled()
	{
		return (new File(getFilePath()).exists());	
	}
	
	private String getFilePath()
	{
		String fn = "";
		if(!getGSEAValues().getDataTablePath().equals("")){
			df = new File(getGSEAValues().getDataTablePath());
			String analysisprefix = df.getName().substring(0, df.getName().length()-4);
			fn = cfDTO.getDefaultWorkFolderPath()+System.getProperty("file.separator")+analysisprefix+"_ICA"+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls";
		}
		if(!getGSEAValues().getSTablePath().equals("")){
			fn = getGSEAValues().getSTablePath();
		}
		
		return fn;
	}
	
	private boolean emptyFieldExists()
	{
		return tfDataTable.getText().trim().isEmpty() && tfSTable.getText().trim().isEmpty();
	}
	
	private GseaDTO getGSEAValues()
	{
		GseaDTO gseaDTO = new GseaDTO();
		gseaDTO.setDataTablePath(tfDataTable.getText().trim());
		gseaDTO.setSTablePath(tfSTable.getText().trim());
		gseaDTO.setDefaultWorkFolderPath(cfDTO.getDefaultWorkFolderPath().trim());
		gseaDTO.setHTMLSourceFolder(cfDTO.getHTMLSourcePath());
		gseaDTO.setGeneSetFolderPath(cfDTO.getGeneSetPath());
		gseaDTO.setNumberOfGSEAPermutations((Integer)sGseaValue.getValue());
		gseaDTO.setDoAnalysis(cDoAnalysis.isSelected());
		gseaDTO.setTopMinNumberValue((Integer)sTopMinNumberValue.getValue());
		gseaDTO.setTopThresholdValue(Float.parseFloat(sTopThresholdValue.getText()));
		gseaDTO.setFDRThresholdValue(Float.parseFloat(sFDRThresholdValue.getText()));
		gseaDTO.setPValueThresholdValue(Float.parseFloat(sPValueThresholdValue.getText()));
		return gseaDTO;
	}

}
