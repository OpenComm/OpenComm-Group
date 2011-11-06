package edu.cornell.opencomm.controller;

import android.content.Context;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

public class SpaceViewController {

	SpaceView spaceView = null;
	public SpaceViewController(SpaceView spaceView){
		this.spaceView = spaceView;
	}
	
	/** Creates a UserView representing a user and adds it to 
	 * the SpaceView
	 * @param user - the new user object  
	 */
	public void addIconToSpaceView(User user){
		UserView newUserView = new UserView(spaceView.getContext(), user, user.getImage());
		spaceView.getAllIcons().add(newUserView);
		spaceView.invalidate();
	}
	
	
	public void changeSpace(Space space){
		
	}
}
