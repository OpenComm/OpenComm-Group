package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.model.User;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.UserView;

public class SpaceViewController {
	public long startTime=-1;
	
	static SpaceView spaceView = null;
	public SpaceViewController(SpaceView spaceView){
		this.spaceView = spaceView;
	}
	
	
	/**
	 * Change the spaceView to represent a different space
	 * @param newSpace - the new space to change the spaceView
	 * to
	 */
	public void changeSpace(Space newSpace){
		Log.v("SpaceViewController", "changeSpace() to " + newSpace);
		MainApplication.screen.setSpace(newSpace);
		MainApplication m = (MainApplication)spaceView.getContext();
		m.invalidateSpaceView();
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
