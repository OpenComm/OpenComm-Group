package com.example.audiodemofall2012;

import java.util.LinkedHashSet;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class SoundSpatializer {
	private static String TAG = "SoundSpatializer";

	private AudioTrack audio; // this source's audio track
	private int region; // region of the sound source
	private int itd; // interaural time delay in shorts
	private float[] vol; // volume of sound source
	private static float spaceVol = 1;

	public static LinkedHashSet<SoundSpatializer> allSoundSpatializer = new LinkedHashSet<SoundSpatializer>();

	/** Size of the read buffer */
	public static final int BUFFER_SIZE = 1024;
	public static final int screenH = 537;
	public static final int screenW = 480;

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
		this.audio = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE * 2 * 2,
				AudioTrack.MODE_STREAM);
		// Start playback
		this.audio.play();
		// set region based on coordinates
		this.setRegion(0, 0);
		// set itd and vol based on coordinates
		this.setSS();
		SoundSpatializer.allSoundSpatializer.add(this);
		Log.i(SoundSpatializer.TAG, "Sound spatializer constructed for region "
				+ this.region + ", ITD: " + this.itd + ", Volume: "
				+ this.vol[0] + ", " + this.vol[1]);
	}

	/** private:: set region of sound source based on position */
	private void setRegion(int x, int y) {
		int xCalc = (int) Math.min(10, Math.max(0,
				Math.floor(((x * 100 / SoundSpatializer.screenW) + 5) / 10)));
		int yCalc = (int) Math.min(10, Math.max(0,
				Math.floor(((y * 100 / SoundSpatializer.screenH) + 5) / 10)));
		this.region = xCalc * 11 + yCalc;
		Log.d(TAG, "coordinate: (" + x + ", " + y + "), coordinate2: (" + xCalc
				+ ", " + yCalc + "), region: " + ((xCalc * 11) + yCalc));
	} // end setRegion method

	/**
	 * private:: set interaural time delay (bytes) based on the region of sound
	 * source
	 */
	private void setSS() {
		switch (this.region) {
		case 0:
			this.itd = -3;
			this.vol[0] = 0.0703f;
			this.vol[1] = 0.0751f;
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 1:
			this.itd = -3;
			this.vol[0] = 0.0740f;
			this.vol[1] = 0.0797f;
			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 2:
			this.itd = -4;
			this.vol[0] = 0.0779f;
			this.vol[1] = 0.0847f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 3:
			this.itd = -4;
			this.vol[0] = 0.0819f;
			this.vol[1] = 0.0900f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 4:
			this.itd = -4;
			this.vol[0] = 0.0858f;
			this.vol[1] = 0.0955f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 5:
			this.itd = -4;
			this.vol[0] = 0.0897f;
			this.vol[1] = 0.1011f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 6:
			this.itd = -5;
			this.vol[0] = 0.0932f;
			this.vol[1] = 0.0532f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 7:
			this.itd = -5;
			this.vol[0] = 0.0962f;
			this.vol[1] = 0.0266f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 8:
			this.itd = -5;
			this.vol[0] = 0.0984f;
			this.vol[1] = 0.0133f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 9:
			this.itd = -5;
			this.vol[0] = 0.0997f;
			this.vol[1] = 0.0066f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 10:
			this.itd = -6;
			this.vol[0] = 0.0999f;
			this.vol[1] = 0.0f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 11:
			this.itd = -3;
			this.vol[0] = 0.0768f;
			this.vol[1] = 0.0817f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 12:
			this.itd = -3;
			this.vol[0] = 0.0817f;
			this.vol[1] = 0.0878f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 13:
			this.itd = -3;
			this.vol[0] = 0.0871f;
			this.vol[1] = 0.0946f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 14:
			this.itd = -3;
			this.vol[0] = 0.0929f;
			this.vol[1] = 0.1022f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 15:
			this.itd = -4;
			this.vol[0] = 0.0987f;
			this.vol[1] = 0.1103f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 16:
			this.itd = -4;
			this.vol[0] = 0.1048f;
			this.vol[1] = 0.1192f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 17:
			this.itd = -4;
			this.vol[0] = 0.1106f;
			this.vol[1] = 0.1284f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 18:
			this.itd = -5;
			this.vol[0] = 0.1156f;
			this.vol[1] = 0.0642f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 19:
			this.itd = -5;
			this.vol[0] = 0.1196f;
			this.vol[1] = 0.0321f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 20:
			this.itd = -5;
			this.vol[0] = 0.1219f;
			this.vol[1] = 0.0160f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 21:
			this.itd = -6;
			this.vol[0] = 0.1223f;
			this.vol[1] = 0.0f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 22:
			this.itd = -2;
			this.vol[0] = 0.0836f;
			this.vol[1] = 0.0882f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 23:
			this.itd = -3;
			this.vol[0] = 0.0900f;
			this.vol[1] = 0.0959f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 24:
			this.itd = -3;
			this.vol[0] = 0.0974f;
			this.vol[1] = 0.1050f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 25:
			this.itd = -3;
			this.vol[0] = 0.1056f;
			this.vol[1] = 0.1157f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 26:
			this.itd = -3;
			this.vol[0] = 0.1146f;
			this.vol[1] = 0.1278f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 27:
			this.itd = -4;
			this.vol[0] = 0.1244f;
			this.vol[1] = 0.1422f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 28:
			this.itd = -4;
			this.vol[0] = 0.1345f;
			this.vol[1] = 0.1585f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 29:
			this.itd = -4;
			this.vol[0] = 0.1440f;
			this.vol[1] = 0.1757f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 30:
			this.itd = -5;
			this.vol[0] = 0.1520f;
			this.vol[1] = 0.0876f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 31:
			this.itd = -5;
			this.vol[0] = 0.1569f;
			this.vol[1] = 0.0438f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 32:
			this.itd = -6;
			this.vol[0] = 0.1577f;
			this.vol[1] = 0.0f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 33:
			this.itd = -2;
			this.vol[0] = 0.0899f;
			this.vol[1] = 0.0936f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 34:
			this.itd = -2;
			this.vol[0] = 0.0980f;
			this.vol[1] = 0.1029f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 35:
			this.itd = -2;
			this.vol[0] = 0.1078f;
			this.vol[1] = 0.1145f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 36:
			this.itd = -2;
			this.vol[0] = 0.1194f;
			this.vol[1] = 0.1287f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 37:
			this.itd = -3;
			this.vol[0] = 0.1329f;
			this.vol[1] = 0.1461f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 38:
			this.itd = -3;
			this.vol[0] = 0.1490f;
			this.vol[1] = 0.1687f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 39:
			this.itd = -3;
			this.vol[0] = 0.1676f;
			this.vol[1] = 0.1977f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 40:
			this.itd = -4;
			this.vol[0] = 0.1874f;
			this.vol[1] = 0.2341f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 41:
			this.itd = -4;
			this.vol[0] = 0.2066f;
			this.vol[1] = 0.2789f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 42:
			this.itd = -5;
			this.vol[0] = 0.2199f;
			this.vol[1] = 0.1395f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 43:
			this.itd = -6;
			this.vol[0] = 0.2219f;
			this.vol[1] = 0.0f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 44:
			this.itd = -1;
			this.vol[0] = 0.0948f;
			this.vol[1] = 0.0969f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 45:
			this.itd = -1;
			this.vol[0] = 0.1046f;
			this.vol[1] = 0.1074f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 46:
			this.itd = -1;
			this.vol[0] = 0.1167f;
			this.vol[1] = 0.1207f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 47:
			this.itd = -1;
			this.vol[0] = 0.1319f;
			this.vol[1] = 0.1378f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 48:
			this.itd = -2;
			this.vol[0] = 0.1508f;
			this.vol[1] = 0.1598f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 49:
			this.itd = -2;
			this.vol[0] = 0.1760f;
			this.vol[1] = 0.1908f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 50:
			this.itd = -2;
			this.vol[0] = 0.2096f;
			this.vol[1] = 0.2362f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 51:
			this.itd = -3;
			this.vol[0] = 0.2538f;
			this.vol[1] = 0.3066f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 52:
			this.itd = -3;
			this.vol[0] = 0.3110f;
			this.vol[1] = 0.4327f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 53:
			this.itd = -4;
			this.vol[0] = 0.3658f;
			this.vol[1] = 0.6762f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 54:
			this.itd = -6;
			this.vol[0] = 0.7258f;
			this.vol[1] = 0.000f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 55:
			this.itd = 0;
			this.vol[0] = 0.0974f;
			this.vol[1] = 0.0974f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 56:
			this.itd = 0;
			this.vol[0] = 0.1081f;
			this.vol[1] = 0.1081f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 57:
			this.itd = 0;
			this.vol[0] = 0.1216f;
			this.vol[1] = 0.1216f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 58:
			this.itd = 0;
			this.vol[0] = 0.1391f;
			this.vol[1] = 0.1391f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 59:
			this.itd = 0;
			this.vol[0] = 0.1619f;
			this.vol[1] = 0.1619f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 60:
			this.itd = 0;
			this.vol[0] = 0.1944f;
			this.vol[1] = 0.1944f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 61:
			this.itd = 0;
			this.vol[0] = 0.2432f;
			this.vol[1] = 0.2432f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 62:
			this.itd = 0;
			this.vol[0] = 0.3228f;
			this.vol[1] = 0.3228f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 63:
			this.itd = 0;
			this.vol[0] = 0.4842f;
			this.vol[1] = 0.4842f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 64:
			this.itd = 0;
			this.vol[0] = 0.9685f;
			this.vol[1] = 0.9685f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 65:
			this.itd = 0;
			this.vol[0] = 1.0000f;
			this.vol[1] = 1.0000f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 66:
			this.itd = 0;
			this.vol[0] = 0.0969f;
			this.vol[1] = 0.0948f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 67:
			this.itd = 0;
			this.vol[0] = 0.1074f;
			this.vol[1] = 0.1046f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 68:
			this.itd = 0;
			this.vol[0] = 0.1207f;
			this.vol[1] = 0.1167f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 69:
			this.itd = 0;
			this.vol[0] = 0.1378f;
			this.vol[1] = 0.1319f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 70:
			this.itd = 1;
			this.vol[0] = 0.1598f;
			this.vol[1] = 0.1508f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 71:
			this.itd = 1;
			this.vol[0] = 0.1908f;
			this.vol[1] = 0.1760f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 72:
			this.itd = 1;
			this.vol[0] = 0.2362f;
			this.vol[1] = 0.2096f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 73:
			this.itd = 2;
			this.vol[0] = 0.3066f;
			this.vol[1] = 0.2538f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 74:
			this.itd = 2;
			this.vol[0] = 0.4327f;
			this.vol[1] = 0.3110f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 75:
			this.itd = 3;
			this.vol[0] = 0.6762f;
			this.vol[1] = 0.3658f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 76:
			this.itd = 5;
			this.vol[0] = 0.0000f;
			this.vol[1] = 0.7258f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 77:
			this.itd = 1;
			this.vol[0] = 0.0936f;
			this.vol[1] = 0.0899f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 78:
			this.itd = 1;
			this.vol[0] = 0.1029f;
			this.vol[1] = 0.0980f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 79:
			this.itd = 1;
			this.vol[0] = 0.1145f;
			this.vol[1] = 0.1078f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 80:
			this.itd = 1;
			this.vol[0] = 0.1287f;
			this.vol[1] = 0.1194f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 81:
			this.itd = 2;
			this.vol[0] = 0.1461f;
			this.vol[1] = 0.1329f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 82:
			this.itd = 2;
			this.vol[0] = 0.1687f;
			this.vol[1] = 0.1490f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 83:
			this.itd = 2;
			this.vol[0] = 0.1977f;
			this.vol[1] = 0.1676f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 84:
			this.itd = 3;
			this.vol[0] = 0.2341f;
			this.vol[1] = 0.1874f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 85:
			this.itd = 3;
			this.vol[0] = 0.2789f;
			this.vol[1] = 0.2066f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 86:
			this.itd = 4;
			this.vol[0] = 0.1395f;
			this.vol[1] = 0.2199f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 87:
			this.itd = 5;
			this.vol[0] = 0.0f;
			this.vol[1] = 0.2219f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 88:
			this.itd = 1;
			this.vol[0] = 0.0882f;
			this.vol[1] = 0.0836f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 89:
			this.itd = 2;
			this.vol[0] = 0.0959f;
			this.vol[1] = 0.0900f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 90:
			this.itd = 2;
			this.vol[0] = 0.1050f;
			this.vol[1] = 0.0974f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 91:
			this.itd = 2;
			this.vol[0] = 0.1157f;
			this.vol[1] = 0.1056f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 92:
			this.itd = 2;
			this.vol[0] = 0.1278f;
			this.vol[1] = 0.1146f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 93:
			this.itd = 3;
			this.vol[0] = 0.1422f;
			this.vol[1] = 0.1244f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 94:
			this.itd = 3;
			this.vol[0] = 0.1585f;
			this.vol[1] = 0.1345f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 95:
			this.itd = 3;
			this.vol[0] = 0.1757f;
			this.vol[1] = 0.1440f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 96:
			this.itd = 4;
			this.vol[0] = 0.0876f;
			this.vol[1] = 0.1520f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 97:
			this.itd = 4;
			this.vol[0] = 0.0438f;
			this.vol[1] = 0.1569f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 98:
			this.itd = 5;
			this.vol[0] = 0.0f;
			this.vol[1] = 0.1577f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 99:
			this.itd = 2;
			this.vol[0] = 0.0817f;
			this.vol[1] = 0.0768f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 100:
			this.itd = 2;
			this.vol[0] = 0.0878f;
			this.vol[1] = 0.0817f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 101:
			this.itd = 2;
			this.vol[0] = 0.0946f;
			this.vol[1] = 0.0871f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 102:
			this.itd = 2;
			this.vol[0] = 0.1022f;
			this.vol[1] = 0.0929f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 103:
			this.itd = 3;
			this.vol[0] = 0.1103f;
			this.vol[1] = 0.0987f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 104:
			this.itd = 3;
			this.vol[0] = 0.1192f;
			this.vol[1] = 0.1048f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 105:
			this.itd = 3;
			this.vol[0] = 0.1284f;
			this.vol[1] = 0.1106f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 106:
			this.itd = 4;
			this.vol[0] = 0.0642f;
			this.vol[1] = 0.1156f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 107:
			this.itd = 4;
			this.vol[0] = 0.0321f;
			this.vol[1] = 0.1196f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 108:
			this.itd = 4;
			this.vol[0] = 0.0160f;
			this.vol[1] = 0.1219f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 109:
			this.itd = 5;
			this.vol[0] = 0.0f;
			this.vol[1] = 0.1223f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 110:
			this.itd = 2;
			this.vol[0] = 0.0751f;
			this.vol[1] = 0.0703f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 111:
			this.itd = 2;
			this.vol[0] = 0.0797f;
			this.vol[1] = 0.0740f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 112:
			this.itd = 3;
			this.vol[0] = 0.0847f;
			this.vol[1] = 0.0779f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 113:
			this.itd = 3;
			this.vol[0] = 0.0900f;
			this.vol[1] = 0.0819f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 114:
			this.itd = 3;
			this.vol[0] = 0.0955f;
			this.vol[1] = 0.0858f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 115:
			this.itd = 3;
			this.vol[0] = 0.1011f;
			this.vol[1] = 0.0897f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 116:
			this.itd = 4;
			this.vol[0] = 0.0506f;
			this.vol[1] = 0.0932f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 117:
			this.itd = 4;
			this.vol[0] = 0.0253f;
			this.vol[1] = 0.0962f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 118:
			this.itd = 4;
			this.vol[0] = 0.0126f;
			this.vol[1] = 0.0984f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 119:
			this.itd = 4;
			this.vol[0] = 0.0063f;
			this.vol[1] = 0.0997f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 120:
			this.itd = 5;
			this.vol[0] = 0.0f;
			this.vol[1] = 0.0999f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 200:
			this.itd = -5;
			this.vol[0] = 0.0412f;
			this.vol[1] = 0.0439f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 201:
			this.itd = -5;
			this.vol[0] = 0.0505f;
			this.vol[1] = 0.0546f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 300:
			this.itd = 4;
			this.vol[0] = 0.0546f;
			this.vol[1] = 0.0505f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;
		case 301:
			this.itd = 4;
			this.vol[0] = 0.0439f;
			this.vol[1] = 0.0412f;

			Log.i(SoundSpatializer.TAG, "ITD and Vol set for region "
					+ this.region);
			break;

		}
	} // end setITD method

	/** = private:: a byte array with the itd added before the source */
	private short[] addITDBefore(short[] source, int itd) {
		int itdVal = Math.abs(itd) / 2;
		int len = source.length + itdVal;
		// create new channel
		short[] added = new short[len];
		// add silence before source
		for (int i = 0; i < itdVal; i++) {
			added[i] = 0; // value of silence in signed 16bit: 0
		}
		// add source
		for (int i = 0; i < source.length; i++) {
			added[i + itdVal] = source[i];
		}
		return added;
	} // end addITDBefore method

	/** = private:: a short array with the itd added after the source */
	private short[] addITDAfter(short[] source, int itd) {
		int itdVal = Math.abs(itd) / 2;
		int len = source.length + itdVal;
		// create new channel
		short[] added = new short[len];
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
	private static short[] monoToStereo(short[] left, short[] right) {
		// check that both left and right mono streams are of equal length
		if (left.length != right.length) {
			Log.e(SoundSpatializer.TAG,
					"Left and right mono streams are not equal in length!");
			return null;
		}
		short[] output = new short[left.length * 2];
		// put mono channels into respective left and right channel
		for (int i = 0; i < left.length; i++) {
			output[(i * 2)] = left[i]; // put mono channel into left stereo
										// channel
			output[(i * 2) + 1] = right[i]; // put mono channel into right
											// stereo channel
		}
		Log.i(SoundSpatializer.TAG,
				"2 mono streams inserted into 1 stereo stream");
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
		this.setStereoVolume(vol[0] / 2, vol[1] / 2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putLeftPSpaceOutOfFocus method

	/** update region, ITD, and vol when Left PSpace is focused */
	public void putRightPSpaceOutOfFocusForLeft() {
		// update region
		this.region = 300;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0] / 2, vol[1] / 2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method

	/** update region, ITD, and vol of main space when Left Space is focused */
	public void putMainSpaceOutOfFocusForLeft() {
		// update region
		this.region = 301;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0] / 2, vol[1] / 2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method

	/** update region, ITD, and vol of main space when Right Space is focused */
	public void putMainSpaceOutOfFocusForRight() {
		// update region
		this.region = 201;
		// update ITD and volume
		this.setSS();
		// lower volume by 1/2
		this.setStereoVolume(vol[0] / 2, vol[1] / 2);
		Log.i(SoundSpatializer.TAG, "SS values updated");
	} // end putRightPSpaceOutOfFocus method

	/** update vol of space in focus */
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
		return "this audio volume: " + this.vol[0] + ", " + this.vol[1] + "; "
				+ SoundSpatializer.spaceVol;
	}

	/** return a SS-modified source */
	public short[] spatializeSource(short[] source, int itd) {
		short[] left, right;
		// if the sound source is left of the user
		if (itd > 0) {
			left = this.addITDAfter(source, itd);
			right = this.addITDBefore(source, itd);
		}
		// if the sound source is right of the user
		else if (itd < 0) {
			left = this.addITDAfter(source, itd);
			right = this.addITDBefore(source, itd);
		}
		// if the sound source is right in front of the user; no itd
		else {
			left = source.clone();
			right = source.clone();
		}
		short[] stereo = monoToStereo(left, right);
		return stereo;
	} // end write method

	public int getItd() {
		return itd;
	}

	public float[] getVol() {
		return vol;
	}

	public void setVol(float[] vol) {
		this.vol = vol;
	}
}