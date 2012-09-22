package classes;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPException;

public class DataClass {
	Timer timer;
	TimerTask tt;
	boolean terminate;

	public DataClass(){
		
	}
	
	public void timer(int seconds){
		timer= new Timer();
		timer.schedule(getTimerTask(), seconds*100);
		
	}
	
	public TimerTask getTimerTask(){
		return tt= new TimerTask(){
			public void run(){
				
				if(terminate==true){
					timer.cancel();
				}
				else{
				System.out.println("Failed to connect in a reasonable amount of time");
				timer.cancel();
				terminate=false;
				
				}
				
			}
		};
		
	}
	
	
	
	
}
