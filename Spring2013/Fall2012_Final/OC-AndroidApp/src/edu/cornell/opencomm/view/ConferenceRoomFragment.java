package edu.cornell.opencomm.view;

import java.util.ArrayList;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.ConferenceRoom;
import edu.cornell.opencomm.model.ConferenceUser;

/**	
 * @author  Spandana Govindgari [frontend], Ankit Singh[frontend],Nora NQ[frontend]
 *
 */
public class ConferenceRoomFragment extends Fragment {
	//TODO : This class to be cleaned up once integrated with BE
	private View roomLayout;
	public int layoutId = R.layout.conference_main_room;
	private String TAG = ConferenceRoom.class.getName();
	public ConferenceRoom conferenceRoom;
	boolean DEBUG = true;
	public ArrayList<UserView> userViews = new ArrayList<UserView>();
	public Context context;
	public static final int radius = 165;
	private ImageView left_gradient; 
	private ImageView right_gradient; 

	public ConferenceRoomFragment(Context context, int layoutId,
			ConferenceRoom room) {
		this.layoutId = layoutId;
		this.context = context;
		this.conferenceRoom = room;
		Log.d(TAG, "layoutId :" + layoutId);
		Log.d(TAG, "Conference Room :" + conferenceRoom);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		if (container == null) {
			return null;
		}
		this.roomLayout = inflater.inflate(layoutId, container, false);
//		left_gradient = (ImageView) ((ViewGroup)roomLayout).findViewById(R.id.leftsidechatgradient);
//		right_gradient = (ImageView) ((ViewGroup)roomLayout).findViewById(R.id.rightsidechatgradient); 

		ViewTreeObserver observer = roomLayout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			public void onGlobalLayout() {
				createUsers();
			}
		});
		return this.roomLayout;
	}

	private boolean isCreated = false;

	public void createUsers() {
		Log.d(TAG, "createUsers : isCreated =" + isCreated);
		if (!isCreated) {
			createTheCirleOfTrust();
			isCreated = true;
		}
	}

	@Override
	public void onDestroyView() {
		isCreated = false;
		super.onDestroyView();
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Add a given userView to this roomFragment's list of UserViews
	 * @param userView
	 */
	public void addUserView(UserView userView) {
		userViews.add(userView);
	}
	
	/** 
	 * Remove a given userView from this roomFragment's list of UserViews
	 * @param userView
	 */
	public void removeUserView(UserView userView){
		userViews.remove(userView);
	}
	
	public ArrayList<UserView> getUserView(){
		return userViews;
	}
	@Override
	public void onStop() {
		userViews = null;
		conferenceRoom = null;
		super.onStop();
	}
	/**
	 * Position the users
	 */
	public void createTheCirleOfTrust() {
		Log.d(TAG, "Drawing the circle of trust :Conference Room :"
				+ conferenceRoom);
		Log.d(TAG, "Room Height :" + roomLayout.getHeight());
		Log.d(TAG, "Room Width :" + roomLayout.getWidth());
		if (conferenceRoom != null) {
			Point center = new Point(roomLayout.getWidth() / 2,
					roomLayout.getHeight() / 2);
			ArrayList<ConferenceUser> userList = conferenceRoom
					.updateLocations(center, radius);
			for (ConferenceUser confUser : userList) {
				
				UserView uv = new UserView(context, confUser);
				//Ankit: Make a primary equal to check
				if(confUser.getUser().compareTo(UserManager.PRIMARY_USER) == 0){
					uv.setOnClickListener(new OnClickListener() {
						//Ankit: Bad coding
						boolean set = false;
						public void onClick(View v) {
							UserView u = (UserView) v;
							if(set){
								u.setBackgroundColor(Color.BLACK);
							}else{
								u.setBackgroundColor(-1);
							}
							set = !set;
							u.invalidate();
						}
					});
				}else{
				ConferenceRoomFragment.UserTouchListner listener = new ConferenceRoomFragment.UserTouchListner();
				uv.setOnTouchListener(listener);
				uv.setOnLongClickListener(listener);
				}
				((ViewGroup) roomLayout).addView(uv);

			}
		}

	}
	public void displayInvitationBar(){
		RelativeLayout invitationBar = (RelativeLayout) roomLayout.findViewById(R.id.side_chat_invitation_bar);
		invitationBar.setVisibility(View.INVISIBLE);
		invitationBar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				v.invalidate();
				
			}
		});
		invitationBar.invalidate();
	}
	public ConferenceUser isOverLapping(int x, int y){
		ArrayList<ConferenceUser> list = conferenceRoom.getCUserList();
		for(ConferenceUser cu : list){
			if(!(cu.getUser().compareTo(UserManager.PRIMARY_USER) == 0)&& isOverlapping(new Point(x, y), cu.getLocation())){
				
				return cu;
			}
		}
		return null;
	}
	public boolean isOverlapping(Point a, Point b){
		int imageWidth = 76;
		int imageHeight = 76;
		Rect rectA = new Rect(a.x, a.y, a.x+imageWidth,a.y+imageHeight);
		Rect rectB = new Rect(b.x, b.y, b.x+imageWidth,b.y+imageHeight);
		return rectA.intersect(rectB);
	}
	public class UserTouchListner implements OnTouchListener,OnLongClickListener {
		/**
		 * 
		 */
		private final int START_DRAGGING = 0;
		/**
		 * 
		 */
		private final int STOP_DRAGGING = 1;
		
		/**
		 * 
		 */
		private final int DRAGGING = 2;
		/**
		 * 
		 */
		private int status =STOP_DRAGGING	;
		/**
		 * 
		 */
		ImageView dittoUser = null ;
		
		Animation a = AnimationUtils.loadAnimation(context, R.anim.set);

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
		 * android.view.MotionEvent)
		 */
		private LayoutParams params;
		public boolean onTouch(View v, MotionEvent event) {
			Log.d("UserTouchListner", "Status : "+status);
			Log.d("UserTouchListner", "Action : "+event.getAction());
			Bitmap b = ((UserView)v).getImage();
			int relativeX = v.getLeft();
			int relativeY = v.getTop();
			int absoluteX = (int) (event.getX()+relativeX);
			int absoluteY= (int) (event.getY()+relativeY);
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d("UserTouchListner:", "ACTION_DOWN");
				status = START_DRAGGING;
				
			}
			else if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d("UserTouchListner", "ACTION_UP");
				status = STOP_DRAGGING;
				((ViewGroup)roomLayout).removeView(dittoUser);
				if(isOnEdge(new Point(absoluteX, absoluteY))!= -1){
					String s =(isOnEdge(new Point(absoluteX, absoluteY))==0)?"Left Room":"Right Room";
					//TODO : Need to show gradient
//					if (s.equals("Left Room")){						
//						a.reset(); 
//						left_gradient.clearAnimation(); 
//						left_gradient.startAnimation(a);
//						
//					}
//					else if(s.equals("Right Room")){
//						a.reset(); 
//						right_gradient.clearAnimation(); 
//						right_gradient.startAnimation(a);
//					}
					//Send invitation using conf room/muc
					Toast.makeText(context, "Send Invitation:"+s, Toast.LENGTH_SHORT).show();
				}
				else {
					ConferenceUser cuoverlap = isOverLapping(absoluteX, absoluteY);
					if (cuoverlap != null) {
						ConferenceUser oldUser = ((UserView) v).getCUser();
						Point oldLocation = oldUser.getLocation();
						oldUser.setLocation(cuoverlap.getLocation());
						cuoverlap.setLocation(oldLocation);
					}
				}
				
				((UserView)v).setImageBitmap(b);
				v.invalidate();
				Log.i("Drag", "Stopped Dragging");
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d("UserTouchListner", "ACTION_MOVE");
				if(status == START_DRAGGING){
					status =DRAGGING;
					params = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					dittoUser =  new 	ImageView(context);
					dittoUser.setImageBitmap(b);
					dittoUser.setPadding(absoluteX,absoluteY, 0, 0);
					((UserView)v).setImageBitmap(null); 
					((UserView)v).setBackgroundResource(R.drawable.greybox);	
					((ViewGroup)roomLayout).addView(dittoUser, params);
					
				}
				else if ( status == DRAGGING ) {
					status = DRAGGING;
					dittoUser.setPadding(absoluteX, absoluteY, 0, 0);
					dittoUser.invalidate();
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_CANCEL){
				if(dittoUser != null ){
					status = STOP_DRAGGING;
					((ViewGroup)roomLayout).removeView(dittoUser);
					((UserView)v).setImageBitmap(b);
					v.invalidate();
				}
			}
			return false;
		}
		public boolean onLongClick(View v) {
			if(status != DRAGGING){
				RelativeLayout bottom_bar_user = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_user_action);
				RelativeLayout bottom_bar_conference = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_conference_action);
				RelativeLayout bottom_bar_conference_moderator = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_conference_action_moderator);
				RelativeLayout action_bar = (RelativeLayout) roomLayout.findViewById(R.id.action_bar);
				bottom_bar_user.setVisibility(View.VISIBLE);
				action_bar.setVisibility(View.VISIBLE);
				bottom_bar_conference.setVisibility(View.INVISIBLE);
				bottom_bar_conference_moderator.setVisibility(View.INVISIBLE);
			}
			return false;
		}
		private int isOnEdge(Point location){
				if (location.x + 76 >= roomLayout.getWidth()){
					return 1; 
				}
				else if (location.x <= 0){
					return 0; 
				}
				else return -1; 
			}
	}
}
