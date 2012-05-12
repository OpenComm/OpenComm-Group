package edu.cornell.opencomm.audio;

import java.util.Iterator;
import java.util.LinkedHashSet;

import edu.cornell.opencomm.Values;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundSpatializer  {
private static String TAG = "SoundSpatializer";

private AudioTrack audio; // this source's audio track
private int region; // region of the sound source
private int itd; // interaural time delay in shorts
private float[] vol; // volume of sound source
private static float spaceVol = 1;

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
	 // initialize everything
		this.region = 0;
		this.itd = 0;
		this.vol = new float[2];
		this.audio = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
				BUFFER_SIZE*2*2, AudioTrack.MODE_STREAM);
	// Start playback
		// set region based on coordinates
		this.setRegion(0, 0);
		// set itd and vol based on coordinates
		this.setSS();
		SoundSpatializer.allSoundSpatializer.add(this);
		Log.i(SoundSpatializer.TAG, "Sound spatializer constructed for region " + this.region + ", ITD: " + this.itd + ", Volume: " + this.vol[0] + ", " + this.vol[1]);
	}

/** private:: set region of sound source based on position */
	private void setRegion(int x, int y) {
		int xCalc = (int) Math.min(10, Math.max(0, Math.floor(((x * 100/Values.screenW) + 5)/10)));
		int yCalc = (int) Math.min(10, Math.max(0, Math.floor(((y * 100/Values.spaceViewH) + 5)/10)));
		this.region = xCalc * 11 + yCalc;
		Log.d(TAG, "coordinate: (" + x + ", " + y + "), coordinate2: (" + xCalc + ", " + yCalc + "), region: " + ((xCalc * 11) + yCalc));
	} // end setRegion method

	/** private:: set interaural time delay (bytes) based on the region of sound source */
	private void setSS() {
		switch (this.region) {
		case 0:
			this.itd = -3;
			this.vol[0] = 0.4140f;
			this.vol[1] = 0.4514f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 1:
			this.itd = -3;
			this.vol[0] = 0.4432f;
			this.vol[1] = 0.4830f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 2:
			this.itd = -4;
			this.vol[0] = 0.4712f;
			this.vol[1] = 0.5136f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 3:
			this.itd = -4;
			this.vol[0] = 0.4971f;
			this.vol[1] = 0.5424f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 4:
			this.itd = -4;
			this.vol[0] = 0.5201f;
			this.vol[1] = 0.5685f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 5:
			this.itd = -4;
			this.vol[0] = 0.5407f;
			this.vol[1] = 0.5925f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 6:
			this.itd = -5;
			this.vol[0] = 0.5581f;
			this.vol[1] = 0.6133f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 7:
			this.itd = -5;
			this.vol[0] = 0.5716f;
			this.vol[1] = 0.6303f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 8:
			this.itd = -5;
			this.vol[0] = 0.5813f;
			this.vol[1] = 0.6436f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 9:
			this.itd = -5;
			this.vol[0] = 0.5867f;
			this.vol[1] = 0.6524f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 10:
			this.itd = -6;
			this.vol[0] = 0.5875f;
			this.vol[1] = 0.6564f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 11:
			this.itd = -3;
			this.vol[0] = 0.4639f;
			this.vol[1] = 0.4961f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 12:
			this.itd = -3;
			this.vol[0] = 0.4961f;
			this.vol[1] = 0.5307f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 13:
			this.itd = -3;
			this.vol[0] = 0.5272f;
			this.vol[1] = 0.5646f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 14:
			this.itd = -3;
			this.vol[0] = 0.5565f;
			this.vol[1] = 0.5969f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 15:
			this.itd = -4;
			this.vol[0] = 0.5829f;
			this.vol[1] = 0.6267f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 16:
			this.itd = -4;
			this.vol[0] = 0.6069f;
			this.vol[1] = 0.6545f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 17:
			this.itd = -4;
			this.vol[0] = 0.6275f;
			this.vol[1] = 0.6792f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 18:
			this.itd = -5;
			this.vol[0] = 0.6437f;
			this.vol[1] = 0.6998f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 19:
			this.itd = -5;
			this.vol[0] = 0.6556f;
			this.vol[1] = 0.7161f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 20:
			this.itd = -5;
			this.vol[0] = 0.6621f;
			this.vol[1] = 0.7270f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 21:
			this.itd = -6;
			this.vol[0] = 0.6631f;
			this.vol[1] = 0.7320f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 22:
			this.itd = -2;
			this.vol[0] = 0.5071f;
			this.vol[1] = 0.5330f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 23:
			this.itd = -3;
			this.vol[0] = 0.5424f;
			this.vol[1] = 0.5704f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 24:
			this.itd = -3;
			this.vol[0] = 0.5770f;
			this.vol[1] = 0.6077f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 25:
			this.itd = -3;
			this.vol[0] = 0.6101f;
			this.vol[1] = 0.6438f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 26:
			this.itd = -3;
			this.vol[0] = 0.6405f;
			this.vol[1] = 0.6778f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 27:
			this.itd = -4;
			this.vol[0] = 0.6688f;
			this.vol[1] = 0.7104f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 28:
			this.itd = -4;
			this.vol[0] = 0.6937f;
			this.vol[1] = 0.7401f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 29:
			this.itd = -4;
			this.vol[0] = 0.7139f;
			this.vol[1] = 0.7656f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 30:
			this.itd = -5;
			this.vol[0] = 0.7290f;
			this.vol[1] = 0.7865f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 31:
			this.itd = -5;
			this.vol[0] = 0.7375f;
			this.vol[1] = 0.8009f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 32:
			this.itd = -6;
			this.vol[0] = 0.7387f;
			this.vol[1] = 0.8076f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 33:
			this.itd = -2;
			this.vol[0] = 0.5417f;
			this.vol[1] = 0.5599f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 34:
			this.itd = -2;
			this.vol[0] = 0.5799f;
			this.vol[1] = 0.5999f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 35:
			this.itd = -2;
			this.vol[0] = 0.6180f;
			this.vol[1] = 0.6402f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 36:
			this.itd = -2;
			this.vol[0] = 0.6551f;
			this.vol[1] = 0.6799f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 37:
			this.itd = -3;
			this.vol[0] = 0.6900f;
			this.vol[1] = 0.7181f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 38:
			this.itd = -3;
			this.vol[0] = 0.7236f;
			this.vol[1] = 0.7558f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 39:
			this.itd = -3;
			this.vol[0] = 0.7543f;
			this.vol[1] = 0.7916f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 40:
			this.itd = -4;
			this.vol[0] = 0.7802f;
			this.vol[1] = 0.8240f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 41:
			this.itd = -4;
			this.vol[0] = 0.8006f;
			this.vol[1] = 0.8523f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 42:
			this.itd = -5;
			this.vol[0] = 0.8127f;
			this.vol[1] = 0.8732f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 43:
			this.itd = -6;
			this.vol[0] = 0.8143f;
			this.vol[1] = 0.8832f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 44:
			this.itd = -1;
			this.vol[0] = 0.5656f;
			this.vol[1] = 0.5751f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 45:
			this.itd = -1;
			this.vol[0] = 0.6061f;
			this.vol[1] = 0.6166f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 46:
			this.itd = -1;
			this.vol[0] = 0.6471f;
			this.vol[1] = 0.6588f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 47:
			this.itd = -1;
			this.vol[0] = 0.6877f;
			this.vol[1] = 0.7010f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 48:
			this.itd = -2;
			this.vol[0] = 0.7269f;
			this.vol[1] = 0.7423f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 49:
			this.itd = -2;
			this.vol[0] = 0.7659f;
			this.vol[1] = 0.7841f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 50:
			this.itd = -2;
			this.vol[0] = 0.8035f;
			this.vol[1] = 0.8256f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 51:
			this.itd = -3;
			this.vol[0] = 0.8377f;
			this.vol[1] = 0.8657f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 52:
			this.itd = -3;
			this.vol[0] = 0.8676f;
			this.vol[1] = 0.9048f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 53:
			this.itd = -4;
			this.vol[0] = 0.8874f;
			this.vol[1] = 0.9391f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 54:
			this.itd = -6;
			this.vol[0] = 0.8900f;
			this.vol[1] = 0.9588f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 55:
			this.itd = 0;
			this.vol[0] = 0.5770f;
			this.vol[1] = 0.5770f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 56:
			this.itd = 0;
			this.vol[0] = 0.6188f;
			this.vol[1] = 0.6188f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 57:
			this.itd = 0;
			this.vol[0] = 0.6613f;
			this.vol[1] = 0.6613f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 58:
			this.itd = 0;
			this.vol[0] = 0.7038f;
			this.vol[1] = 0.7038f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 59:
			this.itd = 0;
			this.vol[0] = 0.7456f;
			this.vol[1] = 0.7456f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 60:
			this.itd = 0;
			this.vol[0] = 0.7881f;
			this.vol[1] = 0.7881f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 61:
			this.itd = 0;
			this.vol[0] = 0.8307f;
			this.vol[1] = 0.8307f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 62:
			this.itd = 0;
			this.vol[0] = 0.8724f;
			this.vol[1] = 0.8724f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 63:
			this.itd = 0;
			this.vol[0] = 0.9149f;
			this.vol[1] = 0.9149f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 64:
			this.itd = 0;
			this.vol[0] = 0.9575f;
			this.vol[1] = 0.9575f;
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
			this.vol[0] = 0.5751f;
			this.vol[1] = 0.5656f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 67:
			this.itd = 0;
			this.vol[0] = 0.6166f;
			this.vol[1] = 0.6061f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 68:
			this.itd = 0;
			this.vol[0] = 0.6588f;
			this.vol[1] = 0.6471f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 69:
			this.itd = 0;
			this.vol[0] = 0.7010f;
			this.vol[1] = 0.6877f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 70:
			this.itd = 1;
			this.vol[0] = 0.7423f;
			this.vol[1] = 0.7269f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 71:
			this.itd = 1;
			this.vol[0] = 0.7841f;
			this.vol[1] = 0.7659f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 72:
			this.itd = 1;
			this.vol[0] = 0.8256f;
			this.vol[1] = 0.8035f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 73:
			this.itd = 2;
			this.vol[0] = 0.8657f;
			this.vol[1] = 0.8377f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 74:
			this.itd = 2;
			this.vol[0] = 0.9048f;
			this.vol[1] = 0.8676f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 75:
			this.itd = 3;
			this.vol[0] = 0.9391f;
			this.vol[1] = 0.8874f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 76:
			this.itd = 5;
			this.vol[0] = 0.9588f;
			this.vol[1] = 0.8900f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 77:
			this.itd = 1;
			this.vol[0] = 0.5599f;
			this.vol[1] = 0.5417f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 78:
			this.itd = 1;
			this.vol[0] = 0.5999f;
			this.vol[1] = 0.5799f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 79:
			this.itd = 1;
			this.vol[0] = 0.6402f;
			this.vol[1] = 0.6180f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 80:
			this.itd = 1;
			this.vol[0] = 0.6799f;
			this.vol[1] = 0.6551f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 81:
			this.itd = 2;
			this.vol[0] = 0.7181f;
			this.vol[1] = 0.6900f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 82:
			this.itd = 2;
			this.vol[0] = 0.7558f;
			this.vol[1] = 0.7236f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 83:
			this.itd = 2;
			this.vol[0] = 0.7916f;
			this.vol[1] = 0.7543f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 84:
			this.itd = 3;
			this.vol[0] = 0.8240f;
			this.vol[1] = 0.7802f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 85:
			this.itd = 3;
			this.vol[0] = 0.8523f;
			this.vol[1] = 0.8006f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 86:
			this.itd = 4;
			this.vol[0] = 0.8732f;
			this.vol[1] = 0.8127f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 87:
			this.itd = 5;
			this.vol[0] = 0.8832f;
			this.vol[1] = 0.8143f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 88:
			this.itd = 1;
			this.vol[0] = 0.5330f;
			this.vol[1] = 0.5071f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 89:
			this.itd = 2;
			this.vol[0] = 0.5704f;
			this.vol[1] = 0.5424f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 90:
			this.itd = 2;
			this.vol[0] = 0.6077f;
			this.vol[1] = 0.5770f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 91:
			this.itd = 2;
			this.vol[0] = 0.6438f;
			this.vol[1] = 0.6101f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 92:
			this.itd = 2;
			this.vol[0] = 0.6778f;
			this.vol[1] = 0.6405f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 93:
			this.itd = 3;
			this.vol[0] = 0.7104f;
			this.vol[1] = 0.6688f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 94:
			this.itd = 3;
			this.vol[0] = 0.7401f;
			this.vol[1] = 0.6937f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 95:
			this.itd = 3;
			this.vol[0] = 0.7656f;
			this.vol[1] = 0.7139f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 96:
			this.itd = 4;
			this.vol[0] = 0.7865f;
			this.vol[1] = 0.7290f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 97:
			this.itd = 4;
			this.vol[0] = 0.8009f;
			this.vol[1] = 0.7375f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 98:
			this.itd = 5;
			this.vol[0] = 0.8076f;
			this.vol[1] = 0.7387f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 99:
			this.itd = 2;
			this.vol[0] = 0.4961f;
			this.vol[1] = 0.4639f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 100:
			this.itd = 2;
			this.vol[0] = 0.5307f;
			this.vol[1] = 0.4961f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 101:
			this.itd = 2;
			this.vol[0] = 0.5646f;
			this.vol[1] = 0.5272f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 102:
			this.itd = 2;
			this.vol[0] = 0.5969f;
			this.vol[1] = 0.5565f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 103:
			this.itd = 3;
			this.vol[0] = 0.6267f;
			this.vol[1] = 0.5829f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 104:
			this.itd = 3;
			this.vol[0] = 0.6545f;
			this.vol[1] = 0.6069f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 105:
			this.itd = 3;
			this.vol[0] = 0.6792f;
			this.vol[1] = 0.6275f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 106:
			this.itd = 4;
			this.vol[0] = 0.6998f;
			this.vol[1] = 0.6437f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 107:
			this.itd = 4;
			this.vol[0] = 0.7161f;
			this.vol[1] = 0.6556f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 108:
			this.itd = 4;
			this.vol[0] = 0.7270f;
			this.vol[1] = 0.6621f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 109:
			this.itd = 5;
			this.vol[0] = 0.7320f;
			this.vol[1] = 0.6631f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 110:
			this.itd = 2;
			this.vol[0] = 0.4514f;
			this.vol[1] = 0.4140f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 111:
			this.itd = 2;
			this.vol[0] = 0.4830f;
			this.vol[1] = 0.4432f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 112:
			this.itd = 3;
			this.vol[0] = 0.5136f;
			this.vol[1] = 0.4712f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 113:
			this.itd = 3;
			this.vol[0] = 0.5424f;
			this.vol[1] = 0.4971f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 114:
			this.itd = 3;
			this.vol[0] = 0.5685f;
			this.vol[1] = 0.5201f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 115:
			this.itd = 3;
			this.vol[0] = 0.5925f;
			this.vol[1] = 0.5407f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 116:
			this.itd = 4;
			this.vol[0] = 0.6133f;
			this.vol[1] = 0.5581f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 117:
			this.itd = 4;
			this.vol[0] = 0.6303f;
			this.vol[1] = 0.5716f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 118:
			this.itd = 4;
			this.vol[0] = 0.6436f;
			this.vol[1] = 0.5813f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 119:
			this.itd = 4;
			this.vol[0] = 0.6524f;
			this.vol[1] = 0.5867f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 120:
			this.itd = 5;
			this.vol[0] = 0.6564f;
			this.vol[1] = 0.5875f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 200:
			this.itd = -5;
			this.vol[0] = 0.0002f;
			this.vol[1] = 0.0625f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 201:
			this.itd = -5;
			this.vol[0] = 0.1845f;
			this.vol[1] = 0.2450f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 300:
			this.itd = 4;
			this.vol[0] = 0.2450f;
			this.vol[1] = 0.1845f;
			this.audio.setStereoVolume(this.vol[0] * SoundSpatializer.spaceVol, this.vol[1] * SoundSpatializer.spaceVol);
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region " + this.region);
			break;
		case 301:
			this.itd = 4;
			this.vol[0] = 0.0625f;
			this.vol[1] = 0.0002f;
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
	
	/** update region, ITD, and vol when Right PSpace is focused */
	public void putLeftPSpaceOutOfFocusForRight() {
		// update region
		this.region = 200;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0]/2, vol[1]/2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putLeftPSpaceOutOfFocus method
	
	/** update region, ITD, and vol when Left PSpace is focused */
	public void putRightPSpaceOutOfFocusForLeft() {
		// update region
		this.region = 300;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0]/2, vol[1]/2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method
	
	/** update region, ITD, and vol of main space when Left Space is focused */
	public void putMainSpaceOutOfFocusForLeft() {
		// update region
		this.region = 301;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0]/2, vol[1]/2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method
	
	/** update region, ITD, and vol of main space when Right Space is focused */
	public void putMainSpaceOutOfFocusForRight() {
		// update region
		this.region = 201;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0]/2, vol[1]/2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method
	
	/** update vol of space in focus*/
	public void putSpaceInFocus() {
		// update stereo volume to 100%
		this.setStereoVolume(vol[0], vol[1]);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method
	
	/** change volume of the audiotrack */
	public void setStereoVolume(float left, float right) {
		this.audio.setStereoVolume(left, right);
	} // end setStereoVolume
	
	public String getVolume() {
		return "this audio volume: " + this.vol[0] + ", " + this.vol[1] + "; " + SoundSpatializer.spaceVol;
	}

	/** return a SS-modified source */
	public short[] spatializeSource(short[] source) {
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
		return SoundSpatializer.monoToStereo(left, right);
	} // end write method	
	
	
/** write a SS-modified source to the track */
	public int writeSS(short[] source) {
		short[] left = new short[0];
		short[] right = new short[0];
		// if the audio has not been updated, update
		this.setSS();
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
}