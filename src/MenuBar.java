import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import model.ConstantCodes;
import util.FileDownloadWorker;


public class MenuBar implements ActionListener
{
	private JMenuItem exit, about, config, userManual;
	private JFrame jParent;
	private FileDownloadWorker fileDownloadWorker;


	public  JMenuBar CreateMenu(JFrame parent) 
	{
		jParent = parent;
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu ("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
	
		
		config = new JMenuItem("Config",KeyEvent.VK_C);
		config.setIcon(new ImageIcon(getClass().getResource("Config.png")));
		config.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.ALT_MASK));
		config.setToolTipText("Set config file pa64rameters");
		config.addActionListener(this);
		fileMenu.add(config);
		

		fileMenu.addSeparator();	
		exit = new JMenuItem("Exit", KeyEvent.VK_E);
		exit.setIcon(new ImageIcon(getClass().getResource("Exit.png")));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.ALT_MASK));
		exit.addActionListener(this);
		exit.setToolTipText("Exit application");
		
		fileMenu.add(exit);
		
			
		//Help Menu
		
		JMenu helpMenu = new JMenu ("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		userManual = new JMenuItem("User Manual", new ImageIcon(getClass().getResource("UserManual.png")));
		userManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.ALT_MASK));
		userManual.addActionListener(this);
		helpMenu.add(userManual);
			
		about = new JMenuItem("About BiODICA", new ImageIcon(getClass().getResource("About.png")));
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.ALT_MASK));
		about.addActionListener(this);
		helpMenu.add(about);
		
		menuBar.add(helpMenu);
		
		return menuBar;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == config)
		{
			 ConfigDialog configDialog = new ConfigDialog(jParent);
			 configDialog.setVisible(true);
		}
		else if(e.getSource() == exit)
		{
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to exit?",ConstantCodes.TITLE,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(reply == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
		}
		else if(e.getSource() == userManual)
		{	
			fileDownloadWorker= new FileDownloadWorker("BIODICA_manual.pdf",jParent);
			fileDownloadWorker.execute();
			jParent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else if(e.getSource() == about)
		{
			HelpDialog helpDialog = new HelpDialog(jParent);
			helpDialog.setVisible(true);
		}
	}

}
