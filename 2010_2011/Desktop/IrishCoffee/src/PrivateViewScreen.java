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
	
	PrivateViewScreen(PApplet theParent, ControlP5 theControlP5, String theName, int theX, int theY, int theWidth, int theHeight, Space s) {
		super(theControlP5, (Tab)(theControlP5.getTab("default")), theName, theX, theY, theWidth, theHeight);
		parent = theParent;
		controlP5 = new ControlP5(parent);
		privateSpaceWin = s;//new Space(parent, 200, 200, 0, 50, 2 * (theX - (theParent.width) / 2), -2 * theY, controlP5);
//		this.width = MainWindow.mainw;
//		this.height = MainWindow.mainh;
//		controlP5.register(privateSpaceWin);
		parent.registerMouseEvent(this);
		isOpen = false;
	}
	
	public void draw(PApplet parent) {
		
		parent.fill(45, 10);
		parent.rectMode(PConstants.ARROW);
		parent.rect(position().x(), position().y(), width, height);
		
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
		if (isOpen){
			if(!checkOutOfBounds(event.getX(),event.getY())) {
				switch (event.getID()) {
					case MouseEvent.MOUSE_PRESSED:
						if (privateSpaceWin.selected == null)
							isOpen = !isOpen;
						break;
				}		
			}
		}
	}
	
	public boolean checkOutOfBounds(int x, int y) {
		return !(x > MainWindow.mainw || y > MainWindow.mainh);
//		int px = (int) privateSpaceWin.position().x;
//		int py = (int) privateSpaceWin.position().y;
//		
//		return (x  > privateSpaceWin.position().x && x < privateSpaceWin.position().x + privateSpaceWin.w
//				&& y > privateSpaceWin.position().y && y < privateSpaceWin.position().y + privateSpaceWin.h);
//		
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
