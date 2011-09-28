package opencomm.rtp.oldraj;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/** Main method: an instance of this class calls
 * the tech demo for rtp parallel session */
public class DemoRTP {
	/** main method 
	 * @throws IOException */
	/** Create-show GUI.  For thread safety, invoke from the event-dispatching thread. */
    private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new GuiRTP();
    }
	
	public static void main(String[] args) {
		// ... schedule a job for the 'event-dispatching thread'
        // ... creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater (new Runnable()  {
			public void run() {
				createAndShowGUI();
			}
        });
	} // end main method
	
	

} // end class DemoRTP
