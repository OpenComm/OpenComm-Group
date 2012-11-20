package edu.cornell.opencomm.view;

import java.util.ArrayList;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.User;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConferenceRoomView extends View {
	private int layout;
	private Context context;
	private ArrayList<UserView> attendees;

	static boolean isCreated;
	public ConferenceRoomView(Context context, int layout) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layout = layout;
		this.attendees = new ArrayList<UserView>();
	}
	
	public void create(){
		if (isCreated){
			inflate(context, layout, (ViewGroup)findViewById(R.id.threepanelpager)); //TODO this is magic
			
		}
	}
	public void resume(){
		if(isCreated){
			//invalidate?
		}else{
			//create
		}
	}
	
	public void addUserView(UserView userView){
		attendees.add(userView);
		invalidate();
		positionUsers(attendees);
	}
	
	
	public void positionUsers(ArrayList<UserView> attendees){
		final int mRadius = 165; 
		final int adjustRadiusX = 153/4; 
		final int adjustRadiusY = 207/4; 
		int i = getWidth()/2-adjustRadiusX; 
		int j = getHeight()/2-adjustRadiusY;
		
		int numberOfPoints = attendees.size(); 
		float angleIncrement = 360/numberOfPoints; 
		for(int n = 0; n< numberOfPoints; n++){
			UserView userView = attendees.get(n);
			int x = (int)(mRadius* Math.cos((angleIncrement*n)*(Math.PI/180) + (Math.PI/2)))+i ;
			int y = (int) (mRadius* Math.sin((angleIncrement*n)*(Math.PI/180) + (Math.PI/2)))+j;
			//userView.setPosition(x, y);
		}
	}
	
	
	protected void onDraw(Canvas canvas) {
		Log.d("ConferenceRoomView", "It's drawing!");
		for(UserView userView : attendees){
			userView.draw(canvas);
		}
	}
 
}
