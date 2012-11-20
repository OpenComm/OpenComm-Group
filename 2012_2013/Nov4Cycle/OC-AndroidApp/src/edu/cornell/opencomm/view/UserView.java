package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageButton;


public class UserView extends ImageButton{
	Context context;
	User person;
	int position_x, position_y; // Top-left corner position
	Bitmap image;
	User user;
	ConferenceView conferenceView;
	

	public UserView(Context context, ConferenceView conferenceView, User user, Bitmap image, int position_x, int position_y) {
		super(context);
		this.context = context;
		this.position_x = position_x;
		this.position_y = position_y;
		this.user = user;
		this.image = image;
		this.conferenceView = conferenceView;
	}
	
	public void draw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawBitmap(image, position_x, position_y, null);
	}
	
}
