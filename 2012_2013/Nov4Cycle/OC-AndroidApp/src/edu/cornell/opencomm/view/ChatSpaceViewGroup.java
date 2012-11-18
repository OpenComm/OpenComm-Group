package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Manager.UserManager;
import edu.cornell.opencomm.model.User;

public class ChatSpaceViewGroup extends ViewGroup implements OnTouchListener{

	private Activity view; 

	/*For debugging purposes*/
	private static final String TAG = ChatSpaceViewGroup.class.getSimpleName(); 

	private static final boolean D = true; 

	private final int adjustRadiusX = 153/4; 

	private final int adjustRadiusY = 207/4; 

	private final int imageSize = 76; 

	//TODO- remove this
	public int p= 0; 
	public int add = 1; 

	public int remove = -1; 

	public ChatSpaceViewGroup(Context context, int p) {
		super(context);
		this.p= p; 

	}

	//TODO-remove this
	public int getP(){
		return this.p; 
	}

	//Constructor 2: required for inflation from resource file 
	public ChatSpaceViewGroup(Context context, AttributeSet attr){
		super (context, attr); 
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpec = MeasureSpec.getSize(widthMeasureSpec); 
		int heightSpec = MeasureSpec.getSize(heightMeasureSpec); 

		final int count = getChildCount(); 
		for(int i = 0; i<count; i++){
			View child = this.getChildAt(i); 
			LayoutParams lp = (LayoutParams)child.getLayoutParams(); 
			measureChild(child, widthMeasureSpec, heightMeasureSpec); 
		}

		this.setMeasuredDimension(widthSpec, heightSpec); 

	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs); 
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	private Point[] createPlaceHolders(int users){
		Point [] points = new Point[users]; 
		final int mRadius = 165; 
		int i = getWidth()/2-adjustRadiusX; 
		int j = getHeight()/2-adjustRadiusY; 

		int numberOfPoints = users; 
		float angleIncrement = 360/numberOfPoints; 
		for(int n = 0; n< numberOfPoints; n++){
			Point p = new Point(); 
			p.x = (int)(mRadius* Math.cos((angleIncrement*n)*(Math.PI/180) + (Math.PI/2)))+i ;
			p.y = (int) (mRadius* Math.sin((angleIncrement*n)*(Math.PI/180) + (Math.PI/2)))+j;
			points[n] = p; 
		}
		return points; 
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

	private ArrayList<User> getConferenceUsers(int p){
		ArrayList<User> users = this.createExampleUsers(); 
		if (p == add){			
			users.add(new User("spandylucky", "Spandana Govindgari", R.drawable.example_picture_4)); 
			return users; 
		}
		if (p == remove){	
			users.remove(3); 
			return users;
		}
		else return users; 
	}

	private int mXPosition; 

	private int mYPosition; 
	private void updateCircle(int p){
		Resources res = this.getResources(); 
		ArrayList<User> conferenceUsers = this.getConferenceUsers(p); 
		//TODO- check to see if the user is the main user- then set the background to black
		Point[] placeholder = this.createPlaceHolders(conferenceUsers.size());
		//Draw the primary user first 
		Button child = (Button) this.getChildAt(1); 
		LayoutParams lp = (LayoutParams)child.getLayoutParams(); 
		child.layout(placeholder[0].x, placeholder[0].y, placeholder[0].x+76, placeholder[0].y+76);

		for (int i = 0; i < conferenceUsers.size(); i++){
			Button child1 = (Button) this.getChildAt(i+1); 	 
			LayoutParams lp1 = (LayoutParams)child1.getLayoutParams(); 
			child1.layout(placeholder[i].x, placeholder[i].y, placeholder[i].x+76, placeholder[i].y+76);
			if (!conferenceUsers.get(i).equals(UserManager.PRIMARY_USER)){
				child1.setBackgroundResource(conferenceUsers.get(i).getImage());
			}
			child1.setOnTouchListener(ChatSpaceViewGroup.this); 
			//			child1.setOnTouchListener(new OnTouchListener(){
			//
			//				public boolean onTouch(View v, MotionEvent event) {
			//					switch (event.getAction()){
			//					case MotionEvent.ACTION_DOWN: 
			//						final int x = (int) event.getX(); 
			//						final int y = (int) event.getY(); 
			//						
			//						//Remember where we started
			//						mXPosition = x; 
			//						mYPosition = y; 
			//						if (D) {Log.v("Crystal", "Are you going here?"+ x); }
			//						return true; 
			//					case MotionEvent.ACTION_MOVE:
			//						final int x_new = (int)event.getX(); 
			//						final int y_new = (int) event.getY();
			//						if (D) {Log.v("Move", "Are you going here?"+ x_new); }	
			//						
			//						//distance moved 
			//						
			//						final int dx = x_new - mXPosition; 
			//						final int dy = y_new - mYPosition;
			//						
			//						//Redraw this view based on the new coordinates
			//						//TODO- Need to change the layout parameters of the child object
			//						//Everything else works fine
			//						v.setPadding((int) event.getRawX(), (int) event.getRawY(), 0, 0); 
			//						v.invalidate(); 
			//						
			//						
			//						if (x_new <= 0){
			//							//TODO- push to the left side chat view
			//						}
			//						if(x_new >= getWidth()- 76){
			//							//TODO -push to the right side chat 
			//						}
			//						return true; 
			//					case MotionEvent.ACTION_UP:
			//						//TODO - push it to the original placeholder 
			//						
			//						return true; 
			//					}
			//					return false; 
			//				}
			//			}); 
			//		}
		}
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {	


		//Gets the chat space view and positions it on the group 
		View child = this.getChildAt(0); 
		LayoutParams lp = (LayoutParams)child.getLayoutParams(); 
		child.layout(0, 0, getWidth(), getHeight());	
		if (D){Log.v("P value", ""+ p); }
		this.updateCircle(getP()); 

		//TODO- Remove this- just for now - dummy add and remove 
		Button child2 = (Button) this.getChildAt(11); 
		LayoutParams lp2 = (LayoutParams)child2.getLayoutParams(); 
		child2.layout(40, 14, 165, 124);

		//TODO- Remove this- just for now - dummy add and remove 
		Button child3 = (Button) this.getChildAt(12); 
		LayoutParams lp3 = (LayoutParams)child3.getLayoutParams(); 
		child3.layout(220, 14, 345, 124);

	}
	public static class LayoutParams extends ViewGroup.LayoutParams{

		public LayoutParams(int width, int height){
			super(width, height); 
		}

		public LayoutParams(Context arg0, AttributeSet arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}


	}

	//TODO
	//Whenever a contact is pressed inside the circle in a conference
	public void contactPressed(View v){
	}

	//TODO - when the chat space view is pressed - display the action bars and usernames
	public void whenViewPressed(View v){

	}



	private ImageView image; 
	public boolean onTouch(View view, MotionEvent me) {
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			image = new ImageView (this.getContext()); 
			image.setImageBitmap(view.getDrawingCache()); 
			RelativeLayout layout = (RelativeLayout) findViewById(R.layout.activity_main); 
			layout.addView(image); 
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {

			Log.i("Drag", "Stopped Dragging");
		} else if (me.getAction() == MotionEvent.ACTION_MOVE) {			
			image.setPadding((int) me.getRawX(), (int) me.getRawY(), 0, 0);
			image.invalidate();
		}

		return false;
	}

}