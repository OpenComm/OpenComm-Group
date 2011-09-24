import java.awt.event.MouseEvent;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Person {
	PApplet parent;
	float x = 0, y = 0;
//	int w, h;
//	float halfW, halfH;
	int index;
//	int oldX, oldY;
	PImage image;
	String name;
//	Space space;
//	boolean selectedForPrivateSpace = false;
	private String imageName;

	public Person(PApplet parent, int index, String imageName, String name) {
		
		this.parent = parent;
		this.name = name;
		this.index = index;
		
		this.imageName = imageName;
		image = parent.loadImage(imageName);
		
	}
	
	public Person setInitialXY(float x, float y){
		this.x = x; this.y = y;
		return this;
	}
	
	public PImage getImage(){
		return parent.loadImage(imageName);
	}
	
	public String getName(){
		return name;
	}

//	public void draw() {
//		
//		parent.rectMode(PConstants.CENTER);
//		
//		parent.imageMode(PConstants.CENTER);
//		if (selectedForPrivateSpace){
//			//draw with thick boarder
//			parent.strokeWeight(10);
//		}
//		else{
//			parent.strokeWeight(1);
//		}
//		parent.stroke(MainWindow.colors[index]);
//		parent.rect(x, y, w + 1, h + 1);
//		parent.image(image, x, y);
//		parent.fill(255);
//		parent.stroke(0);
//		parent.strokeWeight(1);
//		parent.textFont(MainWindow.font);
//		parent.textAlign(PConstants.CENTER);
//		parent.text(name, x, y + halfH + 12);
//		
//	}
//
//	public boolean isMouseover(float mx, float my) {
//		boolean mouseOver = mx > x - halfW && mx < x + halfW && my > y - halfH
//				&& my < y + halfH;
//		return mouseOver;
//	}
//
//	public void mouseEvent(MouseEvent event) {
//		
//		if ((space.selected == null && isMouseover(parent.mouseX,
//				parent.mouseY)) || space.selected == this) {
//			if (space.isCreatingPrivateSpace()){
//				//Treat Mouse Event as a private space selection event
//				switch (event.getID()) {
//				case MouseEvent.MOUSE_PRESSED:
//					//space.selected = this;
//					if (!selectedForPrivateSpace){
//						selectedForPrivateSpace = true;
//						space.addToPrivateSpace(this);
//					}
//					else{
//						selectedForPrivateSpace = false;
//						space.removeFromPrivateSpace(this);
//					}
//					break;
//
//				case MouseEvent.MOUSE_DRAGGED:
//					selectedForPrivateSpace = true;
//					space.addToPrivateSpace(this);
//					break;
//				};
//			}
//			else{
//				// treat as drag/drop
//				switch (event.getID()) {
//					case MouseEvent.MOUSE_PRESSED:
//						space.selected = this;
//						oldX = parent.mouseX;
//						oldY = parent.mouseY;
//						break;
//	
//					case MouseEvent.MOUSE_DRAGGED:
//						float dx = parent.mouseX - oldX;
//						float dy = parent.mouseY - oldY;
//						x += dx;
//						y += dy;
//	
//						//check out of bounds
//						if (x - halfW - 1 < 0)
//							x = halfW + 1;
//						if (x + halfW + 1 > space.w)
//							x = space.w - halfW - 1;
//						if (y - halfH - 1 < 0)
//							y = halfH + 1;
//						if (y + halfH + 1 > space.h)
//							y = space.h - halfH - 1;
//	
//						oldX = parent.mouseX;
//						oldY = parent.mouseY;
//						break;
//	
//					case MouseEvent.MOUSE_RELEASED:
//						space.selected = null; //is this a problem? what if I release off the window? - Frye
//						break;
//					}		
//			}
//		}
//	}
	
	/**
	 * Print name and location of this person
	 */
	public String toString(){
		return "PERSON: " + this.name;
	}
	
	@Override
	/**
	 * Person A = Person B if A.name = B.name
	 */
	public boolean equals(Object o){
		boolean equals = false;
		if (o instanceof Person){
			Person p = (Person)o;
			if (p.name.compareTo(this.name) == 0){
				equals = true;
			}
		}
		return equals;
	}
}
