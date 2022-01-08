import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.ConstantCodes;
import util.WindowEventHandler;



public class MainFrame extends JFrame implements ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private	JButton launchMethodBtn;
	private JComboBox<String> cBoxMethods;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle(ConstantCodes.TITLE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(400, 300, 450, 300);
		setBackground(new Color(220, 220, 220));
		
		
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		
		setIconImage(mainIcon.getImage());
		
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		this.addWindowListener(new WindowEventHandler());
		
		
		// North Panel
		JPanel northPanel = new JPanel();
		
		ImageIcon biodicaImg = new ImageIcon(getClass().getResource("BiODICA.png"));
		
		
		JLabel biodicaImgLbl = new JLabel(biodicaImg);
		biodicaImgLbl.setSize(100, 100);
		biodicaImgLbl.setBorder(new EmptyBorder (45, 135, 25, 135));
		northPanel.add(biodicaImgLbl);
				
		contentPane.add(northPanel, BorderLayout.NORTH);
		

		//Center Panel
		JPanel centerPanel = new JPanel(new GridBagLayout());
     	
     	GridBagConstraints lbMethodsgbc = new GridBagConstraints();
     	lbMethodsgbc.fill = GridBagConstraints.HORIZONTAL;
     	lbMethodsgbc.gridx = 0;
     	lbMethodsgbc.gridy = 4;
     	lbMethodsgbc.insets = new Insets(5, 15, 15,5);
     	JLabel lbMethods = new JLabel("Select Program");
     	lbMethods.setFont(new Font(ConstantCodes.FONT_FAMILY,Font.BOLD,ConstantCodes.FONT_SIZE));
     	centerPanel.add(lbMethods, lbMethodsgbc);   
     	
     	
    	GridBagConstraints dropDownMethodsgbc = new GridBagConstraints();
    	dropDownMethodsgbc.fill = GridBagConstraints.HORIZONTAL;
    	dropDownMethodsgbc.gridx = 1;
    	dropDownMethodsgbc.gridy = 4;
    	dropDownMethodsgbc.insets = new Insets(5, 5, 15, 5);
    	cBoxMethods = new JComboBox<String>();
    	cBoxMethods.addItem("1. IC's Computation");
    	cBoxMethods.addItem("2. GSEA Computation");
    	cBoxMethods.addItem("3. MetaSample Annotation");
    	cBoxMethods.addItem("4. MetaGene Annotation");
    	cBoxMethods.addItem("5. OFTEN Analysis");
    	cBoxMethods.addItem("6. toppGene Analysis");
    	cBoxMethods.addItem("7. Visualization on molecular maps");
    	cBoxMethods.addItem("8. RBH graph Construction");
     	centerPanel.add(cBoxMethods, dropDownMethodsgbc);   
     
     	GridBagConstraints launchMethodgbc = new GridBagConstraints();
     	launchMethodgbc.fill = GridBagConstraints.HORIZONTAL;
     	launchMethodgbc.gridx = 2;
     	launchMethodgbc.gridy = 4;
     	launchMethodgbc.insets = new Insets(5, 5, 15, 15);
     	launchMethodBtn = new JButton("<html><b>Launch</b></html>");
     	launchMethodBtn.addActionListener(this);
     	launchMethodBtn.setIcon(new ImageIcon(getClass().getResource("GSEA.png")));
     	centerPanel.add(launchMethodBtn, launchMethodgbc);
     	    	
     	//Add central panel to main panel
    	contentPane.add(centerPanel, BorderLayout.CENTER);
		
    	//South Panel
    	
    	JLabel copyrightLb = new JLabel("<html>© Copyright 2017-2021, Bioinformatics and Computational Systems Biology, Nazarbayev University, Kazakhstan / <br/> Computational Systems Biology of Cancer, Institute Curie, France</html>");
    	copyrightLb.setFont(new Font("Tahoma", Font.PLAIN, 10));
    	copyrightLb.setBorder(new EmptyBorder (45, 10, 5, 10));
    	
    	contentPane.add(copyrightLb, BorderLayout.SOUTH);
    	setContentPane(contentPane);
     	setJMenuBar(new MenuBar().CreateMenu(this));
     	pack();
     	setMinimumSize(getSize());
     	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == launchMethodBtn)
		{
			switch(cBoxMethods.getSelectedIndex())
			{
				case 0:
					IcaMethod iCAMethod = new IcaMethod(this);
					iCAMethod.setVisible(true);
					break;
				case 1:
					GSEAMethod gSEAMethod = new GSEAMethod(this);
					gSEAMethod.setVisible(true);
					break;
				case 2:
					MetaSampleMethod metaSampleMethod = new MetaSampleMethod(this);
					metaSampleMethod.setVisible(true);
					break;
				case 3:
					MetaGeneMethod metaGeneMethod = new MetaGeneMethod(this);
					metaGeneMethod.setVisible(true);
					break;
				case 4:
					OFTENMethod oftenMethod = new OFTENMethod(this);
					oftenMethod.setVisible(true);
					break;
				case 5:
					TOPPGeneAnalysis toppgene = new TOPPGeneAnalysis(this);
					toppgene.setVisible(true);
					break;	
				case 6:
					NaviCellVisualization navicell = new NaviCellVisualization(this);
					navicell.setVisible(true);
					break;
				case 7:
					BBHGraph bBHGraph = new BBHGraph(this);
					bBHGraph.setVisible(true);
				break;	
					
			}
		}
	}
}
