package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.view.WindowManager;
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
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceRoom;
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
	private String TAG = ConferenceRoom.class.getName();
	public Conference conferenceRoom;
	boolean DEBUG = true;
	public ArrayList<UserView> userViews = new ArrayList<UserView>();
	public Context context;
	public static final int radius = 165;
	private ImageView left_gradient;
	private ImageView right_gradient;

	@SuppressLint("ValidFragment")
	public ConferenceRoomFragment(Context context, int layoutId,
			Conference conferenceModel) {
		this.layoutId = layoutId;
		this.context = context;
		this.conferenceRoom = conferenceModel;
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
		// left_gradient = (ImageView)
		// ((ViewGroup)roomLayout).findViewById(R.id.leftsidechatgradient);
		// right_gradient = (ImageView)
		// ((ViewGroup)roomLayout).findViewById(R.id.rightsidechatgradient);

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
	 * 
	 * @param userView
	 */
	public void addUserView(UserView userView) {
		userViews.add(userView);
	}

	/**
	 * Remove a given userView from this roomFragment's list of UserViews
	 * 
	 * @param userView
	 */
	public void removeUserView(UserView userView) {
		userViews.remove(userView);
	}

	public ArrayList<UserView> getUserView() {
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

				UserView uv = new UserView(context);
				// Ankit: Make a primary equal to check
				if (u.compareTo(UserManager.PRIMARY_USER) == 0) {
					uv.setOnClickListener(new OnClickListener() {
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

	public void displayInvitationBar() {
		RelativeLayout invitationBar = (RelativeLayout) roomLayout
				.findViewById(R.id.side_chat_invitation_bar);
		invitationBar.setVisibility(View.INVISIBLE);
		invitationBar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				v.invalidate();
			}
		});
		invitationBar.invalidate();
	}

	public User isOverLapping(int x, int y) {
		Collection<User> list = conferenceRoom.getParticipants().values();
		for (User u : list) {
			if (!(u.compareTo(UserManager.PRIMARY_USER) == 0)
					&& isOverlapping(new Point(x, y), u.getLocation())) {

				return u;
			}
		}
		return null;
	}

	public boolean isOverlapping(Point a, Point b) {
		int imageWidth = 76;
		int imageHeight = 76;
		Rect rectA = new Rect(a.x, a.y, a.x + imageWidth, a.y + imageHeight);
		Rect rectB = new Rect(b.x, b.y, b.x + imageWidth, b.y + imageHeight);
		return rectA.intersect(rectB);
	}

	public class UserTouchListner implements OnTouchListener,
			OnLongClickListener {
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

		public boolean onTouch(View v, MotionEvent event) {
			Log.d("UserTouchListner", "Status : " + status);
			Log.d("UserTouchListner", "Action : " + event.getAction());
			Bitmap b = ((UserView) v).getImage();
			int relativeX = v.getLeft();
			int relativeY = v.getTop();
			int absoluteX = (int) (event.getX() + relativeX);
			int absoluteY = (int) (event.getY() + relativeY);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Log.d("UserTouchListner:", "ACTION_DOWN");
				status = START_DRAGGING;

			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d("UserTouchListner", "ACTION_UP");
				status = STOP_DRAGGING;
				((ViewGroup) roomLayout).removeView(dittoUser);
				if (isOnEdge(new Point(absoluteX, absoluteY)) != -1) {
					String s = (isOnEdge(new Point(absoluteX, absoluteY)) == 0) ? "Left Room"
							: "Right Room";
					// TODO : Need to show gradient
					// if (s.equals("Left Room")){
					// a.reset();
					// left_gradient.clearAnimation();
					// left_gradient.startAnimation(a);
					//
					// }
					// else if(s.equals("Right Room")){
					// a.reset();
					// right_gradient.clearAnimation();
					// right_gradient.startAnimation(a);
					// }
					// Send invitation using conf room/muc
					Toast.makeText(context, "Send Invitation:" + s,
							Toast.LENGTH_SHORT).show();
				} else {
					/*
					 * ConferenceUser cuoverlap = isOverLapping(absoluteX,
					 * absoluteY); if (cuoverlap != null) { ConferenceUser
					 * oldUser = ((UserView) v).getCUser(); Point oldLocation =
					 * oldUser.getLocation();
					 * oldUser.setLocation(cuoverlap.getLocation());
					 * cuoverlap.setLocation(oldLocation); }
					 */
				}

				((UserView) v).setImageBitmap(b);
				v.invalidate();
				Log.i("Drag", "Stopped Dragging");
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				Log.d("UserTouchListner", "ACTION_MOVE");
				if (status == START_DRAGGING) {
					status = DRAGGING;
					params = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					dittoUser = new ImageView(context);
					dittoUser.setImageBitmap(b);
					dittoUser.setPadding(absoluteX, absoluteY, 0, 0);
					((UserView) v).setImageBitmap(null);
					// ((UserView)v).setBackgroundResource(R.drawable.greybox);
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

		public boolean onLongClick(View v) {
			if (status != DRAGGING) {
				RelativeLayout bottom_bar_user = (RelativeLayout) roomLayout
						.findViewById(R.id.bottom_bar_user_action);
				RelativeLayout bottom_bar_conference = (RelativeLayout) roomLayout
						.findViewById(R.id.bottom_bar_conference_action);
				RelativeLayout bottom_bar_conference_moderator = (RelativeLayout) roomLayout
						.findViewById(R.id.bottom_bar_conference_action_moderator);
				RelativeLayout action_bar = (RelativeLayout) roomLayout
						.findViewById(R.id.action_bar);
				bottom_bar_user.setVisibility(View.VISIBLE);
				action_bar.setVisibility(View.VISIBLE);
				bottom_bar_conference.setVisibility(View.INVISIBLE);
				bottom_bar_conference_moderator.setVisibility(View.INVISIBLE);
			}
			return false;
		}

		private int isOnEdge(Point location) {
			if (location.x + 76 >= roomLayout.getWidth()) {
				return 1;
			} else if (location.x <= 0) {
				return 0;
			} else
				return -1;
		}
	}
}
