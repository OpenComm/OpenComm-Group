package edu.cornell.opencomm;

import java.util.ArrayList;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AddDeleteActivity extends Activity {

	private static final String TAG = "SpaceBackend";

	// Connection to XMPPserver
	private Connection conn;
	// all spaces created
	public static ArrayList<Space> allChats = new ArrayList<Space>();

	// GUI for debug
	private ListView privateSpaces;
	private ListView conferences;
	private Button addSpace;
	private Button deleteSpace;
	private EditText spaceName;
	private ArrayAdapter<String> privateSpacesAdapter;
	private ArrayAdapter<String> conferencesAdapter;

	/**
	 * Called when the activity is first created. Initializes GUI and Connection
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SmackConfiguration.setPacketReplyTimeout(10000);

		// Initialize GUI for debugging
		privateSpaces = (ListView) findViewById(R.id.lvPrivateSpaces);
		conferences = (ListView) findViewById(R.id.lvConferences);
		addSpace = (Button) findViewById(R.id.bAdd);
		deleteSpace = (Button) findViewById(R.id.bDelete);
		spaceName = (EditText) findViewById(R.id.etSpaceName);

		// Add adapters to views for debugging
		privateSpacesAdapter = new ArrayAdapter<String>(this,
				R.layout.debuglist);
		conferencesAdapter = new ArrayAdapter<String>(this, R.layout.debuglist);
		privateSpaces.setAdapter(privateSpacesAdapter);
		conferences.setAdapter(conferencesAdapter);

		// Create connection for testing purposes.
		// TODO: replace with Connection from service
		conn = new XMPPConnection("jabber.org");
		try {
			conn.connect();
			conn.login("opencommsec", "secopencomm");
		} catch (XMPPException e) {
			Log.v(TAG, "Can't connect");
			Log.e(TAG, "exception: ", e);
			System.exit(0);
		}
	}

	@Override
	/**
	 * Creates the main space and adds it to the GUI.
	 * Adds listeners for GUI buttons
	 */
	protected void onStart() {
		super.onStart();

		try {
			addSpace(conn, true, "MainSpace_Demo");
		} catch (XMPPException e) {
			Log.v(TAG, "Main space problem!");
			Log.e(TAG, "exception: ", e);
			System.exit(0);
		}

		// add space on click
		addSpace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name = spaceName.getText().toString();

				try {
					addSpace(conn, false, name);
				} catch (XMPPException e) {
					Log.v(TAG, "Problem adding private space!");
					Log.e(TAG, "exception: ", e);
					System.exit(0);
				}

				spaceName.setText("");

			}
		});

		// delete space on click
		deleteSpace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = spaceName.getText().toString();
				try {
					deleteSpace(name);
				} catch (Throwable e) {
					Log.v(TAG, "Problem deleting space!");
					Log.e(TAG, "exception: ", e);
					System.exit(0);
				}

				spaceName.setText("");
			}

		});
	}

	/**
	 * Add a space. 1) create MUC object 2) join MUC 3) Configure MUC & set self
	 * to owner 4) add MUC to LinkedList of all MUCs
	 * 
	 * @param conn
	 *            Connection to XMPP Server
	 * @param isMainSpace
	 *            whether or not this is the main space
	 * @param spaceID
	 *            name of the MUC
	 * @throws XMPPException
	 *             if cannot create MUC
	 */
	public void addSpace(Connection conn, boolean isMainSpace, String spaceID)
			throws XMPPException {

		// create MUC
		Space chatRoom = new Space(conn, isMainSpace, spaceID);

		// join MUC
		chatRoom.join("opencommsec");

		// add MUC to ArrayList of MUCs
		allChats.add(chatRoom);

		// DEBUG: output list of all spaces
		// for (Space chat : allChats)
		// Log.v(TAG, chat.toString());

		// add to appropriate part of GUI
		if (chatRoom.isMainSpace()) {
			conferencesAdapter.add(spaceID);
			conferencesAdapter.notifyDataSetChanged();
		}
		if (!chatRoom.isMainSpace()) {
			privateSpacesAdapter.add(spaceID);
			privateSpacesAdapter.notifyDataSetChanged();
		}

		// DEBUG: invite risan to space to ensure it exists
		// chatRoom.getMUC().invite("risan@jabber.org", "Debug");
	}

	/**
	 * Iterates through ArrayList of Spaces to find spaceToDestroy. Calls
	 * destroy() on Space spaceToDestroy. Removes spaceToDestroy from GUI.
	 * 
	 * @param spaceToDelete
	 *            - name of Space to delete
	 * @throws Throwable
	 *             - if there's a problem with spaceToDelete.destroy()
	 * @throws XMPPException
	 *             - if there's a problem with spaceToDelete.destroy()
	 */
	public void deleteSpace(String spaceToDelete) throws Throwable,
			XMPPException {

		// Call server to destroy MUC
		Space destroy = null;
		for (Space chat : allChats) {
			// DEBUG: output list of all chats
			// Log.v(TAG, chat.getSpaceId());
			if (spaceToDelete.equals(chat.getSpaceId()))
				destroy = chat;
		}
		destroy.destroy();

		// Remove MUC from ArrayList of MUCs
		allChats.remove(destroy);

		// DEBUG: output list of all spaces
		// for (Space chat : allChats)
		// Log.v(TAG, chat.toString());

		// Remove MUC from appropriate adapter (Won't delete main space)
		if (destroy.isMainSpace())
			conferencesAdapter.remove(spaceToDelete);
		if (!destroy.isMainSpace())
			privateSpacesAdapter.remove(spaceToDelete);

		privateSpacesAdapter.notifyDataSetChanged();
	}
}