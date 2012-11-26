package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceUser;

public class ConferenceRoomFragment extends Fragment {
	private View roomLayout;
	public int layoutId = R.layout.conference_main_room;
	private String TAG = ConferenceRoom.class.getName();
	public ConferenceRoom conferenceRoom;
	boolean DEBUG = true;
	public ArrayList<UserView> userViews = new ArrayList<UserView>();
	public Context context ;
	public static final int radius = 165;

	public ConferenceRoomFragment(Context context,int layoutId,ConferenceRoom room){
		this.layoutId = layoutId;
		this.context = context;
		this.conferenceRoom = room;
		Log.d(TAG,"layoutId :"+layoutId);
		Log.d(TAG, "Conference Room :"+conferenceRoom);
	}
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		if (container == null) {
            return null;
        }
		this.roomLayout = inflater.inflate(layoutId, container, false);
		//ViewPager vp = (ViewPager) roomLayout;
		//int i = vp.getCurrentItem();
		
		/*View v = getView();
		Log.d("Me - View", Boolean.toString(v==null));
		rl = (RelativeLayout) getView().findViewById(R.id.action_bar); */
		
		ViewTreeObserver observer = roomLayout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			public void onGlobalLayout() {
				Log.d(TAG, "Before Room Height :"+roomLayout.getHeight());
				Log.d(TAG, "Before Room Width :"+roomLayout.getWidth());
				createUsers();
			}
		});
		return this.roomLayout;
	}
	private boolean isCreated = false;
		
	public void createUsers(){
		Log.d(TAG, "createUsers : isCreated ="+isCreated);
		if(!isCreated){
			createTheCirleOfTrust();
			isCreated = true;
		}
	}
	@Override
	public void onDestroyView() {
		isCreated = false;
		super.onDestroyView();
	}
	public void setContext(Context context){
		this.context = context;
	}
	
	
	/**
	 * Add a userView to this roomFragment
	 */
	public void addUserView(UserView userView){
		userViews.add(userView);
	}
	
	
	/** 
	 * Position the users
	 */
	public void createTheCirleOfTrust(){
		Log.d(TAG, "Drawing the circle of trust :Conference Room :"+conferenceRoom);
		Log.d(TAG, "Room Height :"+roomLayout.getHeight());
		Log.d(TAG, "Room Width :"+roomLayout.getWidth());
		if (conferenceRoom != null) {
			Point center = new Point(roomLayout.getWidth() / 2,
					roomLayout.getHeight() / 2);
			ArrayList<ConferenceUser> userList = conferenceRoom
					.updateLocations(center, radius);
			int i =10;
			for (ConferenceUser confUser : userList) {
				UserView uv = new UserView(context, confUser);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(76, 76);
				params.leftMargin = confUser.getX();
				params.topMargin = confUser.getY();
				((ViewGroup) roomLayout).addView(uv, params);
				
			}
//			ImageView iv = new ImageView(context);
//			iv.setBackgroundColor(Color.YELLOW);
		}
		
	}
		
		

	/**
	 * Assign the positions of each userView
	 * @param users
	 * @return
	 */
/*	private Point[] createPlaceHolders(ArrayList<UserView> attendees){
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
	} */
	
	
}
