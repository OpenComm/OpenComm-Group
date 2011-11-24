package edu.cornell.opencomm.view;

import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.R.color;
import edu.cornell.opencomm.controller.ContactListController;
import edu.cornell.opencomm.controller.EmptySpaceMenuController;
import edu.cornell.opencomm.controller.UserIconMenuController;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.SpaceViewController;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.model.Space;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.LinearLayout;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/* A SpaceView is the graphical representation of a space, aka the screen you see on the monitor
 * showing all the icons of the people in the space (above the privatespace bar). 
 * This view does not include the Main, Menu, Trash buttons, or bottom PrivateSpace bar.
 * Your icon will not show up on screen */
 
 public class SpaceView extends LinearLayout{
	private static String LOG_TAG="OC_SpaceView"; // for error checking
	private Context context;
	public static int screenWidth=315, screenHeight=365; // the size of the spaceview (the area above the privatespace bar)
    
	Space space; // The space that this SpaceView (screen) is currently representing
    public LinkedList<UserView> allIcons= new LinkedList<UserView>();// List of all the icons that should display on this SpaceView (screen)
    PrivateSpaceIconView open_preview=null; // The PrivateSpaceIconView (PS icon at bottom bar) whose preview is open
    Bitmap voice_image;
    
    /** Temporary variables used in the onTouchEventMethods */
    public UserView selectedIcon; 
    public int initialX, initialY;
    PrivateSpaceIconView hoveredPrivSpace= null;
    UserView initialIcon;
    private boolean dim=false;
    
    
    // Controllers
    SpaceViewController spaceViewController = new SpaceViewController(this);
    //ContactListController contactListController;
    EmptySpaceMenuController empytSpaceMenuController;
    UserIconMenuController userIconMenuController;
    
    /* Constructor: This one is used by the XML file to automatically generate
     * a SpaceView
     */
    public SpaceView(Context context, AttributeSet attrs){
    	super(context, attrs);
    	this.context = context;
    	setFocusable(true);
    	setFocusableInTouchMode(true);
    	//contactListController = new ContactListController(context, this);
    	empytSpaceMenuController = new EmptySpaceMenuController(context,this);
    	userIconMenuController = new UserIconMenuController(context,this);
    	Log.v(LOG_TAG, "Made SpaceView for XML file");
    	Log.v(LOG_TAG, "New allIcons attri");
    	setupImage();
    }
    
    /* Constructor: Create a screen for a NEW privatespace that is empty (except for you) 
     * Initialize all variables */
     public SpaceView(Context context, Space parent_space){
    	super(context);
    	this.context = context;
        space = parent_space;
        setFocusable(true);
        setFocusableInTouchMode(true);
    	//contactListController = new ContactListController(context, this);
        empytSpaceMenuController = new EmptySpaceMenuController(context,this);
        userIconMenuController = new UserIconMenuController(context,this);
        Log.v(LOG_TAG, "Made SpaceView for a self-created space");
        Log.v(LOG_TAG, "New allIcons normal");
        setupImage();
     }
   
     
     /* Add the image of the voice coming from you */
     public void setupImage(){
         BitmapFactory.Options opts = new BitmapFactory.Options();
         opts.inJustDecodeBounds = true;
         voice_image = BitmapFactory.decodeResource(this.getResources(), Values.voice_image); 
     }
     
     /* Manually set the space that this SpaceView corresponds to (used in conjunction with
      * the constructor for the XMl file */
     public void setSpace(Space space){
    	 this.space = space;
     }
     
     /* Change the space that this screen is representing. This also means changing the list of
      * icons that are to be drawn */
     public void changeSpace(Space space){
    	 this.space = space;
    	 allIcons = new LinkedList<UserView>();
    	 addManyPeople(space.getAllIcons());
    	 invalidate();
     }
     
     /* Add many people to this space, add all of their icons to the SpaceView */
     public void addManyPeople(LinkedList<UserView> icons){
        for(UserView p: icons){
            addPerson(p);
        }
     }
   
     /* User added to the space, therefore create a new icon (UserView) for this person
      * and add it to this spaceview */
     public void addPerson(UserView icon){
        allIcons.add(icon);    
        invalidate();
     }
     
      /* User deleted from the space, therefore delete that User's icon from this SpaceView */
     public void deletePerson(User person){
    	 UserView found_icon = null;
    	 int counter = 0;
    	 while(found_icon==null && counter<allIcons.size()){
    		 if(allIcons.get(counter).getPerson()==person)
    			 found_icon = allIcons.get(counter);
    		 counter++;
    	 }
    	 if(found_icon!=null)
    		 allIcons.remove(found_icon);
    	 invalidate();
     }
     
     //GETTERS
     
     /* Get the main background color */
     private int getColor(){
    	 return R.color.main_background;
     }
     /* Get the list of all Icons shown in this spaceview */
     public LinkedList<UserView> getAllIcons(){
    	 return allIcons;
     }
     /* Get the Space that this SpaceView is representing */
     public Space getSpace(){
    	 return space;
     }
     public MainApplication getActivity(){
    	 return (MainApplication)context;
     }
     
     /* Draw everything on the screen including:
      * 1) Background color
      * 1b) Draw voice image
      * 2) All icons (need loop, call the icon's draw function in class UserView)
      * 3) Preview (if not null), need to get icons from the SpaceView and draw them
      * 4) Dimmed background overlay // design team omitted
      * 5) Trashcan icon // -changed to a delete button
      */
     protected void onDraw(Canvas canvas){
		  Log.v(LOG_TAG, "In SpaceView's onDraw");
    	 //(1)
    	 canvas.drawColor(getColor());
    	 //(1b)
    	 canvas.drawBitmap(voice_image, Values.screenW/2 - voice_image.getWidth()/2, 
    			 Values.spaceViewH - voice_image.getHeight(), null);
    	 //(2)
    	 
    	 if(!dim){ 
    		 for (UserView p : allIcons){
    	 
    		 if(p.getPerson()!=MainApplication.user_primary)
    			 p.draw(canvas);
    		 }
    	 //(3) 
    	 if(open_preview!=null){
    		 //TODO VINAY draw the preview and the small icon images here
    	 }
    	 }else{//Crystal Q
    	  for(UserView p: allIcons){
          	 if(p==initialIcon)
          		 Log.v(LOG_TAG, "Nora!!!");
          		 p.draw(canvas);
          	
      	   }
    	 }
     
 }
     public SpaceViewController getSpaceViewController(){
    	 return spaceViewController;
     }
     
     /*doubleclick the button to delete it--Crystal Q*/     
     public void doubleClicktoDelete(UserView toDelete){
    	 // this.invalidate(); TO-DO later
    	 try {
			((MainApplication)context).deletePerson(space, toDelete.getPerson());
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	   	 dim=false; 
  	   	 this.invalidate();
     }    
     
	 /* Handle Touch events... MANY cases to consider 
      * A PS icon = private space icon, squares that represent another private space
      * at the bottom bar of the gui 
      * Only adding people to privatespaces and 
      * Need to be able to do all of this:
      * 1) Drag a person's icon around the screen (but only within screen
      * and not past the buttons), make sure to notify network to update spatialization
      * 2) Highlight a person's icon by tapping once (later perhaps open miniprofile)
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
     
	 boolean clickOnIcon = false;
	 
     public boolean onTouchEvent(MotionEvent event) {
    	 int mouseX = (int) event.getX();
    	 int mouseY = (int) event.getY();
    	 
    	 if(event.getAction() == MotionEvent.ACTION_DOWN){
    		 // If clicked on an icon
    		 selectedIcon = null;
    		 for (UserView icon : allIcons) {
    			 if (icon.clickedInside(mouseX, mouseY)) {
    				 selectedIcon = icon;
 					 icon.getUserViewController().handleClickDown(icon.getX(), icon.getY(), event.getEventTime());
 					 clickOnIcon = true;
    			 }
    		 }
    		 // If clicked on an empty space
    		 if(!clickOnIcon)
    			 this.getSpaceViewController().handleClickDown(event.getEventTime());
         }
    	 else if(event.getAction() == MotionEvent.ACTION_UP){
        	 // If clicked on an empty space
        	 if(!clickOnIcon){
        		 boolean show_popup = this.getSpaceViewController().handleClickUp(event.getEventTime());
        		 if(show_popup)
        			 EmptySpaceMenuController.showFreeSpaceMenu();
        	 }
        	 
 			// if clicked on an icon
        	 else{
        		 boolean showIconMenu = selectedIcon.getUserViewController().handleClickUp(mouseX, mouseY, event.getEventTime());
        		 if(showIconMenu)
        			 this.userIconMenuController.showIconMenu();
 				
 				 // if released icon over an privatespace icon, then add that person to the private space
 				 for(PrivateSpaceIconView p : PrivateSpaceIconView.allPSIcons){
 					 if(p.contains(mouseX, mouseY)){
 						 p.setHighlighted(false);
 						 (p.getSpace()).getInvitationController().inviteUser(selectedIcon.getPerson(), null);
 						 
 						 /* Detect if icon was dropped into the empty PS or an existing PS.
 						    if first icon dropped in space, make a new space*/
 						 if((p.getSpace()).getAllIcons().size()==1){
 							 int newspaceID= Integer.parseInt(p.getSpace().getRoomID()) + 1;
 							 try {
 								 new Space((MainApplication)context,false,String.valueOf(newspaceID),null);
 							 } catch (XMPPException e) {
 								 // TODO Auto-generated catch block
 								 e.printStackTrace();
 							 }
 							 break;
 						 }
 					 }
 				 }
 				 selectedIcon = null;
 				 clickOnIcon = false;
 				
        	 	}
         }else if(event.getAction() == MotionEvent.ACTION_MOVE){
 			 if (selectedIcon != null) {
 				// If a person icon is selected, then move the icon to the current position
 				 selectedIcon.getUserViewController().handleMoved(mouseX, mouseY);
 				 // if icon is dragged over private space, then highlight that private space icon
 				 if (hoveredPrivSpace == null) {
 					 for (PrivateSpaceIconView p : PrivateSpaceIconView.allPSIcons) {
 						 if (p.contains(mouseX, mouseY)) {
 							 //p.setHovered(true);
 							 p.getPrivateSpaceIconController().handleIconHovered();
 							 //p.setHighlighted(true);
 							 hoveredPrivSpace = p;
 						 }
 					 }
 				 } else if (hoveredPrivSpace != null) {
 					 if (!hoveredPrivSpace.contains(mouseX, mouseY)) {
 						 hoveredPrivSpace.getPrivateSpaceIconController().handleIconNotHovered();
 						 //hoveredPrivSpace.setHighlighted(false);
 						 hoveredPrivSpace = null;
 					 }
 				 } 
 			 }
         }
         invalidate();
         return true;
     } 
 }
