package ui;
import java.awt.event.MouseEvent;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import ss.AudioSS;


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
		if (this.who != MainWindow.me) {
			if (MainWindow.audioCnct != null) {
				MainWindow.audioCnct.getAllReceivers().get(this.name).getAudioSS().moveTo(s.num, (int)x, (int)y);
			}
		}
		else {
			this.x = s.w / 2;
			this.y = s.h;
		}
	}
	
	public Person getWho() {
		return this.who;
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
			if (MainWindow.me == who) {
				parent.text("I AM: " + name, x, y+ halfH + 12);
			}
			else {
				parent.text(name, x, y + halfH + 12);
			}
		}
		
	}

	public boolean isMouseover(float mx, float my) {
		boolean mouseOver = mx > x - halfW && mx < x + halfW && my > y - halfH
				&& my < y + halfH;
		return mouseOver;
	}

	public void mouseEvent(MouseEvent event) {
		if (where.privateS)
			return;
		
		if (visable && where.visable){
			if ((where.selected == null && isMouseover(parent.mouseX + where.x,
					parent.mouseY + where.y)) || where.selected == this.who) {
				// if you are creating a private space
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
						// don't move me
						if (this.who != MainWindow.me) {
							if (!where.equals(MainWindow.mainSpace))
							{
								System.out.println("not in mainWindow");
							}						
							oldX = parent.mouseX;
							oldY = parent.mouseY;
						}
						break;
					case MouseEvent.MOUSE_DRAGGED:
						where.selected = this.who;
						if (this.who != MainWindow.me) {
							float dx = parent.mouseX - oldX;
							float dy = parent.mouseY - oldY;
							x += dx;
							y += dy;
	
							//check out of bounds
							checkOutOfBounds();
	
							oldX = parent.mouseX;
							oldY = parent.mouseY;
						}
						break;
						

					case MouseEvent.MOUSE_RELEASED:
						where.selected = null;
						// TODO: RISA
						if (MainWindow.people == null) {
							System.out.println("PersonVisualRep :: this is null");
						}
						// if the user is valid and is not the one running the GUI (not me)
						if (MainWindow.people.contains(this) && this.who != MainWindow.me){
							// obtains the audioReceiver whose sender is the user that was
							// moved, gets the AudioSS, and moves its location to the new coordinates
							MainWindow.audioCnct.getAllReceivers().get(this.name).getAudioSS().moveTo(this.where.num, (int)x, (int)y);
						}
						else {
							//update coordinates to ???
						}
						break;
					}		
				}
			}
		}
		if (event.getID() == MouseEvent.MOUSE_RELEASED)
			where.selected = null;
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
