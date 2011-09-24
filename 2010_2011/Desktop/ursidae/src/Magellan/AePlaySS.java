package opencomm.ss.magellan;

import java.io.File; 
import java.io.IOException; 
import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.DataLine; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.SourceDataLine; 
import javax.sound.sampled.UnsupportedAudioFileException; 
 
public class AePlaySS extends Thread { 
 
    private String filename;
 
    private Position curPosition;
 
    private AudioSS aSS;
    
    private FloatControl gainCtrl;
    private FloatControl balCtrl;
    
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb 524288
 
    enum Position { 
        LEFT, RIGHT, NORMAL
    };
 
    public AudioSS getAudioSS() {
    	return this.aSS;
    }
    
    public AePlaySS(String wavfile, int x, int y) { 
        filename = wavfile;
        curPosition = Position.NORMAL;
        aSS = new AudioSS(x, y);
    } 
 
    public AePlaySS(String wavfile, Position p, int x, int y) { 
        filename = wavfile;
        curPosition = p;
        aSS = new AudioSS(x, y);
    }
    
    public void enterPSpace() {
    	gainCtrl.setValue(aSS.getGain());
    }
    
    public void leavePSpace() {
    	gainCtrl.setValue((aSS.getGain() * 2.5f));
    }
 
    public void run() { 
 
        File soundFile = new File(filename);
        if (!soundFile.exists()) { 
            System.err.println("Wave file not found: " + filename);
            return;
        } 
 
        AudioInputStream audioInputStream = null;
        try { 
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) { 
            e1.printStackTrace();
            return;
        } catch (IOException e1) { 
            e1.printStackTrace();
            return;
        } 
 
        AudioFormat format = getAudioFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try { 
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) { 
            e.printStackTrace();
            return;
        } catch (Exception e) { 
            e.printStackTrace();
            return;
        } 
 
        /** if (auline.isControlSupported(FloatControl.Type.PAN)) { 
            FloatControl pan = (FloatControl) auline
                    .getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT) 
                pan.setValue(1.0f);
            else if (curPosition == Position.LEFT) 
                pan.setValue(-1.0f);
        } */
 
        gainCtrl = (FloatControl) auline.getControl(FloatControl.Type.MASTER_GAIN);
		balCtrl = (FloatControl) auline.getControl(FloatControl.Type.BALANCE);
        gainCtrl.setValue(aSS.getGain());
        balCtrl.setValue(aSS.getBalance());
        System.out.println(filename + " -- " + aSS);
        
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
        
        try { 
            while (nBytesRead != -1) { 
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                	byte[] ssData = aSS.spatialize(abData);
                    auline.write(ssData, 0, ssData.length);
                    System.out.println(filename + " gainCtrl: " + gainCtrl);
                    System.out.println(filename + " balCtrl: " + balCtrl);
                }
            } 
        } catch (IOException e) { 
            e.printStackTrace();
            return;
        } finally { 
            auline.drain();
            auline.close();
        } 
 
    } 
    
	/** Audio format of the source sent to the mixer:
	 * 8kHz, signed 16-bit, 2-channel (stereo), little-Endian */
	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;

		int sampleSizeInBits = 16;

		int channels = 2;

		boolean signed = true;

		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}
} 