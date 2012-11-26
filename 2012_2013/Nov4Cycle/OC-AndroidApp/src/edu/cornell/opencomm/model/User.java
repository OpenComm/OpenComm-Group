package edu.cornell.opencomm.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.network.NetworkService;

/* An object representing a user who is taking part in the conversation */

public class User implements Comparable<User>, Serializable {
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

	/**
	 * TODO Ankit: Move this out of here it is only used for conference and
	 * store it in a map in chat space model The color of the user contact card
	 */
	public int userColor;

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
		}
		this.username = username;
		this.nickname = nickname;
		if (image == 0) {
			this.image = R.drawable.contact_default_image;
		} else {
			this.image = image;
		}
		this.userColor = UserManager.getUserColor(username);
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
			this.vCard.load(NetworkService.getInstance().getConnection(),
					username);
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

	public int compareTo(User arg0) {
		return (getUsername().compareTo(arg0.getUsername()));
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
}
