package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

public class RunScriptWorker extends SwingWorker<Boolean, String>  {
	
	private Process scriptProcess;
	private JTextArea tAConsole;
	private String[] script;
	private JButton btnRunScript;
	private JProgressBar pbProgress;
	
	public RunScriptWorker(JTextArea tAConsole, JButton btnRunScript,JProgressBar pbProgress, String[] script)
	{
		this.tAConsole = tAConsole;
		this.btnRunScript = btnRunScript;
		this.script = script;
		this.pbProgress = pbProgress;
	}
	
	@Override
	protected Boolean doInBackground() throws Exception {
		try
		{
			ProcessBuilder builder = new ProcessBuilder(script);
			builder.redirectErrorStream(true);
			scriptProcess = builder.start();
			
			BufferedReader r = new BufferedReader(new InputStreamReader(scriptProcess.getInputStream()));
			
			pbProgress.setIndeterminate(true);
			Border progressBorder = BorderFactory.createTitledBorder("Running...");
			pbProgress.setBorder(progressBorder);
					
			String line = null;			
			while ((line = r.readLine()) != null && !isCancelled()) 
			{
			    publish(line);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return true;
	}

	@Override
	protected void process(List<String> outputs) 
	{
		for (final String output : outputs) 
		{
			tAConsole.append(output + "\n");
	    }
	}

	@Override
	protected void done() {
		if(scriptProcess!= null)
		{
			scriptProcess.destroyForcibly();
			scriptProcess.destroy();
		}
		btnRunScript.setEnabled(true);
		pbProgress.setIndeterminate(false);
		Border progressBorder = BorderFactory.createTitledBorder("");
		pbProgress.setBorder(progressBorder);
		super.done();
	}
}
