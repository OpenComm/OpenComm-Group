package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.model.ConferenceUser;

/** The graphical icon representing a user (User object) that will show up
 * on the user interface screen */

public class UserView extends ImageButton{

	private static String LOG_TAG = UserView.class.getName(); // for error checking
	private static int USER_WIDTH = 76;
	private static int USER_HEIGHT = 76;
	private Context context;
	private ConferenceUser person; // The person object this icon is representing
	private Bitmap image; // The actual image that will show on the user screen


	public UserView(Context context, ConferenceUser conferenceUser){
		super(context);
		this.context = context;

		this.person = conferenceUser;
		setImage(conferenceUser.getUser().getImage());
		setImageBitmap(image);
		RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(USER_WIDTH,USER_HEIGHT);
		params.leftMargin = conferenceUser.getX();
		params.topMargin = conferenceUser.getY();
		setLayoutParams(params);
		Log.v(LOG_TAG, "Made a UserView for person " + conferenceUser);
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
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		this.image = BitmapFactory.decodeResource(context.getResources(), imageID);
		//        int width = (this.image).getWidth();
		//        int height = (this.image).getHeight();
		int newWidth = 76;
		int newHeight = 76;
		rescaleSize(newWidth,newHeight);
		//        float scaleWidth = ((float) newWidth)/width;
		//        float scaleHeight = ((float) newHeight) / height;
		//        Matrix matrix = new Matrix();
		//        matrix.postScale(scaleWidth, scaleHeight);
		//        this.image = Bitmap.createBitmap(this.image, 0, 0, width, height, matrix, true);
	}



	
}


