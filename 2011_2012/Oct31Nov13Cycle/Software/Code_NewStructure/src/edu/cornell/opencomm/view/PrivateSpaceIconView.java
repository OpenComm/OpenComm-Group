package edu.cornell.opencomm.view;

import java.util.LinkedList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.color;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.PrivateSpaceIconController;
import edu.cornell.opencomm.controller.PrivateSpacePreviewPopupController;
import edu.cornell.opencomm.model.Space;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/** An icon representing a private space room, will appear at the bottom scrollable bar of the screen */ 
public class PrivateSpaceIconView extends ImageButton{
	private static String LOG_TAG="OC_PrivateSpaceView"; // for error checking 
    Context context;
    
	public Space space; // the private space that this icon represents
    public static LinkedList<PrivateSpaceIconView> allPSIcons
    = new LinkedList<PrivateSpaceIconView>(); // the list of all current privatespace icons
    
    public static int[]COLORS = {Color.BLUE, Color.YELLOW, Color.GREEN, 
    	Color.MAGENTA, Color.CYAN, Color.DKGRAY}; // the colors offered for private spaces
    public static int colorInt = -1; // the current color
    private int color = Color.BLUE; // this icons color
    
    /** Controllers */
    PrivateSpaceIconController privateSpaceIconController;
    PrivateSpacePreviewPopupController privateSpacePreviewPopupController;
    
    /** States of the icon */
    boolean isSelected=false; // if this is true, then the icon should appear highlighted/darkened
    boolean isHighlighted=false; // if this is true, should also provide some feedback
    boolean showPreview = false; // true if the preview is open
    boolean clickedOnce=false; // true if you have already clicked this icon once (and should be highlighted)
   boolean clickDownOnce=false;
    /** Initialize all variables 
     * Set up a touch listener (call init) */
    public PrivateSpaceIconView(Context context, Space space){
    	super(context);
    	this.context = context;
        this.space = space;
        privateSpaceIconController = new PrivateSpaceIconController(this);
        privateSpacePreviewPopupController = new PrivateSpacePreviewPopupController(context, this);
        addThisPSView();
        initColors();
        initTouch();
    }
    
    /** Constructor for XML file */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, int defStyle, Space parent){
    	super(context, attrs, defStyle);
    	this.context = context;
    	this.space = parent;
    	 privateSpaceIconController = new PrivateSpaceIconController(this);
        addThisPSView();
        initColors();
        initTouch();
    }
    
    /** Another constructor for XML file */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, Space parent){
    	super(context, attrs);
    	this.context = context;
    	this.space = parent;
    	 privateSpaceIconController = new PrivateSpaceIconController(this);
    	 privateSpacePreviewPopupController = new PrivateSpacePreviewPopupController(context, this);
        addThisPSView();
        initColors();
        initTouch();
    }
    
    /** Add this PrivateSpace Icon officially to the XML and add to the static list of 
     * all PS icons */
    public void addThisPSView(){
        allPSIcons.add(this);
        ((MainApplication)context).addPrivateSpaceButton(this); 
    }
    
    /** Delete this PrivateSpace Icon officially from the XML. Remove from the static list of 
     *  all PS icons */
    public void deleteThisPSView(){
        allPSIcons.remove(this);
        ((MainApplication)context).delPrivateSpaceButton(this); 
    }
    
    /** Initialize this icon's color */
    public void initColors(){
    	colorInt++;
    	this.color = PrivateSpaceIconView.COLORS[colorInt % PrivateSpaceIconView.COLORS.length];
    }
    
    /* Set up a touch listener that will adjust the selections and highlights according
     * to if you touched the icon. 
     * 1) Click once should highlight button and open preview (close other previews) TODO VINAY
     * 2) Click a second time should open up that private space (call method from main application), and unhighlight the button */
    public void initTouch(){
    	this.setOnTouchListener(new View.OnTouchListener(){
    		public boolean onTouch(View view, MotionEvent evt){
    			switch(evt.getAction()){
    			/* TODO VINAY - this is where the clicking of the private space
    			 * icon is happening, and it seems to freeze right after opening up a 
    			 * popup preview! - NORA
    			 */
    			case MotionEvent.ACTION_UP:
    				// Highlight the icon on or off, and return true if on
    				boolean showPopup = privateSpaceIconController.handleClickUp();
    				Log.v("PSIconView", "Clicked up, highlight " + showPopup);
    				if (showPopup)
    					// if the highlight is on, then show the popup preview
    					privateSpacePreviewPopupController.openPopupPreview();
    				else
    					// if not, then close the popup preview
    					privateSpacePreviewPopupController.closePopupPreview();

    				break;
    			}
    			return false;
    		}
    	});
    	invalidate();
    }
    

    

    

    /** Draw this icon, draw darker (or brighter) if this PrivateSpaceIconView is highlighted or selected. 
     * 
     * This particular version will draw a square for the PS icon and then will fill in the square 
     * if highlighted or selected with color
     */
     public void draw(Canvas canvas){
    	 super.onDraw(canvas);
		/* Draw...:
		 * 1. Draw background to erase old state
		 * 2. Draw this private space's color square
		 * 3. Draw smaller darker square if the private space isn't open or being previewed
		 * 4. Draw the image for an empty privatespace button
		 * 5. Draw the preview if open */
    	
    	 //(1)
		 int backgroundColor = R.color.scroll_background;
		 canvas.drawColor(backgroundColor);
		 //(2)
		 RectShape rect = new RectShape();
		 /* This function checks for people in the space. If there are atleast 1 person, 
		  * then the function draws a filled space at the bottom of the mainview 
		  * TODO: vthe user always exist in the space, the participants roster stored 
		  * under space contains at least 1 person*/
		 if(this.getSpace().getAllParticipants().size()> 1){
			 ShapeDrawable normalShape = new ShapeDrawable(rect);
			 normalShape.getPaint().setColor(color);
			 normalShape.setBounds(2, 2, this.getWidth() - 2, this.getHeight() - 2);
			 normalShape.draw(canvas);
		 }
		 //(3)
		 RectShape rect2 = new RectShape();
		 if (!(this.isSelected || this.isHighlighted)) {
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(backgroundColor);
			s.setBounds(6, 6, this.getHeight() - 4, this.getHeight() - 6);
			s.draw(canvas);
		 } 
		 
		 RectShape rect3 = new RectShape();
		 /* This function checks for people in the space. If there are no person, 
		  * then the function draws a empty space at the bottom of the mainview
		  * TODO: (From Network team) the user always exist in the space, the participants roster stored 
		  * under space contains at least 1 person */
		 if(this.getSpace().getAllParticipants().size()< 1){					
			ShapeDrawable sq = new ShapeDrawable(rect3);
			sq.getPaint().setStyle(Paint.Style.FILL);
			sq.getPaint().setColor(Color.TRANSPARENT);
			sq.getPaint().setStyle(Paint.Style.STROKE);
			sq.getPaint().setStrokeWidth(3);
			sq.getPaint().setColor(Color.BLACK);
			sq.setBounds(2,2,this.getWidth() - 2, this.getHeight() - 2);
			sq.draw(canvas);
		 }
		 //(5)
		 if(showPreview){
			 // TODO VINAY you could draw the preview here if you like
	     }
    
    } 
     
     /** Open a different space to the screen */
     // NORA- updated 9/3
     public void openPrivateSpace(){
    	 Log.v(LOG_TAG, "Trying to open this space " + space.getRoomID() + ". Screen on is " + space.isScreenOn());
    	 if(!space.isScreenOn()){
    		 ((MainApplication)context).changeSpace(space);
    	 }
     }
     
     /** Return true if this person clicked within this icon's area on the screen */
     public boolean contains(int x, int y) {
 		int[] location = new int[2];
 		this.getLocationOnScreen(location);
 		if (!this.isShown())
 			return false;
 		return (x > location[0] && x < location[0] + this.getWidth()
 				&& y > location[1] - this.getHeight() && y < location[1]
 				+ this.getHeight());
 	}
    
     /* Toggle a icon if you touch it */
     protected synchronized void toggle(View arg0){
     	this.isSelected = !isSelected;
     	this.invalidate();
     }
     
    // GETTERS
    public Space getSpace(){
        return space;
    }
    public boolean isHighlighted(){
        return isHighlighted;
    }
    public boolean isSelected(){
        return isSelected;
    }
    public PrivateSpaceIconController getPrivateSpaceIconController(){
    	return privateSpaceIconController;
    }
    
    public PrivateSpacePreviewPopupController getPrivateSpacePreviewPopupController(){
    	return privateSpacePreviewPopupController;
    }
    
    // SETTERS
    /** Set the icon to be highlighted or not (in cases where the icon is tapped) */
    public void setSelected(boolean selected){
        isSelected = selected;
        invalidate();
    }
    /** Not the same as selected, but merely highlighted
     * for cases where you drag a person's icon over the privatespace icon and you
     * want to see some feedback */
    public void setHighlighted(boolean highlighted){
        isHighlighted = highlighted;
        invalidate();
    }
    /** Set if the preview of this icon is open */
    public void setPreview(boolean open){
    	showPreview= open;
    }
    
    /** Toggle between being selected and not selected */
    public void toggleSelected(){
    	isSelected = !isSelected;
    }
   
}
