package opencomm.android2;

import java.util.LinkedList;
import java.util.ListIterator;

import opencomm.android2.Button.ButtonType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class ScreenView extends View {
	private Context context;
	private PrivateSpace space; // the group of people this is displaying
	private LinkedList<PersonView> icons;
	int x =0, y=0, w=320, h = 430;
	private PersonView selectedIcon;
	
	public ScreenView(Context context, PrivateSpace space){
		super(context);
		this.context = context;
		this.space = space;
		setFocusable(true);
		setFocusableInTouchMode(true);	
		
		createIcons(space);
	}
	
	/** Takes the people who are in this private space, and creates an icon (PersonView) for them 
	 * for only this screen */
	public void createIcons(PrivateSpace space){
		icons = new LinkedList<PersonView>();
		LinkedList<Person> people = space.getAllPeople();
		
		ListIterator<Person> i= people.listIterator();
		while(i.hasNext()){
			Person p = i.next();
			icons.add(new PersonView(context, p, (int)(Math.floor(w*Math.random())), 
					(int)(Math.floor(h*Math.random())), 50, 50));
			i=people.listIterator(i.nextIndex());
		}
		
	}
	
	protected void onDraw(Canvas canvas){
		// draw background
		canvas.drawColor(0xFFCCCCCC); 
		// draw people icons
		ListIterator<PersonView> i= icons.listIterator();
		while(i.hasNext()){
			PersonView icon = i.next();
			canvas.drawBitmap(icon.getIcon(), icon.getX(), icon.getY(), null);
		}
	} 
	
	public boolean onTouchEvent(MotionEvent event){
		int eventaction = event.getAction();
		int mouseX = (int)event.getX();
		int mouseY = (int)event.getY();
		
		switch(eventaction){
			case MotionEvent.ACTION_DOWN:
				selectedIcon=null;
				ListIterator<PersonView> i= icons.listIterator();
				while(i.hasNext() == true){
					PersonView icon = i.next();
					if(icon.clickedInside(mouseX, mouseY))
						selectedIcon = icon;
					i= icons.listIterator(i.nextIndex());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(selectedIcon!=null){
					selectedIcon.setX(mouseX-(selectedIcon.getW()/2));
					selectedIcon.setY(mouseY-(selectedIcon.getH()/2));
				}
				break;
			case MotionEvent.ACTION_UP:
				selectedIcon = null;
				break;
		}  
		invalidate();
		return true;
	}

}
