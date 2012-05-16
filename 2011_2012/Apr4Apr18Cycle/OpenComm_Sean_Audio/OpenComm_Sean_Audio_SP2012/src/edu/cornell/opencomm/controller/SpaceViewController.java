package edu.cornell.opencomm.controller;

import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.audio.SoundSpatializer;
import edu.cornell.opencomm.model.Space;
import edu.cornell.opencomm.view.SpaceView;

public class SpaceViewController {
    public long startTime=-1;

    static SpaceView spaceView = null;
    public SpaceViewController(SpaceView spaceView){
        SpaceViewController.spaceView = spaceView;
    }


    /**
     * Change the spaceView to represent a different space
     * @param newSpace - the new space to change the spaceView
     * to
     */
    public void changeSpace(Space newSpace){
        MainApplication.screen.cancelLassoMode();
        Log.v("SpaceViewController", "changeSpace() from " + MainApplication.screen.getSpace() + " to " + newSpace);
        MainApplication.screen.getSpace().setScreenOn(false);
        // change audio
        if (MainApplication.screen.getSpace().isMainSpace()) {
        	// if new space is left ps
        	if (Space.allSpaces.size() >= 2) {
        		// left ps filled
        		Iterator<String> spaceItr = MainApplication.screen.getSpace().getAllParticipants().keySet().iterator();
        		while (spaceItr.hasNext()) {
        			MainApplication.screen.getSpace().getAllParticipants().get(spaceItr.next()).getJingle().getSoundSpatializer().putMainSpaceOutOfFocusForLeft();
        		}
        	}
        	// if new space is right ps
        	else if (Space.allSpaces.size() == 3) {
        		// right ps filled
        		Iterator<String> spaceItr = MainApplication.screen.getSpace().getAllParticipants().keySet().iterator();
        		while (spaceItr.hasNext()) {
        			MainApplication.screen.getSpace().getAllParticipants().get(spaceItr.next()).getJingle().getSoundSpatializer().putMainSpaceOutOfFocusForRight();
        		}
        	}
        }
        MainApplication.screen.getSpace().getSpaceController().getPSIV().invalidate();
        MainApplication.screen.setSpace(newSpace);
        newSpace.setScreenOn(true);
        // update new space's volume to 100%
        Iterator<String> spaceItr = newSpace.getAllParticipants().keySet().iterator();
        while (spaceItr.hasNext()) {
        	newSpace.getAllParticipants().get(spaceItr.next()).getJingle().getSoundSpatializer().putSpaceInFocus();
        }
        MainApplication m = (MainApplication)spaceView.getContext();
        m.invalidateSpaceView();
        if(!MainApplication.screen.getSpace().getEntered()){
            Space s = MainApplication.screen.getSpace();
            s.setEntered(true);
            /* If you are the owner and it is your first
             * time entering the space, then open the
             * admin tip popup
             */
            if(s.getOwner()==MainApplication.userPrimary){
                LayoutInflater inflater = (LayoutInflater) m.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //AdminTipView adminTipView = new AdminTipView(inflater);
                //adminTipView.launch();
            }
            /* If you are not the owner of the space, and it
             * is your first time entering the space, then open
             * the tip popup
             */
            else{
                LayoutInflater inflater = (LayoutInflater) m
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //TipView tipView = new TipView(inflater);
                //tipView.launch();
            }
        }
        // sound alteration
        //newSpace;
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

    public void handleLongClick(){
        EmptySpaceMenuController.showFreeSpaceMenu();
    }
}
