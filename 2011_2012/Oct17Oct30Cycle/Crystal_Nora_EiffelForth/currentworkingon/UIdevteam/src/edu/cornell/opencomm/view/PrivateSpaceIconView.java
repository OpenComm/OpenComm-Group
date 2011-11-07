package edu.cornell.opencomm.view;

import java.util.LinkedList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.R.color;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
        addThisPSView();
        initColors();
        initTouch();
    }
    
    /** Constructor for XML file */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, int defStyle, Space parent){
    	super(context, attrs, defStyle);
    	this.context = context;
    	this.space = parent;
        addThisPSView();
        initColors();
        initTouch();
    }
    
    /** Another constructor for XML file */
    public PrivateSpaceIconView(Context context, AttributeSet attrs, Space parent){
    	super(context, attrs);
    	this.context = context;
    	this.space = parent;
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
    			case MotionEvent.ACTION_DOWN:
    				if(!clickDownOnce){
    					clickDownOnce =true;
    					isSelected=true;
    					Log.v(LOG_TAG, "CLICK ONCE");
    				}else{
    				((MainApplication)context).deletePrivateSpaceView(space);
    				clickDownOnce=false;
    				isSelected=false;
						};
    					
    				
    					break;
    			case MotionEvent.ACTION_MOVE:
    				break;
    			case MotionEvent.ACTION_UP:
    				toggle(view);
    				if(!clickedOnce){
    					clickedOnce = true;
    					showPreview = true;
    				}
    				else if (clickedOnce){
    					clickedOnce = false;
    					showPreview = false;
    					openPrivateSpace();
    				}
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
		 * 3. Draw smaller darker square if the private space isn't open or being previewed TO-DO:Crystal: SHOULD BE DELETED
		 * 4. Draw the image for an empty privatespace button
		 * 5. Draw the preview if open */
    	
    	 //(1)
		 int backgroundColor = Color.GRAY;//R.color.darkgray; revise
		 canvas.drawColor(backgroundColor);
		//(2)Crystal   
		 /* This function checks for people in the space. If there are at least 1 person, 
		  * then the function draws a filled space at the bottom of the mainview */
		 if(this.getSpace().getAllPeople().size()>0){
			 Bitmap subview = Bitmap.createBitmap(this.getWidth()-4, this.getHeight()-4, Bitmap.Config.ARGB_8888);
			 Canvas c = new Canvas(subview); 
			 int size=this.space.getAllPeople().size();
			 Rect[] layoutArray=this.getLayout((size>9? 9:size));
			 int counter=0;
			 for (User p : this.space.getAllPeople()){
		   		    Log.v(LOG_TAG, "add people square");
		   		    if(p != this.space.getModerator()){ Paint pan= new Paint();
		   		     pan.setStyle(Paint.Style.FILL);
		   			 pan.setColor(getResources().getColor(p.user_color));
		   		     c.drawRect(layoutArray[counter], pan);
		   		     counter++;
		   		    if(counter> 9) {
		   			break;
		   		}
		   		    }
		    }
			 canvas.drawBitmap(subview, 2, 2, null);
		 }
		 
		 RectShape rect3 = new RectShape();
		 /* This function checks for people in the space. If there are no person, 
		  * then the function draws a empty space at the bottom of the mainview*/
		 if(this.getSpace().getAllPeople().size()==0){					
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
    
		 if(this.isSelected){
			 RectShape r = new RectShape();
				ShapeDrawable s = new ShapeDrawable(r);
				s.getPaint().setColor(Color.YELLOW);
				s.setBounds(this.getLeft()-1,this.getTop()-1, 
						this.getRight()-1,this.getBottom()-1);
			    s.getPaint().setStyle(Paint.Style.STROKE);
			    s.getPaint().setStrokeWidth(1);
				s.draw(canvas);
		 }
    } 
     
     /** Open a different space to the screen */
     // NORA- updated 9/3
     public void openPrivateSpace(){
    	 Log.v(LOG_TAG, "Trying to open this space " + space.getSpaceID() + ". Screen on is " + space.isScreenOn());
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
   //Crystal
     public Rect[] getLayout(int size){
    	 Rect[] layout=new Rect[9];
    	 int w= this.getWidth();
    	 int h=this.getHeight();
    	 int d=2;// the distance between squares
    	 if(size<5){
    		 layout[0]=new Rect(d, d, w/2-1,h/2-1);
    		 layout[1]=new Rect(w/2-1+d, d, w-d, h/2-1);
    		 layout[2]=new Rect(d,h/2-1+d,w/2-1,h-d );
    		 layout[3]=new Rect(w/2-1+d, h/2-1+d,w-d,h-d);
    		 }else{
    			for(int r=0; r<3; r++){
    				for(int c=0;c<3;c++){
    					layout[3*r+c]=new Rect(d*(c+1)+((w-4*d)/3)*c, d*(r+1)+((h-4*d)/3)*r,
    							d*(c+1)+((w-4*d)/3)*(c+1),  d*(r+1)+((h-4*d)/3)*(r+1));
    				}
    			}
    		 //layout[0]= new Rect(d,d,(w-d*4)/3+d,(h-d*4)/4+d);
    		 //layout[1]= new Rect((w-d*4)/3+2*d,d,(h-d*4)/3+2*d, )
    		 }
    	 return layout;
     }
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
}
