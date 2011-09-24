package opencomm.rtp.oldraj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/** An instance of this class creates the GUI for the RTP tech demo */
public class GuiRTP implements ActionListener {
	private JFrame jf;
	private String frameLbl = "Sprint 5: Parallel RTP Demo";
	private Box confBox, userBox;
	private JLabel numUsersLbl, ipAddLbl;
	private JTextField numUInput;
	private JButton submitNumU, startDemo;
	private boolean created;
	private int numUsers;
	private JTextField[] allIPTxt;
	private String[] allUsers;
	
	public int getNumUsers() {
		int numU = 0;
		return numU;
	}
	
	/** Constructor: a new instance of GUI */
	public GuiRTP() {
		jf = new JFrame(frameLbl);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(200, 200);
		
		confBox = new Box(BoxLayout.Y_AXIS);
		userBox = new Box(BoxLayout.X_AXIS);
		// ask for number of users in the demo
		numUsersLbl = new JLabel("How many users in this demo?");
		numUsers = 0;
		submitNumU = new JButton("Submit");
		submitNumU.addActionListener(this);
		numUInput = new JTextField(5);
		userBox.add(numUsersLbl);
		userBox.add(numUInput);
		userBox.add(submitNumU);
		confBox.add(userBox);
		jf.add(confBox);
		jf.pack();
		jf.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitNumU) {
			if (!created) {
				try {
					numUsers = Integer.parseInt(numUInput.getText());
					if (numUsers > 1) {
						ipAddLbl = new JLabel("Please input IP Addresses of all users (xxx.xxx.x.xxx)",JLabel.LEFT);
						confBox.add(ipAddLbl);
						allIPTxt = new JTextField[numUsers];
						for (int i = 1; i <= numUsers; i++) {
							Box ip = new Box(BoxLayout.X_AXIS);
							JLabel ipLbl = new JLabel("User " + i + ": ");
							JTextField ipTxt = new JTextField(20);
							allIPTxt[i-1]=ipTxt;
							ip.add(ipLbl);
							ip.add(ipTxt);
							confBox.add(ip);	
						}
						created = true;
						startDemo = new JButton("Start Demo");
						startDemo.addActionListener(this);
						confBox.add(startDemo);
						jf.pack();
					}
					else {
						JOptionPane.showMessageDialog(this.jf, "You need at least 2 users.", "Oops!", JOptionPane.ERROR_MESSAGE);
					}
				}
				catch (NumberFormatException err) {
					JOptionPane.showMessageDialog(this.jf, "Not a valid input.\nInput a natural number greater than 2" + 
							"\n(2, 3, 4...).", "Oops!", JOptionPane.ERROR_MESSAGE);
					numUInput.setText("");
				}
			}
		}
		if (e.getSource() == startDemo) {
			allUsers = new String[numUsers];
			for (int i = 0; i < this.allIPTxt.length; i++) {
				allUsers[i] = this.allIPTxt[i].getText();
			}
			Arrays.sort(allUsers);
			System.out.println(Arrays.toString(allUsers));
			AudioConnect ac = new AudioConnect(allUsers);
		}
		
	}
	
	
} // end class GuiRTP
