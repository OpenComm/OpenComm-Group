package edu.cornell.opencomm.model;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.packet.VCard;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.audio.JingleController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

/* An object representing a user who is taking part in the conversation */

public class User implements Comparable<User>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "Model.User";
	/**
	 * 
	 */
	private static String LOG_TAG = "Model.User";
	/**
	 * 
	 */
	private static boolean D = true;

	/**
	 * The JID of the user
	 */
	String username;
	/**
	 * The nickname of the user
	 */
	String nickname;
	/**
	 * The VCard of the user
	 */
	VCard vCard;
	/**
	 * The image of the user
	 * use BitMap
	 */
	int image;

	/** 
	 * A bitmap of the User's image
	 */
	Bitmap userImage;
	
	private Presence presence;

	/**
	 */
	public int userColor;

	/**
	 */
	public Point location;

	// for audio
	private JingleController jCtrl;

	/**
	 * use the new constructor with all needed fields
	 * 
	 * @param username
	 *            - the JID of the User
	 * @param nickname
	 *            - the User's chosen nickname
	 * @param image
	 *            - will be replaced by vCard
	 */
	public User(String username, String nickname, int image) {
		if (D) {
			Log.v(LOG_TAG, "Made a person for the user " + username);
			Log.v(LOG_TAG, "nickname: "+nickname);
		}
		this.username = username;
		this.nickname = nickname;
		if (image == 0) {
			this.image = R.drawable.contact_default_image;
		} else {
			this.image = image;
		}
		this.userColor = UserManager.getUserColor(username);
		this.jCtrl = new JingleController(this);
		JingleController.getUsernameToJingleController().put(this.username, this.jCtrl);
	}

	/**
	 * CONSTRUCTOR := a new User
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param photo
	 * @param title
	 * @param username
	 * @param nickname
	 */
	public User(String firstname, String lastname, String email,
			InputStream photo, String title, String username, String nickname) {
		this(username, nickname, 0);
		try {
			// try to get User's VCard
			this.vCard.load(NetworkService.getInstance().getConnection(), username);
		} catch (XMPPException e2) {
			// if no VCard exists, create one
			byte[] bytes = null;
			this.vCard = new VCard();
			try {
				bytes = new byte[photo.available()];
				photo.read(bytes);
			} catch (IOException e1) {
				Log.v(TAG, "cannot get image");
			}
			this.vCard.setFirstName(firstname);
			this.vCard.setLastName(lastname);
			this.vCard.setEmailHome(email);
			this.vCard.setAvatar(bytes);
			this.vCard.setNickName(this.nickname);
			this.vCard.setJabberId(username);
			try {
				this.vCard.save(NetworkService.getInstance().getConnection());
			} catch (XMPPException e) {
				Log.v(TAG, "error creating User");
			}
		}
		this.userImage = getBitMap();
		this.setPresence(NetworkService.getInstance().getConnection().getRoster().getPresence(username + "@opencomm"));
	}

	public User(Occupant o) {
		this(o.getJid(), o.getNick(), 0);
	}
	
	public User(VCard vCard) {
		this.vCard = vCard;
		this.username = vCard.getJabberId();
		this.nickname = vCard.getNickName();
		this.setPresence(NetworkService.getInstance().getConnection().getRoster().getPresence(username + "@opencomm"));
	}

	/** @return - the User's JID */
	public String getUsername() {
		return this.username;
	}

	/** @return - the User's chat nickname */
	public String getNickname() {
		return this.nickname;
	}

	public int getImage() {
		return this.image;
	}

	/** @return - the User's vCard */
	public VCard getVCard() {
		return this.vCard;
	}

	/** @return - the jingle controller associated with this user */
	public JingleController getJingle() {
		return this.jCtrl;
	}

	@Override
	public int compareTo(User arg0) {
		return (username.compareTo(arg0.getUsername()));
		// returns alphabetic comparison of usernames by using string compareTo
	}

	public void storeImage(Bitmap bitmap) {
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, blob);
		byte[] bitmapdata = blob.toByteArray();
		vCard.setAvatar(bitmapdata);
	}

	public Bitmap getBitMap() {
		byte[] bitmapdata = vCard.getAvatar();
		return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
	}

	//REQUIRED METHODS

	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException {
		// write 'this' to 'out'...
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		// populate the fields of 'this' from the data in 'in'...
	}

	public Point getLocation() {
		return this.location;
	}

	public void setLocation(Point location2) {
		this.location = location2; 		
	}

	public int getX() {
		// TODO Auto-generated method stub
		return this.location.x;
	}
	
	public int getY(){
		return this.location.y; 
	}

	public void setImage(int image) {
		this.image = image; 		
	}

	public Presence getPresence() {
		return presence;
	}

	public void setPresence(Presence presence) {
		this.presence = presence;
	}
}

