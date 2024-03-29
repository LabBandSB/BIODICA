/*
BIODICA Navigator software
Copyright (C) 2017-2022 Curie Institute, 26 rue d'Ulm, 75005 Paris - FRANCE

Copyright (C) 2017-2022 National Laboratory Astana, Center for Life Sciences, Nazarbayev University, Nur-Sultan, Kazakhstan



BIODICA Navigator is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.



BIODICA Navigator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.



You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/



/*
BIODICA Navigator authors:
Andrei Zinovyev : http://andreizinovyev.site
Ulykbek Kairov : ulykbek.kairov@nu.edu.kz
Askhat Molkenov : askhat.molkenov@nu.edu.kz
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import model.ConfigDTO;
import model.MetaGeneDTO;
import util.ConfigHelper;
import util.WindowEventHandler;


public class MetaGeneMethod extends JDialog implements ActionListener{

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MetaGeneMethod frame = new MetaGeneMethod(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ConfigDTO cfDTO;
	private JFileChooser fileChooser;
	private JButton btnSTable,btnDataTable,btnMetaGeneAnalysis,btnMetaGeneRNKAnalysis,btnRunMethod, btnClear;;
	private JTextField tfSTable,tfDataTable,tfMetaGeneAnalysis,tfMetaGeneRNKAnalysis;
	private JRadioButton metaGeneRBtn, metaGeneRNKRBtn;
	private ButtonGroup groupRBtn;
	private JLabel lbMetaGeneAnalysis,lbMetaGeneRNKAnalysis;
	private JScrollPane sptAConsole;
	private JProgressBar pbProgress;
	private JTextArea tAConsole;
	private RunMetaGeneWorker runMetaGeneWorker;
	private File df;
	
	//Default values
	//public String DEFAULT_META_GENE_ANALYSIS_PATH = "knowledge/geneproperties/genes.txt";
	//public String DEFAULT_META_GENE_RNK_ANALYSIS_PATH = "knowledge/metagenes/";	
	
	public MetaGeneMethod(JFrame parent) 
	{
		super(parent, "MetaGene Annotation", true);
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
		fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
		
		
		// First row - Title 
		
	 	GridBagConstraints csLbTitle = new GridBagConstraints();
	 	csLbTitle.gridwidth = 3;
	 	csLbTitle.fill = GridBagConstraints.HORIZONTAL;
	 	csLbTitle.insets = new Insets(10,10,10,10);
	 	csLbTitle.gridx = 0;
	 	csLbTitle.gridy = 0;
	 	csLbTitle.weightx = 1.0;
	 	csLbTitle.weighty = 1.0;
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b>  MetaGene Annotation </html>");
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
	 	
	 	
	 	
	 	// Parameters of Meta Gene Method
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
	 	
	 	parametersPanel.setBorder(BorderFactory.createTitledBorder(null, "<html><b>Parameters of MetaGene Method</b></html>",0,0,null,Color.BLUE));
	 	
	 	layout.setAutoCreateGaps(true);
	 	layout.setAutoCreateContainerGaps(true);
	 	
	 	
	 	//Parameters 1 row
	 	
	 	metaGeneRBtn = new JRadioButton("<html><b>MetaGene associations</b></html>");
	 	metaGeneRBtn.setBorder(new EmptyBorder(10, 10, 10, 10));
	 	metaGeneRBtn.setSelected(true);
	 	metaGeneRBtn.addActionListener(this);
	 	
	 	
	 	//Parameters 1.1 row
	 	
	 	lbMetaGeneAnalysis = new JLabel("Table of gene properties*");
	 	lbMetaGeneAnalysis.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	tfMetaGeneAnalysis = new JTextField(30);
	 	tfMetaGeneAnalysis.setText(cfDTO.getGenePropertiesFilePath());

	 	btnMetaGeneAnalysis =  new JButton("Browse");
	 	btnMetaGeneAnalysis.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnMetaGeneAnalysis.addActionListener(this);

	 	

	 	// Parameters 2 row
	 	metaGeneRNKRBtn = new JRadioButton("<html> <b>Compare to previously defined metagenes</b> </html>");
	 	metaGeneRNKRBtn.addActionListener(this);
	 	metaGeneRNKRBtn.setBorder(new EmptyBorder(10, 10, 10, 10));
	 	
	 	
	 	// Parameters 2.1 row
	 	
	 	lbMetaGeneRNKAnalysis = new JLabel("Folder with RNK files*");
	 	lbMetaGeneRNKAnalysis.setBorder(new EmptyBorder(0, 50, 0, 0));
	 	
	 	tfMetaGeneRNKAnalysis = new JTextField(30);
	 	tfMetaGeneRNKAnalysis.setText(cfDTO.getMetaGeneFolderPath());
	 	
		btnMetaGeneRNKAnalysis =  new JButton("Browse");
		btnMetaGeneRNKAnalysis.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
		btnMetaGeneRNKAnalysis.addActionListener(this);
	 	
		lbMetaGeneRNKAnalysis.setEnabled(true);
		tfMetaGeneRNKAnalysis.setEnabled(true);
		btnMetaGeneRNKAnalysis.setEnabled(true);
		lbMetaGeneAnalysis.setEnabled(false);
		tfMetaGeneAnalysis.setEnabled(false);
		btnMetaGeneAnalysis.setEnabled(false);
		metaGeneRNKRBtn.setSelected(true);
		metaGeneRBtn.setSelected(false);
			
		//Group the radio buttons.	 	
	 	groupRBtn = new ButtonGroup();
	 	groupRBtn.add(metaGeneRBtn);
	 	groupRBtn.add(metaGeneRNKRBtn);

		
	 	layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(metaGeneRBtn))
	 		    .addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbMetaGeneAnalysis,0,GroupLayout.DEFAULT_SIZE,240)
	 		    		.addComponent(tfMetaGeneAnalysis)
	 		    		.addComponent(btnMetaGeneAnalysis))
    			.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(metaGeneRNKRBtn))
	    		.addGroup(layout.createSequentialGroup()
	 		    		.addComponent(lbMetaGeneRNKAnalysis,0,GroupLayout.DEFAULT_SIZE,240)
	 		    		.addComponent(tfMetaGeneRNKAnalysis,0,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE )
	 		    		.addComponent(btnMetaGeneRNKAnalysis))
	 			);

	 	layout.setVerticalGroup(layout.createSequentialGroup()
	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		        .addComponent(metaGeneRBtn))
	 		    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(lbMetaGeneAnalysis).addComponent(tfMetaGeneAnalysis).addComponent(btnMetaGeneAnalysis))
	 	 		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(metaGeneRNKRBtn))
 		    	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	 		    	.addComponent(lbMetaGeneRNKAnalysis).addComponent(tfMetaGeneRNKAnalysis).addComponent(btnMetaGeneRNKAnalysis))
	    		);
	 	
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
		else if(e.getSource() == metaGeneRBtn)
		{
			lbMetaGeneAnalysis.setEnabled(true);
			tfMetaGeneAnalysis.setEnabled(true);
			btnMetaGeneAnalysis.setEnabled(true);
			
			lbMetaGeneRNKAnalysis.setEnabled(false);
			tfMetaGeneRNKAnalysis.setEnabled(false);
			btnMetaGeneRNKAnalysis.setEnabled(false);
		}
		else if(e.getSource() == metaGeneRNKRBtn)
		{
			lbMetaGeneAnalysis.setEnabled(false);
			tfMetaGeneAnalysis.setEnabled(false);
			btnMetaGeneAnalysis.setEnabled(false);
			
			lbMetaGeneRNKAnalysis.setEnabled(true);
			tfMetaGeneRNKAnalysis.setEnabled(true);
			btnMetaGeneRNKAnalysis.setEnabled(true);
		}
		else if(e.getSource() == btnMetaGeneAnalysis)
		{	
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfMetaGeneAnalysis.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
		}
		else if(e.getSource() == btnClear)
		{
			tAConsole.setText("\n");
		}

		else if(e.getSource() == btnMetaGeneRNKAnalysis)
		{
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfMetaGeneRNKAnalysis.setText(fileChooser.getSelectedFile().getAbsolutePath());
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
				runMetaGeneWorker = new RunMetaGeneWorker(tAConsole, btnRunMethod, pbProgress, getMetaGeneValues(), this);
				runMetaGeneWorker.execute();
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
		if(!getMetaGeneValues().getDataTablePath().equals("")){
			df = new File(getMetaGeneValues().getDataTablePath());
			String analysisprefix = df.getName().substring(0, df.getName().length()-4);
			fn = cfDTO.getDefaultWorkFolderPath()+System.getProperty("file.separator")+analysisprefix+"_ICA"+System.getProperty("file.separator")+analysisprefix+"_ica_S.xls";
		}
		if(!getMetaGeneValues().getSTablePath().equals("")){
			fn = getMetaGeneValues().getSTablePath();
		}
		return fn;
	}
	
	
	
	private boolean emptyFieldExists()
	{
		if(metaGeneRBtn.isSelected())
			return tfDataTable.getText().trim().isEmpty() || tfMetaGeneAnalysis.getText().trim().isEmpty();
		return (tfDataTable.getText().trim().isEmpty()&&tfSTable.getText().trim().isEmpty()) ||tfMetaGeneRNKAnalysis.getText().trim().isEmpty();
	}
	
	private MetaGeneDTO getMetaGeneValues()
	{
		MetaGeneDTO metaGeneDTO = new MetaGeneDTO();
		metaGeneDTO.setDataTablePath(tfDataTable.getText().trim());
		metaGeneDTO.setSTablePath(tfSTable.getText().trim());
		metaGeneDTO.setDefaultWorkFolderPath(cfDTO.getDefaultWorkFolderPath().trim());
		
		if(metaGeneRBtn.isSelected())
		{
			metaGeneDTO.setGeneAnnotationFilePath(tfMetaGeneAnalysis.getText().trim());
		}
		else
		{
			metaGeneDTO.setGeneAnnotationFilePath(tfMetaGeneRNKAnalysis.getText().trim());
		}
		return metaGeneDTO;
	}	
}
