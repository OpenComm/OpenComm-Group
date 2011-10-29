package edu.cornell.opencomm.view;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.widget.ImageButton;
import android.util.Log;

/** The graphical icon representing a user (User object) that will show up
 * on the user interface screen */
 
public class UserView extends ImageButton{
	private static String LOG_TAG = "OC_PersonView"; // for error checking
	Context context; 
	User person; // The person object this icon is representing
	int x, y; // The position of this icon (Top-Left corner)
	Bitmap image; // The actual image that will show on the user screen
	Bitmap nameBoxImage; // The name box on a user's icon

	boolean isSelected=false; // true if image selected (should show highlight around it)
	boolean isMoved; // true if image was dragged and not simply tapped
	Paint paint;
	
	/** Constructor:
	 * 1)Initialize all variables
	 * 2)Decide positions of image (x,y)
	 * 3)Create Bitmap image from imageID, create namebox image, and set paint
	 */
	public UserView(Context context, User person, int imageID){
		super(context);
		this.context = context;
		
        // (1)
        this.person = person;
        //this.isSelected = false;
        
        // (2)
		this.x = (int)(Math.random()*(Values.screenW - Values.userIconW));
		if(this.x<0)
			this.x = 0;
		this.y = (int)(Math.random()*Values.spaceViewH - Values.userIconH);
		if(this.y<Values.actionBarH)
			this.y = Values.actionBarH;
		
		 // (3)
		setImage(imageID);
		setNameBoxImage(Values.icon_namebox);
		setPaint(); // set paint
	}
	
	/*
	 * Vinay - This method is created in order to be able to create and add
	 * smaller person view icons to the preview popup
	 */
	public UserView(Context context, Bitmap image, int x, int y){
		super(context);
		this.context = context;
		this.image = image;
		this.x = x;
		this.y = y;
		setPopupImage(image);
	}
    
    /* Return true if the mouseX and mouseY parameters are within this UserView's 
     * area onscreen. Used to see if the user tapped on the icon */
    public boolean clickedInside(int mouseX, int mouseY){
        if(mouseX>=x && mouseX<=x+image.getWidth() && mouseY>=y && mouseY<=y+image.getHeight())
            return true;
        return false;
    }
    
    /* Draw this UserView's image, draw darker (or brighter) if
     * this UserView is highlighted */
    public void draw(Canvas canvas){
        // This current version draws will draw the icon, and will draw
        // a colored rectangle around the person's icon if selected
        
        super.onDraw(canvas);
		
		if (isSelected) {
			RectShape rect2 = new RectShape();
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(Color.YELLOW);
			s.setBounds(x - 2, y - 2, x + image.getWidth() + 4, y + image.getHeight() + 4);
			s.draw(canvas);
		}
		canvas.drawBitmap(image, x, y, null);
		if(nameBoxImage!=null){
			canvas.drawBitmap(nameBoxImage, x, y + Values.userIconH - Values.iconTextH, null);
			canvas.drawText(person.getUsername(), 0, Math.min(11,(person.getUsername()).length()), x + Values.iconTextPadding, 
				y + Values.userIconH - Values.iconTextPadding, paint);
		}
		//Crystal
		int b=Values.iconTextPadding;
				if(person.isAdmin){
					Paint ad= new Paint();
					ad.setColor(Color.rgb(0,0,0));
					canvas.drawRect(x-b, y-b, x+image.getWidth()+b,y-b+Values.icon_namebox,ad);
			        ad.setColor(Color.WHITE);
			        ad.setStyle(Paint.Style.FILL);
			        ad.setAntiAlias(true);
			        ad.setTextSize(13);
					canvas.drawText("admin", x-b, y-b, ad);	
				}
				//Crystal
			       Log.v(LOG_TAG, "FINAL!");
			        RectShape rect1= new RectShape();
			        ShapeDrawable bord= new ShapeDrawable(rect1);
			        Log.v(LOG_TAG,"person color"+person.user_color);
			        bord.getPaint().setStyle(Style.STROKE);
			       bord.getPaint().setStrokeWidth(b);
			        bord.getPaint().setColor(getResources().getColor(person.user_color));
			        bord.setAlpha(204);
			        bord.setBounds(x-b,y-b,x+image.getWidth()+b,y+image.getHeight()+b);
			        //border.setPadding(b,b,b,b);
			        bord.draw(canvas);
    }
		
	// GETTERS
		
	/* Returns User object this icon is representing */
	public User getPerson(){
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
	
	/* Create paint */
	public void setPaint(){
		paint = new Paint();
		paint.setDither(true);
		paint.setColor(0xFF000000);;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
	}
	
	/* Change icon pictures and resize them, parameter imageID is a R.drawable int */
	public void setImage(int imageID){
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID);
		int width = (this.image).getWidth();
		int height = (this.image).getHeight();
		int newWidth = Values.userIconW;
		int newHeight = Values.userIconH;
		float scaleWidth = ((float) newWidth)/width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.image = Bitmap.createBitmap(this.image, 0, 0, width, height, matrix, true);
	}
	
	/** Set an image and resize for an image in the popup */
	public void setPopupImage(Bitmap oldImage){
	/*	BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID); */
		int width = (oldImage).getWidth();
		int height = (oldImage).getHeight();
		int newWidth = Values.popUserIconW;
		int newHeight = Values.popUserIconH;
		float scaleWidth = ((float) newWidth)/width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.image = Bitmap.createBitmap(oldImage, 0, 0, width, height, matrix, true);
	}
	
	/* Set the name box image and resize */
	public void setNameBoxImage(int imageID){
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.nameBoxImage = BitmapFactory.decodeResource(context.getResources(), imageID);
		int width = (this.nameBoxImage).getWidth();
		int height = (this.nameBoxImage).getHeight();
		int newWidth = Values.userIconW;
		int newHeight = Values.iconTextH;
		float scaleWidth = ((float) newWidth)/width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.nameBoxImage = Bitmap.createBitmap(this.nameBoxImage, 0, 0, width, height, matrix, true);
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
