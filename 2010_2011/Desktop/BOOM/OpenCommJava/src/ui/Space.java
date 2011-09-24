package ui;
import java.util.ArrayList;

import java.util.LinkedList;

import processing.core.*;
import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import controlP5.Controller;
import controlP5.Tab;

public class Space extends Controller {
	public ArrayList<PersonVisualRep> spacePeople = new ArrayList<PersonVisualRep>();
	public Person selected;
	public int h, w, x, y;
	PApplet parent;
	public int color, trans;
	
	private LinkedList<Person> privateSpaceCreationList;
	private boolean creatingPrivateSpace = false;
	public ConfirmBox confirmingPrivateSpace = null;
	PGraphics selectionLine;
	boolean visable = true;
	public boolean privateS = false;
	public int num;

	
	public Space(PApplet parent, int h, int w, int color, int trans, int x, int y, ControlP5 theControlP5, int num) {
		super(theControlP5, (Tab) (theControlP5.getTab("default")), "name"+System.currentTimeMillis(),	x, y, w, h);
		
		this.parent = parent;
		this.h = h;
		this.w = w;
		this.x = x;
		this.y = y;
		this.color = color;
		this.trans = trans;
		this.num = num;
	}

	public void draw(PApplet parent) {
		
		if (this.visable){
		
			parent.pushMatrix();
			parent.translate(position().x(), position().y());
			parent.rectMode(PConstants.LEFT);
			parent.imageMode(PConstants.CENTER);
//			parent.fill(color, trans);
			parent.fill(70,170,200,100); // NORA 3/7
			parent.rect(x, y, w, h);
			
			if (this.isCreatingPrivateSpace()) {
				// draw lasso if selecting people for private space
				if (parent.mousePressed) {
					selectionLine.stroke(255);
					selectionLine.strokeWeight(4);
					selectionLine.beginDraw();
					selectionLine.line(parent.mouseX, parent.mouseY, parent.pmouseX, parent.pmouseY);
					selectionLine.endDraw();
				}
				parent.image(selectionLine, this.w/2, h/2);
			}
			
			for (PersonVisualRep p : this.spacePeople){
				p.draw();
			}
			
			if (confirmingPrivateSpace != null){
				confirmingPrivateSpace.draw();
			}
			parent.popMatrix();
		}
	}
	
	public void setPrivate(boolean p) {
		privateS = p;
	}
	
	/**
	 * Clear list of people selected for private space, Clear lasso image, and
	 * set to "creating private space" mode
	 */
	public void beginPrivateSpaceSelection() {
		privateSpaceCreationList = new LinkedList<Person>();
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
	public void addToPrivateSpace(PersonVisualRep person) {
		// if the user itself is not in the private space, he/she is added
		if (!privateSpaceCreationList.contains(MainWindow.me)) {
			System.out.println("Space.java :: don't forget myself!");
			privateSpaceCreationList.add(MainWindow.me);
		}
		// add someone who is not myself to the private space and permit sending of audio
		if (person.who != MainWindow.me && !privateSpaceCreationList.contains(person.who)) {
			privateSpaceCreationList.add(person.who);
			// allow the sender with this person.who 
			MainWindow.audioCnct.getAllSenders().get(person.name).canSend(this.num);
		}
	}

	/**
	 * Remove all from list of people pending to be placed in private space
	 */
	public void clearPrivateSpaceSelections() {
		for (PersonVisualRep p : spacePeople) {
			p.selectedForPrivateSpace = false;
			MainWindow.audioCnct.getAllSenders().get(p.name).noSend(this.num);
		}
	}

	/**
	 * remove person from list of people pending placement in private space
	 * 
	 * @param person
	 */
	public void removeFromPrivateSpace(PersonVisualRep person) {
		privateSpaceCreationList.remove(person);
		if (person.who != MainWindow.me) {
			MainWindow.audioCnct.getAllSenders().get(person.name).noSend(this.num);
		}
	}

	/**
	 * If the box asking to confirm the creation of a new space is not showing,
	 * create and show it
	 */
	public void createNewConfirmBox() {
		if (this.confirmingPrivateSpace == null) {
			if (this.privateSpaceCreationList.size() >= 1){
				confirmingPrivateSpace = new ConfirmBox(this, this.w / 2 - 74, this.h/2 - 25);
				confirmingPrivateSpace.show();
			}
			else{
				cancelPrivateSpace();
			}
		}
		
	}

	/**
	 * User confirmed private space: so do the work
	 */
	public void confirmPrivateSpace() {
		if (confirmingPrivateSpace != null) {
			confirmingPrivateSpace = null;
		}
		if (parent instanceof MainWindow)
			((MainWindow)parent).createPrivateSpace(new ArrayList<Person>(privateSpaceCreationList));
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

	public void setPeople(ArrayList<Person> people) {
		this.spacePeople = new ArrayList<PersonVisualRep>(); //clear images
		for (Person p : people){
			PersonVisualRep vr = new PersonVisualRep(p, p.x, p.y, this);
			System.out.println(vr.name + ", (" + vr.x + ", " + vr.y + ")");
			vr.checkOutOfBounds();
			this.spacePeople.add(vr);
		}
	}

	public void setPeopleVisible(boolean b) {
		for (PersonVisualRep v : this.spacePeople){
			v.setVisable(b);
		}
	}

	public void setVisable(boolean b) {
		this.visable = b;
		if (b == false)
			this.selected = null;
	}
	
	
}

// text outline
// color scheme
// initial configuration of people
// images underneath other images
// take whole name