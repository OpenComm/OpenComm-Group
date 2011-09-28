package ui;
import java.awt.event.MouseEvent;

import processing.core.PApplet;
import processing.core.PConstants;
import controlP5.*;

public class Chat extends Tab1 {
	ControlP5 controlP5;
	Textfield myTextfield;
	String textValue = "";
	public static Textarea myTextarea;
	boolean locked;
	float difx, dify;
	PApplet parent;

	public static String content = "";

	public Chat(/* Han-Wei: Feb 26: Correct this */PApplet theParent, int x,
			ControlP5 c) {
		super(/* Han-Wei: Feb 26: Correct this */theParent, x, MainWindow.mainh,
				MainWindow.setupw / 2, MainWindow.setuph - MainWindow.mainh, c);

		// Han-Wei: Feb 26: Correct this
		parent = theParent;
		dothis();
		// parent.registerMouseEvent(this);
	}

	public void dothis() {
		// Han-Wei: Feb 26: Correct this
		controlP5 = new ControlP5(parent); // did not create a new one
		myTextarea = controlP5.addTextarea("chat here", "", x, y + tabh,
				w - 10, h - tabh - h / 5); // NORA - adjusted this 3/6

		// myTextarea.setColorForeground(/*0xffff0000*//*0xFF006699*/0); // NORA
		// - adjusted this, not really sure what it does 3/7

		myTextfield = controlP5.addTextfield("Type here", x,
				y + h - h / 5, w, h / 5);// NORA - adjusted this
		myTextfield.setFocus(true);
		myTextfield.keepFocus(true);

	}

	public void draw(PApplet parent) {
		parent.rectMode(PConstants.ARROW);
	/*	if (number == 0)
			parent.fill(0xFFFF9900);
		else if (number == 1)
			parent.fill(0xFF800080);
		else
			parent.fill(0xFF00FF00); */
		parent.fill(3,143, 153, 200); //NORA - edited this 3/7
		
		// Han-Wei: Feb 26: Correct this
		parent.rect(position().x, position().y, w, tabh);
		parent.fill(28,168, 178, 180); // NORA - edited this
//		parent.fill(value);
		parent.rect(position().x, position().y + tabh, w, h - tabh);
	}

	// Han-Wei: Feb 26: Add this
	// public void mouseEvent(MouseEvent event) {
	// switch (event.getID()) {
	// case MouseEvent.MOUSE_PRESSED:
	// if(getIsInside()) {
	// locked = true;
	// parent.fill(255, 255, 255);
	// } else {
	// locked = false;
	// }
	// difx = parent.mouseX - position().x;
	// dify = parent.mouseY - position().y;
	// break;
	// case MouseEvent.MOUSE_DRAGGED:
	// if(locked) {
	// position().x = parent.mouseX - difx;
	// position().y = parent.mouseY - dify;
	// }
	// break;
	// case MouseEvent.MOUSE_RELEASED:
	// locked = false;
	// break;
	// }
	// }
}
