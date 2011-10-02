package xmpp.demo;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.os.Bundle;

public class XMPPMUCDemo_Andriod_JonathanPullanoActivity extends Activity {
	Chat chat = new Chat();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		try {
			chat.connect("mucopencomm", "opencommmuc");
			chat.createChat("superhappyfunroom", "bigsmiley");
			chat.sendMessage("Hai frends! i luv u :)");
			
			//If you join with this account on pidgin, it will get devoiced
			chat.deleteParticipant("opencommss");
			
			//Wait 5 seconds
			sleep(5000); 
			
			//And then voiced again
			chat.addParticipant("opencommss");
			
			sleep(5000);
			
			chat.leave();
			chat.disconnect();

			while(true);
		} catch (XMPPException e) {
			//Something happened that makes me sad :(
			System.exit(1);
		}
    }
    
    /**
     * S
     * @param time
     */
    private void sleep(int time) {
    	try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}