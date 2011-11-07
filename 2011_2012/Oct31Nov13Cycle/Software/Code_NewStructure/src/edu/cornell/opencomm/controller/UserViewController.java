package edu.cornell.opencomm.controller;

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
	public void handleClickDown(int clickX, int clickY){
		this.initialX = clickX;
		this.initialY = clickY;
	}
	
	/** If double clicked on a UserView, then...
	 *  1) First check if you are the moderator of this space
	 *  2) If you are the moderator, then give control to SpaceController 
	 *  (is this right?) so that it may kick out this person
	 *  
	 *  Make sure this icon is not highlighted
	 */
/*	public void handlePressAndHold(){
		if(MainApplication.user_primary == MainApplication.screen.getSpace().getOwner())
			
	} */
	
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
	public void handleClickUp(int clickX, int clickY){
		int newX, newY;
		if(!dragged)
			userView.toggleSelected();
		else{
			if(clickY>Values.spaceViewH)
				userView.setXY(initialX, initialY);
			else{
				newX = clickX - (userView.getImage().getWidth() / 2);
				newY = clickY - (userView.getImage().getWidth() / 2);
				userView.setXY(newX, newY);
			}
		}
		dragged = false;
	}
}
