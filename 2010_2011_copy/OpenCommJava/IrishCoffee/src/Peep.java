import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/** NORA: Re-did much of this class */

public class Peep {
	PApplet parent;
	int x, y;
	int w = 23, h = 23;
	PImage icon;
	String name;
	private boolean hidden;

	public Peep(PApplet parent, PImage image, String name, int x, int y) {
		this.parent = parent;
		icon = image;
		this.x = x;
		this.y = y;
		icon.width = w;
		icon.height = w;
		this.name = name;
		parent.registerDraw(this);
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw() {
		if (hidden) return;
		parent.rectMode(PConstants.CENTER);
		parent.strokeWeight(1);
		parent.stroke(MainWindow.colors[0]);
		parent.rect(x, y, w + 1, h + 1);
		parent.imageMode(PConstants.CENTER);
		parent.image(icon, x, y);
		parent.fill(255);
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.textFont(MainWindow.font);
		parent.textAlign(PConstants.CENTER);
		parent.text(name, x + Tab1.w / 5, y + h/4);
	}

	public void hide() {
		hidden = true;
	}
}
