package edu.cornell.opencomm.controller;

import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

public class SpaceViewController {
	public long startTime=-1;
	
	SpaceView spaceView = null;
	public SpaceViewController(SpaceView spaceView){
		this.spaceView = spaceView;
	}
	
	/** Creates a UserView representing a user and adds it to 
	 * the SpaceView
	 * @param user - the new user object  
	 */
	public void addIconToSpaceView(User user){
		UserView newUserView = new UserView(spaceView.getContext(), user, user.getImage(), spaceView.getSpace());
		spaceView.getAllIcons().add(newUserView);
		spaceView.invalidate();
	}
	
	
	public void changeSpace(Space space){
		
	}
	
	public void handleClickDown(long time){
		Log.v("svcont", "clickdoewn");
		startTime = time;
	}
	
	public boolean handleClickUp(long time){
		Log.v("svcont", "clickup");
		if((time-startTime)>Values.pressAndHold)
			return true;
		return false;
	}
}
