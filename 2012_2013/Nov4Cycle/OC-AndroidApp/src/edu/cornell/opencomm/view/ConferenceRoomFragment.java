package edu.cornell.opencomm.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceUser;

public class ConferenceRoomFragment extends Fragment {
	View roomLayout;
	RelativeLayout rl;
	public int layoutId = R.layout.confernec_main_room;
	public  String roomName;
	private String TAG = ConferenceRoom.class.getName()+roomName;
	public ConferenceRoom conferenceRoom;
	boolean DEBUG = true;
	public ArrayList<UserView> userViews = new ArrayList<UserView>();
	public Context context ;
	public static final int radius = 165;
//	public ConferenceRoomFragment(ConferenceRoom room){
//		this.room = room;
//	}
//	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
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
		rl= (RelativeLayout) roomLayout; 		
		Log.i("Hellooo", Boolean.toString(rl==null)); 
		//ViewPager vp = (ViewPager) roomLayout;
		//int i = vp.getCurrentItem();
		
		/*View v = getView();
		Log.d("Me - View", Boolean.toString(v==null));
		rl = (RelativeLayout) getView().findViewById(R.id.action_bar); */
		createTheCirleOfTrust();
		return this.roomLayout;
	}
	public void createUsers(){
		
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public ConferenceRoom getConferenceRoom(){
		return conferenceRoom;
	}
	
	public void setConferenceRoom(ConferenceRoom conferenceRoom){
		this.conferenceRoom = conferenceRoom;
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
		Point center = new Point(roomLayout.getWidth()/2, roomLayout.getHeight()/2);
		ArrayList<ConferenceUser> userList = conferenceRoom.updateLocations(center, radius);
		for (ConferenceUser confUser : userList){
			UserView uv = new UserView(context, this, confUser, imageIdToBitmap(confUser.getUser().getImage(), 76, 76)); 
			addUserView(uv);
			Log.i("Conference User- points", "" + confUser.getLocation()); 
			Log.i("Me - rl", "" + Boolean.toString(rl==null));
			Log.i("Me - uv", "" + Boolean.toString(uv==null));
			Log.i("Me - roomlayout", Boolean.toString(roomLayout==null));
			
		/*	ViewPager vp = (ViewPager) roomLayout;
			View view = vp.getChildAt(1);
			RelativeLayout rr = (RelativeLayout) view.findViewById(R.id.main_container);
			rr.addView(uv);  */
			rl.addView(uv);
			//rl.invalidate(); 
			
		}
		//draw();
		
	}
		
		public Bitmap imageIdToBitmap(int imageID, int width, int height){
			Bitmap image = BitmapFactory.decodeResource(getResources(), imageID);
			Bitmap rescaled_image = Bitmap.createScaledBitmap(image, width, height, true);
			return rescaled_image;
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
