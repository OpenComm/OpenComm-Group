package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.R;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;
/**
 *  Class to handle user's touch input on the UserView. 
 *  Only modifies the UserVIew class. 
 * 
 * @author Nora 11/4 
 * 
 */
public class UserViewController {
	long startTime=-1, endTime=-1;
	boolean pressAndHold=false;
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
	 * save the initial icon position 
	 * @param clickX - initial position of icon 
	 * @param clickY - initial position of icon
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
		Log.v("UserViewController", "handleMOved");
		dragged = true;
		int newX = clickX - (userView.getImage().getWidth() / 2);
		int newY = clickY - (userView.getImage().getWidth() / 2);
		userView.setXY(newX,newY);
	}
	
	/** After a click up on an icon. If was a simple click then
	 * toggle the icon's highlite. If had dragged the icon
	 * before lifting up, then change the icon's position. 
	 * If dragged in an inappropriate place,
	 * then return to original position.
	 * 
	 * @param clickX - start position of icon center before dragging
	 * @param clickY - start position of icon center before dragging
	 * @param newX - placed position of icon center after dragging
	 * @param newY - placed position of icon center after dragging
	 */
	public boolean handleClickUp(int clickX, int clickY, long time){
		int newX, newY;
		if(!dragged){
			dragged = false;
			if((time - startTime)>Values.pressAndHold){
				return true;
			}
			return false;
		}
		else{
			if(clickY>(Values.spaceViewH-Values.userIconH/2))
				userView.setXY(initialX, initialY);
			else{
				newX = clickX - (userView.getImage().getWidth() / 2);
				newY = clickY - (userView.getImage().getWidth() / 2);
				userView.setXY(newX, newY);
			}
		}
		return false;
		
	}
	
	/*public void handleLongPress(int clickX, int clickY){
		if(dragged)
			handleClickUp(clickX, clickY);
		else
			UserIconMenuController.showIconMenu();
	}*/
} 
