
import controlP5.Button;
import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Label;
import controlP5.Textlabel;

import processing.core.*;

/**
 * Create a simple 2 button popup to confirm private space creation
 *
 */
public class ConfirmBox extends ControlP5 {
	
	private int dx = 35;
	private int dy = 25;
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
		
		/**** Create the buttons and labels ***/
		Button b = this.addButton("Create",0,x,y+15,dx,dy);
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
		
		
		b = this.addButton("Cancel",0,x+dx+5 ,y+15,dx,dy);
		//Set the button to call this.Reject() when cancel is clicked
		b.addListener(new ControlListener(){
			public void controlEvent(ControlEvent arg0) {
				Reject();
			}});
		//set button colors
		bcolor.setForeground(parent.color(44,197,27));
		bcolor.setBackground(parent.color(44,130,40));
		b.setColor(bcolor);
		
		/**** Create the question Label ****/
		Textlabel la = this.addTextlabel("LabelName","Create Private Space?", x+40, y);
		la.setControlFont(new ControlFont(MainWindow.font));
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
}
