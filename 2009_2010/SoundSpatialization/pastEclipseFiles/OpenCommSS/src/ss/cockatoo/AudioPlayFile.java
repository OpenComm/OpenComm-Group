package ss.cockatoo;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import android.media.*;
/** This class demonstrates sound spatialization using a file */
public class AudioPlayFile implements AudioPlayIF {

	private int x0; // user's x coordinate
	private int y0; // user's y coordinate
	private int xS; // sound source's x coordinate
	private int yS; // sound source's y coordinate
	private double dist; // distance between user and sound source
	private double angle; // direction of sound source; 0 denotes directly in front of user
	private double delay; // interaural time delay in milliseconds
	private AudioTrack playback; // audiotrack with the sound
	private byte[] source; // byte[] source
	private byte[] playss; // source modified based on spatialization
	private static int BUFFER_SIZE = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);; // buffer size in bytes; minimum
	private static int BYTE_MS = 256; // bytes per millisecond :: 8000kHz * 16bit * 2 channels = 256000 bytes/sec
	private static double HEAD_RADIUS = 0.11; // radius of head in m
	private static double SOUND_SPEED = 343.42; // speed of sound in m/s
	
	public void pause() {
		this.playback.pause();
	} // end pause method

	public void play() {
		this.playback.play();
		playback.write(source, x0, x0);
		
	} // end play method
	
	public void stop() {
		this.playback.stop();
	} // end stop method

	/** feed PCM data (short form) to AudioTrack */
	public void write(short[] data) {
		this.playback.write(data, 0, data.length);
	} // end write method

	/** feed PCM data (byte form) offset at start to AudioTrack;
	 * modify the data so that it is stereo compatible and itd is implemented */
	public void write(int start) {
		this.playss = new byte[AudioPlayFile.BUFFER_SIZE];
		// if there is delay, put in silence before/after respective channels
		// write into playback
		this.playback.write(this.playss, 0, this.playss.length);
	} // end write method
	
	/** = set (xP, yP) as new sound source position and update volume */
	public void setPosition(int xP, int yP) {
		// update position of sound source
		this.xS = xP;
		this.yS = yP;
		// update distance, angle, and interaural time delay
		this.dist = Math.sqrt((Math.pow(this.xS - this.x0, 2) + Math.pow(this.yS - this.y0, 2)));
		if (this.dist != 0) {
			this.angle = Math.asin((this.x0 - this.xS)/this.dist);
		}
		else {
			this.angle = 0;
		}
		this.delay = 1000 * AudioPlayFile.HEAD_RADIUS/AudioPlayFile.SOUND_SPEED * (this.angle + Math.sin(this.angle));
		// calculate distance difference between two ears
		double dd = this.delay * AudioPlayFile.SOUND_SPEED;
		// calculate distance from left ear
		double leftDist = this.dist + (dd/2);
		double rightDist = this.dist - (dd/2);
		// set volume based on distance from each ear
		// volume = - 0.001 * distance + 1
		this.playback.setStereoVolume((float) (-0.001 * leftDist + 1), (float) (-0.001 * rightDist + 1));
	} // end setPosition method
	
	/** = wav file converted into PCM data by removing the header */
	public byte[] convertWAV(InputStream is) {
		// get length of audio stored in file
		int musicLength = 0;
		this.source = new byte[1];
		// Create a DataInputStream to read the audio data back from the saved file.
		try {
			musicLength = is.available();
			this.source = new byte[musicLength];
			BufferedInputStream bis = new BufferedInputStream(is);
			DataInputStream dis = new DataInputStream(bis);
			// skip first 44 bytes
			dis.skipBytes(44);
			// Read the file into the music array.
			int i = 0;
			while (i < musicLength) {
				this.source[i] = dis.readByte();
				i++;
			}
			// Close the input streams.
			dis.close();    
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return this.source;
	} // end convertWAV method
	
	/** Constructor: create AudioTrack object (stereo) with the user coordinate (x, y);
	 * default sound position is the same as the user */
	public AudioPlayFile(InputStream is, int x, int y) {
		this.x0 = x;
		this.y0 = y;
		this.xS = x;
		this.yS = y;
		this.dist = 0;
		this.angle = 0;
		this.delay = 0;
		this.playback = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, 
				AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT,
				BUFFER_SIZE, AudioTrack.MODE_STREAM);
		this.playback.setStereoVolume(1.0f, 1.0f);
		this.source = this.convertWAV(is);
	} // end AudioPlayFile method
} // end class AudioPlayFile