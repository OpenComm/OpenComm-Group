package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;

public class ChatSpaceViewGroup extends ViewGroup{

	private Activity view; 

	private static final boolean D = true; 

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
		points[7] = new Point(i-(153/2)-(153/4), j+(153/4));
		return points; 
	}
	private ArrayList<Point> getPoints(int users,int radius,Point center){
		    double slice = 2 * Math.PI / users;
		    ArrayList<Point> pointList  = new ArrayList<Point>();
		    for (int i = 0; i < users; i++)
		    {
		        double angle = slice * i;
		        int newX = (int)(center.x + radius * Math.cos(angle));
		        int newY = (int)(center.y + radius * Math.sin(angle));
		        Point p = new Point(newX, newY);
		        pointList.add(p);
		    }
		    return pointList;
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

	private void updateCircle(int p){
		ArrayList<User> conferenceUsers = this.getConferenceUsers(p); 
		//TODO- check to see if the user is the main user- then set the background to black
		Point[] placeholder = this.createPlaceHolders(conferenceUsers.size()); 
		for (int i = 0; i < conferenceUsers.size(); i++){
			Button child1 = (Button) this.getChildAt(i+1); 
			LayoutParams lp1 = (LayoutParams)child1.getLayoutParams(); 
			child1.layout(placeholder[i].x, placeholder[i].y, placeholder[i].x+76, placeholder[i].y+76);
			if (i == 4){
				child1.setBackgroundColor(Color.BLACK); 
			}
			else {child1.setBackgroundResource(conferenceUsers.get(i).getImage()); }

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

}