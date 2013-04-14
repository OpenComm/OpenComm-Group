package com.cornell.opencomm.rtpstreamer;

import java.io.IOException;
import java.net.SocketException;
import org.sipdroid.net.RtpPacket;
import org.sipdroid.net.RtpSocket;
import org.sipdroid.net.SipdroidSocket;

import edu.cornell.opencomm.audio.JingleController;
import edu.cornell.opencomm.audio.SoundSpatializer;

import android.content.ContentResolver;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;
import android.util.Log;

/**
 * ReceiverThread is a generic stream receiver. It receives packets from RTP and
 * writes them into an OutputStream. Taken from/based on r473 of Sipdroid's
 * RTPStreamReceiver class (sipdroid.org)
 */
public class ReceiverThread extends Thread {

	/** Whether working in debug mode. */
	public static boolean DEBUG = true;

	static String codec = "";

	/** Size of the read buffer */
	public static final int BUFFER_SIZE = 4096;

	/**
	 * Maximum blocking time, spent waiting for reading new bytes [milliseconds]
	 */
	public static final int SO_TIMEOUT = 200;

	/** The RtpSocket */
	RtpSocket rtp_socket = null;

	/** Whether it is running */
	boolean running;
	AudioManager am;
	ContentResolver cr;

	SoundSpatializer soundSpatializer;

	/**
	 * Constructs a RtpStreamReceiver.
	 * 
	 * @param output_stream
	 *            the stream sink
	 * @param socket
	 *            the local receiver SipdroidSocket
	 */
	public ReceiverThread(SipdroidSocket socket, JingleController jCtrl) {
		init(socket);
		this.soundSpatializer = jCtrl.getSoundSpatializer();
	}

	/** Inits the RtpStreamReceiver */
	private void init(SipdroidSocket socket) {
		if (socket != null)
			rtp_socket = new RtpSocket(socket);
	}

	/** @return Whether is running */
	public boolean isRunning() {
		return running;
	}

	/** Stops running */
	public void halt() {
		running = false;
	}

	static ToneGenerator ringbackPlayer;

	double smin = 200, s;

	static boolean restored;

	/** for computing packet loss */
	public static float good;
	/** for computing packet loss */
	public static float late;
	/** for computing packet loss */
	public static float lost;
	/** for computing packet loss */
	public static float loss;

	/** for figuring out if connection timed out */
	public static int timeout;

	// clears out the rtp buffer?
	void empty() {
		try {
			rtp_socket.getDatagramSocket().setSoTimeout(1);
			for (;;)
				rtp_socket.receive(rtp_packet);
		} catch (SocketException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
		}
		try {
			rtp_socket.getDatagramSocket().setSoTimeout(1000);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
	}

	RtpPacket rtp_packet;
	AudioTrack track;

	/** Runs it in a new Thread. */
	public void run() {
		Log.i("ReceiverThread", "started");

		if (rtp_socket == null) {
			if (DEBUG)
				println("ERROR: RTP socket is null");
			return;
		}

		byte[] buffer = new byte[BUFFER_SIZE + 12];
		rtp_packet = new RtpPacket(buffer, 0);

		if (DEBUG)
			println("Reading blocks of max " + buffer.length + " bytes");

		running = true;
		restored = false;

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

		AudioTrack trackLeft = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE,
				AudioTrack.MODE_STREAM);
		AudioTrack trackRight = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE,
				AudioTrack.MODE_STREAM);
		short lin[] = new short[BUFFER_SIZE];
		short lin2[] = new short[BUFFER_SIZE];
		int user, server, lserver, /* luser, */cnt, todo, headroom, len = 0, seq = 0, cnt2 = 0, m = 1, expseq, getseq, vm = 1, gap, gseq;
		timeout = 1;
		boolean islate;
		user = 0;
		lserver = 0;
		// luser = -8000;
		cnt = 0;
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,
				(int) (ToneGenerator.MIN_VOLUME));
		// track.play();
		trackLeft.play();
		trackRight.play();
		empty();
		System.gc();

		Log.i("ReceiverThread", "started");
		while (running) {
			try {
				rtp_socket.receive(rtp_packet);

				if (timeout != 0) { // normal receipt of packet
					tg.stopTone();
					// track.pause();
					trackLeft.pause();
					trackRight.pause();

					// user += track.write(lin2,0,lin2.length);
					int itd = soundSpatializer.getItd();
					float[] vol = soundSpatializer.getVol();
					short[][] ss = soundSpatializer.spatializeSource(lin2, itd);
					trackLeft.setStereoVolume(vol[0], 0);
					trackRight.setStereoVolume(0, vol[1]);
					user += trackLeft.write(ss[0], 0, ss[0].length);
					trackRight.write(ss[1], 0, ss[1].length);
					// user+= trackLeft.write(lin2, 0, lin2.length);
					// trackRight.write(lin2, 0, lin2.length);
					// user += track.write(shortToByte(lin2), 0, lin2.length *
					// 2);
					// user += track.write(monoToStereo(lin2),0,lin2.length *
					// 2);
					// track.play();
					trackLeft.play();
					trackRight.play();
					cnt += 2 * BUFFER_SIZE;
					empty();
				}
				timeout = 0;
			} catch (IOException e) { // no packet to receive, start timing out
				if (timeout == 0) {
					// tg.startTone(ToneGenerator.TONE_SUP_RINGTONE);
				}
				rtp_socket.getDatagramSocket().disconnect();
				if (++timeout > 22) {
					break;
				}
			}
			if (running && timeout == 0) {

				gseq = rtp_packet.getSequenceNumber();
				if (seq == gseq) {
					m++;
					continue;
				}

				// figures out where on the audio track to play the sample
				server = trackLeft.getPlaybackHeadPosition();
				headroom = user - server;

				if (headroom > 1500)
					cnt += len;
				else
					cnt = 0;

				if (lserver == server)
					cnt2++;
				else
					cnt2 = 0;

				if (cnt <= 500 || cnt2 >= 2 || headroom - 875 < len) {
					len = rtp_packet.getPayloadLength();
					G711.alaw2linear(buffer, lin, rtp_packet.getPayloadLength());

				}

				if (headroom < 250) {
					todo = 875 - headroom;
					println("insert " + todo);
					islate = true;
					// Log.d("ReceiverThread", "Is talking? " +
					// isTalking(lin2));
					// user += track.write(lin2,0,todo);
					int itd = soundSpatializer.getItd();
					float[] vol = soundSpatializer.getVol();
					short[][] ss = soundSpatializer.spatializeSource(lin2, itd);
					trackLeft.setStereoVolume(vol[0], 0);
					trackRight.setStereoVolume(0, vol[1]);
					user += trackLeft.write(ss[0], 0, todo + Math.abs(itd));
					trackRight.write(ss[1], 0, todo + Math.abs(itd));
					// user += trackLeft.write(lin2,0,todo);
					// trackRight.write(lin2,0,todo);
					// user += track.write(shortToByte(lin2), 0, todo * 2);
					// Log.d("ReceiverThread", "Lin2's length: " + lin2.length);
					// user += track.write(monoToStereo(lin),0,2 * todo);
				} else
					islate = false;

				if (cnt > 500 && cnt2 < 2) {
					todo = headroom - 875;
					println("cut " + todo);
					if (todo < len) {
						// Log.d("ReceiverThread", "Is talking? " +
						// isTalking(lin));
						// user += track.write(lin,todo, len-todo);
						int itd = soundSpatializer.getItd();
						float[] vol = soundSpatializer.getVol();
						short[][] ss2 = soundSpatializer.spatializeSource(lin,
								itd);
						trackLeft.setStereoVolume(vol[0], 0);
						trackRight.setStereoVolume(0, vol[1]);
						user += trackLeft.write(ss2[0], todo,
								len - todo + Math.abs(itd));
						trackRight.write(ss2[1], todo,
								len - todo + Math.abs(itd));
						// user += trackLeft.write(lin,todo, len-todo);
						// trackRight.write(lin,todo, len-todo);
						// user += track.write(monoToStereo(lin),todo * 2,
						// (len-todo) * 2);
						// user += track.write(shortToByte(lin), todo * 2,
						// (len-todo) * 2);
					}
				} else
				// Log.d("ReceiverThread", "Is talking? " + isTalking(lin));
				// user += track.write(lin,0,len); {
				{
					int itd = soundSpatializer.getItd();
					float[] vol = soundSpatializer.getVol();
					short[][] ss2 = soundSpatializer.spatializeSource(lin, itd);
					trackLeft.setStereoVolume(vol[0], 0);
					trackRight.setStereoVolume(0, vol[1]);
					user += trackLeft.write(ss2[0], 0, len + Math.abs(itd));
					trackRight.write(ss2[1], 0, len + Math.abs(itd));
				}
				// user += track.write(shortToByte(lin), 0, len * 2);
				// user += track.write(monoToStereo(lin), 0, len * 2);
				// calculates loss
				if (seq != 0) {
					getseq = gseq & 0xff;
					expseq = ++seq & 0xff;
					gap = (getseq - expseq) & 0xff;
					if (gap > 0) {
						if (gap > 100)
							gap = 1;
						loss += gap;
						lost += gap;
						good += gap - 1;
					} else {
						if (m < vm)
							loss++;
						if (islate)
							late++;
					}
					good++;
					if (good > 100) {
						good *= 0.99;
						lost *= 0.99;
						loss *= 0.99;
						late *= 0.99;
					}
				}
				m = 1;
				seq = gseq;

				lserver = server;
			}
		}
		trackLeft.stop();
		trackRight.stop();
		trackLeft.release();
		trackRight.release();
		// track.stop();
		// track.release();
		tg.stopTone();
		tg.release();

		tg = new ToneGenerator(AudioManager.STREAM_RING,
				ToneGenerator.MIN_VOLUME);
		tg.startTone(ToneGenerator.TONE_PROP_PROMPT);
		try {
			sleep(500);
		} catch (InterruptedException e) {
		}
		tg.stopTone();
		tg.release();

		rtp_socket.close();
		rtp_socket = null;
		codec = "";

		if (DEBUG)
			println("rtp receiver terminated");
	}

	/** Debug output */
	private static void println(String str) {
		// System.out.println("RtpStreamReceiver: " + str);
	}

	/**
	 * @param b
	 *            a byte
	 * @return integer conversion of byte
	 */
	public static int byte2int(byte b) { // return (b>=0)? b : -((b^0xFF)+1);
		// return (b>=0)? b : b+0x100;
		return (b + 0x100) % 0x100;
	}

	/**
	 * @param b1
	 *            more significant byte
	 * @param b2
	 *            less significant byte
	 * @return integer value of the word
	 */
	public static int byte2int(byte b1, byte b2) {
		return (((b1 + 0x100) % 0x100) << 8) + (b2 + 0x100) % 0x100;
	}

	/**
	 * @return the current codec in use
	 */
	public static String getCodec() {
		return codec;
	}

	private static boolean isTalking(short[] audioBuffer) {
		int numberOfReadShorts = audioBuffer.length;
		int audioSize = audioBuffer.length;
		float tempFloatBuffer[] = new float[3];
		int tempIndex = 0;
		// Analyze Sound.
		if (audioSize > 0) {
			float totalAbsValue = 0.0f;
			short sample = 0;
			// Analyze Sound.
			for (int i = 0; i < audioSize - 1; i++) {
				sample = (short) ((audioBuffer[i]) | audioBuffer[i + 1] << 16);
				totalAbsValue += Math.abs(sample) / (numberOfReadShorts);
			}
			// Log.d("ReceiverThread", totalAbsValue)
			// Analyze temp buffer.
			tempFloatBuffer[tempIndex % 3] = totalAbsValue;
			float temp = 0.0f;
			for (int i = 0; i < 3; ++i)
				temp += tempFloatBuffer[i];

			Log.i("ReceiverThread", "what is temp? " + temp);
			if (temp > 800) {
				tempIndex++;
				return true;
			}

			else {
				return false;
			}
		}
		return false;
	} // isTalking method
}
