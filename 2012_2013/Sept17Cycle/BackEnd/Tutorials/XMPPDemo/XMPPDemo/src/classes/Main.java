package classes;

import java.util.Scanner;

import org.jivesoftware.smack.XMPPException;

public class Main {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		boolean hostBoolean = true;
		
		while(hostBoolean){
				
				final String host;
				DataClass dc= new DataClass();
				System.out.println("Enter host to connect to") ;
				Scanner scanner= new Scanner(System.in);
				host=scanner.nextLine();

					System.out.println("Attempting to connect...");

					try {
						//dc.timer(20);
						Connection connectionMain= new Connection(host);
						connectionMain.getXMPPConnection().connect();
						connectionMain.connectMe("designopencomm@jabber.org", "opencommdesign");
						System.out.println("Connected");
						dc.terminate=true;
						hostBoolean=false;
						
					} catch (Exception e) {
						System.out.println("Failure in connection");
					}
					
			}
		
		
		
		
	
	}
	

}
