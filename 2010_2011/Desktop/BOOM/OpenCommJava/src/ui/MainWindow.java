package ui;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import processing.core.PApplet;
import processing.core.PFont;
import rtp.*;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textfield;

/** Main method:: applet that runs the demo for BOOM */
public class MainWindow extends PApplet {

	public static PFont font;
	public static int[] colors = { 0xFF00CCFF, 0xFF00CC99, 0xFF00CC33,
			0xFF0066FF, 0xFF00D699 };
	public static int mainh = 245;
	public static int mainw = 480;

	public static int setupw = 480;
	public static int setuph = 400;

	static Space mainSpace;
	// HanWei
	PrivateSpace[] pspaces = new PrivateSpace[2];
	ControlP5 controlP5;
	Tab1 chat;
	public static ArrayList<Person> people; // people in main conference
	public static ArrayList<Person> people1; // people in private space 1
	public static ArrayList<Person> people2; // people in private space 2
	public static Space curr; // current space
	public static ArrayList<Person> currPeople; // people in the current space
	public static AudioConnect audioCnct; // all connections created by the particular user
	public static Person me; // who you (the computer that is running this gui) are

	/** set up the gui */
	public void setup() {
		size(setupw, setuph);
		font = createFont("../TAHOMA.TTF", (float) 11.5);

		controlP5 = new ControlP5(this);

		PApplet p = (PApplet) this;
		people = new ArrayList<Person>(); 
		// create people
		/** people.add(new Person(p, 1, "../naj.png", "Najla E.")
				.setInitialXY(67, 108));
		people.add(new Person(p, 2, "../nora.png", "Nora N.")
				.setInitialXY(133, 54));
		people.add(new Person(p, 3, "../risa.png", "Risa N.").setInitialXY(200,
				210));
		people.add(new Person(p, 4, "../mak.png", "Makoto B.").setInitialXY(267,
				162)); */
		// people.add(new Person(67, 108, p, 1, "naj.png","Najla E.", null));
		// people.add(new Person(133, 54, p, 2, "nora.png","Nora N.", null));
		// people.add(new Person(200, 210, p, 3,"risa.png", "Risa N.", null));
		// people.add(new Person(267, 162, p, 4,"mak.png","Makoto B.", null));
		// hardcoded for now
		// NORAS INIT: changed these methods to refer to static Person's already
		// created
		// in class Space
		
		// get all of the users and ip addresses through a config file
		try {
			Configuration cf = new Configuration("../persondata", p);
			people = cf.getConfig(); // set everyone in the main conference
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
		
		// find "me" using my IP address
		try {
			InetAddress myIA = InetAddress.getLocalHost();
			String myIP = myIA.getHostAddress();
			for (int i = 0; i < people.size(); i++) {
				if (people.get(i).ip.equals(myIP)) {
					me = people.get(i);
					// change my x and y coordinates to bottom center and make me immobile
					me.x = mainw / 2;
					me.y = mainh;
					System.out.println("I am " + me.getUserName());
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AudioConnect audioCnct = new AudioConnect(people);
		while (!audioCnct.allCreated()) {
			System.out.println("Waiting for all connections to initialize...");
		}
		audioCnct.start();
		people1 = new ArrayList<Person>();// an array for people in private space 1
		// people1.add(people.get(0));
		// people1.add(people.get(1));

		people2 = new ArrayList<Person>(); // an array for people in private space 2
		// people2.add(people.get(3));
		// people2.add(people.get(2));
		// people2.add(people.get(1));

		pspaces[0] = new PrivateSpace(this, people1, 0, controlP5, 1); // create private space 1
		// Han-Wei: Feb 26: Correct this

		chat = new Chat(this, setupw / 4, controlP5); // create chat
		pspaces[1] = new PrivateSpace(this, people2, setupw * 3 / 4, controlP5, 2); // create private space 2
		// /*Tab1*/ chat= new Chat(this, setupw/2, controlP5);

		// HAN WEI
		// controlP5 = new ControlP5(this);
		// create a new instance of the PrivateRoomBtn controller.
		// pspace1 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn1", 0,
		// mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		mainSpace = new Space(this, mainh, mainw, 0xFF006699, 255, 0, 0,
				controlP5, 0/* Han-Wei: Feb 26 Discard this: true, */);
		mainSpace.setPeople(people);
		mainSpace.setPeopleVisible(true);

		curr = mainSpace;
		controlP5.register(mainSpace);
		controlP5.register(pspaces[0]);
		// create a new instance of the PrivateRoomBtn controller.
		// pspace2 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn2",
		// width-(height-mainh)/2, mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		controlP5.register(pspaces[1]);
		controlP5.register(chat);
		smooth();
	}

	public void draw() {
		// background(255);
		// <REMOVE> when draw order changed
		boolean mainVis = true;
		for (PrivateSpace priv : pspaces) {
			if (priv.privateViewScreen.isOpen()) {
				priv.privateRoom.draw(this);
				mainVis = false;
			}
		}
		mainSpace.setPeopleVisible(mainVis);
		mainSpace.setVisable(mainVis);
		// </REMOVE>
		mainSpace.draw(this);
		// chat.draw(this);
		// for (PrivateSpace priv : pspaces){
		// priv.draw(this);
		// if (priv.privateViewScreen.isOpen()){
		// priv.draw(this);
		// }
		// }
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
			// TODO CHANGE NAME AND TEXT INFORMATION
			String name = me.getName(); // altered by Risa (3/9): for now, just whoever is running the GUI
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			Chat.content += "\n" + name + " (" + dateFormat.format(date) + ") "
					+ myTextfield.getText();
			Chat.myTextarea.setText(Chat.content);
		}
	}

	int spaceToFillIndex = 0;
	
	/** = everyone in private space #ps */
	public ArrayList<Person> getPrivateSpaceList(int ps) {
		if (ps == 0) {
			return people1;
		}
		else if (ps == 1){
			return people2;
		}
		else {
			System.out.println("Not a valid private space");
			return null;
		}
	} // end getPrivateSpaceList method
	
	/**
	 * Create a private space using the people contained in the list people
	 * 
	 * @param people
	 */
	public void createPrivateSpace(ArrayList<Person> privPeople) {
		PrivateSpace pspace = pspaces[spaceToFillIndex];
		spaceToFillIndex = (++spaceToFillIndex % pspaces.length);
		System.out.println(spaceToFillIndex);
		pspace.clear();
		if (spaceToFillIndex == 0) {
			people1.clear();
			for (Person p : privPeople) {
				people1.add(p);
				System.out.println("People added to private space 1: " + p.name + ", (" + p.x + ", " + p.y + ")");
			}
		} else {
			people2.clear();
			for (Person p : privPeople) {
				people2.add(p);
			System.out.println("People added to private space 2: " + p.name + ", (" + p.x + ", " + p.y + ")");
			}
		}
		System.out.println("Creating Private Space with....");
		// TODO: The real work of creating private space goes here.....
		for (Person p : privPeople) {
			System.out.print(p + " | ");
			pspace.addPerson(p);

		}
		pspace.reroom();
		pspace.createPeeps();
		System.out.println();
		System.out.println(people1.size() + " " + people2.size());
	}

}
