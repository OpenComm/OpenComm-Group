package edu.cornell.opencomm.model;

import android.database.Cursor;

/**
 * More information here:
 * http://www.mysamplecode.com/2012/07/android-listview-cursoradapter
 * -sqlite.html
 * 
 * @author noranq
 * 
 */

public class ContactsDbAdapter {
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PHONE_NUMBER = "phone_number";
	public static final String KEY_PICTURE = "picture";

	private static final String TAG = "ContactsDbAdapter";
	private static final String DATABASE_NAME = "Opencomm";
	private static final String MYSQL_TABLE = "Contacts";

	public ContactsDbAdapter() {

	}

	/** Open writable database */
	public void open() {
		// TODO Connect to mysql database
	}

	/** Retrieve all contacts */
	public Cursor getAllContacts() {
		// TODO Fetch all contacts from mysql database
		Cursor contacts = null;
		return contacts;
	}
}
