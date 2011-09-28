package edu.cornell.opencomm;

import java.util.HashMap;
import java.util.LinkedList;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

import edu.cornell.opencomm.network.sp11.MUCGUI;
import edu.cornell.opencomm.network.sp11.NetworkGUI;
import edu.cornell.opencomm.network.sp11.Networks;
import edu.cornell.opencomm.network.sp11.XMPPConnectConfig;
import edu.cornell.opencomm.network.sp11.XMPPService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author noranq
 * 
 */
public class MainApplication extends Activity {
	
	
	private static String LOGTAG = "MainApplication";	
	public static final String PACKAGE_NAME = "edu.cornell.opencomm";
	public static final String ACTION_CREATE_MAIN = PACKAGE_NAME + "ACTION_CREATE_MAIN";

	// TODO: make mainspace static
	public static Space mainspace = null;
	public static MainApplication mainApp = null;
	static Space space; // the space that you are updating
	static LinkedList<Person> allPeople;
	public static final String PS_ID = "edu.cornell.opencomm.which_ps";
	public static final Object INITIAL_USER = "mucopencomm";
	public static final String MAIN_CONFERENCE_ROOMNAME = "OpenCommTest2MainConf";
	public static final String REASON = "Testing GUI Invites";
	public static HashMap<String,Person> id_to_person;
	private String username;
	private String password;
	private static XMPPConnection conn;

	LinearLayout.LayoutParams PSparams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT, 0.0f);

	public void onCreate(Bundle savedInstanceState) {
		// Create activity and make it listen to XML file
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Spaceview is already specified in XML file
		SpaceView spaceview = (SpaceView) findViewById(R.id.space_view);
		/*
		 * If first starting program, then you always start with the mainchat
		 * room this room must be initialized to hold all people involved in
		 * conversation
		 */
		if (mainspace == null) {
			// Used later by network code to call methods which cannot be static
			MainApplication.mainApp = this;
			// create new space for the main space
			space = new Space(this);
			mainspace = space;
			id_to_person = new HashMap<String,Person>();
			initializeMainSpace(space); // initialize people and pictures in the mainspace
			mainspace.setMucName(MainApplication.MAIN_CONFERENCE_ROOMNAME);
			this.username = getIntent().getStringExtra(Networks.KEY_USERNAME);
			this.password = getIntent().getStringExtra(Networks.KEY_PASSWORD);
			preformInitailLogin("jabber.org",5222);
			
			if (username.equals(MainApplication.INITIAL_USER)){
				// This is the user that will start off sending invites to other users
				// A temporary arrangement where all clients wait around until the one user
				// connects and invites them all to the conference
				try {
					mainspace.createMUC(conn, username);
				} catch (XMPPException e) {
					Log.e(LOGTAG, "Failed to create the main MUC room.\nXMPPException error: " + 
							e.getXMPPError().getCode());
					Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
			                Toast.LENGTH_LONG).show();
					e.printStackTrace();
					System.exit(0); // exit the system because the main conference 
						// has not been created, so we are hosed here
				}		
				mainspace.inviteToMUC(allPeople);
			}
		}

		/*
		 * If creating a new private space, then need to know which already
		 * existing PrivateSpace it is using
		 */
		else {
			int ID = getIntent().getIntExtra(PS_ID, -1);
			// space = (PrivateSpace)PrivateSpaceView.currentSpaces
			for (PrivateSpaceView pv : PrivateSpaceView.currentSpaces) {
				createPrivateSpaceIcon(pv);
				PrivateSpace p = (PrivateSpace) (pv.getSpace());
				if (p.getID() == ID) {
					space = p;
					p.setActivity(this);
				}
			}
		}
		space.addSpaceView(spaceview);
		initializeButtons();
		initializePrivateSpaces();
	}
	
	
	public static void setXMPPConnection(XMPPConnection setTo){
		conn = setTo;
	}
	
	/**
	 * Start the XMPP Service which connects to the 
	 * XMPP server and runs in the background
	 * @param host
	 * 		host name (eg jabber.org)
	 * @param port
	 * 		port to connect on
	 */
	private void preformInitailLogin(String host, int port) {
		// connect via XMPP to host server and port of choice
		// Configure and create new XMPP Connection to the host server through
		// the port with stream compression and SASL Auth. enabled
		XMPPConnectConfig xmppConn =
			new XMPPConnectConfig(host, port, true, true);
		Log.i(LOGTAG, "XMPP Connection created:\n" + xmppConn);
		conn = xmppConn.getXmppConn();
		
		// Connect to the server
		try {
			conn.connect();
		} catch (XMPPException e) {
			Log.e(LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		// confirm that the connection has been established
		Log.i(LOGTAG, "XMPP Connection successfully connected? " + 
				(conn.isConnected() ? "yes" : "no"));
		
		try {
			conn.login(username + "@" + host, password);
		} catch (XMPPException e) {
			Toast.makeText(this, "Failed to login.\nXMPPException error: ",Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, "XMPPException error",e);
			e.printStackTrace();
		}
		// confirm that the connection has been established
		Log.i(LOGTAG, "\tUser " + username + " successfully logged in? " 
				+ (conn.isAuthenticated() ? "yes" : "no"));
		
	} //end preformInitialLogin

/**
 * Need to disconnect from the server, however this is called every time
 * a private space is closed, so we have to do this somewhere else....
 */
//	/** Called by the system to notify a Service that it is no longer used and 
//	 * is being removed. If there is a valid connection, it disconnects it */
//	public void onDestroy() {
//		Log.i(LOGTAG, "Destroying XMPP Connection...");
//			// check that the connection is still valid
//			if (conn != null || conn.isConnected()) {
//				// disconnect
//				conn.disconnect();
//				// confirm
//				Log.i(LOGTAG,"XMPP Connection disconnected? " + (conn.isConnected() ? " no" : " yes"));
//			}
//		Log.i(LOGTAG, "XMPP Connection destroyed.");
//	} // end onDestroy method
	
	

	/*
	 * onStart - draw privatespaceviews and personviews again, need to update in
	 * case model was changed
	 */
	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	/**
	 * If is the first main space created (for main conference chat, then make
	 * sure that everyone is added to this list space. Create everybody to begin
	 * with.
	 */
	public void initializeMainSpace(Space mainspace) {
		allPeople = new LinkedList<Person>();
		Person nora = new Person("Nora", "She's the best!", R.drawable.nora, "jabbertestname@jabber.org");
		Person najla = new Person("Najla", "Is dating Jack Sparrow",
				R.drawable.naj, "mucopencomm@jabber.org");
		Person makoto = new Person("Makoto", "Doesn't respond to texts",
				R.drawable.mak, "opencommss@jabber.org");
		Person risa = new Person("Risa", "Is destined to marry her dog",
				R.drawable.risa, "risan@jabber.org");		
	}

	/** Add touch listeners to the buttons */
	public void initializeButtons() {
		// Initialize main, add, and trash button functionality
		// TODO: the add button is temporary

		// set listener to main button
		Button mainButton = (Button) findViewById(R.id.main_button);
		mainButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					// returns you to the main conversation
					Space main = MainApplication.mainspace;
					Intent intent = ((MainApplication) (main.context))
							.getIntent();
					startActivity(intent);
					finish();
					break;
				}
				return false;
			}
		});

		// temporary button that adds private spaces
		Button addButton = (Button) findViewById(R.id.add_button);
		addButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					// create a new privateSpace icon
					createNewPrivateSpace();
					break;
				}
				return false;
			}
		});

		// set listener to trash button
		Button trashButton = (Button) findViewById(R.id.trash_button);
		trashButton.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					// remove private spaces if they are highlighted
					// TODO for now only deletes one at a time, but later should
					// delete multiple
					PrivateSpaceView deleteSpace = null;
					for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
						if (p.isSelected())
							deleteSpace = p;
					}
					removePrivateSpace(deleteSpace);

					// remove people from private spaces (only works in private
					// space)
					// TODO for now only deletes one at a time, but later can
					// delete multiple
					if (space instanceof PrivateSpace) {
						PersonView deleteIcon = null;
						for (PersonView icon : space.getPeople()) {
							if (icon.isSelected())
								deleteIcon = icon;

						}
						removeIcon(space, deleteIcon);
						// if you are deleting someone from a privatespace then tell the network
						
						
					}
					break;
				}
				return false;
			}
		});
	}

	/**
	 * Automatically initialize all private spaces to the bottom bar no matter
	 * where
	 */
	public void initializePrivateSpaces() {
		/*
		 * if(PrivateSpaceView.currentSpaces!=null){ for(PrivateSpaceView pv :
		 * PrivateSpaceView.currentSpaces){ createPrivateSpaceIcon(pv); } }
		 */
	}

	/** Draw PrivateSpace icon to the screen, add to XML file */
	public void createPrivateSpaceIcon(PrivateSpaceView pv) {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		PrivateSpaceView copy = pv.clone();
		copy.setLayoutParams(PSparams);
		copy.setMinimumWidth(50);
		bottomBar.addView(copy);
		bottomBar.invalidate();
	}

	/** Create a new private space, and make icon appear at bottom of screen, 
	 * in addition tell the network that you initiated this creation */
	public void createNewPrivateSpace() {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		PrivateSpace p = new PrivateSpace(this);
		/**
		 * TODO: MUC name may need to be meaningful
		 * For now, the name of the room we create is a constant string heading
		 * followed by the system time it was created,  this solution is not perfect and
		 * could cause collisions of room names if this were a real system
		 */
		p.setMucName("OpenCommTestRoom_" + System.currentTimeMillis());
		PrivateSpaceView pv = p.getPrivateSpaceView();
		pv.setLayoutParams(PSparams);
		pv.setMinimumWidth(50);
		bottomBar.addView(pv);
		bottomBar.invalidate();
		// tell network that you created a private space
		// and give it the muc name associated with the private space
		try {
			p.createMUC(conn, username);
		} catch (XMPPException e) {
			Log.e(LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		sendCreateNewPrivateSpace(p.getMucName());
	}
	
	/** Create a new private space, and make icon appear at bottom of screen.
	 * Use this method when another person has already created a private space 
	 * and is adding you to it
	 */
	public void addExistingPrivateSpace(PrivateSpace p) {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
//		PrivateSpace p = new PrivateSpace(this);
		PrivateSpaceView pv = p.psv;//p.getPrivateSpaceView();
		pv.setLayoutParams(PSparams);
		pv.setMinimumWidth(50);
		bottomBar.addView(pv);
		bottomBar.invalidate();
	}

	/**
	 * remove a private space button from the screen, and delete that
	 * privateSpace, tell the network that you initiated this deletion
	 */
	public void removePrivateSpace(PrivateSpaceView pv) {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		bottomBar.removeView(pv);
		bottomBar.invalidate();
		PrivateSpaceView.currentSpaces.remove(pv);
		PrivateSpaceView.privateSpaceCounter--;
		Space s = pv.getSpace();
		try {
			s.removeUser(username);// remove yourself from the room
				// deleting a private space on your app is the same as leaving it
				// or saying "I don't want to be in this private space"
		} catch (XMPPException e) {
			Log.e(LOGTAG, "Failed to remove self from private space. XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}		
		// Tell network that you are deleting this private space
//		sendDeleteNewPrivateSpace(pv.getId());
	}
	
	/** If another person deleted a privatespace that you happened to be in, then delete
	 * the privatespace without telling the network b/c it already knows
	 */
	public void forcedRemovePrivateSpace(PrivateSpaceView pv) {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		bottomBar.removeView(pv);
		bottomBar.invalidate();
		PrivateSpaceView.currentSpaces.remove(pv);
		PrivateSpaceView.privateSpaceCounter--;
	}

	/** Creates a new activity for this PrivateSpace */
	public void openNewPSActivity(PrivateSpace p) {
		Intent intent = new Intent(MainApplication.this, MainApplication.class);
		int PSid = p.getID();
		intent.putExtra(PS_ID, PSid);
		startActivity(intent);
	}

	/** Reopens the activity for this PrivateSpace */
	public void restartPSActivity(PrivateSpace p) {
		Intent intent = (p.getActivity()).getIntent();
		startActivity(intent);
		finish();
	}

	/** Remove an icon from this PrivateSpace, tell the network that you initiated
	 * this deletion only if it is a PrivateSpace */
	public void removeIcon(Space space, PersonView icon) {
		// remove from private space object
		(space.getPeople()).remove(icon);
		// remove from privateSpaceView
		LinkedList<PersonView> icons = (space.getSpaceView()).getIcons();
		icons.remove(icon);
		LinearLayout screen = (LinearLayout) findViewById(R.id.space_view);
		screen.invalidate();
		// tell the network
		if(space instanceof PrivateSpace)
			sendRemoveUserFromPrivateSpace(((PrivateSpace)space).getMucName(), icon.getPerson());

	}
	
	/** Remove an icon from this PrivateSpace if someone else deleted a person from the PS
	 * you were a part of */
	public void forcedRemoveIcon(Space space, String userName) {
		PersonView removee = findUserIconByXMPPid(space, userName);
		if (removee != null){
			// remove from private space object
			space.getPeople().remove(removee);
			// remove from privateSpaceView
			LinkedList<PersonView> icons = (space.getSpaceView()).getIcons();
			icons.remove(removee);
			LinearLayout screen = (LinearLayout) findViewById(R.id.space_view);
			screen.invalidate();
		}
	}


	/**
	 * Search the list of users in a space and return the icon belongnig to the XMPPid given
	 * @param space
	 * 		space to search
	 * @param userName
	 * 		user's xmpp id that you want to find
	 * @return
	 * 		the person view of that user
	 * 		or null if they are not found
	 */
	public PersonView findUserIconByXMPPid(Space space, String userName) {
		PersonView removee = null;
		for (PersonView p : space.getPeople()){
			if (p.person.getXMPPid().equals(userName) ){
				removee = p;
				break;
			}
		}
		return removee;
	}

	public static void showPreview(LinkedList<PersonView> psv) {
		space.spaceView.setPreview(psv);

	}
	
	
	
	
	
	//TODO
	// Network team code: Instructions
	
	// Important notes....
	
	/* To access a person object through their xmppID number, you can use the static 
	 * Hashmap MainApplication.id_to_person */
	/* To access a xmppID number through a person object, call (personObject).getID() */
	
	/* All PrivateSpaces have int ID numbers (PrivateSpaceObject).getID(), however the main space (class Space)
	 * you start out with does not, which is why for many of the "send" methods I put in a Space object
	 * as a parameter instead of PrivateSpaceID
	 * You may decide to assign the mainspace a int ID as well, it is up to you.
	 */
	
	// Methods: each method stub has more details about methods available
	/*
	 * 1) receiveCreateNewPrivateSpace(int PrivateSpaceID)
	 * 2) sendCreateNewPrivateSpace(int PrivateSpaceID)
	 * 3) receiveDeleteNewPrivateSpace(int PrivateSpaceID)
	 * 4) sendDeleteNewPrivateSpace(int PrivateSpaceID)
	 * 5) receiveAddUserToPrivateSpace(int PrivateSpaceID, Person user)
	 * 6) sendAddUserToPrivateSpace(int PrivateSpaceID, Person user)
	 * 7) receiveRemoveUserFromPrivateSpace(int PrivateSpaceID, Person user)
	 * 8) sendRemoveUserFromPrivateSpace(int PrivateSpaceID, Person user)
	 * 9) updatePrivateSpace(int PrivateSpaceID)
	 * 10) updateMainSpace(int PrivateSpaceID)
	 */ 
	
	
	
	
	/** If someone else created a new private space and added you to it */
	public void receiveCreateNewPrivateSpace(String mucName){
		// TODO Call from a listener in the network code, this should be called after the xmpp client joins the chat
		// when they received an invite
		// Create a private space object on this client
		PrivateSpace p = new PrivateSpace(this);
		// set the muc name to what the private space's muc name is on the server
		p.setMucName(mucName);
		this.addExistingPrivateSpace(p);
		//TODO for network team
		/* Can use:
		 * 1) addExistingPrivateSpace() in class MainApplication to create a new private space & PSicon for this person
		 * 2) forcedAdd(Person p) in class Space to add a person's icon to the space
		 */
	}

	/**
	 * if you created a new PrivateSpace, then tell the network
	 */
	public void sendCreateNewPrivateSpace(String mucName) {
		
	}
	
	/**
	 *  If someone else has deleted the PrivateSpace that you were involved in
	 *  
	 *  Remove the private space Icon from the bottom bar
	 */
	public void receiveDeleteNewPrivateSpace(String mucName){
		// TODO call from network code through a listener
		PrivateSpaceView pv = PrivateSpaceView.getSpaceByID(mucName);
		this.forcedRemovePrivateSpace(pv);
	}
	
	/** If you want to delete a PrivateSpace you are involved in and need to 
	 * notify everyone in that PrivateSpace
	 */
	public void sendDeleteNewPrivateSpace(int PrivateSpaceID){
		// this method is called in removePrivateSpace() in class MainApplication
		// TODO network team
		// Use network code to exit private space via destroying the room
	}
	/** If someone else added a person to the privateSpace you are involved in */
	public void receiveAddUserToPrivateSpace(String  mucRoomName, Person user){
		// TODO: This should be called by a listener in the network code which will receive an invite
		PrivateSpaceView pview = PrivateSpaceView.getSpaceByID(mucRoomName);
		pview.getSpace().forcedAdd(user);
	}
	/** You added a new person to the privateSpace, tell the network that you initiated this */
	public void sendAddUserToPrivateSpace(Space space, Person user){
		space.inviteToMUC(user);
	}
	/** If someone else deleted a person from a privatespace you were involved in */
	public void receiveRemoveUserFromPrivateSpace(String mucName, String userName){
		//TODO call from a listener in network code which listens for users leaving a room
		PrivateSpaceView psv = PrivateSpaceView.getSpaceByID(mucName);
		forcedRemoveIcon(psv.getSpace(),userName);
	}
	/** If you deleted someone from a privateSpace, tell the network that you initited this,
	 * will not apply if you deleted someone from the mainspace */
	public void sendRemoveUserFromPrivateSpace(String mucName, Person user){
		//TODO network team
		/* this method called in class MainApplication method initializeButtons() in onTouch() for trashcan
		 * button in ACTION_UP
		 */
		PrivateSpaceView psv = PrivateSpaceView.getSpaceByID(mucName);
		Space spaceToRemoveFrom = psv.getSpace();
		try {
			spaceToRemoveFrom.removeUser(user.getXMPPid());
		} catch (XMPPException e) {
			Log.e(LOGTAG, "XMPPException error: " + 
					e.getXMPPError().getCode());
			Toast.makeText(this, "XMPPException error: " + e.getXMPPError().getCode(),
	                Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}		
	}
	
	/** If you moved an icon around your privatespace, notify the network so can
	 * adjust the sound locations */
	public void updatePrivateSpace(Space space){
		//TODO for network team
		// TODO Call sound spacialization function
		// this method called in class SpaceView method onTouchEvent() in ACTION_UP
		// need to convert it to the int PrivateSpaceID or SpaceID
	}

}
