package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.interfaces.OCUpdateListener;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceUser;

/** The graphical icon representing a user (User object) that will show up
 * on the user interface screen */

public class UserView extends ImageButton implements OCUpdateListener{

	private static String LOG_TAG = UserView.class.getName(); // for error checking
	private static int USER_WIDTH = 76;
	private static int USER_HEIGHT = 76;
	private Context context;
	private ConferenceUser person; // The person object this icon is representing
	private Bitmap image = null; // The actual image that will show on the user screen

	public UserView(Context context, ConferenceUser conferenceUser){
		super(context);
		this.context = context;

		this.person = conferenceUser;
		person.addLocationUpdateListner(this);
		setImage(conferenceUser.getUser().getImage());
		if(!conferenceUser.getUser().equals(UserManager.PRIMARY_USER)){

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
		// TODO Auto-generated method stub
		RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(USER_WIDTH,USER_HEIGHT);
		params.leftMargin = person.getX();
		params.topMargin = person.getY();
		setLayoutParams(params);
		System.out.println("Location Changed");
		invalidate();
		
	}

}


