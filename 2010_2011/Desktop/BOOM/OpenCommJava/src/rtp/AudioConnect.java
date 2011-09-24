package rtp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ui.Person;

public class AudioConnect {
	private HashMap<String, AudioReceiver> allReceivers; // all AudioReceivers stored by the sender's username
	private HashMap<String, AudioSender> allSenders; // all AudioSenders stored by the receiver's username
	private String[] allUserNames;
	private String[] allUserIP;
	private String myIP;
	private String myUN;
	private InetAddress myIA;
	
	/** = all audio receivers with me as the receiver */
	public HashMap<String, AudioReceiver> getAllReceivers() {
		return this.allReceivers;
	} // end getAllReceivers method
	
	/** = all audio senders with me as the sender */
	public HashMap<String, AudioSender> getAllSenders() {
		return this.allSenders;
	} // end getAllSenders method

	private void create() {
		int sCnt = 0; // counts how many senders created
		int rCnt = 0; // counts how many receivers created
		boolean[] sendPrmt = {true, false, false}; // permission to send audio to spaces
		for (int i = 0; i < this.allUserIP.length; i++) {
			// if the user is running the code
			if (this.allUserIP[i].equals(myIP)) {
				myUN = this.allUserNames[i];
				for (int j = 0; j < this.allUserIP.length; j++) {
					if (i != j) {
						if (i < j) {
							int port = 5010 + i + j * 2 - 1;
							AudioSender as = new AudioSender(port, sendPrmt);
							this.allSenders.put(this.allUserNames[j], as);
							sCnt++;
							System.out.println("AudioSender created from " + allUserIP[i]
									+ " to " + allUserIP[j] + " at " + port);
							AudioReceiver ar = new AudioReceiver(allUserIP[j],(port + 1));
							this.allReceivers.put(this.allUserNames[j], ar);
							rCnt++;
							System.out.println("AudioReceiver created from " + allUserIP[i]
							      + " to " + allUserIP[j] + " at " + (port + 1));
						}
						else {
							int port = 5010 + j + i * 2 - 1;
							AudioSender as = new AudioSender(port + 1, sendPrmt);
							this.allSenders.put(this.allUserNames[j], as);
							sCnt++;
							System.out.println("AudioSender created from " + allUserIP[i]
							         + " to " + allUserIP[j] + " at " + (port + 1));
							AudioReceiver ar = new AudioReceiver(allUserIP[j],port);
							this.allReceivers.put(this.allUserNames[j], ar);
							rCnt++;
							System.out.println("AudioReceiver created from " + allUserIP[i]
							    + " to " + allUserIP[j] + " at " + port);
						}
					}
				}
			}
		}
	}
	
	/** starts the audio receivers and senders */
	public void start() {
		for (int i = 0; i < this.allUserNames.length; i++) {
			if (!this.allUserNames[i].equals(this.myUN)) {
				this.allSenders.get(this.allUserNames[i]).start();
				this.allReceivers.get(this.allUserNames[i]).start();
			}
		}
	}

	public boolean allCreated() {
		return ((this.allUserNames.length-1) == this.allReceivers.size() &&
				(this.allUserNames.length-1) == this.allSenders.size());
	}

	public AudioConnect(ArrayList<Person> people) {
		if (people != null && people.size() > 0) {
			Collections.sort(people);
			this.allUserNames = new String[people.size()];
			this.allUserIP = new String[people.size()];
			this.allSenders = new HashMap<String, AudioSender>();
			this.allReceivers = new HashMap<String, AudioReceiver>();
			for (int i = 0; i < people.size(); i++) {
				this.allUserNames[i] = people.get(i).getUserName();
				this.allUserIP[i] = people.get(i).getIP();
			}
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
}
