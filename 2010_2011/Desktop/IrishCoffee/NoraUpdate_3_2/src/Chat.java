import processing.core.PApplet;
import controlP5.*;

public class Chat extends Tab1 {
	ControlP5 controlP5;
	Textfield myTextfield;
	String textValue = "";
	Textarea myTextarea;

	public Chat(PApplet parent, int x, ControlP5 c) {
		super(parent, x, MainWindow.mainh, MainWindow.setupw / 2,
				MainWindow.setuph - MainWindow.mainh, c);
		// parent.registerDraw(this);
		dothis();
	}

	public void dothis() {
		ControlP5 controlP5 = new ControlP5(parent);
		myTextfield = controlP5.addTextfield("Type here", x,
				((MainWindow) parent).setuph - h / 5 - 15, w, h / 5);
		myTextfield.setFocus(true);
		myTextarea = controlP5.addTextarea("chat here", "I'm here!", x, y
				+ tabh, w, h - h / 5);
		controlP5.addButton("submit", 0, ((MainWindow) parent).setupw - 30,
				((MainWindow) parent).setuph - h / 5 - 14, 30, h / 5 - 1);
		// myTextarea.setColorForeground(0xffff0000);

	}

	public void draw(PApplet parent) {
		if (number == 0)
			parent.fill(0xFFFF9900);
		else if (number == 1)
			parent.fill(0xFF800080);
		else
			parent.fill(0xFF00FF00);

		parent.rect(x, y, w, tabh);
		parent.fill(value);
		parent.rect(x, y + tabh, w, h - tabh);
	}
}
