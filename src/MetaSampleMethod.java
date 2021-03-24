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
import javax.swing.BorderFactory;
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
import model.MetaSampleDTO;
import util.ConfigHelper;
import util.WindowEventHandler;


public class MetaSampleMethod extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JButton btnATable,btnDataTable,btnMetaSampleAnalysis,btnRunMethod, btnClear;;
	private JTextField tfATable,tfDataTable,tfMetaSampleAnalysis;
	private ConfigDTO cfDTO;
	private JScrollPane sptAConsole;
	private JProgressBar pbProgress;
	private JTextArea tAConsole;
	private RunMetaSampleWorker runMetaSampleWorker;
	private File df;
	
	//Default values
	public String DEFAULT_META_SAMPLE_ANALYSIS_PATH = "";
	

	public MetaSampleMethod(JFrame parent) {
		
		super(parent, "Meta Sample Annotation", true);
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
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b>  Meta Sample Analysis </html>");
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

		////// A table path
	 	
	 	GridBagConstraints csLbSTable = new GridBagConstraints();
	 	csLbSTable.fill = GridBagConstraints.HORIZONTAL;
	 	csLbSTable.insets = new Insets(10,10,10,10);
	 	csLbSTable.gridx = 0;
	 	csLbSTable.gridy = 2;
	 	csLbSTable.gridwidth = 1; 
	 	csLbSTable.weighty = 1.0;
	 	JLabel lbSTable = new JLabel("(or) Specify metasample table A*");
	 	contentPane.add(lbSTable, csLbSTable);

	 	
	 	GridBagConstraints csTfSTable =  new GridBagConstraints();
	 	csTfSTable.fill = GridBagConstraints.HORIZONTAL;
	 	csTfSTable.insets = new Insets(10,10,10,10);
	 	csTfSTable.gridx = 1;
	 	csTfSTable.gridy = 2;
	 	csTfSTable.gridwidth = 2; 
	 	csTfSTable.weightx = 1.0;
	 	csTfSTable.weighty = 1.0;
	 	tfATable = new JTextField(30);
	 	tfATable.setText("");
	 	contentPane.add(tfATable, csTfSTable);
	 	
	 	GridBagConstraints csBtnATable =  new GridBagConstraints();
	 	csBtnATable.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnATable.insets = new Insets(10,10,10,10);
	 	csBtnATable.gridx = 3;
	 	csBtnATable.gridy = 2;
	 	csBtnATable.gridwidth = 1; 
	 	btnATable =  new JButton("Browse");
	 	btnATable.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnATable.addActionListener(this);
	 	contentPane.add(btnATable, csBtnATable);
	 	
	 	
	 	//Third row - Meta Sample Analysis path
	 	
	 	
	 	GridBagConstraints csLbMetaSampleAnalysis = new GridBagConstraints();
	 	csLbMetaSampleAnalysis.fill = GridBagConstraints.HORIZONTAL;
	 	csLbMetaSampleAnalysis.insets = new Insets(10,10,10,10);
	 	csLbMetaSampleAnalysis.gridx = 0;
	 	csLbMetaSampleAnalysis.gridy = 3;
	 	csLbMetaSampleAnalysis.gridwidth = 1; 
	 	csLbMetaSampleAnalysis.weighty = 1.0;
	 	JLabel lbMetaSampleAnalysis = new JLabel("Specify Sample Annotation File *");
	 	contentPane.add(lbMetaSampleAnalysis, csLbMetaSampleAnalysis);
	 	
	 	
	 	GridBagConstraints csTfMetaSampleAnalysis =  new GridBagConstraints();
	 	csTfMetaSampleAnalysis.fill = GridBagConstraints.HORIZONTAL;
	 	csTfMetaSampleAnalysis.insets = new Insets(10,10,10,10);
	 	csTfMetaSampleAnalysis.gridx = 1;
	 	csTfMetaSampleAnalysis.gridy = 3;
	 	csTfMetaSampleAnalysis.gridwidth = 1; 
	 	csTfMetaSampleAnalysis.weightx = 1.0;
	 	csTfMetaSampleAnalysis.weighty = 1.0;
	 	tfMetaSampleAnalysis = new JTextField(30);
	 	tfMetaSampleAnalysis.setText(DEFAULT_META_SAMPLE_ANALYSIS_PATH);
	 	contentPane.add(tfMetaSampleAnalysis, csTfMetaSampleAnalysis);
	 	
	 	
	 	GridBagConstraints csBtnMetaSampleAnalysis =  new GridBagConstraints();
	 	csBtnMetaSampleAnalysis.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnMetaSampleAnalysis.insets = new Insets(10,10,10,10);
	 	csBtnMetaSampleAnalysis.gridx = 3;
	 	csBtnMetaSampleAnalysis.gridy = 3;
	 	csBtnMetaSampleAnalysis.gridwidth = 1; 
	 	
	 	
	 	btnMetaSampleAnalysis =  new JButton("Browse");
	 	btnMetaSampleAnalysis.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnMetaSampleAnalysis.addActionListener(this);
	 	contentPane.add(btnMetaSampleAnalysis, csBtnMetaSampleAnalysis);
	 	
	 	
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
		else if(e.getSource() == btnATable){
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfATable.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnMetaSampleAnalysis)
		{
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfMetaSampleAnalysis.setText(fileChooser.getSelectedFile().getAbsolutePath());
	            //ConfigHelper.DEFAULT_DATA_TABLE_PATH = fileChooser.getSelectedFile().getAbsolutePath();
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
				runMetaSampleWorker = new RunMetaSampleWorker(tAConsole, btnRunMethod, pbProgress, getMetaSampleValues(), this);
				runMetaSampleWorker.execute();
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
		if(!getMetaSampleValues().getDataTablePath().equals("")){
			df = new File(getMetaSampleValues().getDataTablePath());
			String analysisprefix = df.getName().substring(0, df.getName().length()-4);
			fn = cfDTO.getDefaultWorkFolderPath()+System.getProperty("file.separator")+analysisprefix+"_ICA"+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls";
		}
		if(!getMetaSampleValues().getATablePath().equals("")){
			fn = getMetaSampleValues().getATablePath();
		}
		return fn;
	}
	
	
	
	private boolean emptyFieldExists()
	{
		return (tfDataTable.getText().trim().isEmpty()&&tfATable.getText().trim().isEmpty()) || tfMetaSampleAnalysis.getText().trim().isEmpty();
	}
	
	private MetaSampleDTO getMetaSampleValues()
	{
		MetaSampleDTO metaSampleDTO = new MetaSampleDTO();
		metaSampleDTO.setDataTablePath(tfDataTable.getText().trim());
		metaSampleDTO.setDefaultWorkFolderPath(cfDTO.getDefaultWorkFolderPath().trim());
		metaSampleDTO.setSampleAnnotationFilePath(tfMetaSampleAnalysis.getText().trim());
		metaSampleDTO.setATablePath(tfATable.getText().trim());

		return metaSampleDTO;
	}
}
