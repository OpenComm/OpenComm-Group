package edu.cornell.opencomm;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import edu.cornell.opencomm.network.sp11.Networks;

public class Space {
	private static final String LOGTAG = "opencomm.Space";
	SpaceView spaceView;
	Context context;
	LinkedList<PersonView> people;
	MultiUserChat muc;
	protected String mucName = "";
	
	public Space(Context c) {
		context = c;
		spaceView = new SpaceView(context, this);
		people = new LinkedList<PersonView>();
	}

	public void addSpaceView(SpaceView sv) {
		spaceView = sv;
		sv.createIcons(this);
		sv.invalidate();
	}
	
	protected Space(){
		
	}

	/** Add a person to this space by putting in their icon, tell the network
	 * that you initiated this adding */
	public void add(Person p) {
		PersonView pv = new PersonView(context, p,
				(int) (Math.floor((SpaceView.w - 55) * Math.random())),
				(int) (Math.floor(SpaceView.mainScreenH * Math.random())), 55,
				55);
		this.people.add(pv);
		// tell the network
		this.inviteToMUC(p);
	}
	
	/** Add a person to this space by putting in their icon because someone else
	 * added this person in */
	public void forcedAdd(Person p) {
		PersonView pv = new PersonView(context, p,
				(int) (Math.floor((SpaceView.w - 55) * Math.random())),
				(int) (Math.floor(SpaceView.mainScreenH * Math.random())), 55,
				55);
		this.people.add(pv);
	}

	public LinkedList<PersonView> getPeople() {
		return people;
	}

	public SpaceView getSpaceView() {
		return spaceView;
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.LTGRAY);

		for (PersonView p : people) {
			p.draw(canvas);
		}

		for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
			p.draw(canvas);
		}
	}
	
	/**
	 * @return the mucName where chat is held on the xmpp server
	 */
	public String getMucName() {
		return mucName;
	}

	/**
	 * @param mucName the mucName which is associated with this private space at the XMPP server
	 */
	public void setMucName(String mucName) {
		this.mucName = mucName + Networks.SERVICE_NAME;
		Log.i(LOGTAG,"Setting muc room name to: " + this.mucName);
	}
	
	/**
	 *  **Call Set MUC name before calling this method**
	 * Create a MUC on the server with the room name that is stored in this class
	 */
	public void createMUC(XMPPConnection conn, String username) throws XMPPException {
		Log.i(LOGTAG,"Creating MUC " + mucName + "\n conn="+conn+"\n user="+username + "\nConnected to server ="
				+( (conn != null && conn.isConnected()) ? "yes":"no") );
		muc = new MultiUserChat(conn, this.mucName);
		if (muc != null){
			Log.i(LOGTAG,"Muc Created, muc room name ="+ muc.getRoom());
			muc.join(username);
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		}else{
			Log.e(LOGTAG,"MUC not created because after trying the object was null");
		}

		/**
		 * Add a listener to the MUC for this space which will by notified when
		 * someone leaves/joins this room
		 */
		muc.addParticipantListener(new PacketListener(){
			@Override
			public void processPacket(Packet packet) {
				processPresencePacket(packet); //handle elsewhere
			}
			
		});
	}
	
	/**
	 * The MUC object associated with this space has received an upadate saying someone has
	 * joined/left the room, we need to update our list of people in the MUC to be
	 * consistent with the server.
	 * @param pres_in
	 * 		The packet received by the presence packet filter.
	 */
	public void processPresencePacket(Packet pres_in){
		Log.i(LOGTAG,"MUC + " + muc.getRoom() + " recieved a packet." + pres_in.toXML());
		org.jivesoftware.smack.packet.Presence pres = (org.jivesoftware.smack.packet.Presence)pres_in;
		String from = pres.getFrom();
		switch (pres.getType()){
		case available:
			// from has joined the chat.  They should be added to this space
			// and shown in the GUI
			Person p = MainApplication.id_to_person.get(from);
			Log.i(LOGTAG,"Person " + p.getName() + "("+p.getXMPPid()+")" + " joined " + muc.getRoom());
			this.forcedAdd(p); // remove the person from this space. using force because network informed us
			break;
		case unavailable:
			// from has left the chat, remove them from the private space
			Log.i(LOGTAG,"Person with xmpp id " + "("+from+")" + " left the room " + muc.getRoom());
			MainApplication.mainApp.forcedRemoveIcon(this, from);
			break;
		default:
			Log.i(LOGTAG,"Recevied presence update I don't handle.");	
		}
	}
	
	/**
	 * Invite a list of people to the MUC which on the server represents this space
	 * @param invitees
	 * 		People to invite to the MUC
	 */
	public void inviteToMUC(List<Person> invitees){
		// Send invitation to invitees
		for (Person inv : invitees) {
			inviteToMUC(inv);
		}
	}
	
	/**
	 * Invites the single user to the MUC room
	 * @param invitee
	 */
	public void inviteToMUC(Person invitee){
		Log.i(LOGTAG,"Inviting " + invitee.getName() + " to room " + this.mucName);
		String xmppID = invitee.getXMPPid();
		if (muc != null){
			Log.i(LOGTAG,"Room " + muc.getRoom() + " is joined =" + (muc.isJoined() ? "yes":"no"));
			muc.invite(xmppID, MainApplication.REASON);	
		}
		else{
			Log.e(LOGTAG,"Cannont invite person to room " + this.mucName + " because the muc object is null");
		}
		
	}

	/**
	 * Kick a user from the multi-user chat that represents this space on the server
	 * @param xmpPid
	 * 		the xmpp-id for that user
	 * @throws XMPPException 
	 * 		thrown when a problem arises while kicking the user from the MUC
	 */
	public void removeUser(String xmpPid) throws XMPPException {
		Log.i(LOGTAG,"Booting" + xmpPid + " from room: " + this.mucName);
		muc.kickParticipant(xmpPid, "Someone doesn't like you.");
	}
	
	/**
	 * Destroy the room associated with this space, this will kick everyone from the room
	 * @throws XMPPException
	 * 		thrown if fails to destroy the room
	 */
	public void destroyRoom() throws XMPPException {
		Log.i(LOGTAG,"Destroying muc room: " + this.mucName);
		muc.destroy("The Reason of Destruction", "");
	}
}
