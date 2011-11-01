package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.R;
import edu.cornell.opencomm.view.UserView;

public class UserViewController {

	private UserView userView = null;
	public UserViewController(UserView uv){
		this.userView = uv;
	}
	
	public void handleClickedDown(){
		
	}
	
	/** If simply clicked a UserView without dragging it then 
	 * toggle between highlighted and unhighlighted
	 */
	public void handleSimpleClick(){
		userView.toggleSelected();
	}
	
	/** If dragged, then change the icon's position */
	public void handleDragging(int x, int y){
		userView.setXY(x,y);
	}
	
	/** After dragging an icon and lifting up, then place then
	 * set the icon's position */
	public void handleFinishedDragging(int x, int y){
		userView.setXY(x, y);
	}
}
