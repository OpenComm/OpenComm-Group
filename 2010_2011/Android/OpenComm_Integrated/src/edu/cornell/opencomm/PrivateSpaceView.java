package edu.cornell.opencomm;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class PrivateSpaceView extends ImageButton {

	Context context;
	PrivateSpace space;

	/** All the private spaces open in the app */
	public static LinkedList<PrivateSpaceView> currentSpaces = new LinkedList<PrivateSpaceView>();

	/** Colors private spaces can have */
	public static int[] COLORS = { Color.BLUE, Color.YELLOW, Color.GREEN,
			Color.MAGENTA, Color.CYAN, Color.DKGRAY };

	/** Number to assign to new private spaces */
	public static int privateSpaceCounter = 0;

	/** List of people currently in this private space */
	// protected LinkedList<Person> peopleInSpace = new LinkedList<Person>();

	private int spaceId = -1;
	private int color = Color.BLUE;

	protected boolean isSelected = false;
	// protected boolean isHovered = false;
	public View.OnTouchListener ontouchlistener;
	public boolean clickedOnce = false;
	// PreviewView preview;
	boolean showPreview = false;
	
	public PrivateSpaceView clone(){
		PrivateSpace ps = this.space.clone();
		PrivateSpaceView psv = new PrivateSpaceView(context,ps);		
		psv.spaceId = this.spaceId;
		psv.color = this.color;
		init2();
		
		return psv;
	}

	public PrivateSpaceView(Context context, AttributeSet attrs, int defStyle,
			Space parent) {
		super(context, attrs, defStyle);
		this.context = context;
		this.space = (PrivateSpace) parent;
		init();
		init2();
	}

	public PrivateSpaceView(Context context, AttributeSet attrs, Space parent) {
		super(context, attrs);
		this.context = context;
		this.space = (PrivateSpace) parent;
		init();
		init2();
	}

	public PrivateSpaceView(Context context, Space parent) {
		super(context);
		this.context = context;
		this.space = (PrivateSpace) parent;
		init();
		init2();
	}

	/**
	 * Initialize the object's spaceId, color, and add it to the list of all
	 * spaces
	 */
	private final synchronized void init() {
		this.spaceId = PrivateSpaceView.privateSpaceCounter++;
		this.color = PrivateSpaceView.COLORS[spaceId
				% PrivateSpaceView.COLORS.length];
	}
	
	private final void init2(){
		this.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View view, MotionEvent evt) {
				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					toggle(view);
					if (!clickedOnce) {
						clickedOnce = true;
						MainApplication.showPreview(space.people);

					} else if (clickedOnce) {
						openPrivateSpace(); // start new privateSpace activity!
											// (either create new or restart)
						clickedOnce = false;
						MainApplication.showPreview(null);
					}
					break;
				}
				return false;
			}
		});
		invalidate();
	}

	/**
	 * Change state of selected variable, make sure only one space is selected
	 * at a time ***This part may need to be moved to the MainChat class***
	 * 
	 * @param arg0
	 */
	protected synchronized void toggle(View arg0) {
		this.isSelected = !isSelected;
		if (isSelected) { // make all others not selected
			for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
				if (p.equals(this))
					continue;
				p.isSelected = false;
			}
		}
		this.invalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		super.onDraw(canvas); // Draw the regular stuff

		// Draw in 3 steps:
		// 1. Draw background to erase old state
		// 2. Draw this private space's color
		// 3. Draw smaller square if the private space isn't open or being
		// previewed //
		int backgroundColor = R.color.scroll_background;
		RectShape rect = new RectShape();
		ShapeDrawable normalShape = new ShapeDrawable(rect);
		normalShape.getPaint().setColor(color);
		normalShape.setBounds(2, 2, this.getWidth() - 2, this.getHeight() - 2);
		canvas.drawColor(backgroundColor);
		normalShape.draw(canvas);
		RectShape rect2 = new RectShape();
		if (!this.isSelected) {
			ShapeDrawable s = new ShapeDrawable(rect2);
			s.getPaint().setColor(backgroundColor);
			s.setBounds(6, 6, this.getHeight() - 4, this.getHeight() - 6);
			s.draw(canvas);
			showPreview = false;

		} else
			showPreview = true;
	}

	public void openPrivateSpace() {
		PrivateSpace p = (PrivateSpace) space;
		// activity has not been created for this PrivateSpace yet
		if (p.getActivity() == null)
			((MainApplication) context).openNewPSActivity(p);
		// if activity has already been created for this, then restart it
		else if (p.getActivity() != null) {
			((MainApplication) context).restartPSActivity(p);
		}
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		invalidate();
	}

	// Nora added isHovered, will work with it more
	/*
	 * public boolean isHovered() { return isHovered; }
	 */

	// If you hover over an Private Space icon with your person icon, then only
	// highlight this icon and turn off everybody else's
	public void setHovered(boolean isHovered) {
		if (isHovered) {
			// setSelected(true);
			for (PrivateSpaceView p : currentSpaces) {
				if (!p.equals(this))
					p.setSelected(false);
				else
					p.setSelected(true);
			}
		} else
			setSelected(false);
		invalidate();
	}

	/**
	 * Returns true if the coordinates given are within this view
	 * 
	 * @param y
	 * @param x
	 * @return
	 */
	public boolean contains(int x, int y) {
		int[] location = new int[2];
		this.getLocationOnScreen(location);
		if (!this.isShown())
			return false;
		return (x > location[0] && x < location[0] + this.getWidth()
				&& y > location[1] - this.getHeight() && y < location[1]
				+ this.getHeight());
	}

	public Space getSpace() {
		return space;
	}

	/**
	 * Get the private space with a particular muc room name number
	 * @param mucName
	 * 		name of the MUC room
	 * @return private space or null if it doesn't exist
	 */
	public static PrivateSpaceView getSpaceByID(String mucName) {
		PrivateSpaceView found = null;
		for(PrivateSpaceView pv: PrivateSpaceView.currentSpaces){
			if ( ((PrivateSpace)(pv.getSpace())).getMucName().equals(mucName) ){
				found = pv;
				break;
			}
		}
		return found;
	}

}
