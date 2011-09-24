import java.awt.event.MouseEvent;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class Person {
	PApplet parent;
	float x, y;
	int w, h;
	float halfW, halfH;
	int index;
	int oldX, oldY;
	PImage image;
	String name;
	Space space;
	boolean selectedForPrivateSpace = false;

	public Person(float x, float y, PApplet parent, int index, PImage image,
			String name, Space s) {
		this.x = x;
		this.y = y;
		this.w = image.width;
		this.h = image.height;
		this.halfW = (float) (w / 2.0);
		this.halfH = (float) (h / 2.0);
		this.index = index;

		this.image = image;

		this.name = name;
		this.space = s;
		this.parent = parent;
		parent.registerDraw(this);
		parent.registerMouseEvent(this);
	}

	// NORA: added this method
	public void setSpace(Space space) {
		this.space = space;
	}
	
	public PImage getImage(){
		return image;
	}
	
	public String getName(){
		return name;
	}

	public void draw() {
		if (!space.isShown)
			return;

		parent.rectMode(PConstants.CENTER);

		parent.imageMode(PConstants.CENTER);
		if (selectedForPrivateSpace) {
			// draw with thick boarder
			parent.strokeWeight(10);
		} else {
			parent.strokeWeight(1);
		}
		parent.stroke(MainWindow.colors[index]);
		parent.rect(x, y, w + 1, h + 1);
		parent.image(image, x, y);
		parent.fill(255);
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.textFont(MainWindow.font);
		parent.textAlign(PConstants.CENTER);
		parent.text(name, x, y + halfH + 12);

	}

	public boolean isMouseover(float mx, float my) {
		boolean mouseOver = mx > x - halfW && mx < x + halfW && my > y - halfH
				&& my < y + halfH;
		return mouseOver;
	}

	public void mouseEvent(MouseEvent event) {
		if (!space.isShown)
			return;

		if ((space.selected == null && isMouseover(parent.mouseX, parent.mouseY))
				|| space.selected == this) {
			if (space.isCreatingPrivateSpace()) {
				// Treat Mouse Event as a private space selection event
				switch (event.getID()) {
				case MouseEvent.MOUSE_PRESSED:
					// space.selected = this;
					if (!selectedForPrivateSpace) {
						selectedForPrivateSpace = true;
						space.addToPrivateSpace(this);
					} else {
						selectedForPrivateSpace = false;
						space.removeFromPrivateSpace(this);
					}
					break;

				case MouseEvent.MOUSE_DRAGGED:
					selectedForPrivateSpace = true;
					space.addToPrivateSpace(this);
					break;
				}
				;
			} else {
				// treat as drag/drop
				switch (event.getID()) {
				case MouseEvent.MOUSE_PRESSED:
					space.selected = this;
					oldX = parent.mouseX;
					oldY = parent.mouseY;
					break;

				case MouseEvent.MOUSE_DRAGGED:
					float dx = parent.mouseX - oldX;
					float dy = parent.mouseY - oldY;
					x += dx;
					y += dy;

					// check out of bounds
					if (x - halfW - 1 < 0)
						x = halfW + 1;
					if (x + halfW + 1 > space.w)
						x = space.w - halfW - 1;
					if (y - halfH - 1 < 0)
						y = halfH + 1;
					if (y + halfH + 1 > space.h)
						y = space.h - halfH - 1;

					oldX = parent.mouseX;
					oldY = parent.mouseY;
					break;

				case MouseEvent.MOUSE_RELEASED:
					space.selected = null; // is this a problem? what if I
											// release off the window? - Frye
					break;
				}
			}
		}
	}

	/**
	 * Print name and location of this person
	 */
	public String toString() {
		return this.name + " (" + x + "," + y + ")";
	}
}
