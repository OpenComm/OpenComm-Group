package edu.cornell.opencomm.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.AdminTipView;
import edu.cornell.opencomm.view.SpaceView;
import edu.cornell.opencomm.view.TipView;

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
		Log.v("SpaceViewController", "changeSpace() from " + MainApplication.screen.getSpace() + " to " + newSpace);
		MainApplication.screen.setSpace(newSpace);
		MainApplication m = (MainApplication)spaceView.getContext();
		m.invalidateSpaceView();
		if(!MainApplication.screen.getSpace().getEntered()){
			Space s = MainApplication.screen.getSpace();
			s.setEntered(true);
			/* If you are the owner and it is your first
			 * time entering the space, then open the 
			 * admin tip popup
			 */
			if(s.getOwner()==MainApplication.user_primary){
				LayoutInflater inflater = (LayoutInflater) m
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				AdminTipView adminTipView = new AdminTipView(inflater);
				adminTipView.launch();
			}
			/* If you are not the owner of the space, and it
			 * is your first time entering the space, then open
			 * the tip popup
			 */
			else{
				LayoutInflater inflater = (LayoutInflater) m
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				TipView tipView = new TipView(inflater);
				tipView.launch();
			}
		}
		Log.v("SpaceViewController", "changeSpace() successfuly to " + MainApplication.screen.getSpace());
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
