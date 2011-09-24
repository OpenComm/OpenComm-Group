package opencomm.rtp.oldraj;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AudioConnect {
	private String[] allUserIP;
	private String myIP;
	private InetAddress myIA;

	private void create() {
		for (int i = 0; i < this.allUserIP.length; i++) {
			// if the user is running the code
			if (this.allUserIP[i].equals(myIP)) {
				for (int j = 0; j < this.allUserIP.length; j++) {
					if (i != j) {
						int port = 5010 + i + j * 2 - 1;
						if (i < j) {
							AudioSender as = new AudioSender(port);
							as.start();
							System.out.println("AudioSender created from " + allUserIP[i]
									+ " to " + allUserIP[j] + " at " + port);
							AudioReceiver ar = new AudioReceiver(allUserIP[j],(port + 1));
							ar.start();
							System.out.println("AudioReceiver created from " + allUserIP[i]
							      + " to " + allUserIP[j] + " at " + (port + 1));
						}
						else {
							AudioSender as = new AudioSender(port + 1);
							as.start();
							System.out.println("AudioSender created from " + allUserIP[i]
							         + " to " + allUserIP[j] + " at " + (port + 1));
							AudioReceiver ar = new AudioReceiver(allUserIP[j],port);
							ar.start();
							System.out.println("AudioReceiver created from " + allUserIP[i]
							    + " to " + allUserIP[j] + " at " + port);
						}
					}
				}
			}
		}
	}

	public AudioConnect(String[] allIP) {
		this.allUserIP = allIP;
		try {
			myIA = InetAddress.getLocalHost();
			this.myIP = myIA.getHostAddress();
			System.out.println("I am " + this.myIP);
			this.create();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
