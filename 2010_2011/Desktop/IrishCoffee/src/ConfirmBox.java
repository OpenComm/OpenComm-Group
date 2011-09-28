
import processing.core.PApplet;
import controlP5.Button;
import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Textlabel;

/**
 * Create a simple 2 button popup to confirm private space creation
 *
 */
public class ConfirmBox extends ControlP5 {
	
	private int x, dx = 35;
	private int y, dy = 25;
	private PApplet parent = null;
	private Space space = null;
	
	/**
	 * Constructor
	 * @param s - Space which is crating private space (i.e. main space)
	 * @param x - x location for this box
	 * @param y - y location for this box
	 */
	public ConfirmBox(Space s,int x, int y){
		super(s.parent);
		this.space = s;
		this.parent = s.parent;
		this.x = x; this.y = y;
		
		this.setMoveable(false);
		/**** Create the buttons and labels ***/
		Button b = this.addButton("Create",0,x + 40,y+20,dx,dy);
		//Set the button to call this.Accept() when create is clicked
		b.addListener(new ControlListener(){
			public void controlEvent(ControlEvent arg0) {
				Accept();
			}});
		//set button colors
		CColor bcolor = new CColor();
		bcolor.setForeground(parent.color(44,197,27));
		bcolor.setBackground(parent.color(44,130,40));
		b.setColor(bcolor);
		
		
		b = this.addButton("Cancel",0,x+dx+45 ,y+20,dx,dy);
		//Set the button to call this.Reject() when cancel is clicked
		b.addListener(new ControlListener(){
			public void controlEvent(ControlEvent arg0) {
				if (arg0.type() == ControlEvent.PRESSED)
					Reject();
			}});
		//set button colors
		bcolor.setForeground(parent.color(44,197,27));
		bcolor.setBackground(parent.color(44,130,40));
		b.setColor(bcolor);
		
		/**** Create the question Label ****/
		Textlabel la = this.addTextlabel("LabelName","Create Private Space?", x + 2*dx, y + 6);
		la.setControlFont(new ControlFont(MainWindow.font));
		
//		parent.registerMouseEvent(this);
	}

	/**
	 * Confirm the creation of a private space.
	 */
	public void Accept(){
		space.confirmPrivateSpace();
		this.hide();
	}
	
	/**
	 * Cancel creation of a private space
	 */
	public void Reject(){
		space.cancelPrivateSpace();
		this.hide();
	}
	
	
	public void draw(){
		this.parent.fill(parent.color(130,20,40));
		this.parent.stroke(parent.color(130,20,40));
		this.parent.rectMode(PApplet.LEFT);
		this.parent.rect(x, y, x + getWidth(), y + getHeight());
		super.draw();
	}
	
//	boolean selected = false;
//	public void mouseEvent(MouseEvent event) {
//			switch (event.getID()) {
//			case MouseEvent.MOUSE_PRESSED:
//				if (isInside(parent.mouseX,parent.mouseY))
//					this.selected = true;
//				break;
//			case MouseEvent.MOUSE_DRAGGED:
//				if (selected){
//					this.x = parent.mouseX;
//					this.y = parent.mouseY;
//				}
//				break;
//			case MouseEvent.MOUSE_RELEASED:
//				this.selected = false;
//				break;
//		}
//	}
	
	private boolean isInside(int xp, int yp) {
		return (xp > x && xp < x + getWidth() && yp > y && yp < y + getHeight());
	}

	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getWidth(){
		return dx*4 + 5;
	}
	
	public int getHeight(){
		return 47;
	}
}
