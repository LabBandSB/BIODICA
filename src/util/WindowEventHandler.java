package util;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class WindowEventHandler extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e)
	{
		 int confirmed = JOptionPane.showConfirmDialog(e.getWindow(), 
			        "Are you sure to close this window?", "Confirm Close?",
			        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			    if (confirmed == JOptionPane.YES_OPTION) {
			    	Container parentContainer = e.getWindow().getParent(); 			    	
			    	if( parentContainer != null)
			    	{
				    	SwingUtilities.updateComponentTreeUI(parentContainer);
			    	}
			    	e.getWindow().dispose();
			    }
	}
	
	
}
