package ss;

import java.util.Arrays;

/** An instance of this class spatializes the source based on the 
 * position of the source. radius of the head, # bytes per millisecond,
 * speed of sound, height and width of conference window is predetermined.
 * Spaces are assigned as follows:
 * Main Window - #0
 * Private Space n - #n
 */
public class AudioSS {
	private int[] xPos; // source's x-coord relative to listener for each space
	private int[] yPos; // source's y-coord relative to listener for each space
	private float[] balance; // balance between the 2 stereo speakers; -1.0 (L) to 1.0 (R) for each space
	private float[] gain; // volume for each space
	private int[] itd; // interaural time delay in bytes for each space
	private double[] itdMS; // interaural time delay in millisecond for each space
	private static int numSpace = 3; // # spaces (1 + # private spaces)
	private static int msToByte = 16; // # bytes per millisecond
	private static double headRad = 0.085; // radius of a head in meters
	private static double speedSound = 343.42; // speed of sound
	private static int height = 245; // height of conference screen
	private static int width = 480; // width of conference screen
	
	/** = source position's x-coord relative to listener */
	private static int getRelXPos(int x) {
		if (x > 0) {
			int rX = x - AudioSS.width/2;
			return rX;
		}
		else {
			return x;
		}
	} // end getRelXPos method
	
	/** = source position's y-coord relative to listener */
	private static int getRelYPos(int y) {
		if (y > 0) {
			int rY = AudioSS.height - y;
			return rY;
		}
		else {
			return y;
		}
	} // end getRelXPos method
	
	/** set the interaural time difference in bytes for the space
	 * assigned number sn  */
	private void setITD(int sn) {
		// check if the coordinates are valid
		if (this.isInSpace(sn)) {
			double d = Math.sqrt(Math.pow(this.xPos[sn], 2) + Math.pow(this.yPos[sn], 2));
			double theta = Math.PI/2 - Math.acos(this.xPos[sn]/d);
			// determine itd in milliseconds
			this.itdMS[sn] = 1000 * AudioSS.headRad/AudioSS.speedSound * (theta + Math.sin(theta));
			int itdByte = (int) (itdMS[sn] * AudioSS.msToByte);
			// set this audioSS's itd to itdByte
			System.out.println("itdByte is " + itdByte);
			// as the audio used is 16-bit, itdByte must be an even number
			if ((itdByte % 2) != 0) {
				if (itdByte > 0) {
					itdByte++;
					System.out.println("fixed: itdByte is " + itdByte);
				}
				else {
					itdByte--;
					System.out.println("fixed: itdByte is " + itdByte);
				}
			}
			this.itd[sn] = itdByte;
		}
		// if this sound source exist within the conference
		else if (this.isInSpace(0) && sn != 0) {
			this.itd[sn] = this.itd[0];
		}
		else {
			System.out.println("This audio source does not belong in the conference");
		}
	} // end getITD method
	
	/** calculate balance between the 2 speakers for the space #sn */
	private void setBalance(int sn) {
		// check if it exists in the space 
		if (this.isInSpace(sn)) {
			// find distance difference between left and right ear in m
			double dd = (this.itdMS[sn]/1000 * AudioSS.speedSound);
			// multiply dd by 4
			float bal = (float) (dd * 4);
			this.balance[sn] = bal;
		}
		// if it exists in the main conference, set balance to that of main conference location
		else if (this.isInSpace(0) && sn != 0) {
			this.balance[sn] = this.balance[0];
		}
		else {
			System.out.println("This audio source does not belong in the conference");
		}
	} // end calcBalance method
	
	/** set gain of the audio for space #sn*/
	private void setGain(int sn) {
		// check that it exists in the space
		if (this.isInSpace(sn)) {
			// find distance from source
			double d = Math.sqrt(Math.pow(this.xPos[sn], 2) + Math.pow(this.yPos[sn], 2));
			// divide distance by 40
			float gn = (float) (d / - 40);
			this.gain[sn] = gn;
		}
		// if it exists in the main conference, then its gain at pspace mode is 1/2
		else if (this.isInSpace(0) && sn != 0) {
			this.gain[sn] = this.gain[0] / 2;
		}
		else {
			System.out.println("This audio source does not belong in the conference");
		}
	} // end getGain method
	
	/** set spatialization parameters for space #sn */
	private void setSS(int sn) {
		this.setITD(sn);
		this.setBalance(sn);
		this.setGain(sn);
	} // end setSS method
	
	/** = private:: a byte array with the itd added before the source
	 *  for space #sn */
	private byte[] addITDBefore(int sn, byte[] source) {
		int itdVal = Math.abs(this.itd[sn]);
		int len = source.length + itdVal;
		// create new channel
		byte[] added = new byte[len];
		// add silence before source
		for (int i = 0; i < itdVal; i++) {
			added[i] = 0; // value of silence in signed 16bit: 0
		}
		// add source
		for (int i = 0; i < source.length; i++) {
			added[i+itdVal] = source[i];
		}
		return added;
	} // end addITDBefore method
	
	/** = private:: a short array with the itd added after the source
	 * for space #sn */
	private byte[] addITDAfter(int sn, byte[] source) {
		int itdVal = Math.abs(this.itd[sn]);
		int len = source.length + itdVal;
		// create new channel
		byte[] added = new byte[len];
		// add silence after source
		for (int i = 0; i < itdVal; i++) {
			added[i + source.length] = 0; // value of silence in signed 16bit: 0
		}
		// add source
		for (int i = 0; i < source.length; i++) {
			added[i] = source[i];
		}
		return added;
	} // end addITDBefore method
	
	/** = private:: stereo stream created from two mono streams left and right */
	private static byte[] monoToStereo(byte[] left, byte[] right) {
		// check that both left and right mono streams are of equal length
		if (left.length != right.length) {
			return null;
		}
		byte[] output = new byte[left.length * 2];
		// put mono channels into respective left and right channel
		for (int i = 0; i < left.length; i = i + 2) {
			output[(i * 2)] = left[i]; // put mono channel into left stereo channel
			output[(i * 2) + 1] = left[i + 1]; // put mono channel into left stereo channel
			output[(i * 2) + 2] = right[i]; // put mono channel into left stereo channel
			output[(i * 2) + 3] = right[i + 1]; // put mono channel into left stereo channel
		}
		return output;
	} // end monoToStereo method
	
	/** = move source to coordinate (x, y) in space #sn */
	public void moveTo(int sn, int x, int y) {
		this.xPos[sn] = AudioSS.getRelXPos(x);
		this.yPos[sn] = AudioSS.getRelYPos(y);
		// update ITD and Volume
		this.setSS(sn);
	} // end moveTo method
	
	/** = this AudioSS's balance for space #sn */
	public float getBalance(int sn) {
		return this.balance[sn];
	}
	
	/** = this AudioSS's gain for space #sn */
	public float getGain(int sn) {
		return this.gain[sn];
	}
	
	/** = a SS-modified stereo source for space #sn */
	public byte[] spatialize(int sn, byte[] source) {
		byte[] left = new byte[0];
		byte[] right = new byte[0];
		// if the sound source is left of the user
		if (this.itd[sn] < 0) {
			left = this.addITDAfter(sn, source);
			right = this.addITDBefore(sn, source);
		}
		// if the sound source is right of the user
		else if (this.itd[sn] > 0) {
			left = this.addITDAfter(sn, source);
			right = this.addITDBefore(sn, source);
		}
		// if the sound source is right in front of the user; no itd
		else {
			left = source.clone();
			right = source.clone();
		}
		// create stereo stream
		byte[] stereo = AudioSS.monoToStereo(left, right);
		return stereo;
	} // end spatialize method

	/** = "this audio source does exist in space #sn" */
	private boolean isInSpace(int sn) {
		return (this.xPos[sn] > 0 && this.yPos[sn] > 0);
	} // end isInSpace method
	
	/** = String representation of this AudioSS
	 * including source position, ITD, balance, gain, head radius, and speed of sound
	 */
	public String toString() {
		String state = "AudioSS:\n";
		for (int i = 0; i < AudioSS.numSpace; i++) {
			if (i == 0) {
				state += "\tMain Window:\n";
			}
			else {
				state += "\tPrivate Space " + i + ":\n";
			}
			// add source position info
			state+= "\tsource position: (" + (this.xPos[i] + AudioSS.width/2) + ", " + (AudioSS.height - this.yPos[i]) + ")\n";
			// add relative position info
			state+= "\trelative position: (" + this.xPos[i] + ", " + this.yPos[i] + ")\n";
			// add ITD info in bytes and millisecond
			state += "\titd: " + this.itd[i] + " bytes/ " + ((float)this.itd[i]/16) + " (ms)\n";
			// add Balance and Gain info
			state += "\tbalance: " + this.balance[i] + ", gain: " + this.gain[i] + "dB\n";
		}
		// add Predetermined Factors
		state += "\t\tHead Radius: " + AudioSS.headRad * 100 + 
		"cm, Speed of Sound: " + AudioSS.speedSound + "m/s\n";
		state += "\t\t8000Hz, signed 16-bit, stereo";
		return state;
	}
	
	/** Constructor: creates an instance of AudioSS based off of
	 * source position for each space, stored in int arrays x and y
	 * :: (0,0) is located top left of conference window
	 */
	public AudioSS(int[] x, int[] y) {
		// check that both x and y have the correct number of arrays
		if (x.length == AudioSS.numSpace && y.length == AudioSS.numSpace) {
			this.balance = new float[AudioSS.numSpace];
			this.gain = new float[AudioSS.numSpace];
			this.itd = new int[AudioSS.numSpace];
			this.itdMS = new double[AudioSS.numSpace];
			this.xPos = new int[AudioSS.numSpace];
			this.yPos = new int[AudioSS.numSpace];
			for (int i = 0; i < AudioSS.numSpace; i++) {
				this.xPos[i] = AudioSS.getRelXPos(x[i]);
				this.yPos[i] = AudioSS.getRelYPos(y[i]);
				this.setSS(i);
			}
		}
		else {
			System.out.println("X and Y coordinates given to create AudioSS is not correct" +
					"\nAudioSS instance not created");
		}
	} // end AudioSS method
	
	/** Constructor: creates an instance of AudioSS based off of
	 * source position (x, y) for main window only ::
	 * (0,0) is located top left of conference window
	 */
	public AudioSS(int x, int y) {
		int[] xTemp = new int[AudioSS.numSpace];
		int[] yTemp = new int[AudioSS.numSpace];
		xTemp[0] = x;
		yTemp[0] = y;
		for (int i = 1; i < AudioSS.numSpace; i++) {
			xTemp[i] = -1;
			yTemp[i] = -1;
		}
		new AudioSS(xTemp, yTemp);
	} // end AudioSS method

} // end Class AudioSS