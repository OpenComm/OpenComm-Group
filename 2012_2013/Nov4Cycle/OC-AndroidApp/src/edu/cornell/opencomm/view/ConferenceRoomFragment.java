package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ConferenceRoom;

public class ConferenceRoomFragment extends Fragment {
	View roomLayout;
	public int layoutId = R.layout.confernec_main_room;
	public  String roomName;
	private String TAG = ConferenceRoom.class.getName()+roomName;
	public ConferenceRoom room;
	boolean DEBUG = true;
	public ArrayList<UserView> userViews;
	
//	public ConferenceRoomFragment(ConferenceRoom room){
//		this.room = room;
//	}
//	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		userViews = new ArrayList<UserView>();
		if (container == null) {
			//ANKIT: i dont know what this means yet
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		this.roomLayout = inflater.inflate(layoutId, container, false);
		return this.roomLayout;
	}
	public void createUsers(){
		
	}
	
	public ConferenceRoom getRoom(){
		return room;
	}
	
	/**
	 * Add a userView to this roomFragment
	 */
	public void addUserView(UserView userView){
		userViews.add(userView);
		
	}
	
	/**
	 * Assign the positions of each userView
	 * @param users
	 * @return
	 */
	private Point[] createPlaceHolders(ArrayList<UserView> attendees){
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
	
	
}
