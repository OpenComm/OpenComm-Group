package edu.cornell.opencomm;

import java.util.LinkedList;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author noranq
 * 
 */
public class SpaceView extends LinearLayout {

	private Context context;
	private Space parent;
	// private PrivateSpace space;
	private LinkedList<PersonView> icons;

	static int w = 320, h = 430;
	static int mainScreenH = 340;
	private PersonView selectedIcon;
	LinkedList<PersonView> currPreview = null;

	// position of the icon before you moved it
	int initialX = 0, initialY = 0;

	// the Private Space that is being hovered over
	PrivateSpaceView hoveredPrivSpace = null;
	Object selected = null;

	int canDraw = 0; // nora experiment

	public SpaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public SpaceView(Context context, Space space) {
		super(context);
		this.context = context;
		// this.space = space;
		parent = space;
		// preview = new PreviewView(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public void setPreview(LinkedList<PersonView> psv) {
		currPreview = psv;
		invalidate();
	}

	/**
	 * Takes the people who are in this private space, and creates an icon
	 * (PersonView) for them for only this screen
	 */

	public void createIcons(Space space) {
		icons = new LinkedList<PersonView>();
		if (!space.getPeople().isEmpty()) {
			for (PersonView icon : space.getPeople()) {
				icons.add(icon);
			}
		}

	}

	public LinkedList<PersonView> getIcons() {
		return icons;
	}

	public void addPerson(Person p) {
		PersonView pv = new PersonView(context, p, (int) (Math.floor(w
				* Math.random())), (int) (Math.floor(mainScreenH
				* Math.random())), 55, 55);
		this.icons.add(pv);
	}

	protected void onDraw(Canvas canvas) {
		// if(canDraw>1)
		// canDraw = PrivateSpaceView.currentSpaces.size();
		if (canDraw == 2)
			// if(icons.size()>1)
			canvas.drawColor(getColor());

		if (currPreview != null) {
			PreviewView prev = new PreviewView(context);
			prev.draw(canvas, currPreview);
		}

		for (PersonView p : icons) {
			p.draw(canvas);
		}
	}

	private int getColor() {
		return R.color.main_background;
	}

	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int mouseX = (int) event.getX();
		int mouseY = (int) event.getY();

		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			selectedIcon = null;
			for (PersonView icon : icons) {
				if (icon.clickedInside(mouseX, mouseY)) {
					selectedIcon = icon;
					initialX = icon.getX();
					initialY = icon.getY();
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			// If a person icon is selected, then move the icon to the current
			// position
			if (selectedIcon != null) {
				selectedIcon.moved = true;
				selectedIcon.setX(mouseX - (selectedIcon.getW() / 2));
				selectedIcon.setY(mouseY - (selectedIcon.getH() / 2));

				// if icon is dragged over private space, then highlight that
				// private space icon
				if (hoveredPrivSpace == null) {
					for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
						if (p.contains(mouseX, mouseY)) {
							p.setHovered(true);
							hoveredPrivSpace = p;
						}
					}
				} else if (hoveredPrivSpace != null) {
					if (!hoveredPrivSpace.contains(mouseX, mouseY)) {
						hoveredPrivSpace.setHovered(false);
						hoveredPrivSpace = null;
					}
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			canDraw = PrivateSpaceView.currentSpaces.size();
			/*
			 * If you highlited an icon, then clicked on nothing on screen, it
			 * should unhighlite all the other icons
			 */
			if (selectedIcon == null && mouseY < mainScreenH) {

				for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
					if (p.isSelected)
						p.setSelected(false);

				}
			}
			if (selectedIcon != null) {
				// if you did not move the icon and just clicked it, then
				// highlite it
				if (!selectedIcon.moved) {
					selectedIcon.changeSelected();
				}
				selectedIcon.moved = false;

				for (PrivateSpaceView p : PrivateSpaceView.currentSpaces) {
					if (p.contains(mouseX, mouseY)) {

						(p.getSpace()).add(selectedIcon.getPerson());
						p.setSelected(false);
						// canDraw++;
					}
				}
				if (mouseY >= mainScreenH) {
					selectedIcon.setX(initialX);
					selectedIcon.setY(initialY);
				}
				selectedIcon = null;
			}
			break;
		}
		invalidate();
		return true;
	}

}
