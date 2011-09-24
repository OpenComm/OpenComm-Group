package edu.cornell.opencomm;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/** An icon representing a private space room, will appear at the bottom scrollable bar of the screen */ 
public class PrivateSpaceView extends ImageButton{
	private static String LOG_TAG="NORA_PrivateSpaceView";
    Context context;
	Space space; // the private space that this icon represents
    boolean isSelected; // if this is true, then the icon should appear highlighted/darkened
    boolean isHighlighted; // if this is true, should also provide some feedback
    // the list of all current privatespace icons
    public static LinkedList<PrivateSpaceView> allPSIcons= new LinkedList<PrivateSpaceView>(); 
    // the colors offered for private spaces
    public static int[]COLORS = {Color.BLUE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.DKGRAY}; 
    public static int colorInt = -1;
    private int spaceID;
    private int color = Color.BLUE; // this icons color
    
    /** Initialize all variables 
     * Set up a touch listener (call init) */
    public PrivateSpaceView(Context context, Space space){
    	super(context);
    	Log.v(LOG_TAG, "Created a new PrivateSpaceView (bottom icon)");
    	this.context = context;
        this.space = space;
        isSelected = false;
        isHighlighted = false;
        addThisPSView();
    }
    
    /** Constructor for XML file */
    public PrivateSpaceView(Context context, AttributeSet attrs, int defStyle, Space parent){
    	super(context, attrs, defStyle);
    	this.context = context;
    	this.space = parent;
    	isSelected = false;
        isHighlighted = false;
        addThisPSView();
    }
    
    /** Another constructor for XML file */
    public PrivateSpaceView(Context context, AttributeSet attrs, Space parent){
    	super(context, attrs);
    	this.context = context;
    	this.space = parent;
    	isSelected = false;
        isHighlighted = false;
        addThisPSView();
    }
    /*
    public PrivateSpaceView clone(){
    	PrivateSpace ps = this.space.clone();
    	PrivateSpaceView psv = new PrivateSpaceView(context, ps);
    	psv.spaceID = this.spaceID;
    	psv.color = this.color;
    	init2();
    	return psv;
    } */
    
    /** Initialize object's spaceId, color, and add it to list
     * of spaces
     */
    public void init(){
    	this.spaceID = colorInt++;
    	this.color = PrivateSpaceView.COLORS[colorInt % PrivateSpaceView.COLORS.length];
    }
    
    public void init2(){
    	this.setOnTouchListener(new View.OnTouchListener(){
    		public boolean onTouch(View view, MotionEvent evt){
    			switch(evt.getAction()){
    			case MotionEvent.ACTION_DOWN:
    				break;
    			case MotionEvent.ACTION_MOVE:
    				break;
    			case MotionEvent.ACTION_UP:
    				break;
    			}
    			return false;
    		}
    	});
    	invalidate();
    }
    
    protected synchronized void toggle(View arg0){
    	//TODO do I need to make others not selected?
    	this.isSelected = !isSelected;
    	this.invalidate();
    }
    
    /** Add this PrivateSpace Icon officially to the GUI (because MainApplication needs
     * to add this button to the XML and can only be done in the MainApplication class */
    public void addThisPSView(){
        allPSIcons.add(this);
        ((MainApplication)context).addPrivateSpaceButton(this); // TODO: put back!
    }
    
    /** Delete this PrivateSpace Icon */
    public void deleteThisPSView(){
        allPSIcons.remove(this);
        ((MainApplication)context).delPrivateSpaceButton(this); // TODO: put back!
    }
    
    /** Set up a touch listener that will adjust the selections and highlights according
     * to if you touched the icon. 
     * 1) Click once should highlight button 
     * 2) Click a second time should open up that private space (call method from main application)
    public void init(){
    
    // TODO Copied from earlier code, will need some fixing
    
    		this.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					toggle(view);
					if (!clickedOnce) {
						clickedOnce = true;
						MainApplication.showPreview(space.people);

					} else if (clickedOnce) {
						openPrivateSpace(); // start new privateSpace activity!
											// (either create new or restart)
						clickedOnce = false;
						MainApplication.showPreview(null);
					}
					break;
				}
				return false;
			}
		});
		invalidate();
    
    }

    /** Draw this icon, draw darker (or brighter) if
     * this PrivateSpaceView is highlighted or selected */
  
    // commented out temporarilty to avoid errors
    
    /* public void draw(Canvas canvas){
    
    // TODO: Copied from old code, this particular version will draw a square for the PS icon
    // and then will fill in the square if highlighted or selected with color
    
    
    		super.onDraw(canvas); // Draw the regular stuff

		// Draw in 3 steps:
		// 1. Draw background to erase old state
		// 2. Draw this private space's color
		// 3. Draw smaller square if the private space isn't open or being
		// previewed //
		int backgroundColor = R.color.scroll_background;
		RectShape rect = new RectShape();
		ShapeDrawable normalShape = new ShapeDrawable(rect);
		normalShape.getPaint().setColor(color);
		normalShape.setBounds(2, 2, this.getWidth() - 2, this.getHeight() - 2);
		canvas.drawColor(backgroundColor);
		normalShape.draw(canvas);
		RectShape rect2 = new RectShape();
		if (!this.isSelected) {
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(backgroundColor);
			s.setBounds(6, 6, this.getHeight() - 4, this.getHeight() - 6);
			s.draw(canvas);
			showPreview = false;

		} else
			showPreview = true;
    
    } */
    
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
    
    // SETTERS
    
    /** Set the icon to be highlighted or not (in cases where the icon is tapped) */
    public void setSelected(boolean selected){
        isSelected = selected;
    }
    
    /** Not the same as selected, but merely highlighted
     * for cases where you drag a person's icon over the privatespace icon and you
     * want to see some feedback */
    public void setHighlighted(boolean highlighted){
        isHighlighted = highlighted;
    }
}
