package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.UserView;

/**
 * Class to handle user's touch input on the UserView. Only modifies the
 * UserView class.
 * 
 * @author Nora 11/4
 * 
 */
public class UserViewController {
	/** The position of the icon on clickdown */
	int initialX, initialY;
	/** True if the icon has been dragged (as opposed to a simple click up and
	 * down with no movement)*/
	boolean dragged;
	/** The userView object that this controller responds to */
	private UserView userView = null;

	/** Constructor: a UserViewController oject */
	public UserViewController(UserView uv) {
		this.userView = uv;
	}

	/**
	 * When a user presses down on a UserView, 
	 * save the initial icon position
	 * 
	 * @param clickX - initial position of icon
	 * @param clickY - initial position of icon
	 */
	public void handleClickDown(int clickX, int clickY) {
		dragged = false;
		this.initialX = clickX;
		this.initialY = clickY;
	}

	/**
	 * If moved, then change the icon's position.
	 * 
	 * @param clickX - position of finger click
	 * @param clickY - position of finger click
	 */
	public void handleMoved(int clickX, int clickY) {
		dragged = true;
		int newX = clickX - (userView.getImage().getWidth() / 2);
		int newY = clickY - (userView.getImage().getWidth() / 2);
		userView.setXY(newX, newY);
	}

	/**
	 * After a click up on an icon. Handle these 2 cases: 
	 * 1) If the icon is clicked up over the bottom bar, then
	 * return the icon to its original position before moving it
	 * 2) If icon is clicked up over the spaceview, then change the 
	 * icon's position
	 * 
	 * @param clickX - placed position of icon center before dragging
	 * @param clickY - placed position of icon center before dragging
	 */
	public void handleClickUp(int clickX, int clickY) {
		int newX, newY;
		// 1) 
		if (clickY > (Values.spaceViewH - Values.userIconH / 2))
			userView.setXY(initialX, initialY);
		// 2) 
		else {
			newX = clickX - (userView.getImage().getWidth() / 2);
			newY = clickY - (userView.getImage().getWidth() / 2);
			userView.setXY(newX, newY);
		}
	}

	/**
	 * When a user press-and-holds a userview, then bring up the usericon menu
	 * from UserIconMenuController. Return true if is a successful
	 * press-and-hold, but false in situations where the icon has already been
	 * dragged.
	 */
	public boolean handleLongPress() {
		if (!dragged) {
			UserIconMenuController.showIconMenu(userView);
			return true;
		}
		return false;
	}
}
