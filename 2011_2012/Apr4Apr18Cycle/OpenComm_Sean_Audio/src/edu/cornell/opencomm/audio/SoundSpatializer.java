package edu.cornell.opencomm.audio;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.cornell.opencomm.Values;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundSpatializer  {
private boolean D = true;
private static String TAG = "SoundSpatializer";

private AudioTrack audio; // this source's audio track
private int region; // region of the sound source
private int itd; // interaural time delay in shorts
private float[] vol; // volume of sound source
private static float spaceVol = 1;


private File fileName;
private volatile boolean playing;
private int fileSize = 5566522; // file size of test files

public static LinkedHashSet<SoundSpatializer> allSoundSpatializer = new LinkedHashSet<SoundSpatializer>();

/** Size of the read buffer */
public static final int BUFFER_SIZE = 1024;

	public AudioTrack getAudio() {
	return audio;
}


public void setAudio(AudioTrack audio) {
	this.audio = audio;
}


	public SoundSpatializer() {
		super();
	    setPlaying(false);
	 // initialize everything
		this.region = 0;
		this.itd = 0;
		this.vol = new float[2];
		this.audio = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,
				BUFFER_SIZE*2*2, AudioTrack.MODE_STREAM);
	// Start playback
		// set region based on coordinates
		this.setRegion(0, 0);
		// set itd and vol based on coordinates
		this.setSS();
		SoundSpatializer.allSoundSpatializer.add(this);
		Log.i(SoundSpatializer.TAG, "Sound spatializer constructed for region " + this.region + ", ITD: " + this.itd + ", Volume: " + this.vol[0] + ", " + this.vol[1]);
	}


/**public void run(){
      // Get the length of the audio stored in the file (16 bit so 2 bytes per short)
      // and create a short array to store the recorded audio.
      int musicLength = (int)(5567000);
      byte[] music = new byte[musicLength];

      try {
        // Create a DataInputStream to read the audio data back from the saved file.
        InputStream is = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(is, 8000);
        DataInputStream dis = new DataInputStream(bis);

        // Read the file into the music array.
        int i = 0;
        while (dis.available() > 0) {
        	/** skip the first 44
        	if (i < 44) {
        		dis.readByte();
        	}
        	else {
        		music[i] = dis.readByte();
        	}*/
        	/**music[i] = dis.readByte();
          i++;
        }

        // Close the input streams.
        dis.close();     

        // Create a new AudioTrack object using the same parameters as the AudioRecord
        // object used to create the file.
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
                                                8000, 
                                               AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                                               AudioFormat.ENCODING_PCM_16BIT, 
                                               musicLength, 
                                               AudioTrack.MODE_STREAM);
        // Start playback
        this.audio.play();

        // Write the music buffer to the AudioTrack object
        while(playing){
			for (int j = 0; j < fileSize; j+=1000) {
				byte[] temp = new byte[1000];
				for (int k = 0; k < 1000; k++) {
					temp[k] = music[j];
				}
				this.writeSS(temp);
			}
        }
      }
      catch(Exception e){
          e.printStackTrace();
      }

} */

/** private:: set region of sound source based on position */
	private void setRegion(int x, int y) {
		int xCalc = (int) Math.min(10, Math.max(0, Math.floor(((x * 100/Values.screenW) + 5)/10)));
		int yCalc = (int) Math.min(10, Math.max(0, Math.floor(((y * 100/Values.spaceViewH) + 5)/10)));
		this.region = xCalc * 11 + yCalc;
		Log.d(TAG, "coordinate: (" + x + ", " + y + "), coordinate2: (" + xCalc + ", " + yCalc + "), region: " + ((xCalc * 11) + yCalc));
		/**x = x * 100/800; // find 
		y = y * 100/480; 
		// region 1: (x, y) = (0 - 20% inclusive, 0 - 25% inclusive)
		if ((x >= 0) && (x <= 20) && (y >= 0) && (y <= 25)) {
			this.region = 1;
		}
		// region 2: (x, y) = (20%exclusive - 40%, 0 - 25% inclusive)
		if ((x > 20) && (x <= 40) && (y >= 0) && (y <= 25)) {
			this.region = 2;
		}
		// region 3: (x, y) = (40%exclusive - 60%, 0 - 25% inclusive)
		if ((x > 40) && (x <= 60) && (y >= 0) && (y <= 25)) {
			this.region = 3;
		}
		// region 4: (x, y) = (60%exclusive - 80%, 0 - 25% inclusive)
		if ((x > 60) && (x <= 80) && (y >= 0) && (y <= 25)) {
			this.region = 4;
		}
		// region 5: (x, y) = (80%exclusive - 100%, 0 - 25% inclusive)
		if ((x > 80) && (x <= 100) && (y >= 0) && (y <= 25)) {
			this.region = 5;
		}
		// region 6: (x, y) = (0 - 20% inclusive, 25%exclusive - 50% inclusive)
		if ((x >= 0) && (x <= 20) && (y > 25) && (y <= 50)) {
			this.region = 6;
		}
		// region 7: (x, y) = (20%exclusive - 40%, 25%exclusive - 50% inclusive)
		if ((x > 20) && (x <= 40) && (y > 25) && (y <= 50)) {
			this.region = 7;
		}
		// region 8: (x, y) = (40%exclusive - 60%, 25%exclusive - 50% inclusive)
		if ((x > 40) && (x <= 60) && (y > 25) && (y <= 50)) {
			this.region = 8;
		}
		// region 9: (x, y) = (60%exclusive - 80%, 25%exclusive - 50% inclusive)
		if ((x > 60) && (x <= 80) && (y > 25) && (y <= 50)) {
			this.region = 9;
		}
		// region 10: (x, y) = (80%exclusive - 100%, 25%exclusive - 50% inclusive)
		if ((x > 80) && (x <= 100) && (y > 25) && (y <= 50)) {
			this.region = 10;
		}
		// region 11: (x, y) = (0 - 20% inclusive, 50%exclusive - 75% inclusive)
		if ((x >= 0) && (x <= 20) && (y > 50) && (y <= 75)) {
			this.region = 11;
		}
		// region 12: (x, y) = (20%exclusive - 40%, 25%exclusive - 50% inclusive)
		if ((x > 20) && (x <= 40) && (y > 50) && (y <= 75)) {
			this.region = 12;
		}
		// region 13: (x, y) = (40%exclusive - 60%, 25%exclusive - 50% inclusive)
		if ((x > 40) && (x <= 60) && (y > 50) && (y <= 75)) {
			this.region = 13;
		}
		// region 14: (x, y) = (60%exclusive - 80%, 25%exclusive - 50% inclusive)
		if ((x > 60) && (x <= 80) && (y > 50) && (y <= 75)) {
			this.region = 14;
		}
		// region 15: (x, y) = (80%exclusive - 100%, 25%exclusive - 50% inclusive)
		if ((x > 80) && (x <= 100) && (y > 50) && (y <= 75)) {
			this.region = 15;
		}
		// region 16: (x, y) = (0 - 20% inclusive, 75%exclusive - 100% inclusive)
		if ((x >= 0) && (x <= 20) && (y > 75) && (y <= 100)) {
			this.region = 16;
		}
		// region 17: (x, y) = (20%exclusive - 40%, 25%exclusive - 50% inclusive)
		if ((x > 20) && (x <= 40) && (y > 75) && (y <= 100)) {
			this.region = 17;
		}
		// region 18: (x, y) = (40%exclusive - 60%, 25%exclusive - 50% inclusive)
		if ((x > 40) && (x <= 60) && (y > 75) && (y <= 100)) {
			this.region = 18;
		}
		// region 19: (x, y) = (60%exclusive - 80%, 25%exclusive - 50% inclusive)
		if ((x > 60) && (x <= 80) && (y > 75) && (y <= 100)) {
			this.region = 19;
		}
		// region 20: (x, y) = (80%exclusive - 100%, 25%exclusive - 50% inclusive)
		if ((x > 80) && (x <= 100) && (y > 75) && (y <= 100)) {
			this.region = 20;
		} */
	} // end setRegion method

	/** private:: set interaural time delay (bytes) based on the region of sound source */
	private void setSS() {
		switch (this.region) {
		case 0:
			this.itd = -2;
			this.vol[0] = 0.0029f;
			this.vol[1] = 0.0495f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 1:
			this.itd = -2;
			this.vol[0] = 0.0801f;
			this.vol[1] = 0.1310f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 2:
			this.itd = -2;
			this.vol[0] = 0.1567f;
			this.vol[1] = 0.2127f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 3:
			this.itd = -3;
			this.vol[0] = 0.2303f;
			this.vol[1] = 0.2926f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 4:
			this.itd = -3;
			this.vol[0] = 0.2990f;
			this.vol[1] = 0.3685f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 5:
			this.itd = -3;
			this.vol[0] = 0.3639f;
			this.vol[1] = 0.4424f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 6:
			this.itd = -4;
			this.vol[0] = 0.4219f;
			this.vol[1] = 0.5111f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 7:
			this.itd = -4;
			this.vol[0] = 0.4698f;
			this.vol[1] = 0.5714f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 8:
			this.itd = -5;
			this.vol[0] = 0.5063f;
			this.vol[1] = 0.6222f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 9:
			this.itd = -5;
			this.vol[0] = 0.5273f;
			this.vol[1] = 0.6582f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 10:
			this.itd = -6;
			this.vol[0] = 0.5303f;
			this.vol[1] = 0.6750f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 11:
			this.itd = -2;
			this.vol[0] = 0.0367f;
			this.vol[1] = 0.0749f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 12:
			this.itd = -2;
			this.vol[0] = 0.1169f;
			this.vol[1] = 0.1589f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 13:
			this.itd = -2;
			this.vol[0] = 0.1970f;
			this.vol[1] = 0.2436f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 14:
			this.itd = -2;
			this.vol[0] = 0.2749f;
			this.vol[1] = 0.3271f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 15:
			this.itd = -3;
			this.vol[0] = 0.3484f;
			this.vol[1] = 0.4074f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 16:
			this.itd = -3;
			this.vol[0] = 0.4190f;
			this.vol[1] = 0.4866f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 17:
			this.itd = -3;
			this.vol[0] = 0.4835f;
			this.vol[1] = 0.5620f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 18:
			this.itd = -4;
			this.vol[0] = 0.5381f;
			this.vol[1] = 0.6301f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 19:
			this.itd = -4;
			this.vol[0] = 0.5810f;
			this.vol[1] = 0.6896f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 20:
			this.itd = -5;
			this.vol[0] = 0.6062f;
			this.vol[1] = 0.7334f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 21:
			this.itd = -6;
			this.vol[0] = 0.6098f;
			this.vol[1] = 0.7545f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 22:
			this.itd = -2;
			this.vol[0] = 0.0649f;
			this.vol[1] = 0.0942f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 23:
			this.itd = -2;
			this.vol[0] = 0.1478f;
			this.vol[1] = 0.1801f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 24:
			this.itd = -2;
			this.vol[0] = 0.2312f;
			this.vol[1] = 0.2673f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 25:
			this.itd = -2;
			this.vol[0] = 0.3130f;
			this.vol[1] = 0.3538f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 26:
			this.itd = -2;
			this.vol[0] = 0.3912f;
			this.vol[1] = 0.4378f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 27:
			this.itd = -2;
			this.vol[0] = 0.4677f;
			this.vol[1] = 0.5220f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 28:
			this.itd = -3;
			this.vol[0] = 0.5393f;
			this.vol[1] = 0.6038f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 29:
			this.itd = -3;
			this.vol[0] = 0.6020f;
			this.vol[1] = 0.6803f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 30:
			this.itd = -4;
			this.vol[0] = 0.6534f;
			this.vol[1] = 0.7506f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 31:
			this.itd = -5;
			this.vol[0] = 0.6849f;
			this.vol[1] = 0.8058f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 32:
			this.itd = -6;
			this.vol[0] = 0.6892f;
			this.vol[1] = 0.8339f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 33:
			this.itd = -1;
			this.vol[0] = 0.0869f;
			this.vol[1] = 0.1068f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 34:
			this.itd = -1;
			this.vol[0] = 0.1721f;
			this.vol[1] = 0.1941f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 35:
			this.itd = -1;
			this.vol[0] = 0.2582f;
			this.vol[1] = 0.2829f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 36:
			this.itd = -1;
			this.vol[0] = 0.3435f;
			this.vol[1] = 0.3715f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 37:
			this.itd = -2;
			this.vol[0] = 0.4260f;
			this.vol[1] = 0.4583f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 38:
			this.itd = -2;
			this.vol[0] = 0.5080f;
			this.vol[1] = 0.5462f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 39:
			this.itd = -2;
			this.vol[0] = 0.5869f;
			this.vol[1] = 0.6335f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 40:
			this.itd = -3;
			this.vol[0] = 0.6588f;
			this.vol[1] = 0.7176f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 41:
			this.itd = -3;
			this.vol[0] = 0.7216f;
			this.vol[1] = 0.7999f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 42:
			this.itd = -4;
			this.vol[0] = 0.7633f;
			this.vol[1] = 0.8720f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 43:
			this.itd = -6;
			this.vol[0] = 0.7687f;
			this.vol[1] = 0.9134f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 44:
			this.itd = -1;
			this.vol[0] = 0.1024f;
			this.vol[1] = 0.1124f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 45:
			this.itd = -1;
			this.vol[0] = 0.1892f;
			this.vol[1] = 0.2003f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 46:
			this.itd = -1;
			this.vol[0] = 0.2774f;
			this.vol[1] = 0.2899f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 47:
			this.itd = -1;
			this.vol[0] = 0.3653f;
			this.vol[1] = 0.3796f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 48:
			this.itd = -1;
			this.vol[0] = 0.4511f;
			this.vol[1] = 0.4676f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 49:
			this.itd = -1;
			this.vol[0] = 0.5377f;
			this.vol[1] = 0.5575f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 50:
			this.itd = -1;
			this.vol[0] = 0.6230f;
			this.vol[1] = 0.6476f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 51:
			this.itd = -2;
			this.vol[0] = 0.7042f;
			this.vol[1] = 0.7364f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 52:
			this.itd = -2;
			this.vol[0] = 0.7811f;
			this.vol[1] = 0.8275f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 53:
			this.itd = -3;
			this.vol[0] = 0.8412f;
			this.vol[1] = 0.9195f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 54:
			this.itd = -6;
			this.vol[0] = 0.8482f;
			this.vol[1] = 0.9929f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 55:
			this.itd = 0;
			this.vol[0] = 0.1109f;
			this.vol[1] = 0.1109f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 56:
			this.itd = 0;
			this.vol[0] = 0.1987f;
			this.vol[1] = 0.1987f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 57:
			this.itd = 0;
			this.vol[0] = 0.2881f;
			this.vol[1] = 0.2881f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 58:
			this.itd = 0;
			this.vol[0] = 0.3775f;
			this.vol[1] = 0.3775f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 59:
			this.itd = 0;
			this.vol[0] = 0.4652f;
			this.vol[1] = 0.4652f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 60:
			this.itd = 0;
			this.vol[0] = 0.5546f;
			this.vol[1] = 0.5546f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 61:
			this.itd = 0;
			this.vol[0] = 0.6440f;
			this.vol[1] = 0.6440f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 62:
			this.itd = 0;
			this.vol[0] = 0.7318f;
			this.vol[1] = 0.7318f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 63:
			this.itd = 0;
			this.vol[0] = 0.8212f;
			this.vol[1] = 0.8212f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 64:
			this.itd = 0;
			this.vol[0] = 0.9106f;
			this.vol[1] = 0.9106f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 65:
			this.itd = 0;
			this.vol[0] = 1.0000f;
			this.vol[1] = 1.0000f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 66:
			this.itd = 0;
			this.vol[0] = 0.1124f;
			this.vol[1] = 0.1024f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 67:
			this.itd = 0;
			this.vol[0] = 0.2003f;
			this.vol[1] = 0.1892f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 68:
			this.itd = 0;
			this.vol[0] = 0.2899f;
			this.vol[1] = 0.2774f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 69:
			this.itd = 0;
			this.vol[0] = 0.3796f;
			this.vol[1] = 0.3653f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 70:
			this.itd = 0;
			this.vol[0] = 0.4676f;
			this.vol[1] = 0.4511f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 71:
			this.itd = 0;
			this.vol[0] = 0.5575f;
			this.vol[1] = 0.5377f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 72:
			this.itd = 0;
			this.vol[0] = 0.6476f;
			this.vol[1] = 0.6230f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 73:
			this.itd = 1;
			this.vol[0] = 0.7364f;
			this.vol[1] = 0.7042f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 74:
			this.itd = 1;
			this.vol[0] = 0.8275f;
			this.vol[1] = 0.7811f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 75:
			this.itd = 2;
			this.vol[0] = 0.9195f;
			this.vol[1] = 0.8412f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 76:
			this.itd = 5;
			this.vol[0] = 0.9929f;
			this.vol[1] = 0.8482f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 77:
			this.itd = 0;
			this.vol[0] = 0.1068f;
			this.vol[1] = 0.0869f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 78:
			this.itd = 0;
			this.vol[0] = 0.1941f;
			this.vol[1] = 0.1721f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 79:
			this.itd = 0;
			this.vol[0] = 0.2829f;
			this.vol[1] = 0.2582f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 80:
			this.itd = 0;
			this.vol[0] = 0.3715f;
			this.vol[1] = 0.3435f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 81:
			this.itd = 1;
			this.vol[0] = 0.4583f;
			this.vol[1] = 0.4260f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 82:
			this.itd = 1;
			this.vol[0] = 0.5462f;
			this.vol[1] = 0.5080f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 83:
			this.itd = 1;
			this.vol[0] = 0.6335f;
			this.vol[1] = 0.5869f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 84:
			this.itd = 2;
			this.vol[0] = 0.7176f;
			this.vol[1] = 0.6588f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 85:
			this.itd = 2;
			this.vol[0] = 0.7999f;
			this.vol[1] = 0.7216f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 86:
			this.itd = 3;
			this.vol[0] = 0.8720f;
			this.vol[1] = 0.7633f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 87:
			this.itd = 5;
			this.vol[0] = 0.9134f;
			this.vol[1] = 0.7687f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 88:
			this.itd = 1;
			this.vol[0] = 0.0942f;
			this.vol[1] = 0.0649f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 89:
			this.itd = 1;
			this.vol[0] = 0.1801f;
			this.vol[1] = 0.1478f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 90:
			this.itd = 1;
			this.vol[0] = 0.2673f;
			this.vol[1] = 0.2312f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 91:
			this.itd = 1;
			this.vol[0] = 0.3538f;
			this.vol[1] = 0.3130f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 92:
			this.itd = 1;
			this.vol[0] = 0.4378f;
			this.vol[1] = 0.3912f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 93:
			this.itd = 1;
			this.vol[0] = 0.5220f;
			this.vol[1] = 0.4677f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 94:
			this.itd = 2;
			this.vol[0] = 0.6038f;
			this.vol[1] = 0.5393f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 95:
			this.itd = 2;
			this.vol[0] = 0.6803f;
			this.vol[1] = 0.6020f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 96:
			this.itd = 3;
			this.vol[0] = 0.7506f;
			this.vol[1] = 0.6534f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 97:
			this.itd = 4;
			this.vol[0] = 0.8058f;
			this.vol[1] = 0.6849f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 98:
			this.itd = 5;
			this.vol[0] = 0.8339f;
			this.vol[1] = 0.6892f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 99:
			this.itd = 1;
			this.vol[0] = 0.0749f;
			this.vol[1] = 0.0367f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 100:
			this.itd = 1;
			this.vol[0] = 0.1589f;
			this.vol[1] = 0.1169f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 101:
			this.itd = 1;
			this.vol[0] = 0.2436f;
			this.vol[1] = 0.1970f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 102:
			this.itd = 1;
			this.vol[0] = 0.3271f;
			this.vol[1] = 0.2749f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 103:
			this.itd = 2;
			this.vol[0] = 0.4074f;
			this.vol[1] = 0.3484f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 104:
			this.itd = 2;
			this.vol[0] = 0.4866f;
			this.vol[1] = 0.4190f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 105:
			this.itd = 2;
			this.vol[0] = 0.5620f;
			this.vol[1] = 0.4835f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 106:
			this.itd = 3;
			this.vol[0] = 0.6301f;
			this.vol[1] = 0.5381f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 107:
			this.itd = 3;
			this.vol[0] = 0.6896f;
			this.vol[1] = 0.5810f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 108:
			this.itd = 4;
			this.vol[0] = 0.7334f;
			this.vol[1] = 0.6062f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 109:
			this.itd = 5;
			this.vol[0] = 0.7545f;
			this.vol[1] = 0.6098f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 110:
			this.itd = 1;
			this.vol[0] = 0.0495f;
			this.vol[1] = 0.0029f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 111:
			this.itd = 1;
			this.vol[0] = 0.1310f;
			this.vol[1] = 0.0801f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 112:
			this.itd = 1;
			this.vol[0] = 0.2127f;
			this.vol[1] = 0.1567f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 113:
			this.itd = 2;
			this.vol[0] = 0.2926f;
			this.vol[1] = 0.2303f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 114:
			this.itd = 2;
			this.vol[0] = 0.3685f;
			this.vol[1] = 0.2990f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 115:
			this.itd = 2;
			this.vol[0] = 0.4424f;
			this.vol[1] = 0.3639f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 116:
			this.itd = 3;
			this.vol[0] = 0.5111f;
			this.vol[1] = 0.4219f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 117:
			this.itd = 3;
			this.vol[0] = 0.5714f;
			this.vol[1] = 0.4698f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 118:
			this.itd = 4;
			this.vol[0] = 0.6222f;
			this.vol[1] = 0.5063f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 119:
			this.itd = 4;
			this.vol[0] = 0.6582f;
			this.vol[1] = 0.5273f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 120:
			this.itd = 5;
			this.vol[0] = 0.6750f;
			this.vol[1] = 0.5303f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;


		}
	} // end setITD method

	/** = private:: a byte array with the itd added before the source */
	private short[] addITDBefore(short[] source) {
		int itdVal = Math.abs(this.itd)/2;
		int len = source.length + itdVal;
		// create new channel
		short[] added = new short[len];
		// add silence before source
		for (int i = 0; i < itdVal; i++) {
			added[i] = 0; // value of silence in signed 16bit: 0
			Log.i(SoundSpatializer.TAG, "Silence added before source");
		}
		// add source
		for (int i = 0; i < source.length; i++) {
			added[i+itdVal] = source[i];
			Log.i(SoundSpatializer.TAG, "Source modified with ITD before source");
		}
		return added;
	} // end addITDBefore method
	
	/** = private:: a short array with the itd added after the source */
	private short[] addITDAfter(short[] source) {
		int itdVal = Math.abs(this.itd)/2;
		int len = source.length + itdVal;
		// create new channel
		short[] added = new short[len];
		// add silence after source
		for (int i = 0; i < itdVal; i++) {
			added[i + source.length] = 0; // value of silence in signed 16bit: 0
			Log.i(SoundSpatializer.TAG, "Silence added after source");
		}
		// add source
		for (int i = 0; i < source.length; i++) {
			added[i] = source[i];
			Log.i(SoundSpatializer.TAG, "Source modified with ITD after source");
		}
		return added;
	} // end addITDBefore method
	
	/** = private:: stereo stream created from two mono streams left and right */
	private static short[] monoToStereo(short[] left, short[] right) {
		// check that both left and right mono streams are of equal length
		if (left.length != right.length) {
			Log.e(SoundSpatializer.TAG, "Left and right mono streams are not equal in length!");
			return null;
		}
		short[] output = new short[left.length * 2];
		// put mono channels into respective left and right channel
		for (int i = 0; i < left.length; i++) {
			output[(i * 2)] = left[i]; // put mono channel into left stereo channel
			output[(i * 2) + 1] = right[i]; // put mono channel into right stereo channel
		}
		Log.i(SoundSpatializer.TAG, "2 mono streams inserted into 1 stereo stream");
		return output;
	} // end monoToStereo method
	
	/** update region, ITD, and vol based on new position */
	public void moveTo(int nx, int ny) {
		// update region
		this.setRegion(nx, ny);
		// update ITD and volume
		this.setSS();
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end moveTo method
	
	/** change volume of the audiotrack */
	public void setStereoVolume(float left, float right) {
		this.audio.setStereoVolume(left * SoundSpatializer.spaceVol, right * SoundSpatializer.spaceVol);
	} // end setStereoVolume
	
	public String getVolume() {
		return "this audio volume: " + this.vol[0] + ", " + this.vol[1] + "; " + SoundSpatializer.spaceVol;
	}

	
/** write a SS-modified source to the track */
	public int writeSS(short[] source) {
		short[] left = new short[0];
		short[] right = new short[0];
		// if the audio has not been updated, update
		this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
		// if the sound source is left of the user
		if (this.itd > 0) {
			left = this.addITDAfter(source);
			right = this.addITDBefore(source);
		}
		// if the sound source is right of the user
		else if (this.itd < 0) {
			left = this.addITDAfter(source);
			right = this.addITDBefore(source);
		}
		// if the sound source is right in front of the user; no itd
		else {
			left = source.clone();
			right = source.clone();
		}
		Log.i(SoundSpatializer.TAG, "Left and right mono streams have been modified.");
		// create stereo stream
		short[] stereo = SoundSpatializer.monoToStereo(left, right);
		// write the stereo channel into the audiotrack
		return this.audio.write(stereo, 0, stereo.length);
	} // end write method
	public void setFileName(File fileName) {
	    this.fileName = fileName;
	}
	
	public File getFileName() {
	    return fileName;
	}
	
	public void setPlaying(boolean playing) {
	    this.playing = playing;
	}
	
	public boolean isPlaying() {
	    return playing;
	}
	
	public static void setSpaceVolume(int vol) {
		Log.d("SoundSpatializer", "space volume: " + (float) vol);
		SoundSpatializer.spaceVol = vol/100.0f;
		Iterator<SoundSpatializer> ssItr = allSoundSpatializer.iterator();
		while (ssItr.hasNext()) {
			SoundSpatializer ss = ssItr.next();
			ss.getAudio().setStereoVolume(ss.vol[0] * SoundSpatializer.spaceVol, ss.vol[1] * SoundSpatializer.spaceVol);
		}
		Log.d("SoundSpatializer", "space volume: " + spaceVol);
	}
}