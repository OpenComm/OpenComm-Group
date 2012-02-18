package ss.cockatoo;

import java.io.*;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ToggleButton;

public class DemoCockatoo extends Activity {
	private byte[] music = this.convertWAV("R.raw.test");
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/** int minSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, 
				AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT, 
				minSize, AudioTrack.MODE_STREAM);
		track.write(music, 0, 80000);
		track.play();
		
		final ToggleButton control = (ToggleButton) findViewById(R.id.control);
		final Button one = (Button) findViewById(R.id.one);
		final Button two = (Button) findViewById(R.id.two);
		/**final Button three = (Button) findViewById(R.id.three);
		final Button four = (Button) findViewById(R.id.four);
		final Button five = (Button) findViewById(R.id.five);
		final Button six = (Button) findViewById(R.id.six);
		final Button seven = (Button) findViewById(R.id.seven);
		final Button eight = (Button) findViewById(R.id.eight); */
	        /** control.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		// play the music if it's on
	        		if (control.isChecked()) {
	        			test.play();
	        		}
	        		// if it's off
	        		else {
	        			test.pause();
	        		}
	        	} // end onClick method
	        });
	        one.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(25, 25);
	        	}
	        });
	        two.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(400, 25);
	        	}
	        });
	        three.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(775, 25);
	        	}
	        });
	        four.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(25, 240);
	        	}
	        });
	        five.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(400, 240);
	        	}
	        });
	        six.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(775, 240);
	        	}
	        });
	        seven.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(25, 480);
	        	}
	        });
	        eight.setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		test.setPosition(775, 480);
	        	}
	        }); */
	}
	
	/** = wav file converted into PCM data by removing the header */
	public byte[] convertWAV(String fileName) {
		// read file into array
		File file = new File(fileName);
		// get length of audio stored in file
		int musicLength = (int)file.length();
		music = new byte[musicLength - 44];
		// Create a DataInputStream to read the audio data back from the saved file.
		try {
			InputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			DataInputStream dis = new DataInputStream(bis);
			// skip first 44 bytes
			dis.skipBytes(44);
			// Read the file into the music array.
			int i = musicLength - 44;
			while (i > 0) {
				music[i] = dis.readByte();
				i--;
			}
			// Close the input streams.
			dis.close();    
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return music;
	} // end convertWav method
} // end DemoCockatoo class
