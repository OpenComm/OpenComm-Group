import java.awt.event.MouseEvent;
import processing.core.*;
import controlP5.*;

// PrivateViewScreen dims the entire background when a private space window shows up
// When users click the PrivateViewScreen, the private space window disappears
public class PrivateViewScreen extends Controller {
	PApplet parent;
	ControlP5 controlP5;
	Space privateSpaceWin;
	private boolean isOpen;		// isOpen toggles when users click outside of the private space window
	
	//Han-Wei:
	int screenWidth;
	int screenHeight;
	
	PrivateViewScreen(PApplet theParent, ControlP5 theControlP5, String theName, int theX, int theY, int theWidth, int theHeight, Space s) {
		super(theControlP5, (Tab)(theControlP5.getTab("default")), theName, theX, theY, theWidth, theHeight);
		parent = theParent;
		screenWidth = theWidth;
		screenHeight = theHeight;		
		controlP5 = new ControlP5(parent);
		privateSpaceWin = s;//new Space(parent, 200, 200, 0, 50, 2 * (theX - (theParent.width) / 2), -2 * theY, controlP5);
		controlP5.register(privateSpaceWin);
		parent.registerMouseEvent(this);
		isOpen = false;
	}
	
	public void draw(PApplet parent) {
		
		parent.fill(0, 100);
		parent.rect(position().x(), position().y(), screenWidth, screenHeight);
		
		if (isOpen) {
			privateSpaceWin.position().x += (100 - privateSpaceWin.position().x) * 0.2;
			privateSpaceWin.position().y += (100 - privateSpaceWin.position().y) * 0.2;
		}
		
		if (!isOpen) {
			privateSpaceWin.position().x += (2 * privateSpaceWin.x - privateSpaceWin.position().x) * 0.2;
			privateSpaceWin.position().y += (2 * privateSpaceWin.y - privateSpaceWin.position().y) * 0.2;
		}
	}
	
	public void mouseEvent(MouseEvent event) {
		if(getIsInside() && !privateSpaceWin.isInside()) {
//			switch (event.getID()) {
//				case MouseEvent.MOUSE_CLICKED:
//					isOpen = !isOpen;
//					break;
//			}		
		}
	}
	
	public boolean isOpen() {
		return isOpen;
	}

	// set the value of isOpen
	public void setIsOpen(boolean theIsOpen) {
		isOpen = theIsOpen;
	}	
	
	public void setValue(float theValue) {
	}
	
	public void addToXMLElement(ControlP5XMLElement theElement) {
	}
}
