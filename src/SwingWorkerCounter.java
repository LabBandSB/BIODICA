import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
 
/** Test SwingWorker on the counter application with a compute-intensive task */
@SuppressWarnings("serial")
public class SwingWorkerCounter extends JPanel {
   // For counter
   private JTextField tfCount;
   private int count = 0;
   private boolean stop = false;
   // For SwingWorker
   JButton btnStartWorker;   // to start the worker
   JButton btnStopWorker;   // to start the worker
   private JLabel lblWorker; // for displaying the result
 
   /** Constructor to setup the GUI components */
   public SwingWorkerCounter () {
      setLayout(new FlowLayout());
 
      add(new JLabel("Counter"));
      tfCount = new JTextField("0", 10);
      tfCount.setEditable(false);
      add(tfCount);
 
      JButton btnCount = new JButton("Count");
      add(btnCount);
      btnCount.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ++count;
            tfCount.setText(count + "");
         }
      });
 
      /** Create a SwingWorker instance to run a compute-intensive task 
          Final result is String, no intermediate result (Void) */
      final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
         /** Schedule a compute-intensive task in a background thread */
         @Override
         protected String doInBackground() throws Exception {
            // Sum from 1 to a large n
            long sum = 0;
            for (int number = 1; number < 1000000000; ++number) {
               sum += number;
               sum = sum/10;
               sum = sum*10;
               sum = sum/100;
               sum = sum*100;
               sum = sum/100;
               sum = sum*100;
            }
            return sum + "";
         }
         
         //public void cancel() {
        //	 lblWorker.setText("Cancelling... ");
        //	 //stop = true;
         //}
 
         /** Run in event-dispatching thread after doInBackground() completes */
         @Override
         protected void done() {
        	 
        	System.out.println("in done...");
        	
            try {
               // Use get() to get the result of doInBackground()
               String result = get();               
               // Display the result in the label (run in EDT)
               lblWorker.setText("Result is " + result);
               btnStartWorker.setEnabled(false); 
            } catch (InterruptedException e) {
               e.printStackTrace();
            } catch (ExecutionException e) {
               e.printStackTrace();
            } catch (CancellationException e) {
              System.out.println("The process has been cancelled");
              lblWorker.setText("Cancelled");
            }
            
         }
      };
 
      btnStartWorker = new JButton("Start Worker");
      add(btnStartWorker);
      btnStartWorker.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
         //try {
            worker.execute();                 // start the worker thread
         //} catch (CancellationException ce){
      	 //  stop = true;
         //}
            
            lblWorker.setText("  Running...");
            btnStartWorker.setEnabled(false); // Each instance of SwingWorker run once
         }
      });
      btnStopWorker = new JButton("Stop Worker");
      add(btnStopWorker);
      btnStopWorker.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
        	 worker.cancel(true);
         }
      });
      
      lblWorker = new JLabel("  Not started...");
      add(lblWorker);
 
   }
 
   /** The entry main() method */
   public static void main(String[] args) {
      // Run the GUI construction in the Event-Dispatching thread for thread-safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            JFrame frame = new JFrame("SwingWorker Test");
            frame.setContentPane(new SwingWorkerCounter());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 150);
            frame.setVisible(true);
         }
      });
   }
}