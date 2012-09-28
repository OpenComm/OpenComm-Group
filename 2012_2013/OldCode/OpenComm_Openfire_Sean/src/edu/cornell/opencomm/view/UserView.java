package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.controller.MainApplication;
import edu.cornell.opencomm.controller.UserIconMenuController;
import edu.cornell.opencomm.controller.UserViewController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;

/**
 * The graphical icon representing a user (User object) that will show up on the
 * user interface screen
 */

public class UserView extends ImageButton {
	private static String LOG_TAG = "OC_PersonView"; // for error checking
	Context context;
	User person; // The person object this icon is representing
	int x, y; // The position of this icon (Top-Left corner)
	Bitmap image; // The actual image that will show on the user screen
	Bitmap nameBoxImage; // The name box on a user's icon
	Space space;

	// font
	private Typeface font;

	boolean isSelected = false; // true if image selected (should show highlight
								// around it)
	boolean isMoved; // true if image was dragged and not simply tapped
	Paint paint;
	UserViewController userViewController;
	UserView thisUserView;
	static UserView selectedIcon;
	boolean clickOnIcon;

	/**
	 * Constructor: 1)Initialize all variables 2)Decide positions of image (x,y)
	 * 3)Create Bitmap image from imageID, create namebox image, and set paint
	 */
	public UserView(Context context, User person, int imageID, Space space,
			int x, int y) {
		super(context);
		this.context = context;

		// (1)
		this.person = person;
		// this.isSelected = false;
		this.space = space;

		// (2)
		/*
		 * this.x = (int)(Math.random()*(Values.screenW - Values.userIconW));
		 * if(this.x<0) this.x = 0; this.y =
		 * (int)(Math.random()*Values.spaceViewH - Values.userIconH);
		 * if(this.y<Values.actionBarH) this.y = Values.actionBarH;
		 */
		this.x = x;
		this.y = y;

		// (3)
		setImage(imageID);
		setNameBoxImage(Values.icon_namebox);
		setPaint(context); // set paint
		Log.v(LOG_TAG, "Made a UserView for person " + person);

		userViewController = new UserViewController(this);
		// thisUserView = this;
		// setupListeners();
	}

	/*
	 * Vinay - This method is created in order to be able to create and add
	 * smaller person view icons to the preview popup
	 */
	public UserView(Context context, Bitmap image, int x, int y) {
		super(context);
		this.context = context;
		this.image = image;
		this.x = x;
		this.y = y;
		setPopupImage(image);
		// thisUserView = this;
		// setupListeners();
	}

	/*
	 * Return true if the mouseX and mouseY parameters are within this
	 * UserView's area onscreen. Used to see if the user tapped on the icon
	 */
	public boolean clickedInside(int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x + image.getWidth() && mouseY >= y
				&& mouseY <= y + image.getHeight())
			return true;
		return false;
	}

	/*
	 * Draw this UserView's image, draw darker (or brighter) if this UserView is
	 * highlighted
	 */

	public void draw(Canvas canvas) {
		// This current version draws will draw the icon, and will draw
		// a colored rectangle around the person's icon if selected

		super.onDraw(canvas);
		// Log.v("UserView", "UserView's onDraw()");
		int b = Values.iconBorderPadding;
		int namebox = Values.iconTextH;// the namebox height
		if (isSelected) {
			RectShape rect2 = new RectShape();
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(Color.YELLOW);
			s.setBounds(x - b - Values.selectedBorder, y - b
					- Values.selectedBorder, x + image.getWidth() + 2
					* Values.selectedBorder + b, y + image.getHeight() + 2
					* Values.selectedBorder + b + namebox);
			s.draw(canvas);
		}

		/*
		 * if(nameBoxImage!=null){ canvas.drawBitmap(nameBoxImage, x, y +
		 * Values.userIconH - Values.iconTextH, null);
		 * canvas.drawText(person.getUsername(), 0,
		 * Math.min(11,(person.getUsername()).length()), x +
		 * Values.iconTextPadding, y + Values.userIconH -
		 * Values.iconTextPadding, paint); }
		 */
		// Crystal

		if (person != null) {
			// Crystal
			// Log.v(LOG_TAG, "FINAL!");
			RectShape rect1 = new RectShape();
			ShapeDrawable bord = new ShapeDrawable(rect1);
			// Log.v(LOG_TAG,"person color"+person.user_color);
			// bord.getPaint().setStyle(Style.STROKE);
			// bord.getPaint().setStrokeWidth(b);
			bord.getPaint()
					.setColor(getResources().getColor(person.user_color));
			bord.setAlpha(204);
			bord.setBounds(x - b, y - b, x + image.getWidth() + b,
					y + image.getHeight() + b);
			Log.v(LOG_TAG,
					"SIZE" + (image.getWidth() + 2 * b) + ""
							+ (image.getHeight() + 2 * b));
			// border.setPadding(b,b,b,b);
			bord.draw(canvas);
			// Crystal image
			Bitmap overlay = Bitmap.createBitmap(image.getWidth(),
					image.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(overlay);

			paint.setAntiAlias(true);
			paint.setTextSize(Values.nameTextSize);

			c.drawBitmap(image, 0, 0, null);
			RectShape nTag = new RectShape();
			ShapeDrawable nameTag = new ShapeDrawable(nTag);
			nameTag.getPaint().setColor(
					getResources().getColor(person.user_color));
			nameTag.setAlpha(204);
			nameTag.setBounds(0, image.getHeight() - namebox, image.getWidth(),
					image.getHeight());
			nameTag.draw(c);
			c.drawText(person.getUsername(), 0,
					Math.min(11, (person.getUsername()).length()), 0/*
																	 * Values.
																	 * iconTextPadding
																	 */,
					image.getHeight() - namebox + Values.nameTextSize/*
																	 * 5/2*Values
																	 * .
																	 * iconBorderPadding
																	 * +10
																	 */, paint);
			canvas.drawBitmap(overlay, x, y, null);

			if (person == space.getOwner()) {

				Bitmap adminTag = Bitmap.createBitmap(image.getWidth() + 2 * b,
						20, Bitmap.Config.ARGB_8888);
				Canvas ac = new Canvas(adminTag);
				Paint ad = new Paint();
				ad.setColor(Color.rgb(0, 0, 0));
				ad.setAlpha(204);
				ac.drawRect(0, 0, image.getWidth() + 2 * b, 20, ad);
				ad.setAlpha(255);
				ad.setColor(Color.WHITE);
				ad.setStyle(Paint.Style.FILL);
				ad.setAntiAlias(true);
				ad.setTextSize(Values.adminTextSize);
				ac.drawText("admin", Values.textAdjust,
						(Values.adminBox - Values.textAdjust), ad);
				canvas.drawBitmap(adminTag, x - b, y - b, null);

			}
		} else {
			canvas.drawBitmap(image, x, y, null);
		}
	}

	// GETTERS

	/* Returns User object this icon is representing */
	public User getPerson() {
		return person;
	}

	/* Returns X position of icon (Top-Left) */
	public int getX() {
		return x;
	}

	/* Returns Y position of icon (Top-Left) */
	public int getY() {
		return y;
	}

	/* Returns actual image of icon that will show on screen */
	public Bitmap getImage() {
		return image;
	}

	/* Returns true if this icon has been highlighted by the user */
	public boolean getIsSelected() {
		return isSelected;
	}

	/* Returns true if this icon was moved and not just pressed */
	public boolean getMoved() {
		return isMoved;
	}

	/** Returns this UserView's userViewController */
	public UserViewController getUserViewController() {
		return userViewController;
	}

	// SETTERS

	/* Change X position of icon (Top-Left) */
	public void setX(int newX) {
		x = newX;
	}

	/* Change Y position of icon (Top-Left) */
	public void setY(int newY) {
		y = newY;
	}

	public void setXY(int newX, int newY) {
		Log.v("UserView", "setting new x to " + newX + ":" + newY);
		x = newX;
		y = newY;
		invalidate();
	}

	/* Create paint */
	public void setPaint(Context context) {
		paint = new Paint();
		paint.setDither(true);
		paint.setColor(0xFF000000);
		;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		font = Typeface.createFromAsset(context.getAssets(), Values.font);
		paint.setTypeface(font);
	}

	/*
	 * Change icon pictures and resize them, parameter imageID is a R.drawable
	 * int
	 */
	public void setImage(int imageID) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		if (person.getVCard().getAvatar() == null) {
			this.image = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.question);
			int width = (this.image).getWidth();
			int height = (this.image).getHeight();
			int newWidth = Values.userIconW;
			int newHeight = Values.userIconH;
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			this.image = Bitmap.createBitmap(this.image, 0, 0, width, height,
					matrix, true);
		} else {
			this.image = BitmapFactory
					.decodeByteArray(person.getVCard().getAvatar(), 0, 
							person.getVCard().getAvatar().length, opts);
			int width = (this.image).getWidth();
			int height = (this.image).getHeight();
			int newWidth = Values.userIconW;
			int newHeight = Values.userIconH;
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			this.image = Bitmap.createBitmap(this.image, 0, 0, width, height,
					matrix, true);
		}
	}

	/** Set an image and resize for an image in the popup */
	public void setPopupImage(Bitmap oldImage) {
		/*
		 * BitmapFactory.Options opts = new BitmapFactory.Options();
		 * opts.inJustDecodeBounds = true; this.image =
		 * BitmapFactory.decodeResource(context.getResources(), imageID);
		 */
		int width = (oldImage).getWidth();
		int height = (oldImage).getHeight();
		int newWidth = Values.popUserIconW;
		int newHeight = Values.popUserIconH;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.image = Bitmap.createBitmap(oldImage, 0, 0, width, height, matrix,
				true);
	}

	/* Set the name box image and resize */
	public void setNameBoxImage(int imageID) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.nameBoxImage = BitmapFactory.decodeResource(
				context.getResources(), imageID);
		int width = (this.nameBoxImage).getWidth();
		int height = (this.nameBoxImage).getHeight();
		int newWidth = Values.userIconW;
		int newHeight = Values.iconTextH;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.nameBoxImage = Bitmap.createBitmap(this.nameBoxImage, 0, 0, width,
				height, matrix, true);
	}

	/* Change whether icon is highlighted or not (from user tapping the icon) */
	public void setIsSelected(boolean IsIt) {
		isSelected = IsIt;
		Log.v(LOG_TAG, "Icon " + this + " is now selected " + isSelected);
	}

	public void toggleSelected() {
		isSelected = !isSelected;
		Log.v(LOG_TAG, "Icon " + this + " is now selected " + isSelected);
	}

	/* Set whether this icon was dragged or not */
	public void setMoved(boolean moved) {
		isMoved = moved;
	}
}
