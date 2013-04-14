package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.interfaces.OCUpdateListener;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceUser;
import edu.cornell.opencomm.util.OCBitmapDecoder;

/**
 * @author Spandana Govindgari [frontend], Nora NQ[frontend],Ankit
 *         Singh[frontend
 * 
 *         ISSUES: The border color gets cut off at the top when drawn. However,
 *         on drag the border color is intact. 2. Why does every user have the
 *         same default color? Is hashing working?
 */
public class UserView extends ImageButton implements OCUpdateListener {

	private static String LOG_TAG = UserView.class.getName(); // for error
	// checking
	/**
	 * The context in which the view exists
	 */
	private Context context;
	/**
	 * The ConferenceUser object for which the user view exists
	 */
	private ConferenceUser conferenceUser;

	// private static final int cornerSize = 4;
	// private static final int borderSize = 6;

	public UserView(Context context, ConferenceUser conferenceUser) {
		super(context);
		this.context = context;
		this.conferenceUser = conferenceUser;
		this.conferenceUser.addLocationUpdateListner(this);
		this.setBackgroundResource(0); 
		init();

		Log.d(LOG_TAG, "Created a UserView for : "
				+ conferenceUser.getUser().getUsername());
	}

	private void init() {
		if (conferenceUser.getUser().compareTo(UserManager.PRIMARY_USER) == 0) {
			Bitmap  bm = getRoundedCornerBitmap(OCBitmapDecoder.getThumbnailFromResource(
					getResources(), UserManager.PRIMARY_USER.getImage()));
			int width = bm.getWidth();
	        int height = bm.getHeight();
	        float scaleWidth = ((float) OCBitmapDecoder.THUMBNAIL_WIDTH)/width;
	        float scaleHeight = ((float) OCBitmapDecoder.THUMBNAIL_HEIGTH) / height;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        this.setImageBitmap(Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true));
		} else {
			this.setImageBitmap(getImage());
		}
		invalidateLocation();
	}
	
	private void invalidateLocation(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				OCBitmapDecoder.THUMBNAIL_WIDTH,OCBitmapDecoder.THUMBNAIL_HEIGTH);
		params.leftMargin = conferenceUser.getX();
		params.topMargin = conferenceUser.getY();
		setLayoutParams(params);
	}
	public Point getCurrentLocation() {
		return conferenceUser.getLocation();
	}

	public ConferenceUser getCUser() {
		return conferenceUser;
	}


	// Redraws the bitmap and makes the edges rounded and adds border color//
	// Borrowed from:
	// http://stackoverflow.com/questions/11012556/border-over-a-bitmap-with-rounded-corners-in-android//

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {		
		
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float screenWidth = (float) display.getWidth();
		float userPicSize = screenWidth * 13/48;
		
		Bitmap output = Bitmap.createBitmap((int)userPicSize, (int)userPicSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		// just for conversion
		int borderDips = 2;
		final int borderSizePx = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, (float) borderDips, context
				.getResources().getDisplayMetrics());
		// prepare for canvas
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, (int)userPicSize, (int)userPicSize);
		// prepare canvas for transfer
		paint.setAntiAlias(true);
		paint.setColor(0xFFFFFFFF);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(userPicSize / 2, userPicSize / 2, userPicSize / 2 - 5, paint);

		// draw bitmap
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, null, rect, paint);

		// draw border
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth((float) borderSizePx);
		canvas.drawCircle(userPicSize / 2, userPicSize / 2, userPicSize / 2 - 5, paint);

		//draw banner
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRect(0, 80, 130, 105, paint);
		
		//draw banner border
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawRect(0, 80, 130, 105, paint);
		
		//draw user name
		paint.setColor(Color.WHITE);
		paint.setTextSize(15);
	    paint.setTextAlign(Align.CENTER);
		canvas.drawText("Hello", 65, 98, paint);

		return output;
		
	}

	public Bitmap getImage() {
		//Consider caching the image?
		Bitmap  bm = getRoundedCornerBitmap(OCBitmapDecoder.getThumbnailFromResource(
				getResources(), conferenceUser.getUser().getImage()));
		int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) OCBitmapDecoder.THUMBNAIL_WIDTH)/width;
        float scaleHeight = ((float) OCBitmapDecoder.THUMBNAIL_HEIGTH) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	}
	@Override
	public void invalidate() {
		if(conferenceUser != null){
		invalidateLocation();
		}
		super.invalidate();
	}
	public void onUpdate(int eventId, Object data) {
		invalidate();
	}

}
