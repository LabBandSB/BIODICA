import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import util.RunScriptWorker;


public class ScriptDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnRunScript,btnStopScript, btnClear;
	private JTextArea tAConsole;
	private JScrollPane sptAConsole;
	private RunScriptWorker runScriptWorker;
	private String[] script;
	private JProgressBar pbProgress;

	
	public ScriptDialog(JFrame parent,JDialog parentDialog, String title,String[] script) 
	{
		super(parent, title, true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.script = script;
		ImageIcon mainIcon = new ImageIcon(getClass().getResource("BiODICA.png"));
		setIconImage(mainIcon.getImage());

		/*addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {               
            	if (JOptionPane.showConfirmDialog(e.getWindow(), 
                        "Are you sure to close this window?", "Confirm Close?", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            			{
            				if(parentDialog instanceof IcaMethod)
            				{
            					((IcaMethod)parentDialog).BindData();
            				}
	            			stopWorkingProcess();
					    	SwingUtilities.getWindowAncestor(parentDialog).revalidate(); 					    	
	            			e.getWindow().dispose();
            			}
            }
        });*/
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane = new JPanel(new GridBagLayout());
				
		// Output text area 
		GridBagConstraints csTAconsole = new GridBagConstraints();
		csTAconsole.ipady = 1;
		csTAconsole.fill = GridBagConstraints.BOTH;
		csTAconsole.insets = new Insets(10,10,0,10);
		csTAconsole.gridx = 0;
		csTAconsole.gridy = 0;
		csTAconsole.gridwidth = 1; 	
		csTAconsole.weightx = 1.0;
		csTAconsole.weighty = 2.0;
		
		tAConsole = new JTextArea(20,80);

		
		tAConsole.append(System.getProperty("user.dir") + "> " + String.join(" ", script) + "\n");
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
		csProgressBar.gridy = 1;
		csProgressBar.gridwidth = 1;
		
		pbProgress = new JProgressBar();
		pbProgress.setIndeterminate(false);
		Border progressBorder = BorderFactory.createTitledBorder("");
		pbProgress.setBorder(progressBorder);
		pbProgress.setVisible(true);
	
	
		contentPane.add(pbProgress,csProgressBar);
		
	 	
	 	// Clear Button
	 	JPanel btnsPanel = new JPanel(new BorderLayout());
	 	btnsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
	 	
	 	JPanel clearPanel = new JPanel(new FlowLayout());
	 	
	 	btnClear = new JButton("Clear");
	 	btnClear.setIcon(new ImageIcon(getClass().getResource("Clear.png")));
	 
	 	btnClear.addActionListener(this);
	 	
	 	clearPanel.add(btnClear);
	 	
	    // Run and Stop Buttons
	 		 	
	 	btnRunScript = new JButton("Run");
	 	btnRunScript.setIcon(new ImageIcon(getClass().getResource("Run.png")));

	 	btnRunScript.addActionListener(this);
     	btnStopScript = new JButton("Stop");
	 	btnStopScript.setIcon(new ImageIcon(getClass().getResource("Stop.png")));
     	btnStopScript.addActionListener(this);
     	
	 	
	 	JPanel runStopPanel = new JPanel(new FlowLayout());
	 	runStopPanel.add(btnRunScript);
	 	runStopPanel.add(btnStopScript);
	 	

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
		if(e.getSource() == btnRunScript)
		{		
			runScriptWorker = new RunScriptWorker(tAConsole, btnRunScript,pbProgress, script);
			runScriptWorker.execute();
			btnRunScript.setEnabled(false);
		}
		else if(e.getSource() == btnStopScript)
		{
			stopWorkingProcess();
		}
		else if(e.getSource() == btnClear)
		{
			tAConsole.setText(System.getProperty("user.dir") + "> " + String.join(" ", script) + "\n");
		}
	}
	
	public void stopWorkingProcess()
	{
		if(runScriptWorker != null)
		{
			runScriptWorker.cancel(true);
		}	
	}
	
}
