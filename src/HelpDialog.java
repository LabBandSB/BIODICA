import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class HelpDialog extends JDialog implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JButton btnOK;
	
	/**
	 * Create the dialog.
	 */
	public HelpDialog(JFrame parent) {
		super(parent, "About BiODICA", true);
		setResizable(false);
		
		JPanel panel = new JPanel();
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("About.png"));
		setIconImage(mainIcon.getImage());
        panel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        panel.setBorder(new EmptyBorder(5, 5, 0, 5));
	       
        
        JLabel helpText = new JLabel("<html> <body><p style='width: 350px;'>Welcome to the BIODICA wiki! <br/> <br/> What is BIODICA? BIODICA - computational pipeline for Independent Component Analysis of BIg Omics Data. It is a collaboration project between Lab of Bioinformatics and Systems Biology (Center for Life Sciences, Nazarbayev University, Kazakhstan) and Computational Systems Biology of Cancer Lab (Institute Curie, France). Principal Investigators and leading researchers of BIODICA Project: Andrei Zinovyev and Ulykbek Kairov. <br/> <br/> CONTACTS All quires about BIODICA state and development should be sent to 1) Andrei Zinovyev  (<U>http://www.ihes.fr/~zinovyev</U>) 2) Ulykbek Kairov (<U>https://www.researchgate.net/profile/Ulykbek_Kairov)</U> <br/> <br/> BIODICA is a computational pipeline implemented in Java language for (1) automating deconvolution of large omics datasets with optimization of deconvolution parameters, (2) helping in interpretation of the results of deconvolution application by automated annotation of the compoentns using the best practices, (3) comparing the results of deconvolution of independent datasets for distinguishing reproducible signals, universal and specific for a particular disease/data type or subtype.</p></body></html>");
		
        panel.add(helpText);
		

        btnOK = new JButton("OK");
        btnOK.addActionListener(this);
        
        JPanel bp = new JPanel();
        bp.add(btnOK);
     
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
        
        
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(parent);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == btnOK)
		{
			dispose();
		}
	}
	

}
