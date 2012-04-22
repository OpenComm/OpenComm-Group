package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.widget.ImageButton;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.UserViewController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

/** The graphical icon representing a user (User object) that will show up
 * on the user interface screen */

public class UserView extends ImageButton{
    private static String LOG_TAG = "OC_PersonView"; // for error checking
    Context context;
    User person; // The person object this icon is representing
    int x, y; // The position of this icon (Top-Left corner)
    Bitmap image; // The actual image that will show on the user screen
    Bitmap nameBoxImage; // The name box on a user's icon
    Space space;
    boolean ghost = false;
    
    //font
    private Typeface font;

    boolean isSelected=false; // true if image selected (should show highlight around it)
    boolean isMoved; // true if image was dragged and not simply tapped
    Paint paint;
    UserViewController userViewController;
    UserView thisUserView;
    static UserView selectedIcon;
    boolean clickOnIcon;
    private boolean lassoed;

    final private static int GHOST_ALPHA = 50;

    /** Constructor:
     * 1)Initialize all variables
     * 2)Decide positions of image (x,y)
     * 3)Create Bitmap image from imageID, create namebox image, and set paint
     */
    public UserView(Context context, User person, int imageID, Space space, int x, int y){
        super(context);
        this.context = context;

        // (1)
        this.person = person;
        //this.isSelected = false;
        this.space = space;

        // (2)
        /*	this.x = (int)(Math.random()*(Values.screenW - Values.userIconW));
		if(this.x<0)
			this.x = 0;
		this.y = (int)(Math.random()*Values.spaceViewH - Values.userIconH);
		if(this.y<Values.actionBarH)
			this.y = Values.actionBarH;  */
        this.x = x;
        this.y = y;

        // (3)
        setImage(imageID);
        setNameBoxImage(Values.icon_namebox);
        setPaint(context); // set paint
        Log.v(LOG_TAG, "Made a UserView for person " + person);

        userViewController = new UserViewController(this);
        //thisUserView = this;
        //setupListeners();
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
        //thisUserView = this;
        //setupListeners();
    }

    //Used to create spooky ghosts
    public UserView(Context context, User person, Bitmap image, int x, int y) {
        super(context);
        this.context = context;
        this.person = person;
        this.x = x;
        this.y = y;
        this.image = image;
        this.ghost = true;
        setNameBoxImage(Values.icon_namebox);
        setPaint(context); // set paint
        userViewController = new UserViewController(this);
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


    @Override
    public void draw(Canvas canvas){
        // This current version draws will draw the icon, and will draw
        // a colored rectangle around the person's icon if selected

        super.onDraw(canvas);
        //  Log.v("UserView", "UserView's onDraw()");
        int b=Values.iconBorderPadding;
        int namebox=Values.iconTextH;//the namebox height
        if (isSelected) {
            RectShape rect2 = new RectShape();
            ShapeDrawable s = new ShapeDrawable(rect2);
            s.getPaint().setColor(Color.YELLOW);
            s.setBounds(x -b-Values.selectedBorder, y - b-Values.selectedBorder, x + image.getWidth() + 2*Values.selectedBorder+b, y + image.getHeight() + 2*Values.selectedBorder+b+namebox);
            s.draw(canvas);
        }

        /*if(nameBoxImage!=null){
                canvas.drawBitmap(nameBoxImage, x, y + Values.userIconH - Values.iconTextH, null);
                canvas.drawText(person.getUsername(), 0, Math.min(11,(person.getUsername()).length()), x + Values.iconTextPadding,
                    y + Values.userIconH - Values.iconTextPadding, paint);
            }*/
        //Crystal

        if(person!=null){
            //Crystal
            //  Log.v(LOG_TAG, "FINAL!");
            RectShape rect1= new RectShape();
            ShapeDrawable bord= new ShapeDrawable(rect1);
            // Log.v(LOG_TAG,"person color"+person.user_color);
            //bord.getPaint().setStyle(Style.STROKE);
            // bord.getPaint().setStrokeWidth(b);
            bord.getPaint().setColor(getResources().getColor(person.user_color));
            if(ghost)
                bord.setAlpha(GHOST_ALPHA);
            else
                bord.setAlpha(204);
            bord.setBounds(x-b,y-b,x+image.getWidth()+b,y+image.getHeight()+b);
            Log.v(LOG_TAG, "SIZE"+(image.getWidth()+2*b)+""+(image.getHeight()+2*b));
            //border.setPadding(b,b,b,b);
            
            bord.draw(canvas);
            //Crystal image
            Bitmap overlay = Bitmap.createBitmap(image.getWidth(),image.getHeight(), Bitmap.Config.ARGB_8888);
            
            Canvas c = new Canvas(overlay);

            paint.setAntiAlias(true);
            paint.setTextSize(Values.nameTextSize);
            if(ghost) {
                paint.setAlpha(GHOST_ALPHA);
                Paint imagePaint = new Paint();
                imagePaint.setAntiAlias(true);
                imagePaint.setAlpha(GHOST_ALPHA);
                c.drawBitmap(image, 0, 0, imagePaint);
            } else
                c.drawBitmap(image, 0, 0, null);
            RectShape nTag= new RectShape();
            ShapeDrawable nameTag= new ShapeDrawable(nTag);
            nameTag.getPaint().setColor(getResources().getColor(person.user_color));
            nameTag.setAlpha(204);
            nameTag.setBounds(0, image.getHeight()-namebox,image.getWidth(), image.getHeight());
            if(ghost)
                nameTag.setAlpha(GHOST_ALPHA);
            nameTag.draw(c);
            c.drawText(person.getUsername(), 0, Math.min(11,(person.getUsername()).length()),0/*Values.iconTextPadding*/,
                    image.getHeight()-namebox+Values.nameTextSize/*5/2*Values.iconBorderPadding+10*/, paint);
            canvas.drawBitmap(overlay, x, y, null);

            if(space != null && person==space.getOwner()){

                Bitmap adminTag = Bitmap.createBitmap(image.getWidth()+2*b,20, Bitmap.Config.ARGB_8888);
                Canvas ac = new Canvas(adminTag);
                Paint ad= new Paint();
                ad.setColor(Color.rgb(0,0,0));
                ad.setAlpha(204);
                ac.drawRect( 0, 0, image.getWidth()+2*b,20, ad);
                ad.setAlpha(255);
                ad.setColor(Color.WHITE);
                ad.setStyle(Paint.Style.FILL);
                ad.setAntiAlias(true);
                ad.setTextSize(Values.adminTextSize);
                ac.drawText("admin",Values.textAdjust, (Values.adminBox-Values.textAdjust), ad);
                canvas.drawBitmap(adminTag,x-b, y-b, null);



            }
        }
        else{
            canvas.drawBitmap(image, x, y, null);
        }
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
    /** Returns this UserView's userViewController */
    public UserViewController getUserViewController(){
        return userViewController;
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

    public void setXY(int newX, int newY){
        Log.v("UserView", "setting new x to " + newX + ":" + newY);
        x = newX;
        y = newY;
        invalidate();
    }

    /* Create paint */
    public void setPaint(Context context){
        paint = new Paint();
        paint.setDither(true);
        paint.setColor(0xFF000000);;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        font = Typeface.createFromAsset(context.getAssets(), Values.font);
        paint.setTypeface(font);
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

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    //Returns a ghostly version of this UserView
    public UserView getGhost() {
        return new UserView(context,person,image,x,y);
    }

    public void setLassoed(boolean lassoed) {
        this.lassoed = lassoed;
    }

    public boolean isLassoed() {
        return lassoed;
    }

    //Implementation of the reply to:
    //http://stackoverflow.com/questions/99353/how-to-test-if-a-line-segment-intersects-an-axis-aligned-rectange-in-2d
    public boolean segmentIntersects(Point p1, Point p2) {
        int b = Values.iconBorderPadding;
        int right = x + image.getWidth() + 2*b;
        int bottom = y + image.getHeight() + 2*b;
        Point TL = new Point(x, y);
        Point TR = new Point(right, y);
        Point BR = new Point(right, bottom);
        Point BL = new Point(x, bottom);

        if(p1.x > TR.x && p2.x > TR.x) { return false; }
        if(p1.x < BL.x && p2.x < BL.x) { return false; }
        if(p1.y < TR.y && p2.y < TR.y) { return false; }
        if(p1.y > BL.y && p2.y > BL.y) { return false; }

        ArrayList<Point> corners = new ArrayList<Point>(
            Arrays.asList(TL, TR, BR, BL)
        );

        ArrayList<Integer> funcs = new ArrayList<Integer>();
        for(Point p : corners) {
            int func = (p2.y - p1.y) * p.x + (p1.x - p2.x) * p.y + (p2.x * p1.y - p1.x * p2.y);
            Log.d("TEXAS", "hogFunc:" + func);
            if(func == 0){
                return true;
            }
            funcs.add(func);
        }
        if(funcs.get(0) > 0 && findVal(funcs, true) ||
           funcs.get(0) < 0 && findVal(funcs, false)) {
            return false;
        }

        return true;
    }

    private boolean findVal(ArrayList<Integer> funcs, boolean positive) {
        for(int i = 1; i < funcs.size(); i++) {
            if(positive != (funcs.get(i) > 0))
                return false;
        }
        return true;
    }

}
