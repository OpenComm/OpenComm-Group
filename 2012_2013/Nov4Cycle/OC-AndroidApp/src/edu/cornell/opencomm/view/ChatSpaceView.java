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
		this.updateCircle(canvas); 

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

	private Point[] createPlaceHolders(int users){
		//TODO - Currently maxing the number of conference users to 8 - tops! 
		Point [] points = new Point[8]; 
		int mRadius = 165; 
		int i = getWidth()/2-(153/4); 
		int j = getHeight()/2-mRadius-(207/4); 
		points[0] = new Point(i, j); 
		points[1] = new Point(i+(153/2)+(153/4), j+(153/4)); 
		points[2] = new Point(i+(153), j+(153)); 
		points[3] = new Point(i+(153)-(153/4), j+153+(153/2)+(153/4)); 
		points[4] = new Point(i, j+153+153+(153/4)); 
		points[5] = new Point(i-(153)+(153/4), j+153+(153/2)+(153/4)); 
		points[6]= new Point(i-153, j+153); 
		points[7] = new Point(i-(153/2)-(153/4), j-(153/4));
		return points; 
	}

	//Call this method whenever you want to refresh the circle. Adds placeholders at appropriate places and redraws the locations
	//Get the users from the conference happening now
	//Inflate the circle with the users who have entered the conference
	public void updateCircle(Canvas canvas){
		ArrayList<User> conferenceUsers = createExampleUsers(); 
		Point [] placeholders = this.createPlaceHolders(conferenceUsers.size()); 
		//TODO- Need a method to determine if the user is a main user or not 
		//If he is then should be displayed in a black box at the end of the circle
		int i = 0; 
		for (User user: conferenceUsers){
			//Place all conference participants except the main user- replace by the method that checks to see if he is the main user or not
			if (i!= 4){
				Paint paint = new Paint(); 
				int bmg = user.getImage(); 
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bmg);
				bitmap= Bitmap.createScaledBitmap(bitmap, 76, 76, true); 
				canvas.drawBitmap(bitmap, (float) placeholders[i].x, (float)placeholders[i].y, paint); 
			}
			else{
				Paint paint = new Paint(); 
				canvas.drawRect((float) placeholders[i].x, (float) placeholders[i].y, (float) placeholders[i].x+76, (float) placeholders[i].y+76, paint); 
//				int bmg = user.getImage(); 
//				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bmg);
//				bitmap= Bitmap.createScaledBitmap(bitmap, 76, 76, true); 
//				canvas.drawBitmap(bitmap, (float) placeholders[i].x, (float)placeholders[i].y, paint); 
			}
			i = i+1;
		}
	}
	//This method is also a part of the action bar/context methods
	//Do this when the person is dragged outside the circle's radius - especially when the picture reaches the ends of the screen
	public void addPersonToSideChat(){

	}

	//Context/Action Bar Methods:
	//When add is pressed- need to pop up the invite page
	//For now: Just resize the circle and place the person
	//Moderator and User 
	public void addPressed(){

	}

	//Moderator only
	public void removePerson(){

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

	//TODO- Backend Implementation- Till then dummy users 
	public ArrayList<User> createExampleUsers(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("naka_shaka_laka", "Risa Naka", R.drawable.example_picture_1));
		users.add(new User("noratheexplora", "Nora Ng-Quinn", R.drawable.example_picture_2));
		users.add(new User("makomania", "Makoto Bentz", R.drawable.example_picture_3));
		users.add(new User("graeme_craka", "Graeme Bailey", R.drawable.example_picture_1));
		users.add(new User("naj_hodge", "Najla Elmachtoub", R.drawable.example_picture_2));
		users.add(new User("xu_mu_moo", "Jason Xu", R.drawable.example_picture_3));
		return users;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}

}