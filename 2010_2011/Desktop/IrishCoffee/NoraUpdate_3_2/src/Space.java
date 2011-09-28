import java.util.ArrayList;

import java.util.LinkedList;

import processing.core.*;
import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import controlP5.Controller;
import controlP5.Tab;

public class Space extends Controller {
	// NORA: might not need this for now since I created an ArrayList like this
	// in class MainWindow
	// public static ArrayList<Person> people = new ArrayList<Person>();

	public static Person selected;
	public int h, w, x, y;
	PApplet parent;
	public int color, trans;

	private LinkedList<Person> privateSpace;
	private boolean creatingPrivateSpace = false;
	public ConfirmBox confirmingPrivateSpace = null;

	public boolean isShown;

	PGraphics selectionLine;

	public Space(PApplet parent, int h, int w, int color, int trans, int x,
			int y, ControlP5 theControlP5, boolean show) {
		super(theControlP5, (Tab) (theControlP5.getTab("default")), "name", x,
				y, w, h);

		this.parent = parent;
		this.h = h;
		this.w = w;
		this.x = x;
		this.y = y;
		this.color = color;
		this.trans = trans;
		isShown = show;
		// parent.registerDraw(this);

		/*
		 * people.add(new Person(67, 108, parent, 1,
		 * parent.loadImage("naj.png"), "Najla E.", this)); people.add(new
		 * Person(133, 54, parent, 2, parent.loadImage("nora.png"), "Nora N.",
		 * this)); people.add(new Person(200, 210, parent, 3,
		 * parent.loadImage("risa.png"), "Risa N.", this)); people.add(new
		 * Person(267, 162, parent, 4, parent.loadImage("mak.png"), "Makoto B.",
		 * this));
		 */

		// NORA: set all the Person's space parameter to this object
		for (int i = 0; i < MainWindow.people.size(); i++) {
			MainWindow.people.get(i).setSpace(this);
		}
	}

	public void draw(PApplet parent) {
		if (!isShown)
			return;

		parent.rectMode(PConstants.LEFT);
		parent.fill(color, trans);
		parent.rect(x, y, w, h);

		if (this.isCreatingPrivateSpace()) {
			// draw lasso if selecting people for private space
			if (parent.mousePressed) {
				selectionLine.stroke(255);
				selectionLine.strokeWeight(6);
				selectionLine.beginDraw();
				selectionLine.line(parent.mouseX, parent.mouseY,
						parent.pmouseX, parent.pmouseY);
				selectionLine.endDraw();
			}
			parent.image(selectionLine, w / 2, h / 2);
		}
	}

	/**
	 * Clear list of people selected for private space, Clear lasso image, and
	 * set to "creating private space" mode
	 */
	public void beginPrivateSpaceSelection() {
		privateSpace = new LinkedList<Person>();
		selectionLine = parent.createGraphics(w, h, PApplet.P2D);
		creatingPrivateSpace = true;
	}

	/**
	 * True if in "creating private space" mode
	 * 
	 * @return
	 */
	public boolean isCreatingPrivateSpace() {
		return creatingPrivateSpace;
	}

	/**
	 * Add the person to list of people to put in private space upon completion
	 * 
	 * @param person
	 */
	public void addToPrivateSpace(Person person) {
		if (!privateSpace.contains(person)) {
			privateSpace.add(person);
		}
	}

	/**
	 * Remove all from list of people pending to be placed in private space
	 */
	public void clearPrivateSpaceSelections() {
		// NORA: adjusted from just people to MainWindow people since I took out
		// that parameter
		for (Person p : MainWindow.people) {
			p.selectedForPrivateSpace = false;
		}
	}

	/**
	 * remove person from list of people pending placement in private space
	 * 
	 * @param person
	 */
	public void removeFromPrivateSpace(Person person) {
		privateSpace.remove(person);
	}

	/**
	 * If the box asking to confirm the creation of a new space is not showing,
	 * create and show it
	 */
	public void createNewConfirmBox() {
		if (this.confirmingPrivateSpace == null) {
			confirmingPrivateSpace = new ConfirmBox(this, this.w / 2 - 45,
					this.h - 20);
			// confirmingPrivateSpace = new
			// ConfirmBox(this,parent.mouseX,parent.mouseY);
		}
		confirmingPrivateSpace.show();
	}

	/**
	 * User confirmed private space: so do the work
	 */
	public void confirmPrivateSpace() {
		if (confirmingPrivateSpace != null) {
			confirmingPrivateSpace = null;
		}
		if (parent instanceof MainWindow)
			((MainWindow) (this.parent)).createPrivateSpace(privateSpace);
		clearPrivateSpaceSelections();
		creatingPrivateSpace = false;
	}

	/**
	 * Terminate creation of private space.
	 */
	public void cancelPrivateSpace() {
		if (confirmingPrivateSpace != null) {
			confirmingPrivateSpace = null;
		}
		clearPrivateSpaceSelections();
		creatingPrivateSpace = false;
	}

	@Override
	public void addToXMLElement(ControlP5XMLElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValue(float arg0) {
		// TODO Auto-generated method stub

	}

}

// text outline
// color scheme
// initial configuration of people
// images underneath other images
// take whole name