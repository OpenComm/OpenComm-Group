package com.example.audiodemofall2012;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import android.util.Log;

public class SoundController {

	private static final String TAG = "SoundController";
	private static final int BUFFER_SIZE = 1024;
	private static final String AUDIO_FILE = "/mnt/sdcard/amazonmp3/sample_audio.wav";

	private SoundSpatializer spatializer;
	private BufferedInputStream audioSource;
	private ArrayBlockingQueue<short[]> audio;

	public SoundController() {
		spatializer = new SoundSpatializer();
		audioSource = createAudioSource(AUDIO_FILE);
		audio = new ArrayBlockingQueue<short[]>(32);
		final Semaphore availableAudio = new Semaphore(0);
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						audio.put(streamAudio(audioSource));
					} catch (InterruptedException e) {
						Log.v(TAG, e.getMessage());
					}
					availableAudio.release();
				}
			}
		}).start();

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						availableAudio.acquire();
					} catch (InterruptedException e) {
						Log.e(TAG, e.getMessage());
					}
					writeAudio(spatializer.spatializeSource(audio.poll(),
							spatializer.getItd()));
				}
			}

		}).start();
	}

	public void writeAudio(short[] audio) {
		spatializer.getAudio().write(audio, 0, audio.length);
	}

	public void manipulateSource(int x, int y) {
		float[] vol;

		spatializer.moveTo(x, y);
		vol = spatializer.getVol();
		spatializer.setStereoVolume(vol[0], vol[1]);
	}

	public BufferedInputStream createAudioSource(String path) {
		File file = new File(path);
		FileInputStream is = null;
		BufferedInputStream bis = null;

		try {
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			System.exit(1);
		}
		return bis;
	}

	public short[] streamAudio(BufferedInputStream bis) {
		byte[] source = new byte[BUFFER_SIZE * 2];
		short[] audio = new short[BUFFER_SIZE];
		ByteBuffer convert = null;
		try {
			// our files are in WAV, so we need to skip the 44-byte header
			//bis.skip(44);
			if (bis.available() < BUFFER_SIZE) {
				Arrays.fill(source, (byte) 0);
			} else {
				bis.read(source);
			}
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
		}
		convert = ByteBuffer.wrap(source);
		convert.asShortBuffer().get(audio);
		return audio;
	}

	public void closeAudioSource(BufferedInputStream bis) {
		try {
			bis.close();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
		}
	}

	public SoundSpatializer getSpatializer() {
		return spatializer;
	}

}
