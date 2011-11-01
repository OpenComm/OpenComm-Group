package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.PrivateSpaceIconView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

public class UserViewController {

	private UserView userView = null;
	public UserViewController(UserView uv){
		this.userView = uv;
	}
	
	
	/** If simply clicked a UserView without dragging it then 
	 * toggle between highlighted and unhighlighted
	 */
	public void handleSimpleClick(){
		userView.toggleSelected();
	}
	
	/** If double clicked on a UserView, then...
	 *  1) First check if you are the moderator of this space
	 *  2) If you are the moderator, then give control to SpaceController 
	 *  (is this right?) so that it may kick out this person
	 *  
	 *  Make sure this icon is not highlighted
	 */
	public void doubleClick(SpaceView space){
		// TODO NORA
	}
	
	/** If dragged, then change the icon's position. 
	 * 
	 * @param x - position of icon center 
	 * @param y - position of icon center
	 */
	public void handleDragging(int x, int y){
		userView.setXY(x,y);
	}
	
	/** After dragging an icon and lifting up, then place then
	 * set the icon's position. If dragged in an inappropriate place,
	 * then return to original position.
	 * 
	 * @param x - start position of icon center before dragging
	 * @param y - start position of icon center before dragging
	 * @param newX - placed position of icon center after dragging
	 * @param newY - placed position of icon center after dragging
	 */
	public void handleFinishedDragging(int startX, int startY, int newX, int newY){
		userView.setXY(newX, newY);
		// TODO NORA - check to make sure within bounds
	}
	
	/** If dragged on top of a PrivateSpaceIconView, then...
	 * 1) Set the position of the icon back to its initial position before dragging
	 * 2) Give this task over to SpaceController (or whoever) so that it may add this 
	 * person to this space that the PrivateSpaceIcon represents
	 *  
	 * @param PSIcon - the PrivateSpaceIconView that the UserView was dropped over
	 * @param startX - the start position of UserView center before dragging
	 * @param startY - the start position of UserView center before dragging
	 */
	public void handleDroppedOntoPSIcon(PrivateSpaceIconView PSIcon, int startX, int startY){
		userView.setXY(startX, startY);
		// TODO NETWORK - task (2), not sure which class will do this
	}
}
