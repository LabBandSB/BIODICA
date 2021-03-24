import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import model.ConfigDTO;
import model.ConstantCodes;
import model.IcaDTO;
import util.ConfigHelper;
import util.HTMLGenerator;
import util.WindowEventHandler;
import vdaoengine.utils.Utils;


public class IcaMethod extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfDataTable, tfRangeNoOfComponents;
	private JButton btnDataTable, btnRunMethod, btnClear, btnOpenResults;
	private JFileChooser fileChooser;
	private JRadioButton fixedNoRBtn, rangeNoRbtn, optimalNoRBtn, precomputedNoRBtn;
	private ButtonGroup groupRBtn;
	private JSpinner sNoOfComponents;
	private JLabel lbNoOfComponents,lbRangeOfComponents;
	private ConfigDTO cfDTO;
	private JScrollPane sptAConsole;
	private JProgressBar pbProgress;
	private JTextArea tAConsole;
	private RunICAWorker runICAWorker;
	private JCheckBox cbVisOption;
	private JComboBox cbPrecomputed; 	
	
	
	//Default values
	public Number DEFAULT_FIXED_NO_OF_COMPONENTS = 10;
	


	public IcaMethod(JFrame parent) 
	{
		super(parent, "IC's Computation", true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		setIconImage(mainIcon.getImage());
		
		ConfigHelper cfHelper = new ConfigHelper();
		cfDTO = cfHelper.getFoldersPathValuesFromConfigFile();
	
		contentPane = new JPanel(new GridBagLayout());
	
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		this.addWindowListener(new WindowEventHandler());
		
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
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b> Independent Component Analysis</html>");
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

	 	
	 	// Vis Option
	 	
	 	GridBagConstraints csLbVisOption = new GridBagConstraints();
	 	csLbVisOption.fill = GridBagConstraints.HORIZONTAL;
	 	csLbVisOption.insets = new Insets(10,10,10,10);
	 	csLbVisOption.gridx = 0;
	 	csLbVisOption.gridy = 2;
	 	csLbVisOption.gridwidth = 1; 

	 	csLbVisOption.weighty = 1.0;
	 	JLabel lbVisOption = new JLabel("Produce visualization (unselect to accelerate)");
	 	contentPane.add(lbVisOption, csLbVisOption);
	 	
	 	csLbVisOption = new GridBagConstraints();
	 	csLbVisOption.fill = GridBagConstraints.HORIZONTAL;
	 	csLbVisOption.insets = new Insets(10,10,10,10);
	 	csLbVisOption.gridx = 1;
	 	csLbVisOption.gridy = 2;
	 	csLbVisOption.gridwidth = 1; 

	 	csLbVisOption.weighty = 1.0;
	 	cbVisOption = new JCheckBox();
	 	cbVisOption.setSelected(true);
	 	contentPane.add(cbVisOption, csLbVisOption);	 	

	 	
	 	// Parameters of ICA Method
	 	
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
	 	
	 	parametersPanel.setBorder(BorderFactory.createTitledBorder(null, "<html><b>Parameters of ICA method</b></html>",0,0,null,Color.BLUE));
	 	
	 	layout.setAutoCreateGaps(true);
	 	layout.setAutoCreateContainerGaps(true);
	 	
	 	//Parameters 1 row
	 	
	 	fixedNoRBtn = new JRadioButton("<html><b>Fixed number of components </b></html>");
	 	fixedNoRBtn.setBorder(new EmptyBorder(10, 10, 10, 10));
	 	fixedNoRBtn.setSelected(true);
	 	fixedNoRBtn.addActionListener(this);
	 	
	 	//	Parameters 1.1 row
	 	lbNoOfComponents = new JLabel("Number of components");
	 	lbNoOfComponents.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	sNoOfComponents = new JSpinner(new SpinnerNumberModel(DEFAULT_FIXED_NO_OF_COMPONENTS, 0, 999,
      1));
	 	JFormattedTextField sMinNoOfDistinctValuesInNumericalsTxt = ((JSpinner.NumberEditor) sNoOfComponents.getEditor()).getTextField();
	 	((NumberFormatter) sMinNoOfDistinctValuesInNumericalsTxt.getFormatter()).setAllowsInvalid(false);

	 	
	 	// Parameters 2 row
	 	rangeNoRbtn = new JRadioButton("<html> <b>Scanning a range of component numbers (Optional) </b> </html>");
	 	rangeNoRbtn.addActionListener(this);
	 	rangeNoRbtn.setBorder(new EmptyBorder(10, 10, 10, 10));
	 	
	 
	 	// Parameters 2.1 row
	 	
	 	lbRangeOfComponents = new JLabel("Range of numbers, separated by comma (Specify from 2 to \u221e)");
	 	lbRangeOfComponents.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	tfRangeNoOfComponents = new JTextField(30);
	 	
		lbRangeOfComponents.setEnabled(false);
		tfRangeNoOfComponents.setEnabled(false);
		
		
		// Parameters 3 row
		
		optimalNoRBtn = new JRadioButton("<html> <b>Select optimal number IC's </b> </html>");
		optimalNoRBtn.addActionListener(this);
		optimalNoRBtn.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Parameters 4 row
		
		precomputedNoRBtn = new JRadioButton("<html> <b>Choose precomputed number of IC's </b> </html>");
		precomputedNoRBtn.addActionListener(this);
		precomputedNoRBtn.setBorder(new EmptyBorder(10, 10, 10, 10));

		cbPrecomputed = new JComboBox();
		cbPrecomputed.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	//precomputedNoRBtn.setSelected(true);
	            }
	        });
		//cbPrecomputed.setSize(10, 1);
		
		
		BindData();

		//Group the radio buttons.	 	
	 	groupRBtn = new ButtonGroup();
	 	groupRBtn.add(fixedNoRBtn);
	 	groupRBtn.add(rangeNoRbtn);
	 	groupRBtn.add(optimalNoRBtn);
	 	groupRBtn.add(precomputedNoRBtn);
	 	
	 	
	 	layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(fixedNoRBtn))
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbNoOfComponents)
	 		    		.addComponent(sNoOfComponents, 0,GroupLayout.DEFAULT_SIZE, 60))
    			.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(rangeNoRbtn))
	    		.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbRangeOfComponents)
	 		    		.addComponent(tfRangeNoOfComponents,0,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE ))
 		    	.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(optimalNoRBtn))	
 		    	.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(precomputedNoRBtn))
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(cbPrecomputed))	
 		    	
	 			);

	 	layout.setVerticalGroup(layout.createSequentialGroup()
	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		        .addComponent(fixedNoRBtn))
	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(lbNoOfComponents).addComponent(sNoOfComponents))
	 	 		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(rangeNoRbtn))
 		    	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(lbRangeOfComponents).addComponent(tfRangeNoOfComponents))
	    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(optimalNoRBtn))
 		    	.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(precomputedNoRBtn))
	 		    .addGroup(layout.createSequentialGroup()		
	 		    		.addComponent(cbPrecomputed))	
	 			
	 			);
	 	
	 	
	 	contentPane.add(parametersPanel,csParametersPanel);
	 	
	 	
	 	// Output text area 
		GridBagConstraints csTAconsole = new GridBagConstraints();
		csTAconsole.ipady = 1;
		csTAconsole.fill = GridBagConstraints.BOTH;
		csTAconsole.insets = new Insets(10,10,0,10);
		csTAconsole.gridx = 0;
		csTAconsole.gridy = 4;
		csTAconsole.gridwidth = 4; 	
		csTAconsole.weightx = 1.0;
		csTAconsole.weighty = 2.0;
		
		tAConsole = new JTextArea(20,80);
		tAConsole.setLineWrap(true);
		tAConsole.setEditable(false);
		tAConsole.setWrapStyleWord(true);
		Border border = BorderFactory.createLineBorder(Color.GRAY);
		tAConsole.setBorder(BorderFactory.createCompoundBorder(border, 
		            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		sptAConsole = new JScrollPane(tAConsole);	
		sptAConsole.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sptAConsole.setMinimumSize(new Dimension(100,300));		
	 	contentPane.add(sptAConsole, csTAconsole);
	 	
	 	
	 	GridBagConstraints csProgressBar = new GridBagConstraints();
		csProgressBar.gridheight = 0;
		csProgressBar.weightx = 1.0;
		csProgressBar.fill = GridBagConstraints.HORIZONTAL;
		csProgressBar.insets = new Insets(0,10,0,10);
		csProgressBar.gridx = 0;
		csProgressBar.gridy = 5;
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


	 	JPanel runStopPanel = new JPanel(new FlowLayout());
	 	runStopPanel.add(btnRunMethod);

	 	btnOpenResults = new JButton("Open results");
	 	btnOpenResults.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnOpenResults.addActionListener(this);
	 	JPanel openResultsPanel = new JPanel(new FlowLayout());
	 	openResultsPanel.add(btnOpenResults);
	 	
	 	
	 	btnsPanel.add(clearPanel, BorderLayout.EAST);
	 	btnsPanel.add(runStopPanel, BorderLayout.WEST);
	 	btnsPanel.add(openResultsPanel, BorderLayout.CENTER);
	 	
		
		getContentPane().add(contentPane, BorderLayout.CENTER);
	 	getContentPane().add(btnsPanel, BorderLayout.PAGE_END);
	 	
 		pack();
 		setMinimumSize(getSize());
		setLocationRelativeTo(parent);
	}
	
	public void BindData()
	{
		boolean isOptNumEnabled = IsOptimalNumEnabled();
		optimalNoRBtn.setEnabled(isOptNumEnabled);
		cbPrecomputed.setEnabled(isOptNumEnabled);
		precomputedNoRBtn.setEnabled(isOptNumEnabled);
		if(!isOptNumEnabled){ 
			optimalNoRBtn.setToolTipText("<html><span style='color:#FF3F33'>You have to first complete ICA Method with fixed or ranged numbers</span></html>");
			precomputedNoRBtn.setToolTipText("<html><span style='color:#FF3F33'>You have to first complete ICA Method with fixed or ranged numbers or specify the data file for which the compututation was already done</span></html>");
		}
	 	
	}
	
	public boolean IsOptimalNumEnabled()
	{
		if(!tfDataTable.getText().trim().equals("")){
		File df = new File(getICAValues().getDataTablePath());
		String analysisprefix = df.getName().substring(0, df.getName().length()-4);
		String filesParentPath = cfDTO.getDefaultWorkFolderPath()+System.getProperty("file.separator")+analysisprefix+"_ICA";
		boolean fileExists = new File(filesParentPath+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls").exists() && new File(filesParentPath+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls").exists();
		if(fileExists){
			Vector<Integer> nums = precomputedNumbersOfComponents(filesParentPath+System.getProperty("file.separator")+"stability");
			cbPrecomputed.removeAllItems();
			for(Integer i: nums)
				cbPrecomputed.addItem(i);
		}
		return fileExists;
		}else{
			return false;
		}
	}
	
	public Vector<Integer> precomputedNumbersOfComponents(String folder){
		Vector<Integer> nums = new Vector<Integer>();
		File files[] = new File(folder).listFiles();
		for(File f: files){
			String fname = f.getName();
			if(fname.contains("stability"))if(fname.endsWith(".txt")){
				Vector<String> lines = Utils.loadStringListFromFile(f.getAbsolutePath());
				nums.add(lines.size());
			}
		}
		Collections.sort(nums);
		return nums;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnDataTable)
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	tfDataTable.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        	ConfigHelper.DEFAULT_DATA_TABLE_PATH = fileChooser.getSelectedFile().getAbsolutePath();
	        	BindData();
	        }
		}
		else if(e.getSource() == fixedNoRBtn)
		{
			sNoOfComponents.setEnabled(true);
			lbNoOfComponents.setEnabled(true);
			lbRangeOfComponents.setEnabled(false);
			tfRangeNoOfComponents.setEnabled(false);
		}
		
		else if(e.getSource() == rangeNoRbtn)
		{
			sNoOfComponents.setEnabled(false);
			lbNoOfComponents.setEnabled(false);		
			lbRangeOfComponents.setEnabled(true);
			tfRangeNoOfComponents.setEnabled(true);	
		}
		else if(e.getSource() == optimalNoRBtn)
		{
			sNoOfComponents.setEnabled(false);
			lbNoOfComponents.setEnabled(false);
			lbRangeOfComponents.setEnabled(false);
			tfRangeNoOfComponents.setEnabled(false);			
		}
		else if(e.getSource() == precomputedNoRBtn)
		{
			sNoOfComponents.setEnabled(false);
			lbNoOfComponents.setEnabled(false);
			lbRangeOfComponents.setEnabled(false);
			tfRangeNoOfComponents.setEnabled(false);			
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
				try {					 
					
					 if(optimalNoRBtn.isSelected())
						 runICAWorker = new RunICAWorker(tAConsole,btnRunMethod, pbProgress,getICAValues(),ConstantCodes.OPTIMAL_COMPONENT_NO,this);
					 else if(precomputedNoRBtn.isSelected())
						 runICAWorker = new RunICAWorker(tAConsole,btnRunMethod, pbProgress,getICAValues(),ConstantCodes.PRECOMPUTED, this);
					 else
						 runICAWorker = new RunICAWorker(tAConsole,btnRunMethod, pbProgress,getICAValues(),ConstantCodes.ICA_METHOD, this);
					 runICAWorker.execute();	
					 this.setEnabled(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource() == btnOpenResults)
		{
			try {
				 runICAWorker = new RunICAWorker(tAConsole,btnOpenResults, pbProgress,getICAValues(),ConstantCodes.OPEN_ICA_RESULTS,this);
				 runICAWorker.execute();	
				 this.setEnabled(false);
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

		}
		
	}
	
	private boolean emptyFieldExists()
	{
		return tfDataTable.getText().trim().isEmpty() || 
				(tfRangeNoOfComponents.isEnabled() && tfRangeNoOfComponents.getText().trim().isEmpty());
	}
	

	private IcaDTO getICAValues()
	{
		IcaDTO icaDTO = new IcaDTO();
		icaDTO.setDataTablePath(tfDataTable.getText().trim());
		icaDTO.setDefaultWorkFolderPath(cfDTO.getDefaultWorkFolderPath().trim());
		icaDTO.setMATLABFolderPath(cfDTO.getMATLABFolderPath());
		icaDTO.setMatlabicaFolderPath(cfDTO.getMatlabicaFolderPath());
		icaDTO.setUseDocker(cfDTO.isUseDocker());
		icaDTO.setVisOption(cbVisOption.isSelected());
		icaDTO.setICAApproach(cfDTO.getICAApproach());
		icaDTO.setICAMeasure(cfDTO.getICAMeasure());
		icaDTO.setICAMaxNumIterations(cfDTO.getICAMaxNumIterations());
		if(tfRangeNoOfComponents.isEnabled())
			icaDTO.setSNoOfComponents(tfRangeNoOfComponents.getText());
		else
			icaDTO.setSNoOfComponents(sNoOfComponents.getValue().toString());
		if(precomputedNoRBtn.isSelected()){
			System.out.println(""+cbPrecomputed.getSelectedItem());
			icaDTO.setSNoOfComponents(""+cbPrecomputed.getSelectedItem());
		}
		return icaDTO;
	}
}
