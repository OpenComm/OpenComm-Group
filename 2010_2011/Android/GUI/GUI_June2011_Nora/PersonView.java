package edu.cornell.opencomm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.ImageButton;
import android.util.Log;

/** The graphical icon representing a user (Person object) that will show up
 * on the user interface screen */
 
public class PersonView extends ImageButton{
	private static String LOG_TAG = "OC_PersonView"; // for error checking
	Context context; 
	Person person; // The person object this icon is representing
	int x, y; // The position of this icon (Top-Left corner)
	Bitmap image; // The actual image that will show on the user screen

	boolean isSelected=false; // true if image selected (should show highlight around it)
	boolean isMoved; // true if image was dragged and not simply tapped

	/** Constructor:
	 * 1)Initialize all variables
	 * 2)Decide positions of image (x,y)
	 * 3)Create Bitmap image from imageID
	 */
	public PersonView(Context context, Person person, int imageID){
		super(context);
		this.context = context;
		Log.v(LOG_TAG, "Creating an icon for Person " + person.getUsername());
		
        // (1)
        this.person = person;
        //this.isSelected = false;
        
        // (2)
		this.x = (int)(Math.random()*SpaceView.screenWidth)-55;
		if(this.x<0)
			this.x = 0;
		this.y = (int)(Math.random()*SpaceView.screenHeight)-55;
		if(this.y<0)
			this.y = 0;
		
		 // (3)
        BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID);
	}
    
    /* Return true if the mouseX and mouseY parameters are within this PersonView's 
     * area onscreen. Used to see if the user tapped on the icon */
    public boolean clickedInside(int mouseX, int mouseY){
        if(mouseX>=x && mouseX<=x+image.getWidth() && mouseY>=y && mouseY<=y+image.getHeight())
            return true;
        return false;
    }
    
    /* Draw this PersonView's image, draw darker (or brighter) if
     * this PersonView is highlighted */
    public void draw(Canvas canvas){
        // TODO: copied form earlier code, might need some fixing
        // This current version draws will draw the icon, and will draw
        // a colored rectangle around the person's icon if selected
        
        super.onDraw(canvas);
		
		if (isSelected) {
			//Log.v(LOG_TAG, "Icon " + this + " is selected");
			RectShape rect2 = new RectShape();
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(Color.YELLOW);
			//int w=10, h=10;
			s.setBounds(x - 2, y - 2, x + image.getWidth() + 4, y + image.getHeight() + 4);
			s.draw(canvas);
		}
		canvas.drawBitmap(image, x, y, null);
    }
		
	// GETTERS
		
	/* Returns Person object this icon is representing */
	public Person getPerson(){
        return person;
	}
	/* Returns X position of icon (Top-Left) */
	public int getX(){
        return x;
	}
	/* Returns Y position of icon (Top-Left) */
	public int getY(){
        return y;
	}
	/* Returns actual image of icon that will show on screen */
	public Bitmap getImage(){
        return image;
	}
	/* Returns true if this icon has been highlighted by the user */
	public boolean getIsSelected(){
        return isSelected;
	}
	/* Returns true if this icon was moved and not just pressed */
	public boolean getMoved(){
		return isMoved;
	}
	
	// SETTERS
	
	/* Change X position of icon (Top-Left) */
	public void setX(int newX){
        x = newX;
	}
	/* Change Y position of icon (Top-Left) */
	public void setY(int newY){
        y = newY;
	}
	/* Change icon pictures, parameter imageID is a R.drawable int */
	public void setImage(int imageID){
        BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID);
	}
	/* Change whether icon is highlighted or not (from user tapping the icon) */
	public void setIsSelected(boolean IsIt){
        isSelected = IsIt;
        Log.v(LOG_TAG, "Icon " + this + " is now selected " + isSelected);
	} 
	public void toggleSelected(){
		isSelected = !isSelected;
		Log.v(LOG_TAG, "Icon " + this + " is now selected " + isSelected);
	}
	/* Set whether this icon was dragged or not */
	public void setMoved(boolean moved){
		isMoved = moved;
	}
}
