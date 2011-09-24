package ui;
import processing.core.PApplet;
import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import controlP5.Controller;
import controlP5.Tab;

public class Tab1 extends Controller {
	// Needs to keep track of tabs
	// Need size of tab
	// Color of top strip
	// Need to draw tab
	// parent PApplet
	
	public int w, h; // NORA - made this not static 3/6
	public int x, y;
	public int tabh=25;
	public PApplet parent;
	public static int counter=0;
	public int number;
	public int maxTabs; // maximum number of tabs

	int value= /*0xFFFFFFFF;//0xFF800080*/0xFF333399;

	public Tab1(PApplet parent, int x, int y, int w, int h, ControlP5 theControlP5) {
		super(theControlP5, (Tab) (theControlP5.getTab("default")),
				"Name"+System.nanoTime(), x, y, w, h);
		this.parent = parent;
		this.h = h;
		this.w = w;
		this.x = x;
		this.y = y;
		number=counter;
		counter++;
		//parent.registerDraw(this);
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