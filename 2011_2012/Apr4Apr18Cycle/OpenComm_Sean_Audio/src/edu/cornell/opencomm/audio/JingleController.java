package edu.cornell.opencomm.audio;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.sipdroid.net.SipdroidSocket;

import android.util.Log;

import com.cornell.opencomm.jingleimpl.JingleIQPacket;
import com.cornell.opencomm.jingleimpl.ReasonElementType;
import com.cornell.opencomm.jingleimpl.sessionmgmt.IQMessages;
import com.cornell.opencomm.jingleimpl.sessionmgmt.JingleIQActionMessages;
import com.cornell.opencomm.jingleimpl.sessionmgmt.JingleIQBuddyPacketRouter;
import com.cornell.opencomm.jingleimpl.sessionmgmt.SessionCallStateMachine;
import com.cornell.opencomm.networking.PortHandler;
import com.cornell.opencomm.rtpstreamer.MicrophonePusher;
import com.cornell.opencomm.rtpstreamer.ReceiverThread;
import com.cornell.opencomm.rtpstreamer.SenderThread;

import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.Network;

public class JingleController {
	// Debugging variable
	private static String TAG = "JingleController";
	private static boolean D = true;
	
	private static HashMap<String, JingleController> UsernameToJingleController = new HashMap<String, JingleController> ();
	
	private User user;
	private String remoteIPAddress = null; // Buddy's IP
	private String localIPAddress = null;
	private int remotePort; // Buddy's port
	private int localPort;
	private SessionCallStateMachine state;
	private String SID = null;	
	private JingleIQActionMessages jiqActionMessageSender = null;
	private IQMessages iqMessageSender = null;	
	private XMPPConnection connection = null;
	
	public ReceiverThread receiver = null;
	//public AudioPusher pusher = null;
	public MicrophonePusher pusher = null;
	public SenderThread sender = null;
	
	private SoundSpatializer soundSpatializer;
	
	public SoundSpatializer getSoundSpatializer() {
		return soundSpatializer;
	}


	public void setSoundSpatializer(SoundSpatializer soundSpatializer) {
		this.soundSpatializer = soundSpatializer;
	}


	public JingleController(User user) {
		this.user = user;
		this.connection = LoginController.xmppService.getXMPPConnection();
		this.soundSpatializer = new SoundSpatializer();
		state = new SessionCallStateMachine("JingleController.constructor for user " + user.getUsername());
				
		jiqActionMessageSender = new JingleIQActionMessages();
		iqMessageSender = new IQMessages();
		
		jiqActionMessageSender.setConnection(connection);
		iqMessageSender.setConnection(connection);
		JingleController.UsernameToJingleController.put(this.user.getUsername(), this);
	}
	
	
	public static HashMap<String, JingleController> getUsernameToJingleControllerMap() {
		return JingleController.UsernameToJingleController;
	}
	
	public String getRemoteIPAddress() {
		return remoteIPAddress;
	}

	public void setRemoteIPAddress(String remoteIPAddress) {
		this.remoteIPAddress = remoteIPAddress;
	}

	public String getLocalIPAddress() {
		return localIPAddress;
	}

	public void setLocalIPAddress(String localIPAddress) {
		this.localIPAddress = localIPAddress;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	/**
	 * Processes the packet handed to it by the <code>JingleIQBuddyPacketRouter</code>
	 * 
	 * This method in conjunction with the <code>SessionCallStateMachine.changeSessionState</code>
	 * handles the state transitions of an ongoing jingle session.
	 * 
	 * @param incomingPacket
	 */
	public void processPacket(IQ incomingPacket){
		
		if (incomingPacket instanceof JingleIQPacket) {
			JingleIQPacket jiqPacket = (JingleIQPacket) incomingPacket;
			
			Log.i(JingleController.TAG, "JIQPacket Received in MUCBuddy: " + 
					"From: "  + jiqPacket.getFrom()+ "To: " + jiqPacket.getTo() +
					"Initiator: " + jiqPacket.getAttributeInitiator() + 
					"Responder: " + jiqPacket.getAttributeResponder() + "Action: " + jiqPacket.getAttributeAction());
			
			// Send ACK
			iqMessageSender.sendResultAck(jiqPacket);
			
			String action = jiqPacket.getAttributeAction();
			if(state.getSessionState() == SessionCallStateMachine.STATE_ENDED){
				if(action.equals(JingleIQPacket.AttributeActionValues.SESSION_INITIATE)){
					state.changeSessionState(action); // Sets to Pending
					
					// Check to see if we can respond with session accept or with session_terminate
					if(supportApplication(jiqPacket)){						
						if(supportTransport(jiqPacket)){
							// Get the initiator's IP and Ports
							String ipAddress = jiqPacket.getElementContent().get(0).getElementTransport().getCandidates().get(0).getAttributeIP();
							Integer port = (int)jiqPacket.getElementContent().get(0).getElementTransport().getCandidates().get(0).getAttributePort(); 
							
							setRemoteIPAddress(ipAddress);
							setRemotePort(port);
							
							// Can send out session_accept							
							jiqActionMessageSender.sendSessionAccept(jiqPacket, this);
							// TODO: Time to send RTP Comfort Noise
							
							// TODO: If not received RTP Noise upto certain time, terminate session.
							
							// TODO: If receive Noise, then : 
							state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT); // sets to Active.
							Log.i(JingleController.TAG, "State: " + state.getSessionStateString());
							
							try {
								Log.i("MUCBuddy", "Starting receiver on port " + this.getLocalPort());
								SipdroidSocket recv_socket = new SipdroidSocket(this.getLocalPort());
								receiver = new ReceiverThread(this, recv_socket);
								receiver.start();
								int sendPort = PortHandler.getInstance().getSendPort();
								SipdroidSocket send_socket = new SipdroidSocket(sendPort);
								BlockingQueue<short[]> queue = new LinkedBlockingQueue<short[]>();
								Log.i("MUCBuddy", "Starting sender to " + this.getRemoteIPAddress() + ":" + this.getRemotePort() + " on port " + sendPort);
								sender = new SenderThread(true, 8000/160, 160, send_socket, this.getRemoteIPAddress(), this.getRemotePort(), queue);
								//pusher = AudioPusher.getInstance("/test3.wav", buddyJID, queue);
								MicrophonePusher pusher = MicrophonePusher.getInstance(String.valueOf(sendPort), queue);
								sender.start();
								if (!pusher.isRunning()) pusher.start();
							} catch (Exception e) {
								e.printStackTrace();
							}													

						} else {
							// send terminate
							ReasonElementType reason = new ReasonElementType(ReasonElementType.TYPE_UNSUPPORTED_TRANSPORTS, null);
							reason.setAttributeSID(jiqPacket.getAttributeSID());
							jiqActionMessageSender.sendSessionTerminate(jiqPacket.getTo(), jiqPacket.getFrom(), jiqPacket.getAttributeSID(), reason, this);
							state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE);
						}
					} else {
						// send terminate
						ReasonElementType reason = new ReasonElementType(ReasonElementType.TYPE_UNSUPPORTED_APPLICATIONS, null);
						reason.setAttributeSID(jiqPacket.getAttributeSID());
						jiqActionMessageSender.sendSessionTerminate(jiqPacket.getTo(), jiqPacket.getFrom(), jiqPacket.getAttributeSID(), reason, this);
						state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE);
					}				
				} else {
					Log.i(JingleController.TAG, "This Combination not supported yet " + 
							"State: " + state.getSessionStateString() +
							"Action: " + action);
				}
				
			} else if(state.getSessionState() == SessionCallStateMachine.STATE_PENDING){
				if(action.equals(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT)){		
					String ipAddress = jiqPacket.getElementContent().get(0).getElementTransport().getCandidates().get(0).getAttributeIP();
					Integer port = (int)jiqPacket.getElementContent().get(0).getElementTransport().getCandidates().get(0).getAttributePort(); 
					
					setRemoteIPAddress(ipAddress);
					setRemotePort(port);
					
					// Can send out session_accept							
					// jiqActionMessageSender.sendSessionAccept(jiqPacket, this);
					// TODO: Time to send RTP Comfort Noise
					
					// TODO: If not received RTP Noise upto certain time, terminate session.
					
					// TODO: If receive Noise, then : state.changeSessionState(action); // set to Active
					
					state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT); // sets to Active.
					Log.i(JingleController.TAG, "State: " + state.getSessionStateString());
					
					try {
						Log.i("MUCBuddy", "Starting receiver on port " + this.getLocalPort());
						SipdroidSocket recv_socket = new SipdroidSocket(this.getLocalPort());
						receiver = new ReceiverThread(this, recv_socket);
						receiver.start();
						int sendPort = PortHandler.getInstance().getSendPort();
						SipdroidSocket send_socket = new SipdroidSocket(sendPort);
						BlockingQueue<short[]> queue = new LinkedBlockingQueue<short[]>();
						Log.i("MUCBuddy", "Starting sender to " + this.getRemoteIPAddress() + ":" + this.getRemotePort() + " on port " + sendPort);
						sender = new SenderThread(true, 8000/160, 160, send_socket, this.getRemoteIPAddress(), this.getRemotePort(), queue);
						//pusher = AudioPusher.getInstance("/test3.wav", buddyJID, queue);
						MicrophonePusher pusher = MicrophonePusher.getInstance(String.valueOf(sendPort), queue);
						sender.start();
						if (!pusher.isRunning()) pusher.start();
					} catch (Exception e) {
						e.printStackTrace();
					}					
					
				} else if(action.equals(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE)){
					state.changeSessionState(action); // set to Terminate
				} else {
					Log.i(JingleController.TAG, "This Combination not supported yet " + 
							"State: " + state.getSessionStateString() +
							"Action: " + action);
				}
			} else if(state.getSessionState() == SessionCallStateMachine.STATE_ACTIVE){
				if(action.equals(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE)){
					state.changeSessionState(action); // set to Terminate
					if (receiver.isRunning()) receiver.halt();
					if (sender.isRunning()) sender.halt();
					pusher.removeQueue(user.getUsername() + Network.DEFAULT_HOST + "/Smack");
					if (pusher.isRunning() && pusher.numQueues() == 0) pusher.halt();
				} else {
					Log.i(JingleController.TAG, "This Combination not supported yet " + 
							"State: " + state.getSessionStateString() +
							"Action: " + action);
				}
			}		
			
		} else if (incomingPacket instanceof IQ) {
			IQ iq = (IQ) incomingPacket;
			Log.i(JingleController.TAG, "IQ Received in MUCBuddy " + "From: "  + iq.getFrom()+ 
					"To: " + iq.getTo() + "Type: " + iq.getType());
			
			if (iq.getType() == IQ.Type.RESULT) {
				if(state.getSessionState() == SessionCallStateMachine.STATE_PENDING){
					state.setSessionState(SessionCallStateMachine.STATE_PENDING); // Stay in pending
				} else if (state.getSessionState() == SessionCallStateMachine.STATE_ACTIVE){
					
				} else if (state.getSessionState() == SessionCallStateMachine.STATE_ENDED){
					
				}			
			}
		}
	}
	
	private boolean supportTransport(JingleIQPacket jiqPacket) {
		boolean supported = true;
				
		return supported;
	}

	private boolean supportApplication(JingleIQPacket jiqPacket) {
		boolean supported = true;
		
		return supported;
	}
	
	public String getbuddyJID(){
		return this.user.getUsername()+ Network.DEFAULT_HOST + "/Smack";
	}
	
	public SessionCallStateMachine getSessionState(){
		return state;
	}
	
	public void setSID(String sid){
		SID = sid;
	}
	
	public String getSID(){
		return SID;
	}

	public JingleIQActionMessages getJiqActionMessageSender() {
		return jiqActionMessageSender;
	}

	public void setJiqActionMessageSender(
			JingleIQActionMessages jiqActionMessageSender) {
		this.jiqActionMessageSender = jiqActionMessageSender;
	}

	public IQMessages getIqMessageSender() {
		return iqMessageSender;
	}

	public void setIqMessageSender(IQMessages iqMessageSender) {
		this.iqMessageSender = iqMessageSender;
	}
}
