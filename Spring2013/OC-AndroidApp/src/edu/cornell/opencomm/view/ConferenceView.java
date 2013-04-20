package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.ConferenceController;
import edu.cornell.opencomm.manager.UserManager;
import edu.cornell.opencomm.model.Conference;
import edu.cornell.opencomm.model.ConferenceConstants;
import edu.cornell.opencomm.model.Conference_Dummy;
import edu.cornell.opencomm.model.User;

/**
 * 
 * @author  Spandana Govindgari [frontend], Ankit Singh[frontend],Nora NQ[frontend]
 * 
 */
public final class ConferenceView extends FragmentActivity implements
ConferenceConstants, ViewPager.OnPageChangeListener {

	private static String TAG = ConferenceView.class.getName();

	/**
	 * The conference data model
	 */
	private Conference conferenceModel;
//	private Conference_Dummy conferenceModel;

	/**
	 * The conference controller
	 */
	private ConferenceController conferenceController;

	/**
	 * The conference pager adaptor
	 */
	private ConferencePageAdapter mPagerAdapter;

	public Context context; 

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */


	private static ConferenceView _instance = null;

	private WakeLock mWakeLock;
	private View roomLayout;

	/*
	 *controls on this view
	 */
	public TextView txtv_ConfTitle;

	public static ConferenceView getInstance() {
		if (_instance == null) {
			_instance = new ConferenceView();
		}
		return _instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		// Bind the conference view/activity to the layout
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conference_layout);
		//instantiate the handlers
		txtv_ConfTitle = (TextView) findViewById(R.id.confernecev2_title);

		roomLayout = (ViewGroup) findViewById(R.layout.conference_v2);
		System.out.println("1");
		Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
		System.out.println("2");
		conferenceController = ConferenceController.getInstance(); 
		context = this; 
		
		conferenceModel = conferenceController.getRoom();
		
//		conferenceModel = conferenceController.getCurrentConference(); 
		// Bind the DataModel(s)
		// Get the main room id from the intent
		initPager();
		//this.createTheCirleOfTrust(); 
		System.out.println("3"); 

	}

	private void initPager() {
		List<Fragment> fragments = createFragments();
		mPagerAdapter = new ConferencePageAdapter(
				super.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) super.findViewById(R.id.threepanelpager);
		pager.setAdapter(this.mPagerAdapter);
		pager.setOnPageChangeListener(this);
	}

	private List<Fragment> createFragments(){
		List<Fragment> conferenceFragments = new Vector<Fragment>();
		//conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(LEFT_ROOM_INDEX)));
		conferenceFragments.add(new ConferenceRoomFragment(this, MAIN_ROOM_LAYOUT, conferenceModel));
		//conferenceFragments.add(new ConferenceRoomFragment(this, SIDE_ROOM_LAYOUT, conferenceModel.getRoomByTag(RIGHT_ROOM_INDEX)));
		return conferenceFragments;
	}

	/**
	 * Remove the corresponding UserView for each given ConferenceUser and add to the ConferenceRoomFragment
	 * @param users
	 * @param roomView
	 */
	public void removeUserViewsFromConference(ConferenceRoomFragment roomView, ArrayList<UserView> userViews){
		for (UserView userView : userViews){
			roomView.removeUserView(userView);
		}
	}

	/**
	 * Exit the conference and return to the conference card page for this conference
	 */
	public void exitConference(){
		Log.d("MODERATOR", "exitConference");
		Intent i = new Intent(this, DashboardView.class);
		onStop();
		startActivity(i);
	}

	/**
	 * Switch the view back to the page with the given index
	 * @param roomIndex
	 */
	public void returnToPage(int roomIndex){
		ViewPager pager = (ViewPager) super.findViewById(R.id.threepanelpager);
		pager.invalidate();
	}

	/* [TODO]: check to see if this should be onPause() */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	//triggered when add person button was pressed (on confernece_v2)
	public void addPersonClicked(View v)
	{
		//TODO: get username of person to be added (hardcoded for now)
		Log.v(TAG, "trying to add oc4testorg@cuopencomm");
		ConferenceController.getInstance().HandleAddPerson("oc4testorg@cuopencomm");
	}


	//triggered when overflow button was pressed (on confernece_v2)
	public void overflowButtonClicked(View v)
	{
		conferenceController.HandleOverflow();
	}


	//triggered when back button was pressed (on confernece_v2)
	public void backButtonClicked(View v)
	{
		conferenceController.HandleBackButton();
	}


	//change the title of the conference ("CONFERENCE NAME" by default)
	public void renameConference(String title)
	{
		conferenceController.setTitle(title);
	}



	// Context Bar methods

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}
	int currentPageNumber = 1;
	@Override
	public void onPageSelected(int roomNumber) {
		Log.d(TAG, "Room Number selected :" + roomNumber);
		currentPageNumber = roomNumber;
		mPagerAdapter.getItem(roomNumber);

		//		fragment.createUsers();
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onStop() {
		conferenceModel = null;
		conferenceController = null;
		mPagerAdapter = null; 
		super.onStop();
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int eventType = event.getKeyCode();
		boolean returnType = true;
		switch (eventType) {
		case KeyEvent.KEYCODE_I:
			System.out.println("ConferenceView.dispatchKeyEvent()");
			ConferenceRoomFragment fragment = (ConferenceRoomFragment) mPagerAdapter.getItem(currentPageNumber);
			fragment.displayInvitationBar();
			break;
		case KeyEvent.KEYCODE_J:
			//Someone joins the room
			break;
		case KeyEvent.KEYCODE_E:
			//Mod ends the conference
			break;
		case KeyEvent.KEYCODE_M:
			//When you get mode priv
			break;
		case KeyEvent.KEYCODE_K:
			//when someone gets 
			break;

		default:
			returnType = super.dispatchKeyEvent(event);
			break;
		}
		return returnType;
	}

//	public void createTheCirleOfTrust() {
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//		int screenWidth = display.getWidth();
//		float radius = screenWidth * 3/8;
//		Point center = new Point(screenWidth/2, screenWidth/2);
//		ArrayList<User> userList = conferenceController.updateLocations(center, (int)radius);
//		for (User confUser : userList) {
//
//			UserView uv = new UserView(this, confUser);
//			//Ankit: Make a primary equal to check
//			if(confUser.compareTo(UserManager.PRIMARY_USER) == 0){
//				uv.setOnClickListener(new OnClickListener() {
//					//Ankit: Bad coding
//					boolean set = false;
//					public void onClick(View v) {
//						UserView u = (UserView) v;
//						if(set){
//							u.setBackgroundColor(Color.BLACK);
//						}else{
//							u.setBackgroundColor(-1);
//						}
//						set = !set;
//						u.invalidate();
//					}
//				});
//			}else{
//				UserTouchListner listener = new UserTouchListner();
//				uv.setOnTouchListener(listener);
//				uv.setOnLongClickListener(listener);
//			}
//			((ViewGroup) roomLayout).addView(uv);
//		}
//
//	}
//
//	public User isOverLapping(int x, int y){
//		ArrayList<User> list = conferenceController.getCUserList();
//		for(User cu : list){
//			if(!(cu.compareTo(UserManager.PRIMARY_USER) == 0)&& isOverlapping(new Point(x, y), cu.getLocation())){
//
//				return cu;
//			}
//		}
//		return null;
//	}
//
//
//	public boolean isOverlapping(Point a, Point b){
//		int imageWidth = 76;
//		int imageHeight = 76;
//		Rect rectA = new Rect(a.x, a.y, a.x+imageWidth,a.y+imageHeight);
//		Rect rectB = new Rect(b.x, b.y, b.x+imageWidth,b.y+imageHeight);
//		return rectA.intersect(rectB);
//	}
//
//
//
//	public class UserTouchListner implements OnTouchListener,OnLongClickListener {
//		/**
//		 * 
//		 */
//		private final int START_DRAGGING = 0;
//		/**
//		 * 
//		 */
//		private final int STOP_DRAGGING = 1;
//
//		/**
//		 * 
//		 */
//		private final int DRAGGING = 2;
//		/**
//		 * 
//		 */
//		private int status =STOP_DRAGGING	;
//		/**
//		 * 
//		 */
//		ImageView dittoUser = null ;
//
//		Animation a = AnimationUtils.loadAnimation(context, R.anim.set);
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
//		 * android.view.MotionEvent)
//		 */
//		private LayoutParams params;
//		public boolean onTouch(View v, MotionEvent event) {
//			Log.d("UserTouchListner", "Status : "+status);
//			Log.d("UserTouchListner", "Action : "+event.getAction());
//			Bitmap b = ((UserView)v).getImage();
//			int relativeX = v.getLeft();
//			int relativeY = v.getTop();
//			int absoluteX = (int) (event.getX()+relativeX);
//			int absoluteY= (int) (event.getY()+relativeY);
//
//			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				Log.d("UserTouchListner:", "ACTION_DOWN");
//				status = START_DRAGGING;
//
//			}
//			else if (event.getAction() == MotionEvent.ACTION_UP) {
//				Log.d("UserTouchListner", "ACTION_UP");
//				status = STOP_DRAGGING;
//				((ViewGroup)roomLayout).removeView(dittoUser);
//				if(isOnEdge(new Point(absoluteX, absoluteY))!= -1){
//					String s =(isOnEdge(new Point(absoluteX, absoluteY))==0)?"Left Room":"Right Room";
//					//TODO : Need to show gradient
//					//			 if (s.equals("Left Room")){	
//					//			 a.reset(); 
//					//			 left_gradient.clearAnimation(); 
//					//			 left_gradient.startAnimation(a);
//					//	
//					//			 }
//					//			 else if(s.equals("Right Room")){
//					//			 a.reset(); 
//					//			 right_gradient.clearAnimation(); 
//					//			 right_gradient.startAnimation(a);
//					//			 }
//					//Send invitation using conf room/muc
//					Toast.makeText(context, "Send Invitation:"+s, Toast.LENGTH_SHORT).show();
//				}
//				else {
//					User cuoverlap = isOverLapping(absoluteX, absoluteY);
//					if (cuoverlap != null) {
//						User oldUser = ((UserView) v).getUser();
//						Point oldLocation = oldUser.getLocation();
//						oldUser.setLocation(cuoverlap.getLocation());
//						cuoverlap.setLocation(oldLocation);
//					}
//				}
//
//				((UserView)v).setImageBitmap(b);
//				v.invalidate();
//				Log.i("Drag", "Stopped Dragging");
//			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//				Log.d("UserTouchListner", "ACTION_MOVE");
//				if(status == START_DRAGGING){
//					status =DRAGGING;
//					params = new LayoutParams(LayoutParams.WRAP_CONTENT,
//							LayoutParams.WRAP_CONTENT);
//					dittoUser = new ImageView(context);
//					dittoUser.setImageBitmap(b);
//					dittoUser.setPadding(absoluteX,absoluteY, 0, 0);
//					((UserView)v).setImageBitmap(null); 
//					//((UserView)v).setBackgroundResource(R.drawable.greybox);	
//					((ViewGroup)roomLayout).addView(dittoUser, params);
//
//				}
//				else if ( status == DRAGGING ) {
//					status = DRAGGING;
//					dittoUser.setPadding(absoluteX, absoluteY, 0, 0);
//					dittoUser.invalidate();
//				}
//			}
//			else if(event.getAction() == MotionEvent.ACTION_CANCEL){
//				if(dittoUser != null ){
//					status = STOP_DRAGGING;
//					((ViewGroup)roomLayout).removeView(dittoUser);
//					((UserView)v).setImageBitmap(b);
//					v.invalidate();
//				}
//			}
//			return false;
//		}
//		public boolean onLongClick(View v) {
//			if(status != DRAGGING){
//				RelativeLayout bottom_bar_user = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_user_action);
//				RelativeLayout bottom_bar_conference = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_conference_action);
//				RelativeLayout bottom_bar_conference_moderator = (RelativeLayout) roomLayout.findViewById(R.id.bottom_bar_conference_action_moderator);
//				RelativeLayout action_bar = (RelativeLayout) roomLayout.findViewById(R.id.action_bar);
//				bottom_bar_user.setVisibility(View.VISIBLE);
//				action_bar.setVisibility(View.VISIBLE);
//				bottom_bar_conference.setVisibility(View.INVISIBLE);
//				bottom_bar_conference_moderator.setVisibility(View.INVISIBLE);
//			}
//			return false;
//		}
//		private int isOnEdge(Point location){
//			if (location.x + 76 >= roomLayout.getWidth()){
//				return 1; 
//			}
//			else if (location.x <= 0){
//				return 0; 
//			}
//			else return -1; 
//		}
//	}
//




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

}
