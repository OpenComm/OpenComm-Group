/** The MainApplication handles and manages the PrivateSpaces for every
 * User involved. Receives its notifications from the GUI, and then
 * updates the data of the private space, and talks with the network. */

package edu.cornell.opencomm.view;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.controller.FontSetter;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

public final class ConferenceView extends Activity {


	/**
	 * The SpaceView object (UI) representing the space that the user is
	 * currently talking to
	 */
	public static SpaceView spaceView;
	
	public static ChatView chatView;
	
	public static ConferenceController conferenceController;
	// Controllers
	//TODO : Create a delegate for the Conference controller to handle this
//	SideChatIconMenuController sideChatIconMenuController;





	//TODO: 1. Create Conferenece model having 
	//1. Main chat data
	//2/ Empty side chat data
	//3. side chat data

	

	/**
	 * Called when application is first created. <b>Set up:</b>
	 * <ol>
	 * <li>Create and open the main space and put the primary user (the user
	 * that started this application) into the space</li>
	 * <li>Initialize button functionality: main button</li>
	 * </ol>
	 * <b>Assumptions:</b>
	 * <ul>
	 * <li>The main space is created by the primary user</li>
	 * <li>XMPPConnection is established and authorized before this Activity is
	 * called
	 * </ul>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO : reuse the same context
		//		ContextTracker.setContext(this);

		// Open up the layout specified by the main XML
		setContentView(R.layout.conference_layout);
		
//		spaceView = (SpaceView) findViewById(R.id.space_view);
		// Check if the mainspace was already created
		if (Space.getMainSpace() == null) {
			// Obtain username used to log into the application
			Intent start_intent = getIntent();
			//TODO :Get the correct string extrea from start conference view
			// username = start_intent.getStringExtra(Network.KEY_USERNAME);
			// Create instance of primary user
			//TODO : ConferenceController needs to delegate this to 
			// SpaceDelegate
//			try {
//				// create the mainspace
//				SpaceController.createMainSpace(this);
//
//				// create an empty private space
//				// SpaceController.addSpace(this);
//
//				// TODO add private space preview
//			} catch (XMPPException e) {
//				// Log.e(TAG, "onCreate - Error (" + e.getXMPPError().getCode()
//				// + ") " + e.getXMPPError().getMessage());
//				e.printStackTrace();
//			}
//			spaceView.setSpace(Space.getMainSpace());
//			Space.getMainSpace().setScreenOn(true);
			//TODO: register for listners
			
			//TODO: Set the font for the main conference view and its child space view and chat view
			
		}
		
		initializeButtons();

//		spaceView.setOnKeyListener(onKeyListener);


	}
	private void applyFont(){
		//TODO: Make sure font setting cascades to chatView and spaceView
		FontSetter.applySanSerifFont(ConferenceView.this, findViewById(R.layout.conference_layout));
	}
	/**
	 * An onKeyListner to listen to any key events. Will be used mainly for
	 * debugging purposes.
	 */
	public View.OnKeyListener onKeyListener = new View.OnKeyListener() {
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Delegate everything to conference controller
			return false;
		}

		
	};


	public void initializeButtons() {
		//TODO initialize buttons for side chat
	}


	public void disconnect() {
	//TODO : Tell the controller to disconnect the connection to server
	//Clean up the view and launch login/ dashboard view(See Specs)
	}

	
	/**
	 * Remove an existing PrivateSpace for yourself. Make sure to also delete
	 * the PrivateSpace's corresponding PrivateSpaceIconView and SpaceView. This
	 * method also called (by network) if someone else deleted a PrivateSpace
	 * that you were a part of, or if you decided to leave but are not moderator
	 * of the space
	 */
	public void deletePrivateSpace(Space spaceToDelete) {
		
	}

	/**
	 * Change the space whose contents the screen (spaceview) is showing. Need
	 * to notify network of this change so that it can adjust sound
	 */
	/*
	 * public void changeSpace(Space s){ SpaceView spaceView =
	 * (SpaceView)findViewById(R.id.space_view);
	 * screen.getSpace().setScreenOn(false); spaceView.changeSpace(s);
	 * s.setScreenOn(true);
	 *
	 * /* TODO network: 1) Adjust sound in network (if you want the space
	 * onscreen to be louder than other for example)
	 */
	// } */

	/**
	 * Need to add the new PrivateSpace button to the bottom GUI by altering the
	 * XML code
	 */
	public void addPrivateSpaceButton() {
		//TODO Delegate this to chat space view/chat view

	}

	/**
	 * Removes all UI traces of a privatespace/sidechat 1) Removes space from
	 * hashmap of all spaces 2) Removes the corresponding privatespaceButton 3)
	 * Changes the space to the Mainspace (but only if wasn't a mainspace)
	 * However, if you are leaving the mainspace, then start a new activity and
	 * go ack to the dashboard
	 */
	public void delPrivateSpaceUI() {
		//TODO: delete the private space
	
	}

	/**
	 * Need to delete the this PrivateSpace button to the bottom GUI by altering
	 * the XML code
	 */
	public void delPrivateSpaceButton() {
		//Delegate this space/chat view to delete the button
	}

	/**
	 * Remove this person from this space, take away that person's icon from the
	 * SpaceView as well. Could be situations where: you do not want this person
	 * in your mainspace, this person removed him/herself from the privatespace,
	 * the privatespace got deleted, you kicked someone out of the group (if you
	 * are moderator)
	 *
	 * @throws XMPPException
	 */
	public void deletePerson(Space space, User person) {
		//TODO Delegate this to control to delete the user and update the Conference model
	}

	/**
	 * Reupdates the spaceview UI. Does the same thing as invalidate(). Purpose
	 * of this method is so that any thread that is not from the UI or the
	 * activity may call it (to avoid the "Only the original thread that created
	 * a view hierarchy can touch its views" error)
	 */
	public void invalidateSpaceView() {
		runOnUiThread(new Runnable() {
			public void run() {
				spaceView.invalidate();
			}
		});
	}

	/**
	 * Same reasons as invalidateSpaceView() except to invalidate a
	 * privatespaceicon when someone is add/removed from the space
	 *
	 * @param psv
	 *            - the PrivateSpaceIconView whose GUI needs to be updated
	 */
	public void invalidatePSIconView() {
		//TODO delegate this to space view
	}

	
}
