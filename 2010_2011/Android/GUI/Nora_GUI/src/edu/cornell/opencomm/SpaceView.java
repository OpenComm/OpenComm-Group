package edu.cornell.opencomm;

import java.util.LinkedList;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.util.Log;

/* A SpaceView is the graphical representation of a space, aka the screen you see on the monitor
 * showing all the icons of the people in the space. This view does not include the Main, Menu,
 * Trash buttons, or bottom PrivateSpace bar */
 
 public class SpaceView extends LinearLayout{
	private static String LOG_TAG="NORA_SpaceView";
	private Context context;
    Space space; // The space that this SpaceView (screen) is representing
    public static int screenWidth=800, screenHeight=600;
    
    // List of all the icons that should display on this SpaceView (screen)
    // Does not include the YOUR (the user's) icon (unless you want it to, then you have to discuss)
    public LinkedList<PersonView> allIcons= new LinkedList<PersonView>(); 
    
    // The Spaceview that the PrivateSpace icon (that was tapped) represents
    // for a preview, if none then null
    SpaceView open_preview; 
    
    /* Constructor: This one is used by the XML file to automatically generate
     * a SpaceView
     */
    public SpaceView(Context context, AttributeSet attrs){
    	super(context, attrs);
    	this.context = context;
    	setFocusable(true);
    	setFocusableInTouchMode(true);
    	Log.v(LOG_TAG, "Made SpaceView for XML file");
    }
    
    /* Constructor: Create a screen for a NEW privatespace that is empty (except for you) 
     * Initialize all variables */
     public SpaceView(Context context, Space parent_space){
    	super(context);
    	this.context = context;
        space = parent_space;
        open_preview = null;
        setFocusable(true);
        setFocusableInTouchMode(true);
        Log.v(LOG_TAG, "Made SpaceView for a self-created space");
     }
     
     /* Constructor: Create a screen for an already existing privatespace
     * Initialize all variables
     * Add all the the icons of the people for htis space view, call addManyPeople()*/
  /*   public SpaceView(Space parent_space, LinkedList<Person> people){
        space = parent_space;
        open_preview = null;
        addManyPeople(people);
     } */
     
     /* Add many people to this space, add all of their icons to the SpaceView */
     public void addManyPeople(LinkedList<Person> people){
        for(Person p: people){
            addPerson(p);
        }
     }
     
     /* Person added to the space, therefore add that Person's icon to this SpaceView */
     public void addPerson(Person person){
    	 Log.v(LOG_TAG, "Person " + person.getUsername() + "'s icon was added to " + space.getSpaceID() + "'s SpaceView with mainspace = " + space.isMainSpace());
        PersonView icon = person.getPersonView();
        allIcons.add(icon);    
        Log.v(LOG_TAG, "Space " + space.getSpaceID() + " 's SpaceView now has " + allIcons.size() + " people");
        invalidate();
     }
     
      /* Person deleted from the space, therefore delete that Person's icon from this SpaceView */
     public void deletePerson(Person person){
        PersonView icon = person.getPersonView();
        allIcons.remove(icon);
     }
     
     /* Get the main background color */
     private int getColor(){
    	 return R.color.main_background;
     }
     
     /* Draw everything on the screen including:
      * 1) Background color
      * 2) All icons (need loop, call the icon's draw function in class PersonView)
      * 3) Preview (if not null), need to get icons from the SpaceView and draw them
      */
     protected void onDraw(Canvas canvas){
     
         // TODO: copied this from old code, may have to fix
    	 // in process right now!
    	 Log.v(LOG_TAG, "In spaceview draw: allIcons has " + allIcons.size() + " people!");
    	 //(1)
    	 canvas.drawColor(getColor());
    	 
    	 //(2)
    	 for (PersonView p : allIcons)
    		 p.draw(canvas);
    	 //(3) TODO get to this step later!
    	 
     
     /*   if(canDraw>1)
		 canDraw = PrivateSpaceView.currentSpaces.size();
		if (canDraw == 2)
			// if(icons.size()>1)
			canvas.drawColor(getColor());

		if (currPreview != null) {
			PreviewView prev = new PreviewView(context);
			prev.draw(canvas, currPreview);
		}

		for (PersonView p : icons) {
			p.draw(canvas);
		}
       */
     }
     
     
     /* Handle Touch events... MANY cases to consider 
      * A PS icon = private space icon, squares that represent another private space
      * at the bottom bar of the gui 
      * Only adding people to privatespaces and */
     public boolean onTouchEvent(MotionEvent event){
     /* Need to be able to do all of this:
      * 1) Drag a person's icon around the screen (but only within screen
      * and not past the buttons), make sure to notify network to update spatialization
      * 2a) Highlight a person's icon by tapping once (later perhaps open miniprofile)
      * 2b) Tap again on highlighted icon to unhighlight
      * 3a) If icon dragged over PS button (but not yet released)
      * then highlight the PS button, and open up a highlighted preview
      * 3b) If icon released over PS button, then return icon to original position
      * and show the icon added to the preview for a brief second.
      * Need to notify network that added person to a privatespace
      * 4a) Tap once on PS icon to highlight and open up preview (with icons of everybody
      * in that private space
      * 4b) Tap a second time on highlighted PS icon to open up that private space's screen 
      * aka SpaceView
      * 5) Tapping on an empty part of the screen unhighlights any highlighted item
      */
      
      // Later...
      /* 6) Circling multiple icons should draw a circle around multiple icons
       * while drawing, and once released should highlight all encircled icons 
       * 7) Be able to drag multiple highlighted icons as a unit 
       */
       
       
       
       // TODO: copied this from old code- may have to fix
       
       /*
       
       int eventaction = event.getAction();
		int mouseX = (int) event.getX();
		int mouseY = (int) event.getY();

		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			selectedIcon = null;
			for (PersonView icon : icons) {
				if (icon.clickedInside(mouseX, mouseY)) {
					selectedIcon = icon;
					initialX = icon.getX();
					initialY = icon.getY();
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			// If a person icon is selected, then move the icon to the current
			// position
			if (selectedIcon != null) {
				selectedIcon.moved = true;
				selectedIcon.setX(mouseX - (selectedIcon.getW() / 2));
				selectedIcon.setY(mouseY - (selectedIcon.getH() / 2));

				// if icon is dragged over private space, then highlight that
				// private space icon
				if (hoveredPrivSpace == null) {
					for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
						if (p.contains(mouseX, mouseY)) {
							p.setHovered(true);
							hoveredPrivSpace = p;
						}
					}
				} else if (hoveredPrivSpace != null) {
					if (!hoveredPrivSpace.contains(mouseX, mouseY)) {
						hoveredPrivSpace.setHovered(false);
						hoveredPrivSpace = null;
					}
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			canDraw = PrivateSpaceView.currentSpaces.size();
			/*
			 * If you highlited an icon, then clicked on nothing on screen, it
			 * should unhighlite all the other icons
			 */
		/*	if (selectedIcon == null && mouseY < mainScreenH) {

				for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
					if (p.isSelected)
						p.setSelected(false);

				}
			}
			if (selectedIcon != null) {
				// if you did not move the icon and just clicked it, then
				// highlite it
				if (!selectedIcon.moved) {
					selectedIcon.changeSelected();
				}
				// if you did move the icon, then notify the network so it can update sound spatializatio
				else{
					((MainApplication)context).updatePrivateSpace(parent);
				}
				selectedIcon.moved = false;

				for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
					if (p.contains(mouseX, mouseY)) {

						(p.getSpace()).add(selectedIcon.getPerson());
						p.setSelected(false);
						// canDraw++;
					}
				}
				if (mouseY >= mainScreenH) {
					selectedIcon.setX(initialX);
					selectedIcon.setY(initialY);
				}
				selectedIcon = null;
			}
			break;
		}
		invalidate();*/
		return true;
       
       
       
     }
     
 }