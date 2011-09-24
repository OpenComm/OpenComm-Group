package rtp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import ui.MainWindow;
import ui.PersonVisualRep;


/** An instance of this class allows a local host
 * to send audio */
public class AudioSender extends Thread {
	ServerSocket MyService;
	Socket clientSocket = null;
	BufferedInputStream input;
	TargetDataLine targetDataLine;
	int port;
	boolean[] sendPrmt; // permission to send to specific space

	BufferedOutputStream out;
	  ByteArrayOutputStream byteArrayOutputStream;
	  AudioFormat audioFormat;	
	
	  SourceDataLine sourceDataLine;	  
	 byte tempBuffer[] = new byte[10000];
	
	 public AudioSender(int port, boolean[] send) {   
		this.port = port;
		this.sendPrmt = send;
	}
	 
	 private AudioFormat getAudioFormat(){
		    float sampleRate = 8000.0F;		  
		    int sampleSizeInBits = 16;		   
		    int channels = 1;		    
		    boolean signed = true;		    
		    boolean bigEndian = false;		 
		    return new AudioFormat(
		                      sampleRate,
		                      sampleSizeInBits,
		                      channels,
		                      signed,
		                      bigEndian);
		  }

	
	private void captureAudio() {
		try {

			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
			System.out.println("Available mixers:");
			for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
				System.out.println(mixerInfo[cnt].getName());
			}
			audioFormat = getAudioFormat();

			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);

			Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);

			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);

			targetDataLine.open(audioFormat);
			targetDataLine.start();

			Thread captureThread = new CaptureThread();
			captureThread.start();		
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	class CaptureThread extends Thread {

		byte tempBuffer[] = new byte[10000];

		public void run() {			
			try {
				while (true) {
					if (sendPrmt[MainWindow.curr.num]) {
						int cnt = targetDataLine.read(tempBuffer, 0,
								tempBuffer.length);
					}
					// if not, send an empty buffer
					out.write(tempBuffer);	
				}
				
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}
		}
	}

	@Override
	public void run() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo =  new DataLine.Info( SourceDataLine.class,audioFormat);
    		sourceDataLine = (SourceDataLine)
    	    AudioSystem.getLine(dataLineInfo);
    	    sourceDataLine.open(audioFormat);
    	    sourceDataLine.start();
			MyService = new ServerSocket(port);
			clientSocket = MyService.accept();
			captureAudio();
			input = new BufferedInputStream(clientSocket.getInputStream());	
			out=new BufferedOutputStream(clientSocket.getOutputStream());
			
			while(input.read(tempBuffer)!=-1){			
				sourceDataLine.write(tempBuffer,0,10000);
			}
		} catch (IOException e) {
			System.out.println("IO exception");
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			System.out.println("Line Unavailable Exception");
			e.printStackTrace();
		}
       

	}
	/** allows this sender to send to this receiver when in space #sn */
	public void canSend(int sn) {
		if (sn < this.sendPrmt.length) {
			this.sendPrmt[sn] = true;
		}
	} // end canSend method
	
	/** prohibit this sender from sending anything to this receiver when in space #sn */
	public void noSend(int sn) {
		if (sn < this.sendPrmt.length) {
			this.sendPrmt[sn] = false;
		}
	} // end canSend method
	public boolean isCreated() {
		return (MyService != null);
	}

}
