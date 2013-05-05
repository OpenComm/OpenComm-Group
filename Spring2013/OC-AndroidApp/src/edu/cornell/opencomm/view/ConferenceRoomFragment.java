package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jivesoftware.smackx.muc.MultiUserChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.User;

/**
 * @author Spandana Govindgari [frontend], Ankit Singh[frontend],Nora
 *         NQ[frontend]
 * 
 */
@SuppressLint("ValidFragment")
public class ConferenceRoomFragment extends Fragment {
	// TODO : This class to be cleaned up once integrated with BE
	private View roomLayout;
	public int layoutId = R.layout.conference_main_room;
	private String TAG = MultiUserChat.class.getName();
	public Conference conferenceRoom;
	// public Conference_Dummy conferenceRoom;
	boolean DEBUG = true;
	public HashMap<User, UserView> userViews = new HashMap<User, UserView>();
	public Context context;
	public static final int radius = 165;

	public ConferenceRoomFragment() {
	}

	@SuppressLint("ValidFragment")
	public ConferenceRoomFragment(Context context, int layoutId,
			Conference conferenceModel) {
		this.layoutId = layoutId;
		this.context = context;
		this.conferenceRoom = conferenceModel;
		Log.d(TAG, "layoutId :" + layoutId);
		Log.d(TAG, "Conference Room :" + conferenceRoom);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		if (container == null) {
			return null;
		}
		this.roomLayout = inflater.inflate(layoutId, container, false);
		ViewTreeObserver observer = roomLayout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
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
	 * Remove a given userView from this roomFragment's list of UserViews
	 * 
	 * @param userView
	 */
	public void removeUserView(UserView userView) {
		userViews.remove(userView);
	}

	public HashMap<User, UserView> getUserView() {
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
		WindowManager wm = (WindowManager) roomLayout.getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenWidth = display.getWidth();
		float radius = screenWidth * 3 / 8;
		if (conferenceRoom != null) {
			Point center = new Point(roomLayout.getWidth() / 2,
					roomLayout.getHeight() / 2);
			ArrayList<User> userList = conferenceRoom.updateLocations(center,
					(int) radius);
			for (User u : userList) {

				UserView uv = new UserView(context, u);
				userViews.put(u, uv); 
				// Ankit: Make a primary equal to check
				if (u.compareTo(UserManager.PRIMARY_USER) == 0) {
					uv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							UserView u = (UserView) v;
							u.setBackgroundColor(Color.BLACK);
							u.invalidate();
						}
					});
				} else {
					ConferenceRoomFragment.UserTouchListner listener = new ConferenceRoomFragment.UserTouchListner();
					uv.setOnTouchListener(listener);
					uv.setOnLongClickListener(listener);
				}
				((ViewGroup) roomLayout).addView(uv);
			}
		}

	}

	public void addUser(User u, Conference conferenceModel){
		conferenceRoom = conferenceModel; 
		this.conferenceRoom.getUsers().add(u); 
		Log.i("adding users", "" + conferenceRoom.getUsers().size()); 
		this.createTheCirleOfTrust(); 
	}



	public class UserTouchListner implements OnTouchListener,
	OnLongClickListener {
		private ArrayList<User> confUsers = conferenceRoom.getUsers();  
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
		private int status = STOP_DRAGGING;
		/**
		 * 
		 */
		ImageView dittoUser = null;

		Animation a = AnimationUtils.loadAnimation(context, R.anim.set);

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
		 * android.view.MotionEvent)
		 */
		private LayoutParams params;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d("UserTouchListner", "Status : " + status);
			Log.d("UserTouchListner", "Action : " + event.getAction());
			Bitmap b = ((UserView) v).getImage();
			Log.i("Who is this?", ((UserView) v).getUser().getUsername()); 
			double relativeX = v.getLeft() - v.getWidth() / 2;
			double relativeY = v.getTop() - v.getHeight() / 2;

			int absoluteX = (int) (event.getX() + relativeX);
			int absoluteY = (int) (event.getY() + relativeY);
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d("UserTouchListner:", "ACTION_DOWN");
				status = START_DRAGGING;

			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d("UserTouchListner", "ACTION_UP");
				status = STOP_DRAGGING;
				((ViewGroup) roomLayout).removeView(dittoUser);
				User cuoverlap = this.isOverLapping(absoluteX,absoluteY); 
				if (cuoverlap != null) { 
					User oldUser = ((UserView) v).getUser();
					Point oldLocation = oldUser.getLocation();
					((ViewGroup) roomLayout).removeView(dittoUser);
					Point newLoc = cuoverlap.getLocation(); 
					//change the array conference room users 
					int h = 0, j = 0;  
					for (int i= 0; i < confUsers.size(); i++){
						User o = confUsers.get(i); 
						if (oldUser.compareTo(o) == 0){
							h = i; 
							cuoverlap.setLocation(oldLocation);    
						}
						if (cuoverlap.compareTo(o) == 0){
							j= i; 
							oldUser.setLocation(newLoc);  
						}
					}
					Log.i("h and h", ""+h+ " "+ j); 
					Collections.swap(confUsers, h, j);
					Bitmap old = ((UserView) v).getImage(); 
					((UserView) v).setImageBitmap(((UserView) v).getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),
							cuoverlap.getImage()), cuoverlap));   
					UserView dummy = userViews.get(cuoverlap);
					dummy.setImageBitmap(dummy.getRoundedCornerBitmap(old, oldUser));
					((UserView) v).setUser(cuoverlap); 
					dummy.setUser(oldUser); 
					userViews.put(dummy.getUser(), dummy); 
					userViews.put(cuoverlap, ((UserView)v));
					for (User u: confUsers){
						Log.i("user list", u.getUsername()); 
					}
					Log.i("Drag", "Stopped Dragging");
				}	
				else{
					((UserView) v).setImageBitmap(b);
					((UserView) v).invalidate();
				}
				v.invalidate();
				Log.i("Drag", "Stopped Dragging");
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d("UserTouchListner", "ACTION_MOVE");
				if (status == START_DRAGGING) {
					status = DRAGGING;
					params = new LayoutParams(
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					dittoUser = new ImageView(context);
					dittoUser.setImageBitmap(b);
					dittoUser.setPadding(absoluteX, absoluteY, 0, 0);
					((UserView) v).setImageBitmap(null);
					((ViewGroup) roomLayout).addView(dittoUser, params);

				} else if (status == DRAGGING) {
					status = DRAGGING;
					dittoUser.setPadding(absoluteX, absoluteY, 0, 0);
					dittoUser.invalidate();
				}
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				if (dittoUser != null) {
					status = STOP_DRAGGING;
					((ViewGroup) roomLayout).removeView(dittoUser);
					((UserView) v).setImageBitmap(b);
					v.invalidate();
				}
			}
			return false;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			return true;
		}

		public User isOverLapping(int x, int y) {
			for (User u: confUsers){
				Log.i("user list 1", u.getUsername() + u.getX()); 
			}
			for (User u : confUsers) {
				if (!(u.compareTo(UserManager.PRIMARY_USER) == 0)
						&& isOverlapping(new Point(x, y), u.getLocation())) {
					return u;
				}
			}
			return null;
		}

		public boolean isOverlapping(Point a, Point b) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			float screenWidth = display.getWidth();
			float userPicSize = screenWidth * 13/48;		
			int imageWidth = (int) userPicSize;
			int imageHeight = (int) userPicSize;
			Rect rectA = new Rect(a.x, a.y, a.x + imageWidth, a.y + imageHeight);
			Rect rectB = new Rect(b.x, b.y, b.x + imageWidth, b.y + imageHeight);
			return rectA.intersect(rectB);
		}
	}
}
