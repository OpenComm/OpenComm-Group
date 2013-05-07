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
import com.cornell.opencomm.jingleimpl.sessionmgmt.SessionCallStateMachine;
import com.cornell.opencomm.networking.PortHandler;
import com.cornell.opencomm.rtpstreamer.MicrophonePusher;
import com.cornell.opencomm.rtpstreamer.ReceiverThread;
import com.cornell.opencomm.rtpstreamer.SenderThread;

import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.network.NetworkService;

public class JingleController {
	private static final String TAG = "JingleController";

	private XMPPConnection connection;
	private SessionCallStateMachine state;
	private JingleIQActionMessages jiqActionMessageSender;
	private IQMessages iqMessageSender;
	private String remoteIPAddress;
	private String localIPAddress;
	private int remotePort;
	private int localPort;
	public ReceiverThread receiver;
	public SenderThread sender;
	public MicrophonePusher pusher;
	private String buddyJID;
	private User user;
	private String SID;

	private static HashMap<String, JingleController> UsernameToJingleController = new HashMap<String, JingleController>();
	private SoundSpatializer soundSpatializer;

	public JingleController(User user) {
		Log.v(TAG, "making jingle controller for " + user.getUsername());
		this.user = user;
		this.connection = NetworkService.getInstance().getConnection();
		this.state = new SessionCallStateMachine();
		Log.v(TAG, "made state machine");
		this.soundSpatializer = new SoundSpatializer();
		Log.v(TAG, "made sound spatializer");
		jiqActionMessageSender = new JingleIQActionMessages();
		iqMessageSender = new IQMessages();
		Log.v(TAG, "make message senders");
		jiqActionMessageSender.setConnection(connection);
		iqMessageSender.setConnection(connection);
		JingleController.UsernameToJingleController.put(
				this.user.getUsername(), this);
	}

	public static HashMap<String, JingleController> getUsernameToJingleController() {
		return UsernameToJingleController;
	}

	public SoundSpatializer getSoundSpatializer() {
		return soundSpatializer;
	}

	public void setSoundSpatializer(SoundSpatializer soundSpatializer) {
		this.soundSpatializer = soundSpatializer;
	}

	// TODO: Why is this one method!?!? Refactor!
	/**
	 * Processes the packet handed to it by the
	 * <code>JingleIQBuddyPacketRouter</code>
	 * 
	 * This method in conjunction with the
	 * <code>SessionCallStateMachine.changeSessionState</code> handles the state
	 * transitions of an ongoing jingle session.
	 * 
	 * @param incomingPacket
	 */
	public void processPacket(IQ incomingPacket) {

		if (incomingPacket instanceof JingleIQPacket) {
			JingleIQPacket jiqPacket = (JingleIQPacket) incomingPacket;

			Log.i("JingleController",
					"JIQPacket Received in JingleController ("
							+ state.getSessionStateString() + ") : " + "From: "
							+ jiqPacket.getFrom() + "To: " + jiqPacket.getTo()
							+ "Initiator: " + jiqPacket.getAttributeInitiator()
							+ "Responder: " + jiqPacket.getAttributeResponder()
							+ "Action: " + jiqPacket.getAttributeAction());

			// Send ACK
			iqMessageSender.sendResultAck(jiqPacket);
			Log.d("JingleController",
					"result acknowledgement sent for JIQPacket with action "
							+ jiqPacket.getAttributeAction());

			String action = jiqPacket.getAttributeAction();
			if (state.getSessionState() == SessionCallStateMachine.STATE_ENDED) {
				if (action
						.equals(JingleIQPacket.AttributeActionValues.SESSION_INITIATE)) {
					state.changeSessionState(action); // Sets to Pending
					// Check to see if we can respond with session accept or
					// with session_terminate
					if (supportApplication(jiqPacket)) {
						if (supportTransport(jiqPacket)) {
							// Get the initiator's IP and Ports
							String ipAddress = jiqPacket.getElementContent()
									.get(0).getElementTransport()
									.getCandidates().get(0).getAttributeIP();
							Integer port = (int) jiqPacket.getElementContent()
									.get(0).getElementTransport()
									.getCandidates().get(0).getAttributePort();

							setRemoteIPAddress(ipAddress);
							setRemotePort(port);

							// Can send out session_accept
							jiqActionMessageSender.sendSessionAccept(jiqPacket,
									this);
							// TODO: Time to send RTP Comfort Noise

							// TODO: If not received RTP Noise upto certain
							// time, terminate session.

							// TODO: If receive Noise, then :
							state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT); // sets
																											// to
																											// Active.
							Log.i(JingleController.TAG,
									"State change to Accept? : "
											+ state.getSessionStateString());

							try {
								Log.i("JingleController",
										"Starting receiver on port "
												+ this.getLocalPort());
								SipdroidSocket recv_socket = new SipdroidSocket(
										this.getLocalPort());
								receiver = new ReceiverThread(recv_socket, this);
								receiver.start();
								int sendPort = PortHandler.getInstance()
										.getSendPort();
								SipdroidSocket send_socket = new SipdroidSocket(
										sendPort);
								BlockingQueue<short[]> queue = new LinkedBlockingQueue<short[]>();
								Log.i("JingleController", "Starting sender to "
										+ this.getRemoteIPAddress() + ":"
										+ this.getRemotePort() + " on port "
										+ sendPort);
								sender = new SenderThread(true, 8000 / 160,
										160, send_socket,
										this.getRemoteIPAddress(),
										this.getRemotePort(), queue);
								// pusher =
								// AudioPusher.getInstance("/test3.wav",
								// buddyJID, queue);
								MicrophonePusher pusher = MicrophonePusher
										.getInstance(String.valueOf(sendPort),
												queue);
								sender.start();
								if (!pusher.isRunning())
									pusher.start();
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							// send terminate
							Log.e("JingleController", "Transport unupported");
							ReasonElementType reason = new ReasonElementType(
									ReasonElementType.TYPE_UNSUPPORTED_TRANSPORTS,
									null);
							reason.setAttributeSID(jiqPacket.getAttributeSID());
							jiqActionMessageSender.sendSessionTerminate(
									jiqPacket.getTo(), jiqPacket.getFrom(),
									jiqPacket.getAttributeSID(), reason, this);
							state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE);
						}
					} else {
						// send terminate
						Log.e("JingleController", "Application unupported");
						ReasonElementType reason = new ReasonElementType(
								ReasonElementType.TYPE_UNSUPPORTED_APPLICATIONS,
								null);
						reason.setAttributeSID(jiqPacket.getAttributeSID());
						jiqActionMessageSender.sendSessionTerminate(
								jiqPacket.getTo(), jiqPacket.getFrom(),
								jiqPacket.getAttributeSID(), reason, this);
						state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE);
					}
				} else {
					Log.i(JingleController.TAG,
							"This Combination not supported yet " + "State: "
									+ state.getSessionStateString()
									+ "Action: " + action);
				}

			} else if (state.getSessionState() == SessionCallStateMachine.STATE_PENDING) {
				if (action
						.equals(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT)) {
					String ipAddress = jiqPacket.getElementContent().get(0)
							.getElementTransport().getCandidates().get(0)
							.getAttributeIP();
					Integer port = (int) jiqPacket.getElementContent().get(0)
							.getElementTransport().getCandidates().get(0)
							.getAttributePort();

					setRemoteIPAddress(ipAddress);
					setRemotePort(port);

					// Can send out session_accept
					// jiqActionMessageSender.sendSessionAccept(jiqPacket,
					// this);
					// TODO: Time to send RTP Comfort Noise

					// TODO: If not received RTP Noise upto certain time,
					// terminate session.

					// TODO: If receive Noise, then :
					// state.changeSessionState(action); // set to Active

					state.changeSessionState(JingleIQPacket.AttributeActionValues.SESSION_ACCEPT); // sets
																									// to
																									// Active.
					Log.i(JingleController.TAG, "State after accepting: "
							+ state.getSessionStateString());

					try {
						Log.i("MUCBuddy",
								"Starting receiver on port "
										+ this.getLocalPort());
						SipdroidSocket recv_socket = new SipdroidSocket(
								this.getLocalPort());
						receiver = new ReceiverThread(recv_socket, this);
						receiver.start();
						int sendPort = PortHandler.getInstance().getSendPort();
						SipdroidSocket send_socket = new SipdroidSocket(
								sendPort);
						BlockingQueue<short[]> queue = new LinkedBlockingQueue<short[]>();
						Log.i("MUCBuddy",
								"Starting sender to "
										+ this.getRemoteIPAddress() + ":"
										+ this.getRemotePort() + " on port "
										+ sendPort);
						sender = new SenderThread(true, 8000 / 160, 160,
								send_socket, this.getRemoteIPAddress(),
								this.getRemotePort(), queue);
						// pusher = AudioPusher.getInstance("/test3.wav",
						// buddyJID, queue);
						MicrophonePusher pusher = MicrophonePusher.getInstance(
								String.valueOf(sendPort), queue);
						sender.start();
						if (!pusher.isRunning())
							pusher.start();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (action
						.equals(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE)) {
					state.changeSessionState(action); // set to Terminate
				} else {
					Log.i(JingleController.TAG,
							"This Combination not supported yet " + "State: "
									+ state.getSessionStateString()
									+ "Action: " + action);
				}
			} else if (state.getSessionState() == SessionCallStateMachine.STATE_ACTIVE) {
				if (action
						.equals(JingleIQPacket.AttributeActionValues.SESSION_TERMINATE)) {
					state.changeSessionState(action); // set to Terminate
					if (receiver.isRunning())
						receiver.halt();
					if (sender.isRunning())
						sender.halt();
					// Maybe this will help?
					pusher.removeQueue(String.valueOf(PortHandler.getInstance().getSendPort()));
					if (pusher.isRunning() && pusher.numQueues() == 0)
						pusher.halt();
				} else {
					Log.i(JingleController.TAG,
							"This Combination not supported yet " + "; State: "
									+ state.getSessionStateString()
									+ "; Action: " + action);
				}
			}

		} else if (incomingPacket instanceof IQ) {
			IQ iq = incomingPacket;
			Log.i(JingleController.TAG,
					"IQ Received in JingleController: From: " + iq.getFrom()
							+ "; To: " + iq.getTo() + "; Type: " + iq.getType());

			if (iq.getType() == IQ.Type.RESULT) {
				if (state.getSessionState() == SessionCallStateMachine.STATE_PENDING) {
					state.setSessionState(SessionCallStateMachine.STATE_PENDING); // Stay
																					// in
																					// pending
				} else if (state.getSessionState() == SessionCallStateMachine.STATE_ACTIVE) {
					state.setSessionState(SessionCallStateMachine.STATE_ACTIVE);
				} else if (state.getSessionState() == SessionCallStateMachine.STATE_ENDED) {
					state.setSessionState(SessionCallStateMachine.STATE_ENDED);
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

	public SessionCallStateMachine getSessionState() {
		return state;
	}

	public void setSID(String sid) {
		SID = sid;
	}

	public String getSID() {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBuddyJID() {
		return buddyJID;
	}

}
