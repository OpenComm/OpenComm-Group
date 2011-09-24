import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class PrivateSpace extends Tab1 {
	ArrayList<Person> people;
	ControlP5 c;
	PApplet parent;
	private boolean isOpen;
	Space privateRoom;
	ArrayList<Peep> peeps; // NORA

	public PrivateSpace(PApplet parent, ArrayList<Person> group, int x,
			ControlP5 c) {
		super(parent, x, MainWindow.mainh, MainWindow.setupw / 4,
				MainWindow.setuph - MainWindow.mainh, c);
		people = group;
		this.parent = parent;
		this.c = new ControlP5(this.parent);

		privateRoom = new Space(parent, 200, 200, 255, 255,
				2 * (x - (parent.width) / 2), -2 * y, c, false);
		c.register(privateRoom);

		// parent.registerDraw(this);
		peeps = new ArrayList<Peep>();
		createPeeps(); // changed by NORA
		parent.registerMouseEvent(this);
	}

	/**
	 * 
	 * Get the people in the private space. Get each person's images and names,
	 * and assign them a position. Put this information in a Peep
	 * 
	 * -NORA
	 * 
	 */
	public void createPeeps() {
		for (int i = 0; i < people.size(); i++) {
			peeps.add(new Peep(parent, people.get(i).getImage(), people.get(i)
					.getName(), x + w / 3, (y + tabh )+ (h - tabh) / (people.size() + 1)
					* (i + 1)));
		}
	}

	/**
	 * 
	 * Draw: PrivateSpace, Title Tab, Up to 3 icons, Icons with names underneath
	 * Animate: Blossoming out of private rooms
	 * 
	 */
	public void draw(PApplet parent) {
		// Choose color of tab, and draw tab title
		if (number == 0)
			parent.fill(0xFFFF9900);
		else if (number == 1)
			parent.fill(0xFF800080);
		else
			parent.fill(0xFF00FF00);
		parent.stroke(MainWindow.colors[1]);
		parent.rect(x, y, w, tabh);

		// Draw tab body
		parent.fill(value);
		parent.rect(x, y + tabh, w, h - tabh);

		// Draw icons
		// parent.imageMode(PConstants.CENTER);
		// parent.image(people.get(0).image, 0f, 0f);
		/*
		 * for (int i = 0; i < people.size(); i++) {
		 * 
		 * parent.image(people.get(i).image, super.x + super.w / 2, (h - tabh) /
		 * (people.size() + 1) * (i + 1)); }
		 */

		// parent.rectMode(PConstants.CENTER);

		/*
		 * if (selectedForPrivateSpace){ //draw with thick boarder
		 * parent.strokeWeight(10); } else{ parent.strokeWeight(1); }
		 */
		// parent.stroke(MainWindow.colors[index]);
		// parent.rect(x, y, w + 1, h + 1);

		// animate the private room
		if (isOpen) {
			System.out.println("blossoming");
			privateRoom.position().x += (100 - privateRoom.position().x) * 0.2;
			privateRoom.position().y += (100 - privateRoom.position().y) * 0.2;
			privateRoom.draw(parent);
		}

		if (!isOpen) {
			privateRoom.position().x += (2 * privateRoom.x - privateRoom
					.position().x) * 0.2;
			privateRoom.position().y += (2 * privateRoom.y - privateRoom
					.position().y) * 0.2;
		}
	}

	public void mouseEvent(MouseEvent event) {
		if (getIsInside()) {
			switch (event.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				isOpen = !isOpen;
				privateRoom.isShown = !privateRoom.isShown;
				break;
			}
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	// set the value of isOpen
	public void setIsOpen(boolean theIsOpen) {
		isOpen = theIsOpen;
	}

}
