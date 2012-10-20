package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class UserView extends Button{
	Context context;
	User person;
	Space space;
	Bitmap image;
	Paint paint;
	int x, y; //position of icon
	boolean ghost = false;

	public UserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UserView(Context context, User person, int imageID, Space space, int x, int y){
		super(context);
		this.context = context;
		this.person = person;
		this.space = space;
	}

	public UserView(Context context, User person, Bitmap image, int x, int y) {
		super(context);
		this.context = context;
		this.person = person;
		this.x = x;
		this.y = y;
		this.image = image;
		this.ghost = true;
		setPaint(context);
	}

//	public void onDraw(Canvas canvas){
////		canvas.drawCircle(x, y, 20, paint);
//	}

	public User getPerson(){
		return person;
	}

	public UserView getGhost() {
		return new UserView(context,person,image,x,y);
	}

	public void setPopupImage(Bitmap oldImage){
		/*	BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			this.image = BitmapFactory.decodeResource(context.getResources(), imageID); */
		int width = (oldImage).getWidth();
		int height = (oldImage).getHeight();
		int newWidth = 42;
		int newHeight = 42;
		float scaleWidth = ((float) newWidth)/width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		this.image = Bitmap.createBitmap(oldImage, 0, 0, width, height, matrix, true);	
	}

	public void setPaint(Context context){
		paint = new Paint();
		paint.setDither(true);
		paint.setColor(0xFF000000);;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
	}
}
