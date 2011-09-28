import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/** NORA: Re-did much of this class */

public class Peep {
	PApplet parent;
	int x, y;
	public static int w = 23, h = 23;
	PImage icon;
	String name;
	private boolean hidden;
	PrivateSpace ps; // NORA - added this

	// NORA - added ps and took away x - 3/6
	public Peep(PApplet parent, PImage image, String name, /*int x,*/ int y, PrivateSpace ps) {
		this.parent = parent;
		icon = image;
		this.y = y; 
		icon.width = w;
		icon.height = w;
		this.name = name;
		this.ps = ps; // NORA - added this - 3/6
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
		parent.rect(ps.x + ps.w/4, ps.y + ps.tabh +  ps.tabh*3/4 + y, w+1, h+1); // NORA - updated 3/6
		parent.imageMode(PConstants.CENTER);
		parent.image(icon, ps.x + ps.w/4, ps.y + ps.tabh + ps.tabh*3/4 + y); // NORA - updated 3/6
		parent.fill(255);
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.textFont(MainWindow.font);
		parent.textAlign(PConstants.LEFT);
		parent.text(name, ps.x + ps.w/4 + w, ps.y + 19*ps.tabh/10 + y); // NORA - updated 3/6
	}

//	peeps.add(new Peep(parent, people.get(i).getImage(), people.get(i)
//			.getName(), /*x + w / 3, (y + tabh )+ (h - tabh) / (people.size() + 1) // NORA - adjusted this
//			* (i + 1), */this));
	 
	public void hide() {
		hidden = true;
	}
}
