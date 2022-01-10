import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.FileDownloadWorker;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;



public class MainFrame_new extends JFrame implements ActionListener {

	private JPanel contentPane;
	
	private JButton btnRunICA;
	private JButton btnGsea;
	private JButton btnToppGene;
	private JButton btnOFTEN;
	private JButton btnNaviCell;
	private JButton btnMetagene;
	private JButton btnMetaSample;
	private JButton btnRBH;
	private JButton btnParameters;
	private JButton btnHelp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame_new frame = new MainFrame_new();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame_new() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width; 
		
		System.out.println("screenWidth="+screenWidth+",screenHeight="+screenHeight);
		
		//for (DisplayMode mode : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes())
		//{
		//  System.out.println(mode.getWidth() + " x " + mode.getHeight() + "(" + mode.getBitDepth() + ") : refresh rate " + mode.getRefreshRate());
		//}
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		System.out.println(mode.getWidth() + " x " + mode.getHeight() + "(" + mode.getBitDepth() + ") : refresh rate " + mode.getRefreshRate());
		
		setBounds(100, 100, 787, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		setIconImage(mainIcon.getImage());
		ImageIcon biodicaImg = new ImageIcon(getClass().getResource("BiODICA.png"));
		contentPane.setLayout(null);
		JLabel biodicaImgLbl = new JLabel(biodicaImg);
		biodicaImgLbl.setLocation(10, 0);
		biodicaImgLbl.setSize(450, 318);
		biodicaImgLbl.setBorder(new EmptyBorder (0, 0, 0, 0));
		contentPane.add(biodicaImgLbl);
		
		JLabel lblNewLabel = new JLabel("<html>\u00A9 Copyright 2017-2021, Bioinformatics and Computational Systems Biology, Nazarbayev University, Kazakhstan / <br/> Computational Systems Biology of Cancer, Institute Curie, France</html>");
		lblNewLabel.setBounds(0, 323, 805, 26);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		contentPane.add(lblNewLabel);
		
		//JButton btnNewButton = new JButton("Run ICA");
		// RUN ICA
		btnRunICA = new JButton();
		btnRunICA.addActionListener(this);
		btnRunICA.setBackground(SystemColor.controlHighlight);
		btnRunICA.setToolTipText("Compute ICA on a gene (rows) vs sample (columns) tab-delimited matrix");
		btnRunICA.setBounds(527, 11, 232, 61);
		ImageIcon runICAIcon = new ImageIcon(getClass().getResource("run_ica_icon1.png"));
		btnRunICA.setIcon(runICAIcon);
		btnRunICA.setIconTextGap(0);
		
		contentPane.add(btnRunICA);
		
		btnParameters = new JButton("Parameters");
		btnParameters.addActionListener(this);
		btnParameters.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnParameters.setBackground(SystemColor.controlHighlight);
		btnParameters.setBounds(527, 309, 118, 34);
		ImageIcon ParametersIcon = new ImageIcon(getClass().getResource("parameters_icon1.png"));
		btnParameters.setIcon(ParametersIcon);		
		contentPane.add(btnParameters);
		
		//GSEA
		btnGsea = new JButton();
		btnGsea.addActionListener(this);
		btnGsea.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnGsea.setBackground(SystemColor.controlHighlight);
		btnGsea.setBounds(527, 83, 71, 61);
		btnGsea.setToolTipText("Run GSEA enrichment analysis on ICA metagenes");
		ImageIcon runGSEAIcon = new ImageIcon(getClass().getResource("gsea_icon1.png"));
		btnGsea.setIcon(runGSEAIcon);		
		contentPane.add(btnGsea);
		
		// METAGENE
		//JButton btnNewButton_1_2 = new JButton("<html>MetaGene<br>Annotation</html>");
		btnMetagene = new JButton();
		btnMetagene.addActionListener(this);
		btnMetagene.setBackground(SystemColor.controlHighlight);
		btnMetagene.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnMetagene.setBounds(607, 155, 71, 61);
		btnMetagene.setToolTipText("Run analysis of ICA metagenes");
		ImageIcon runMetaGeneIcon = new ImageIcon(getClass().getResource("metagene_icon1.png"));
		btnMetagene.setIcon(runMetaGeneIcon);		
		contentPane.add(btnMetagene);
		
		// METASAMPLE
		//JButton btnNewButton_1_2_1 = new JButton("<html>MetaSample<br>Annotation</html>");
		btnMetaSample = new JButton();
		btnMetaSample.addActionListener(this);
		btnMetaSample.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnMetaSample.setBackground(SystemColor.controlHighlight);
		btnMetaSample.setBounds(688, 155, 71, 61);
		btnMetaSample.setToolTipText("Run analysis of ICA metasamples");
		ImageIcon runMetaSampleIcon = new ImageIcon(getClass().getResource("metasample_icon1.png"));
		btnMetaSample.setIcon(runMetaSampleIcon);		
		contentPane.add(btnMetaSample);
		
		// ToppGene
		//JButton btnNewButton_1_3 = new JButton("<html>toppGene</html>");
		btnToppGene = new JButton("");
		btnToppGene.addActionListener(this);
		btnToppGene.setBackground(SystemColor.controlHighlight);	
		btnToppGene.setToolTipText("Run ToppGene enrichment analysis on ICA metagenes");
		btnToppGene.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnToppGene.setBounds(608, 83, 71, 61);
		ImageIcon runToppGeneIcon = new ImageIcon(getClass().getResource("toppgene_icon1.png"));
		btnToppGene.setIcon(runToppGeneIcon);		
		contentPane.add(btnToppGene);
		
		// OFTEN
		//JButton btnNewButton_1_2_2 = new JButton("OFTEN");
		btnOFTEN = new JButton();
		btnOFTEN.addActionListener(this);
		btnOFTEN.setBackground(SystemColor.controlHighlight);
		btnOFTEN.setToolTipText("Run OFTEN analysis on ICA metagenes");
		btnOFTEN.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnOFTEN.setBounds(688, 83, 71, 61);
		ImageIcon runOFTENIcon = new ImageIcon(getClass().getResource("often_icon1.png"));
		btnOFTEN.setIcon(runOFTENIcon);		
		contentPane.add(btnOFTEN);
		
		// NAVICELL
		//JButton btnNewButton_1_2_1_1 = new JButton("<html>NaviCell</html>");
		btnNaviCell = new JButton();
		btnNaviCell.addActionListener(this);
		btnNaviCell.setBackground(SystemColor.controlHighlight);
		btnNaviCell.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNaviCell.setToolTipText("Visualize ICA metagenes on top of molecular maps");
		btnNaviCell.setBounds(527, 155, 71, 61);
		ImageIcon runNAVICELLIcon = new ImageIcon(getClass().getResource("navicell_icon1.png"));
		btnNaviCell.setIcon(runNAVICELLIcon);		
		contentPane.add(btnNaviCell);
		
		// RBH
		//JButton btnNewButton_1_4 = new JButton("<html>RBH graph</html>");
		btnRBH = new JButton();
		btnRBH.addActionListener(this);
		btnRBH.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnRBH.setToolTipText("Compute RBH graph for a set of ICA decompositions");
		btnRBH.setBackground(SystemColor.controlHighlight);
		btnRBH.setBounds(527, 227, 71, 61);
		ImageIcon runRBHIcon = new ImageIcon(getClass().getResource("rbh_icon1.png"));
		btnRBH.setIcon(runRBHIcon);		
		contentPane.add(btnRBH);
		
		btnHelp = new JButton("Help");
		btnHelp.addActionListener(this);
		btnHelp.setBounds(655, 309, 104, 34);
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnHelp.setBackground(SystemColor.controlHighlight);
		ImageIcon runHelpIcon = new ImageIcon(getClass().getResource("help_icon1.png"));
		btnHelp.setIcon(runHelpIcon);		
		contentPane.add(btnHelp);
		
     	//pack();
     	setMinimumSize(getSize());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnRunICA)
		{
			IcaMethod iCAMethod = new IcaMethod(this);
			iCAMethod.setVisible(true);
		}
		if(e.getSource() == btnGsea)
		{
			GSEAMethod gSEAMethod = new GSEAMethod(this);
			gSEAMethod.setVisible(true);
		}
		if(e.getSource() == btnToppGene)
		{
			TOPPGeneAnalysis toppgene = new TOPPGeneAnalysis(this);
			toppgene.setVisible(true);
		}
		if(e.getSource() == btnOFTEN)
		{
			OFTENMethod oftenMethod = new OFTENMethod(this);
			oftenMethod.setVisible(true);
		}
		if(e.getSource() == btnNaviCell)
		{
			NaviCellVisualization navicell = new NaviCellVisualization(this);
			navicell.setVisible(true);
		}		
		if(e.getSource() == btnMetagene)
		{
			MetaGeneMethod metaGeneMethod = new MetaGeneMethod(this);
			metaGeneMethod.setVisible(true);
		}
		if(e.getSource() == btnMetaSample)
		{
			MetaSampleMethod metaSampleMethod = new MetaSampleMethod(this);
			metaSampleMethod.setVisible(true);
		}		
		if(e.getSource() == btnRBH)
		{
			BBHGraph bBHGraph = new BBHGraph(this);
			bBHGraph.setVisible(true);
		}		
		if(e.getSource() == btnParameters)
		{
			 ConfigDialogNew configDialog = new ConfigDialogNew(this);
			 configDialog.setVisible(true);
		}
		if(e.getSource() == btnHelp)
		{
			//HelpDialog helpDialog = new HelpDialog(this);
			//helpDialog.setVisible(true);
			FileDownloadWorker fileDownloadWorker= new FileDownloadWorker("BIODICA_manual.pdf",this);
			fileDownloadWorker.execute();
		}
		
	}
	
	
	
}
