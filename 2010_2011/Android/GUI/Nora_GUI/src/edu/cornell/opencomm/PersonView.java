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
	private static String LOG_TAG = "NORA_PersonView";
	Context context; // dummy for now
	Person person; // The person object this icon is representing
	int x, y; // The position of this icon (Top-Left corner)
	Bitmap image; // The actual image that will show on the user screen
	
	// If true, the image is isSelected, symbolizing that 
	// the icon has been pressed by the user
	boolean isSelected;

	/** Constructor:
	 * 1)Initialize all variables
	 * 2)Decide positions of image (x,y)
	 * 3)Create Bitmap image from imageID
	 */
	public PersonView(Context context, Person person, int imageID){
		super(context); // blahblahblah dummy
		this.context = context;
		Log.v(LOG_TAG, "Creating an icon for Person " + person.getUsername());
        // (1)
        this.person = person;
        this.isSelected = false;
        // (3)
        BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID);
        // (2)
		double x = Math.random();
        this.x = (int)(Math.random()*SpaceView.screenWidth - image.getWidth());
        this.y = (int)(Math.random()*SpaceView.screenHeight - image.getHeight());
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
        
        Log.v(LOG_TAG, "I'm drawing in PersonView!");
        super.onDraw(canvas);
		
		if (isSelected) {
			RectShape rect2 = new RectShape();
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(Color.YELLOW);
			int w=10, h=10;
			s.setBounds(x - 2, y - 2, x + w + 4, y + h + 4);
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
	} 
}
