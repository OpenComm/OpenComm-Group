package edu.cornell.opencomm.controller;

import android.util.Log;
import edu.cornell.opencomm.Values;
import edu.cornell.opencomm.view.PrivateSpaceIconView;

/**
 * Controller for the icons at the bottom of the conference.
 */
public class PrivateSpaceIconController {
    boolean clickedOnce = false;

    //TODO: Sanity check this class w.r.t. this variable
    private PrivateSpaceIconView PSIcon = null;
    
    final static boolean D = Values.D;
    final static String TAG = "PrivateSpaceIconController";
    
    /**
     * Constructor
     */
    public PrivateSpaceIconController(PrivateSpaceIconView icon) {
        if (D) Log.d(TAG, "PrivateSpaceIconController");
        this.PSIcon = icon;
    } // end PrivateSpaceIconController

    /** 
     * Handle when a PrivateSpaceIcon is clicked up. Keep track of
     * if it has been clicked once or a second time.
     * @return - true if has clicked up and down once, which should open up
     * PopupPreview. False if clicked again. These should alternate
     */
    public boolean handleClickUp(){
        if (D) Log.d(TAG, "handleClickUp");
        clickedOnce = !clickedOnce;
        return clickedOnce;
    } // end handleClickUp

}
