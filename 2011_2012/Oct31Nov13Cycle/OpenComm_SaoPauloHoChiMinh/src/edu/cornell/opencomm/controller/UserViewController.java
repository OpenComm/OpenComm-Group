package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;
/**
 *  Class to handle user's touch input on the UserView. 
 *  Only modifies the UserView class. 
 * 
 * @author Nora 11/4 
 * 
 */
public class UserViewController {
	/** The time that the touching down event occured */
	long startTime=-1;
	/** The position of the icon on clickdown */
	int initialX, initialY; 
	/** True if the icon has been dragged 
	 * (as opposed to a simple click up and down with no movement) */
	boolean dragged;
	/** The userView object that this controller responds to */
	private UserView userView = null;
	
	/** Constructor: a UserViewController oject */
	public UserViewController(UserView uv){
		this.userView = uv;
	}
	
	/** When a user presses down on a UserView,
	 * save the initial icon position and time of event
	 * @param clickX - initial position of icon 
	 * @param clickY - initial position of icon
	 * @param time - time that the clicking down occured
	 */
	public void handleClickDown(int clickX, int clickY, long time){
		startTime = time;
		dragged = false;
		this.initialX = clickX;
		this.initialY = clickY;
	}
	
	/** If moved, then change the icon's position. 
	 * 
	 * @param clickX - position of finger click
	 * @param clickY - position of finger click
	 */
	public void handleMoved(int clickX, int clickY){
		dragged = true;
		int newX = clickX - (userView.getImage().getWidth() / 2);
		int newY = clickY - (userView.getImage().getWidth() / 2);
		userView.setXY(newX,newY);
	}
	
	/** After a click up on an icon. Return true if the person long pressed.
	 * Handle these 3 cases:
	 * 1) Simple click (no dragging) - do nothing for now, return false
	 * 2) Long press (no dragging) - return true
	 * 3) Click-and-drag - Change icon's position, change to initial
	 * position (when clicked down) if icon was dragged into the bottom bar,
	 * return false
	 * 
	 * @param clickX - placed position of icon center before dragging
	 * @param clickY - placed position of icon center before dragging
	 * @param time - time the touching up action occured
	 */
	public boolean handleClickUp(int clickX, int clickY, long time){
		int newX, newY;
		if(!dragged){
			if((time - startTime)>Values.pressAndHold){
				// Long press functionality here
				return true;
			}
			else
				// Simple click functionality here
				;
			return false;
		}
		else{
			// Click-and-drag functionality here
			
			// 1) If dragged into the bottom bar
			if(clickY>(Values.spaceViewH-Values.userIconH/2))
				userView.setXY(initialX, initialY);
			// 2) If dragged within the spaceview
			else{
				newX = clickX - (userView.getImage().getWidth() / 2);
				newY = clickY - (userView.getImage().getWidth() / 2);
				userView.setXY(newX, newY);
			}
		}
		return false;	
	}
} 
