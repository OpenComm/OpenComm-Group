package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.interfaces.OCUpdateListener;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceUser;
/**
 * @author  Spandana Govindgari [frontend], Ankit Singh[frontend],Nora NQ[frontend]
 * ISSUES: The border color gets cut off at the top when drawn. 
 * However, on drag the border color is intact.
 * 2. Why does every user have the same default color? Is hashing working?
 */
public class UserView extends ImageButton implements OCUpdateListener{

	private static String LOG_TAG = UserView.class.getName(); // for error checking
	private static int USER_WIDTH = 76;
	private static int USER_HEIGHT = 76;
	private Context context;
	private ConferenceUser person; // The person object this icon is representing
	private Bitmap image = null; // The actual image that will show on the user screen
	private int borderColor; 
	private static final int cornerSize = 4; 
	private static final int borderSize = 6; 

	public UserView(Context context, ConferenceUser conferenceUser){
		super(context);
		this.context = context;
		this.person = conferenceUser;
		this.borderColor = conferenceUser.getUser().userColor; 
		Log.d(LOG_TAG, "border color = " + borderColor); 
		person.addLocationUpdateListner(this);
		setImage(conferenceUser.getUser().getImage());
		if(conferenceUser.getUser().compareTo(UserManager.PRIMARY_USER) == 0){
			setBackgroundColor(Color.BLACK);
		}else{
			setImageBitmap(image);		
		}

		RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(USER_WIDTH,USER_HEIGHT);
		params.leftMargin = conferenceUser.getX();
		params.topMargin = conferenceUser.getY();
		setLayoutParams(params);

		Log.v(LOG_TAG, "Made a UserView for person " + conferenceUser);
	}
	public Point getCurrentLocation(){
		return person.getLocation();
	}
	public ConferenceUser  getCUser(){
		return person;
	}
	/**
	 * @param newWidth
	 * @param newHeight
	 */
	public void rescaleSize(int newWidth, int newHeight){
		int width = (image).getWidth();
		int height = (image).getHeight();
		float scaleWidth = ((float) newWidth)/width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		image=Bitmap.createBitmap(this.image, 0, 0, width, height, matrix, true);
		image = getRoundedCornerBitmap(image, this.borderColor, UserView.cornerSize, UserView.borderSize, this.context); 
	}
	
	//Redraws the bitmap and makes the edges rounded and adds border color//
	//Borrowed from: http://stackoverflow.com/questions/11012556/border-over-a-bitmap-with-rounded-corners-in-android//
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
	            Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    //just for conversion
	    final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
	            context.getResources().getDisplayMetrics());
	    final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
	            context.getResources().getDisplayMetrics());
	    //prepare for canvas 
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);

	    // prepare canvas for transfer
	    paint.setAntiAlias(true);
	    paint.setColor(0xFFFFFFFF);
	    paint.setStyle(Paint.Style.FILL);
	    canvas.drawARGB(0, 0, 0, 0);
	    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

	    // draw bitmap
	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    // draw border
	    paint.setColor(color);
	    paint.setStyle(Paint.Style.STROKE);
	    paint.setStrokeWidth((float) borderSizePx);
	    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

	    return output;
	}
	/**
	 * @param imageID
	 */
	 public void setImage(int imageID){
		 if (imageID != -1) {
			 BitmapFactory.Options opts = new BitmapFactory.Options();
			 opts.inJustDecodeBounds = true;
			 this.image = BitmapFactory.decodeResource(context.getResources(),
					 imageID);
			 int newWidth = 76;
			 int newHeight = 76;
			 rescaleSize(newWidth, newHeight);
		 }else{
			 image = null;
		 }
	 }

	 public Bitmap getImage(){
		 return image;
	 }
	 public void onUpdate(int eventId, Object data) {
		 RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(USER_WIDTH,USER_HEIGHT);
		 params.leftMargin = person.getX();
		 params.topMargin = person.getY();
		 setLayoutParams(params);
		 System.out.println("Location Changed");
		 invalidate();

	 }

}


