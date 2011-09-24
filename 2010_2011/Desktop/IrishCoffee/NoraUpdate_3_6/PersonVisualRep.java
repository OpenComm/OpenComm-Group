import java.awt.event.MouseEvent;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;


public class PersonVisualRep {
	Person who;
	float x, y;
	int w,h;
	int oldX, oldY;
	float halfW, halfH;
	Space where;
	boolean selectedForPrivateSpace = false;
	PApplet parent;
	final String name;
	private boolean visable = false;
	
	/**
	 * 
	 * @param toRep
	 * @param x
	 * @param y
	 * @param s
	 */
	public PersonVisualRep(Person toRep, float x, float y, Space s){
		
		who = toRep;
		PImage img = toRep.image;
		name = toRep.name;
		 
		this.w = img.width;
		this.h = img.height;
		this.halfW = (float) (w / 2.0);
		this.halfH = (float) (h / 2.0);
		
		where = s;
		this.x = x  +s.x;
		this.y = y + s.y;
		this.parent = toRep.parent;
		parent.registerMouseEvent(this);
	}
	
	public void setVisable(boolean tf){
		visable = tf;
	}
	
	public void draw() {
		if (visable){
			parent.rectMode(PConstants.CENTER);
			
			parent.imageMode(PConstants.CENTER);
			if (selectedForPrivateSpace){
				//draw with thick boarder
				parent.strokeWeight(10);
			}
			else{
				parent.strokeWeight(1);
			}

			parent.stroke(MainWindow.colors[getIndex()]);
			parent.rect(x, y, w + 1, h + 1);
			parent.image(getImage(), x, y);
			parent.fill(255);
			parent.stroke(0);
			parent.strokeWeight(1);
			parent.textFont(MainWindow.font);
			parent.textAlign(PConstants.CENTER);
			parent.text(name, x, y+ halfH + 12);
		}
		
	}

	public boolean isMouseover(float mx, float my) {
		boolean mouseOver = mx > x - halfW && mx < x + halfW && my > y - halfH
				&& my < y + halfH;
		return mouseOver;
	}

	public void mouseEvent(MouseEvent event) {
		if (visable && where.visable){
			if ((where.selected == null && isMouseover(parent.mouseX + where.x,
					parent.mouseY + where.y)) || where.selected == this.who) {
				if (where.isCreatingPrivateSpace()){
					//Treat Mouse Event as a private space selection event
					switch (event.getID()) {
					case MouseEvent.MOUSE_PRESSED:
						//space.selected = this;
						if (!selectedForPrivateSpace){
							selectedForPrivateSpace = true;
							where.addToPrivateSpace(this);
						}
						else{
							selectedForPrivateSpace = false;
							where.removeFromPrivateSpace(this);
						}
						break;

					case MouseEvent.MOUSE_DRAGGED:
						selectedForPrivateSpace = true;
						where.addToPrivateSpace(this);
						break;
					};
				}
				else{
					// treat as drag/drop
					switch (event.getID()) {
					case MouseEvent.MOUSE_PRESSED:
						where.selected = this.who;
						oldX = parent.mouseX;
						oldY = parent.mouseY;
						break;

					case MouseEvent.MOUSE_DRAGGED:
						float dx = parent.mouseX - oldX;
						float dy = parent.mouseY - oldY;
						x += dx;
						y += dy;

						//check out of bounds
						checkOutOfBounds();

						oldX = parent.mouseX;
						oldY = parent.mouseY;
						break;

					case MouseEvent.MOUSE_RELEASED:
						where.selected = null;
						break;
					}		
				}
			}
		}
	}
	
	public void checkOutOfBounds() {
        if (x < where.x + halfW + 1)
                x = where.x + halfW + 1;
        if (x + halfW + 1 >  where.w)
                x = where.w - halfW - 1;
        if (y < where.y + halfH)
                y = where.y + halfH + 1;
        if (y + halfH + 14 >  where.h)
                y = where.h - halfH - 14;
}

	
	public int getIndex(){
		return this.who.index;
	}
	
	public PImage getImage(){
		return who.image;
	}
	
	public PImage getImageCopy(){
		return who.getImage();
	}
}
