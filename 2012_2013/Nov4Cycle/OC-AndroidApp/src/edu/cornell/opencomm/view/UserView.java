package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


public class UserView extends ImageButton{
	Context context;
	Bitmap image;
	User user;
	ConferenceRoomFragment roomView;
	Point position;
	
	
	public UserView(Context context, ConferenceRoomFragment roomView, User user, Bitmap image) {
		super(context);
		this.context = context;
		this.user = user;
		this.image = image;
		this.roomView = roomView;
		initTouchListener((View)this);
	}
	
	public UserView(Context context, AttributeSet attr){
		super(context);
		initTouchListener((View)this);
	}
	
	
	public void initTouchListener(View view){
		view.setOnTouchListener(new View.OnTouchListener(){
		    public boolean onTouch(View v, MotionEvent e){
		        switch(e.getAction()){
		        case MotionEvent.ACTION_DOWN:
		        	// ghost the userview
		        	// create a image that is the same and is draggable
		        	break;
		        case MotionEvent.ACTION_UP:
		        	// change position of the view 
		        	
		        	break;
		        case MotionEvent.ACTION_MOVE:
		        	
		        	break;
		        }
		        return true;
		    }
		});
	}
	
	
	
	public void draw(Canvas canvas){
		Log.d("UserView", "I'm drawing too!");
		super.onDraw(canvas);
		//Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.example_picture_1);
		//canvas.drawBitmap(image, position_x, position_y, null);
		//Bitmap bm_2 = Bitmap.createScaledBitmap(bm, 49, 49, false);
		//canvas.drawBitmap(image, position_x, position_y, null);
	}
	
	public void setPosition(Point position){
		this.position = position;
	}
}
