package edu.cornell.opencomm;

import java.util.HashMap;
import java.util.LinkedList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * @author noranq
 * 
 */
public class MainApplication extends Activity {

	// TODO: make mainspace static
	public static Space mainspace = null;
	static Space space; // the space that you are updating
	static LinkedList<Person> allPeople;
	public static final String PS_ID = "edu.cornell.opencomm.which_ps";
	public static HashMap<String,Person> id_to_person;

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
			space = new Space(this);
			mainspace = space;
			id_to_person = new HashMap<String,Person>();
			initializeMainSpace(space); // initialize people
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
		Person nora = new Person("Nora", "She's the best!", R.drawable.nora, "Nora's xmppID");
		Person najla = new Person("Najla", "Is dating Jack Sparrow",
				R.drawable.naj, "Najla's xmppID");
		Person makoto = new Person("Makoto", "Doesn't respond to texts",
				R.drawable.mak, "Makoto's xmppID");
		Person risa = new Person("Risa", "Is destined to marry her dog",
				R.drawable.risa, "Risa's xmppID");
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
		PrivateSpaceView pv = p.getPrivateSpaceView();
		pv.setLayoutParams(PSparams);
		pv.setMinimumWidth(50);
		bottomBar.addView(pv);
		bottomBar.invalidate();
		// tell network that you created a privatespace
		sendCreateNewPrivateSpace(p.getID());
	}
	
	/** Create a new private space, and make icon appear at bottom of screen.
	 * Use this method when another person has already created a private space 
	 * and is adding you to it
	 */
	public void addExistingPrivateSpace() {
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.privateSpaceLinearLayout);
		PrivateSpace p = new PrivateSpace(this);
		PrivateSpaceView pv = p.getPrivateSpaceView();
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
		// Tell network that you are deleting this private space
		sendDeleteNewPrivateSpace(pv.getId());
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
			sendRemoveUserFromPrivateSpace(((PrivateSpace)space).getID(), icon.getPerson());

	}
	
	/** Remove an icon from this PrivateSpace if someone else deleted a person from the PS
	 * you were a part of */
	public void forcedRemoveIcon(Space space, PersonView icon) {
		// remove from private space object
		(space.getPeople()).remove(icon);
		// remove from privateSpaceView
		LinkedList<PersonView> icons = (space.getSpaceView()).getIcons();
		icons.remove(icon);
		LinearLayout screen = (LinearLayout) findViewById(R.id.space_view);
		screen.invalidate();
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
	public void receiveCreateNewPrivateSpace(int PrivateSpaceID){
		//TODO for network team
		/* Can use:
		 * 1) addExistingPrivateSpace() in class MainApplication to create a new private space & PSicon for this person
		 * 2) forcedAdd(Person p) in class Space to add a person's icon to the space
		 */
	}

	/**
	 * if you created a new PrivateSpace, then tell the network
	 */
	public void sendCreateNewPrivateSpace(int PrivateSpaceID) {
		// This method is called in createNewPrivateSpace() in class MainApplication
		// TODO network team
	}
	
	/** If someone else has deleted the PrivateSpace that you were involved in */
	public void receiveDeleteNewPrivateSpace(int PrivateSpaceID){
		//TODO for network team
		/* Can use:
		 * forcedRemovePrivateSpace() in class MainApplication to delete a private space you are already in
		 */
		
	}
	
	/** If you want to delete a PrivateSpace you are involved in and need to 
	 * notify everyone in that PrivateSpace
	 */
	public void sendDeleteNewPrivateSpace(int PrivateSpaceID){
		// this method is called in removePrivateSpace() in class MainApplication
		// TODO network team
	}
	/** If someone else added a person to the privateSpace you are involved in */
	public void receiveAddUserToPrivateSpace(int PrivateSpaceID, Person user){
		// TODO
		/* Can use:
		 * forcedAdd(Person p) in class Space to add a person
		 */
	}
	/** You added a new person to the privateSpace, tell the network that you initiated this */
	public void sendAddUserToPrivateSpace(Space space, Person user){
		// this method is called in class Space method add(Person p)
		// need to convert it to the int PrivateSpaceID or Space ID
		// TODO network team
	}
	/** If someone else deleted a person from a privatespace you were involved in */
	public void receiveRemoveUserFromPrivateSpace(int PrivateSpaceID, Person user){
		//TODO network team
		// Can use:
		// forcedRemoveIcon() in class MainApplication
		
	}
	/** If you deleted someone from a privateSpace, tell the network that you initited this,
	 * will not apply if you deleted someone from the mainspace */
	public void sendRemoveUserFromPrivateSpace(int PrivateSpaceID, Person user){
		//TODO network team
		/* this method called in class MainApplication method initializeButtons() in onTouch() for trashcan
		 * button in ACTION_UP
		 */
	}
	
	/** If you moved an icon around your privatespace, notify the network so can
	 * adjust the sound locations */
	public void updatePrivateSpace(Space space){
		//TODO for network team
		// this method called in class SpaceView method onTouchEvent() in ACTION_UP
		// need to convert it to the int PrivateSpaceID or SpaceID
	}

}
