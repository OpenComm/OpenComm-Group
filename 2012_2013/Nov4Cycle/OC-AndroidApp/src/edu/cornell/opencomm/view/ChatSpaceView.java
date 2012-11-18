package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;

public class ChatSpaceView extends View{

	public ChatSpaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	//Constructor 2: required for inflation from resource file 
	public ChatSpaceView(Context context, AttributeSet attr){
		super (context, attr); 

	}
	//Constructor 3: required for inflation from resource file
	public ChatSpaceView(Context context, AttributeSet attr, int defaultStyles){
		super(context, attr, defaultStyles); 
	}


	@Override 
	protected void onMeasure(int widthSpec, int heightSpec){
		int measuredWidth = MeasureSpec.getSize(widthSpec); 
		int measuredHeight = MeasureSpec.getSize(heightSpec); 
		this.setMeasuredDimension(measuredWidth, measuredHeight); 
	}
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas); 

		//get the size of your control based on the last call to onMeasure
		int height = this.getMeasuredHeight(); 
		int width = this.getMeasuredWidth(); 

		//Draw the circle
		createCircle(canvas); 

		//add users to the circle
//		this.updateCircle(canvas); 

	}
	//Main View Methods:
	//TODO - I dont think this should be implemented here. Should it?
	//When the screen is clicked- need to display the action bar/context bar and display usernames
	public void onScreenClicked(){

	}

	//When the user drags the person to another location inside the circle
	public void onDrag(){

	}

	public void createCircle(Canvas canvas){
		//Draws a circle 
		Paint paint1 = new Paint();
		Paint paint2 = new Paint();
		final RectF rect = new RectF();
		//Working with pixels 
		int mRadius = 165;
		//TODO - Did not adjust for the action bar on top and context bar on the bottom
		rect.set(getWidth()/2- mRadius, getHeight()/2 - mRadius, getWidth()/2 + mRadius, getHeight()/2 + mRadius); 
		paint1.setColor(Color.GRAY);
		paint1.setStrokeWidth(2);
		paint1.setAntiAlias(true);
		paint1.setStrokeCap(Paint.Cap.BUTT);
		paint1.setStyle(Paint.Style.STROKE);
		paint2.setColor(Color.GRAY);
		paint2.setStrokeWidth(2);
		paint2.setAntiAlias(true);
		paint2.setStrokeCap(Paint.Cap.BUTT);
		paint2.setStyle(Paint.Style.STROKE);
		canvas.drawArc(rect, 0, 60, false, paint1);
		canvas.drawArc(rect, 60, 60, false, paint2);
		canvas.drawArc(rect, 120, 60, false, paint1);
		canvas.drawArc(rect, 180, 60, false, paint2);
		canvas.drawArc(rect, 240, 60, false, paint1);
		canvas.drawArc(rect, 300, 60, false, paint2);
	}


	//This method is also a part of the action bar/context methods
	//Do this when the person is dragged outside the circle's radius - especially when the picture reaches the ends of the screen
	public void addPersonToSideChat(){

	}

	//Moderator Only
	public boolean endConferencePressed(){
		//Stubbed
		return true; 
	}

	//Moderator only- Pressed when moderator wants to select someone else as a moderator?
	public void moderatorPressed(){

	}

	//Moderatory and User
	public void leavePressed(){

	}

	//When the moderator leaves the conference
	public void moderatorLeft(){

	}

	//Moderator and User
	public void profilePressed(){

	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

}