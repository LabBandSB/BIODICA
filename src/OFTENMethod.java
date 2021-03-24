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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import model.ConfigDTO;
import model.ConstantCodes;
import model.OftenDTO;
import util.ConfigHelper;
import util.WindowEventHandler;


public class OFTENMethod extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JTextField tfDataTable,tfOften,tfOftenValues;
	private JButton btnDataTable,btnSTable,btnOften,btnRunMethod, btnClear;;
	private JTextField tfSTable;
	private ConfigDTO cfDTO;
	private JScrollPane sptAConsole;
	private JProgressBar pbProgress;
	private JTextArea tAConsole;
	private RunOFTENWorker runOFTENWorker;
	private File df;
	
	
	//Default values
	//public String DEFAULT_OFTEN_PATH = "knowledge/networks/undirected/hprd9_pc_clicks.xgmml";
	public String DEFAULT_OFTEN_VALUES = "50,100,600,100";
	
	public OFTENMethod(JFrame parent) 
	{
		super(parent, "OFTEN Analysis", true);
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
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b>  OFTEN Analysis </html>");
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
	 	
	 	
 		//Second row - OFTEN path
	 	GridBagConstraints csLbOften = new GridBagConstraints();
	 	csLbOften.fill = GridBagConstraints.HORIZONTAL;
	 	csLbOften.insets = new Insets(10,10,10,10);
	 	csLbOften.gridx = 0;
	 	csLbOften.gridy = 3;
	 	csLbOften.gridwidth = 1; 
	 	csLbOften.weighty = 1.0;
	 	JLabel lbOften = new JLabel("Specify PPI network*");
	 	contentPane.add(lbOften, csLbOften);
	 	
	 	
		GridBagConstraints csTfOften =  new GridBagConstraints();
		csTfOften.fill = GridBagConstraints.HORIZONTAL;
		csTfOften.insets = new Insets(10,10,10,10);
		csTfOften.gridx = 1;
		csTfOften.gridy = 3;
		csTfOften.gridwidth = 1; 
	 	csTfOften.weightx = 1.0;
	 	csTfOften.weighty = 1.0;
	 	tfOften = new JTextField(30);
	 	tfOften.setText(cfDTO.getNetworkUndirectedFilePath());
	 	contentPane.add(tfOften, csTfOften);
	 	
	 	
	 	GridBagConstraints csBtnOften =  new GridBagConstraints();
	 	csBtnOften.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnOften.insets = new Insets(10,10,10,10);
	 	csBtnOften.gridx = 3;
	 	csBtnOften.gridy = 3;
	 	csBtnOften.gridwidth = 1; 
	 	btnOften =  new JButton("Browse");
	 	btnOften.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnOften.addActionListener(this);
	 	contentPane.add(btnOften, csBtnOften);
	 	 	
	 	// Parameters of GSEA Computation 	
 	 	GridBagConstraints csParametersPanel =  new GridBagConstraints();
 	 	csParametersPanel.fill = GridBagConstraints.HORIZONTAL;
 	 	csParametersPanel.gridwidth = 4;
 	 	csParametersPanel.anchor = GridBagConstraints.NORTHWEST;
 	 	csParametersPanel.insets = new Insets(10,10,10,10);
 	 	csParametersPanel.gridx = 0;
 	 	csParametersPanel.gridy = 4;
 	 	csParametersPanel.weightx = 4.0;
 	 	csParametersPanel.weighty = 1.0;
 	 	
 	 	JPanel parametersPanel =  new JPanel(new GridBagLayout());
 	 	GroupLayout layout = new GroupLayout(parametersPanel);
 	 	parametersPanel.setLayout(layout);
 	 	
 	 	parametersPanel.setBorder(BorderFactory.createTitledBorder(null, "<html><b>Parameter of OFTEN Method</b></html>",0,0,null,Color.BLUE));
 	 	layout.setAutoCreateGaps(true);
 	 	layout.setAutoCreateContainerGaps(true);
	 	 	 	
 	 	//Parameters 1 row	
 	 	JLabel lbOftenValue = new JLabel("Scan k (Min,Step,Max,NumberOfPerms)");
 	 	lbOftenValue.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
 	 	lbOftenValue.setBorder(new EmptyBorder(0, 50, 0, 0));

 	 	tfOftenValues = new JTextField();
 	 	tfOftenValues.setText(DEFAULT_OFTEN_VALUES);
 	 	
 	 	layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
 	 		    .addGroup(layout.createSequentialGroup()
 	 		    		.addComponent(lbOftenValue)
 	 		    		.addComponent(tfOftenValues, 0,GroupLayout.DEFAULT_SIZE, 250)));

 	 	layout.setVerticalGroup(layout.createSequentialGroup()
 	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
 	 		        .addComponent(lbOftenValue).addComponent(tfOftenValues)));
	 	
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
 		sptAConsole.setMinimumSize(new Dimension(200,300));
 		
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

	 	boolean isRunBtnEnabled = isRunBtnEnabled();
	 	btnRunMethod.setEnabled(isRunBtnEnabled);
		if(!isRunBtnEnabled) btnRunMethod.setToolTipText("<html><span style='color:#FF3F33'>Source: \"" + getFilePath() +"\" does not exist.</span></html>");


	 	JPanel runStopPanel = new JPanel(new FlowLayout());
	 	runStopPanel.add(btnRunMethod);
	
	 	
	 	btnsPanel.add(clearPanel, BorderLayout.EAST);
	 	btnsPanel.add(runStopPanel, BorderLayout.WEST);
	 	
		getContentPane().add(contentPane, BorderLayout.CENTER);
	 	getContentPane().add(btnsPanel, BorderLayout.PAGE_END);
 			 	
 		pack();
 		setMinimumSize(getSize());
		setLocationRelativeTo(parent);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
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
		else if(e.getSource() == btnOften)
		{
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfOften.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		
		else if(e.getSource() == btnRunMethod)
		{
			if(emptyFieldExists())
			{
				JOptionPane.showMessageDialog(this, "Please, fill out all required fields", "Empty field", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				runOFTENWorker = new RunOFTENWorker(tAConsole, btnRunMethod, pbProgress, getOftenValues(), this);
				runOFTENWorker.execute();
				this.setEnabled(false);
			}
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
		if(!getOftenValues().getDataTablePath().equals("")){
			df = new File(getOftenValues().getDataTablePath());
			String analysisprefix = df.getName().substring(0, df.getName().length()-4);
			fn = cfDTO.getDefaultWorkFolderPath()+System.getProperty("file.separator")+analysisprefix+"_ICA"+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls";
		}
		if(!getOftenValues().getSTablePath().equals("")){
			fn = getOftenValues().getSTablePath();
		}
		
		return fn;
	}
	
	
	
	private boolean emptyFieldExists()
	{
		return (tfDataTable.getText().trim().isEmpty()&&tfSTable.getText().trim().isEmpty()) 
				|| tfOften.getText().trim().isEmpty() || tfOftenValues.getText().trim().isEmpty();
	}
	
	
	
	private OftenDTO getOftenValues()
	{
		OftenDTO oftenDTO = new OftenDTO();
		oftenDTO.setDataTablePath(tfDataTable.getText().trim());
		oftenDTO.setSTablePath(tfSTable.getText().trim());
		oftenDTO.setDefaultWorkFolderPath(cfDTO.getDefaultWorkFolderPath().trim());
		oftenDTO.setDoOften(tfOften.getText().trim()+"#"+tfOftenValues.getText().trim());
		return oftenDTO;
	}
}
