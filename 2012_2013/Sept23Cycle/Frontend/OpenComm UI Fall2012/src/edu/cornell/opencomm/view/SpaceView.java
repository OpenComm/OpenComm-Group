package edu.cornell.opencomm.view;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.controller.SpaceViewController;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.util.Values;

/* A SpaceView is the graphical representation of a space, aka the screen you see on the monitor
 * showing all the icons of the people in the space (above the privatespace bar).
 * This view does not include the Main, Menu, Trash buttons, or bottom PrivateSpace bar.
 * Your icon will not show up on screen */

public class SpaceView extends View {
	private static String LOG_TAG = "OC_SpaceView"; // for error checking
	private Context context;
	/** The space that this SpaceViwe is currently representing */
	Space space;
	Bitmap voice_image;

	/** Temporary variables used in the onTouchEventMethods */
	public UserView selectedIcon;
	boolean clickOnIcon = false;
	private boolean dim = false;
	private boolean drag = true;
	private boolean lassomode = false;
	private boolean menuPopup = false;
	private Point savedPoint = null;
	private ArrayList<ArrayList<Point>> dragPoints = new ArrayList<ArrayList<Point>>();
	private ArrayList<UserView> lassoedIcons = new ArrayList<UserView>();

	private boolean D = Values.D;

	private double INSENSITIVITY = 50.0;

	/**
	 * Constructor: This one is used by the XML file to automatically generate a
	 * SpaceView
	 */
	public SpaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setupControllers();
		setupImage();
		setupEventListeners();
	}

	/**
	 * Constructor: Create a screen for a NEW privatespace that is empty (except
	 * for you) Initialize all variables
	 */
	public SpaceView(Context context, Space space) {
		super(context);
		this.context = context;
		this.space = space;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setupControllers();
		setupImage();
		setupEventListeners();
	}

	/** Initialize all Controllers for this SpaceView */
	public void setupControllers() {
	}

	/**
	 * Setup all Event Listeners for this SpaceView: onTouchListener and
	 * onLongClickListener
	 */
	public void setupEventListeners() {
		// The onTouch Listener (responds to any touch events)
	}

	public void cancelLassoMode() {
	}

	private void performGhostDrag(int mouseX, int mouseY) {
	}

	private double distance(Point p1, Point p2) {
		double diffX = p1.x - p2.x;
		double diffY = p1.y - p2.y;
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}

	private synchronized void createLasso(View view, MotionEvent event) {
	}

	/** Add the image of the voice coming from you */
	public void setupImage() {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		voice_image = BitmapFactory.decodeResource(this.getResources(),
				Values.voice_image);
	}

	/**
	 * Manually set the space that this SpaceView corresponds to (used in
	 * conjunction with the constructor for the XMl file
	 */
	public void setSpace(Space space) {
		this.space = space;
	}

	/*
	 * Draw everything on the screen including: 1) voice image 2) All icons of
	 * people in this space
	 */
	@Override
	protected void onDraw(Canvas canvas) {
	}

	// GETTERS

	/** Get the main background color */
	private int getColor() {
		//TODO: Ankit What to use
		return R.color.off_white;
	}

	/** Get the list of all Icons shown in this spaceview */
	public LinkedList<UserView> getAllIcons() {
		return space.getAllIcons();
	}

	/** Get the Space that this SpaceView is representing */
	public Space getSpace() {
		return space;
	}
	//TODO : Ankit
	/** Get the MainApplication object */
	public ConferenceView getActivity() {
		return (ConferenceView) context;
	}

	/** Get the SpaceViewController */
	public SpaceViewController getSpaceViewController() {
		return null;
	}
}
