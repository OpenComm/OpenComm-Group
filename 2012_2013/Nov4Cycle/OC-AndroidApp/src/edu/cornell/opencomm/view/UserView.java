package edu.cornell.opencomm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ConferenceUser;


public class UserView extends ImageButton{
	Context context;
	Bitmap image;
	ConferenceUser conferenceUser;
	ConferenceRoomFragment roomView;
	Point position;
	
	public UserView(Context context, ConferenceRoomFragment roomView, ConferenceUser confUser, Bitmap image) {
		super(context);
		Log.d("UserView - c1", "1 + " + this);
		this.context = context;
		Log.d("UserView - c1 cU", Boolean.toString(confUser==null));
		this.conferenceUser = confUser;
		this.image = image;
		this.roomView = roomView;
		initTouchListener((View)this);
		this.setLayoutParams(new LayoutParams(76, 76));
	}
	
	public UserView(Context context, AttributeSet attr){
		super(context);
		Log.d("UserView - c2", "2");
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
	
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas); 
		Log.i("Canvas size- height and width", "" + canvas.getHeight() + " " + canvas.getWidth()); 
		Log.d("UserView", "I'm drawing too!" + this);
		Drawable background = new BitmapDrawable(this.image); 
		this.setBackgroundDrawable(background); 
		//TODO- Here!! THis is the issue - conference user is fucked up
		//this.setPosition(this.conferenceUser.LOCATION); 
		Paint p = new Paint();
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.example_picture_1);
		Bitmap rescaled_image = Bitmap.createScaledBitmap(image, 76, 76, true);
		//Log.i("is confuser null?" , Boolean.toString(conferenceUser == null)); 
		
		//canvas.drawBitmap(rescaled_image, this.conferenceUser.getX(), 0, p); 

	}
	
	
	public void setPosition(Point position){
		this.position = position;
	}
}
