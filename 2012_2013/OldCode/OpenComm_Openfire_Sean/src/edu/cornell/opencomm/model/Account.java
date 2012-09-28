package edu.cornell.opencomm.model;

import android.util.Log;
import edu.cornell.opencomm.Values;

/**
 *
 * @author jonathan
 *
 */
public class Account {

    public static String TAG = "Account";
    public static boolean D = Values.D;

    private String username;
    private String password;
    private String title;

    /**
     * Constructor
     */
    public Account(String username, String password, String title) {
        if (D) Log.d(TAG, "Account constructor called");
        this.username = username;
        this.password = password;
        this.title = title;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return password
     */
    public String getPassword(){
        return password;
    }
}
