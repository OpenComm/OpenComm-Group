package opencomm.ss.magellan;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;



/** Main: this demonstrates a conference with 3 people */
public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		JFrame f =new JFrame("SS Demo -- OpenComm.Magellan -- Private Space Mode");
		//quit Java after closing the window
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 400); //set size in pixels
		f.setResizable(false); // don't make the window resizable
		
		// make panels of 5 x 2
		JPanel p = new JPanel(new GridLayout(4, 2));
		
		// add buttons
		final JToggleButton b1 = new JToggleButton("All My Loving");
		b1.setSelected(true);
		final JToggleButton b2 = new JToggleButton("It Won't Be Long");
		b2.setSelected(true);
		final JToggleButton b3 = new JToggleButton("Come Together");
		b3.setSelected(true);
		final JToggleButton b4 = new JToggleButton("Hold Me Tight");
		b4.setSelected(true);
		
		// add text
		final JTextField t1 = new JTextField("In PSpace");
		t1.setHorizontalAlignment(JTextField.CENTER);
		final JTextField t2 = new JTextField("In PSpace");
		t2.setHorizontalAlignment(JTextField.CENTER);
		final JTextField t3 = new JTextField("In PSpace");
		t3.setHorizontalAlignment(JTextField.CENTER);
		final JTextField t4 = new JTextField("In PSpace");
		t4.setHorizontalAlignment(JTextField.CENTER);

		
		String dir = "C:/Documents and Settings/risa/My Documents/workspace/OpenComm_Java_SS/";
		// make AudioSS instances for each button
		final AePlaySS apw1 = new AePlaySS(dir + "AllMyLoving.wav", 0, 20);
		apw1.start();
		final AePlaySS apw2 = new AePlaySS(dir + "ItWontBeLong.wav", 1200, 20);
		apw2.start();
		final AePlaySS apw3 = new AePlaySS(dir + "ComeTogether.wav", 0, 460);
		apw3.start(); 
		final AePlaySS apw4 = new AePlaySS(dir + "HoldMeTight.wav", 800, 460);
		apw4.start();

		// add buttons
		p.add(b1);
		p.add(b2);
		p.add(t1);
		p.add(t2);
		p.add(t3);
		p.add(t4);
		p.add(b3);
		p.add(b4);
		f.add(p);
		
		// when buttons get pressed, respective sounds are streamed
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (b1.isSelected()) {
					t1.setText("In PSpace");
					apw1.enterPSpace();
				}
				else {
					t1.setText("Not in PSpace");
					apw1.leavePSpace();
				}
			}
		});
		
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (b2.isSelected()) {
					t2.setText("In PSpace");
					apw2.enterPSpace();
				}
				else {
					t2.setText("Not in PSpace");
					apw2.leavePSpace();
				}
			}
		});
		
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (b3.isSelected()) {
					t3.setText("In PSpace");
					apw3.enterPSpace();
				}
				else {
					t3.setText("Not in PSpace");
					apw3.leavePSpace();
				}
			}
		});
		
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (b4.isSelected()) {
					t4.setText("In PSpace");
					apw4.enterPSpace();
				}
				else {
					t4.setText("Not in PSpace");
					apw4.leavePSpace();
				}
			}
		});
		
		f.setVisible(true); //show the window
	}


}
