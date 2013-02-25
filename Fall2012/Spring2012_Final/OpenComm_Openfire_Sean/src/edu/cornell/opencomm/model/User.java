package edu.cornell.opencomm.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.LoginController;
import edu.cornell.opencomm.network.Network;

/* An object representing a user who is taking part in the conversation */

public class User {
	// DEBUGGING
	private static String LOG_TAG = "Model.User"; // for error checking
	private static boolean D = true;
	
	String username; // The User's JID
	String nickname; // The User's chat nickname
	VCard vCard; // The User's vCard, used to pass image
	int image; // icon - will be replaced by vCard
	
	//Crystal
	public int user_color=R.color.blue;
	static int[] colors={R.color.blue,R.color.green,R.color.orange,R.color.teal,
		R.color.light_purple,R.color.red, R.color.pink, R.color.grass_green,R.color.ocean_blue,
		R.color.showy_green};
	static int color_pointer=0;
	static Hashtable<String, Integer> user_color_table= new Hashtable<String, Integer> ();
	
	// Maps JID to User
	private static HashMap<String, User> allUsers = new HashMap<String, User>();
	
	// Maps nickname to User -- TODO: delete if not needed by UI
	public static HashMap<String,User> nickname_to_user = 
			new HashMap<String,User>();
    
	/** CONSTRUCTOR: = a new User
	 * 
	 * @param username - the JID of the User
	 * @param nickname - the User's chosen nickname
	 * @param image - will be replaced by vCard
	 */
	public User(String username, String nickname, int image){
        if (D){
        	Log.v(LOG_TAG, "Made a person for the user " + username);
        }
        this.username = username;
        this.nickname = nickname;
        if (image == 0){
        	this.image = R.drawable.question;
        } else {
            this.image = image;	
        }
        
        //Load VCard if possible
        this.vCard = new VCard();
        try {
			vCard.load(LoginController.xmppService.getXMPPConnection(), username);
		} catch (XMPPException e1) {
			// TODO Auto-generated catch block
			Log.v(LOG_TAG, "VCard did not load for " + username + ". Trying to create one...");
		}
      //Create VCard if needed
        if (vCard.equals(null)){
        	URL imUrl;
			try {
				imUrl = new URL("http://i.imgur.com/CDewa.jpg");
	        	vCard.setAvatar(imUrl);
			} catch (MalformedURLException e) {
				Log.v(LOG_TAG, "Fix the URL, dummy!");
			}
        	vCard.setJabberId(username);
        	vCard.setNickName(nickname);
        	Log.v(LOG_TAG, "Success! made a VCard for " + username);
        	try {
				vCard.save(LoginController.xmppService.getXMPPConnection());
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				Log.v(LOG_TAG, "Failed to save VCard to server for " + username);
			}
        }
        
        if (user_color_table.containsKey(username)){
        	user_color=user_color_table.get(username);
        }else{
        user_color=colors[color_pointer];
        color_pointer=(color_pointer>=9? 0 : ++color_pointer);
        user_color_table.put(username, user_color);
	   }
        allUsers.put(username, this);
        nickname_to_user.put(nickname, this);
	}
	
	// SETTERS AND GETTERS
	
	/** @return - the User's JID */
	public String getUsername(){
        return username;
	}
	
	/** @return - the User's chat nickname */
	public String getNickname(){
        return nickname;
	}
	
	/** @return - the int representation of the User's image */
	public int getImage(){
		return image;
	}
	
	/** @return - the User's vCard */
	public VCard getVCard(){
		return vCard;
	}

	/** Change the user's nickname 
	 * @param new_nickname - the User's new nickname
	 */
	// TODO: ask Design if we want this/if a User can have different nicks for each Space
	public void setNickname(String new_nickname){
	   nickname = new_nickname;
	}
	
	/** @return - all users with their JID as the key */
	public static HashMap<String, User> getAllUsers() {
		return allUsers;
	} // end getAllUsers method
	
	/** @return - all Users with their nickname as the key */
	public static HashMap<String, User> getAllNicknames(){
		return nickname_to_user;
	}
}

