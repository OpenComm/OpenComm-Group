import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textfield;

public class MainWindow extends PApplet {

	public static PFont font;
	public static int[] colors = { 0xFF00CCFF, 0xFF00CC99, 0xFF00CC33,
			0xFF0066FF, 0xFF00D699 };
	public static int mainh = 245;
	public static int mainw = 480;

	public static int setupw = 480;
	public static int setuph = 400;

	// HanWei
	PrivateSpace[] pspaces = new PrivateSpace[2];
	ControlP5 controlP5;
	Tab1 chat;
	public static ArrayList<Person> people = new ArrayList<Person>();

	Space mainSpace;

	public void setup() {
		size(setupw, setuph);
		font = createFont("TAHOMA.TTF", (float) 11.5);

		controlP5 = new ControlP5(this);

		PApplet p = (PApplet) this;
		people.add(new Person(p, 1, "naj.png", "Najla E.")
				.setInitialXY(67, 108));
		people.add(new Person(p, 2, "nora.png", "Nora N.")
				.setInitialXY(133, 54));
		people.add(new Person(p, 3, "risa.png", "Risa N.").setInitialXY(200,
				210));
		people.add(new Person(p, 4, "mak.png", "Makoto B.").setInitialXY(267,
				162));
		// people.add(new Person(67, 108, p, 1, "naj.png","Najla E.", null));
		// people.add(new Person(133, 54, p, 2, "nora.png","Nora N.", null));
		// people.add(new Person(200, 210, p, 3,"risa.png", "Risa N.", null));
		// people.add(new Person(267, 162, p, 4,"mak.png","Makoto B.", null));

		// NORAS INIT: changed these methods to refer to static Person's already
		// created
		// in class Space
		ArrayList<Person> people1 = new ArrayList<Person>();
		people1.add(people.get(0));
		people1.add(people.get(1));
		ArrayList<Person> people2 = new ArrayList<Person>();
		people2.add(people.get(3));
		people2.add(people.get(2));
		people2.add(people.get(1));

		pspaces[0] = new PrivateSpace(this, people1, 0, controlP5);
		pspaces[1] = new PrivateSpace(this, people2, setupw / 4, controlP5);
		/* Tab1 */chat = new Chat(this, setupw / 2, controlP5);

		// Han-Wei: Feb 26: Correct this
//		chat = new Chat(this, setupw / 2, controlP5);       // NORA - comment this second chat out - 3/6

		// HAN WEI
		// controlP5 = new ControlP5(this);
		// create a new instance of the PrivateRoomBtn controller.
		// pspace1 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn1", 0,
		// mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		controlP5.register(pspaces[0]);
		// create a new instance of the PrivateRoomBtn controller.
		// pspace2 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn2",
		// width-(height-mainh)/2, mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		controlP5.register(pspaces[1]);
		controlP5.register(chat);

		mainSpace = new Space(this, mainh, mainw, 0xFF006699, 255, 0, 0,
				controlP5/* Han-Wei: Feb 26 Discard this: true, */);
		mainSpace.setPeople(people);
		mainSpace.setPeopleVisible(true);
		controlP5.register(mainSpace);
		smooth();
	}

	public void draw() {
		// background(255);
		// <REMOVE> when draw order changed
		boolean mainVis = true;
		for (PrivateSpace priv : pspaces) {
			if (priv.privateViewScreen.isOpen()) {
				mainVis = false;
			}
		}
		mainSpace.setPeopleVisible(mainVis);
		mainSpace.setVisable(mainVis);
		// </REMOVE>
		mainSpace.draw(this);
	}

	/**
	 * Look for a SHIFT key Press to begin drawing lasso, and selecting for
	 * private space
	 */
	public void keyPressed() {
		if (key == CODED) {// Coded keys are ALT, CTRL, SHIFT, UP, DOWN etc....
			if (keyCode == SHIFT && !mainSpace.isCreatingPrivateSpace()) {
				mainSpace.beginPrivateSpaceSelection();
				mainSpace.selected = null;
				System.out.println("Selecting For Private Space....");
			}
		}
	}

	/**
	 * Look for a SHIFT key release to ask for confirmation for creating private
	 * space
	 */
	public void keyReleased() {
		if (key == CODED) { // Coded keys are ALT, CTRL, SHIFT, UP, DOWN etc....
			if (keyCode == SHIFT) {
				mainSpace.createNewConfirmBox();
			}
		}
	}

	// NORA - added this, important for chat
	/**
	 * NEED COMMENT
	 */
	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.controller() instanceof Textfield) {
			Textfield myTextfield = (Textfield) theEvent.controller();
			String name = "Nora"; // NORA: Made a local variable for now, need
									// to change this
			// And find out who said what
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			Chat.content += "\n" + name + " (" + dateFormat.format(date) + ") "
					+ myTextfield.getText();
			Chat.myTextarea.setText(Chat.content);
		}
	}

	int spaceToFillIndex = 0;

	/**
	 * Create a private space using the people contained in the list people
	 * 
	 * @param people
	 */
	public void createPrivateSpace(ArrayList<Person> privPeople) {
		PrivateSpace pspace = pspaces[spaceToFillIndex];
		spaceToFillIndex = (++spaceToFillIndex % pspaces.length);
		pspace.clear();
		System.out.println("Creating Private Space with....");
		// TODO: The real work of creating private space goes here.....
		for (Person p : privPeople) {
			System.out.print(p + " | ");
			pspace.addPerson(p);
		}
		pspace.reroom();
		pspace.createPeeps();
		System.out.println();
	}
}
