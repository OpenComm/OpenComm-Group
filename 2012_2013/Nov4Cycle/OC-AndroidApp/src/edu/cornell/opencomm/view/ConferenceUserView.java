package edu.cornell.opencomm.view;

import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


public class ConferenceUserView extends ImageButton{
	Context context;
	User person;
	int position_x=0, position_y=0; // Top-left corner position
	Bitmap image;
	User user;
	ConferenceView conferenceView;
	
	
	public ConferenceUserView(Context context, ConferenceView conferenceView, User user, Bitmap image) {
		super(context);
		this.context = context;
		this.user = user;
		this.image = image;
		this.conferenceView = conferenceView;
		initTouchListener((View)this);
	}
	
	public ConferenceUserView(Context context, ConferenceView conferenceView, User user, Bitmap image, int position_x, int position_y) {
		super(context);
		this.context = context;
		this.position_x = position_x;
		this.position_y = position_y;
		this.user = user;
		this.image = image;
		this.conferenceView = conferenceView;
		initTouchListener((View)this);
	}
	
	public ConferenceUserView(Context context, AttributeSet attr){
		super(context);
	}
	
	
	public void initTouchListener(View view){
		view.setOnTouchListener(new View.OnTouchListener(){
		    public boolean onTouch(View v, MotionEvent e){
		        switch(e.getAction()){
		        case MotionEvent.ACTION_DOWN:
		        	// ghost the userview
		        	// create a image that is the same and is draggable
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
		canvas.drawBitmap(image, position_x, position_y, null);
	}
	
	public void setPositionX(int x){
		this.position_x = x;
	}
	public void setPositionY(int y){
		this.position_y = y;
	}
	public void setPosition(int x, int y){
		this.position_x = x;
		this.position_y = y;
	}
}
