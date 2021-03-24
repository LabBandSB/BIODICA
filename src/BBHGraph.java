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
import model.BBHGraphDTO;
import util.WindowEventHandler;


public class BBHGraph extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JTextField tfFinalGraph;
	private JButton btnFinalGraph,btnRunMethod, btnClear;
	private JScrollPane sptAConsole;
	private JProgressBar pbProgress;
	private JTextArea tAConsole;
	private RunBBHGraph runBBHGraph;
	
	//Default values
	public String DEFAULT_FINAL_GRAPH_PATH = "";

	public BBHGraph(JFrame parent) 
	{
		super(parent, "RBH Graph Construction", true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		setIconImage(mainIcon.getImage());
	
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		this.addWindowListener(new WindowEventHandler());
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
	 	JLabel lbTitle = new JLabel("<html><b>Type of the Analysis:</b> RBH Graph Construction </html>");
	 	contentPane.add(lbTitle, csLbTitle);
		
 	 	//Second row - FinalGraph path
	 	
	 	GridBagConstraints csLbFinalGraph = new GridBagConstraints();
	 	csLbFinalGraph.fill = GridBagConstraints.HORIZONTAL;
	 	csLbFinalGraph.insets = new Insets(10,10,10,10);
	 	csLbFinalGraph.gridx = 0;
	 	csLbFinalGraph.gridy = 1;
	 	csLbFinalGraph.gridwidth = 1; 
	 	csLbFinalGraph.weighty = 1.0;
	 	JLabel lbFinalGraph = new JLabel("Specify folder with *_S.xls files*");
	 	contentPane.add(lbFinalGraph, csLbFinalGraph);
		 	
	 	GridBagConstraints csTfFinalGraph =  new GridBagConstraints();
	 	csTfFinalGraph.fill = GridBagConstraints.HORIZONTAL;
	 	csTfFinalGraph.insets = new Insets(10,10,10,10);
	 	csTfFinalGraph.gridx = 1;
	 	csTfFinalGraph.gridy = 1;
	 	csTfFinalGraph.gridwidth = 1; 
	 	csTfFinalGraph.weightx = 1.0;
	 	csTfFinalGraph.weighty = 1.0;
	 	tfFinalGraph = new JTextField(30);
	 	tfFinalGraph.setText(DEFAULT_FINAL_GRAPH_PATH);
	 	contentPane.add(tfFinalGraph, csTfFinalGraph);
	
	 	GridBagConstraints csBtnFinalGraph =  new GridBagConstraints();
	 	csBtnFinalGraph.fill = GridBagConstraints.HORIZONTAL;
	 	csBtnFinalGraph.insets = new Insets(10,10,10,10);
	 	csBtnFinalGraph.gridx = 3;
	 	csBtnFinalGraph.gridy = 1;
	 	csBtnFinalGraph.gridwidth = 1; 
	
	 	btnFinalGraph =  new JButton("Browse");
	 	btnFinalGraph.setIcon(new ImageIcon(getClass().getResource("Browse.png")));
	 	btnFinalGraph.addActionListener(this);
	 	contentPane.add(btnFinalGraph, csBtnFinalGraph);

	 	
	 	
	 	// Output text area 
  		GridBagConstraints csTAconsole = new GridBagConstraints();
  		csTAconsole.ipady = 1;
  		csTAconsole.fill = GridBagConstraints.BOTH;
  		csTAconsole.insets = new Insets(10,10,0,10);
  		csTAconsole.gridx = 0;
  		csTAconsole.gridy = 2;
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
  		csProgressBar.gridy = 3;
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
		if (e.getSource() == btnFinalGraph) 
		{
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            tfFinalGraph.setText(fileChooser.getSelectedFile().getAbsolutePath());
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
				runBBHGraph = new RunBBHGraph(tAConsole, btnRunMethod, pbProgress, getBBHGraphValues(), this);
				runBBHGraph.execute();
				this.setEnabled(false);	
			}
		}
	}
	
	private boolean emptyFieldExists()
	{
		return tfFinalGraph.getText().trim().isEmpty();
	}
	
	private BBHGraphDTO getBBHGraphValues()
	{
		BBHGraphDTO bBHGraphDTO = new BBHGraphDTO();
		bBHGraphDTO.setTfFinalGraph(tfFinalGraph.getText().trim());
		return bBHGraphDTO;
	}
	
}
