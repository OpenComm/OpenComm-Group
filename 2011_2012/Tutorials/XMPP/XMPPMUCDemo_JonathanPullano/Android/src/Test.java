import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;


public class Test {
	public static void main(String[] args) throws InterruptedException {
		Chat chat = new Chat();
		
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
     * Pauses execution, prints any exceptions that occur
     * @param time
     */
    private static void sleep(int time) {
    	try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
